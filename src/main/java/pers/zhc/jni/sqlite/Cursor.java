package pers.zhc.jni.sqlite;

import pers.zhc.jni.JNI;

/**
 * @author bczhc
 */
public class Cursor {
    private final long cursorId;
    /**
     * for the call of {@link Statement#checkIfReleased()}
     */
    private final Statement statement;

    public Cursor(long cursorId, Statement statement) {
        this.cursorId = cursorId;
        this.statement = statement;
    }

    public void reset() {
        statement.checkIfReleased();
        JNI.Sqlite3.Cursor.reset(cursorId);
    }

    /**
     * Step.
     * like Java iterator
     *
     * @return next
     */
    public boolean step() {
        statement.checkIfReleased();
        return JNI.Sqlite3.Cursor.step(cursorId);
    }

    /**
     * Get blob data.
     * @param column column, the leftmost is 0.
     * @return data
     */
    public byte[] getBlob(int column) {
        statement.checkIfReleased();
        return JNI.Sqlite3.Cursor.getBlob(cursorId, column);
    }

    /**
     * Get text.
     * @param column column, the leftmost is 0.
     * @return text string
     */
    public String getText(int column) {
        statement.checkIfReleased();
        return JNI.Sqlite3.Cursor.getText(cursorId, column);
    }

    /**
     * Get double value.
     * @param column column, the leftmost is 0.
     * @return double value
     */
    public double getDouble(int column) {
        statement.checkIfReleased();
        return JNI.Sqlite3.Cursor.getDouble(cursorId, column);
    }

    /**
     * Get float value.
     * @param column column, the leftmost is 0.
     * @return float value
     */
    public float getFloat(int column) {
        statement.checkIfReleased();
        return (float) JNI.Sqlite3.Cursor.getDouble(cursorId, column);
    }

    /**
     * Get long value.
     * @param column column, the leftmost is 0.
     * @return long value.
     */
    public long getLong(int column) {
        statement.checkIfReleased();
        return JNI.Sqlite3.Cursor.getLong(cursorId, column);
    }

    /**
     * Get integer value.
     * @param column column, the leftmost is 0.
     * @return integer value
     */
    public int getInt(int column) {
        statement.checkIfReleased();
        return JNI.Sqlite3.Cursor.getInt(cursorId, column);
    }
}
