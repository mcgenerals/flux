package com.baguars.flux.check.checks.movements.speed;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class SpeedD extends Check {
    public SpeedD() {
        super("Speed", "D", Category.MOVEMENTS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if(e.getDeltaXZ() == 0 || e.getLastDeltaXZ() ==0){
            return;
        }
        double deltaXZData = Math.abs(e.getDeltaXZ() / e.getLastDeltaXZ());
        if( deltaXZData > 2){
            e.p.speedDbuf.onTick();
            if(e.p.speedDbuf.getTick() > 5){
                e.fail(this);
                e.verbose(this,"data=" + deltaXZData);
            }
        }
    }
}
