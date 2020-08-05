package de.remadisson.db.instances;

import de.remadisson.db.instances.Column;
import de.remadisson.db.instances.Index;

import java.util.Arrays;

public class Table {

    private String tablename;
    private Index index;
    private Column[] columns;
    private boolean overwrite;

    public Table(String tablename, Index index, boolean overwrite, Column... columns){
        this.tablename = tablename;
        this.index = index;
        this.columns = columns;
        this.overwrite = overwrite;
    }

    public String getTableName(){
        return tablename;
    }

    public Index getIndex(){
        return index;
    }

    public Column[] getColumns(){
        return columns;
    }

    public String getColumnsAsString(){
        String[] columnString = {""};
        Arrays.stream(columns).forEach(v -> {
            if(columnString[0].equalsIgnoreCase("")){
                columnString[0] += v.getColumn();
            } else {
                columnString[0] += "," + v.getColumn();
            }
        });
        return columnString[0];
    }

    public boolean isOverWrite(){
        return overwrite;
    }

    public String getCreateTableString(){
        return "CREATE TABLE " + (overwrite ? "" : "IF NOT EXISTS") + " `" + tablename +"` ("+ getColumnsAsString() +", " + index.getColmnIndex() + ");";
    }
}
