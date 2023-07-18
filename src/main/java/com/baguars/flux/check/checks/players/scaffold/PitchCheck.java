package com.baguars.flux.check.checks.players.scaffold;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class PitchCheck extends Check {
    public PitchCheck() {
        super("Scaffold","B","Pitch Speed Detection. §c(Experimental)",Category.PLAYERS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if( e.getDeltaPitch() > 3.5 && e.p.tickExistSincePlace < 2 && e.getDeltaY() == 0 ){
            e.fail(this);
            e.verbose(this,"dp="+e.getDeltaPitch());
        }
    }
}
