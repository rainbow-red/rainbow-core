package de.remadisson.rainbowcore.user.instances;

import com.google.gson.JsonObject;
import de.remadisson.rainbowcore.user.enums.UserTablist;

import java.util.Date;

public class UserSettings {

    private UserTablist tablist;
    private String lastOnline;

    public UserSettings(UserTablist tablist, String lastOnline){
        this.tablist = tablist;
        this.lastOnline = lastOnline;
    }

    public void setTablist(UserTablist list){
        tablist = list;
    }

    public UserTablist getTablist(){
        return tablist;
    }

    public String getTablistString(){
        return tablist.getHeaderAndFooter();
    }

    public String getLastOnline(){
        return lastOnline;
    }

    public void updateLastOnline(){
        lastOnline = new Date().toString();
    }

    public String getJSONString(){
        JsonObject object = new JsonObject();
        object.addProperty("tablist", tablist.name());

        return object.toString();
    }

}
