package com.baguars.flux.check.checks.combats.autoclicker;

import com.baguars.flux.Flux;
import com.baguars.flux.check.Category;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.FluxMoveEvent;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.block.Action;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.inventory.ItemStack;

public class AutoClicker extends Check {

    public AutoClicker() {
        super("AutoClicker", "A","Simple AutoClicker check", Category.COMBATS);
    }

    @EventHandler
    public void onArm(PlayerInteractEvent e){
        if( e.getAction() == Action.LEFT_CLICK_AIR ){
            Flux.instance.getPlayer( e.getPlayer() ).left.onTick();
        }
    }

    @EventHandler
    public void onMove(FluxMoveEvent e){
        int clicks = Flux.instance.getPlayer( e.getPlayer() ).left.getTick();
        if( clicks >= 20 ){
            e.fail(this);
            e.verbose(this,"clicks="+clicks);
        }
    }
}
