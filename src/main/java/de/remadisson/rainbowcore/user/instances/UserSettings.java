package de.remadisson.rainbowcore.user.instances;

import com.google.gson.JsonObject;
import com.velocitypowered.api.proxy.ProxyServer;
import de.remadisson.rainbowcore.api.UserDataAPI;
import de.remadisson.rainbowcore.events.UserUpdateEvent;
import de.remadisson.rainbowcore.user.enums.UserTablist;

import javax.inject.Inject;
import java.text.SimpleDateFormat;
import java.util.*;

public class UserSettings {

    //@Inject
    //public ProxyServer proxyServer;

    private UUID uuid;
    private UserTablist tablist;
    private String lastOnline;
    private HashMap<String, Object> additionSettings = new HashMap<>();

    public UserSettings(UUID uuid, UserTablist tablist, String lastOnline){
        this.tablist = tablist;
        this.lastOnline = lastOnline;
        this.uuid = uuid;

      //  proxyServer.getEventManager().fire(new UserUpdateEvent(new UserDataAPI().getUser(uuid), false));
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
        //proxyServer.getEventManager().fire(new UserUpdateEvent(new UserDataAPI().getUser(uuid), false));
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
        //proxyServer.getEventManager().fire(new UserUpdateEvent(new UserDataAPI().getUser(uuid), false));
        return this;
    }

    public UserSettings removeAdditionSetting(String key){
        additionSettings.remove(key);
        //proxyServer.getEventManager().fire(new UserUpdateEvent(new UserDataAPI().getUser(uuid), false));
        return this;
    }

    public UserSettings setAdditonSettings(HashMap<String, Object> additionSettings){
        this.additionSettings = additionSettings;
        //proxyServer.getEventManager().fire(new UserUpdateEvent(new UserDataAPI().getUser(uuid), false));
        return this;
    }

    public HashMap<String, Object> getAdditionalSettings(){
        return additionSettings;
    }
}
