package de.remadisson.rainbowcore.api;

import com.velocitypowered.api.proxy.Player;
import de.remadisson.rainbowcore.files;
import net.luckperms.api.LuckPerms;
import net.luckperms.api.context.DefaultContextKeys;
import net.luckperms.api.model.group.Group;
import net.luckperms.api.model.user.User;
import net.luckperms.api.node.Node;
import net.luckperms.api.node.NodeType;
import net.luckperms.api.node.types.ChatMetaNode;
import org.checkerframework.checker.nullness.qual.NonNull;

import java.util.ArrayList;
import java.util.OptionalInt;
import java.util.UUID;
import java.util.concurrent.ExecutionException;

public class UserInitialsAPI {

    private final String username;
    private final UUID uuid;
    private final User user;

    private final LuckPerms api = files.luckPermsAPI;

    public UserInitialsAPI(Player p) throws ExecutionException, InterruptedException {
        username = p.getUsername();
        uuid = p.getUniqueId();
        user = api.getUserManager().isLoaded(uuid) ? api.getUserManager().getUser(uuid) : api.getUserManager().loadUser(uuid).get();
    }

    public UserInitialsAPI(String username) throws NullPointerException, ExecutionException, InterruptedException {
        this.username = username;
        uuid = MojangAPI.getPlayerProfile(username).getUUID();
        user = api.getUserManager().isLoaded(uuid) ? api.getUserManager().getUser(uuid) : api.getUserManager().loadUser(uuid).get();
    }

    public UserInitialsAPI(UUID uuid) throws NullPointerException, ExecutionException, InterruptedException {
        username = MojangAPI.getPlayerProfile(uuid).getName();
        this.uuid = uuid;
        user = api.getUserManager().isLoaded(uuid) ? api.getUserManager().getUser(uuid) : api.getUserManager().loadUser(uuid).get();
    }



    /*
     * PLAYER INITNALS
     */

    public Group getPlayerHighestGroup(){
        return getGroup(user.getPrimaryGroup());
    }

    public String getPrefix(){
        return user.getCachedData().getMetaData().getPrefix();
    }

    public String getSuffix(){
        return user.getCachedData().getMetaData().getSuffix();
    }


    public int getPlayerWeight(){

        // Thanks LuckPerms for provided Method :^)
        return user.getNodes().stream()
                .filter(Node::hasExpiry)
                .filter(NodeType.PREFIX::matches)
                .map(NodeType.PREFIX::cast)
                .filter(n -> n.getContexts().getAnyValue(DefaultContextKeys.SERVER_KEY)
                        .map(v -> v.equals("factions")).orElse(false))
                .mapToInt(ChatMetaNode::getPriority)
                .max()
                .orElse(0);

    }



    /*
     * PLAYER'S GROUP INITIALS
     */

    public static Group getGroup(String group){

        if(!files.luckPermsAPI.getGroupManager().isLoaded(group)){
            files.luckPermsAPI.getGroupManager().loadGroup(group);
        }

        return files.luckPermsAPI.getGroupManager().getGroup(group);
    }

    public static String getGroupName(Group group){
        return group.getName();
    }

    public static String getGroupPrefix(Group group){
        return group.getCachedData().getMetaData().getPrefix();
    }

    public static String getGroupSuffix(Group group){
        return group.getCachedData().getMetaData().getSuffix();
    }

    public static @NonNull OptionalInt getGroupWeight(Group group){
        return group.getWeight();
    }

    public static ArrayList<Group> getGroups() {
        files.luckPermsAPI.getGroupManager().loadAllGroups();
        return new ArrayList<>(files.luckPermsAPI.getGroupManager().getLoadedGroups());
    }


}
