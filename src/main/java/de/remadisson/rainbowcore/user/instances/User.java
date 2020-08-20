package de.remadisson.rainbowcore.user.instances;

import de.remadisson.rainbowcore.api.DataBaseAPI;
import de.remadisson.rainbowcore.api.MojangAPI;
import de.remadisson.rainbowcore.sql.Database;

import java.sql.SQLException;
import java.util.UUID;

public class User {

    private final String username;
    private final UUID uuid;
    private UserSettings settings;

    public User(String username) throws NullPointerException{
        this.username = username;
        uuid = MojangAPI.getPlayerProfile(username).getUUID();
    }

    public User(UUID uuid) throws NullPointerException {
        this.uuid = uuid;
        username = MojangAPI.getPlayerProfile(uuid).getName();
    }

    public UUID getUUID(){
        return uuid;
    }

    public String getUsername(){
        return username;
    }

    public UserSettings getSettings() throws SQLException {
        if(settings == null){
           settings = Database.getUserSettings(uuid);
        }
            return settings;

    }

}