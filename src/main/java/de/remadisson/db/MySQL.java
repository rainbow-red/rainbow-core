package de.remadisson.db;

import java.sql.*;

public class MySQL {

    private final String host;
    private final int port;
    private final String user;
    private final String password;
    private final String database;

    private Connection con;

    public MySQL(String host, int port, String user, String password, String database){
        this.host = host;
        this.port = port;
        this.user = user;
        this.password = password;
        this.database = database;
    }

    public Connection connect(){
        Connection con = null;
        if (!isConnected()) {
            try {
                Class.forName("com.mysql.jdbc.Driver");
                con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
            } catch (ClassNotFoundException | SQLException ex) {
                ex.printStackTrace();
            }
        }

        return con;
    }

    public MySQL disconnect(){
        if (isConnected()) {
            try {
                con.close();
            } catch (SQLException e) {
                e.printStackTrace();
            }
        }
        return this;
    }

    public boolean isConnected(){
        try {
            if(!con.isClosed()) {
                return query("SELECT 1") != null;
            } else {
                return false;
            }
        } catch (NullPointerException | SQLException ex) {
            return false;
        }
    }

    public MySQL update(String query) throws SQLException {
            PreparedStatement s = con.prepareStatement(query);
            s.executeUpdate();
            s.close();
        return this;
    }

    public ResultSet query(String query) throws SQLException{
            PreparedStatement ps = con.prepareStatement(query);
            ps.executeQuery();
            ResultSet rs = ps.getResultSet();
            return rs;
    }

    public void reconnect(){
        if (isConnected()) {
            disconnect();
        }
        con = connect();
    }
}
