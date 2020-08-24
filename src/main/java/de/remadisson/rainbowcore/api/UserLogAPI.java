package de.remadisson.rainbowcore.api;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.util.ArrayList;
import java.util.UUID;

public class UserLogAPI {

    private final UUID uuid;
    private final FileAPI fileAPI;

    public UserLogAPI(UUID uuid, String path){
        this.uuid = uuid;
        fileAPI = new FileAPI(uuid.toString(), path);
    }

    public void log(String line) throws IOException {
        fileAPI.addContent(line);
    }

    public ArrayList<String> getLines() throws FileNotFoundException {
        return fileAPI.getContent();
    }

    public File getFile(){
        return fileAPI.getFile();
    }

    public String getFolder() { return fileAPI.getPath(); }

    public UUID getUUID(){ return uuid; }

    
}
