package de.remadisson.api;

import de.remadisson.db.MySQL;
import de.remadisson.db.instances.Table;
import de.remadisson.db.instances.Value;

import java.sql.ResultSet;
import java.sql.SQLException;

public class DataBaseAPI {

    private MySQL mysql;

    public DataBaseAPI(MySQL mysql){
        this.mysql = mysql;
    }

    /**
     * Returns the MySQL. To have more arranging possibilities.
     * @return
     */
    public MySQL getMySQL(){
        return mysql;
    }

    /**
     * Allows to change the given MySQL.
     * @param mysql
     */
    public void setMySQL(MySQL mysql){
        this.mysql = mysql;
    }


    /*
        Actual SQL-Translation with rawSQL
     */

    public void updateRawSQL(String sql) throws SQLException {
        // Directly inserting from the Raw
        mysql.update(sql);
    }

    public ResultSet queryRawSQL(String sql) throws SQLException{
        // Raw Callback for the Raw feeling! ^^
        return mysql.query(sql);
    }

    /*
        Actual SQL-Translation just limited with fixed methods
     */

    public void createTable(Table table){
    }

    public void dropTable(String tableName){
    }

    public void insertValues(Value value){
    }

    public void updateValue(Value value){
    }

    public void deleteValues(Value value){
    }

    public Object getValues(){
        return null;
    }
}
