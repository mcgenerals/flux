package com.baguars.flux.check.checks.movements.speed;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class DeltaXZCheck extends Check {

    public DeltaXZCheck() {
        super("Speed", "B","Fast DeltaXZ Detection.", Category.MOVEMENTS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if( e.p.tick_00001 < 5 || e.p.tick_00001 == 11 ){
            return;
        }
        if( e.p.tickExistSinceOnLadder < 10 ){
            return;
        }
        if( e.p.lastDamageByEntity < 20 ){
            return;
        }
        if( e.p.ticksSinceOnIce < 20 ){
            return;
        }
        if( e.isAboveBlockSolid ){
            return;
        }
        double limit = 0.5;
        if(e.getDeltaX() > limit || e.getDeltaZ() > limit ){
            e.p.speedBBuf.onTick();
            if(e.p.speedBBuf.getTick() < 5){
                return;
            }
            e.fail(this);
            e.verbose(this,"at="+e.p.tick_00001);
        }
    }

}
