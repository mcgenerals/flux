package com.baguars.flux.check.checks.movements.speed;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SpeedCheck extends Check {

    public SpeedCheck() {
        super("Speed", "A","Accel Check. Detects bad Bhop", Category.MOVEMENTS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if (e.p.lastDamage < 10) {
            return;
        }
        if( e.p.lastDamageByEntity < 20 ){
            return;
        }
        if( e.p.ticksSinceOnIce < 20 ){
            return;
        }
        if( e.p.teleportTick < 10 ){
            return;
        }
        long accelX = (long) (Math.abs(e.getDeltaX() - e.getLastDeltaX()) * 10000);
        long accelZ = (long) (Math.abs(e.getDeltaZ() - e.getLastDeltaZ()) * 10000);
        if( accelX > 5000 || accelZ > 5000 ){
            e.fail(this);
            e.verbose(this,"x="+accelX + " z="+accelZ);
        }
    }

}
