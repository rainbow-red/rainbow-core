package de.remadisson.rainbowcore.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.command.RawCommand;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.remadisson.rainbowcore.files;
import de.remadisson.rainbowcore.velocity;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.HashMap;
import java.util.Map;
import java.util.TreeMap;

public class HubCommand implements RawCommand {

    private String prefix = files.prefix;

    private final ProxyServer proxyServer = velocity.getProxy();


    @Override
    public void execute(final Invocation invocation){

        final CommandSource sender = invocation.source();
        final String[] args = invocation.arguments().split(" ");

        if(!(sender instanceof Player)){
            sender.sendMessage(TextComponent.of(prefix + "§eOkay...? §8- §7I'm not aksing why, but I am unable to do anything for you! Sorry! >:( "));
            return;
        }

        Player player = (Player) sender;

        if(player.getCurrentServer().get().getServerInfo().getName().toLowerCase().startsWith("l")){
            sender.sendMessage(TextComponent.of(prefix + "§cYou are already on a §4Lobby-Server!"));
            return;
        }

        player.sendMessage(TextComponent.of(prefix + "§7Sending to lobby.."));

        HashMap<Integer, RegisteredServer> lobbys = new HashMap<>();
        proxyServer.getAllServers().forEach(proxyServer -> {
            if (proxyServer.getServerInfo().getName().toLowerCase().startsWith("l")) {
                lobbys.put(proxyServer.getPlayersConnected().size(), proxyServer);
            }
        });

        Map.Entry<Integer, RegisteredServer> sorted = new TreeMap<Integer, RegisteredServer>(lobbys).firstEntry();
        player.createConnectionRequest(sorted.getValue()).connect();
        player.sendMessage(TextComponent.of(prefix + "§eYou have been send to a Lobby-Server!"));



    }
}
