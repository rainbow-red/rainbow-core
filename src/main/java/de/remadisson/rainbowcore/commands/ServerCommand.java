package de.remadisson.rainbowcore.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import de.remadisson.rainbowcore.files;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.ArrayList;
import java.util.Collection;
import java.util.Collections;
import java.util.List;
import java.util.stream.Collectors;

public class ServerCommand implements Command {

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public ServerCommand(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Override
    public void execute(CommandSource sender, @NotNull String[] args){
        ArrayList<String> allowedServer = new ArrayList<>();
            for(RegisteredServer server : server.getAllServers()){
                String serverName = server.getServerInfo().getName().toLowerCase();
                if(sender.hasPermission("core.server." + serverName) || sender.hasPermission("core.server.*") || sender.hasPermission("core.*")){
                    allowedServer.add(serverName);
                }
        }

            if(allowedServer.isEmpty()) {
                sender.sendMessage(TextComponent.of(files.message_no_permission));
                return;
            }

            if(args.length == 1){
                String one = args[0];

                switch(one.toLowerCase()){
                    case "help":{
                        sendHelp(sender);
                    }

                    default: {
                        if(!allowedServer.contains(one.toLowerCase())){
                            if(!server.getAllServers().stream().map(RegisteredServer::getServerInfo).map(ServerInfo::getName).map(String::toLowerCase).collect(Collectors.toList()).contains(one.toLowerCase())){
                                sender.sendMessage(TextComponent.of(files.prefix + "§cThere is no server named §4" + one.toLowerCase()));
                                return;
                            }

                            sender.sendMessage(TextComponent.of(files.message_no_permission));
                            return;
                        }

                        if(!server.getServer(one.toLowerCase()).isPresent()){
                            sender.sendMessage(TextComponent.of(files.prefix + "§cThis Server is currently not available!"));
                            return;
                        }

                        if(sender instanceof Player){
                            RegisteredServer newServer = server.getServer(one.toLowerCase()).get();
                            Player serverPlayer = (Player) sender;
                            sender.sendMessage(TextComponent.of(files.prefix + "§eConnecting to §a" + newServer.getServerInfo().getName()));
                            serverPlayer.createConnectionRequest(newServer).connect();

                        } else {
                            sender.sendMessage(TextComponent.of(files.prefix + "§4DAMN §cagain you....! - §7Why are you always doing this to me?!"));
                        }
                    }
                }
            } else {
                sendHelp(sender);
            }

    }

    @Override
    public List<String> suggest(@NotNull CommandSource sender, String[] args) {
        List<String> suggestions = new ArrayList<>();
        List<String> instances = new ArrayList<>();
        instances.addAll(server.getAllServers().stream().map(RegisteredServer::getServerInfo).map(ServerInfo::getName).filter(filter -> sender.hasPermission("core.server." + filter) || sender.hasPermission("core.*") || sender.hasPermission("core.server.*")).collect(Collectors.toList()));

        if (args.length == 0) {
            suggestions.addAll(instances);
        } else if (args.length == 1) {
            suggestions.addAll(instances.stream().filter(filter -> filter.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList()));
        }

        return suggestions;

    }
        public boolean sendHelp(CommandSource sender){
        sender.sendMessage(TextComponent.of(files.prefix + "§eHelp for §aServer§8:"));
        sender.sendMessage(TextComponent.of(files.prefix + "§f- §e/server §7<§eserver§7>"));
        return true;
    }
}
