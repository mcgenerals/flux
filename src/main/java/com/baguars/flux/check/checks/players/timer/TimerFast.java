package com.baguars.flux.check.checks.players.timer;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class TimerFast extends Check {


    public TimerFast() {
        super("Timer", "FAST","It detects timer that faster than vanilla.", Category.PLAYERS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if( e.getDeltaXZ() == 0 ){
            return;
        }
        if( e.diffBetweenNormal >= 5 ){
            e.p.timerBuffer1.onTick();
        }
        if(e.p.timerBuffer1.getTick() > 15){
            e.fail(this);
            e.verbose(this,"data=" + e.diffBetweenNormal );
        }
    }

}
