package com.baguars.flux.check.checks.movements.motion;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class SimpleYSpeed extends Check {

    public SimpleYSpeed() {
        super("Motion", "A","Step Check", Category.MOVEMENTS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if( e.p.tickExistSinceOnLadder < 10 ){
            return;
        }
        if( e.p.lastDamageByEntity < 20 ){
            return;
        }
        if( e.p.ticksSinceOnSlime < 20 ){
            return;
        }
        if( e.p.teleportTick < 10 ){
            return;
        }
        if( e.getDeltaY() > 0.5 && e.getLastDeltaY() == 0 ){
            e.fail(this);
            e.verbose(this,"dy=" + e.getDeltaY());
        }

    }
}
