package de.remadisson.rainbowcore.user.instances;

import com.google.gson.JsonObject;
import de.remadisson.rainbowcore.user.enums.UserTablist;

import java.text.SimpleDateFormat;
import java.util.*;

public class UserSettings {

    private UserTablist tablist;
    private String lastOnline;
    private HashMap<String, Object> additionSettings = new HashMap<>();

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

    public String getHeader(){
        return tablist.getHeader();
    }

    public String getFooter(){
        return tablist.getFooter();
    }

    public String getLastOnline(){
        return lastOnline;
    }

    public String updateLastOnline(){
        lastOnline = getNewOnlineValue();
        return getLastOnline();
    }

    public String getJSONString(){
        JsonObject object = new JsonObject();
        object.addProperty("tablist", tablist.name());
        for(Map.Entry<String, Object> additional : getAdditionalSettings().entrySet()){
            object.addProperty(additional.getKey(), String.valueOf(additional.getValue()));
        }
        return object.toString();
    }

    public static String getNewOnlineValue(){
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        return date.format(cal.getTime());
    }

    public UserSettings addAdditionSetting(String key, Object value){
        additionSettings.put(key, value);
        return this;
    }

    public UserSettings removeAdditionSetting(String key){
        additionSettings.remove(key);
        return this;
    }

    public UserSettings setAdditonSettings(HashMap<String, Object> additionSettings){
        this.additionSettings = additionSettings;
        return this;
    }

    public HashMap<String, Object> getAdditionalSettings(){
        return additionSettings;
    }
}
