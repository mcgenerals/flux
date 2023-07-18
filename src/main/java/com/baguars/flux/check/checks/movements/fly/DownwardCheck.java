package com.baguars.flux.check.checks.movements.fly;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.Location;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class DownwardCheck extends Check {

    public DownwardCheck() {
        super("Flight", "B","Simple Glide Detection.", Category.MOVEMENTS);
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
        if ( e.getLastWaterTick() < 12 ) {
            return;
        }
        if(e.p.ticksSinceWeb < 5){
            return;
        }
        if( e.p.tickExistSinceOnLadder < 10 ){
            return;
        }
        if( e.isBelowBlockSolid){
            return;
        }
        if ( e.p.lastDamage < 10) {
            return;
        }
        if( e.p.lastDamageByEntity < 20 ){
            return;
        }
        if( e.getDeltaY() < 0.016 ){
            return;
        }
        if( e.p.ticksSinceOnSlime < 50 ){
            return;
        }
        if( e.getDeltaY() <= e.getLastDeltaY() && e.getTo().getY() < e.getFrom().getY() ){
            e.p.flyBBuffer.onTick();
            if(e.p.flyBBuffer.getTick() < 5){
                return;
            }
            e.fail(this);
            e.verbose(this,"dy=" + e.getDeltaY() );
        }
    }

}
