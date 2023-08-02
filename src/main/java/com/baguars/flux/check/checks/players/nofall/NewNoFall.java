package com.baguars.flux.check.checks.players.nofall;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class NewNoFall extends Check {
    public NewNoFall() {
        super("NoFall","C","test",Category.PLAYERS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        String str = "";
        if( e.p.CuboidGround != e.isClientGround() &&  e.p.CuboidAirTick > 1 ){
            e.p.groundSpoofBufferA.onTick();
            if( e.p.groundSpoofBufferA.getTick() > 5 ){
                str = "Failed In Method(1)\nBuffer=" + e.p.groundSpoofBufferA.getTick();
            }
        }
        if( e.p.CuboidGround ){
            e.p.fallDistance = 0;
        }else {
            e.p.fallDistance += e.getDeltaY();
        }
        double diff = e.p.fallDistance - e.getPlayer().getFallDistance();
        if( e.p.CuboidAirTick > 2 && diff > 1.7 ) {
            e.p.groundSpoofBufferB.onTick();
            if (e.p.groundSpoofBufferB.getTick() > 5) {
                str += "Failed In Method(2)\nDiff=" + diff + "\nBuffer=" + e.p.groundSpoofBufferB.getTick();
            }
        }
        if( str != "" ){
            e.fail(this);
            e.verbose(this,str);
        }
    }
}
