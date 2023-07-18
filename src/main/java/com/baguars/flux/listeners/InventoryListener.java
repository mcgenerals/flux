package com.baguars.flux.listeners;

import com.baguars.flux.Flux;
import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.inventory.CategoryMenu;
import com.baguars.flux.inventory.ChecksCategory;
import com.baguars.flux.inventory.MainMenu;
import org.bukkit.Bukkit;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.inventory.InventoryClickEvent;
import org.bukkit.inventory.Inventory;
import org.bukkit.inventory.ItemStack;

public class InventoryListener implements Listener {
    @EventHandler
    public void onPickUp(InventoryClickEvent e) {
        Player player = (Player) e.getWhoClicked( );
        if( !player.hasPermission("flux.admin") ){
            return;
        }
        Inventory open = e.getClickedInventory( );
        ItemStack item = e.getCurrentItem( );
        if ( open == null ) {
            return;
        }
        if( item == null ){
            return;
        }
        if( item.getItemMeta() == null ){
            return;
        }
        String openname = open.getName( );
        String itemname = item.getItemMeta().getDisplayName();
        if ( openname.contains( "Flux[MainMenu]" ) ) {
            e.setCancelled( true );
            if( itemname.contains( "§9Flux Information" ) ){
                player.chat("/flux verbose");
            }
            if( itemname.contains( "§6§lSettings" ) ){
                player.openInventory(new CategoryMenu().i.getInventory());
            }
            if( itemname.equalsIgnoreCase( "§6§lClose" ) ){
                player.closeInventory();
            }
        }else if ( openname.contains( "Flux[Category]" ) ) {
            e.setCancelled( true );
            if( itemname.contains( "Combats" ) ){
                player.openInventory( new ChecksCategory( Category.COMBATS ).i.getInventory() );
            }
            if( itemname.contains( "Movements" ) ){
                player.openInventory( new ChecksCategory( Category.MOVEMENTS ).i.getInventory() );
            }
            if( itemname.contains( "Players" ) ){
                player.openInventory( new ChecksCategory( Category.PLAYERS ).i.getInventory() );
            }
            if( itemname.equalsIgnoreCase( "§6§lBack" ) ){
                player.openInventory(new MainMenu().i.getInventory());
            }
        }else if ( openname.startsWith("Flux(") && openname.endsWith("S)") ) {
            e.setCancelled( true );
            if( itemname.equalsIgnoreCase( "§6§lBack" ) ){
                player.openInventory(new CategoryMenu().i.getInventory());
            }else if( !itemname.contains("Glass") ){
                String name = itemname.split("-")[0];
                String type = itemname.split("-")[1];
                for(Check c : Flux.instance.checks){
                    if(c.getName().equalsIgnoreCase(name) && c.getType().equalsIgnoreCase(type)){
                        c.status = !c.status;
                        Flux.instance.config.set("checks." + c.getCategory().toString().toLowerCase() + "." + name.toLowerCase() + "." + type.toLowerCase()+".status",c.status);
                        Flux.instance.saveConfig();
                        Flux.instance.reloadConfig();
                        Flux.instance.config = Flux.instance.getConfig();
                        Flux.instance.checks(Bukkit.getPluginManager(),1);
                        player.openInventory( new ChecksCategory( c.getCategory() ).i.getInventory() );
                    }
                }
            }
        }
    }
}
