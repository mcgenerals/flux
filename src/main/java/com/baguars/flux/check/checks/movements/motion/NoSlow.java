package com.baguars.flux.check.checks.movements.motion;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class NoSlow extends Check {
    public NoSlow() {
        super("Motion", "D","NoSlow Detection", Category.MOVEMENTS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if( e.p.BlockingTicks > 5 ){
            if( e.p.ticksSinceSprint < 5 ){
                e.fail(this);
                e.verbose(this,"Sprinting while Blocking.");
            }
        }
        if( e.p.SneakingTicks > 5 ){
            if( e.p.ticksSinceSprint < 5 ){
                e.fail(this);
                e.verbose(this,"Sprinting while Sneaking.");
            }
        }
        //if( e.p.ticksSinceEat == 0 ){
        //    if( e.p.SprintTicks > 0 ){
        //        e.fail(this);
        //        e.verbose(this,"Sprinting while Eating.");
        //    }
        //}
    }
}
