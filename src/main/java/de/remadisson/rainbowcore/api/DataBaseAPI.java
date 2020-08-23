package de.remadisson.rainbowcore.api;

import de.remadisson.rainbowcore.db.MySQL;
import de.remadisson.rainbowcore.db.instances.EntryRow;
import de.remadisson.rainbowcore.db.instances.Table;
import de.remadisson.rainbowcore.db.instances.UpdateValue;
import de.remadisson.rainbowcore.db.instances.Value;

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
    public boolean valueExists(String tableName, String key, Object value) throws SQLException, NullPointerException {
        ResultSet rs = mysql.query("SELECT * FROM `"+ tableName +"` WHERE `" + key + "` LIKE '" + value.toString() + "'");

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
    public void createTable(Table table) throws SQLException {
        mysql.update(table.getCreateTableString());
    }

    /**
     * Drops a certain Table
     * @param tableName
     * @throws SQLException
     */
    public void dropTable(String tableName) throws SQLException {
        mysql.update("DROP TABLE ` " + tableName + " `");
    }

    /**
     * Inserts a Single Value
     * @param value
     */
    public void insertValue(String tablename, Value value, String idkey, String idvalue) throws SQLException {
        mysql.update("INSERT INTO `" + tablename + "` ('"+ value.getKey() +"') VALUES ('" + value.getValue() + "')");
    }

    /**
     * Inserts a List of Values
     * @param valueList
     */
    public void insertValues(String tablename, ArrayList<Value> valueList) throws SQLException {
      String sql = "INSERT INTO `" + tablename + "` ";
      String keys = "( `" + valueList.get(0).getKey() + "`";
      String values = ") VALUES ('"  + valueList.get(0).getValue() + "'";

      for(Value value : valueList){
          if(value != valueList.get(0)){

              keys += " , `"  + value.getKey() + "`";
              values += " , '" + value.getValue() + "'";

          }
      }
      // Add values to the SQL-Syntax
      sql += keys + values + ")";
      mysql.update(sql);
    }

    /**
     * Updates a certain Value
     * @param value
     */
    public void updateValue(UpdateValue value) throws SQLException{
        mysql.update("UPDATE `" + value.getTable() + "` SET `"+value.getKey()+"`='"+value.getValue()+"' WHERE `" + value.getIDKey() + "` LIKE '" + value.getIDValue() + "'");
    }

    /**
     * Updates multiple Values at once
     * @param tablename
     * @param idkey
     * @param idvalue
     * @param valueList
     */
    public void updateValues(String tablename, String idkey, Object idvalue, ArrayList<Value> valueList){
        String sql = "UPDATE `" + tablename + "` SET `" + valueList.get(0).getKey()+ "`='" + valueList.get(0).getValue() + "' ";

        for(Value value : valueList){
            if(value != valueList.get(0)){

                sql += " , `" + valueList.get(0).getKey()+ "`='" + valueList.get(0).getValue() + "' ";

            }
        }
        sql += " WHERE `" + idkey + "` LIKE '" + idvalue.toString() + "'" ;
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
    public EntryRow getData(String TableName, String idKey, Object idValue) throws SQLException, NullPointerException {
        if(!valueExists(TableName, idKey, idValue.toString())) return null;

        ResultSet rs = mysql.query("SELECT * FROM `" + TableName + "` WHERE `" + idKey + "` LIKE '" + idValue + "'");

        if(rs.next()) {
            Value[] values = new Value[rs.getMetaData().getColumnCount()];

            for(int columnIndex = 1; columnIndex <= rs.getMetaData().getColumnCount(); columnIndex++){
                int javaIndex = columnIndex-1;
                String key = rs.getMetaData().getColumnName(columnIndex);
                Object value = rs.getObject(columnIndex);

                values[javaIndex] = new Value(key, value);
            } // END OF FOR LOOP

            return new EntryRow(TableName, rs.getRow(), idKey, idValue, values);
        } // END OF rs.next IF

        return null;
    }

}
