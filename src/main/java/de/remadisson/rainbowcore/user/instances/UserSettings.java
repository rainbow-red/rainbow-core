package de.remadisson.rainbowcore.user.instances;

import de.remadisson.rainbowcore.user.enums.UserTablist;

import java.util.Date;

public class UserSettings {

    private UserTablist tablist;
    private Date lastOnline;

    public UserSettings(UserTablist tablist){
        this.tablist = tablist;
        lastOnline = new Date();
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

    public Date getLastOnline(){
        return lastOnline;
    }

    public void updateLastOnline(){
        lastOnline = new Date();
    }

}
