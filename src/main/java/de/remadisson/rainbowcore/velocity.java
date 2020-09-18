package de.remadisson.rainbowcore;

import com.google.inject.Guice;
import com.google.inject.Inject;
import com.google.inject.Injector;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyShutdownEvent;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import de.remadisson.rainbowcore.api.UserDataAPI;
import de.remadisson.rainbowcore.commands.*;
import de.remadisson.rainbowcore.manager.JoinListener;
import de.remadisson.rainbowcore.manager.ServerPingListener;
import de.remadisson.rainbowcore.manager.UserManager;
import de.remadisson.rainbowcore.sql.Database;
import de.remadisson.rainbowcore.user.UserAutoUnload;
import de.remadisson.rainbowcore.user.UserTablistUpdate;
import de.remadisson.rainbowcore.user.instances.User;
import de.remadisson.rainbowcore.user.instances.UserSettings;
import org.slf4j.Logger;

import java.sql.SQLException;
import java.util.Map;
import java.util.UUID;

@Plugin(
        id = "rainbowcore",
        name = "RainbowCore",
        version = "1.0.9",
        description = "This Plugin adds features that will create a main structure for our network",
        url = "https://www.rainbow.red",
        authors = {"remadisson"}
)
public class velocity {

    private final String prefix = files.console;
    private Logger logger;
    private ProxyServer server;
    private Injector injector = Guice.createInjector();

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

        final CommandManager cm = server.getCommandManager();

        //Unregistering Commands
        cm.unregister("server");
        cm.unregister("glist");

        // Registering Commands
        cm.register(new lockdownCommand(server, logger), "lockdown");
        cm.register(new devCoreCommand(server, logger), "devcore");
        cm.register(new HubCommand(server, logger), "hub", "lobby", "l", "leave");
        cm.register(new FindCommand(server, logger), "find");
        cm.register(new SendCommand(server, logger), "send");
        cm.register(new ServerCommand(server, logger), "server");
        cm.register(new ListCommand(server,logger), "glist");

        // Registering Listeners
        server.getEventManager().register(this, injector.getInstance(UserSettings.class));
        server.getEventManager().register(this, injector.getInstance(UserDataAPI.class));
        server.getEventManager().register(this, new JoinListener(server));
        server.getEventManager().register(this, new ServerPingListener(server));
        server.getEventManager().register(this, new UserManager(server));

        // Enabling User Save and auto unload
        UserAutoUnload.UserUpdateAndUnload(server);

        //Enabling UserTablistUpdate
        UserTablistUpdate.updateTablist(server);

        // Sending a Message to Console
        logger.info((prefix + "§2Rainbow-Core has successfully been started!"));

    }

    @Subscribe
    public void onProxyShutdown(ProxyShutdownEvent e){
        UserDataAPI api = new UserDataAPI();
        int savedUsers = 0;
        for(Map.Entry<UUID, User> entry : api.getloadedUsers().entrySet()){
            try {
                    Database.saveUser(entry.getValue());
                    savedUsers++;
                } catch (SQLException throwables) {
                    throwables.printStackTrace();
                }
            }
        if(savedUsers > 0) {
            System.out.println(files.console + " §e" + savedUsers + " §aUsers has been saved!");
        }
    }

    public static velocity getInstance(){
        return plugin;
    }

}
