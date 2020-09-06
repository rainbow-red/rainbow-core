package de.remadisson.rainbowcore.user.instances;

import de.remadisson.rainbowcore.api.DataBaseAPI;
import de.remadisson.rainbowcore.api.MojangAPI;
import de.remadisson.rainbowcore.sql.Database;
import de.remadisson.rainbowcore.user.enums.UserTablist;

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

    /**
     * Returns the UUID of thee named player
     * @return
     */
    public UUID getUUID(){
        return uuid;
    }

    /**
     * Returns the Username of named Player
     * @return
     */
    public String getUsername(){
        return username;
    }

    /**
     * Returns UserSettings if loaded / loads user settings if not loaded
     * @return
     * @throws SQLException
     */
    public UserSettings getSettings() throws SQLException {
        if(settings == null){
           settings = Database.getUserSettings(uuid);
        }
            return settings;

    }


    /**
     * Allows easier change of UserTablist
     * @param tablist
     * @return
     * @throws SQLException
     */
    public User changeTablist(UserTablist tablist) throws SQLException {
        getSettings().setTablist(tablist);
        return this;
    }

    /**
     * Returns the UserTablist
     * @return
     * @throws SQLException
     */
    public UserTablist getUserTablist() throws SQLException {
        return getSettings().getTablist();
    }

}
