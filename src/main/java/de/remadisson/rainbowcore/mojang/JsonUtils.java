package de.remadisson.rainbowcore.mojang;

import com.google.gson.JsonArray;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.net.URLConnection;
import java.util.HashMap;
import java.util.UUID;

public class JsonUtils {

    public static HashMap<String,String> getPlayerInJson(UUID uuid){
        HashMap<String, String> playerProfile = new HashMap<>();
        try {
            String replaced = uuid.toString().replace("-", "");
            JsonParser jsonParser = new JsonParser();
            URL request = new URL("https://api.mojang.com/user/profiles/" + replaced + "/names");
            URLConnection recon = request.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(recon.getInputStream()));

            String input;
            if((input = in.readLine()) != null){
                JsonArray jsonArray = jsonParser.parse(input).getAsJsonArray();
                String slot = jsonArray.get(jsonArray.size()-1).toString();
                JsonObject object = jsonParser.parse(slot).getAsJsonObject();
                playerProfile.put("id", uuid.toString());
                playerProfile.put("name", object.get("name").toString().replace("\"", ""));
            }

            in.close();

        }catch(IOException | NullPointerException ex){
            System.out.println("REQUEST FROM '" + uuid + "' returns null!");
            return null;
        }
        return playerProfile;
    }

    public static HashMap<String,String> getPlayerInJson(String name){

        JsonParser parser = new JsonParser();
        HashMap<String,String> playerProfile = new HashMap<>();
        try{

            URL request = new URL("https://api.mojang.com/users/profiles/minecraft/" + name);
            URLConnection recon = request.openConnection();
            BufferedReader in = new BufferedReader(new InputStreamReader(recon.getInputStream()));

            String input;
            if((input = in.readLine()) != null){
                JsonObject element = parser.parse(input).getAsJsonObject();
                playerProfile.put("id", element.get("id").toString().replace("\"", ""));
                playerProfile.put("name", element.get("name").toString().replace("\"", ""));

            }

            in.close();

        }catch(IOException | NullPointerException ex){
            System.out.println("REQUEST FROM '" + name + "' returns null!");
            return null;
        }
        return playerProfile;
    }
}
