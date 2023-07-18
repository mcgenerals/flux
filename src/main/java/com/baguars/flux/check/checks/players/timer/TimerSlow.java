package com.baguars.flux.check.checks.players.timer;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class TimerSlow extends Check {


    public TimerSlow() {
        super("Timer", "SLOW","It detects timer that slower than vanilla.", Category.PLAYERS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if( e.getDeltaXZ() == 0 ){
            return;
        }
        if( e.diffBetweenNormal <= -5 ){
            e.p.timerBuffer2.onTick();
        }
        if(e.p.timerBuffer2.getTick() > 10){
            e.fail(this);
            e.verbose(this,"data=" + e.diffBetweenNormal );
        }
    }

}
