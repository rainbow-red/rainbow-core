package de.remadisson.rainbowcore.manager;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.DisconnectEvent;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.remadisson.rainbowcore.api.UserDataAPI;
import de.remadisson.rainbowcore.files;
import de.remadisson.rainbowcore.sql.Database;
import de.remadisson.rainbowcore.user.enums.UserTablist;
import de.remadisson.rainbowcore.user.instances.User;
import net.kyori.text.TextComponent;

import java.sql.SQLException;
import java.text.SimpleDateFormat;
import java.util.*;

public class JoinListener {

    private static ProxyServer proxyServer = null;

    @Inject
    public JoinListener(ProxyServer proxyServer){
        JoinListener.proxyServer = proxyServer;
    }

    @Subscribe
    public void onJoin(ServerPreConnectEvent e){
        Player player = e.getPlayer();
        if(files.lockdown.get("global").getStatus()){
            if(!player.hasPermission("core.lockdown.join") && !player.hasPermission("core.lockdown.*") && !files.lockdown.get("global").containsUser(player.getUniqueId())){
                player.disconnect(TextComponent.of("§cRainbow§8-§4LOCKDOWN\n§eThe RainbowRed-Network is currently under lockdown.\n§eWe give our best, to have best results!\n\n§bTwitter§f§b @RainbowRedNet for Updates!"));
                e.setResult(ServerPreConnectEvent.ServerResult.denied());
            }
        }

        if(files.lockdown.get(e.getOriginalServer().getServerInfo().getName().toLowerCase()).getStatus()){
            if(!player.hasPermission("core.lockdown.join") && !player.hasPermission("core.lockdown.*") && !files.lockdown.get(e.getOriginalServer().getServerInfo().getName().toLowerCase()).getStatus()){
                player.disconnect(TextComponent.of("§cRainbow§8-§4LOCKDOWN\n§eThe RainbowRed-Network is currently under lockdown.\n§eWe give our best, to have best results!\n\n§bTwitter§f§b @RainbowRedNet for Updates!"));
                e.setResult(ServerPreConnectEvent.ServerResult.denied());
            }
        }
    }

    @Subscribe
    public void DisconnectEvent(DisconnectEvent e){
        UserDataAPI api = new UserDataAPI();
        files.pool.execute(() -> {
        for(Map.Entry<UUID, User> entry : api.getloadedUsers().entrySet()){
            try {
                Database.saveUser(entry.getValue());
            } catch (SQLException throwables) {
                throwables.printStackTrace();
            }
        }
        });
    }

    @Subscribe
    public void LoginEvent(LoginEvent e){
        Player player = e.getPlayer();
        UUID uuid = e.getPlayer().getUniqueId();
        UserDataAPI api = new UserDataAPI();

        files.pool.execute(() -> {
           if(!api.isLoaded(player.getUniqueId())) {
               api.getloadedUsers().put(uuid, new User(uuid));
           }
            try {
                applyHeaderAndFooter(player, "§crainbowlicious");
            } catch (InterruptedException interruptedException) {
                interruptedException.printStackTrace();
            }
        });


    }

    @Subscribe
    public void onPostServerConnect(ServerPostConnectEvent e){
        files.pool.execute(() -> {
            if(e.getPreviousServer() != e.getPlayer().getCurrentServer().get()){
                try {
                    applyHeaderAndFooter(e.getPlayer(), "§crainbowlicious");
                } catch (InterruptedException interruptedException) {
                    interruptedException.printStackTrace();
                }
            }
        });
    }


    public static void applyHeaderAndFooter(Player p, String animation) throws InterruptedException {

        UserDataAPI api = new UserDataAPI();

        if(!api.isLoaded(p.getUniqueId())) return;

        User user = api.getUser(p.getUniqueId());

        try {
            UserTablist tablist = user.getSettings().getTablist() != null ? user.getSettings().getTablist() : UserTablist.STANDARD;

            Calendar cal = new GregorianCalendar();
            SimpleDateFormat time = new SimpleDateFormat("HH:mm");
            time.setTimeZone(TimeZone.getTimeZone("UTC"));
            SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy");
            date.setTimeZone(TimeZone.getTimeZone("UTC"));

            p.setHeaderAndFooter(TextComponent.of(tablist.getReplacedHeader(p.getCurrentServer().isPresent() ? p.getCurrentServer().get().getServerInfo().getName() : "§cLoading..",time.format(cal.getTime()),date.format(cal.getTime()))), TextComponent.of(tablist.getReplacedFooter(animation, proxyServer.getPlayerCount())));
        } catch (SQLException throwables) {
            throwables.printStackTrace();
        }
    }

}
