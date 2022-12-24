package pers.zhc.jni.sqlite;

import org.jetbrains.annotations.Contract;
import org.jetbrains.annotations.NotNull;

public class Schema {
    public final Type type;
    public final String name;
    public final String tableName;
    public final int rootPage;
    public final String sql;

    public enum Type {
        TABLE, INDEX, VIEW, TRIGGER;

        @Contract(pure = true)
        public static Type fromString(@NotNull String value) {
            switch (value) {
                case "table":
                    return Type.TABLE;
                case "index":
                    return Type.INDEX;
                case "view":
                    return Type.VIEW;
                case "trigger":
                    return Type.TRIGGER;
                default:
                    throw new RuntimeException("Unexpected type name: " + value);
            }
        }
    }

    public Schema(Type type, String name, String tableName, int rootPage, String sql) {
        this.type = type;
        this.name = name;
        this.tableName = tableName;
        this.rootPage = rootPage;
        this.sql = sql;
    }

    @Override
    public String toString() {
        return "Schema{" +
                "type=" + type +
                ", name='" + name + '\'' +
                ", tableName='" + tableName + '\'' +
                ", rootPage=" + rootPage +
                ", sql='" + sql + '\'' +
                '}';
    }
}
