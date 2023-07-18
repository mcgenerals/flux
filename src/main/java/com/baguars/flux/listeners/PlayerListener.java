package com.baguars.flux.listeners;

import com.baguars.flux.Flux;
import com.baguars.flux.check.Check;
import com.baguars.flux.check.Fail;
import com.baguars.flux.check.FluxFlagEvent;
import com.baguars.flux.check.FluxMoveEvent;
import com.baguars.flux.manager.BetterLong;
import com.baguars.flux.manager.FluxPlayer;
import net.md_5.bungee.api.chat.ClickEvent;
import net.md_5.bungee.api.chat.ComponentBuilder;
import net.md_5.bungee.api.chat.HoverEvent;
import net.md_5.bungee.api.chat.TextComponent;
import org.bukkit.Bukkit;
import org.bukkit.GameMode;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Player;
import org.bukkit.event.EventHandler;
import org.bukkit.event.Listener;
import org.bukkit.event.block.Action;
import org.bukkit.event.block.BlockBreakEvent;
import org.bukkit.event.block.BlockPlaceEvent;
import org.bukkit.event.entity.EntityDamageByEntityEvent;
import org.bukkit.event.entity.EntityDamageEvent;
import org.bukkit.event.entity.FoodLevelChangeEvent;
import org.bukkit.event.player.PlayerInteractEvent;
import org.bukkit.event.player.PlayerTeleportEvent;
import org.bukkit.inventory.ItemStack;

public class PlayerListener implements Listener {

    @EventHandler
    public void onDamage(EntityDamageEvent e){
        if( e.getEntity() instanceof Player ){
            Player p = ((Player) e.getEntity()).getPlayer();
            if( p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR ){
                return;
            }
            FluxPlayer fp = Flux.instance.getPlayer( p );
            if(fp!=null){
                fp.lastDamage = 0;
            }
        }
    }
    @EventHandler
    public void onBlockPlace(BlockPlaceEvent e){
        if( e.getPlayer().getGameMode() == GameMode.CREATIVE || e.getPlayer().getGameMode() == GameMode.SPECTATOR ){
            return;
        }
        FluxPlayer fp = Flux.instance.getPlayer(e.getPlayer());
        fp.tickExistSincePlace = 0;
        fp.blockPlaceCount.onTick();;
        fp.lastPlaceTime = System.currentTimeMillis();
    }

    @EventHandler
    public void onBlockBreak(BlockBreakEvent e){
        if( e.getPlayer().getGameMode() == GameMode.CREATIVE || e.getPlayer().getGameMode() == GameMode.SPECTATOR ){
            return;
        }
        FluxPlayer fp = Flux.instance.getPlayer(e.getPlayer());
        fp.tickExistSinceBreak = 0;
        fp.blockBreakCount.onTick();
        if( fp.lastBreakTime != 0 ){
            double difference = System.currentTimeMillis() - fp.lastBreakTime;
            if( difference < 45 ){
                fp.bufferNuker.onTick();
            }
        }
        fp.lastBreakTime = System.currentTimeMillis();
    }

    @EventHandler
    public void onHit(EntityDamageByEntityEvent e){
        if(e.getDamager() instanceof Player) {
            Player p = ((Player) e.getDamager()).getPlayer();
            if( p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR ){
                return;
            }
            FluxPlayer fp = Flux.instance.getPlayer(p);
            fp.hitYaw = p.getLocation().getYaw();
            fp.hitPitch = p.getLocation().getPitch();
            fp.ticksSinceHit = 0;
            fp.lastHitEntity = e.getEntity();
            fp.hitCounter.onTick();
            if( e.getEntity() instanceof Player ){
                FluxPlayer fpe = Flux.instance.getPlayer(((Player) e.getEntity()).getPlayer());
                if(fpe!=null){
                    fpe.lastDamageByEntity = 0;
                }
            }
        }
    }

    //@EventHandler
    public static void onMove(Player p,double XXX,double YYY,double ZZZ,long timeee){
        Location to = new Location( p.getWorld() ,XXX,YYY,ZZZ );
        to.setYaw( p.getLocation().getYaw() );
        to.setPitch( p.getLocation().getPitch() );
        FluxPlayer fp = Flux.instance.getPlayer( p );
        if(fp.lastLoc==null){
            fp.lastLoc = to;
        }
        if( p.getGameMode() == GameMode.CREATIVE || p.getGameMode() == GameMode.SPECTATOR ){
            return;
        }
        if( p.getAllowFlight() || p.isFlying() ){
            return;
        }
        fp.ticksSinceHit++;
        fp.tickExistSincePlace++;
        fp.tickExistSinceBreak++;
        if(fp.onLadder()){
            fp.tickExistSinceOnLadder = 0;
            fp.ladderTick++;
        }else{
            fp.tickExistSinceOnLadder++;
            fp.ladderTick = 0;
        }
        fp.lastDamage++;
        FluxMoveEvent event = new FluxMoveEvent(p,to,fp.lastX,fp.lastY,fp.lastZ);
        Flux.instance.getServer().getPluginManager().callEvent(event);

        if( event.isAroundGround() ){
            fp.lastGroundY = event.getTo().getY();
            fp.PossibleMovedY = 0;
        }
        fp.lastDamageByEntity++;

        if( event.isAroundGround() ){
            fp.betterAirTick = 0;
        }else{
            fp.betterAirTick++;
        }

        if( event.getDeltaY() >= 0.41 && to.getY() > fp.lastLoc.getY() ){
            fp.tick_00001 = 0;
        }else {
            fp.tick_00001++;
        }

        if( event.isAroundSlime() ){
            fp.slimeTick ++;
            fp.ticksSinceOnSlime = 0;
        }else {
            fp.slimeTick = 0;
            fp.ticksSinceOnSlime++;
        }
        if( event.isAroundIce() ){
            fp.iceTick++;
            fp.ticksSinceOnIce = 0;
        }else {
            fp.iceTick = 0;
            fp.ticksSinceOnIce++;
        }

        if( event.getPlayer().isSneaking() ){
            fp.ticksSinceSneak = 0;
            fp.SneakingTicks++;
        }else {
            fp.ticksSinceSneak++;
            fp.SneakingTicks = 0;
        }

        if( event.isAroundWeb() ){
            fp.ticksSinceWeb = 0;
            fp.WebTicks++;
        }else {
            fp.ticksSinceWeb++;
            fp.WebTicks = 0;
        }

        if( event.getPlayer().isBlocking() ){
            fp.BlockingTicks++;
            fp.ticksSinceBlocking = 0;
        }else {
            fp.BlockingTicks = 0;
            fp.ticksSinceBlocking++;
        }

        if( event.getPlayer().isSprinting() ){
            fp.SprintTicks++;
            fp.ticksSinceSprint = 0;
        }else {
            fp.SprintTicks = 0;
            fp.ticksSinceSprint++;
        }

        fp.teleportTick++;
        fp.ticksSinceEat++;


        for( Fail f : event.fails ){
            for(Check c : Flux.instance.checks){
                if(c.getName()==f.c && c.getType()==f.t){
                    f.idk = c.getCategory();
                    if( !c.status ){
                        event.fails.remove(f);
                    }
                }
            }
        }


        for( Fail fails : event.fails){
            FluxFlagEvent fe = new FluxFlagEvent(p,fails.c,fails.t, fails.idk,fails.hasVerbose,fails.info);
            Flux.instance.getServer().getPluginManager().callEvent(fe);
            if(!fe.isCancelled()){
                String checkStr = fails.c + "|" + fails.t;
                fp.flag( checkStr );
                int checkVl = fp.getVL( checkStr );
                int totalVl = fp.getVL( null );
                String bar = "§7|§7|§7|§7|§7|§7|§7|§7|§7|§7|§7|§7|§7|§7|§7|§7|§7|§7|§7|§7|";
                int barsToAdd = (int) (Math.min(1, checkVl / (double) Flux.instance.getCheck(fails.c + "|" + fails.t).banVL ) * 20.0);
                for(int i = 0; i < barsToAdd; i++) {
                    bar = bar.replaceFirst("§7\\|","§c|");
                }
                //String alert = "§c[§6§lFlux§r§c]§e " + event.p.getName() + "§a failed §e" + fails.c + " (" + fails.t + ") §aVL: " + checkVl + "/" + totalVl + ( fails.hasVerbose ? "§aInfo: " + fails.info : "");
                String alert = "§8§l[§6§lFLUX§r§8§l]§e " + event.p.getName() + "§7 failed §e" + fails.c + " (" + fails.t + ") §8[" + bar + "§8] ";
                TextComponent tc = new TextComponent(alert);
                tc.setHoverEvent(new HoverEvent(HoverEvent.Action.SHOW_TEXT, (new ComponentBuilder("§9CheckVL:§f " + event.p.getCheckVL(checkStr) + "\n§9TotalVL:§f " + totalVl + "\n§9Ping:§f " + event.p.getPing() + "\n§9CPS:§f " + ( (Math.floor(event.p.getAverageCPS()[0] * 10)) / 10 ) + ( fails.hasVerbose ? "\n§9Info:§f " + fails.info : "" ) +  "\n\n§7Click To Teleport"  )).create()));
                tc.setClickEvent( new ClickEvent(ClickEvent.Action.RUN_COMMAND,"/tp " + event.p.getName() ));
                for( Player xp : Bukkit.getOnlinePlayers() ){
                    if( ( xp.hasPermission("flux.admin")&& Flux.instance.getPlayer( xp ).flagEnabled ) ){
                        //xp.sendMessage(alert );
                        xp.spigot().sendMessage(tc);
                    }
                }
                if(  Flux.instance.isDebugMode  ){
                    event.p.getPlayer().spigot().sendMessage(tc);
                }
            }
        }
        fp.lastLoc = to;
        fp.lastYaw = to.getYaw();
        fp.lastPitch = to.getPitch();

        Flux.instance.pf.add( System.currentTimeMillis() - timeee );
    }

    @EventHandler
    public void onTeleport(PlayerTeleportEvent e){
        FluxPlayer fp = Flux.instance.getPlayer( e.getPlayer() );
        if(fp==null){
            //ITS TOO IMPOSSIBLE TO HAPPEN. BUT IF PLAYER GOT TELEPORTED BEFORE FLUX REGISTER THE PLAYER. IT WILL ERROR SO!!
            return;
        }
        fp.teleportTick = 0;
    }

    @EventHandler
    public void onEat(PlayerInteractEvent e){
        Action action = e.getAction();
        Player player = e.getPlayer();
        if(action == Action.RIGHT_CLICK_AIR || action == Action.RIGHT_CLICK_BLOCK ){
            ItemStack is = player.getItemInHand();
            if( is != null && is.getType().isEdible() && ( player.getFoodLevel() < 20 || is.getType() == Material.GOLDEN_APPLE || is.getType() == Material.POTION )){
                Flux.instance.getPlayer(player).ticksSinceEat = 0;
            }
        }
    }
}
