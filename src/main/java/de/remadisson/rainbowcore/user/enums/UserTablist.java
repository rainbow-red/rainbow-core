package de.remadisson.rainbowcore.user.enums;

public enum UserTablist {

    // ServerName -> %servername%
    // Player-Counter -> %networkplayercounter%
    // TIME -> %time_HH:mm%
    // DATE -> %date_yyyy.MM.dd%

    MINIMAL("§l§5R§da§9i§bn§ab§eo§6w§cRED§o§8\n§7[On server: %servername%]", "§7Players on network: %networkplayercounter%"),
    STANDARD("§l§5R§da§9i§bn§ab§eo§6w§cRED§o§8\n§7[On server: %servername%]\n§r§fTime: %time_HH:mm% UTC\n§7Playerlist:", "§7Players on network: %networkplayercounter%"),
    EXTENDED("§l§5R§da§9i§bn§ab§eo§6w§cRED§bNetwork\n§o§7[On server: %servername%]\n§r§fTime: %time_HH:mm% UTC | Date: %date_yyyy.MM.dd%\n§7Players on this server:",
            "§7Players on network: %networkplayercounter%\n§l§fThank you for being %animation:rainbowlicious%§r§l§f!");

    private String header;
    private String footer;

    UserTablist(String header, String footer){
        this.header = header;
        this.footer = footer;
    }

    public String getHeader(){
        return header;
    }

    public String getFooter(){ return footer; }

    public String getReplacedHeader(String servername, String time, String date){
        return header.replace("%servername%", servername).replace("%time_HH:mm%", time).replace("%date_yyyy.MM.dd%", date);
    }

    public String getReplacedFooter(String animation, int networkcounter){
        return footer.replace("%networkplayercounter%", String.valueOf(networkcounter)).replace("%animation:rainbowlicious%", animation);
    }

}
