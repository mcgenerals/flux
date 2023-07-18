package com.baguars.flux.check.checks.movements.motion;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class MovementPatcher extends Check {

    public MovementPatcher() {
        super("Motion", "E","Invalid Y Movement Detection. §c(Experimental)", Category.MOVEMENTS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if( e.p.CuboidAirTick < 7 ){
            return;
        }
        double predicationY = e.p.CuboidAirTick * 0.07;
        double difference = Math.abs(e.getDeltaY() - predicationY);
        if( difference > 1 ){
            e.fail(this);
            e.verbose(this,"debug=" + difference );
        }
    }
}
