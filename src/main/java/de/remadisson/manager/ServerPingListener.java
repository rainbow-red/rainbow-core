package de.remadisson.manager;

import com.google.inject.Inject;
import com.velocitypowered.api.event.Subscribe;
import com.velocitypowered.api.event.proxy.ProxyPingEvent;
import com.velocitypowered.api.proxy.ProxyServer;
import com.velocitypowered.api.proxy.server.ServerPing;
import de.remadisson.files;
import net.kyori.text.TextComponent;

public class ServerPingListener {

    private ProxyServer server;

    @Inject
    public ServerPingListener(ProxyServer server){
        this.server = server;
    }

    @Subscribe
    public void onServerPing(ProxyPingEvent e){
        ServerPing ping = e.getPing();
        ServerPing.Builder builder = ping.asBuilder();

        // IF SERVER IS IN LOCKDOWN
        if(files.lockdown.get("global").getStatus()){

            // builder.mods(new ModInfo.Mod("FML", "§cWe are under Construction!\n§bFollow us on Twitter: @RainbowRedNet"));

            builder.description(TextComponent.of("§cWe are under Construction!\n§bFollow us on Twitter: @RainbowRedNet"));
            builder.version(new ServerPing.Version(1, "§4LOCKDOWN"));

        } else {

            //  builder.mods(new ModInfo.Mod("FML", "§eWe are Rainbowlicious!\n§6Minecraft §e1§f.§e16§f.§e1"));

            builder.description(TextComponent.of("§eWe are Rainbowlicious!\n§6Minecraft §e1§f.§e16§f.§e1"));

            //  builder.version(new ServerPing.Version(0, "§7" + server.getPlayerCount() + "§8/§7" + server.getConfiguration().getShowMaxPlayers()));

        }

        //builder.description(TextComponent.of("§aWeb§f: \n§arainbow.red\n\n§bTwitter§f:\n§bRainbowRedNet"));
        e.setPing(builder.build());
    }
}
