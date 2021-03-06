package de.remadisson.rainbowcore.commands;

import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import com.velocitypowered.api.proxy.server.ServerInfo;
import de.remadisson.rainbowcore.files;
import de.remadisson.rainbowcore.velocity;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;

import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class ListCommand implements RawCommand {

    private ProxyServer server = velocity.getProxy();

    @Override
    public void execute(final Invocation invocation){

        final CommandSource sender = invocation.source();
        final String[] args = invocation.arguments().split(" ");

            ArrayList<String> allowedServers = new ArrayList<>();
            if(sender instanceof Player){
                for(RegisteredServer server : server.getAllServers()){
                    if(sender.hasPermission("core.glist." + server.getServerInfo().getName().toLowerCase()) || sender.hasPermission("core.*") || sender.hasPermission("core.glist.*")){
                        allowedServers.add(server.getServerInfo().getName().toLowerCase());
                    }
                }

                if(sender.hasPermission("core.glist.all")){
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
                        sender.sendMessage(TextComponent.of(files.prefix + "§f- §e/glist §7<§eserver§7/§eall§7>"));
                        break;
                    }

                    case "all": {
                        if(sender.hasPermission("core.glist.all")){
                            sender.sendMessage(TextComponent.of(files.prefix + "§eThere are §a" + server.getPlayerCount() + " §eplayer(s) online§8:"));
                            for(RegisteredServer server : server.getAllServers()){
                                if(server.getPlayersConnected().size() > 0) {
                                    sender.sendMessage(TextComponent.of(files.prefix + "§f- §a" + server.getServerInfo().getName() + " §3(" + server.getPlayersConnected().size() + ")§8: §7" + appendPlayers(server.getPlayersConnected().stream().map(Player::getUsername).collect(Collectors.toList()))));
                                }
                            }
                        } else {
                            sender.sendMessage(TextComponent.of(files.message_no_permission));
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
                            if(!server.getAllServers().stream().map(RegisteredServer::getServerInfo).map(ServerInfo::getName).map(String::toLowerCase).collect(Collectors.toList()).contains(one.toLowerCase())){
                                sender.sendMessage(TextComponent.of(files.prefix + "§cThere is no server named §4" + one.toLowerCase()));
                                return;
                            }
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
                sender.sendMessage(TextComponent.of(files.prefix + "§f- /glist §7<§eserver§7/all§7>"));
            }
    }

    @Override
    public List<String> suggest(@NotNull CommandSource sender, String[] args){
        List<String> suggestions = new ArrayList<>();
        List<String> instances = new ArrayList<>();
        instances.addAll(server.getAllServers().stream().map(RegisteredServer::getServerInfo).map(ServerInfo::getName).filter(filter -> sender.hasPermission("core.glist." + filter) || sender.hasPermission("core.*") || sender.hasPermission("core.glist.*")).collect(Collectors.toList()));
        if(sender.hasPermission("core.glist.all") || sender.hasPermission("core.glist.*") || sender.hasPermission("core.*")) {
            instances.add("all");
        }

        if(args.length == 0) {
            suggestions.addAll(instances);
        } else if(args.length == 1){
            suggestions.addAll(instances.stream().filter(filter -> filter.toLowerCase().startsWith(args[0].toLowerCase())).collect(Collectors.toList()));
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
