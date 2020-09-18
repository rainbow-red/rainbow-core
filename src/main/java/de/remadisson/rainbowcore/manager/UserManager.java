package de.remadisson.rainbowcore.manager;

import com.google.common.eventbus.Subscribe;
import com.velocitypowered.api.proxy.ProxyServer;
import de.remadisson.rainbowcore.events.UserUpdateEvent;
import de.remadisson.rainbowcore.files;

public class UserManager {

    private ProxyServer server;

    public UserManager(ProxyServer server){
        this.server = server;
    }

    @Subscribe
    public void UserUpdateEvent(UserUpdateEvent e){
        System.out.println(files.debug + "Event does work!");
    }
}
