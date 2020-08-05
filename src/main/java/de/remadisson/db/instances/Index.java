package de.remadisson.db.instances;

import de.remadisson.db.enums.ColumnIndex;

public class Index {

    private String columnname;
    private ColumnIndex index;

    public Index(String columnname, ColumnIndex index){
        this.columnname = columnname;
        this.index = index;
    }

    public String getColumnName(){
        return columnname;
    }

    public ColumnIndex getIndex(){
        return index;
    }

    public String getColmnIndex(){
        return index.getColumnIndex() + "(`" + columnname + "`)";
    }
}
