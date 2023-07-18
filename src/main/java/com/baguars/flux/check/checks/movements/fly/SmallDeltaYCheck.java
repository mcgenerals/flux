package com.baguars.flux.check.checks.movements.fly;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class SmallDeltaYCheck extends Check {

    public SmallDeltaYCheck() {
        super("Flight", "C","Weird DeltaY while in air detection. §c(Experimental)", Category.MOVEMENTS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if( e.p.CuboidGround ){
            return;
        }
        if( e.p.lastWater < 5 ) {
            return;
        }
        if( e.p.tickExistSinceOnLadder < 5 ){
            return;
        }
        if( e.getDeltaY() <= 0.0001 ){
            if(e.p.flyCbuffer++ > 2){
                e.fail(this);
                e.verbose(this,"bf=" + e.p.flyCbuffer );
            }
        }else {e.p.flyCbuffer = 0;}
    }

}
