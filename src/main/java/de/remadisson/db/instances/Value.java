package de.remadisson.db.instances;

public class Value {

    private final String table;
    private final String key;
    private final Object value;

    public Value(String table, String key, Object value){
        this.table = table;
        this.key = key;
        this.value = value;
    }

    public String getTable() { return table; }

    public String getKey(){
        return key;
    }

    public String getValue(){
        return value.toString();
    }
}
