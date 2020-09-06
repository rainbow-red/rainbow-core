package de.remadisson.rainbowcore.user;

import com.velocitypowered.api.proxy.ProxyServer;
import de.remadisson.rainbowcore.api.UserDataAPI;
import de.remadisson.rainbowcore.files;
import de.remadisson.rainbowcore.manager.JoinListener;
import de.remadisson.rainbowcore.velocity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.concurrent.TimeUnit;

public class UserTablistUpdate {

    private static int animation_status = 0;
    private static ArrayList<String> animation = new ArrayList<>(Arrays.asList("§7Thank you for being rainbowlicious!",
            "§dT§7hank you for being rainbowlicious!",
            "§9T§dh§7ank you for being rainbowlicious!",
            "§bT§9h§da§7nk you for being rainbowlicious!",
            "§2T§bh§9a§dn§7k you for being rainbowlicious!",
            "§aT§2h§ba§9n§dk§7 you for being rainbowlicious!",
            "§eT§ah§2a§bn§9k§d§7 you for being rainbowlicious!",
            "§cT§eh§aa§2n§bk§9 §dy§7ou for being rainbowlicious!",
            "§7T§ch§ea§an§2k§b §9y§do§7u for being rainbowlicious!",
            "§7Th§ca§en§ak§2 §by§9o§du§7 for being rainbowlicious!",
            "§7Tha§cn§ek§a §2y§bo§9u§d §7for being rainbowlicious!",
            "§7Than§ck§e §ay§2o§bu§9 §df§7or being rainbowlicious!",
            "§7Thank§c §ey§ao§2u§b §9f§do§7r being rainbowlicious!",
            "§7Thank §cy§eo§au§2 §bf§9o§dr§7 being rainbowlicious!",
            "§7Thank y§co§eu§a §2f§bo§9r§d §7being rainbowlicious!",
            "§7Thank yo§cu§e §af§2o§br§9 §db§7eing rainbowlicious!",
            "§7Thank you§c §ef§ao§2r§b §9b§de§7ing rainbowlicious!",
            "§7Thank you §cf§eo§ar§2 §bb§9e§di§7ng rainbowlicious!",
            "§7Thank you f§co§er§a §2b§be§9i§dn§7g rainbowlicious!",
            "§7Thank you fo§cr§e §ab§2e§bi§9n§dg§7 rainbowlicious!",
            "§7Thank you for§c §eb§ae§2i§bn§9g§d §7rainbowlicious!",
            "§7Thank you for §cb§ee§ai§2n§bg§9 §dr§7ainbowlicious!",
            "§7Thank you for b§ce§ei§an§2g§b §9r§da§7inbowlicious!",
            "§7Thank you for be§ci§en§ag§2 §br§9a§di§7nbowlicious!",
            "§7Thank you for bei§cn§eg§a §2r§ba§9i§dn§7bowlicious!",
            "§7Thank you for bein§cg§e §ar§2a§bi§9n§db§7owlicious!",
            "§7Thank you for being§c §er§aa§2i§bn§9b§do§7wlicious!",
            "§7Thank you for being §cr§ea§ai§2n§bb§9o§dw§7licious!",
            "§7Thank you for being r§ca§ei§an§2b§bo§9w§dl§7icious!",
            "§7Thank you for being ra§ci§en§ab§2o§bw§9l§di§7cious!",
            "§7Thank you for being rai§cn§eb§ao§2w§bl§9i§dc§7ious!",
            "§7Thank you for being rain§cb§eo§aw§2l§bi§9c§di§7ous!",
            "§7Thank you for being rainb§co§ew§al§2i§bc§9i§do§7us!",
            "§7Thank you for being rainbo§cw§el§ai§2c§bi§9o§du§7s!",
            "§7Thank you for being rainbow§cl§ei§ac§2i§bo§9u§ds§7!",
            "§7Thank you for being rainbowl§ci§ec§ai§2o§bu§9s§d!",
            "§7Thank you for being rainbowl§ci§ec§ai§2o§bu§9s§d!",
            "§7Thank you for being rainbowl§ci§ec§ai§2o§bu§9s§d!",
            "§7Thank you for being rainbowl§ci§ec§ai§2o§bu§9s§d!",
            "§7Thank you for being rainbowl§ci§ec§ai§2o§bu§9s§d!",
            "§7Thank you for being rainbowl§ci§ec§ai§2o§bu§ds!",
            "§7Thank you for being rainbowl§ci§ec§ai§2o§dus!",
            "§7Thank you for being rainbowl§ci§ec§ai§dous!",
            "§7Thank you for being rainbowl§ci§ec§dious!",
            "§7Thank you for being rainbowl§ci§dcious!",
            "§7Thank you for being rainbowl§dicious!",
            "§7Thank you for being rainbow§dlicious§7!",
            "§7Thank you for being rainbo§dwliciou§7s!",
            "§7Thank you for being rainb§dowlicio§7us!",
            "§7Thank you for being rain§dbowlici§7ous!",
            "§7Thank you for being rai§dnbowlic§7ious!",
            "§7Thank you for being ra§dinbowli§7cious!",
            "§7Thank you for being r§dainbowl§7icious!",
            "§7Thank you for being §drainbow§7licious!",
            "§7Thank you for being§d rainbo§7wlicious!",
            "§7Thank you for bein§dg rainb§7owlicious!",
            "§7Thank you for bei§dng rain§7bowlicious!",
            "§7Thank you for be§ding rai§7nbowlicious!",
            "§7Thank you for b§deing ra§7inbowlicious!",
            "§7Thank you for §dbeing r§7ainbowlicious!",
            "§7Thank you for§d being §7rainbowlicious!",
            "§7Thank you fo§dr being§7 rainbowlicious!",
            "§7Thank you f§dor bein§7g rainbowlicious!",
            "§7Thank you §dfor bei§7ng rainbowlicious!",
            "§7Thank you§d for be§7ing rainbowlicious!",
            "§7Thank yo§du for b§7eing rainbowlicious!",
            "§7Thank y§dou for §7being rainbowlicious!",
            "§7Thank §dyou for§7 being rainbowlicious!",
            "§7Thank§d you fo§7r being rainbowlicious!",
            "§7Than§dk you f§7or being rainbowlicious!",
            "§7Tha§dnk you §7for being rainbowlicious!",
            "§7Th§dank you§7 for being rainbowlicious!",
            "§7T§dhank yo§7u for being rainbowlicious!",
            "§dThank y§7ou for being rainbowlicious!",
            "§dThank §7you for being rainbowlicious!",
            "§dThank§7 you for being rainbowlicious!",
            "§dThan§7k you for being rainbowlicious!",
            "§dTha§7nk you for being rainbowlicious!",
            "§dTh§7ank you for being rainbowlicious!",
            "§dT§7hank you for being rainbowlicious!",
            "§7Thank you for being rainbowlicious!",
            "§7Thank you for being rainbowlicious!",
            "§7Thank you for being rainbowlicious!",
            "§7Thank you for being rainbowlicious!",
            "§7Thank you for being rainbowlicious!"));

    public static void updateTablist(ProxyServer proxyServer){

        files.pool.execute(() -> {
            proxyServer.getScheduler().buildTask(velocity.getInstance(), () -> {

                if(proxyServer.getPlayerCount() > 0) {
                    proxyServer.getAllPlayers().forEach(player -> {
                        try {
                            JoinListener.applyHeaderAndFooter(player, animation.get(animation_status));
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                        try {
                            Thread.sleep(10);
                        } catch (InterruptedException e) {
                            e.printStackTrace();
                        }
                    });

                    if (animation_status < animation.size()-1) {
                        animation_status++;
                    } else {
                        animation_status = 0;
                    }
                }
            }).repeat(100, TimeUnit.MILLISECONDS).schedule();
        });
    }

}
