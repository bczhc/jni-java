package pers.zhc.jni.sqlite;

import org.intellij.lang.annotations.Language;
import org.jetbrains.annotations.NotNull;
import pers.zhc.jni.JNI;
import pers.zhc.util.Assertion;

import java.util.ArrayList;
import java.util.concurrent.atomic.AtomicBoolean;

/**
 * @author bczhc
 */
public class SQLite3 {
    private long id;
    private boolean isClosed = false;
    private String databasePath;

    /**
     * SQLITE_ROW native defined value.
     */
    public static final int SQLITE_ROW = JNI.Sqlite3.SQLITE_ROW;

    private SQLite3() {
    }

    @NotNull
    public static SQLite3 open(String path) {
        final SQLite3 db = new SQLite3();
        db.id = JNI.Sqlite3.open(path);
        db.databasePath = path;
        return db;
    }

    public void close() {
        if (isClosed) {
            throw new SQLiteException("Already closed");
        }

        JNI.Sqlite3.close(this.id);
        isClosed = true;
    }

    /**
     * execute SQLite statement with callback
     *
     * @param cmd      statement
     * @param callback callback
     */
    public void exec(@Language("SQLite") String cmd, JNI.Sqlite3.SqliteExecCallback callback) {
        JNI.Sqlite3.exec(this.id, cmd, callback);
    }

    public void exec(@Language("SQLite") String cmd) {
        exec(cmd, null);
    }

    public void execBind(@Language("SQLite") String cmd, Object[] binds) {
        final Statement statement = compileStatement(cmd, binds);
        statement.step();
        statement.release();
    }

    public void key(String key) {
        JNI.Sqlite3.key(id, key);
    }

    public void rekey(String key) {
        JNI.Sqlite3.rekey(id, key);
    }

    public boolean isClosed() {
        return isClosed;
    }

    public boolean hasTable(String tableName) {
        AtomicBoolean r = new AtomicBoolean(false);
        try {
            exec("SELECT name FROM sqlite_master WHERE type='table' AND name='" + tableName + "';", contents -> {
                r.set(true);
                return 1;
            });
        } catch (Exception e) {
            e.printStackTrace();
        }
        return r.get();
    }

    /**
     * Get if a record exists.
     *
     * @param selectSql <p>SQLite select statement</p>
     * @return existence boolean
     */
    public boolean hasRecord(@Language("SQLite") String selectSql, Object[] binds) {
        final Statement statement = compileStatement(selectSql);
        if (binds != null) {
            statement.bind(binds);
        }
        boolean stepRow = statement.stepRow();
        statement.release();

        return stepRow;
    }

    public boolean hasRecord(@Language("SQLite") String selectSql) {
        return hasRecord(selectSql, null);
    }

    public boolean checkIfCorrupt() {
        return JNI.Sqlite3.checkIfCorrupt(id);
    }

    public Statement compileStatement(@Language("SQLite") String sql) {
        long statementId = JNI.Sqlite3.compileStatement(this.id, sql);
        return new Statement(statementId);
    }

    public Statement compileStatement(@Language("SQLite") String sql, @NotNull Object[] binds) {
        long statementId = JNI.Sqlite3.compileStatement(this.id, sql);
        final Statement statement = new Statement(statementId);
        statement.bind(binds);
        return statement;
    }

    public String getDatabasePath() {
        return databasePath;
    }

    public void beginTransaction() {
        this.exec("BEGIN TRANSACTION ");
    }

    public void commit() {
        this.exec("COMMIT ");
    }

    public int getRecordCount(String table) {
        final Statement statement = compileStatement("SELECT COUNT() FROM " + table);
        final Cursor cursor = statement.getCursor();
        Assertion.doAssertion(cursor.step());
        final int count = cursor.getInt(0);
        statement.release();

        return count;
    }

    public ArrayList<Schema> querySchema() {
        ArrayList<Schema> schemaList = new ArrayList<>();

        Statement statement = compileStatement("SELECT type, name, tbl_name, rootpage, sql FROM sqlite_master");
        Cursor cursor = statement.getCursor();
        while (cursor.step()) {
            String typeName = cursor.getText(0);
            String name = cursor.getText(1);
            String tableName = cursor.getText(2);
            int rootPage = cursor.getInt(3);
            String sql = cursor.getText(4);
            schemaList.add(new Schema(Schema.Type.fromString(typeName), name, tableName, rootPage, sql));
        }
        statement.release();

        return schemaList;
    }

    public ArrayList<ColumnInfo> queryTableInfo(String table) {
        ArrayList<ColumnInfo> columns = new ArrayList<>();
        Statement statement = this.compileStatement("SELECT cid, name, type, \"notnull\", dflt_value, pk\n" +
                "FROM pragma.pragma_table_info(?)");
        statement.bindText(1, table);
        Rows<ColumnInfo> rows = statement.queryRows(cursor -> new ColumnInfo(
                cursor.getInt(0),
                cursor.getText(1),
                cursor.getText(2),
                cursor.getInt(3),
                cursor.getText(4),
                cursor.getInt(5)
        ));
        ((Iterable<ColumnInfo>) () -> rows).forEach(columns::add);

        statement.release();
        return columns;
    }
}