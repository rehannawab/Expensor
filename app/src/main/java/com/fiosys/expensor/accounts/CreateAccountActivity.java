package com.fiosys.expensor.accounts;

import android.app.Activity;
import android.database.sqlite.SQLiteDatabase;
import android.os.AsyncTask;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.fiosys.expensor.R;
import com.fiosys.expensor.db.ExpensorDatabaseHelper;

import java.sql.SQLException;

public class CreateAccountActivity extends Activity implements View.OnClickListener{

    EditText name;
    EditText balance;
    Button create;
    Button cancel;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_account);
        name = (EditText) findViewById(R.id.account_name_input);
        balance = (EditText) findViewById(R.id.account_balance_input);
        create = (Button) findViewById(R.id.account_create_button);
        cancel = (Button) findViewById(R.id.account_create_cancel_button);
        create.setOnClickListener(this);
        cancel.setOnClickListener(this);
    }

    @Override
    public void onClick(View v) {

        switch (v.getId()){
            case R.id.account_create_button:
                if(!createAccount()) return;
                break;
            case R.id.account_create_cancel_button:
                setResult(RESULT_CANCELED);
                break;
        }

        finish();

    }

    private boolean createAccount(){
        if(name.getText().toString().equals("")) {
            Toast.makeText(this, "Please Enter Name", Toast.LENGTH_SHORT).show();
            return false;
        }

        Account account = new Account(name.getText().toString(), Double.valueOf(balance.getText().toString()));

        new CreateAccountTask().execute(account);
        setResult(RESULT_OK);
        return true;
    }

    private class CreateAccountTask extends AsyncTask<Account, Void, Integer>{
        @Override
        protected Integer doInBackground(Account... params) {
            try {
                return ExpensorDatabaseHelper.getInstance(CreateAccountActivity.this).getAccountDao().create(params[0]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return 0;
        }

        @Override
        protected void onPostExecute(Integer aLong) {
            super.onPostExecute(aLong);
            if(aLong < 1){
                Toast.makeText(CreateAccountActivity.this, "Database Error", Toast.LENGTH_LONG).show();
            }
            else{
                Toast.makeText(CreateAccountActivity.this, "Entry Successfull", Toast.LENGTH_LONG).show();
            }
        }
    }

}
