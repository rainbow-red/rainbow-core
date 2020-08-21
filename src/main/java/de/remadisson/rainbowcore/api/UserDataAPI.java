package de.remadisson.rainbowcore.api;

import de.remadisson.rainbowcore.files;
import de.remadisson.rainbowcore.user.instances.User;

import java.util.HashMap;
import java.util.Map;
import java.util.UUID;

public class UserDataAPI {

    private final static HashMap<UUID, User> cachedUsers = new HashMap<>();

    public UserDataAPI() {}

    public HashMap<UUID, User> getloadedUsers(){
        return cachedUsers;
    }

    public boolean isLoaded(UUID uuid){
        return getloadedUsers().get(uuid) != null;
    }

    public User getUser(UUID uuid){
        return getloadedUsers().get(uuid);
    }

    public User getUser(String username) {
        for (Map.Entry<UUID, User> user : getloadedUsers().entrySet()) {
            if (user.getValue().getUsername().equalsIgnoreCase(username)) {
                return user.getValue();
            }
        }
        return null;
    }

}
