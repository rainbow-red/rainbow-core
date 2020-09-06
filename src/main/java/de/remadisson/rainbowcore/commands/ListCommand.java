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
import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

public class ListCommand implements Command {

    private ProxyServer server;
    private Logger logger;

    @Inject
    public ListCommand(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Override
    public void execute(CommandSource sender, @NotNull String[] args){
            ArrayList<String> allowedServers = new ArrayList<>();
            if(sender instanceof Player){
                for(RegisteredServer server : server.getAllServers()){
                    if(sender.hasPermission("core.glist." + server.getServerInfo().getName()) || sender.hasPermission("core.*") || sender.hasPermission("core.list.*")){
                        allowedServers.add(server.getServerInfo().getName().toLowerCase());
                    }
                }

                if(sender.hasPermission("core.list.all")){
                    allowedServers.add("all");
                }

                if(allowedServers.isEmpty()){
                    sender.sendMessage(TextComponent.of(files.message_no_permission));
                    return;
                }
            } else {
                allowedServers.addAll(server.getAllServers().stream().map(RegisteredServer::getServerInfo).map(ServerInfo::getName).collect(Collectors.toList()));
                allowedServers.add("all");
            }

            if(args.length == 1){
                String one = args[0];

                switch(one.toLowerCase()){
                    case "help": {
                        sender.sendMessage(TextComponent.of(files.prefix + "§eHelp for §aGlist§8:"));
                        sender.sendMessage(TextComponent.of(files.prefix + "§f- /glist §7<§eserver§8/all§7>"));
                        break;
                    }

                    case "all": {
                        if(sender.hasPermission("core.list.all")){
                            sender.sendMessage(TextComponent.of(files.prefix + "§eThere are §a" + server.getPlayerCount() + " §eplayer(s) online§8:"));
                            for(RegisteredServer server : server.getAllServers()){
                                if(server.getPlayersConnected().size() > 0) {
                                    sender.sendMessage(TextComponent.of(files.prefix + "§f- §a" + server.getServerInfo().getName() + " §3(" + server.getPlayersConnected().size() + ")§8: §7" + appendPlayers(server.getPlayersConnected().stream().map(Player::getUsername).collect(Collectors.toList()))));
                                }
                            }
                        }
                        break;
                    }

                    default:
                        if(allowedServers.contains(one.toLowerCase())){
                            if(server.getServer(one).isPresent()) {
                                RegisteredServer listedServer = server.getServer(one).get();
                                sender.sendMessage(TextComponent.of(files.prefix + "§a" + listedServer.getServerInfo().getName() + " §3(" + listedServer.getPlayersConnected().size() + ")§8: §7" + appendPlayers(listedServer.getPlayersConnected().stream().map(Player::getUsername).collect(Collectors.toList()))));
                            }else{
                                sender.sendMessage(TextComponent.of(files.prefix + "§cThis Server is currently not available!"));
                            }
                        } else {
                            sender.sendMessage(TextComponent.of(files.message_no_permission));
                            break;
                        }
                        return;
                }
            } else if(args.length == 0){
                if(sender instanceof Player) {
                    RegisteredServer listedServer = ((Player) sender).getCurrentServer().get().getServer();
                    sender.sendMessage(TextComponent.of(files.prefix + "§a" + listedServer.getServerInfo().getName() + " §3(" + listedServer.getPlayersConnected().size() + ")§8: §7" + appendPlayers(listedServer.getPlayersConnected().stream().map(Player::getUsername).collect(Collectors.toList()))));
                } else {
                    sender.sendMessage(TextComponent.of(files.prefix + "§dOof"));
                }
            } else {
                sender.sendMessage(TextComponent.of(files.prefix + "§eHelp for §aGlist§8:"));
                sender.sendMessage(TextComponent.of(files.prefix + "§f- /glist §7<§eserver§8/all§7>"));
            }
    }

    @Override
    public List<String> suggest(@NotNull CommandSource sender, String[] args){
        List<String> suggestions = new ArrayList<>();
        List<String> instances = new ArrayList<>();
        instances.addAll(server.getAllServers().stream().map(RegisteredServer::getServerInfo).map(ServerInfo::getName).collect(Collectors.toList()));
        instances.add("all");

        if(args.length == 0) {
            suggestions.addAll(instances);
        } else if(args.length == 1){
            instances.stream().forEach(server -> {
                if(server.startsWith(args[0].toLowerCase())) suggestions.add(server);
            });
        }

        return suggestions;
    }

    public String appendPlayers(List<String> players){
        if(players.isEmpty()) return "";

        String appendedPlayers = players.get(0);

            for(String player : players){
                if(player != players.get(0)) {
                    appendedPlayers += ", " + player;
                }
            }

        return appendedPlayers;
    }
}
