package de.remadisson.rainbowcore.manager;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.connection.LoginEvent;
import com.velocitypowered.api.event.player.ServerPostConnectEvent;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.remadisson.rainbowcore.api.UserDataAPI;
import de.remadisson.rainbowcore.files;
import de.remadisson.rainbowcore.user.instances.User;
import net.kyori.text.TextComponent;

import java.util.UUID;

public class JoinListener {

    private final ProxyServer proxyServer;

    @Inject
    public JoinListener(ProxyServer proxyServer){
        this.proxyServer = proxyServer;
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
    public void LoginEvent(LoginEvent e){
        Player player = e.getPlayer();
        UUID uuid = e.getPlayer().getUniqueId();
        UserDataAPI api = new UserDataAPI();
        if(api.isLoaded(player.getUniqueId())){
            return;
        }

        files.pool.execute(() -> {
            api.getloadedUsers().put(uuid, new User(uuid));
        });

        applyHeaderAndFooter(player);
    }

    @Subscribe
    public void onPostServerConnect(ServerPostConnectEvent e){
        if(e.getPreviousServer() != e.getPlayer().getCurrentServer().get()){
            applyHeaderAndFooter(e.getPlayer());
        }
    }

    public void applyHeaderAndFooter(Player p){
        int max_player_count = 100;
        String temp_header = "§e§l« §eRainbowRED §e§l» \n §7Welcome " + p.getUsername() + " \n §a§7You play on §b" + p.getCurrentServer().get().getServerInfo().getName() + " \n";
        String temp_footer = "\n §e " + proxyServer.getPlayerCount() + " §7/§e" + max_player_count + "§7 Players are online! \n §9Discord§7: §9remady.me/dc \n §bTwitter§7: §bRainbowNetwork";
        p.setHeaderAndFooter(TextComponent.of(temp_header), TextComponent.of(temp_footer));
    }

}
