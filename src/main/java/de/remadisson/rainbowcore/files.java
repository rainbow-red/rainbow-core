package de.remadisson.rainbowcore;

import com.google.gson.JsonArray;
import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import com.mongodb.util.JSON;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.remadisson.rainbowcore.api.FileAPI;
import de.remadisson.rainbowcore.api.JsonConfigurationAPI;
import de.remadisson.rainbowcore.manager.LockdownServer;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.LuckPermsProvider;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;
import java.util.concurrent.Executors;
import java.util.concurrent.ThreadPoolExecutor;

public class files {

    /**
     * This file includes often used static variables or methods that are useful.
     */

    public static final String prefix = "§8»§r ";
    public static final String console = "§eRainbowCore " + prefix;
    public static final String debug = "§8[§dDEBUG§8] " + console;
    public static final String message_no_permission = prefix + "§cYou have no permission to perform this command!";

    public static final LuckPerms luckPermsAPI = LuckPermsProvider.get();

    public static ThreadPoolExecutor pool = (ThreadPoolExecutor) Executors.newCachedThreadPool();

    // Saving Files with their context (JSON)
    public static ArrayList<FileAPI> files = new ArrayList<>();

    // Saving Servers with ther Lockdown Statuses
    public static HashMap<String, LockdownServer> lockdown = new HashMap<>();

    /**
     * This Method uses JSON Strings to make clear if there are any servers, where normal players should'nt join.
     * @param logger
     * @param server
     */
    public static void loadLockdownFile(Logger logger, ProxyServer server){
        // Initing the FileAPI -> Creating Files and Checking if they're empty
        FileAPI fileAPI = new FileAPI("config.json", "plugins/RainbowCore/");
        JsonConfigurationAPI config = new JsonConfigurationAPI(fileAPI);

        try {
            if(!config.contains("lockdown")) {
                // Adding Servers to an JSON
                JsonObject lockdownedServers = new JsonObject();
                JsonObject lockdowndetails = new JsonObject();
                JsonArray users = new JsonArray();

                lockdowndetails.addProperty("status", false);
                lockdowndetails.add("users", users);

                lockdownedServers.add("global", lockdowndetails);

                for (RegisteredServer servers : server.getAllServers()) {
                    lockdownedServers.add(servers.getServerInfo().getName().toLowerCase(), lockdowndetails);
                }

                config.add("lockdown", lockdownedServers).save();
            }
                // Getting the servers from JSON
                JsonObject obj = JsonParser.parseString(JSON.parse(config.getObject("lockdown").toString()).toString()).getAsJsonObject();

                // Appling Servers and their values to an HashMap
                for (Map.Entry<String, JsonElement> s : obj.entrySet()) {
                    //lockdown.put(s.getKey(), s.getValue().getAsBoolean());
                    JsonObject values = s.getValue().getAsJsonObject();
                    lockdown.put(s.getKey(), new LockdownServer(s.getKey(), values.get("status").getAsBoolean(), values.get("users").getAsJsonArray()));
                }


        } catch (IOException e) {
            e.printStackTrace();
        }

        // Adding config to an Arraylist, to edit it later!
        files.add(fileAPI);
    }

}
