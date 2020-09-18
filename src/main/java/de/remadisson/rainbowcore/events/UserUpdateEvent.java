package de.remadisson.rainbowcore.events;

import de.remadisson.rainbowcore.user.instances.User;

public class UserUpdateEvent {

    /**
     * Saves the User locally.
     */
    private User user;

    /**
     * Checks if the user is deleted not updated.
     */
    private boolean unload;

    /**
     * This Event cares about the data that needs to be send to the actual Minecraft-Servers, so we can interact with GUI (Graphic-User-Interfaces)
     * or use different triggers to perform certain commands in our core system.
     * @param user
     */
    public UserUpdateEvent(User user, boolean unload){
        this.user = user;
        this.unload = unload;
    }

    /**
     * Returns the User if the Event has been triggered!
     * @return
     */
    public User getUser(){
        return user;
    }

    /**
     * Returns if the user is Unloaded after
     * @return
     */
    public boolean getUnload(){ return unload; }
}
