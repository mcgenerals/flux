package com.baguars.flux.check.checks.movements.speed;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class YawAndSpeedCheck extends Check {

    public YawAndSpeedCheck() {
        super("Speed", "C","I skidded this from nik's tutorial :D thanks for this amazing check OwO", Category.MOVEMENTS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        double accelXZ = Math.abs( e.getDeltaXZ() - e.getLastDeltaXZ() ) * 100;
        String debug = "DeltaYaw=" + e.getDeltaYaw()  + " deltaXZ="  + e.getDeltaXZ() + " accelXZ=" + accelXZ;
        if( e.getDeltaYaw() > 1.5F && e.getDeltaXZ() > 0.15D && accelXZ < 1.05E-5 ){
            e.fail(this);
            e.verbose(this,debug);
        }
    }

}
