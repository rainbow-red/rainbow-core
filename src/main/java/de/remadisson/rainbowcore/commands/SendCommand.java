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
import java.util.Arrays;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

public class SendCommand implements Command {

    private ProxyServer server;
    private Logger logger;

    @Inject
    public SendCommand(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Override
    public void execute(CommandSource sender, @NotNull String[] args){
        List<String> allowedServer = new ArrayList<>(server.getAllServers()
                .stream()
                .map(RegisteredServer::getServerInfo)
                .map(ServerInfo::getName)
                .filter(filter -> sender.hasPermission("core.*") ||
                        sender.hasPermission("core.send.*") ||
                        sender.hasPermission("core.send." + filter.toLowerCase()))
                .collect(Collectors.toList()));

                allowedServer.addAll(Arrays.asList("all", "current"));

            if(allowedServer.isEmpty()){
                sender.sendMessage(TextComponent.of(files.message_no_permission));
                return;
            }

            if(args.length == 2){

                String one = args[0];
                String two = args[1];

                switch(one.toLowerCase()){
                    case "current": {
                        if(!(sender instanceof Player)){
                            sender.sendMessage(TextComponent.of(files.prefix + "You're behindert!"));
                            return;
                        }

                        Player player = (Player) sender;

                        if(!sender.hasPermission("core.send.current")){
                            sender.sendMessage(TextComponent.of(files.message_no_permission));
                            return;
                        }

                        if(!isServer(two)){
                            sender.sendMessage(TextComponent.of(files.prefix + "§cThere is no server named §4" + two));
                            return;
                        }

                        if(!allowedServer.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(two.toLowerCase())){
                            sender.sendMessage(TextComponent.of(files.message_no_permission));
                            return;
                        }

                        Optional<RegisteredServer> target = server.getServer(two);

                        if(!target.isPresent()){
                            sender.sendMessage(TextComponent.of(files.prefix + "§cThe server §4" + two + " §cis currently not available!"));
                            return;
                        }

                        if(!sender.hasPermission("core.server." + target.get().getServerInfo().getName()) && !sender.hasPermission("core.server.*") && !sender.hasPermission("core.*")){
                            sender.sendMessage(TextComponent.of(files.message_no_permission));
                            return;
                        }

                        int count = player.getCurrentServer().get().getServer().getPlayersConnected().size();
                        files.pool.execute(() -> {
                            player.getCurrentServer().get().getServer().getPlayersConnected().forEach(toConnect -> {

                                toConnect.createConnectionRequest(target.get()).connect();
                                toConnect.sendMessage(TextComponent.of(files.prefix + "§eYou have been moved to §a" + target.get().getServerInfo().getName()));
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });

                            sender.sendMessage(TextComponent.of(files.prefix + "§eYou have send §3" + count + " Players §eto §a" + target.get().getServerInfo().getName()));
                        });

                        break;
                    }

                    case "all": {

                        if(!sender.hasPermission("core.send.all")){
                            sender.sendMessage(TextComponent.of(files.message_no_permission));
                            return;
                        }

                        if(!isServer(two)){
                            sender.sendMessage(TextComponent.of(files.prefix + "§cThere is no server named §4" + two));
                            return;
                        }

                        if(!allowedServer.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(two.toLowerCase())){
                            sender.sendMessage(TextComponent.of(files.message_no_permission));
                            return;
                        }

                        Optional<RegisteredServer> target = server.getServer(two);

                        if(!target.isPresent()){
                            sender.sendMessage(TextComponent.of(files.prefix + "§cThe server §4" + two + " §cis currently not available!"));
                            return;
                        }

                        if(!sender.hasPermission("core.server." + target.get().getServerInfo().getName()) && !sender.hasPermission("core.server.*") && !sender.hasPermission("core.*")){
                            sender.sendMessage(TextComponent.of(files.message_no_permission));
                            return;
                        }

                        int count = server.getAllPlayers().size();
                        files.pool.execute(() -> {
                            server.getAllPlayers().forEach(toConnect -> {

                                toConnect.createConnectionRequest(target.get()).connect();
                                toConnect.sendMessage(TextComponent.of(files.prefix + "§eYou have been moved to §a" + target.get().getServerInfo().getName()));
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });

                            sender.sendMessage(TextComponent.of(files.prefix + "§eYou have send §3" + count + " Players §eto §a" + target.get().getServerInfo().getName()));
                        });
                        break;
                    }

                    default:

                        List<Player> selectedPlayers = new ArrayList<>();

                        if(!isOnlinePlayer(one)) {
                            if (!isServer(one)) {
                                sender.sendMessage(TextComponent.of(files.prefix + "§cThere is no server named §4" + two));
                                return;
                            }

                            if (!allowedServer.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(one)) {
                                sender.sendMessage(TextComponent.of(files.message_no_permission));
                                return;
                            }

                            Optional<RegisteredServer> fromServer = server.getServer(one.toLowerCase());

                            if (!fromServer.isPresent()) {
                                sender.sendMessage(TextComponent.of(files.prefix + "§cThe server §4" + one + " §cis currently not available!"));
                                return;
                            }

                            if (!sender.hasPermission("core.sever." + fromServer.get().getServerInfo().getName())) {
                                sender.sendMessage(TextComponent.of(files.message_no_permission));
                                return;
                            }

                            selectedPlayers.addAll(fromServer.get().getPlayersConnected());

                        } else {
                            if(!sender.hasPermission("core.send.player") && !sender.hasPermission("core.send." + two) && !sender.hasPermission("core.send.*") && !sender.hasPermission("core.*")){
                                sender.sendMessage(TextComponent.of(files.message_no_permission));
                                return;
                            }

                            Player targetPlayer = server.getPlayer(one).get();

                            if(targetPlayer == sender){
                                sender.sendMessage(TextComponent.of(files.prefix + "§cYou cannot send yourself!"));
                                return;
                            }

                            selectedPlayers.add(targetPlayer);
                        }

                        if(selectedPlayers.isEmpty()){
                            sender.sendMessage(TextComponent.of(files.prefix + "§cNo players selected to send to a different server!"));
                            return;
                        }

                        RegisteredServer fromServer = selectedPlayers.get(selectedPlayers.size()-1).getCurrentServer().get().getServer();

                        if(!isServer(two)){
                            sender.sendMessage(TextComponent.of(files.prefix + "§cThere is no server named §4" + two));
                            return;
                        }

                        if(!allowedServer.stream().map(String::toLowerCase).collect(Collectors.toList()).contains(two) && !sender.hasPermission("core.send.player")){
                            sender.sendMessage(TextComponent.of(files.message_no_permission));
                            return;
                        }

                        Optional<RegisteredServer> targetServer = server.getServer(two.toLowerCase());

                        if(!targetServer.isPresent()){
                            sender.sendMessage(TextComponent.of(files.prefix + "§cThe server §4" + two + " §cis currently not available!"));
                            return;
                        }

                        if(fromServer.getServerInfo().getName().equalsIgnoreCase(targetServer.get().getServerInfo().getName())){
                            sender.sendMessage(TextComponent.of(files.prefix + "§cYou cannot send Users from §c" + fromServer.getServerInfo().getName() + " §cto §4" + targetServer.get().getServerInfo().getName()));
                            return;
                        }

                        if(!sender.hasPermission("core.sever." + targetServer.get().getServerInfo().getName()) && !sender.hasPermission("core.server.*") && !sender.hasPermission("core.*")){
                            sender.sendMessage(TextComponent.of(files.message_no_permission));
                            return;
                        }

                        int count = selectedPlayers.size();
                        files.pool.execute(() -> {
                            selectedPlayers.forEach(toConnect -> {

                                toConnect.createConnectionRequest(targetServer.get()).connect();
                                toConnect.sendMessage(TextComponent.of(files.prefix + "§eYou have been moved to §a" + targetServer.get().getServerInfo().getName()));
                                try {
                                    Thread.sleep(100);
                                } catch (InterruptedException e) {
                                    e.printStackTrace();
                                }
                            });

                            sender.sendMessage(TextComponent.of(files.prefix + "§eYou have send §3" + count + " Players §efrom §2" + fromServer.getServerInfo().getName() + " §eto §a" + targetServer.get().getServerInfo().getName()));
                        });

                }
            } else {
                sender.sendMessage(TextComponent.of(files.prefix + "§eHelp for §aSend§8:"));
                sender.sendMessage(TextComponent.of(files.prefix + "§f- §e/send §7<§eplayer§7/§eserver§7/§eall§7/§ecurrent§7> §7<§eserver§7>"));
            }
    }

    @Override
    public List<String> suggest(@NotNull CommandSource sender, String[] args){
        List<String> suggestions = new ArrayList<>();

        List<String> allowedServer = new ArrayList<>(server.getAllServers()
                .stream()
                .map(RegisteredServer::getServerInfo)
                .map(ServerInfo::getName)
                .filter(filter -> sender.hasPermission("core.send.*") || sender.hasPermission("core.*") || sender.hasPermission("core.send." + filter))
                .collect(Collectors.toList()));



        if(args.length == 0){
            if(sender.hasPermission("core.send.current")) allowedServer.add("current");
            if(sender.hasPermission("core.send.all")) allowedServer.add("all");
            suggestions.addAll(allowedServer);
        }

        if(args.length == 1) {

            if(sender.hasPermission("core.send.current")) allowedServer.add("current");
            if(sender.hasPermission("core.send.all")) allowedServer.add("all");

            if(args[0].length() > 2){
                suggestions.addAll(server.getAllPlayers()
                        .stream()
                        .map(Player::getUsername)
                        .filter(filter-> filter.toLowerCase().startsWith(args[0].toLowerCase()))
                        .collect(Collectors.toList()));

            }

            suggestions.addAll(allowedServer.stream()
                    .filter(filter -> filter.toLowerCase().startsWith(args[0].toLowerCase()))
                    .collect(Collectors.toList()));
        }

        if(args.length == 2){

            allowedServer.remove("all");
            allowedServer.remove("current");

            suggestions.addAll(allowedServer.stream()
                    .filter(filter -> filter.toLowerCase().startsWith(args[1].toLowerCase()) && !filter.equalsIgnoreCase(args[0]))
                    .collect(Collectors.toList()));
        }
        return suggestions;
    }


    public boolean isServer(String potentialServer){
        return server.getAllServers()
                .stream()
                .map(RegisteredServer::getServerInfo)
                .map(ServerInfo::getName)
                .map(String::toLowerCase)
                .collect(Collectors.toList())
                .contains(potentialServer.toLowerCase());
    }

    public boolean isOnlinePlayer(String name){
        return server.getPlayer(name).isPresent();
    }
}
