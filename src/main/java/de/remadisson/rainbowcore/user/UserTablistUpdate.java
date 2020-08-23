package de.remadisson.rainbowcore.user;

import com.velocitypowered.api.proxy.ProxyServer;
import de.remadisson.rainbowcore.files;
import de.remadisson.rainbowcore.manager.JoinListener;
import de.remadisson.rainbowcore.velocity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class UserTablistUpdate {

    private static int animation_status = 0;
    private static ArrayList<String> animation = new ArrayList<>(Arrays.asList("§crainbowlicious", "§6rainbowlicious", "§erainbowlicious", "§arainbowlicious", "§brainbowlicious", "§drainbowlicious"));

    public static void updateTablist(ProxyServer proxyServer){

        files.pool.execute(() -> {
            proxyServer.getScheduler().buildTask(velocity.getInstance(), () -> {

                if(proxyServer.getPlayerCount() > 0) {
                    proxyServer.getAllPlayers().forEach(player -> {
                        JoinListener.applyHeaderAndFooter(player, animation.get(animation_status));
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });

                    if (animation_status < 5) {
                        animation_status++;
                    } else {
                        animation_status = 0;
                    }
                }
            }).repeat(1250, TimeUnit.MILLISECONDS).schedule();
        });
    }

}
