package com.baguars.flux.check.checks.combats.killaura;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class YawDiffCheck extends Check {

    public YawDiffCheck() {
        super("KillAura", "A","Yaw Difference Check. §c(Experimental)", Category.COMBATS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        double diff = Math.abs( e.p.hitYaw - e.getPlayer().getLocation().getYaw() );
        if( e.p.ticksSinceHit < 2 && e.p.lastHitEntity != null ){
            double diffYaw =  Math.abs( diff - e.getDeltaYaw() );
            if( diff > 1 && diffYaw > 3 ){
                e.p.killAuraAbuffer.onTick();
                if(e.p.killAuraAbuffer.getTick() > 2){
                    e.fail(this);
                    e.verbose(this,"accelYaw=" + diffYaw + " deltaDiff="+ diff);
                }
            }
        }
    }

}
