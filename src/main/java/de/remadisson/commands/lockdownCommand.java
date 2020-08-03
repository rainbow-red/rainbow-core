package de.remadisson.commands;

import com.google.inject.Inject;
import com.velocitypowered.api.command.Command;
import com.velocitypowered.api.command.CommandSource;
import com.velocitypowered.api.proxy.Player;
import de.remadisson.files;
import net.kyori.adventure.text.TextComponent;
import org.checkerframework.checker.nullness.qual.NonNull;

public class lockdownCommand implements Command {

    /*
     *  Lockdown Command Syntax: /lockdown <set/unset/add/remove> <global/server-name/player>
     */

    private final String prefix = files.prefix;

    @Override
    public void execute(CommandSource sender, @NonNull String[] args) {
        if(sender instanceof Player){
            sender.sendMessage(TextComponent.of(files.prefix + "You are a Player."));
        } else {
            sender.sendMessage(TextComponent.of((files.console + "You are a Console.")));
        }
    }
}
