package com.baguars.flux.manager;

import com.baguars.flux.Flux;
import com.baguars.flux.check.Counter;
import org.bukkit.Bukkit;
import org.bukkit.Location;
import org.bukkit.Material;
import org.bukkit.entity.Entity;
import org.bukkit.entity.Player;



import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;
import java.util.concurrent.ConcurrentLinkedQueue;

public class FluxPlayer {

    public boolean CuboidGround;
    public int CuboidAirTick = 0;

    Player player;
    public Location lastLoc;
    public HashMap<String,Integer> violations;

    public Entity lastHitEntity;
    public boolean flagEnabled = false;
    public int clientGround,clientAir;
    public int ground,air,water,lastWater;
    public double lastX,lastY,lastZ;
    public float yaw,pitch;
    public float lastYaw,lastPitch;
    public float deltaYaw,deltaPitch,lastDeltaYaw,lastDeltaPitch;

    public float hitYaw,hitPitch;
    public int ticksSinceHit;

    public int tickExistSincePlace,tickExistSinceBreak;

    public int tickExistSinceOnLadder,ladderTick;

    public int flyCbuffer = 0;
    public Counter noFallBuffer = new Counter();
    public int lastDamage,lastDamageByEntity = 0;

    public Counter left = new Counter();
    public Counter right = new Counter();
    public double lastGroundY,PossibleMovedY;

    public int betterAirTick = 0;

    public int tick_00001 = 0;

    public int slimeTick,ticksSinceOnSlime = 0;
    public int iceTick,ticksSinceOnIce = 0;

    String name;

    public int teleportTick = 0;


    public long packet,lastPacket = 0;
    public Counter timerBuffer1 = new Counter();
    public Counter timerBuffer2 = new Counter();

    Player p;

    public Counter blockBreakCount = new Counter();
    public Counter blockPlaceCount = new Counter();

    public Counter bufferNuker = new Counter();

    public long lastBreakTime = 0;
    public long lastPlaceTime = 0;

    public Counter tick;

    public float yawTest = 0;

    public int ticksSinceEat = 0;

    public Counter speedBBuf = new Counter();
    public Counter flyABuffer = new Counter();

    public Counter hitCounter = new Counter();

    public Counter noFallBBuffer = new Counter();

    public Counter flyBBuffer = new Counter();
    public Counter killAuraAbuffer = new Counter();

    public Counter speedDbuf = new Counter();

    public double lastDiff = 0;

    public Counter speedEBuffer = new Counter();

    public int ticksSinceSneak,SneakingTicks = 0;
    public int ticksSinceWeb,WebTicks = 0;

    public int ticksSinceBlocking,BlockingTicks = 0;

    public int ticksSinceSprint,SprintTicks = 0;

    public Counter sameYawBuffer = new Counter(1000);
    public Counter criticalBBuffer = new Counter();

    ConcurrentLinkedQueue<BetterInteger> cps_left = new ConcurrentLinkedQueue<>();
    //ConcurrentLinkedQueue<BetterInteger> cps_right = new ConcurrentLinkedQueue<>();
    double averageLeft,averageRight = 0;

    public FluxPlayer(Player p){
        player = p;
        PossibleMovedY = 0;
        lastGroundY = 0;
        violations = new HashMap<>();
        this.name = p.getName();
        this.p = p;
        this.ground = 0;
        this.air = 0;
        this.water = 0;
        this.lastWater = 0;
        this.clientGround = 0;
        this.clientAir = 0;
        this.lastX = 0;
        this.lastY = 0;
        this.lastZ = 0;
        this.ticksSinceHit = 0;
        this.hitPitch = 0;
        this.hitYaw = 0;
        ticksSinceHit = 0;
        tickExistSinceBreak = 0;
        tickExistSincePlace = 0;
        tick = new Counter();
        ladderTick = 0;
        tickExistSinceOnLadder = 0;
        lastDamageByEntity = 0;

    }

    public double getSensitivity(){
        float deltaPitch = Math.abs(this.deltaPitch);
        float lastDeltaPitch = Math.abs(this.lastDeltaPitch);
        float gcd = (float) getGcd(deltaPitch,lastDeltaPitch);
        double sens1 = Math.cbrt(0.8333 * gcd);
        double sens2 = ( 1.666 * sens1 ) - 0.3333;
        return sens2 * 200;
    }

    private double getGcd(double a,double b){
        if(a<b){
            return getGcd(b,a);
        }
        if(Math.abs(b) < 0.001){
            return a;
        }else{
            return getGcd(b,a - Math.floor(a/b) * b);
        }
    }

    public void CheckAverage(){
        for(BetterInteger i : cps_left){
            if( (System.currentTimeMillis() - i.d) >= 1000 * 10 ){
                cps_left.remove(i);
            }
        }
        //for(BetterInteger i : cps_right){
        //    if( (System.currentTimeMillis() - i.d) >= 1000 * 10 ){
        //        cps_right.remove(i);
        //    }
        //}

        if(left.getTick() > 0){
            cps_left.add( new BetterInteger(left.getTick()) );
        }
        //if(right.getTick() > 0){
        //    cps_right.add( new BetterInteger(right.getTick()) );
        //}
        checkAverageLeft();
        checkAverageRight();
    }

    public void checkAverageLeft(){
        double sum = 0.0;
        boolean isAllSame = true;
        for (BetterInteger number : cps_left) {
            sum += number.i;
        }
        averageLeft = sum / cps_left.size();
    }

    public void checkAverageRight(){
        //double sum = 0.0;
        //for (BetterInteger number : cps_right) {
        //    sum += number.i;
        //}
        //averageRight = sum / cps_right.size();
    }

    public double[] getAverageCPS(){
        if(averageLeft==0){
            averageLeft = left.getTick();
        }
        if(averageRight==0){
            //averageRight = right.getTick();
        }
        return new double[]{averageLeft, 0.0};
    }

    public int getPing() {
        String version = Bukkit.getServer().getClass().getPackage().getName().split("\\.")[3];
        try {
            Class<?> craftPlayerClass = Class.forName("org.bukkit.craftbukkit." + version + ".entity.CraftPlayer");
            Object craftPlayer = craftPlayerClass.cast(player);
            Class<?> entityPlayerClass = Class.forName("net.minecraft.server." + version + ".EntityPlayer");
            Object entityPlayer = craftPlayerClass.getMethod("getHandle").invoke(craftPlayer);
            int ping = (int) entityPlayerClass.getField("ping").get(entityPlayer);
            return ping;
        } catch (Exception e) {
            e.printStackTrace();
        }
        return 0; // Return 0 if the ping cannot be obtained
    }
    public boolean onLadder() {
        Material b = player.getLocation().clone().subtract(0.0D, 0.1D, 0.0D).getBlock().getType();
        return (b == Material.LADDER || b == Material.VINE || b == Material.WEB);
    }

    public Player getPlayer(){
        return p;
    }

    public String getName(){
        return name;
    }

    public int tval = 0;
    public ConcurrentHashMap<String,Integer> val = new ConcurrentHashMap<>();

    public int getVL(String check){
        if( check == null ){
            return tval;
        }
        if( violations.get( check ) == null ){
            violations.put( check , 0 );
        }
        return violations.get( check );
    }


    public int getCheckVL(String check){
        if( val.get( check ) == null ){
            val.put( check , 0);
        }
        return val.getOrDefault(check,0);
    }

    public boolean flag(String check){
        boolean a = false;
        if(getVL(check) >= Flux.instance.getCheck(check).banVL){
            violations.put(check,0);
            a = true;
        }
        violations.put(check,getVL( check ) + 1 );
        if(val.get(check) == null){
            val.put(check,0);
        }
        val.put(check,val.get(check) + 1);
        tval++;
        return a;
    }

}
