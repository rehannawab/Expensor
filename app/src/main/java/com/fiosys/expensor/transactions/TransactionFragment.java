package com.fiosys.expensor.transactions;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.widget.ArrayAdapter;
import android.widget.CursorAdapter;
import android.widget.ListView;

import com.fiosys.expensor.ExpandAnimation;
import com.fiosys.expensor.R;
import com.fiosys.expensor.TopLevelActivity;
import com.fiosys.expensor.db.ExpensorDatabaseHelper;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;

import java.sql.SQLException;

public class TransactionFragment extends ListFragment {

    CloseableIterator<Transaction> iterator;
    CursorAdapter ca;
    public static final int CREATE_TRANSACTION = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public TransactionFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


    }

    @Override
    public void onResume() {
        super.onResume();
        new LoadTransactions().execute();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(iterator!=null) iterator.closeQuietly();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((TopLevelActivity) activity).setmTitle("Transactions");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        View compressedLayout = v.findViewById(R.id.transaction_compressed);
        ExpandAnimation compress = new ExpandAnimation(compressedLayout, 250);

        final View expandedLayout = v.findViewById(R.id.transaction_expanded);
        final ExpandAnimation expandAnimation = new ExpandAnimation(expandedLayout, 500);

        compress.setAnimationListener(new Animation.AnimationListener() {
            @Override
            public void onAnimationStart(Animation animation) {

            }

            @Override
            public void onAnimationEnd(Animation animation) {
                expandedLayout.startAnimation(expandAnimation);
            }

            @Override
            public void onAnimationRepeat(Animation animation) {

            }
        });

        compressedLayout.startAnimation(compress);



    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        if (item.getItemId() == R.id.transaction_create) {
            Intent intent = new Intent(getActivity(), CreateTransactionActivity.class);
            startActivityForResult(intent, CREATE_TRANSACTION);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class LoadTransactions extends AsyncTask<Void, Void, CursorAdapter>{
        @Override
        protected void onPostExecute(CursorAdapter cursorAdapter) {
            super.onPostExecute(cursorAdapter);
            setListAdapter(cursorAdapter);
        }

        @Override
        protected CursorAdapter doInBackground(Void... params) {
            try {
                iterator = ExpensorDatabaseHelper.getInstance(getActivity()).getTransactionDao().getIteratorForCursorAdapter();
            } catch (SQLException e) {
                e.printStackTrace();
            }

            AndroidDatabaseResults results =
                    (AndroidDatabaseResults)iterator.getRawResults();

            ca = new TransactionCursorAdapter(getActivity(), results.getRawCursor(), 0);

            return ca;
        }
    }

}
