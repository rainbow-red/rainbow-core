package de.remadisson.rainbowcore.api;

import com.velocitypowered.api.util.UuidUtils;
import de.remadisson.rainbowcore.mojang.JsonUtils;
import de.remadisson.rainbowcore.mojang.PlayerProfile;

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
            return new PlayerProfile(values.get("name"), UUID.fromString(values.get("id").contains("-") ? values.get("id") : UuidUtils.fromUndashed(values.get("id")).toString()));
        }catch(NullPointerException ex){
            return null;
        }
    }



}
