package com.baguars.flux.check.checks.movements.fly;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class BetterAitTickFlyDetection extends Check {

    public BetterAitTickFlyDetection() {
        super("Flight", "E","Bad Air Action Detection.", Category.MOVEMENTS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if( e.p.CuboidAirTick < 7 ){
            return;
        }
        Player p = e.getPlayer();
        if( p.isInsideVehicle() || p.getVehicle() != null ){
            return;
        }
        if (e.getLastWaterTick() < 12 ) {
            return;
        }
        if( e.p.tickExistSinceOnLadder < 10 ){
            return;
        }
        if( e.p.lastDamageByEntity < 20 ){
            return;
        }
        if( e.p.tickExistSinceOnLadder < 5 ){
            return;
        }
        if( e.p.ticksSinceOnSlime < 50 ){
            return;
        }
        if( e.getTo().getY() >= e.getFrom().getY() || e.getDeltaY() == e.getLastDeltaY() ){
            e.fail(this);
            e.verbose(this,"at=" + e.p.betterAirTick );
        }
    }

}
