package com.fiosys.expensor.db;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by root on 20/9/15.
 */
public abstract class AbstractDB<T> {

    protected SQLiteDatabase db;

    public AbstractDB(SQLiteDatabase db) {
        this.db = db;
    }



    public abstract String getTableName();

    public abstract T getRecord(Long id);

    protected abstract String[] getColsAsStringArray();

    protected abstract void populateContentValuesForInsert(ContentValues contentValues, T record);

    public List<Long> storeBatch(List<T> records){

        List<Long> ids = new ArrayList<Long>();
        for(T record : records){

            ids.add(store(record));

        }

        return ids;
    }

    public Long store(T record) {

        return db.insert(getTableName(), null, getContentValues(record));

    }

    public int update(Long id, T record){

        return db.update(getTableName(),
                getContentValues(record),
                "_id = ?",
                new String[]{id.toString()});

    }

    private ContentValues getContentValues(T record){

        ContentValues contentValues = new ContentValues();

        populateContentValuesForInsert(contentValues, record);

        return contentValues;

    }

    protected Cursor getCursorForUniqueRecord(Long id){
        return db.query(getTableName(),
                getColsAsStringArray(),
                "_id = ?",
                new String[]{id.toString()},
                null,
                null,
                null);
    }

    public boolean delete(Long id){

        int rows = db.delete(getTableName(),
                "_id = ?",
                new String[]{id.toString()});

        return rows>0;

    }


}
