package me.spighetto.mypoop.version;

import me.spighetto.mypoopversionsinterfaces.IPoop;
import net.md_5.bungee.api.ChatColor;
import net.md_5.bungee.api.ChatMessageType;
import net.md_5.bungee.api.chat.BaseComponent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.Sound;
import org.bukkit.entity.Item;
import org.bukkit.entity.Player;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.lang.reflect.Method;
import java.time.Duration;
import java.util.Optional;
import java.util.logging.Level;
import java.util.logging.Logger;

/**
 * Detects runtime capabilities exposed by the Bukkit/Paper API and provides
 * safe fallbacks so the plugin can run across 1.19.4, 1.20.6 and 1.21.x without NMS.
 */
public final class VersionCapabilities {

    private static final int DEFAULT_TITLE_FADE_IN = 10;
    private static final int DEFAULT_TITLE_STAY = 40;
    private static final int DEFAULT_TITLE_FADE_OUT = 10;

    private final Logger logger;

    // Adventure (Paper 1.19+)
    private final Class<?> adventureComponentClass;
    private final Class<?> adventureTitleClass;
    private final Method adventureComponentTextMethod;
    private final Method adventureLegacySerializerFactory;
    private final Method adventureLegacyDeserializeMethod;
    private final Object adventureLegacySerializerInstance;
    private final Method adventureTitleFactoryTwoArgs;
    private final Method adventureTitleFactoryThreeArgs;
    private final Class<?> adventureTitleTimesClass;
    private final Method adventureTitleTimesFactory;
    private final Method playerShowTitleMethod;
    private final Method playerSendActionBarComponentMethod;

    // Legacy string-based APIs
    private final Method playerSendTitleMethod;
    private final Method playerSendTitleSimpleMethod;

    // Spigot bridge for action bar
    private final Class<?> playerSpigotClass;
    private final Method spigotSendMessageMethod;

    private final boolean supportsCustomModelData;

    public VersionCapabilities(Logger logger) {
        this.logger = logger;

        this.adventureComponentClass = loadClass("net.kyori.adventure.text.Component");
        this.adventureTitleClass = loadClass("net.kyori.adventure.title.Title");
        this.adventureTitleTimesClass = loadClass("net.kyori.adventure.title.Title$Times");

        this.adventureComponentTextMethod = findStatic(adventureComponentClass, "text", String.class);

        Class<?> serializerClass = loadClass("net.kyori.adventure.text.serializer.legacy.LegacyComponentSerializer");
        this.adventureLegacySerializerFactory = findStatic(serializerClass, "legacyAmpersand");
        this.adventureLegacyDeserializeMethod = serializerClass != null
            ? findMethod(serializerClass, "deserialize", String.class)
            : null;
        this.adventureLegacySerializerInstance = createLegacySerializerInstance();
        this.adventureTitleFactoryTwoArgs = findStatic(adventureTitleClass, "title", adventureComponentClass, adventureComponentClass);
        this.adventureTitleFactoryThreeArgs = findStatic(adventureTitleClass, "title", adventureComponentClass, adventureComponentClass, adventureTitleTimesClass);
        this.adventureTitleTimesFactory = adventureTitleTimesClass != null
            ? findStatic(adventureTitleTimesClass, "of", Duration.class, Duration.class, Duration.class)
            : null;
        this.playerShowTitleMethod = findMethod(Player.class, "showTitle", adventureTitleClass);
        this.playerSendActionBarComponentMethod = findMethod(Player.class, "sendActionBar", adventureComponentClass);

        this.playerSendTitleMethod = findMethod(Player.class, "sendTitle", String.class, String.class, int.class, int.class, int.class);
        this.playerSendTitleSimpleMethod = findMethod(Player.class, "sendTitle", String.class, String.class);

        this.playerSpigotClass = loadClass("org.bukkit.entity.Player$Spigot");
        this.spigotSendMessageMethod = findMethod(playerSpigotClass, "sendMessage", ChatMessageType.class, BaseComponent[].class);

        this.supportsCustomModelData = detectCustomModelData();
    }

    public boolean supportsTitles() {
        return playerShowTitleMethod != null || playerSendTitleMethod != null || playerSendTitleSimpleMethod != null;
    }

    public boolean supportsActionBar() {
        return playerSendActionBarComponentMethod != null || spigotSendMessageMethod != null;
    }

    public boolean sendTitle(Player player, String title) {
        return sendTitleInternal(player, title, "");
    }

    public boolean sendSubtitle(Player player, String subtitle) {
        return sendTitleInternal(player, "", subtitle);
    }

    public boolean sendActionBar(Player player, String message) {
        if (player == null || message == null) {
            return false;
        }

        // Adventure-first
        if (playerSendActionBarComponentMethod != null) {
            Object component = toComponent(message);
            if (component != null) {
                try {
                    playerSendActionBarComponentMethod.invoke(player, component);
                    return true;
                } catch (Exception ex) {
                    logger.log(Level.FINE, "Failed to send action bar via adventure API", ex);
                }
            }
        }

        // Spigot legacy bridge
        if (spigotSendMessageMethod != null) {
            try {
                Object spigot = player.spigot();
                BaseComponent component = new TextComponent(colorize(message));
                Object[] args = new Object[] { ChatMessageType.ACTION_BAR, new BaseComponent[] { component } };
                spigotSendMessageMethod.invoke(spigot, args);
                return true;
            } catch (Exception ex) {
                logger.log(Level.FINE, "Failed to send action bar via Spigot bridge", ex);
            }
        }

        return false;
    }

    public IPoop spawnPoop(Player player) {
        if (player == null) {
            return null;
        }

        Location location = player.getLocation();
        ItemStack stack = new ItemStack(Material.COCOA_BEANS);
        ItemMeta meta = stack.getItemMeta();
        if (meta != null) {
            if (supportsCustomModelData) {
                try {
                    meta.setCustomModelData(1);
                } catch (NoSuchMethodError ignored) {
                    // Should not happen thanks to detection, keep silent fallback.
                }
            }
            stack.setItemMeta(meta);
        }

        Item item = player.getWorld().dropItem(location, stack);
        item.setPickupDelay(Integer.MAX_VALUE);
        playDropSound(player);
        return new BukkitPoop(item);
    }

    public String colorize(String input) {
        if (input == null) {
            return "";
        }
        return ChatColor.translateAlternateColorCodes('&', input);
    }

    private boolean sendTitleInternal(Player player, String title, String subtitle) {
        if (player == null) {
            return false;
        }

        // Adventure showTitle exposes best UX on modern Paper
        if (playerShowTitleMethod != null) {
            Object titleComponent = toComponent(title == null ? "" : title);
            Object subtitleComponent = toComponent(subtitle == null ? "" : subtitle);
            Object titleObject = buildAdventureTitle(titleComponent, subtitleComponent);
            if (titleObject != null) {
                try {
                    playerShowTitleMethod.invoke(player, titleObject);
                    return true;
                } catch (Exception ex) {
                    logger.log(Level.FINE, "Failed to send title via adventure API", ex);
                }
            }
        }

        // Legacy Bukkit API with timings
        if (playerSendTitleMethod != null) {
            try {
                playerSendTitleMethod.invoke(
                    player,
                    colorize(title),
                    colorize(subtitle),
                    DEFAULT_TITLE_FADE_IN,
                    DEFAULT_TITLE_STAY,
                    DEFAULT_TITLE_FADE_OUT
                );
                return true;
            } catch (Exception ex) {
                logger.log(Level.FINE, "Failed to send title via legacy API (with timings)", ex);
            }
        }

        if (playerSendTitleSimpleMethod != null) {
            try {
                playerSendTitleSimpleMethod.invoke(player, colorize(title), colorize(subtitle));
                return true;
            } catch (Exception ex) {
                logger.log(Level.FINE, "Failed to send title via legacy API", ex);
            }
        }

        return false;
    }

    private Object buildAdventureTitle(Object titleComponent, Object subtitleComponent) {
        if (titleComponent == null && subtitleComponent == null) {
            return null;
        }

        Object safeTitle = Optional.ofNullable(titleComponent).orElseGet(() -> toComponent(""));
        Object safeSubtitle = Optional.ofNullable(subtitleComponent).orElseGet(() -> toComponent(""));

        if (adventureTitleFactoryTwoArgs != null) {
            try {
                return adventureTitleFactoryTwoArgs.invoke(null, safeTitle, safeSubtitle);
            } catch (Exception ex) {
                logger.log(Level.FINE, "Failed to build adventure title (2 args)", ex);
            }
        }

        if (adventureTitleFactoryThreeArgs != null && adventureTitleTimesFactory != null) {
            try {
                Object times = adventureTitleTimesFactory.invoke(
                    null,
                    Duration.ofMillis(DEFAULT_TITLE_FADE_IN * 50L),
                    Duration.ofMillis(DEFAULT_TITLE_STAY * 50L),
                    Duration.ofMillis(DEFAULT_TITLE_FADE_OUT * 50L)
                );
                return adventureTitleFactoryThreeArgs.invoke(null, safeTitle, safeSubtitle, times);
            } catch (Exception ex) {
                logger.log(Level.FINE, "Failed to build adventure title (3 args)", ex);
            }
        }

        return null;
    }

    private Object toComponent(String message) {
        if (message == null) {
            message = "";
        }

        if (adventureLegacySerializerInstance != null && adventureLegacyDeserializeMethod != null) {
            try {
                return adventureLegacyDeserializeMethod.invoke(adventureLegacySerializerInstance, message);
            } catch (Exception ex) {
                logger.log(Level.FINEST, "Failed to deserialize legacy component", ex);
            }
        }

        if (adventureComponentTextMethod != null) {
            try {
                return adventureComponentTextMethod.invoke(null, message);
            } catch (Exception ex) {
                logger.log(Level.FINEST, "Failed to build plain adventure component", ex);
            }
        }

        return null;
    }

    private void playDropSound(Player player) {
        try {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SLIME_SQUISH_SMALL, 1.0f, 1.2f);
        } catch (NoSuchFieldError ignored) {
            player.getWorld().playSound(player.getLocation(), Sound.ENTITY_SLIME_HURT, 1.0f, 1.5f);
        }
    }

    private Object createLegacySerializerInstance() {
        if (adventureLegacySerializerFactory == null) {
            return null;
        }
        try {
            return adventureLegacySerializerFactory.invoke(null);
        } catch (Exception ex) {
            logger.log(Level.FINEST, "Failed to create legacy serializer", ex);
            return null;
        }
    }

    private boolean detectCustomModelData() {
        try {
            ItemMeta.class.getMethod("setCustomModelData", Integer.class);
            return true;
        } catch (NoSuchMethodException ignored) {
            return false;
        }
    }

    private Class<?> loadClass(String className) {
        if (className == null) {
            return null;
        }
        try {
            return Class.forName(className);
        } catch (ClassNotFoundException ex) {
            return null;
        }
    }

    private Method findMethod(Class<?> target, String name, Class<?>... parameterTypes) {
        if (target == null || name == null) {
            return null;
        }
        try {
            Method method = target.getMethod(name, parameterTypes);
            method.setAccessible(true);
            return method;
        } catch (NoSuchMethodException ignored) {
            return null;
        }
    }

    private Method findStatic(Class<?> target, String name, Class<?>... parameterTypes) {
        Method method = findMethod(target, name, parameterTypes);
        if (method != null && (method.getModifiers() & java.lang.reflect.Modifier.STATIC) == 0) {
            return null;
        }
        return method;
    }
}
