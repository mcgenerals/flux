package com.baguars.flux.check.checks.players.nofall;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class WierdDataCheck extends Check {
    public WierdDataCheck() {
        super("NoFall","A","Detects NoFall and GroundSpoof.",Category.PLAYERS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if( ( ( e.getDeltaY() > 0 && e.getFrom().getY() > e.getFrom().getY() ) || ( e.getDeltaY() == 0 && e.p.CuboidAirTick > 24 ) ) && e.getPlayer().isOnGround() ){
            e.p.noFallBuffer.onTick();
            if(e.p.noFallBuffer.getTick() > 5){
                e.fail(this);
            }
        }
    }
}
