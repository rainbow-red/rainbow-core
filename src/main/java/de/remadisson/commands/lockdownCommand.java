package de.remadisson.commands;

import com.google.inject.Inject;
import com.typesafe.config.ConfigException;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.remadisson.api.MojangAPI;
import de.remadisson.files;
import de.remadisson.manager.LockdownServer;
import net.kyori.adventure.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.w3c.dom.Text;

import java.util.Map;
import java.util.Optional;
import java.util.UUID;

public class lockdownCommand implements Command {

    /*
     *  Lockdown Command Syntax: /lockdown <on/off/add/remove/help> <global/server-name> [player]
     *
     *  Permissons: core.lockdown.status, core.lockdown.player, core.lockdown.global, core.lockdown.[server-name], core.lockdown.join.global/[server-name]
     *
     * */

    private final String prefix = files.prefix;

    @Inject
    ProxyServer server;

    @Override
    public void execute(CommandSource sender, @NonNull String[] args) {
        if(args.length == 0){
            // Checking for Needed permissons
            if(!sender.hasPermission("core.lockdown.status") && !sender.hasPermission("core.lockdown.*")){
                sender.sendMessage(TextComponent.of(files.message_no_permission));
                return;
            }

                // Seding the Headtext
                sender.sendMessage(TextComponent.of(prefix + "§eThe current §cLockdown§7-§6Statuses §eare: "));

                // Variables for more information
                int locked = 0, current = 0;
                boolean dots = false;

                // Sending Parameters for Server: Global Seperate
                sender.sendMessage(TextComponent.of(prefix + "§bGlobal §f: " + (!files.lockdown.get("global").getStatus() ? "§aunlocked" : "§clocked")));

                // Looping through the lockdown servers
                for(Map.Entry<String, LockdownServer> entry : files.lockdown.entrySet()){

                    if(entry.getValue().getStatus()) locked++;

                    if(!entry.getKey().equalsIgnoreCase("global")) {
                        if (current < 10) {
                            current++;
                            sender.sendMessage(TextComponent.of(prefix + "§b" + entry.getKey() + "§f : " + (!entry.getValue().getStatus() ? "§aunlocked" : "§clocked")));
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
                case "on":
                case "off": {

                    //Getting the Servers, that can be used in context with lockdown.
                    String servers = allowedServers(sender);

                    // Checking if the String is empty, if true: The Player has no Permisson for that command!
                    if(servers.equalsIgnoreCase("")){
                        sender.sendMessage(TextComponent.of(files.message_no_permission));
                        return;
                    }

                    //Sending the Callback
                    sender.sendMessage(TextComponent.of(prefix + "§cUsage: §e/lockdown on/off " + servers));
                    return;
                }

                /**
                 * Permissions needed:
                 */
                case "remove":
                case "add": {

                    // Checking Permissions to add/remove
                    if(!sender.hasPermission("core.lockdown.*") && !sender.hasPermission("core.lockdown.player")){
                        sender.sendMessage(TextComponent.of(files.message_no_permission));
                        return;
                    }

                    sender.sendMessage(TextComponent.of(prefix + "§cUsage: §e/lockdown add/remove <Global/Server> <Player>"));
                    return;
                }

                /**
                 * Needed Permission core.lockdown.[server-name] or core.lockdown.global or core.lockdown.*
                 */
                default:
                case "help": {

                    // Send Help
                    sendHelp(sender);

                }

            } // End of Switch
        } else if(args.length == 2){

            String instruction = args[0].toLowerCase().trim();
            String input = args[1].toLowerCase().trim();

            switch(instruction){
                case "on": {
                    changeLockdown(sender, input, true);
                }

                case "off": {
                    changeLockdown(sender, input, false);
                }

                case "remove":
                case "add": {

                    // Checking Permissions to add/remove
                    if(!sender.hasPermission("core.lockdown.*") && !sender.hasPermission("core.lockdown.player")){
                        sender.sendMessage(TextComponent.of(files.message_no_permission));
                        return;
                    }

                    sender.sendMessage(TextComponent.of(prefix + "§cUsage: §e/lockdown add/remove <Global/Server> <Player>"));
                    return;
                }
            }

        } else if(args.length == 3){
            String instruction = args[0].toLowerCase().trim();
            String input = args[1].toLowerCase().trim();
            String input2 = args[2].toLowerCase().trim();

            UUID uuid = getUUID(input2);

            switch(instruction){
                case "on":
                case "off": {

                    //Getting the Servers, that can be used in context with lockdown.
                    String servers = allowedServers(sender);

                    // Checking if the String is empty, if true: The Player has no Permisson for that command!
                    if(servers.equalsIgnoreCase("")){
                        sender.sendMessage(TextComponent.of(files.message_no_permission));
                        return;
                    }

                    //Sending the Callback
                    sender.sendMessage(TextComponent.of(prefix + "§cUsage: §e/lockdown on/off " + servers));
                    return;
                }

                case "remove": {

                    if(uuid.toString() == null) {
                        sender.sendMessage(TextComponent.of(prefix + "§cThe Player §4" + input.toUpperCase() + " §cis not availabe!"));
                        return;
                    }

                    if(!isServer(input)){
                        sender.sendMessage(TextComponent.of(prefix + "§cThe Server §4" + input.toUpperCase() + "§c could not be found!"));
                        return;
                    }

                    LockdownServer ls = files.lockdown.get(input);

                    if(!ls.containsUser(uuid)){
                        server.sendMessage(TextComponent.of(prefix + "§cThis User is not allowed on this Server!"));
                        return;
                    }

                    ls.removeUser(uuid);
                    sender.sendMessage(TextComponent.of(prefix + "§eThe Player §a" + input2.toUpperCase() + " §eis now §cforbbidden§a to join to §a" + input.toUpperCase()));

                }
                case "add": {

                    if(uuid.toString() == null) {
                        sender.sendMessage(TextComponent.of(prefix + "§cThe Player §4" + input.toUpperCase() + " §cis not availabe!"));
                        return;
                    }

                    if(!isServer(input)){
                        sender.sendMessage(TextComponent.of(prefix + "§cThe Server §4" + input.toUpperCase() + "§c could not be found!"));
                        return;
                    }

                    LockdownServer ls = files.lockdown.get(input);

                    if(ls.containsUser(uuid)){
                        server.sendMessage(TextComponent.of(prefix + "§cThis User is already allowed on this Server!"));
                        return;
                    }

                    ls.addUser(uuid);
                    sender.sendMessage(TextComponent.of(prefix + "§eThe Player §a" + input2.toUpperCase() + " §eis now added to §a" + input.toUpperCase()));
                }
            }
        }else{
            // Send Help
            sendHelp(sender);
        }

        LockdownServer.saveLockdownServers(files.lockdown);
    }

    private boolean changeLockdown(CommandSource sender, String input, boolean status) {
        if(isServer(input)){
            LockdownServer ls = files.lockdown.get(input);
            if(ls.getStatus()){
                sender.sendMessage(TextComponent.of(prefix + "§cThis Server §8(§7" + input.toUpperCase() +") §chas alraedy Lockdown §aactivated!"));
                return true;
            }
            ls.setStatus(status);

            sender.sendMessage(TextComponent.of(prefix + "§eThe Server §a" + input.toUpperCase() + (status ? " §eis now in §cLockdown!" : " §eis now §aunlocked!")));
            server.getServer(input).get().getPlayersConnected().forEach(player -> {
                if(player.hasPermission("core.lockdown.join")) {
                    player.disconnect(TextComponent.of("§eYou have been kicked!\n§4This server is now in Lockdown!"));
                }
            });
        } else {
            sender.sendMessage(TextComponent.of(prefix + "§cThe Server §4" + input.toUpperCase() + " §ccould not be found!"));
        }
        return false;
    }

    private boolean isServer(String name){
        return server.getAllServers().stream().map(RegisteredServer::getServerInfo).anyMatch(serverInfo -> name.equalsIgnoreCase(serverInfo.getName()));
    }

    private UUID getUUID(String name) {
        if(server.getPlayer(name).isPresent()){
            Player target = server.getPlayer(name).get();
            return target.getUniqueId();
        } else {
            return MojangAPI.getPlayerProfile(name).getUUID();
        }
    }

    private void sendHelp(CommandSource sender){
        // Checking Permission to help
        if(!sender.hasPermission("core.lockdown.*") && sender.hasPermission("core.lockdown.player") && sender.hasPermission("core.lockdown.status") && files.lockdown.entrySet().stream().anyMatch(entry -> !sender.hasPermission("core.lockdown." + entry.getKey()))){
            sender.sendMessage(TextComponent.of(files.message_no_permission));
            return;
        }

        allowedServers(sender);

        // Send Headmessage
        sender.sendMessage(TextComponent.of(prefix + "§cLockdown §7- §6Help:"));

        //Checking permissions
        sender.sendMessage(TextComponent.of(prefix + "§f- §e/lockdown"));

        // Check permissions || Send Player add Message
        if(sender.hasPermission("core.lockdown.*") || sender.hasPermission("core.lockdown.player")) {
            sender.sendMessage(TextComponent.of(prefix + "§f- §e/lockdown add/remove <Global/Server> <Player>"));
        }

        // Getting Servers and checking Permissions for the Player
        String servers = allowedServers(sender);
        if(!servers.equalsIgnoreCase("")){
            sender.sendMessage(TextComponent.of(prefix + "§f- §e/lockdown on/off " + servers));
        }
    }

    private String allowedServers(CommandSource sender) {
        final String[] servers = {""};
        files.lockdown.entrySet().stream().forEach(entry -> {
            if(sender.hasPermission("core.lockdown." + entry.getKey().toLowerCase()) || sender.hasPermission("core.lockdown.*")){
                String cap = (entry.getValue().getStatus() ? "§c" : "§a") + entry.getKey().substring(0,1).toUpperCase() + entry.getKey().substring(1);
                if(servers[0] == ""){
                    servers[0] = cap;
                } else {
                    if(entry.getValue().getStatus()) {
                        servers[0] += "§r, " + cap;
                    } else {
                        servers[0] = cap + "§r," + servers[0];
                    }
                }
            }
        });
        return servers[0];
    }
}
