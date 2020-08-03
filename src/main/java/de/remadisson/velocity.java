package de.remadisson;

import com.google.inject.Inject;
import com.velocitypowered.api.event.proxy.ProxyInitializeEvent;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.plugin.Plugin;
import com.velocitypowered.api.proxy.ProxyServer;
import org.slf4j.Logger;

@Plugin(
        id = "rainbowcore",
        name = "RainbowCore",
        version = "1.0-SNAPSHOT",
        description = "This Plugin adds features that will create a main structure for our network",
        url = "https://www.rainbow.red",
        authors = {"Remadisson"}
)
public class velocity {

    @Inject
    private Logger logger;
    private ProxyServer server;

    @Inject
    public velocity(ProxyServer server, Logger logger){
        this.server = server;
        this.logger = logger;
        //Executed while loading

    }

    @Subscribe
    public void onProxyInitialization(ProxyInitializeEvent event) {
        // Executed after finishing!

    }
}
