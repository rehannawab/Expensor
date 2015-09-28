package com.fiosys.expensor.category;

import android.app.Activity;
import android.content.Intent;
import android.database.Cursor;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.app.ListFragment;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.fiosys.expensor.R;
import com.fiosys.expensor.TopLevelActivity;
import com.fiosys.expensor.db.ExpensorDatabaseHelper;
import com.fiosys.expensor.transactions.CreateTransactionActivity;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;

import java.sql.SQLException;

/**
 * A fragment representing a list of Items.
 * <p>
 * <p>
 */
public class CategoryFragment extends ListFragment {

    CloseableIterator<Category> iterator;
    CursorAdapter ca;
    public static final int CREATE_CATEGORY = 1;

    /**
     * Mandatory empty constructor for the fragment manager to instantiate the
     * fragment (e.g. upon screen orientation changes).
     */
    public CategoryFragment() {
        setHasOptionsMenu(true);
    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {

        Log.i(null, "Oncreateview categoryfragment");



        return super.onCreateView(inflater, container, savedInstanceState);
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(null, "on resume of categoryfragment");

        new LoadCategories().execute();

    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }


    @Override
    public void onAttach(Activity activity) {
        super.onAttach(activity);
        ((TopLevelActivity) activity).setmTitle("Categories");
    }

    @Override
    public void onDetach() {
        super.onDetach();
    }

    @Override
    public void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent(getActivity(), ViewCategory.class);
        intent.putExtra("id", id);
        startActivity(intent);

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        if(iterator != null) {
            iterator.closeQuietly();
        }
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        Log.i(null, "On Options item selected from category fragment");

        if (item.getItemId() == R.id.category_create) {
            Intent intent = new Intent(getActivity(), CreateTransactionActivity.class);
            startActivity(intent);
//            startActivityForResult(intent, CREATE_CATEGORY);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
    }

    private class LoadCategories extends AsyncTask<Void, Void, CursorAdapter>{

        @Override
        protected CursorAdapter doInBackground(Void... params) {
            Log.i(null, "Inside background");
            try {
                iterator = ExpensorDatabaseHelper.getInstance(CategoryFragment.this.getActivity()).getCategoryDao().getIteratorForCursorAdapter();


            } catch (SQLException e) {
                e.printStackTrace();
            }
            AndroidDatabaseResults results =
                    (AndroidDatabaseResults)iterator.getRawResults();
            ca = new SimpleCursorAdapter(CategoryFragment.this.getActivity(),
                    android.R.layout.simple_list_item_1,
                    results.getRawCursor(),
                    new String[]{CategoryDAO.COL_NAME},
                    new int[]{android.R.id.text1},
                    0);
            Log.i(null, "Exiting background");
            return ca;
        }

        @Override
        protected void onPostExecute(CursorAdapter cursorAdapter) {
            super.onPostExecute(cursorAdapter);
            setListAdapter(cursorAdapter);
        }
    }

}
