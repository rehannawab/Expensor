package com.fiosys.expensor;

import android.app.Activity;

import android.app.ActionBar;
import android.app.Fragment;
import android.app.FragmentManager;
import android.content.Context;
import android.content.Intent;
import android.os.Build;
import android.os.Bundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.support.v4.widget.DrawerLayout;
import android.widget.ArrayAdapter;
import android.widget.TextView;

import com.fiosys.expensor.accounts.AccountFragment;
import com.fiosys.expensor.category.CategoryFragment;
import com.fiosys.expensor.category.CreateCategoryActivity;
import com.fiosys.expensor.transactions.TransactionFragment;

public class TopLevelActivity extends Activity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    public static final int TRANSACTIONS_POS = 0;
    public static final int ACCOUNT_POS = 1;
    public static final int CATEGORIES_POS = 2;

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    @Override
    protected void onStart() {
        super.onStart();

        Log.i(null, "On start top activity");

    }

    @Override
    protected void onRestart() {
        super.onRestart();
        Log.i(null, "On restart top activity");
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.i(null, "On resume top activity");
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        Log.i(null, "On create top activity");

        setContentView(R.layout.activity_top_level);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        int position = getIntent().getIntExtra("position",TRANSACTIONS_POS);

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout), position);
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getFragmentManager();
        Fragment fragment = null;
        Log.i(null, "Position " + position);
        switch (position){
            case TRANSACTIONS_POS:
                fragment = new TransactionFragment();
                break;

            case ACCOUNT_POS:
                fragment = new AccountFragment();
                break;

            case CATEGORIES_POS:
                fragment = new CategoryFragment();
                break;

            default:
                fragment = new AccountFragment();
                break;

        }

        fragmentManager.beginTransaction()
                    .replace(R.id.container,fragment)
                    .commit();
    }

    public void setmTitle(String mTitle) {
        this.mTitle = mTitle;
    }

    public void restoreActionBar() {
        ActionBar actionBar = getActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {

        Log.i(null, "Inside on create options menu of top level activity");
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            Fragment fragment =  getFragmentManager().findFragmentById(R.id.container);

            if(fragment instanceof CategoryFragment){
                getMenuInflater().inflate(R.menu.menu_create_category, menu);
            }
            else if(fragment instanceof TransactionFragment){
                getMenuInflater().inflate(R.menu.menu_create_transaction, menu);
            }
            else if (fragment instanceof AccountFragment){
                getMenuInflater().inflate(R.menu.menu_create_account, menu);
            }
            else
            {
                getMenuInflater().inflate(R.menu.top_level, menu);
            }

//            getMenuInflater().inflate(R.menu.global, menu);
            restoreActionBar();
            return true;
        }
        return super.onCreateOptionsMenu(menu);
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {

            Intent intent = new Intent(this, CreateCategoryActivity.class);
            startActivity(intent);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    /**
     * A placeholder fragment containing a simple view.
     */
    public static class PlaceholderFragment extends Fragment {
        /**
         * The fragment argument representing the section number for this
         * fragment.
         */
        private static final String ARG_SECTION_NUMBER = "section_number";

        /**
         * Returns a new instance of this fragment for the given section
         * number.
         */
        public static PlaceholderFragment newInstance(int sectionNumber) {
            PlaceholderFragment fragment = new PlaceholderFragment();
            Bundle args = new Bundle();
            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
            fragment.setArguments(args);
            return fragment;
        }

        public PlaceholderFragment() {
        }

        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView = inflater.inflate(R.layout.fragment_top_level, container, false);
            return rootView;
        }




        @Override
        public void onAttach(Activity activity) {
            Log.i(null, "OnAttach");
            super.onAttach(activity);
//            ((TopLevelActivity) activity).onSectionAttached(
//                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
