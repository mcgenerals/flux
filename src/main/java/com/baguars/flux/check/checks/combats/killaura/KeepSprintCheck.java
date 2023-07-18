package com.baguars.flux.check.checks.combats.killaura;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class KeepSprintCheck extends Check {

    public KeepSprintCheck() {
        super("KillAura", "C", "Keep Sprint Check. §c(Experimental)",Category.COMBATS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        Player p = e.getPlayer();
        if( p.isSprinting() && e.p.ticksSinceHit < 1 ){
            e.fail(this);
        }
    }

}
