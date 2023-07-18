package com.baguars.flux.check;

import com.baguars.flux.Flux;
import com.baguars.flux.manager.FluxPlayer;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.block.Block;
import org.bukkit.entity.Player;
import org.bukkit.event.Event;
import org.bukkit.event.HandlerList;
import org.bukkit.event.player.PlayerMoveEvent;

import java.util.ArrayList;
import java.util.concurrent.ConcurrentLinkedDeque;

public class FluxMoveEvent extends Event {
    private static final HandlerList handlers = new HandlerList();


    Player player;
    double deltaX,deltaY,deltaZ;
    double deltaXZ,lastDeltaXZ;
    double lastDeltaX,lastDeltaY,lastDeltaZ;

    Location to,from;
    boolean onGround,onServerGroundMath;
    int groundTick,clientGroundTick,airTick,clientAirTick;

    public boolean isInWeb = false;
    public boolean isBelowBlockSolid = false;
    public boolean isAboveBlockSolid = false;

    public FluxPlayer p;
    public ConcurrentLinkedDeque<Fail> fails = new ConcurrentLinkedDeque<>();

    public void fail(Check check){
        fails.add( new Fail(check.checkName,check.checkType,"",false) );
    }

    public void verbose(Check check,String info){
        for(Fail f : fails){
            if(f.c==check.checkName&&f.t==check.checkType){
                f.info = info;
                f.hasVerbose = true;
            }
        }
    }

    public FluxMoveEvent(Player p,Location newLoc, double lastDeltaX, double lastDeltaY, double lastDeltaZ) {
        FluxPlayer fp = Flux.instance.getPlayer( p );
        Location to = newLoc;
        Location from = fp.lastLoc;
        this.to = to;
        this.from = from;
        this.deltaX = Math.abs( Math.abs( to.getX() ) - Math.abs( from.getX() ) );
        this.deltaY = Math.abs( Math.abs( to.getY() ) - Math.abs( from.getY() ) );
        this.deltaZ = Math.abs( Math.abs( to.getZ() ) - Math.abs( from.getZ() ) );
        this.lastDeltaX = lastDeltaX;
        this.lastDeltaY = lastDeltaY;
        this.lastDeltaZ = lastDeltaZ;
        fp.lastX = deltaX;
        fp.lastY = deltaY;
        fp.lastZ = deltaZ;
        this.player = p;
        this.onGround = player.isOnGround();
        this.onServerGroundMath =  (to.getY() % 0.015625D == 0);
        if(onGround){
            fp.clientGround += 1;
            fp.clientAir = 0;
        }else {
            fp.clientGround = 0;
            fp.clientAir += 1;
        }
        if(onServerGroundMath){
            fp.ground += 1;
            fp.air = 0;
        }else{
            fp.air += 1;
            fp.ground = 0;
        }
        if( player.getEyeLocation().getBlock().isLiquid() || player.getLocation().getBlock().isLiquid() ){
            fp.water += 1;
            fp.lastWater = 0;
        }else {
            fp.lastWater += 1;
            fp.water = 0;
        }
        this.clientAirTick = fp.clientAir;
        this.clientGroundTick = fp.clientGround;
        this.airTick = fp.air;
        this.groundTick = fp.ground;
        fp.tick.onTick();
        this.p = fp;
        fp.yaw = to.getYaw() % 360;
        fp.pitch = to.getPitch() % 90;
        fp.lastYaw = from.getYaw() % 360;
        fp.lastPitch = from.getPitch() % 90;
        fp.lastDeltaYaw = fp.deltaYaw;
        fp.lastDeltaPitch = fp.deltaPitch;

        fp.deltaYaw = Math.abs( fp.yaw - fp.lastYaw );
        fp.deltaPitch = Math.abs( fp.pitch - fp.lastPitch );

        Location standingBlock = to.clone();
        standingBlock.setY(standingBlock.getY() - 1);
        Block block = standingBlock.getBlock();
        isBelowBlockSolid = ( block.getType().isSolid() || ( !(block.getType() == Material.AIR) && !(block.getType() == Material.WATER) )  );
        Location aboveBlock = from.clone();
        aboveBlock.setY(aboveBlock.getY() + 2);
        block = aboveBlock.getBlock();
        isAboveBlockSolid = ( block.getType().isSolid() || ( !(block.getType() == Material.AIR) && !(block.getType() == Material.WATER) )  );

        if( player.getLocation().getBlock().toString().contains("WEB") || player.getEyeLocation().getBlock().toString().contains("WEB") ){
            isInWeb = true;
        }

        deltaXZ = Math.hypot(getDeltaX(),getDeltaZ());
        lastDeltaXZ = Math.hypot(getLastDeltaX(), getLastDeltaZ());

        final Cuboid nearBox = new Cuboid(new CustomLocation(to.getX(),to.getY(),to.getZ(),to.getYaw(),to.getPitch())).expand(0.5, 0.55, 0.5);
        if(nearBox.checkBlocks(player.getWorld(), material -> material == Material.AIR)){
            fp.CuboidGround = false;
            fp.CuboidAirTick++;
        }else {
            fp.CuboidGround = true;
            fp.CuboidAirTick = 0;
        }
        /*
            Cuboid ground check made by DerRedstoner.
            Thanks :D
            https://github.com/DerRedstoner/CheatGuard/
        */
        if( getDeltaXZ() > 0 ){
            this.p.lastPacket = this.p.packet;
            this.p.packet = System.currentTimeMillis();
            double diff = this.p.packet - this.p.lastPacket;
            diffBetweenNormal = 50 - diff;
        }



        aroundCheck();//hahaha
    }

    public double diffBetweenNormal;

    public boolean aroundGround,aroundSlime,aroundIce,aroundWeb,aroundStairs = false;

    public void aroundCheck(){
        int radius = 2;
        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    Block block = this.to.getWorld().getBlockAt(this.to.clone().add(x, y, z));
                    if (block.getType().isSolid() || ( !(block.getType() == Material.AIR) && !(block.getType() == Material.WATER) ) ) {
                        aroundGround = true;
                    }
                    if (block.getType() == Material.SLIME_BLOCK) {
                        aroundSlime = true;
                    }
                    if (block.getType().toString().contains("ICE")) {
                        aroundIce = true;
                    }
                    if(block.getType().toString().contains("STAIR")){
                        aroundStairs = true;
                    }
                }
            }
        }
        radius = 2;
        int yradius = 4;
        for (int x = -radius; x < radius; x++) {
            for (int y = -yradius; y < yradius; y++) {
                for (int z = -radius; z < radius; z++) {
                    Block block = this.to.getWorld().getBlockAt(this.to.clone().add(x, y+1, z));
                    if (block.getType().toString().contains("WEB")){
                        aroundWeb = true;
                    }
                }
            }
        }
    }

    public boolean isAroundGround(){

        /*int radius = 2;
        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    Block block = this.to.getWorld().getBlockAt(this.to.clone().add(x, y, z));
                    if (block.getType().isSolid() || ( !(block.getType() == Material.AIR) && !(block.getType() == Material.WATER) ) ) {
                        return true;
                    }
                }
            }
        }
        return false;
        */
        return aroundGround;
    }

    public boolean isAroundSlime(){
        /*
        int radius = 2;
        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    Block block = this.to.getWorld().getBlockAt(this.to.clone().add(x, y, z));
                    if (block.getType() == Material.SLIME_BLOCK){
                        return true;
                    }
                }
            }
        }
        return false;*/
        return aroundSlime;
    }

    public boolean isAroundIce(){
        /*int radius = 2;
        for (int x = -radius; x < radius; x++) {
            for (int y = -radius; y < radius; y++) {
                for (int z = -radius; z < radius; z++) {
                    Block block = this.to.getWorld().getBlockAt(this.to.clone().add(x, y, z));
                    if (block.getType().toString().contains("ICE")){
                        return true;
                    }
                }
            }
        }
        return false;*/
        return aroundIce;
    }

    public boolean isAroundWeb(){
        /*int radius = 2;
        int yradius = 4;
        for (int x = -radius; x < radius; x++) {
            for (int y = -yradius; y < yradius; y++) {
                for (int z = -radius; z < radius; z++) {
                    Block block = this.to.getWorld().getBlockAt(this.to.clone().add(x, y+1, z));
                    if (block.getType().toString().contains("WEB")){
                        return true;
                    }
                }
            }
        }
        return false;*/
        return aroundWeb;
    }

    public boolean isAroundStairs(){
        return aroundStairs;
    }

    public Player getPlayer(){
        return player;
    }

    public Location getTo(){
        return to;
    }

    public Location getFrom(){
        return from;
    }

    public boolean isServerGround(){
        return onServerGroundMath;
    }

    public boolean isClientGround(){
        return onGround;
    }

    public double getDeltaX() {
        return deltaX;
    }

    public double getDeltaY() {
        return deltaY;
    }

    public double getDeltaZ() {
        return deltaZ;
    }

    public double getLastDeltaX() {
        return lastDeltaX;
    }

    public double getLastDeltaY() {
        return lastDeltaY;
    }

    public double getLastDeltaZ() {
        return lastDeltaZ;
    }

    public double getDeltaXZ(){
        return deltaXZ;
    }

    public double getLastDeltaXZ(){
        return lastDeltaXZ;
    }

    @Deprecated
    public int getAirTick(){
        return airTick;
    }

    public int getClientAirTick(){
        return clientAirTick;
    }

    public int getGroundTick(){
        return groundTick;
    }

    public int getClientGroundTick(){
        return clientGroundTick;
    }

    public int getWaterTick(){
        return p.water;
    }

    public int getLastWaterTick(){
        return p.lastWater;
    }


    public double getDeltaYaw() {
        return p.deltaYaw;
    }

    public double getDeltaPitch() {
        return p.deltaPitch;
    }


    public double getLastDeltaYaw() {
        return p.lastDeltaYaw;
    }

    public double getLastDeltaPitch() {
        return p.lastDeltaPitch;
    }


    public HandlerList getHandlers() {
        return handlers;
    }

    public static HandlerList getHandlerList() {
        return handlers;
    }
}
