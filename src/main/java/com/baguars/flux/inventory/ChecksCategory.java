package com.baguars.flux.inventory;

import com.baguars.flux.Flux;
import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import org.bukkit.Material;
import org.bukkit.inventory.ItemStack;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.ArrayList;

public class ChecksCategory {

    public InventoryGUI i;
    public ChecksCategory(Category category) {
        InventoryGUI i = new InventoryGUI();
        i.create("Flux(" + category + ")",4,9);
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
        int slot = 0;
        boolean zz = true;
        for( int z = 0; z < 4*9; z++ ){
            i.addItem(( zz ? grayGlass : yellowGlass ),slot);
            zz = !zz;
            slot++;
        }
        ItemStack dye1 = new ItemStack(Material.INK_SACK, 1, (short) 8);
        ItemMeta dye1m = dye1.getItemMeta();
        dye1.setItemMeta(dye1m);
        ItemStack dye2 = new ItemStack(Material.INK_SACK, 1, (short) 10);
        ItemMeta dye2m = dye2.getItemMeta();
        dye2.setItemMeta(dye2m);


        slot = 0;
        for(Check c : Flux.instance.checks){
            if( c.getCategory() == category ){
                ArrayList<String> desc = new ArrayList<>();
                desc.add("Description: ");
                desc.add( (c.desc == "" ? "No Description" : c.desc ) );
                i.addItem(c.getName() + "-" + c.getType(),( c.status ? dye2 : dye1 ),desc,slot);
                slot++;
            }
        }

        i.addItem("§6§lBack",Material.IRON_DOOR, 31);

        this.i = i;
    }
}
