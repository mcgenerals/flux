package com.baguars.flux;

import com.baguars.flux.check.Check;
import com.baguars.flux.check.checks.combats.autoclicker.AutoClicker;
import com.baguars.flux.check.checks.combats.criticals.CriticalA;
import com.baguars.flux.check.checks.combats.criticals.CriticalB;
import com.baguars.flux.check.checks.combats.killaura.*;
import com.baguars.flux.check.checks.combats.reach.Reach;
import com.baguars.flux.check.checks.movements.fly.*;
import com.baguars.flux.check.checks.movements.motion.*;
import com.baguars.flux.check.checks.movements.speed.*;
import com.baguars.flux.check.checks.players.bot.Baritone;
import com.baguars.flux.check.checks.players.nofall.GroundSpoofCheck;
import com.baguars.flux.check.checks.players.nofall.NewNoFall;
import com.baguars.flux.check.checks.players.scaffold.PitchCheck;
import com.baguars.flux.check.checks.players.timer.TimerFast;
import com.baguars.flux.check.checks.players.nofall.WierdDataCheck;
import com.baguars.flux.check.checks.players.scaffold.YawCheck;
import com.baguars.flux.check.checks.players.timer.TimerSlow;
import com.baguars.flux.commands.FluxCommand;
import com.baguars.flux.listeners.InventoryListener;
import com.baguars.flux.listeners.JoinQuitListener;
import com.baguars.flux.listeners.PlayerListener;
import com.baguars.flux.listeners.PunishListener;
import com.baguars.flux.manager.FluxPlayer;
import com.baguars.flux.manager.Performance;
import com.comphenix.protocol.PacketType;
import com.comphenix.protocol.ProtocolLibrary;
import com.comphenix.protocol.events.*;
import org.bukkit.Bukkit;
import org.bukkit.configuration.file.FileConfiguration;
import org.bukkit.entity.Player;
import org.bukkit.plugin.PluginManager;
import org.bukkit.plugin.java.JavaPlugin;
import org.bukkit.scheduler.BukkitRunnable;

import java.util.ArrayList;
import java.util.concurrent.Executor;
import java.util.concurrent.Executors;

public final class Flux extends JavaPlugin {

    public static Flux instance;
    ArrayList<FluxPlayer> player = new ArrayList<>();
    public ArrayList<Check> checks = new ArrayList<>();
    public FileConfiguration config;
    public boolean isDebugMode = false; //FOR TEST SERVERS. IF YOU NEED TO CUSTOMIZE IT, USE FLAG EVENT
    public Executor packetThread = Executors.newSingleThreadExecutor();
    public boolean agree = false;

    @Override
    public void onEnable() {
        instance = this;
        saveDefaultConfig();
        config = getConfig();
        agree = config.getBoolean("agree");

        //Commands
        getCommand("flux").setExecutor( new FluxCommand() );

        checks = new ArrayList<>(); //RESET
        setup(0);
        protocolSetup();
        averageCPSTask();
    }

    public void averageCPSTask(){
        BukkitRunnable task = new BukkitRunnable() {
            @Override
            public void run() {
                for(FluxPlayer p : player){
                    p.CheckAverage();
                }
            }
        };

        // Schedule the task to run repeatedly with a delay of 0 ticks and a period of 20 ticks
        task.runTaskTimer(Flux.instance, 0, 20);
    }

    public void setup(int i){
        if(!agree){
            return;
        }
        //REGISTER LISTENERS
        PluginManager pm = getServer().getPluginManager();
        if(i==0) {
            pm.registerEvents(new JoinQuitListener(), this);
            pm.registerEvents(new PlayerListener(), this);
            pm.registerEvents(new InventoryListener(), this);
            pm.registerEvents(new PunishListener(),this);
        }
        // CHECKS
        //Combats
        checks.add( new AutoClicker());
        //checks.add( new RightClicker());

        checks.add( new YawDiffCheck());
        checks.add( new ZeroPitchCheck() );
        checks.add( new KeepSprintCheck());
        checks.add( new SameDeltaYaw() );
        checks.add( new MovementPatcher() );

        //checks.add( new TestBotCheck() );

        checks.add( new CriticalA() );
        checks.add( new CriticalB() );

        checks.add( new Reach() ); //It will paid check
        //Movements
        checks.add( new PredicationCheck() );
        checks.add( new DownwardCheck() );
        checks.add( new SmallDeltaYCheck() );


        checks.add( new SpeedCheck() );
        checks.add( new DeltaXZCheck() );
        checks.add( new YawAndSpeedCheck() );
        checks.add( new SpeedD() );
        checks.add( new Predication() );

        checks.add( new SimpleYSpeed() ); //MotionA
        checks.add( new WeirdDeltaYCheck() ); //MotionB
        checks.add( new WebSpeed() ); // MotionC
        checks.add( new NoSlow()); // MotionD
        //checks.add( new TestPred() ); i forgot but its from our dev's old anti-cheat
        //Players
        checks.add( new WierdDataCheck());
        checks.add( new GroundSpoofCheck() );
        checks.add( new NewNoFall() );

        checks.add( new YawCheck() );
        checks.add( new PitchCheck() );
        checks.add( new TimerFast() );
        checks.add( new TimerSlow() );
        checks.add( new Baritone() );




        //checks.add( new TestBotCheck());


        // REGISTER CHECKS
        checks(pm,i);
        for(Player p : Bukkit.getOnlinePlayers()){
            this.registerPlayer(p);
        }
    }

    @Override
    public void onDisable(){
        this.getServer().getScheduler().cancelTasks(this);
        ProtocolLibrary.getProtocolManager().removePacketListeners(this);
    }

    public Performance pf = new Performance();

    public void protocolSetup(){
        ProtocolLibrary.getProtocolManager().addPacketListener(new PacketAdapter(  this,
                ListenerPriority.NORMAL,
                PacketType.Play.Client.FLYING,
                PacketType.Play.Client.POSITION,
                PacketType.Play.Client.POSITION_LOOK) {
            @Override
            public void onPacketReceiving(PacketEvent event) {
                if(!agree){
                    return;
                }
                packetThread.execute(() ->{
                    long time = System.currentTimeMillis();
                    if ( event.getPacketType() == PacketType.Play.Client.FLYING || event.getPacketType() == PacketType.Play.Client.POSITION || event.getPacketType() == PacketType.Play.Client.POSITION_LOOK) {
                        Player player = event.getPlayer();
                        // Custom move logic here
                        double x = event.getPacket().getDoubles().read(0);
                        double y = event.getPacket().getDoubles().read(1);
                        double z = event.getPacket().getDoubles().read(2);
                        if( event.getPacketType() == PacketType.Play.Client.FLYING ){
                            FluxPlayer fp = getPlayer(player);
                            x = fp.lastLoc.getX();
                            y = fp.lastLoc.getY();
                            z = fp.lastLoc.getZ();
                        }
                        //run on async!
                        PlayerListener.onMove(player,x,y,z,time);
                    }
                });
            }

            @Override
            public void onPacketSending(PacketEvent event) {
                // No action needed in this example
            }
        });
    }

    public void checks(PluginManager pm,int i){
        for( Check c : checks ){
            String check = c.getName().toLowerCase();
            String type = c.getType().toLowerCase();
            String cate = c.getCategory().toString().toLowerCase();
            c.status = config.getBoolean("checks." + cate + "." + check + "." + type + ".status" );
            c.punish = config.getBoolean("checks." + cate + "." + check + "." + type + ".punish" );
            c.banVL = config.getInt("checks." + cate + "." + check + "." + type + ".vl" );
            if(i==0){pm.registerEvents( c , this );}
        }
    }

    public Check getCheck(String check){
        for(Check c : checks){
            String cs = c.getName()+"|"+c.getType();
            if(cs.equalsIgnoreCase(check)){
                return c;
            }
        }
        return null;
    }

    public FluxPlayer getPlayer(Player p){
        for(FluxPlayer fp : player){
            if( fp.getName().equalsIgnoreCase(p.getName()) ){
                return fp;
            }
        }
        return null;
    }

    public FluxPlayer registerPlayer(Player p){
        FluxPlayer fp = this.getPlayer( p );
        if( fp == null ){
            fp = new FluxPlayer( p );
            player.add( fp );
        }
        return fp;
    }

    public boolean removePlayer(Player p){
        FluxPlayer fp = getPlayer( p );
        if( fp != null ){
            player.remove( fp );
            return true;
        }
        return false;
    }

}
