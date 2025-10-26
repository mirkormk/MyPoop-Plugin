package me.spighetto.mypoop.version;

import me.spighetto.mypoopversionsinterfaces.IPoop;
import net.md_5.bungee.api.ChatColor;
import org.bukkit.entity.Item;

/**
 * Default Bukkit-based implementation of {@link IPoop} backed by an {@link Item} entity.
 * Keeps API surface narrow so it can be used across multiple Minecraft versions without NMS.
 */
final class BukkitPoop implements IPoop {

    private final Item poopItem;

    BukkitPoop(Item poopItem) {
        this.poopItem = poopItem;
    }

    @Override
    public void setName(String name) {
        applyCustomName(name, null);
    }

    @Override
    public void setName(String name, String colorCode) {
        applyCustomName(name, colorCode);
    }

    @Override
    public Item getPoopItem() {
        return poopItem;
    }

    @Override
    public void delete() {
        poopItem.remove();
    }

    private void applyCustomName(String baseName, String colorCode) {
        String raw = baseName == null ? "" : baseName;
        if (colorCode != null && !colorCode.isEmpty()) {
            raw = colorCode + raw;
        }
        String colored = ChatColor.translateAlternateColorCodes('&', raw);
        poopItem.setCustomNameVisible(true);
        poopItem.setCustomName(colored);
    }
}
