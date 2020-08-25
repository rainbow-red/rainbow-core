package de.remadisson.rainbowcore.user.enums;

public enum UserTablist {

    // ServerName -> %servername%
    // Player-Counter -> %networkplayercounter%
    // TIME -> %time_HH:mm%
    // DATE -> %date_yyyy.MM.dd%

    MINIMAL("§l§5R§da§9i§bn§ab§eo§6w§cRED§o§8\n§o§7On server: §a%servername%", "§7Players on network: §b%networkplayercounter%"),
    STANDARD("§l§5R§da§9i§bn§ab§eo§6w§cRED§o§8\n\n§o§7On server: §a%servername%\n§r§7Time§8: §e%time_HH:mm% §eUTC\n\n§r§7Playerlist:", "\n§7Players on network: §b%networkplayercounter%"),
    EXTENDED("§l§5R§da§9i§bn§ab§eo§6w§cRED§bNetwork\n\n§o§7On server: §a%servername%\n§r§7Time§8: §e%time_HH:mm% §eUTC §8| §7Date§8: §e%date_yyyy.MM.dd%\n\n§r§7Players on this server:",
            "\n§7Players on network: §b%networkplayercounter%\n§l§7Thank you for being %animation:rainbowlicious%§r§l§f!");

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
        time = time.replaceAll(":", "§7:§e");
        date = date.replaceAll("/", "§7/§e");
        return header.replace("%servername%", servername).replace("%time_HH:mm%", time).replace("%date_yyyy.MM.dd%", date);
    }

    public String getReplacedFooter(String animation, int networkcounter){
        return footer.replace("%networkplayercounter%", String.valueOf(networkcounter)).replace("%animation:rainbowlicious%", animation);
    }

}
