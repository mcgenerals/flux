package com.baguars.flux.inventory;

import com.baguars.flux.Flux;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class CategoryMenu {

    public InventoryGUI i;

    public CategoryMenu(){
        InventoryGUI i = new InventoryGUI();
        i.create("Flux[Category]",3,9);
        //Glass
        ItemStack grayGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 7);
        ItemMeta grayGlassMeta = grayGlass.getItemMeta();
        grayGlassMeta.setDisplayName("Gray Glass");
        grayGlass.setItemMeta(grayGlassMeta);
        ItemStack yellowGlass = new ItemStack(Material.STAINED_GLASS_PANE, 1, (short) 4);
        ItemMeta yellowGlassMeta = yellowGlass.getItemMeta();
        yellowGlassMeta.setDisplayName("Yellow Glass");
        yellowGlass.setItemMeta(yellowGlassMeta);
        //Done

        //Line1
        i.addItem(grayGlass,0);
        i.addItem(yellowGlass, 1);
        i.addItem(grayGlass, 2);
        i.addItem(yellowGlass, 3);
        i.addItem(grayGlass, 4);
        i.addItem(yellowGlass, 5);
        i.addItem(grayGlass, 6);
        i.addItem(yellowGlass, 7);
        i.addItem(grayGlass, 8);
        //Line2
        i.addItem(yellowGlass, 9);
        i.addItem(grayGlass, 10);
        i.addItem("§aCombats",Material.IRON_SWORD, 11);
        i.addItem(grayGlass, 12);
        i.addItem("§aMovements",Material.GOLD_BOOTS, 13);
        i.addItem(grayGlass, 14);
        i.addItem("§aPlayers",Material.COMPASS, 15);
        i.addItem(grayGlass, 16);
        i.addItem(yellowGlass, 17);
        //Line3
        i.addItem(grayGlass,18);
        i.addItem(yellowGlass, 19);
        i.addItem(grayGlass, 20);
        i.addItem(yellowGlass, 21);
        i.addItem("§6§lBack",Material.IRON_DOOR, 22);
        i.addItem(yellowGlass, 23);
        i.addItem(grayGlass, 24);
        i.addItem(yellowGlass, 25);
        i.addItem(grayGlass, 26);

        this.i = i;
    }

}
