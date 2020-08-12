package de.remadisson.rainbowcore.db.enums;

public enum ColumnType {

    // NUMERIC
    BIT("BIT"), TINYINT("TINYINT"), SMALLINT("SMALLINT"), INT("INT"), BIGINT("BIGINT"), DECIMAL("DECIMAL"), NUMERIC("NUMERIC"), FLOAT("FLOAT"), REAL("REAL"),

    // DATE/TIME
    DATE("DATE"), TIME("TIME"), DATETIME("DATETIME"), TIMESTAMP("TIMESTAMP"), YEAR("YEAR"),

    // Character/String
    CHAR("CHAR"), VARCHAR("VARCHAR"), TEXT("TEXT"),

    // Unicode Character/String
    NCHAR("NCHAR"), NVarchar("NVarchar"), NText("NText"),

    // Binary
    BINARY("BINARY"), VARBINARY("VARBINARY"), IMAGE("IMAGE"),

    // Miscellaneuous
    CLOB("CLOB"), BLOB("BLOB"), XML("XML"), JSON("JSON");

    private String columnType;

    ColumnType(String columnType){
        this.columnType = columnType;
    }

    public String getColumnType(){
        return columnType;
    }
}
