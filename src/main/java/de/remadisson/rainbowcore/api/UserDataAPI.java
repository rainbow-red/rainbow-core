package de.remadisson.rainbowcore.api;

import de.remadisson.rainbowcore.files;
import de.remadisson.rainbowcore.user.instances.User;

import java.util.HashMap;
import java.util.UUID;

public class UserDataAPI {

    private static HashMap<UUID, User> cachedUsers = new HashMap<>();

    public UserDataAPI() {}

    public HashMap<UUID, User> getloadedUsers(){
        return cachedUsers;
    }

    public User loadUser(UUID uuid){
        // TODO LOAD USER :^)

        return null;
    }

    public void saveUser(User user){
        files.pool.execute(() -> {
            // TODO SAVE USER
        });
    }

    public boolean isLoaded(User user){
        return getloadedUsers().get(user.getUUID()) != null;
    }





}
