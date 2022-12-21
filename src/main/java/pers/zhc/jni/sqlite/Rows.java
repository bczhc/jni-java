package pers.zhc.jni.sqlite;

import java.util.Iterator;

public class Rows<T> implements Iterator<T> {
    private final Cursor cursor;
    private final Statement.RowMapper<T> rowMapper;

    public Rows(Cursor cursor, Statement.RowMapper<T> rowMapper) {
        this.cursor = cursor;
        this.rowMapper = rowMapper;
    }

    @Override
    public boolean hasNext() {
        return cursor.step();
    }

    @Override
    public T next() {
        return rowMapper.mapRow(cursor);
    }
}
