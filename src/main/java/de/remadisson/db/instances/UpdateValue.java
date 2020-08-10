package de.remadisson.db.instances;

public class Value {

    private final String table;
    private final String idKey;
    private final Object idValue;
    private final String key;
    private final Object value;

    public Value(String table, String idKey, Object idValue, String key, Object value){
        this.table = table;
        this.idKey = idKey;
        this.idValue = idValue;
        this.key = key;
        this.value = value;
    }

    public String getTable() { return table; }

    public String getIDKey() { return idKey; }

    public Object getIDValue() { return idValue; }

    public String getKey(){
        return key;
    }

    public String getValue(){
        return value.toString();
    }
}
