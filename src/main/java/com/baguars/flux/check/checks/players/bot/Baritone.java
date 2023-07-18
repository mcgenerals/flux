package com.baguars.flux.check.checks.players.bot;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class Baritone extends Check {
    public Baritone() {
        super("Bot","Baritone","Baritone check",Category.PLAYERS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        //double accelYaw = Math.abs( e.getDeltaYaw() - e.getLastDeltaYaw() );
        //double accelPitch = Math.abs( e.getDeltaPitch() - e.getLastDeltaPitch() );
        /*
         THIS IS BAD CHECK!
        */
        if( e.getDeltaYaw() == 0 || e.getDeltaPitch() == 0){
            return;
        }
        if( ( e.getDeltaYaw() < 0.05 && e.getDeltaPitch() < 0.05 ) && e.p.tickExistSinceBreak < 2 ){
            e.fail(this);
            e.verbose(this,"dy=" + e.getDeltaYaw() + " dp=" + e.getDeltaPitch() );
        }
    }
}
