package pers.zhc.jni.sqlite;

/**
 * @author bczhc
 */
public class SQLiteException extends RuntimeException {
    public SQLiteException() {
    }

    public SQLiteException(String message) {
        super(message);
    }
}
