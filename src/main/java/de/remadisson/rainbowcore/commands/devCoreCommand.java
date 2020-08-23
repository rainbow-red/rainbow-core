package de.remadisson.rainbowcore.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.tree.CommandNode;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.remadisson.rainbowcore.api.UserDataAPI;
import de.remadisson.rainbowcore.files;
import de.remadisson.rainbowcore.user.enums.UserTablist;
import de.remadisson.rainbowcore.user.instances.User;
import net.kyori.adventure.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.Collection;

public class devCoreCommand implements Command {

    private final String prefix = files.prefix;

    @Inject
    private final ProxyServer proxyServer;
    private final Logger logger;

    @Inject
    public devCoreCommand(ProxyServer server, Logger logger) {
        proxyServer = server;
        this.logger = logger;
    }

    /*

        devCore-Command Syntax: devCore <tablist/user>
                                        tablist <set, info> <Player> <MINIMAL, STANDART, EXTENDED>
                                        user <Name or UUID> <info, load, unload> <--unsaved>

        Permissions: core.dev.tablist
                                    .set
                                    .info
                                    .*
                     core.dev.user
                                    .info
                                    .load
                                    .save
                                    .*

     */

    @Override
    public void execute(CommandSource sender, @NonNull String[] args){
            if(!sender.hasPermission("core.dev.*") &&
                    !sender.hasPermission("core.dev.tablist.*") &&
                    !sender.hasPermission("core.dev.user.*") &&
                    !sender.hasPermission("core.dev.tablist.set") &&
                    !sender.hasPermission("core.dev.tablist.info") &&
                    !sender.hasPermission("core.dev.user.info") &&
                    !sender.hasPermission("core.dev.user.load") &&
                    !sender.hasPermission("core.dev.user.save")){
                sender.sendMessage(TextComponent.of(files.message_no_permission));
                return;
            }

            if(args.length == 0){
                sendHelp(sender);
                return;
            }

            switch(args[0].toLowerCase()){
                case "tablist": {

                    if(args.length == 1) {
                       sendHelp(sender);
                        break;


                    } else if(args.length == 3){

                        Player player = proxyServer.getPlayer(args[2]).get();

                        if(!player.isActive()){
                            sender.sendMessage(TextComponent.of(prefix + "§cThe Player §4" + args[2].toUpperCase() + " §cis currently offline!"));
                            break;
                        }

                        switch(args[1].toLowerCase()){
                            case "set": {
                                sendHelp(sender);
                                break;
                            }

                            case "info": {
                                try {
                                    sender.sendMessage(TextComponent.of(prefix + "§eThe User §6" + player.getUsername() + "§7(" + player.getUniqueId() + ") §ehas the §6" + new UserDataAPI().getUser(player.getUniqueId()).getSettings().getTablist().toString() + " §eTablist"));
                                } catch (SQLException throwables) {
                                    throwables.printStackTrace();
                                }
                                return;
                            }

                            default: {
                                sendHelp(sender);
                            }
                        }

                    } else if(args.length == 4){
                      Player player = proxyServer.getPlayer(args[2]).get();

                      if(!player.isActive()){
                          sender.sendMessage(TextComponent.of(prefix + "§cThe Player §4" + args[2].toUpperCase() + " §cis currently offline!"));
                          break;
                      }

                      UserTablist tablist;
                      try {
                          tablist = UserTablist.valueOf(args[3].toUpperCase());
                      } catch(IllegalArgumentException ex){
                          sender.sendMessage(TextComponent.of(prefix + "§cThere is no tablist named " + args[3].toUpperCase()));
                          return;
                      }

                    switch(args[1].toLowerCase()){
                        case "set": {
                            UserDataAPI api = new UserDataAPI();
                            User user = api.getUser(player.getUniqueId());
                            try {
                                user.getSettings().setTablist(tablist);
                            } catch (SQLException throwables) {
                                throwables.printStackTrace();
                            }

                            sender.sendMessage(TextComponent.of(prefix + "§eThe Tablist of §6" + player.getUsername() + "§e is now §6" + tablist.name()));
                            return;
                        }

                        case "info": {
                            sendHelp(sender);
                        }

                        default: {
                            sendHelp(sender);
                        }
                    }

                    } else {
                        sendHelp(sender);
                    }

                    break;
                }
                default:
                sendHelp(sender);
            }
    }

    private void sendHelp(CommandSource sender){
        sender.sendMessage(TextComponent.of(prefix + "§eHelp for devCore Command"));
    }

}
