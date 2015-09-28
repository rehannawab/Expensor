package com.fiosys.expensor.category;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.fiosys.expensor.R;
import com.fiosys.expensor.accounts.CreateAccountActivity;
import com.fiosys.expensor.db.ExpensorDatabaseHelper;

import java.sql.SQLException;

public class CreateCategoryActivity extends Activity implements View.OnClickListener {

    private Spinner type;
    private TextView name;
    private Button create;
    private Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_category);

        type = (Spinner) findViewById(R.id.category_type_spinner);
        name = (TextView) findViewById(R.id.category_name);
        create = (Button) findViewById(R.id.category_create);
        cancel = (Button) findViewById(R.id.category_create_cancel);
        create.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.category_create:
                if(!createCategory()) return;
                break;
            case R.id.category_create_cancel:
                setResult(RESULT_CANCELED);
                break;

        }
        finish();

    }

    private boolean createCategory(){
        if(name.getText().toString().equals("")) {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        Category category = new Category(type.getSelectedItem().toString(), name.getText().toString());

        new CreateCategoryTask().execute(category);
        setResult(RESULT_OK);
        return true;
    }



    private class CreateCategoryTask extends AsyncTask<Category, Void, Integer>{

        @Override
        protected Integer doInBackground(Category... params) {
            int result = 0;
            try {
                CategoryDAO dao = ExpensorDatabaseHelper.getInstance(CreateCategoryActivity.this).getCategoryDao();
                result = dao.create(params[0]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        }

        @Override
        protected void onPostExecute(Integer result) {
            super.onPostExecute(result);
            if(result < 0){
                Toast.makeText(CreateCategoryActivity.this, "Database Error", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(CreateCategoryActivity.this, "Entry Successfull", Toast.LENGTH_LONG).show();
            }
        }
    }

}
