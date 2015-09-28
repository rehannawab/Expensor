package com.fiosys.expensor.category;


import android.database.Cursor;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

/**
 * Created by Rehan on 18/9/15.
 */
public class CategoryDAO extends BaseDaoImpl<Category, Long> /*AbstractDB<Category>*/ {

    public static final String COL_ID = "_id";

    public static final String TABLE_NAME = "CATEGORY";

    public static final String COL_NAME = "name";

    public static final String COL_TYPE = "type";

    public CategoryDAO(ConnectionSource connectionSource,
                       DatabaseTableConfig<Category> tableConfig) throws SQLException {

        super(connectionSource, tableConfig);
    }

    public CloseableIterator<Category> getIteratorForCursorAdapter() throws SQLException {
        Cursor cursor = null;
        QueryBuilder<Category, Long> qb = queryBuilder();
        qb.where().isNotNull(COL_ID);
        return iterator(qb.prepare());


    }


}
