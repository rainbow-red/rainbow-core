package de.remadisson.rainbowcore.user.enums;

public enum UserTablist {

    MINIMAL(""), STANDART(""), EXTENDED("");

    private String headerAndFooter;

    UserTablist(String headerAndFooter){ this.headerAndFooter = headerAndFooter; }

    public String getHeaderAndFooter(){
        return headerAndFooter;
    }

}
