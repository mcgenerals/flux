package com.baguars.flux.inventory;

import com.baguars.flux.Flux;
import com.baguars.flux.check.Check;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class MainMenu {

    public InventoryGUI i;

    public MainMenu(){
        InventoryGUI i = new InventoryGUI();
        i.create("Flux[MainMenu]",3,9);
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
        ArrayList<String> desc = new ArrayList<>();
        desc.add("");
        desc.add("§9Anti-Cheat Status");
        desc.add("§9* Type§f Free");
        desc.add("§9* Version§f " + Flux.instance.getDescription().getVersion());
        desc.add("§9* Author§f MCGenerals");
        int x = 0;for(Check c : Flux.instance.checks){ if(c.status){x++;} }
        desc.add("§9* Checks §f" + x + "/" +  Flux.instance.checks.size() );
        desc.add("");
        desc.add("§9Server Information");
        desc.add("§9* ServerType §f" + Flux.instance.getServer().getName());
        desc.add("§9* ServerVersion §f" + Flux.instance.getServer().getVersion());
        desc.add("");
        desc.add("§7Click To Switch Verbose Status.");
        i.addItem("§9Flux Information",Material.BEACON, desc, 11);
        i.addItem(grayGlass, 12);
        i.addItem("§6§lSettings",Material.COMPASS, 13);
        i.addItem(grayGlass, 14);
        i.addItem("§6§lClose",Material.IRON_DOOR, 15);
        i.addItem(grayGlass, 16);
        i.addItem(yellowGlass, 17);
        //Line3
        i.addItem(grayGlass,18);
        i.addItem(yellowGlass, 19);
        i.addItem(grayGlass, 20);
        i.addItem(yellowGlass, 21);
        i.addItem(grayGlass, 22);
        i.addItem(yellowGlass, 23);
        i.addItem(grayGlass, 24);
        i.addItem(yellowGlass, 25);
        i.addItem(grayGlass, 26);

        this.i = i;
    }

}
