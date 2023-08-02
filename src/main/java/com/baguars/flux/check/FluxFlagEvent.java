package com.baguars.flux.check;

import com.baguars.flux.Flux;
import com.baguars.flux.manager.FluxPlayer;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;

public class FluxFlagEvent extends Event {
    private static final HandlerList handlers = new HandlerList();

    private boolean hasVerbose,cancelled;
    private String check,type,info;
    private Category category;
    private Player player;
    private FluxPlayer fluxPlayer;
    private int checkVL = 0;
    private int totalVL = 0;

    public FluxFlagEvent(Player player,String check,String type,Category category,boolean hasVerbose,String info) {
        this.player = player;
        this.fluxPlayer = Flux.instance.getPlayer( player );
        this.check = check;
        this.type = type;
        this.info = info;
        this.hasVerbose = hasVerbose;
        this.category = category;
        this.cancelled = false;
        this.checkVL = fluxPlayer.getVL( check + "|" + type );
        this.totalVL = fluxPlayer.getVL( null );
    }

    public int getVL(){
        return checkVL;
    }

    public int getTotalVL(){
        return totalVL;
    }

    public Player getPlayer(){
        return player;
    }

    public String getCheck(){
        return check;
    }

    public String getType(){
        return type;
    }

    public Category getCategory(){
        return category;
    }

    public boolean hasVerbose(){
        return hasVerbose;
    }

    public String getInfo(){
        return info;
    }

    @Deprecated
    public FluxPlayer getPlayerAsFluxPlayer(){
        return fluxPlayer;
    }

    public boolean isCancelled(){
        return cancelled;
    }

    public void setCancelled(boolean val){
        cancelled = val;
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
