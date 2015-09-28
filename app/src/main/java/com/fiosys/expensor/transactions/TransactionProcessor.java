package com.fiosys.expensor.transactions;

import android.content.Context;

import com.fiosys.expensor.accounts.Account;
import com.fiosys.expensor.accounts.AccountDAO;
import com.fiosys.expensor.db.ExpensorDatabaseHelper;

import java.sql.SQLException;

/**
 * Created by root on 26/9/15.
 */
public class TransactionProcessor {

    Context context;
    TransactionDAO transactionDAO;
    AccountDAO accountDAO;

    TransactionProcessor(Context context) throws SQLException {

        this.context = context;
        this.transactionDAO = ExpensorDatabaseHelper.getInstance(context).getTransactionDao();
        this.accountDAO = ExpensorDatabaseHelper.getInstance(context).getAccountDao();

    }

    public int create(Transaction transaction) throws SQLException {

       int result = transactionDAO.create(transaction);

        updateAccounts(transaction);

        return result;

    }

    private void updateAccounts(Transaction transaction) throws SQLException {

        transaction.processTransaction();

        Account creditAccount = transaction.getCreditAccount();
        Account debitAccount = transaction.getDebitAccount();

        if(creditAccount != null){
            accountDAO.update(creditAccount);
        }
        if(debitAccount != null){
            accountDAO.update(debitAccount);
        }

    }

}
