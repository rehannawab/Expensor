package com.fiosys.expensor.db;

import android.content.Context;
import android.database.sqlite.SQLiteDatabase;

import com.fiosys.expensor.R;
import com.fiosys.expensor.accounts.Account;
import com.fiosys.expensor.accounts.AccountDAO;
import com.fiosys.expensor.category.Category;
import com.fiosys.expensor.category.CategoryDAO;
import com.fiosys.expensor.transactions.Transaction;
import com.fiosys.expensor.transactions.TransactionDAO;
import com.j256.ormlite.android.apptools.OrmLiteSqliteOpenHelper;
import com.j256.ormlite.dao.Dao;
import com.j256.ormlite.support.ConnectionSource;
import com.j256.ormlite.table.TableUtils;

import java.sql.SQLException;

/**
 * Created by Rehan on 18/9/15.
 */
public class ExpensorDatabaseHelper extends OrmLiteSqliteOpenHelper/*SQLiteOpenHelper*/ {

    private static ExpensorDatabaseHelper sInstance;

    private static final String DB_NAME = "expensor"; // the name of our database
    private static final int DB_VERSION = 1;

    private static CategoryDAO categoryDao;
    private static AccountDAO accountDao;
    private static TransactionDAO transactionDAO;

    public static synchronized ExpensorDatabaseHelper getInstance(Context context) {

        // Use the application context, which will ensure that you
        // don't accidentally leak an Activity's context.
        // See this article for more information: http://bit.ly/6LRzfx
        if (sInstance == null) {
            sInstance = new ExpensorDatabaseHelper(context.getApplicationContext());
        }
        return sInstance;
    }

    private ExpensorDatabaseHelper(Context context) {
        super(context, DB_NAME, null, DB_VERSION, R.raw.ormlite_config);
    }

    @Override
    public void onCreate(SQLiteDatabase database, ConnectionSource connectionSource) {
        updateMyDatabase(database, 0, DB_VERSION, connectionSource);
    }

    @Override
    public void onUpgrade(SQLiteDatabase database, ConnectionSource connectionSource, int oldVersion, int newVersion) {

    }

//    @Override
//    public void onCreate(SQLiteDatabase db) {
//        updateMyDatabase(db, 0, DB_VERSION);
//    }
//
//    @Override
//    public void onUpgrade(SQLiteDatabase db, int oldVersion, int newVersion) {
//
//    }

    private void updateMyDatabase(SQLiteDatabase db, int oldVersion, int newVersion, ConnectionSource connectionSource){

        if(oldVersion < 1){

//            db.execSQL("CREATE TABLE TRANSACTIONS (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    + "DATE INTEGER, "
//                    + "DEBIT_ACC INTEGER, "
//                    + "CREDIT_ACC INTEGER, "
//                    + "TYPE TEXT, "
//                    + "CATEGORY INTEGER, "
//                    + "AMOUNT INTEGER);");

//            db.execSQL("CREATE TABLE ACCOUNT (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    + "NAME TEXT UNIQUE, "
//                    + "BALANCE REAL);");

            try {
                TableUtils.createTable(connectionSource, Category.class);
                TableUtils.createTable(connectionSource, Account.class);
                TableUtils.createTable(connectionSource, Transaction.class);
            } catch (SQLException e) {
                e.printStackTrace();
            }

//            db.execSQL("CREATE TABLE CATEGORY (_id INTEGER PRIMARY KEY AUTOINCREMENT, "
//                    + "NAME TEXT , "
//                    + "TYPE TEXT , "
//                    + "UNIQUE(NAME, TYPE));");

            createCategories();

        }

    }

    private void createCategories(){

        try {
            getCategoryDao().create(new Category("Food etc.", "Expense"));
            getCategoryDao().create(new Category("Rent", "Expense"));
            getAccountDao().create(new Account("Axis", 25000.00));
        } catch (SQLException e) {
            e.printStackTrace();
        }

//        CategoryDAO categoryDB = new CategoryDAO(db);
//        List<Category> categories = new ArrayList<Category>();
//
//        categories.add(new Category("Salary", "Income"));
//        categories.add(new Category("Food", "Expense"));
//        categories.add(new Category("Rent", "Expense"));
//        categories.add(new Category("Laundry", "Expense"));
//
//        categoryDB.storeBatch(categories);




    }

    public CategoryDAO getCategoryDao() throws SQLException {
        if(categoryDao == null) {
            categoryDao = getDao(Category.class);
        }
        return categoryDao;
    }

    public AccountDAO getAccountDao() throws SQLException {
        if(accountDao == null) {
            accountDao = getDao(Account.class);
        }
        return accountDao;
    }

    public TransactionDAO getTransactionDao() throws SQLException {
        if(transactionDAO == null) {
            transactionDAO = getDao(Transaction.class);
        }
        return transactionDAO;
    }

}
