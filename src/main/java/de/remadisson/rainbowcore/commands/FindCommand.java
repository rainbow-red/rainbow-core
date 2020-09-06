package de.remadisson.rainbowcore.commands;

import com.google.inject.Inject;
import com.mojang.brigadier.tree.CommandNode;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandMeta;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import com.velocitypowered.api.proxy.ProxyServer;
import de.remadisson.rainbowcore.files;
import net.kyori.adventure.text.TextComponent;
import org.jetbrains.annotations.NotNull;
import org.slf4j.Logger;
import org.w3c.dom.Text;

import java.util.Collection;

public class FindCommand implements Command {

    private ProxyServer server;
    private Logger logger;

    @Inject
    public FindCommand(ProxyServer server, Logger logger) {
        this.server = server;
        this.logger = logger;
    }

    @Override
    public void execute(CommandSource sender, @NotNull String[] args){
        if(sender instanceof Player){
            if(!sender.hasPermission("core.find") && !sender.hasPermission("core.*") && !sender.hasPermission("core.find.*")){
               sender.sendMessage(TextComponent.of(files.message_no_permission));
                return;
            }

            if(args.length == 1) {
                if(server.getPlayer(args[0]).isPresent()) {
                    Player target = server.getPlayer(args[0]).get();
                    sender.sendMessage(TextComponent.of(files.prefix + "§eThe Player §6" + args[0] + " §eis on §a" + target.getCurrentServer().get().getServerInfo().getName()));
                } else {
                    sender.sendMessage(TextComponent.of(files.prefix + "§cThe Player §4" + args[0] + " §cis currently not online!"));
                }
            } else {
                sender.sendMessage(TextComponent.of(files.prefix + "§eHelp for §aFind§8:"));
                sender.sendMessage(TextComponent.of(files.prefix + "§f- §e/find §7<§ePlayer§7>"));
            }
        }
    }
}
