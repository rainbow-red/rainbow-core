package de.remadisson.rainbowcore;

import com.google.inject.Inject;
import com.velocitypowered.api.command.CommandManager;
import com.velocitypowered.api.command.CommandMeta;
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
import org.slf4j.Logger;

import java.sql.Array;
import java.sql.SQLException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.UUID;
import java.util.stream.Collector;
import java.util.stream.Collectors;

@Plugin(
        id = "rainbowcore",
        name = "RainbowCore",
        version = "1.1.0",
        description = "This Plugin adds features that will create a main structure for our network",
        url = "https://www.rainbow.red",
        authors = {"remadisson"}
)
public class velocity {

    private final String prefix = files.console;
    private static ProxyServer proxy;

    public static velocity plugin;

    @Inject
    public velocity(ProxyServer server, Logger logger) {
        proxy = server;
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
        files.loadLockdownFile();

        final CommandManager cm = proxy.getCommandManager();

        //Unregistering Commands
        cm.unregister("server");
        cm.unregister("glist");

        // Registering Commands
        cm.register(registerCommand(cm, "lockdown"),new lockdownCommand());
        cm.register(registerCommand(cm,"devcore"),new devCoreCommand());
        cm.register(registerCommand(cm,"hub", "lobby", "l", "leave"), new HubCommand());
        cm.register(registerCommand(cm,"find"), new FindCommand());
        cm.register(registerCommand(cm,"send"), new SendCommand());
        cm.register(registerCommand(cm,"server"), new ServerCommand());
        cm.register(registerCommand(cm,"glist"), new ListCommand());

        // Registering Listeners
        //server.getEventManager().register(this, injector.getInstance(UserSettings.class));
        //server.getEventManager().register(this, injector.getInstance(UserDataAPI.class));
        proxy.getEventManager().register(this, new JoinListener(proxy));
        proxy.getEventManager().register(this, new ServerPingListener(proxy));
        proxy.getEventManager().register(this, new UserManager(proxy));

        // Enabling UserSave and AutoUnload
        UserAutoUnload.UserUpdateAndUnload(proxy);

        //Enabling UserTablistUpdate
        UserTablistUpdate.updateTablist(proxy);

        // Sending a Message to Console
        System.out.println((prefix + "§2Rainbow-Core has successfully been started!"));

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

    public CommandMeta registerCommand(CommandManager cm, String... aliases){
        String primary = aliases[0];
        String[] optional = Arrays.stream(aliases).filter(filter -> !filter.equalsIgnoreCase(aliases[0])).toArray(String[]::new);
        return cm.metaBuilder(primary).aliases(optional).build();
    }

    public static velocity getInstance(){
        return plugin;
    }

    public static ProxyServer getProxy(){
        return proxy;
    }
}
