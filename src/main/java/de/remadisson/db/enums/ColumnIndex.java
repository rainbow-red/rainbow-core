package de.remadisson.db.enums;

public enum ColumnIndex {

    PRIMARY("PRIMARY KEY"), UNIQUE("UNIQUE"), INDEX("INDEX"), FULLTEXT("FULLTEXT"), SPATAL("SPATAL");

    private String ColumnIndex;

    ColumnIndex(String ColumnIndex){
        this.ColumnIndex = ColumnIndex;
    }

    public String getColumnIndex(){
        return ColumnIndex;
    }
}
