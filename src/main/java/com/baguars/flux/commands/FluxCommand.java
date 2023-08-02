package com.baguars.flux.commands;

import com.baguars.flux.Flux;
import com.baguars.flux.inventory.MainMenu;
import com.baguars.flux.manager.FluxPlayer;
import org.bukkit.Bukkit;
import org.bukkit.command.Command;
import org.bukkit.command.CommandExecutor;
import org.bukkit.command.CommandSender;
import org.bukkit.entity.Player;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.concurrent.ConcurrentHashMap;

public class FluxCommand implements CommandExecutor {

    boolean isRead = false;

    @Override
    public boolean onCommand(CommandSender sender, Command command, String s, String[] args) {
        if(!( sender instanceof Player)){
            return true;
        }
        if( !sender.hasPermission("flux.admin") ){
            sender.sendMessage("§c[§6§lFlux§r§c]§c You don't have permission to do this!" );
            return true;
        }
        Player p = ((Player) sender).getPlayer();
        if( args.length == 0 ){
            sender.sendMessage(
                    "§b§m-§b§l▶◀§b§m§l---------------------§b§l▶◀§b§m-" +
                    "\n§6§lFlux AntiCheat§3§l " + Flux.instance.getDescription().getVersion() +
                    "\n§b§m-§b§l▶◀§b§m§l---------------------§b§l▶◀§b§m-§r\n " +
                            ( Flux.instance.agree ? "\n§b/flux §7- show helps" : "") +
                            ( Flux.instance.agree ?"\n§b/flux gui §7- open gui" : "" ) +
                            ( Flux.instance.agree ?"\n§b/flux debugmode §7- Toggles Flux debug mode. this is for test-server." : "" ) +
                            ( Flux.instance.agree ?"\n§b/flux verbose §7- Toggles Flux verbose" : "" ) +
                            ( Flux.instance.agree ?"\n§b/flux performance §7- To check Average time taken to run the check." : "" ) +
                            ( Flux.instance.agree ?"\n§b/flux reset <player> §7- To reset player's vl." : "" ) +
                            ( Flux.instance.agree ?"\n§b/flux ping <player> §7- To test player's ping." : "" ) +
                            ( Flux.instance.agree ?"\n§b/flux cps <player> §7- To test player's average cps." : "" ) +

                            ( !Flux.instance.agree ? "\n§b/flux tos §7- show tos." : "") +
                            ( !Flux.instance.agree ? "\n§b/flux agree §7- agree to tos. by running that you agreed to our tos." : "") +
                            ( Flux.instance.agree ?"\n§b/flux reload §7- reload config" : "" ) +
                    "\n \n§b§m-§b§l▶◀§b§m§l---------------------§b§l▶◀§b§m-");
        }else if( args.length == 1){
            if( !Flux.instance.agree ){
                if( args[0].equalsIgnoreCase("tos") ){
                    isRead = true;
                    sender.sendMessage("§c[§6§lFlux§r§c]§a Terms Of Service.\nFluxAntiCheat (hereinafter referred to as \"the Software\") prohibits any modification, duplication, resale, or re-upload for commercial purposes without the permission of MCGenerals (hereinafter referred to as \"the Rights Holder\"). However, the Software allows modification, duplication, and re-upload for educational purposes, including personal learning. The Rights Holder assumes no responsibility for any troubles or issues arising from the use of this Software.\n" + "\n" + "Furthermore, any projects that utilize code from this Software must be released under the MIT license or another license that permits code reuse, unless specifically permitted by the Rights Holder. It is mandatory to include the name of this Software, the utilized portions or links (mcgenerals.com), in the published source code.");
                }else if( args[0].equalsIgnoreCase("agree") ){
                    if(isRead) {
                        sender.sendMessage("§c[§6§lFlux§r§c]§a Thanks for agreed to our TOS! Starting Flux...");
                        Flux.instance.config.set("agree", true);
                        Flux.instance.saveConfig();
                        Flux.instance.reloadConfig();
                        Flux.instance.config = Flux.instance.getConfig();
                        Flux.instance.agree = true;
                        Flux.instance.setup(0);
                        sender.sendMessage("§c[§6§lFlux§r§c]§a Flux start protecting your server.");
                    }else{
                        sender.sendMessage("§c[§6§lFlux§r§c]§a Please read TOS first. /flux tos");
                    }
                }else {
                    sender.sendMessage("§c[§6§lFlux§r§c]§c Unknown Command!");
                }
            }else if( args[0].equalsIgnoreCase("verbose") && Flux.instance.agree ){
                Flux.instance.getPlayer(p).flagEnabled = !Flux.instance.getPlayer(p).flagEnabled;
                p.sendMessage("§c[§6§lFlux§r§c]§b Verbose is " + ( Flux.instance.getPlayer(p).flagEnabled ? "§aEnabled" : "§cDisabled" ) );
            }else if( args[0].equalsIgnoreCase("performance") && Flux.instance.agree ){
                long dl = Flux.instance.pf.check();
                p.sendMessage("§c[§6§lFlux§r§c]§b The average time taken to perform the check is " + dl + "ms." );
            }else if( args[0].equalsIgnoreCase("gui") && Flux.instance.agree ){
                MainMenu m = new MainMenu();
                p.openInventory(m.i.getInventory());
            }else if( args[0].equalsIgnoreCase("debugmode") && Flux.instance.agree ){
                Flux.instance.isDebugMode = !Flux.instance.isDebugMode;
                p.sendMessage("§c[§6§lFlux§r§c]§b Debug Mode is " + ( Flux.instance.isDebugMode ? "§aEnabled" : "§cDisabled" ) );
            }else if( args[0].equalsIgnoreCase("reload") && Flux.instance.agree ){
                p.sendMessage("§c[§6§lFlux§r§c]§b Config Reloading..." );
                Flux.instance.reloadConfig();
                Flux.instance.config = Flux.instance.getConfig();
                Flux.instance.checks = new ArrayList<>(); //RESET
                Flux.instance.setup(1);
                p.sendMessage("§c[§6§lFlux§r§c]§b Config Reloaded!" );
            }else {
                sender.sendMessage("§c[§6§lFlux§r§c]§c Unknown Command!");
            }
        }else if( args.length == 2  && Flux.instance.agree ){
            if( args[0].equalsIgnoreCase("reset") ){
                Player tp = Bukkit.getPlayer( args[1] );
                if( tp == null ){
                    p.sendMessage("§c[§6§lFlux§r§c]§c " + args[1] + " is offline" );
                }else {
                    FluxPlayer target = Flux.instance.getPlayer( tp );
                    target.violations = new HashMap<>();
                    target.tval = 0;
                    target.val = new ConcurrentHashMap<>();
                    p.sendMessage("§c[§6§lFlux§r§c]§a " + args[1] + "'s VL Data is now reset." );
                }
            }else if( args[0].equalsIgnoreCase("ping") ){
                Player tp = Bukkit.getPlayer( args[1] );
                if( tp == null ){
                    p.sendMessage("§c[§6§lFlux§r§c]§c " + args[1] + " is offline" );
                }else {
                    p.sendMessage("§c[§6§lFlux§r§c]§a " + args[1] + "'s ping is " + Flux.instance.getPlayer( tp ).getPing() + "ms." );
                }
            }else if( args[0].equalsIgnoreCase("cps") ){
                Player tp = Bukkit.getPlayer( args[1] );
                FluxPlayer fp = Flux.instance.getPlayer( tp );
                if( tp == null ){
                    p.sendMessage("§c[§6§lFlux§r§c]§c " + args[1] + " is offline" );
                }else {
                    p.sendMessage("§c[§6§lFlux§r§c]§a " + args[1] + "'s cps is " + ( (Math.floor(fp.getAverageCPS()[0] * 10)) / 10 ) + " in last 10seconds.");
                }
            }else {
                sender.sendMessage("§c[§6§lFlux§r§c]§c Unknown Command!");
            }
        }
        return true;
    }
}
