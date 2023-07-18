package com.baguars.flux.check.checks.combats.criticals;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class CriticalA extends Check {
    public CriticalA() {
        super("Critical","A","Bad Critical Check",Category.COMBATS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if(e.p.tick_00001 < 15 || e.isAboveBlockSolid || e.getTo().getY() < e.getFrom().getY() ) {
            return;
        }
        if( e.p.slimeTick < 5 ){
            return;
        }
        if( e.p.lastHitEntity != null &&
                e.p.ticksSinceHit < 4 &&
                !e.isClientGround() && ( e.getDeltaY() > 0 || e.p.ground < 1 ) ) {
            e.fail(this);
        }
    }
}
