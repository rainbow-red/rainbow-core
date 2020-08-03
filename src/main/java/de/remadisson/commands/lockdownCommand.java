package de.remadisson.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import de.remadisson.files;
import net.kyori.adventure.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;
import java.util.Map;

public class lockdownCommand implements Command {

    /*
     *  Lockdown Command Syntax: /lockdown <set/unset/add/remove/help> <global/server-name/player>
     *
     *  Permissons: core.lockdown.status, core.lockdown.player, core.lockdown.global, core.lockdown.[server-name], core.lockdown.join.global/[server-name]
     *
     * */

    private final String prefix = files.prefix;

    @Override
    public void execute(CommandSource sender, @NonNull String[] args) {
        if(args.length == 0){

            if(!sender.hasPermission("core.lockdown.status") && !sender.hasPermission("core.lockdown.*")){
                sender.sendMessage(TextComponent.of(files.message_no_permission));
                return;
            }
                sender.sendMessage(TextComponent.of(prefix + "§eThe current §cLockdown§7-§6Statuses §eare: "));

                int locked = 0, current = 0;
                boolean dots = false;

                sender.sendMessage(TextComponent.of(prefix + "§bGlobal §f: " + (!files.lockdown.get("global") ? "§aunlocked" : "§clocked")));

                for(Map.Entry<String, Boolean> entry : files.lockdown.entrySet()){

                    if(entry.getValue()) locked++;

                    if(!entry.getKey().equalsIgnoreCase("global")) {
                        if (current < 10) {
                            current++;
                            sender.sendMessage(TextComponent.of(prefix + "§b" + entry.getKey() + "§f : " + (!entry.getValue() ? "§aunlocked" : "§clocked")));
                        } else {
                            if (!dots) {
                                dots = true;
                                sender.sendMessage(TextComponent.of(prefix + "§e... §9" + (files.lockdown.size() - current) + " §emore Servers"));
                            }
                        }
                    }
                }
                sender.sendMessage(TextComponent.of(prefix + "§eThere are §a" + locked + "§7/§2" + files.lockdown.size() + " §eServers locked!"));

        } else if(args.length == 1){
            String instruction = args[0].toLowerCase().trim();

            switch(instruction){

                /*
                 * Needed Permission core.lockdown.[server-name] or core.lockdown.global or core.lockdown.*
                 */
                case "set":
                case "unset": {

                    //Getting the Servers, that can be used in context with lockdown.
                    final String[] servers = {""};
                    files.lockdown.entrySet().stream().forEach(entry -> {
                        if(sender.hasPermission("core.lockdown." + entry.getKey().toLowerCase()) || sender.hasPermission("core.lockdown.*")){
                            String cap = (entry.getValue() ? "§c" : "§a") + entry.getKey().substring(0,1).toUpperCase() + entry.getKey().substring(1);
                            if(servers[0] == ""){
                                servers[0] = cap;
                            } else {
                                servers[0] += "§r, " + cap;
                            }
                        }
                    });

                    // Checking if the String is empty, if true: The Player has no Permisson for that command!
                    if(servers[0] == ""){
                        sender.sendMessage(TextComponent.of(files.message_no_permission));
                        return;
                    }

                    //Sending the Callback
                    sender.sendMessage(TextComponent.of(prefix + "§cUsage: §e/lockdown set/unset " + servers[0]));
                    return;
                }

                /**
                 *
                 */
                case "remove":
                case "add": {
                    //TODO Adding Lockdown information into UserManagingAPI or saving local;
                }

                /**
                 *
                 */
                default:
                case "help": {}

            }
        }
    }
}
