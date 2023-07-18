package com.baguars.flux.check.checks.movements.fly;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class PredicationCheck extends Check {

    public PredicationCheck() {
        super("Flight", "A","Upward Fly Detection.", Category.MOVEMENTS);
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        if( e.getClientAirTick() < 7 ){
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
        if (e.p.lastDamage < 10) {
            return;
        }
        if( e.p.lastDamageByEntity < 20 ){
            return;
        }
        if( e.p.ticksSinceOnSlime < 50 ){
            return;
        }
        if( e.getFrom().getY() > e.getTo().getY() ){
            double predictedDeltaY = (e.getLastDeltaY() - 0.08d) * 0.98d;
            double diffPred = Math.abs(e.getDeltaY() - predictedDeltaY);
            if(diffPred > 0.15681){
                e.p.flyABuffer.onTick();
                if(e.p.flyABuffer.getTick() > 5){
                    e.fail(this);
                    e.verbose(this,"buffer=" + e.p.flyABuffer.getTick() );
                }
            }
        }
    }

}