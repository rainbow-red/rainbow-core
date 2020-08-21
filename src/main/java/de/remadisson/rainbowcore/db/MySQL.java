package de.remadisson.rainbowcore.db;

import de.remadisson.rainbowcore.files;

import javax.sound.midi.Soundbank;
import java.sql.*;

public class MySQL {

    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final String database;

    private Connection con;

    public MySQL(String host, int port, String user, String password, String database) {
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    public Connection connect() throws SQLException, NullPointerException {
        Connection con = null;
        if (!isConnected()) {
            try {
                Class.forName("com.mysql.cj.jdbc.Driver");
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true&useUnicode=true&useJDBCCompliantTimezoneShift=true&useLegacyDatetimeCode=false&serverTimezone=UTC", user, password);

        }

        return con;
    }

    public MySQL disconnect() throws SQLException, NullPointerException {
        if (isConnected()) {
            con.close();
        }

        return this;
    }

    public boolean isConnected() throws SQLException {
        if (con != null && !con.isClosed()) {
            return con.isValid(30*60*1000);
        } else {
            return false;
        }
    }

    public MySQL update(String query) throws SQLException {
        if (!isConnected()) {
            try {
                reconnect();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        PreparedStatement s = con.prepareStatement(query);
        s.executeUpdate();
        s.close();
        return this;
    }

    public ResultSet query(String query) throws SQLException {

        if (!isConnected()) {
            try {
                reconnect();
            } catch (ClassNotFoundException e) {
                e.printStackTrace();
            }
        }

        PreparedStatement ps = con.prepareStatement(query);
        ps.executeQuery();
        ResultSet rs = ps.getResultSet();
        return rs;
    }

    public void reconnect() throws SQLException, ClassNotFoundException, NullPointerException {
        if (isConnected()) {
            disconnect();
        }
        con = connect();
    }
}
