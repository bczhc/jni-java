package pers.zhc.jni.sqlite;

public class ColumnInfo {
    public final int columnId;
    public final String name;
    public final String type;
    public final int notNull;
    public final String defaultValue;
    public final int primaryKey;

    public ColumnInfo(int columnId, String name, String type, int notNull, String defaultValue, int primaryKey) {
        this.columnId = columnId;
        this.name = name;
        this.type = type;
        this.notNull = notNull;
        this.defaultValue = defaultValue;
        this.primaryKey = primaryKey;
    }
}
