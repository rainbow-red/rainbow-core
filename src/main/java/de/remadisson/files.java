package de.remadisson;

import com.google.gson.JsonObject;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.RegisteredServer;
import de.remadisson.api.FileAPI;
import org.slf4j.Logger;

import java.io.IOException;
import java.util.HashMap;

public class files {

    /**
     * This file includes often used static variables or methods that are useful.
     */

    public static final String prefix = "§8»§r ";
    public static final String console = "§eRainbowCore " + prefix;
    public static final String debug = "§8[§dDEBUG§8] " + console;

    public static HashMap<String, Boolean> lockdown = new HashMap<>();


    public static void loadLockdownFile(Logger logger, ProxyServer server){
        FileAPI config = new FileAPI(logger,"config.json", "plugins/RainbowCore/");
        try {
            JsonObject lockdownedServers = new JsonObject();
            lockdownedServers.addProperty("global", false);
            for(RegisteredServer servers : server.getAllServers()){
                lockdownedServers.addProperty(servers.getServerInfo().getName(), false);
            }
            config.add("lockdown", lockdownedServers).save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
