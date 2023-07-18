package com.baguars.flux.check.checks.movements.motion;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class WeirdDeltaYCheck extends Check {
    public WeirdDeltaYCheck() {
        super("Motion", "B", Category.MOVEMENTS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if(e.p.ticksSinceWeb < 5){
            return;
        }
        if(e.getLastWaterTick() < 5){
            return;
        }
        if(e.isAroundStairs()){
            return;
        }
        if(e.p.tickExistSinceOnLadder < 5){
            return;
        }
        double diff = e.getDeltaY() / e.getLastDeltaY();
        double data = Math.abs( e.p.lastDiff - diff ) * 100000;
        if( data < 100 && e.getDeltaY() < 1 ){
            e.fail(this);
            e.verbose(this,"data=" + data + " dy=" + e.getDeltaY() );
        }
        e.p.lastDiff = diff;
    }
}
