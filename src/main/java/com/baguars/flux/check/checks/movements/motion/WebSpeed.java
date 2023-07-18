package com.baguars.flux.check.checks.movements.motion;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class WebSpeed extends Check {
    public WebSpeed() {
        super("Motion", "C","WebSpeed Detection", Category.MOVEMENTS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if(e.isInWeb && e.p.WebTicks > 1){
            double diff = (e.getDeltaXZ() - e.getLastDeltaXZ()) * 100;
            if( diff > 0.065 ){
                e.fail(this);
                e.verbose(this,"accel=" + diff );
            }
        }
    }
}
