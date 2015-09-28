package com.fiosys.expensor.transactions;

import android.content.ContentValues;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;

import com.fiosys.expensor.accounts.Account;
import com.fiosys.expensor.db.ExpensorDatabaseHelper;
import com.j256.ormlite.dao.BaseDaoImpl;
import com.j256.ormlite.dao.CloseableIterator;
import com.j256.ormlite.stmt.QueryBuilder;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.DatabaseTableConfig;

import java.sql.SQLException;

/**
 * Created by Rehan on 20/9/15.
 */
public class TransactionDAO extends BaseDaoImpl<Transaction, Long>/*AbstractDB<Transaction>*/ {

    private static final String TABLE_NAME = "TRANSACTIONS";
    private static final String COL_ID = "_id";
    private static final String COL_DATE = "date";
    private static final String COL_DEBIT_ACC = "debitAccount";
    private static final String COL_CREDIT_ACC = "creditAccount";
    private static final String COL_TYPE = "type";
    private static final String COL_CATEGORY = "category";
    private static final String COL_AMOUNT = "amount";

    public TransactionDAO(ConnectionSource connectionSource,
                   DatabaseTableConfig<Transaction> tableConfig) throws SQLException {

        super(connectionSource, tableConfig);
    }

    public CloseableIterator<Transaction> getIteratorForCursorAdapter() throws SQLException {
        Cursor cursor = null;
        QueryBuilder<Transaction, Long> qb = queryBuilder();
        qb.where().isNotNull(COL_ID);
        return iterator(qb.prepare());


    }
}
