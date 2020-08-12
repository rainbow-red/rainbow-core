package de.remadisson.rainbowcore.db.instances;

public class EntryRow {

    private final String tableName, idKey;
    private final Object idValue;
    private final int row;
    private final Value[] values;

    public EntryRow(String tableName, int row, String idKey, Object idValue, Value... values){
            this.tableName = tableName;
            this.row = row;
            this.idKey = idKey;
            this. idValue = idValue;
            this.values = values;
    }

    public String getTableName(){
        return tableName;
    }

    public int getRow(){
        return row;
    }

    public String getIDKey(){
        return idKey;
    }

    public Object getIDValue(){
        return idValue;
    }

    public Value[] getValues(){
        return values;
    }
}
