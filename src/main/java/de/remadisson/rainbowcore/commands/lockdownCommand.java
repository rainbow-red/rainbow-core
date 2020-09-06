package de.remadisson.rainbowcore.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.TabCompleteEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.remadisson.rainbowcore.api.MojangAPI;
import de.remadisson.rainbowcore.files;
import de.remadisson.rainbowcore.manager.LockdownServer;
import net.kyori.adventure.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;

import java.util.*;

public class lockdownCommand implements Command {

    /*
     *  Lockdown Command Syntax: /lockdown <on/off/add/remove/help> <global/server-name> [player]
     *
     *  Permissons: core.lockdown.status, core.lockdown.player, core.lockdown.global, core.lockdown.[server-name], core.lockdown.join.global/[server-name]
     *
     * */

    private final String prefix = files.prefix;

    private final ProxyServer server;
    private final Logger logger;

    @Inject
    public lockdownCommand(ProxyServer server, Logger logger){
        this.server = server;
        this.logger = logger;
    }

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
                    break;
                }

                case "off": {
                    changeLockdown(sender, input, false);
                    break;
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

                case "list": {

                    // Checking for permissions

                    if(!sender.hasPermission("core.lockdown.player") && !sender.hasPermission("core.lockdown.*")){
                        sender.sendMessage(TextComponent.of(files.message_no_permission));
                        return;
                    }

                    sender.sendMessage(TextComponent.of(prefix + "§7This Task is operated async, so it could take a second to finish.."));

                    if(!isServer(input)) {
                        sender.sendMessage(TextComponent.of(prefix + "§cThe Server §4" + input.toUpperCase() + " §ccould not be found!"));
                        return;
                    }

                    files.pool.execute(() -> {
                        String[] players = {""};
                        if(!server.getServer(input).isPresent() && !input.equalsIgnoreCase("global")){
                            sender.sendMessage(TextComponent.of(prefix + "§cThe Server §4" + input.toUpperCase() + " §ccould not be found!"));
                            return;
                        }

                        files.lockdown.get(input).getUsers().forEach(item -> {
                            if(players[0] == "") {
                                players[0] += "§6" + this.getName(UUID.fromString(item.getAsString())) + " §8(§7"+item.getAsString()+"§8)";
                            } else {
                                players[0] += "§f, §e" + this.getName(UUID.fromString(item.getAsString())) + " §8(§7"+item.getAsString()+"§8)";;
                            }
                        });

                        if(players[0] == ""){
                            sender.sendMessage(TextComponent.of(prefix + "§cThere are currently no Players for: §e" + input.toUpperCase()));
                        } else {
                            sender.sendMessage(TextComponent.of(prefix + "§eThe current Players for §a" + input.toUpperCase() + " §eare:"));
                            sender.sendMessage(TextComponent.of(prefix + players[0]));
                        }
                    });

                    return;
                }

                default:
                    sendHelp(sender);
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

                    // Checking if User exists.
                    if(uuid.toString() == null) {
                        sender.sendMessage(TextComponent.of(prefix + "§cThe Player §4" + input.toUpperCase() + " §cis not availabe!"));
                        return;
                    }

                    // Checking if Server exists.
                    if(!isServer(input)){
                        sender.sendMessage(TextComponent.of(prefix + "§cThe Server §4" + input.toUpperCase() + "§c could not be found!"));
                        return;
                    }

                    // Getting the Lockdown-Server data.
                    LockdownServer ls = files.lockdown.get(input);

                    // Checking if User is already allowed
                    if(!ls.containsUser(uuid)){
                        server.sendMessage(TextComponent.of(prefix + "§cThis User is not allowed on this Server!"));
                        return;
                    }

                    // Removing user from Lockdown-Server
                    ls.removeUser(uuid);
                    sender.sendMessage(TextComponent.of(prefix + "§eThe Player §a" + input2.toUpperCase() + " §eis now §cforbbidden§a to join to §a" + input.toUpperCase()));
                    break;

                }
                case "add": {

                    // Checking if User exists.
                    if(uuid.toString() == null) {
                        sender.sendMessage(TextComponent.of(prefix + "§cThe Player §4" + input.toUpperCase() + " §cis not availabe!"));
                        return;
                    }

                    // Checking if Server exists.
                    if(!isServer(input)){
                        sender.sendMessage(TextComponent.of(prefix + "§cThe Server §4" + input.toUpperCase() + "§c could not be found!"));
                        return;
                    }

                    // Getting Lockdown-Server data.
                    LockdownServer ls = files.lockdown.get(input);

                    // Checking if User is already alllowed
                    if(ls.containsUser(uuid)){
                        server.sendMessage(TextComponent.of(prefix + "§cThis User is already allowed on this Server!"));
                        return;
                    }

                    // Adding user to Lockdown-Server
                    ls.addUser(uuid);
                    sender.sendMessage(TextComponent.of(prefix + "§eThe Player §a" + input2.toUpperCase() + " §eis now added to §a" + input.toUpperCase()));
                    break;
                }
            }
        }else{
            // Send Help
            sendHelp(sender);
        }

        // Saving the changes.
        LockdownServer.saveLockdownServers(files.lockdown);
    }

    /**
     * Changes Values for the LockdownData
     * @param sender
     * @param input
     * @param status
     * @return
     */
    private void changeLockdown(CommandSource sender, String input, boolean status) {
        if(isServer(input) && (input.equalsIgnoreCase("global") || server.getServer(input).isPresent())){
            LockdownServer ls = files.lockdown.get(input);
            if(status) {
                if (ls.getStatus()) {
                    sender.sendMessage(TextComponent.of(prefix + "§cThis Server §8(§7" + input.toUpperCase() + "§8) §chas Lockdown already §aactivated!"));
                    return;
                }
            } else {
                if(!ls.getStatus()){
                    sender.sendMessage(TextComponent.of(prefix + "§cThis Server §8(§7" + input.toUpperCase() + "§8) §chas Lockdown already §4deactivated!"));
                    return;
                }
            }
            ls.setStatus(status);

            sender.sendMessage(TextComponent.of(prefix + "§eThe Server §a" + input.toUpperCase() + (status ? " §eis now in §cLockdown!" : " §eis now §aunlocked!")));

            if(!input.equalsIgnoreCase("global")) {
                server.getServer(input).get().getPlayersConnected().forEach(player -> {
                    if (!player.hasPermission("core.lockdown.join") && !player.hasPermission("core.lockdown.*")) {
                        player.disconnect(TextComponent.of("§eYou have been kicked!\n§4This server is now under lockdown!"));
                    }
                });
                return;
            }

            server.getAllPlayers().stream().forEach(player -> {
                if(!player.hasPermission("core.lockdown.join") && !player.hasPermission("core.lockdown.*")){
                    player.disconnect(TextComponent.of("§eYou have been kicked!\n§4This server is now under lockdown!"));
                }
            });

        } else {
            sender.sendMessage(TextComponent.of(prefix + "§cThe Server §4" + input.toUpperCase() + " §ccould not be found!"));
        }
    }

    @Override
    public List<String> suggest(@NotNull  CommandSource sender, String[] args){
        List<String> suggestions = new ArrayList<String>();

        if(args.length == 0) {
            suggestions.addAll(Arrays.asList("help", "add", "remove", "on", "off"));
        }else if(args.length == 1){
            List<String> instances = new ArrayList<String>(Arrays.asList("help", "add", "remove", "on", "off"));

            instances.stream().forEach(entry ->{
                if(entry.toLowerCase().startsWith(args[0].toLowerCase())){
                    suggestions.add(entry);
                }
            });

        } else if(args.length == 2){
            files.lockdown.entrySet().forEach(entry -> {
                if((sender.hasPermission("core.lockdown." + entry.getKey().toLowerCase()) || sender.hasPermission("core.lockdown.*")) && entry.getKey().toLowerCase().startsWith(args[1].toLowerCase())){
                    suggestions.add(entry.getKey());
                }
            });
        } else if(args.length == 3){
            if((args[0].equalsIgnoreCase("add") || args[0].equalsIgnoreCase("remove")) && args[2].length() > 0){
                if(sender instanceof Player){
                    Player player = (Player) sender;
                    for(Player online : player.getCurrentServer().get().getServer().getPlayersConnected()){
                        if(online.getUsername().toLowerCase().startsWith(args[2].toLowerCase())){
                            suggestions.add(online.getUsername());
                        }
                    }
                }
            }
        }
        return suggestions;
    }

    /**
     * Returns true if the Name is as Server available.
     * @param name
     * @return
     */
    private boolean isServer(String name){
        return server.getAllServers().stream().map(RegisteredServer::getServerInfo).anyMatch(serverInfo -> name.equalsIgnoreCase(serverInfo.getName())) || name.equalsIgnoreCase("global");
    }

    private String getName(UUID uuid){
        if(server.getPlayer(uuid).isPresent()){
            return server.getPlayer(uuid).get().getUsername();
        }

        return MojangAPI.getPlayerProfile(uuid).getName();
    }

    /**
     * Returns the UUID or NULL if the User exists.
     * @param name
     * @return
     */
    private UUID getUUID(String name) {
        if(server.getPlayer(name).isPresent()){
            Player target = server.getPlayer(name).get();
            return target.getUniqueId();
        } else {
            return MojangAPI.getPlayerProfile(name).getUUID();
        }
    }

    /**
     * Sends Help to the User
     * @param sender
     */
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

    /**
     * Returns the Servers, that are needed for certain actions.
     * @param sender
     * @return
     */
    private String allowedServers(CommandSource sender) {
        final String[] servers = {""};
        files.lockdown.entrySet().forEach(entry -> {
            if(sender.hasPermission("core.lockdown." + entry.getKey().toLowerCase()) || sender.hasPermission("core.lockdown.*")){
                String cap = (entry.getValue().getStatus() ? "§c" : "§a") + entry.getKey().substring(0,1).toUpperCase() + entry.getKey().substring(1);
                if(servers[0] == ""){
                    servers[0] = cap;
                } else {
                    if(entry.getValue().getStatus()) {
                        servers[0] = cap + "§r, " + servers[0];
                    } else {
                        servers[0] += "§r, " + cap;
                    }
                }
            }
        });
        return servers[0];
    }
}
