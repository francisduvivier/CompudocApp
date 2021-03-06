package duviwin.compudocapp;

import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.preference.PreferenceManager;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentPagerAdapter;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.ViewPager;
import android.support.v7.app.ActionBar;
import android.support.v7.app.ActionBarActivity;
import android.view.Menu;
import android.view.MenuItem;

import java.util.Locale;

import duviwin.compudocapp.Connection.Connection;
import duviwin.compudocapp.Events.EventSystem;
import duviwin.compudocapp.mijn_afspraken.MijnAfsprFragment;
import duviwin.compudocapp.mijn_opdrachten.MijnOpdrFragment;
import duviwin.compudocapp.open_opdrachten.OpenOpdrFragment;
import duviwin.compudocapp.trial_check.TrialChecker;
import duviwin.compudocapp.usage_stats.MyTracker;


public class MainActivity extends ActionBarActivity implements ActionBar.TabListener
//        , MijnOpdrFragment.OnFragmentInteractionListener
{

    public static final int REQ_CODE_SETTINGS_MAIN = 1;
    /**
     * The {@link android.support.v4.view.PagerAdapter} that will provide
     * fragments for each of the sections. We use a
     * {@link FragmentPagerAdapter} derivative, which will keep every
     * loaded fragment in memory. If this becomes too memory intensive, it
     * may be best to switch to a
     * {@link android.support.v4.app.FragmentStatePagerAdapter}.
     */
    SectionsPagerAdapter mSectionsPagerAdapter;
    Connection conn;

//    public void onFragmentInteraction(String id){}

    /**
     * The {@link ViewPager} that will host the section contents.
     */
    ViewPager mViewPager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        EventSystem.context=getApplicationContext();
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        TrialChecker.checkTrial(System.currentTimeMillis(),this);

        MyTracker.startAnalytics(this);
        // Set up the action bar.
        final ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_TABS);

        // Create the adapter that will return a fragment for each of the three
        // primary sections of the activity.
        mSectionsPagerAdapter = new SectionsPagerAdapter(getSupportFragmentManager());

        // Set up the ViewPager with the sections adapter.
        mViewPager = (ViewPager) findViewById(R.id.pager);
        mViewPager.setAdapter(mSectionsPagerAdapter);


        // When swiping between different sections, select the corresponding
        // tab. We can also use ActionBar.Tab#select() to do this if we have
        // a reference to the Tab.
        mViewPager.setOnPageChangeListener(new ViewPager.SimpleOnPageChangeListener() {
            @Override
            public void onPageSelected(int position) {
                actionBar.setSelectedNavigationItem(position);
            }
        });

        // For each of the sections in the app, add a tab to the action bar.
        for (int i = 0; i < mSectionsPagerAdapter.getCount(); i++) {
            // Create a tab with text corresponding to the page title defined by
            // the adapter. Also specify this Activity object, which implements
            // the TabListener interface, as the callback (listener) for when
            // this tab is selected.
            ActionBar.Tab newTab=actionBar.newTab();
            newTab.setText(mSectionsPagerAdapter.getPageTitle(i))
                    .setTabListener(this);
            actionBar.addTab(newTab);
        }
        SharedPreferences prefMgr= PreferenceManager
                .getDefaultSharedPreferences(getBaseContext());
        if(prefMgr.getString("userNameKey", "").equals("")){
            Intent showSettings=new Intent(this,SettingsActivity.class);

            startActivityForResult(showSettings,REQ_CODE_SETTINGS_MAIN);
        }

    }




    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        switch (id) {
            case R.id.action_settings:
                Intent showSettings = new Intent(this, SettingsActivity.class);

                startActivityForResult(showSettings, REQ_CODE_SETTINGS_MAIN);
                return true;
            case R.id.action_refresh:
                Intent intent = getIntent();
                finish();
                startActivity(intent);
                break;
        }

        return super.onOptionsItemSelected(item);
    }
    @Override
    public void onActivityResult (int requestCode, int resultCode, Intent data){
        if(requestCode==REQ_CODE_SETTINGS_MAIN){
            Intent intent = getIntent();
            finish();
            startActivity(intent);
        }
    }
    @Override
    public void onTabSelected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
        // When the given tab is selected, switch to the corresponding page in
        // the ViewPager.
        mViewPager.setCurrentItem(tab.getPosition());
//        new AlertDialog.Builder(this)
//                .setTitle("onTabSelected: "+ tab.getText())
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
        MyTracker.send(getString(R.string.main_tab_select), "Main Tab: " + tab.getText(), null);

    }

    @Override
    public void onTabUnselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//        new AlertDialog.Builder(this)
//                .setTitle("onTabUnselected: "+ tab.getText())
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
    }

    @Override
    public void onTabReselected(ActionBar.Tab tab, FragmentTransaction fragmentTransaction) {
//                new AlertDialog.Builder(this)
//                .setTitle("onTabReselected: "+ tab.getText())
//                .setIcon(android.R.drawable.ic_dialog_alert)
//                .show();
    }

    /**
     * A {@link FragmentPagerAdapter} that returns a fragment corresponding to
     * one of the sections/tabs/pages.
     */
    public class SectionsPagerAdapter extends FragmentPagerAdapter {

        public SectionsPagerAdapter(FragmentManager fm) {
            super(fm);
        }

        @Override
        public Fragment getItem(int position) {
            // getItem is called to instantiate the fragment for the given page.
            // Return a PlaceholderFragment (defined as a static inner class below).
            switch (position) {
                case 0:
                    return new OpenOpdrFragment();
                case 1:
                    return new MijnOpdrFragment();
                case 2:
                    return new MijnAfsprFragment();
            default:
                return new OpenOpdrFragment();
            }
        }

        @Override
        public int getCount() {
            // Show 3 total pages.
            return 3;
        }

        @Override
        public CharSequence getPageTitle(int position) {
            Locale l = Locale.getDefault();
            switch (position) {
                case 0:
                    return getString(R.string.title_open).toUpperCase(l);
                case 1:
                    return getString(R.string.title_mijn).toUpperCase(l);
                case 2:
                    return getString(R.string.title_afspraken).toUpperCase(l);
//                case 3:
//                    return getString(R.string.title_factuur).toUpperCase(l);
            }
            return null;
        }
    }

//    /**
//     * A placeholder fragment containing a simple view.
//     */
//    public static class PlaceholderFragment extends Fragment {
//        /**
//         * The fragment argument representing the section number for this
//         * fragment.
//         */
//        private static final String ARG_SECTION_NUMBER = "section_number";
//
//        /**
//         * Returns a new instance of this fragment for the given section
//         * number.
//         */
//        public static PlaceholderFragment newInstance(int sectionNumber) {
//            PlaceholderFragment fragment = new PlaceholderFragment();
//            Bundle args = new Bundle();
//            args.putInt(ARG_SECTION_NUMBER, sectionNumber);
//            fragment.setArguments(args);
//            return fragment;
//
//        }
//
//        public PlaceholderFragment() {
//        }
//
//        @Override
//        public View onCreateView(LayoutInflater inflater, ViewGroup container,
//                                 Bundle savedInstanceState) {
//            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
//            return rootView;
//        }
//    }

}
