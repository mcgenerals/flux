package com.baguars.flux.check.checks.movements.speed;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class Predication extends Check {

    public Predication() {
        super("Speed", "E","Predication", Category.MOVEMENTS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e) {
        if(e.p.ticksSinceSneak < 5){ //it will be bypass but i dont care just make sneak speed limit check
            return;
        }
        if(e.isAroundStairs()){
            return;
        }
        if(e.isAboveBlockSolid) {
            return;
        }
        double pred = e.getLastDeltaXZ() * 0.91;
        double diff = (e.getDeltaXZ() - pred);
        if (diff > 0.1) {
            e.p.speedEBuffer.onTick();
            if(e.p.speedEBuffer.getTick() > 5){
                e.fail(this);
                e.verbose(this,"diff=" + diff + " xz=" + e.getDeltaXZ() + " pred=" + pred);
            }
        }
    }
}
