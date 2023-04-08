package me.spighetto.mypoopversionsinterfaces;

import org.bukkit.entity.Item;

public interface IPoop {

    void setName(String name);
    void setName(String name, String colorCode);
    Item getPoopItem();
    void delete();
}
