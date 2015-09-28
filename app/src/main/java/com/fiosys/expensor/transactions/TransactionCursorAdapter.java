package com.fiosys.expensor.transactions;

import android.content.Context;
import android.database.Cursor;
import android.os.AsyncTask;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.fiosys.expensor.R;
import com.fiosys.expensor.db.ExpensorDatabaseHelper;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.util.Calendar;

/**
 * Created by root on 26/9/15.
 */
public class TransactionCursorAdapter extends CursorAdapter {

    LayoutInflater mInflater;

    public TransactionCursorAdapter(Context context, Cursor cursor, int flags){

        super(context, cursor, flags);

        mInflater = (LayoutInflater) context.getSystemService(Context.LAYOUT_INFLATER_SERVICE);


    }

    @Override
    public View newView(Context context, Cursor cursor, ViewGroup parent) {

        View layout = mInflater.inflate(R.layout.transaction_list_item,parent,false);

        View compressedLayout = layout.findViewById(R.id.transaction_compressed);

        ((LinearLayout.LayoutParams) compressedLayout.getLayoutParams()).bottomMargin = 0;
        compressedLayout.setVisibility(View.VISIBLE);


        View expandedLayout = layout.findViewById(R.id.transaction_expanded);

        ((LinearLayout.LayoutParams) expandedLayout.getLayoutParams()).bottomMargin = -250;
        expandedLayout.setVisibility(View.GONE);

        return layout;
    }

    @Override
    public void bindView(View view, Context context, Cursor cursor) {

        Long id = cursor.getLong(0);
        new GetTransaction(view, context).execute(id);



    }

    public void bindViewNow(View view, Context context, Transaction transaction){

        TextView amount = (TextView) view.findViewById(R.id.transaction_amount);
        TextView type   = (TextView) view.findViewById(R.id.transaction_type);
        TextView date   = (TextView) view.findViewById(R.id.transaction_date);

        /* Expanded View */
        TextView date_expanded = (TextView) view.findViewById(R.id.transaction_date_expanded);
        TextView type_expanded = (TextView) view.findViewById(R.id.transaction_type_expanded);
        TextView account_expanded = (TextView) view.findViewById(R.id.transaction_account_expanded);
        TextView category_expanded = (TextView) view.findViewById(R.id.transaction_category_expanded);
        TextView amount_expanded = (TextView) view.findViewById(R.id.transaction_amount_expanded);

        /* Transfer */
        TextView account_from_expanded = (TextView) view.findViewById(R.id.transaction_account_from_expanded);
        TextView account_to_expanded = (TextView) view.findViewById(R.id.transaction_account_to_expanded);

        /* Layout */

        LinearLayout accountLayout = (LinearLayout) view.findViewById(R.id.account_layout);
        LinearLayout accountTransferLayout = (LinearLayout) view.findViewById(R.id.account_transfer_layout);
        LinearLayout categoryExpanded = (LinearLayout) view.findViewById(R.id.category_expanded);

        /* Setting up Calendar instance */
        Calendar calendar = Calendar.getInstance();
        calendar.setTime(transaction.getDate());

        /* Setting Views */
        amount.setText(new BigDecimal(transaction.getAmount()).toPlainString());
        type.setText(transaction.getType() != null ? transaction.getType().toString() : "null");
        date.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/" + calendar.get(Calendar.MONTH) + "/" + calendar.get(Calendar.YEAR));

        /* Setting Expanded Views */
        date_expanded.setText(calendar.get(Calendar.DAY_OF_MONTH) + "/"
                + calendar.get(Calendar.MONTH)
                + "/" + calendar.get(Calendar.YEAR)
                + "   "
                + calendar.get(Calendar.HOUR_OF_DAY)
                + " : "
                + calendar.get(Calendar.MINUTE)
                + " : "
                + calendar.get(Calendar.SECOND));
        type_expanded.setText(transaction.getType() != null? transaction.getType().toString() : "");

        amount_expanded.setText(new BigDecimal(transaction.getAmount()).toPlainString());

        category_expanded.setText(transaction.getCategory() != null?  transaction.getCategory().getName() : "");

        if(transaction.getType() == Transaction.Type.INCOME){
            account_expanded.setText(transaction.getCreditAccount().getName());
        }
        if(transaction.getType() == Transaction.Type.EXPENSE){
            account_expanded.setText(transaction.getDebitAccount().getName());
        }
        if(transaction.getType() == Transaction.Type.TRANSFER){
            category_expanded.setVisibility(View.GONE);
            accountLayout.setVisibility(View.GONE);
            accountTransferLayout.setVisibility(View.VISIBLE);
            account_from_expanded.setText(transaction.getDebitAccount().getName());
            account_to_expanded.setText(transaction.getCreditAccount().getName());
        }

    }

    private class GetTransaction extends AsyncTask<Long, Void, Transaction>{

        View view;
        Context context;

        public GetTransaction(View view, Context contet) {
            this.view = view;
            this.context = context;
        }

        @Override
        protected Transaction doInBackground(Long... params) {
            Transaction transaction = null;

            try {
                transaction = ExpensorDatabaseHelper.getInstance(context).getTransactionDao().queryForId(params[0]);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return transaction;
        }

        @Override
        protected void onPostExecute(Transaction transaction) {
            super.onPostExecute(transaction);
            bindViewNow(view, context, transaction);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }

}
