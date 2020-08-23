package de.remadisson.rainbowcore.sql;

import com.google.gson.JsonObject;
import com.google.gson.JsonParser;
import de.remadisson.rainbowcore.api.DataBaseAPI;
import de.remadisson.rainbowcore.api.MojangAPI;
import de.remadisson.rainbowcore.db.MySQL;
import de.remadisson.rainbowcore.db.enums.ColumnIndex;
import de.remadisson.rainbowcore.db.enums.ColumnType;
import de.remadisson.rainbowcore.db.instances.*;
import de.remadisson.rainbowcore.files;
import de.remadisson.rainbowcore.user.enums.UserTablist;
import de.remadisson.rainbowcore.user.instances.User;
import de.remadisson.rainbowcore.user.instances.UserSettings;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Date;
import java.util.UUID;

public class Database {
    public static MySQL mysql;
    public static final String tablename = "user_settings";


    public static MySQL DatabaseConnect() throws SQLException, ClassNotFoundException {
        login log = new login();
        MySQL mysql = new MySQL(log.getHost(), log.getPort(), log.getUser(), log.getPassword(), log.getDatabse());
        mysql.connect();
        createTable(mysql);
        return mysql;
    }

    public static void createTable(MySQL mysql) throws SQLException {
        Table table = new Table(tablename, new Index("uuid", ColumnIndex.UNIQUE), false, new Column("uuid", ColumnType.VARCHAR, 250, false),
                new Column("name", ColumnType.VARCHAR, 20, false), new Column("settings", ColumnType.TEXT, 1200, false), new Column("lastonline", ColumnType.TEXT, 30, false));

        DataBaseAPI api = new DataBaseAPI(mysql);
        api.createTable(table);
    }

    public static boolean userExists(UUID uuid) throws SQLException {
        DataBaseAPI api = new DataBaseAPI(mysql);
        return api.valueExists(tablename, "uuid", uuid);
    }

    public static void saveUser(User user) throws SQLException {
        DataBaseAPI api = new DataBaseAPI(mysql);
        if(!userExists(user.getUUID())){
            api.insertValues(tablename, new ArrayList<Value>(Arrays.asList(new Value("uuid", user.getUUID()) , new Value("name", user.getUsername()) ,new Value("settings", user.getSettings().getJSONString()), new Value("lastOnline", user.getSettings().getLastOnline()))));
        } else {
            api.updateValues(tablename, "uuid", user.getUUID(),  new ArrayList<Value>(Arrays.asList(new Value("settings", user.getSettings().getJSONString()), new Value("lastOnline", user.getSettings().getLastOnline()))));
        }

    }

    public static UserSettings getUserSettings(UUID uuid) throws SQLException{
        DataBaseAPI api = new DataBaseAPI(mysql);
        if(userExists(uuid)) {
            EntryRow er = api.getData(tablename, "uuid", uuid);

            final UserTablist[] tablist = {UserTablist.STANDART};
            final String[] lastOnline = {new Date().toString()};
            Arrays.stream(er.getValues()).forEach(value -> {
                if (value.getKey().equalsIgnoreCase("settings")) {
                    tablist[0] = UserTablist.valueOf(JsonParser.parseString(value.getValue()).getAsJsonObject().get("tablist").toString().replaceAll("\"", ""));
                } else if (value.getKey().equalsIgnoreCase("lastonline")) {
                    lastOnline[0] = value.getValue();
                }
            });
            return new UserSettings(tablist[0], lastOnline[0]);
        }
        return new UserSettings(UserTablist.STANDART, new Date().toString());
    }

}
