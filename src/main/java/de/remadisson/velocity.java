package de.remadisson;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.remadisson.commands.lockdownCommand;
import org.slf4j.Logger;

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
    @Inject
    public velocity(ProxyServer server, Logger logger){
        this.server = server;
        this.logger = logger;

        /*
         *  Executed while loading
         */

            // Sending a Message to Console
            logger.info((prefix + "§aRainbow-Core is now starting!"));

            //Creating new Lockdown JSON-File
            files.loadLockdownFile(logger, server);

            // Registering Commands
            final CommandManager cm = server.getCommandManager();
            cm.register(new lockdownCommand(), "lockdown");

    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        /*
         * Executed after finishing!
         */

        // Sending a Message to Console
        logger.info((prefix + "§2Rainbow-Core has successfully been started!"));

    }
}
