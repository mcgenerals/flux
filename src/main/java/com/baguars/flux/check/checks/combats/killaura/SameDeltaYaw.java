package com.baguars.flux.check.checks.combats.killaura;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class SameDeltaYaw extends Check {
    public SameDeltaYaw() {
        super("KillAura", "D","Same YawDelta Detection", Category.COMBATS);
    }


    @EventHandler
    public void onMove(FluxMoveEvent e){
        if( e.getDeltaYaw() != 0 && e.getDeltaYaw() == e.getLastDeltaYaw() ){
            e.p.sameYawBuffer.onTick();
            if(e.p.sameYawBuffer.getTick() > 1){
                e.fail(this);
            }
        }
    }
}
