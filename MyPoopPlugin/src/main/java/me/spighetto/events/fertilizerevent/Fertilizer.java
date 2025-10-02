package me.spighetto.events.fertilizerevent;

import me.spighetto.mypoop.MyPoop;
import org.bukkit.*;
import org.bukkit.block.Block;
import org.bukkit.block.data.Ageable;
import org.bukkit.block.data.type.Sapling;
import org.bukkit.command.ConsoleCommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

public class Fertilizer {

    private final MyPoop plugin;
    private final Player player;
    private final Location playerLocation;

    List<CropsFound> cropsList = new ArrayList<>();
    List<SaplingsFound> saplingsList = new ArrayList<>();

    static class CropsFound implements Comparable<CropsFound>{

        public Block crop;
        public double dist;

        public CropsFound(Block block, double dist) {
            crop = block;
            this.dist = dist;
        }

        @Override
        public int compareTo(CropsFound o) {
            return Double.compare(this.dist, o.dist);
        }
    }

    static class SaplingsFound implements Comparable<SaplingsFound>{

        public Block sapling;
        public double dist;

        public SaplingsFound(Block block, double dist) {
            sapling = block;
            this.dist = dist;
        }

        @Override
        public int compareTo(SaplingsFound o) {
            return Double.compare(this.dist, o.dist);
        }
    }

    public Fertilizer(MyPoop plugin, Player player) {
        this.plugin = plugin;

        this.player = player;
        playerLocation = player.getLocation();

        if(plugin.getPoopConfig().getRadius() >= 0)
            tryFertilize();
    }

    public void tryFertilize() {
        getNearbyCrops();

        if(!cropsList.isEmpty()) {
            if (plugin.getPoopConfig().getAllCropsNearby()) {
                int x = 0;
                do {
                    fertilizeCrops(x);
                    x++;
                } while (x < cropsList.size());
            } else {
                if (plugin.getPoopConfig().getRandomGrow()) {
                    int randomIndex = (int) (Math.random() * cropsList.size());
                    fertilizeCrops(randomIndex);
                } else {
                    fertilizeCrops(0);
                }
            }
        }

        if(!saplingsList.isEmpty()) {
            if (plugin.getPoopConfig().getAllCropsNearby()) {
                int x = 0;
                do {
                    fertilizeSaplings(x);
                    x++;
                } while (x < saplingsList.size());
            } else {
                if (plugin.getPoopConfig().getRandomGrow()) {
                    int randomIndex = (int) (Math.random() * saplingsList.size());
                    fertilizeSaplings(randomIndex);
                } else {
                    fertilizeSaplings(0);
                }
            }
        }
    }

    private void getNearbyCrops() {
        cropsList.clear();
        saplingsList.clear();

        //double plY = Math.abs(playerLocation.getY() - Math.floor(playerLocation.getY())) > 0.5 ? Math.round(playerLocation.getY()) : Math.floor(playerLocation.getY());
        //double plX = Math.abs(playerLocation.getX() - Math.floor(playerLocation.getX())) > 0.5 ? Math.round(playerLocation.getX()) : Math.floor(playerLocation.getX());
        //double plZ = Math.abs(playerLocation.getZ() - Math.floor(playerLocation.getZ())) > 0.5 ? Math.round(playerLocation.getZ()) : Math.floor(playerLocation.getZ());

        Bukkit.getScheduler().scheduleSyncDelayedTask(plugin, () -> {

            double plY = playerLocation.getY();
            double plX = playerLocation.getX();
            double plZ = playerLocation.getZ();
            long y = Math.round(plY);

            for(int x = (int) (plX - plugin.getPoopConfig().getRadius()); x <= plX + plugin.getPoopConfig().getRadius(); x++) {
                for(int z = (int) (plZ - plugin.getPoopConfig().getRadius()); z <= plZ + plugin.getPoopConfig().getRadius(); z++) {
                    Location l = new Location(player.getLocation().getWorld(), x, y, z);
                    l.add(x > 0 ? 0.5 : -0.5, 0.0, z > 0 ? 0.5 : -0.5);
                    double dist = (plX - l.getX()) * (plX - l.getX()) + (plZ - l.getZ()) * (plZ - l.getZ());

                    if (l.getBlock().getBlockData() instanceof Ageable) {
                        if (((Ageable) l.getBlock().getBlockData()).getAge() < ((Ageable) l.getBlock().getBlockData()).getMaximumAge()) {
                            cropsList.add(new CropsFound(l.getBlock(), dist));
                        }
                    } else if (l.getBlock().getBlockData() instanceof Sapling) {
                        saplingsList.add(new SaplingsFound(l.getBlock(), dist));
                    } else {
                        l.getWorld().spawnParticle(Particle.BLOCK_DUST, l.getX() + 0.5, l.getY() + 1, l.getZ() + 0.5, 50, 0.2, 0.2, 0.2, l.getBlock().getBlockData());
                    }
                }
            }
        }, 2);

        Collections.sort(cropsList);
        printCrops();
    }

    private void printCrops() {
        cropsList.forEach(cropped -> {
            player.sendMessage(cropped.crop.getType() + " -> " + cropped.crop.getX() + " - " + cropped.crop.getZ() + " | " + cropped.dist + "\n");
        });

        saplingsList.forEach(saplingg -> {
            player.sendMessage(saplingg.sapling.getType() + " -> " + saplingg.sapling.getX() + " - " + saplingg.sapling.getZ() + " | " + saplingg.dist + "\n");
        });
    }

    private void fertilizeCrops(Integer index) {
        Ageable crop = (Ageable) cropsList.get(index).crop.getBlockData();
        crop.setAge(crop.getAge() + 1);
        cropsList.get(index).crop.setBlockData(crop);
        player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, cropsList.get(index).crop.getX() + 0.5, cropsList.get(index).crop.getY() + 0.5, cropsList.get(index).crop.getZ() + 0.5, 10, 0.2,0.2,0.2, 1);
    }

    private void fertilizeSaplings(Integer index) {
        int ran = (int) Math.round(Math.random()*2);
        player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, saplingsList.get(index).sapling.getX() + 0.5, saplingsList.get(index).sapling.getY() + 0.5, saplingsList.get(index).sapling.getZ() + 0.5, 10, 0.2,0.2,0.2, 1);

        if(/*ran == 2*/true) {
            Block sapling = saplingsList.get(index).sapling;
            Location sapLoc = sapling.getLocation();
            Material sapType = sapling.getType();
            saplingsList.get(index).sapling.setType(Material.AIR);
            //player.sendMessage(sapType.toString());
            try{
                if(saplingToTree(sapType, sapLoc) != null) {
                    if(!sapling.getLocation().getWorld().generateTree(sapling.getLocation(), saplingToTree(sapType, sapLoc))) {
                        saplingsList.get(index).sapling.setType(sapType);
                    }
                }
            } catch (Exception e) {
                ConsoleCommandSender c = Bukkit.getConsoleSender();
                c.sendMessage(e.toString());
            }
        }

        /*
        Sapling saplings = (Sapling) saplingsList.get(index).sapling.getBlockData();
        Block block = saplingsList.get(index).sapling;
        if(saplings.getStage() <= saplings.getMaximumStage()) {
            Bukkit.getServer().getScheduler().runTaskLater(MyPoop.plugin, () -> {
                saplings.setStage(saplings.getMaximumStage());
            }, 100L);

            block.setBlockData(saplings);
            player.getWorld().spawnParticle(Particle.VILLAGER_HAPPY, saplingsList.get(index).sapling.getX() + 0.5, saplingsList.get(index).sapling.getY() + 0.5, saplingsList.get(index).sapling.getZ() + 0.5, 10, 0.2,0.2,0.2, 1);
        }
        player.sendMessage(String.valueOf(saplings.getStage()));

         */

    }

    private TreeType saplingToTree(Material sapType, Location sapLoc){
        switch (sapType) {
            case SPRUCE_SAPLING:
                return TreeType.SWAMP;
            case BIRCH_SAPLING:
                return TreeType.BIRCH;
            case DARK_OAK_SAPLING:
                if(checkIfFourSaplings(sapType, sapLoc))
                    return TreeType.DARK_OAK;
                else
                    return null;
            case JUNGLE_SAPLING:
                if(checkIfFourSaplings(sapType, sapLoc))
                    return TreeType.JUNGLE;
                else
                    return TreeType.SMALL_JUNGLE;
            case ACACIA_SAPLING:
                return TreeType.ACACIA;
            default:
                return TreeType.TREE;
        }
    }

    private Boolean checkIfFourSaplings(Material sapType, Location sapLoc) {
        int counter = 0;

        for(int x = (int) (sapLoc.getX() - 1); x <= sapLoc.getX() + 1; x++) {
            for(int z = (int) (sapLoc.getZ() - 1); z <= sapLoc.getZ() + 1; z++) {

                Block b = new Location(player.getLocation().getWorld(), x, sapLoc.getY(), z).getBlock();

                if (b.getType().equals(sapType)) {
                    counter++;
                }
            }
        }

        return counter >= 4;
    }
}
