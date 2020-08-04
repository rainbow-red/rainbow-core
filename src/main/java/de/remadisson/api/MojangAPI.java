package de.remadisson.api;

import de.remadisson.mojang.JsonUtils;
import de.remadisson.mojang.PlayerProfile;

import java.util.HashMap;
import java.util.UUID;

public class MojangAPI {

    public static PlayerProfile getPlayerProfile(UUID uuid){
        HashMap<String, String> values = JsonUtils.getPlayerInJson(uuid);
        try {
            return new PlayerProfile(values.get("name"), UUID.fromString(values.get("id")));
        }catch(NullPointerException ex){
            return null;
        }
    }

    public static PlayerProfile getPlayerProfile(String name){
        HashMap<String, String> values = JsonUtils.getPlayerInJson(name);
        try {
            return new PlayerProfile(values.get("name"), UUID.fromString(values.get("id")));
        }catch(NullPointerException ex){
            return null;
        }
    }



}
