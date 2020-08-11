package de.remadisson.manager;

import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.player.ServerPreConnectEvent;
import com.velocitypowered.api.proxy.Player;
import de.remadisson.files;
import net.kyori.adventure.text.TextComponent;

public class JoinListener {

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
            if(!player.hasPermission("core.lockdown.join") && !player.hasPermission("core.lockdown.*")){
                player.disconnect(TextComponent.of("§cRainbow§8-§4LOCKDOWN\n§eThe RainbowRed-Network is currently under lockdown.\n§eWe give our best, to have best results!\n\n§bTwitter§f§b @RainbowRedNet for Updates!"));
                e.setResult(ServerPreConnectEvent.ServerResult.denied());
            }
        }
    }

}
