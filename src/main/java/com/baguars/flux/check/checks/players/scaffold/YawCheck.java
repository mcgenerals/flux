package com.baguars.flux.check.checks.players.scaffold;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class YawCheck extends Check {
    public YawCheck() {
        super("Scaffold","A","Yaw Speed Detection. §c(Experimental)",Category.PLAYERS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if( e.getDeltaPitch() > 1 && e.p.tickExistSincePlace < 2 && e.getDeltaYaw() >= 100){
            e.fail(this);
            e.verbose(this,"dy="+e.getDeltaYaw() + " dp="+e.getDeltaPitch());
        }
    }
}
