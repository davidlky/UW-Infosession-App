package co.mrdavidliu.uwinfosession;

import android.app.Activity;
import android.app.ListFragment;
import android.os.AsyncTask;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.app.ActionBar;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.content.Context;
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
import android.widget.ListView;
import android.widget.TextView;

import org.apache.http.HttpResponse;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.lang.reflect.Array;
import java.net.HttpURLConnection;
import java.net.URL;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Date;
import java.util.GregorianCalendar;

public class MainActivity extends AppCompatActivity
        implements NavigationDrawerFragment.NavigationDrawerCallbacks {

    /**
     * Fragment managing the behaviors, interactions and presentation of the navigation drawer.
     */
    private NavigationDrawerFragment mNavigationDrawerFragment;

    /**
     * Used to store the last screen title. For use in {@link #restoreActionBar()}.
     */
    private CharSequence mTitle;

    /**
     * Stores the current list of info sessions
     */
    private ArrayList<InfoSession> infosessions = null;
    private static CustomAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        mNavigationDrawerFragment = (NavigationDrawerFragment)
                getSupportFragmentManager().findFragmentById(R.id.navigation_drawer);
        mTitle = getTitle();

        // Set up the drawer.
        mNavigationDrawerFragment.setUp(
                R.id.navigation_drawer,
                (DrawerLayout) findViewById(R.id.drawer_layout));

        //set up data
        setupCalendar();
    }

    @Override
    public void onNavigationDrawerItemSelected(int position) {
        // update the main content by replacing fragments
        FragmentManager fragmentManager = getSupportFragmentManager();
        fragmentManager.beginTransaction()
                .replace(R.id.container, PlaceholderFragment.newInstance(position + 1))
                .commit();
    }

    public void onSectionAttached(int number) {
        switch (number) {
            case 1:
                mTitle = getString(R.string.title_section1);
                break;
            case 2:
                mTitle = getString(R.string.title_section2);
                break;
            case 3:
                mTitle = getString(R.string.title_section3);
                break;
        }
    }

    public void restoreActionBar() {
        ActionBar actionBar = getSupportActionBar();
        actionBar.setNavigationMode(ActionBar.NAVIGATION_MODE_STANDARD);
        actionBar.setDisplayShowTitleEnabled(true);
        actionBar.setTitle(mTitle);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        if (!mNavigationDrawerFragment.isDrawerOpen()) {
            // Only show items in the action bar relevant to this screen
            // if the drawer is not showing. Otherwise, let the drawer
            // decide what to show in the action bar.
            getMenuInflater().inflate(R.menu.main, menu);
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
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private void setupCalendar(){
        try {
            SetupCalendar setupCalendar = new SetupCalendar();
            String response = setupCalendar.execute().get();
            if(!response.equals("")){
                JSONObject json = new JSONObject(response);
                infosessions = convertResponse((JSONArray)json.get("data"));
                adapter = new CustomAdapter(this,infosessions);
            }
        }catch (Exception e){
            Log.d("MainActivity","Getting API - Failed parse string");
        }
    }

    private ArrayList<InfoSession> convertResponse (JSONArray input){
        ArrayList<InfoSession> infos = new ArrayList<InfoSession>();
        for (int i =0; i< input.length();i++) {
            InfoSession session = new InfoSession();
            try {
                JSONObject info= (JSONObject) input.get(i);
                session.id = Integer.parseInt((String) info.get("id"));
                session.description = (String)info.get("description");
                session.employer = (String)info.get("employer");
                session.location = (String)info.get("location");
                session.website = (String)info.get("website");
                session.audience = (String)info.get("audience");
                session.programs = (String)info.get("programs");
                //parse for time and date
                String[] months = new String[]{
                  "January","February","March","April","May","June","July","August","September","October","November","December"
                };
                String[] date = ((String)info.get("date")).split(" ");
                date[1] = date[1].substring(0,date[1].length()-1);
                String time1 = (String)info.get("start_time");
                String time2 = (String)info.get("end_time");
                int[] times = new int[]{
                        Integer.parseInt(time1.substring(0,time1.indexOf(':')))+((time1.substring(time1.length()-2).equals("PM"))?12:0),
                        Integer.parseInt(time1.substring(time1.indexOf(':')+1,time1.indexOf(' '))),
                        Integer.parseInt(time2.substring(0,time2.indexOf(':')))+((time2.substring(time2.length()-2).equals("PM"))?12:0),
                        Integer.parseInt(time2.substring(time2.indexOf(':')+1,time2.indexOf(' ')))
                };
                session.start_time = new GregorianCalendar(Integer.parseInt(date[2]),Arrays.asList(months).indexOf(date[0])+1,Integer.parseInt(date[1]),times[0],times[1]);
                session.end_time = new GregorianCalendar(Integer.parseInt(date[2]),session.start_time.get(Calendar.MONTH),Integer.parseInt(date[1]),times[2],times[3]);
                infos.add(session);
            } catch (JSONException e) {
                e.printStackTrace();
            }

        }
        return infos;
    }

    private class SetupCalendar extends AsyncTask<String, Void, String>{

        @Override
        protected String doInBackground(String... params) {
            try {
                StringBuilder response  = new StringBuilder();
                URL url = new URL("http://api.uwaterloo.ca/v2/terms/1141/infosessions.json?key="+getString(R.string.waterlooapi));
                // Starts the query

                HttpURLConnection httpconn = (HttpURLConnection)url.openConnection();
                if (httpconn.getResponseCode() == HttpURLConnection.HTTP_OK)
                {
                    BufferedReader input = new BufferedReader(new InputStreamReader(httpconn.getInputStream()),8192);
                    String strLine = null;
                    while ((strLine = input.readLine()) != null)
                    {
                        response.append(strLine);
                    }
                    input.close();
                }
                return response.toString();

            } catch (IOException e) {
                e.printStackTrace();
            }
            return "";
        }
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
            View rootView = inflater.inflate(R.layout.fragment_main, container, false);
            //setup calendar
            ListView lv = (ListView) rootView.findViewById(R.id.info_sessions);
            lv.setAdapter(adapter);
            if(lv.getAdapter() == null){
                lv.setAdapter(adapter);
            }
            else{
                adapter.notifyDataSetChanged();
            }
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }
    }

}
