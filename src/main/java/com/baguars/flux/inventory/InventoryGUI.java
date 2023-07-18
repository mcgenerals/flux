package com.baguars.flux.inventory;


import org.bukkit.*;
import org.bukkit.inventory.*;
import org.bukkit.inventory.meta.ItemMeta;

import java.util.*;

public class InventoryGUI {

    public InventoryGUI(){

    }

    /**
     *
     * @author BaGuAr
     *
     * I remake InvGUI.java. old own is here:
     * https://github.com/BaGuAr/InvGUI/blob/main/InvGUI.java
     * also BetterInvGUI.java is not tested. if you found bugs. please report it!
     * If you'll use this , do not remove this message
     * and If you'll upload your project(that using this) to Internet(spigot-mc/mc-market etc) please put this github(BetterInvGUI.java) link to your project's description
     * Thanks for using this!
     *
     */

    private org.bukkit.inventory.Inventory inv;
    private int hight;
    private int width;

    public InventoryGUI create(org.bukkit.inventory.Inventory from){
        inv = from;
        return this;
    }

    public InventoryGUI create(String name, int hight, int width){
        this.hight = hight;
        this.width = width;
        inv = Bukkit.createInventory( null ,hight * width ,name );
        return this;
    }

    public InventoryGUI create(String name, int size){
        inv = Bukkit.createInventory( null ,size ,name );
        return this;
    }

    public enum ChestType{
        LARGE,NORMAL
    }

    public InventoryGUI create(String name, ChestType c){
        return create( name ,( c == ChestType.LARGE ? 6 : 3 ) * 9 );
    }

    public InventoryGUI setName(String name){
        org.bukkit.inventory.Inventory inv = new InventoryGUI().create( name , getSlotLength() + 1 ).getInventory();
        inv.setContents( this.inv.getContents() );
        this.inv = inv;
        return this;
    }

    public InventoryGUI setHight(int hight){
        this.hight = hight;
        inv.setMaxStackSize( hight * width );
        return this;
    }

    public InventoryGUI setWidth(int width){
        this.width = width;
        inv.setMaxStackSize( hight * width );
        return this;
    }

    public int getHight(){
        return hight;
    }

    public int getWidth(){
        return width;
    }

    public int[] getSize(){
        return new int[]{getHight(),getWidth()};
    }

    public InventoryGUI addItem(String name, ItemStack stack, int index){
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        stack.setItemMeta(meta);
        inv.setItem( index,stack );
        return this;
    }

    public InventoryGUI addItem(String name, Material material, int index){
        return addItem( name , new ItemStack( material ) , index );
    }

    public InventoryGUI addItem(String name, ItemStack stack, List list, int index){
        ItemMeta meta = stack.getItemMeta();
        meta.setDisplayName(name);
        meta.setLore(list);
        stack.setItemMeta(meta);
        inv.setItem( index , stack );
        return this;
    }

    public InventoryGUI addItem(String name, Material material, List list, int index){
        return addItem( name , new ItemStack( material ) , list, index );
    }

    public InventoryGUI addItem(ItemStack stack, int index){
        inv.setItem( index , stack );
        return this;
    }

    public InventoryGUI addItem(Material material, int index){
        return addItem( new ItemStack( material ) , index );
    }

    public int getSlotLength(){
        return ( hight * width ) - 1;
    }


    public org.bukkit.inventory.Inventory getInventory(){
        return inv;
    }

    @Deprecated
    public List getAsList(String str){
        List list = new ArrayList();
        String[] strx = str.split( "\n" );
        for( String string : strx ){
            list.add( string );
        }
        return list;
    }
}