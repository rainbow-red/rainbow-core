package de.remadisson.rainbowcore.db.instances;

import de.remadisson.rainbowcore.db.enums.ColumnType;

public class Column {

    private String name;
    private ColumnType type;
    private int length;
    private boolean nullable;

    public Column(String name, ColumnType type, int length, boolean nullable){
        this.name = name;
        this.type = type;
        this.length = length;
        this.nullable = nullable;
    }

    public String getName(){
        return name;
    }

    public String getType(){
        return type.getColumnType();
    }

    public int getLength(){
        return length;
    }

    public boolean isNullable() { return nullable; }

    public String getColumn(){
        return " `" + name + "` " + getType() + "("+length+")" + (nullable ? " NULL " : " NOT NULL ");
    }
}
