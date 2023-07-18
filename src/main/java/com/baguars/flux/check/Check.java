package com.baguars.flux.check;

import org.bukkit.event.Listener;

public class Check implements Listener {

    String checkName;
    String checkType;
    Category category;
    public boolean status = true;

    public int banVL = 20;
    public boolean punish = false;

    public String desc = "";

    public Check(String name,String type,Category category){
        this.checkName = name;
        this.checkType = type;
        this.category = category;
    }

    public Check(String name,String type,String info,Category category){
        this.checkName = name;
        this.checkType = type;
        this.category = category;
        this.desc = info;
    }


    public String getName(){
        return checkName;
    }

    public String getType(){
        return checkType;
    }

    public Category getCategory(){return category;}


}
