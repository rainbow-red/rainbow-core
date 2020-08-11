package de.remadisson.api;

import de.remadisson.db.MySQL;
import de.remadisson.db.instances.EntryRow;
import de.remadisson.db.instances.Table;
import de.remadisson.db.instances.UpdateValue;
import de.remadisson.db.instances.Value;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;

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

    /**
     * Returns true if a certain value in a certain table exists.
     * @param tableName
     * @param key
     * @param value
     * @return
     * @throws SQLException
     * @throws NullPointerException
     */
    public boolean valueExists(String tableName, String key, String value) throws SQLException, NullPointerException{
        ResultSet rs = mysql.query("SELECT * FROM '"+ tableName +"' WHERE " + key + " LIKE '" + value + "'");

        // Gets the next line from the table
        if(rs.next()){
            // return true if the String is available
            return rs.getString(key) != null;
        }

        // Value and Key are not identified
        return false;
    }

    /**
     * Creates a new Table
     * @param table
     * @throws SQLException
     */
    public void createTable(Table table) throws SQLException{
        mysql.update(table.getCreateTableString());
    }

    /**
     * Drops a certain Table
     * @param tableName
     * @throws SQLException
     */
    public void dropTable(String tableName) throws SQLException{
        mysql.update("DROP TABLE ` " + tableName + " `");
    }

    /**
     * Inserts a Single Value
     * @param value
     */
    public void insertValue(Value value) throws SQLException{
        mysql.update("INSERT INTO `" + value.getTable() + "`('"+ value.getKey() +"') VALUES ('" + value.getValue() + "')" );
    }

    /**
     * Inserts a List of Values
     * @param valueList
     */
    public void insertValues(ArrayList<Value> valueList) throws SQLException{
        for(Value value : valueList){
            insertValue(value);
        }
    }

    /**
     * Updates a certain Value
     * @param value
     */
    public void updateValue(UpdateValue value) throws SQLException{
        mysql.update("UPDATE `" + value.getTable() + "` SET `"+value.getKey()+"`='"+value.getValue()+"' WHERE `" + value.getIDKey() + "` LIKE '" + value.getIDValue() + "'");
    }

    /**
     * Deletes / Drops certain values
     * @param TableName
     * @param key
     * @param value
     */
    public void deleteValues(String TableName, String key, String value) throws SQLException{
        mysql.update("DELETE FROM `" + TableName + "` WHERE `" + key + "` LIKE '" + value + "'");
    }

    /**
     * Returns Data / GETS DATA
     * @return
     */
    public ResultSet getDataRaw(String TableName, String idKey, Object idValue) throws SQLException{
        return mysql.query("SELECT * FROM `" + TableName + "` WHERE `" + idKey + "` LIKE '" + idValue +"'");
    }

    /**
     * Returns Data / GETS DATA
     * @return
     */
    public EntryRow getData(String TableName, String idKey, Object idValue) throws SQLException{
        if(!valueExists(TableName, idKey, idValue.toString())) return null;

        ResultSet rs = mysql.query("SELECT * FROM `" + TableName + "` WHERE `" + idKey + "` LIKE '" + idValue + "'");

        if(rs.next()) {
            Value[] values = {null};
            for(int columnIndex = 0; columnIndex < rs.getMetaData().getColumnCount(); columnIndex++){
                values[columnIndex] = new Value(TableName, rs.getMetaData().getColumnName(columnIndex), rs.getObject(columnIndex));
            } // END OF FOR LOOP

            return new EntryRow(TableName, rs.getRow(), idKey, idValue, values);
        } // END OF rs.next IF

        return null;
    }

}
