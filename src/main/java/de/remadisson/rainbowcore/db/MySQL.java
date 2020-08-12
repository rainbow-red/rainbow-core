package de.remadisson.rainbowcore.db;

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

    public Connection connect() throws ClassNotFoundException, SQLException, NullPointerException {
        Connection con = null;
        if (!isConnected()) {
            Class.forName("com.mysql.jdbc.Driver");
            con = DriverManager.getConnection("jdbc:mysql://" + host + ":" + port + "/" + database + "?autoReconnect=true", user, password);
        }

        return con;
    }

    public MySQL disconnect() throws SQLException, NullPointerException{
        if (isConnected()) {
                con.close();
        }

        return this;
    }

    public boolean isConnected() throws SQLException, NullPointerException {
        if (!con.isClosed()) {
            return query("SELECT 1") != null;
        } else {
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

    public void reconnect() throws SQLException, ClassNotFoundException, NullPointerException{
        if (isConnected()) {
            disconnect();
        }
        con = connect();
    }
}
