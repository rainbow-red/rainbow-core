package de.remadisson.api;

import com.google.gson.JsonElement;
import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.remadisson.files;
import org.slf4j.Logger;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.text.ParseException;


public class FileAPI {

    /*
     * This API IS MADE FOR ONE JSON PER FILE.
     */

    private String filename;
    private JsonObject content;
    private File file;
    private Logger logger;

    public FileAPI(Logger logger, String filename, String foldername) {
        this.filename = filename;
        this.logger = logger;
            File folder = new File(foldername);

            // Checking if Folder already exists
            if(!folder.exists()){
                folder.mkdir();
            }

            this.file = new File(folder, filename);
            //Check if File exists
            if(!file.exists()){
                try {
                    // Create new File
                    file.createNewFile();

                    // Debug message
                    logger.info((files.console + "§a" + filename + " §ehas been created!"));

                } catch (IOException e) {
                    e.printStackTrace();
                }
            }

        try {
            // Loading the Config into the Variable
            content = readJSON().isJsonNull() ? new JsonObject() : readJSON().getAsJsonObject();
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }

    }

    private JsonElement readJSON() throws IOException, ParseException {
        JsonParser parser = new JsonParser();
        FileReader reader = new FileReader(file);

        JsonElement object = parser.parse(reader);

       return object;
    }

    public FileAPI set(Object key, Object value){
        content.addProperty(String.valueOf(key), String.valueOf(value));
        return this;
    }

    public FileAPI add(Object key, Object value){
        if(!contains(key)) {
            content.addProperty(String.valueOf(key), String.valueOf(value));
        }
        return this;
    }

    public FileAPI remove(Object key){
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

    public FileAPI reload(){
        try {
            // Reading File
            JsonObject object = readJSON().isJsonNull() ? new JsonObject() : readJSON().getAsJsonObject();

            // Checking for JsonContent
            if(object.getAsJsonArray().size() > 1){
                logger.info((files.console + "§4WARNING: §cThe File '§e" + filename + "§c' has more than one JSON inside. The 2nd Will be Ignored!"));
            }

            // Setting content
            content = object;
        } catch (IOException | ParseException e) {
            e.printStackTrace();
        }
        return this;
    }

    public FileAPI save() throws IOException{
        FileWriter writer = new FileWriter(file);
        writer.write(content.getAsJsonObject().toString());
        writer.flush();
        return this;
    }

}