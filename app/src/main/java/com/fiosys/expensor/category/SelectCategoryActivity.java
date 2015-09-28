package com.fiosys.expensor.category;

import android.app.Activity;
import android.app.ListActivity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.CursorAdapter;
import android.widget.ListView;
import android.widget.SimpleCursorAdapter;
import android.widget.Toast;

import com.fiosys.expensor.AccountCursorAdapter;
import com.fiosys.expensor.R;
import com.fiosys.expensor.db.ExpensorDatabaseHelper;
import com.j256.ormlite.android.AndroidDatabaseResults;
import com.j256.ormlite.dao.CloseableIterator;

import java.sql.SQLException;

public class SelectCategoryActivity extends ListActivity {

    CloseableIterator<Category> iterator;
    CursorAdapter ca;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Override
    public void onResume() {
        super.onResume();
        try {
            iterator = ExpensorDatabaseHelper.getInstance(this).getCategoryDao().getIteratorForCursorAdapter();


        } catch (SQLException e) {
            Toast.makeText(this, "Cannot get cursor", Toast.LENGTH_LONG).show();
        }
        AndroidDatabaseResults results =
                (AndroidDatabaseResults)iterator.getRawResults();
        ca = new SimpleCursorAdapter(this,
                android.R.layout.simple_list_item_1,
                results.getRawCursor(),
                new String[]{CategoryDAO.COL_NAME},
                new int[]{android.R.id.text1},
                0);

        setListAdapter(ca);


    }

    @Override
    protected void onListItemClick(ListView l, View v, int position, long id) {
        super.onListItemClick(l, v, position, id);
        Intent intent = new Intent();
        intent.putExtra("id", id);
        setResult(RESULT_OK, intent);
        finish();
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        if(iterator!=null) iterator.closeQuietly();
    }
}
