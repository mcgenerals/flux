package com.baguars.flux.check.checks.combats.killaura;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class ZeroPitchCheck extends Check {

    public ZeroPitchCheck() {
        super("KillAura", "B","Zero Pitch KillAura Detection.", Category.COMBATS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        Player p = e.getPlayer();
        if( p.getLocation().getPitch() == 0 && e.p.ticksSinceHit < 2 && e.p.lastHitEntity != null ){
            e.fail(this);
        }
    }

}
