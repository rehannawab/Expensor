package com.fiosys.expensor.accounts;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.Toast;

import com.fiosys.expensor.AccountCursorAdapter;
import com.fiosys.expensor.ExpandAnimation;
import com.fiosys.expensor.R;
import com.fiosys.expensor.TopLevelActivity;
import com.fiosys.expensor.db.ExpensorDatabaseHelper;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;

import java.sql.SQLException;

/**
 * A fragment representing a list of Items.
 * <p>
 */
public class AccountFragment extends ListFragment {

    CloseableIterator<Account> iterator;
    CursorAdapter ca;
    public static final int CREATE_ACCOUNT = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public AccountFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);



    }

    @Override
    public void onResume() {
        super.onResume();

        new LoadAccounts().execute();
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(iterator!=null) iterator.closeQuietly();
    }

    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((TopLevelActivity) activity).setmTitle("Accounts");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);

        View buttonBox = v.findViewById(R.id.button_box);

        Button b = (Button) v.findViewById(R.id.button_account_edit);
        Button b2 = (Button) v.findViewById(R.id.button_account_delete);

        Log.e(null, "Button Box is clickable focusable before " + buttonBox.isClickable() + buttonBox.isFocusable());
        Log.e(null, "button is clickable focuasble before " + b.isClickable() + b.isFocusable());
        ExpandAnimation ea = new ExpandAnimation(buttonBox, 500);

        buttonBox.startAnimation(ea);

        b.setFocusable(false);
        b2.setFocusable(false);

        b.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.i(null, "button clicked");
                Toast.makeText(getActivity(), "Button clicked", Toast.LENGTH_LONG).show();
            }
        });

        b2.setOnClickListener(new View.OnClickListener(){
            public void onClick(View v){
                Log.i(null, "button clicked");
                Toast.makeText(getActivity(), "Button clicked", Toast.LENGTH_LONG).show();
            }
        });

        Log.e(null, "Button Box is clickable focusable after " + buttonBox.isClickable() + buttonBox.isFocusable());
        Log.e(null, "button is clickable focuasble after " + b.isClickable() + b.isFocusable());


    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.i(null, "On Options item selected from category fragment");

        if (item.getItemId() == R.id.account_create) {
            Intent intent = new Intent(getActivity(), CreateAccountActivity.class);
            startActivityForResult(intent, CREATE_ACCOUNT);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class LoadAccounts extends AsyncTask<Void, Void, CursorAdapter>{
        @Override
        protected CursorAdapter doInBackground(Void... params) {
            try {
                iterator = ExpensorDatabaseHelper.getInstance(getActivity()).getAccountDao().getIteratorForCursorAdapter();


            } catch (SQLException e) {
                Toast.makeText(getActivity(), "Cannot get cursor", Toast.LENGTH_LONG).show();
            }
            AndroidDatabaseResults results =
                    (AndroidDatabaseResults)iterator.getRawResults();

            ca = new AccountCursorAdapter(getActivity(), R.layout.test, results.getRawCursor(), 0);

            return ca;
        }

        @Override
        protected void onPostExecute(CursorAdapter cursorAdapter) {
            super.onPostExecute(cursorAdapter);
            setListAdapter(cursorAdapter);
        }
    }

}
