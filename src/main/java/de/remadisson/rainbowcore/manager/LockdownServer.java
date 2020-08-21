package de.remadisson.rainbowcore.manager;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.remadisson.rainbowcore.files;

import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.UUID;

public class LockdownServer {

    private final String name;
    private boolean status;
    private final JsonArray users;

    public LockdownServer(String name, boolean status, JsonArray users) {
        this.name = name;
        this.status = status;
        this.users = users;
    }

    public String getName(){
        return name;
    }

    public boolean getStatus(){
        return status;
    }

    public LockdownServer setStatus(boolean status){
        this.status = status;
        return this;
    }

    public ArrayList<UUID> getUsersAsList(){
        ArrayList<UUID> list = new ArrayList<>();
        users.forEach(id -> list.add(UUID.fromString(id.toString())));
        return list;
    }

    public JsonArray getUsers(){
        return users;
    }

    public LockdownServer addUser(UUID uuid){
        users.add(uuid.toString());
        return this;
    }

    public LockdownServer removeUser(UUID uuid){
        users.add(uuid.toString());
        return this;
    }

    public boolean containsUser(UUID uuid){
        return users.contains(JsonParser.parseString(uuid.toString()));
    }

    public static void saveLockdownServers(HashMap<String, LockdownServer> servers){

        JsonObject lockdownedServers = new JsonObject();

        servers.forEach((k, v) -> {
            JsonObject lockdowndetails = new JsonObject();
            JsonArray users =  v.getUsers();

            lockdowndetails.addProperty("status", v.getStatus());
            lockdowndetails.add("users", users);
            lockdownedServers.add(k,lockdowndetails);
        });

        try {
            files.files.get(0).set("lockdown", lockdownedServers).save();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

}
