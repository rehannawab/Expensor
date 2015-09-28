package com.fiosys.expensor.accounts;



import android.database.Cursor;

import com.fiosys.expensor.category.Category;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

/**
 * Created by root on 20/9/15.
 */
public class AccountDAO extends BaseDaoImpl<Account, Long>/*AbstractDB<Account>*/ {

    public static final String TABLE_NAME = "ACCOUNT";
    public static final String COL_ID = "_id";
    public static final String COL_NAME = "name";
    public static final String COL_BALANCE = "balance";

    public AccountDAO(ConnectionSource connectionSource,
                      DatabaseTableConfig<Account> tableConfig) throws SQLException {

        super(connectionSource, tableConfig);
    }

    public CloseableIterator<Account> getIteratorForCursorAdapter() throws SQLException {
        Cursor cursor = null;
        QueryBuilder<Account, Long> qb = queryBuilder();
        qb.where().isNotNull(COL_ID);
        return iterator(qb.prepare());


    }
}
