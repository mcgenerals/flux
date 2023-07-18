package com.baguars.flux.listeners;

import com.baguars.flux.Flux;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.player.PlayerJoinEvent;
import org.bukkit.event.player.PlayerQuitEvent;

public class JoinQuitListener implements Listener {
    @EventHandler
    public void onJoin(PlayerJoinEvent e){
        Flux.instance.registerPlayer( e.getPlayer() );
    }
    @EventHandler
    public void onQuit(PlayerQuitEvent e){
        Flux.instance.removePlayer( e.getPlayer() );
    }
}
