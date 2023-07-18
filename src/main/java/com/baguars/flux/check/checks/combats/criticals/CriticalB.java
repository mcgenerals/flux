package com.baguars.flux.check.checks.combats.criticals;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class CriticalB extends Check {
    public CriticalB() {
        super("Critical","B","Test Check",Category.COMBATS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if(e.p.tick_00001 < 15 || e.isAboveBlockSolid || e.getTo().getY() < e.getFrom().getY() ) {
            return;
        }
        if( e.p.slimeTick < 5 ){
            return;
        }
        if( e.p.lastHitEntity != null && e.p.ticksSinceHit < 4 && ( e.isAroundGround() != e.getPlayer().isOnGround() || e.isAroundGround() != e.isServerGround() ) ) {
            e.p.criticalBBuffer.onTick();
            if(e.p.criticalBBuffer.getTick() > 2){
                e.fail(this);
            }
        }
    }
}
