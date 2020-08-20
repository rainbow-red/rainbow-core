package de.remadisson.rainbowcore;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.remadisson.rainbowcore.commands.lockdownCommand;
import de.remadisson.rainbowcore.manager.JoinListener;
import de.remadisson.rainbowcore.manager.ServerPingListener;
import de.remadisson.rainbowcore.sql.Database;
import de.remadisson.rainbowcore.user.UserAutoUnload;
import org.slf4j.Logger;

import javax.xml.crypto.Data;
import java.sql.SQLException;

@Plugin(
        id = "rainbowcore",
        name = "RainbowCore",
        version = "1.0-SNAPSHOT",
        description = "This Plugin adds features that will create a main structure for our network",
        url = "https://www.rainbow.red",
        authors = {"remadisson"}
)
public class velocity {

    private final String prefix = files.console;
    private Logger logger;
    private ProxyServer server;

    public static velocity plugin;

    @Inject
    public velocity(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
        plugin = this;

        /*
         *  Executed while loading
         */

        // Sending a Message to Console
        logger.info((prefix + "§aRainbow-Core is now starting!"));

    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        /*
         * Executed after finishing!
         */

        files.pool.execute(() -> {
            try {
                Database.mysql = Database.DatabaseConnect();
            } catch (SQLException | ClassNotFoundException throwables) {
                throwables.printStackTrace();
            }
        });



        //Creating new Lockdown JSON-File
        files.loadLockdownFile(logger, server);

        // Registering Commands
        final CommandManager cm = server.getCommandManager();
        cm.register(new lockdownCommand(server, logger), "lockdown");

        server.getEventManager().register(this, new JoinListener(server));
        server.getEventManager().register(this, new ServerPingListener(server));

        // Enabling User Save and auto unload
        UserAutoUnload.UserUpdateAndUnload(server);

        // Sending a Message to Console
        logger.info((prefix + "§2Rainbow-Core has successfully been started!"));

    }

    public static velocity getInstance(){
        return plugin;
    }

}
