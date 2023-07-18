package com.baguars.flux.check.checks.players.nofall;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.event.EventHandler;

public class GroundSpoofCheck extends Check {
    public GroundSpoofCheck() {
        super("NoFall","B","Detects NoFall and GroundSpoof.",Category.PLAYERS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if( !e.p.CuboidGround && e.isServerGround() != e.getPlayer().isOnGround() ){
            e.p.noFallBBuffer.onTick();
            if(e.p.noFallBBuffer.getTick() > 5){
                e.fail(this);
            }
        }
    }
}
