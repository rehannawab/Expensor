package com.fiosys.expensor.category;

import android.app.Activity;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.fiosys.expensor.R;
import com.fiosys.expensor.TopLevelActivity;
import com.fiosys.expensor.db.ExpensorDatabaseHelper;

import java.sql.SQLException;

public class ViewCategory extends Activity {

    TextView type;
    TextView name;

    Category category;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_view_category);

        type = (TextView) findViewById(R.id.type);
        name = (TextView) findViewById(R.id.name);

        Long id = getIntent().getLongExtra("id", 0);



        Log.i(null, "Got id " + id);
        new GetCategory().execute(id);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_view_category, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.delete_category) {
            new DeleteCategory().execute(category.getId());
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private class GetCategory extends AsyncTask<Long, Void, Category>{
        @Override
        protected Category doInBackground(Long... params) {
            try {
                return ExpensorDatabaseHelper.getInstance(ViewCategory.this).getCategoryDao().queryForId(params[0]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return null;
        }

        @Override
        protected void onPostExecute(Category gotCategory) {
            super.onPostExecute(gotCategory);
            category = gotCategory;
            Log.i(null, "Inside Post Execute");

            if(category == null){
                Log.e(null, "Could not find category");
            }
            type.setText(category.getType());
            name.setText(category.getName());
        }
    }

    private class DeleteCategory extends AsyncTask<Long, Void, Integer>{
        @Override
        protected void onPostExecute(Integer aBoolean) {
            if(aBoolean == 1){
                Toast.makeText(ViewCategory.this, "CategoryDeleted", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(ViewCategory.this, TopLevelActivity.class);
                intent.putExtra("position",TopLevelActivity.CATEGORIES_POS);
                startActivity(intent);
            }
            super.onPostExecute(aBoolean);
        }

        @Override
        protected Integer doInBackground(Long... params) {
            try {
                return ExpensorDatabaseHelper.getInstance(ViewCategory.this).getCategoryDao().deleteById(params[0]);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return 0;

        }
    }

}
