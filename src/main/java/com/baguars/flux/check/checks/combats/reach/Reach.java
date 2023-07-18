package com.baguars.flux.check.checks.combats.reach;

import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;

public class Reach extends Check {

    public Reach() {
        super("Reach", "A","3.5 reach detection. it also detects 3.1 reach before. but too many false.", Category.COMBATS);
    }

    @EventHandler
    public void onDamange(FluxMoveEvent e){
        Player p = e.getPlayer();
        if( e.p.ticksSinceHit > 2 || e.p.lastHitEntity == null ){
            return;
        }
        double distance = p.getEyeLocation().distance( e.p.lastHitEntity.getLocation() ) - 1.531294;
        //p.sendMessage( "dist=" + distance );
        if( distance > 3.5 ){
            e.fail(this);
            e.verbose(this,"dist=" + distance );
        }
    }

}
