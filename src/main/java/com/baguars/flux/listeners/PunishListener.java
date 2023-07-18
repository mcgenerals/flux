package com.baguars.flux.listeners;

import com.baguars.flux.Flux;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxFlagEvent;
import org.bukkit.Bukkit;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;

public class PunishListener implements Listener {

    @EventHandler
    public void onFlag(FluxFlagEvent e){
        Player p = e.getPlayer();
        Check c = Flux.instance.getCheck(e.getCheck()+"|"+e.getType());
        if( c.punish && (e.getVL()+1)  == c.banVL  ){
            Bukkit.broadcastMessage(Flux.instance.config.getString("punish.message").replace("%player%",p.getName()).replace("&","§").replace("§§","&").replace("%br%","\n"));
            if(!Flux.instance.isDebugMode){
                Bukkit.getScheduler().runTask(Flux.instance, () -> {
                    Bukkit.dispatchCommand(Bukkit.getConsoleSender(),Flux.instance.config.getString("punish.command").replace("%player%",p.getName()).replace("&","§").replace("§§","&") );
                });
            }
        }
    }
}
