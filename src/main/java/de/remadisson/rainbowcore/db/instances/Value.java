package de.remadisson.rainbowcore.db.instances;

public class Value {

    //private String table;
    private final String key;
    private final Object value;

    public Value(String key, Object value){
        this.key = key;
        this.value = value;
    }


   // public String getTable() { return table; }

    public String getKey(){
        return key;
    }

    public String getValue(){
        return value.toString();
    }
}
