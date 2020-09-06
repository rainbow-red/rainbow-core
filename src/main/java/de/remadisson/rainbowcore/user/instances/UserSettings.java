package de.remadisson.rainbowcore.user.instances;

import com.google.gson.JsonObject;
import de.remadisson.rainbowcore.user.enums.UserTablist;

import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.TimeZone;

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
        lastOnline = getLastOnlineValue();
        return lastOnline;
    }

    public String getJSONString(){
        JsonObject object = new JsonObject();
        object.addProperty("tablist", tablist.name());

        return object.toString();
    }

    public static String getLastOnlineValue(){
        Calendar cal = new GregorianCalendar();
        SimpleDateFormat date = new SimpleDateFormat("dd/MM/yyyy - HH:mm");
        date.setTimeZone(TimeZone.getTimeZone("UTC"));
        return date.format(cal.getTime());
    }

}
