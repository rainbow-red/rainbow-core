package de.remadisson.rainbowcore.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.remadisson.rainbowcore.files;

import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;

public class JsonConfigurationAPI {

    private FileAPI fileAPI;
    private JsonObject content;

    public JsonConfigurationAPI(FileAPI fileAPI){
        this.fileAPI = fileAPI;

        try {
            // Loading the Config into the Variable
            content = readJSON().isJsonNull() ? new JsonObject() : readJSON().getAsJsonObject();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
    }

    private JsonElement readJSON() throws IOException, ParseException {
        JsonParser parser = new JsonParser();
        FileReader reader = new FileReader(fileAPI.getFile());

        JsonElement object = parser.parse(reader);

        return object;
    }

    public JsonConfigurationAPI set(Object key, Object value){
        content.addProperty(String.valueOf(key), String.valueOf(value));
        return this;
    }

    public JsonConfigurationAPI add(Object key, Object value){
        if(!contains(key)) {
            content.addProperty(String.valueOf(key), String.valueOf(value));
        }
        return this;
    }

    public JsonConfigurationAPI remove(Object key){
        content.remove(String.valueOf(key));
        return this;
    }

    public String getString(Object key){
        return content.get(String.valueOf(key)).toString();
    }

    public Integer getInteger(Object key){
        return content.get(String.valueOf(key)).getAsInt();
    }

    public Double getDouble(Object key){
        return content.get(String.valueOf(key)).getAsDouble();
    }

    public boolean getBoolean(Object key){
        return content.get(String.valueOf(key)).getAsBoolean();
    }

    public Object getObject(Object key){
        return content.get(String.valueOf(key));
    }

    public static Object getJsonObject(JsonObject json, Object key){
        return json.get(String.valueOf(key));
    }

    public boolean contains(Object key){
        return content.get(String.valueOf(key)) != null;
    }

    public JsonConfigurationAPI reload(){
        try {
            // Reading File
            JsonObject object = readJSON().isJsonNull() ? new JsonObject() : readJSON().getAsJsonObject();

            // Checking for JsonContent
            if(object.getAsJsonArray().size() > 1){
                System.out.println(files.console + "§4WARNING: §cThe File '§e" + fileAPI.getFile().getName() + "§c' has more than one JSON inside. The 2nd Will be Ignored!");
            }

            // Setting content
            content = object;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

    public JsonConfigurationAPI save() throws IOException{
        FileWriter writer = new FileWriter(fileAPI.getFile());
        writer.write(content.getAsJsonObject().toString());
        writer.flush();
        return this;
    }

}
