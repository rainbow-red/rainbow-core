package de.remadisson.db.instances;

public class Value {

    private String key;
    private Object value;

    public Value(String key, Object value){
        this.key = key;
        this.value = value;
    }

    public String getKey(){
        return key;
    }

    public String getValue(){
        return value.toString();
    }
}
