package com.fiosys.expensor.transactions;

import android.app.Activity;
import android.app.DatePickerDialog;
import android.app.Dialog;
import android.app.DialogFragment;
import android.app.Fragment;
import android.app.TimePickerDialog;
import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.text.format.DateFormat;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.DatePicker;
import android.widget.EditText;
import android.widget.ProgressBar;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.TimePicker;
import android.widget.Toast;

import com.fiosys.expensor.R;
import com.fiosys.expensor.accounts.Account;
import com.fiosys.expensor.accounts.SelectAccountActivity;
import com.fiosys.expensor.category.Category;
import com.fiosys.expensor.category.CategoryFragment;
import com.fiosys.expensor.category.CreateCategoryActivity;
import com.fiosys.expensor.category.SelectCategoryActivity;
import com.fiosys.expensor.db.ExpensorDatabaseHelper;

import org.w3c.dom.Text;

import java.sql.SQLException;
import java.util.Calendar;

public class CreateTransactionActivity extends Activity
        implements TimePickerDialog.OnTimeSetListener,DatePickerDialog.OnDateSetListener,
        View.OnClickListener{

    public static final int SELECT_FROM_ACCOUNT = 1;
    public static final int SELECT_TO_ACCOUNT = 2;
    public static final int SELECT_CATEGORY = 3;

    private Calendar timeOfTransaction;
    private Transaction transaction;

    private Account fromAccount;
    private Account toAccount;
    private Category category;

    Spinner transactionType;
    EditText categoryText;
    EditText fromAccountText;
    EditText toAccountText;
    EditText amount;
    Button create;
    Button cancel;
    TextView date;
    TextView time;
    ProgressBar progressBar;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_transaction);

        date            = (TextView) findViewById(R.id.transaction_date_inpue);
        time            = (TextView) findViewById(R.id.transaction_time_inpue);
        transactionType = (Spinner) findViewById(R.id.transaction_type_spinner);
        categoryText    = (EditText) findViewById(R.id.category_text);
        fromAccountText = (EditText) findViewById(R.id.account_text);
        toAccountText   = (EditText) findViewById(R.id.account_to_text);
        amount          = (EditText) findViewById(R.id.amount_text);
        create          = (Button) findViewById(R.id.transaction_create);
        cancel          = (Button) findViewById(R.id.transaction_create_cancel);
        progressBar     = (ProgressBar) findViewById(R.id.progressBar);

        create.setOnClickListener(this);
        cancel.setOnClickListener(this);

        timeOfTransaction = Calendar.getInstance();

        updateTimeOfTransaction();

        transactionType.setAdapter(new ArrayAdapter(this,
                android.R.layout.simple_spinner_dropdown_item,
                Transaction.Type.names()));

//        EditText s = (EditText) findViewById(R.id.editText);

//        s.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                startActivity(new Intent(CreateTransactionActivity.this, CreateCategoryActivity.class));
//            }
//        });

    }

    private void updateTimeOfTransaction(){

        date.setText(timeOfTransaction.get(Calendar.DAY_OF_MONTH) + "/"
                    + timeOfTransaction.get(Calendar.MONTH) + "/"
                    + timeOfTransaction.get(Calendar.YEAR));

        time.setText(timeOfTransaction.get(Calendar.HOUR_OF_DAY) + ":"
                + timeOfTransaction.get(Calendar.MINUTE));

    }

    public void onClickGetDate(View view){
        DialogFragment newFragment = new DatePickerFragment();
        newFragment.show(getFragmentManager(), "datePicker");
    }

    public void onClickGetTime(View view){
        DialogFragment newFragment = new TimePickerFragment();
        newFragment.show(getFragmentManager(), "timePicker");
    }

    public void onAccountSelect(View view){

        Intent intent = new Intent(this, SelectAccountActivity.class);
        if(view.getId() == R.id.account_text) startActivityForResult(intent, SELECT_FROM_ACCOUNT);
        if(view.getId() == R.id.account_to_text) startActivityForResult(intent, SELECT_TO_ACCOUNT);
    }

    public void onCategorySelect(View view){

        Intent intent = new Intent(this, SelectCategoryActivity.class);
        startActivityForResult(intent, SELECT_CATEGORY);

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_create_transaction, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);

        Log.d(null, "On Activity Result");

        Long id;
        if(data==null) return;

        id = data.getLongExtra("id", 1);

        switch(requestCode){

            case SELECT_FROM_ACCOUNT:
                new GetAccount(GetAccount.FROM_ACCOUNT).execute(id);
                break;
            case SELECT_TO_ACCOUNT:
                new GetAccount(GetAccount.TO_ACCOUNT).execute(id);
                break;
            case SELECT_CATEGORY:
                new GetCategory().execute(id);

        }

    }

    public void onTimeSet(TimePicker view, int hourOfDay, int minute) {
        // Do something with the time chosen by the user

        timeOfTransaction.set(Calendar.HOUR_OF_DAY, hourOfDay);
        timeOfTransaction.set(Calendar.MINUTE, minute);
        updateTimeOfTransaction();

    }

    public void onDateSet(DatePicker view, int year, int month, int day) {
        // Do something with the date chosen by the user

        timeOfTransaction.set(Calendar.YEAR, year);
        timeOfTransaction.set(Calendar.MONTH, month);
        timeOfTransaction.set(Calendar.DAY_OF_MONTH, day);

        updateTimeOfTransaction();
    }

    @Override
    public void onClick(View v) {

        switch(v.getId()){
            case R.id.transaction_create:
                populateTransaction();
                new CreateTransaction().execute(transaction);
                break;
            case R.id.transaction_create_cancel:
                setResult(RESULT_CANCELED);
                finish();
                break;
        }

    }

    private void populateTransaction(){

        Log.e(null, "Amount is " + amount.getText().toString());

        Double amountValue = Double.parseDouble(amount.getText().toString());


        transaction = new Transaction(timeOfTransaction.getTime(),
                fromAccount,
                toAccount,
                Transaction.Type.fromString(transactionType.getSelectedItem().toString()),
                category,
                amountValue);

    }


    public static class TimePickerFragment extends DialogFragment {



        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current time as the default values for the picker
            final Calendar c = Calendar.getInstance();
            int hour = c.get(Calendar.HOUR_OF_DAY);
            int minute = c.get(Calendar.MINUTE);

            CreateTransactionActivity CTA = (CreateTransactionActivity) getActivity();

            // Create a new instance of TimePickerDialog and return it
            return new TimePickerDialog(getActivity(), (CreateTransactionActivity)getActivity(), hour, minute,
                    DateFormat.is24HourFormat(getActivity()));
        }


    }

    public static class DatePickerFragment extends DialogFragment {

        @Override
        public Dialog onCreateDialog(Bundle savedInstanceState) {
            // Use the current date as the default date in the picker
            final Calendar c = Calendar.getInstance();
            int year = c.get(Calendar.YEAR);
            int month = c.get(Calendar.MONTH);
            int day = c.get(Calendar.DAY_OF_MONTH);

            // Create a new instance of DatePickerDialog and return it
            return new DatePickerDialog(getActivity(), (CreateTransactionActivity)getActivity(), year, month, day);
        }


    }


    private class GetAccount extends AsyncTask<Long, Void, Account>{

        static final int FROM_ACCOUNT = 1;
        static final int TO_ACCOUNT = 2;

        private int accountToUpdate;

        GetAccount(int accountToUpdate){
            super();
            this.accountToUpdate = accountToUpdate;
        }

        @Override
        protected Account doInBackground(Long... params) {

            Account account = null;

            try {
                account = ExpensorDatabaseHelper.getInstance(CreateTransactionActivity.this).getAccountDao().queryForId(params[0]);
            } catch (SQLException e) {
                e.printStackTrace();
            }

            return account;
        }

        @Override
        protected void onPostExecute(Account account) {
            super.onPostExecute(account);

            if(account!=null){
                switch(accountToUpdate){
                    case FROM_ACCOUNT:
                        fromAccount = account;
                        fromAccountText.setText(account.getName());
                        break;
                    case TO_ACCOUNT:
                        toAccount = account;
                        toAccountText.setText(account.getName());
                        break;

                }
            }

        }
    }

    private class GetCategory extends AsyncTask<Long, Void, Category>{


        @Override
        protected Category doInBackground(Long... params) {
            Category category = null;
            try {
                category = ExpensorDatabaseHelper.getInstance(CreateTransactionActivity.this).getCategoryDao().queryForId(params[0]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return category;
        }

        @Override
        protected void onPostExecute(Category categoryResult) {
            super.onPostExecute(categoryResult);
            category = categoryResult;
            categoryText.setText(category.getName());
        }
    }

    private class CreateTransaction extends AsyncTask<Transaction, Void, Integer>{


        @Override
        protected void onPreExecute() {
            super.onPreExecute();
            progressBar.setIndeterminate(true);
            progressBar.setVisibility(View.VISIBLE);
            create.setVisibility(View.GONE);
            cancel.setVisibility(View.GONE);
        }

        @Override
        protected void onPostExecute(Integer integer) {
            super.onPostExecute(integer);
            setResult(RESULT_OK);
            finish();

        }

        @Override
        protected Integer doInBackground(Transaction... params) {
            Transaction transactionToSave = params[0];
            int result = 0;
            try {
                result = new TransactionProcessor(CreateTransactionActivity.this).create(params[0]);
            } catch (SQLException e) {
                e.printStackTrace();
            }
            return result;
        }
    }

}
