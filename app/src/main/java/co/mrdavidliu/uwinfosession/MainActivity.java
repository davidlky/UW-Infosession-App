package co.mrdavidliu.uwinfosession;

import android.app.Activity;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ListView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;
import com.roomorama.caldroid.CaldroidListener;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.GregorianCalendar;
import java.util.HashMap;
import java.util.TreeMap;

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
    private static CalendarGridAdapter cal_adapter;
    private TreeMap<Character,Integer> alphabet;
    private TreeMap<String,Integer> dates;
    private static TreeMap<Date,Integer> calendar_count;

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
            if((mNavigationDrawerFragment.getArguments() != null) && (mNavigationDrawerFragment.getArguments().getInt("section_number") == 1)) {
                getMenuInflater().inflate(R.menu.main, menu);
                restoreActionBar();
            }else{
                getMenuInflater().inflate(R.menu.calendar, menu);
                restoreActionBar();
            }
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

        return super.onOptionsItemSelected(item);
    }

    private void setupCalendar(){
        try {
            SetupCalendar setupCalendar = new SetupCalendar();
            String response = setupCalendar.execute().get();
            if(!response.equals("")){
                JSONObject json = new JSONObject(response);
                infosessions = convertResponse((JSONArray)json.get("data"));
                String[] d = dates.keySet().toArray(new String[0]);
                for (int i = 1; i<d.length;i++){
                    dates.put(d[i],dates.get(d[i])+dates.get(d[i-1])+1);
                }
                Character[] a = alphabet.keySet().toArray(new Character[0]);
                for (int i = 1; i<a.length;i++){
                    alphabet.put(a[i],alphabet.get(a[i])+alphabet.get(a[i-1]));
                }
                adapter = new CustomAdapter(this,infosessions,dates,alphabet);
            }
        }catch (Exception e){
            Log.d("MainActivity","Getting API - Failed parse string");
        }
    }

    private ArrayList<InfoSession> convertResponse (JSONArray input){
        ArrayList<InfoSession> infos = new ArrayList<InfoSession>();
        dates = new TreeMap<String,Integer>(new Comparator<String>() {
            public int compare(String o1, String o2) {
                try {
                    return (new SimpleDateFormat("MMM").parse(o1)).compareTo((new SimpleDateFormat("MMM").parse(o2)));
                } catch (ParseException e) {
                    e.printStackTrace();
                }
                return 0;
            }
        });
        alphabet = new TreeMap<Character,Integer>(new Comparator<Character>() {
            public int compare(Character o1, Character o2) {
                return o1.compareTo(o2);
            }
        });
        calendar_count = new TreeMap<>();
        for (int i =0; i< input.length();i++) {
            InfoSession session = new InfoSession();
            try {
                JSONObject info= (JSONObject) input.get(i);
                session.id = Integer.parseInt((String) info.get("id"));
                session.description = (String)info.get("description");
                session.employer = (String)info.get("employer");

                session.location = (String)info.get("location");
                if(!session.location.equals("")) {
                    Character a = session.employer.charAt(0);

                    int count = alphabet.containsKey(a) ? alphabet.get(a) : 0;
                    alphabet.put(a, count + 1);
                    session.website = (String) info.get("website");
                    session.audience = (String) info.get("audience");
                    session.programs = (String) info.get("programs");
                    //parse for time and date
                    String[] months = new String[]{
                            "January", "February", "March", "April", "May", "June", "July", "August", "September", "October", "November", "December"
                    };
                    String[] date = ((String) info.get("date")).split(" ");
                    date[1] = date[1].substring(0, date[1].length() - 1);
                    String time1 = (String) info.get("start_time");
                    String time2 = (String) info.get("end_time");
                    int[] times = new int[]{
                            Integer.parseInt(time1.substring(0, time1.indexOf(':'))) + ((time1.substring(time1.length() - 2).equals("PM")) ? 12 : 0),
                            Integer.parseInt(time1.substring(time1.indexOf(':') + 1, time1.indexOf(' '))),
                            Integer.parseInt(time2.substring(0, time2.indexOf(':'))) + ((time2.substring(time2.length() - 2).equals("PM")) ? 12 : 0),
                            Integer.parseInt(time2.substring(time2.indexOf(':') + 1, time2.indexOf(' ')))
                    };
                    session.start_time = new GregorianCalendar(Integer.parseInt(date[2]), Arrays.asList(months).indexOf(date[0]), Integer.parseInt(date[1]), times[0], times[1]);
                    session.end_time = new GregorianCalendar(Integer.parseInt(date[2]), session.start_time.get(Calendar.MONTH), Integer.parseInt(date[1]), times[2], times[3]);

                    session.start_time.set(Calendar.ERA,GregorianCalendar.AD);
                    session.start_time.set(Calendar.YEAR,Integer.parseInt(date[2]));
                    session.end_time.set(Calendar.ERA,GregorianCalendar.AD);
                    session.end_time.set(Calendar.YEAR,Integer.parseInt(date[2]));
                    Date currdate = new Date(session.start_time.get(Calendar.YEAR),session.start_time.get(Calendar.MONTH),session.start_time.get(Calendar.DAY_OF_MONTH));
                    if(!calendar_count.containsKey(currdate)) {
                        calendar_count.put(currdate, 1);
                    }else{
                        calendar_count.put(currdate, calendar_count.get(currdate)+1);
                    }
                    String d = new SimpleDateFormat("MMM").format(session.start_time.getTime());

                    count = dates.containsKey(d) ? dates.get(d) : 0;
                    dates.put(d, count + 1);
                    infos.add(session);
                }
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
                URL url = new URL("https://api.uwaterloo.ca/v2/resources/infosessions.json?key="+getString(R.string.waterlooapi));
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

    public void changeList(int choice){
        if(adapter.currentSort!=choice){
            adapter.currentSort = choice;
            if(choice ==0){
                Collections.sort(adapter.infosessions2, new Comparator<InfoSession>() {
                    public int compare(InfoSession one, InfoSession two) {
                        return one.employer.compareTo(two.employer);
                    }
                });
                adapter.infosessions = adapter.infosessions2;
                adapter.notifyDataSetChanged();
            }else{
                Collections.sort(adapter.infosessions2, new Comparator<InfoSession>() {
                    public int compare(InfoSession one, InfoSession two) {
                        return one.start_time.compareTo(two.start_time);
                    }
                });
                adapter.infosessions = adapter.infosessions2;
                adapter.notifyDataSetChanged();

            }
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


        @Override
        public View onCreateView(LayoutInflater inflater, ViewGroup container,
                                 Bundle savedInstanceState) {
            View rootView;
            //setup calendar
            if (getArguments().getInt(ARG_SECTION_NUMBER)==1) {
                rootView = inflater.inflate(R.layout.fragment_main, container, false);
                ListView lv = (ListView) rootView.findViewById(R.id.info_sessions);
                lv.setFastScrollEnabled(true);
                lv.setAdapter(adapter);
                lv.setOverScrollMode(View.OVER_SCROLL_NEVER);
                if (lv.getAdapter() == null) {
                    lv.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                adapter.filter_date(-1,1,1);
                if(adapter.currentSort==1) {
                    lv.setSelection(adapter.getPositionForDate(new GregorianCalendar()));
                }
            }else{
                ((MainActivity)getActivity()).changeList(1);
                rootView = inflater.inflate(R.layout.fragment_calendar_view, container, false);
                ListView lv = (ListView) rootView.findViewById(R.id.info_sessions);
                lv.setAdapter(adapter);
                lv.setOverScrollMode(View.OVER_SCROLL_NEVER);
                if (lv.getAdapter() == null) {
                    lv.setAdapter(adapter);
                } else {
                    adapter.notifyDataSetChanged();
                }
                GregorianCalendar c = new GregorianCalendar();
                adapter.filter_date(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                BackgroundChange asyncTask = new BackgroundChange();
                asyncTask.execute();

            }
            return rootView;
        }

        @Override
        public void onAttach(Activity activity) {
            super.onAttach(activity);
            ((MainActivity) activity).onSectionAttached(
                    getArguments().getInt(ARG_SECTION_NUMBER));
        }

        private class BackgroundChange extends AsyncTask<Void, Void, Void> {
            final CalendarFragment caldroidFragment = new CalendarFragment();

            @Override
            protected Void doInBackground(Void... params) {
                Bundle args = new Bundle();
                Calendar cal = Calendar.getInstance();
                args.putInt(CaldroidFragment.MONTH, cal.get(Calendar.MONTH) + 1);
                args.putInt(CaldroidFragment.YEAR, cal.get(Calendar.YEAR));
                args.putBoolean(CaldroidFragment.SQUARE_TEXT_VIEW_CELL, false);
                args.putInt(CaldroidFragment.THEME_RESOURCE, CalendarFragment.STYLE_NO_FRAME);
                args.putInt(CaldroidFragment.THEME_RESOURCE, com.caldroid.R.style.CaldroidDefaultDark);
                args.putBoolean(CaldroidFragment.SIX_WEEKS_IN_CALENDAR, false);
                caldroidFragment.setArguments(args);

                FragmentTransaction t = getActivity().getSupportFragmentManager().beginTransaction();
                //t.setCustomAnimations(R.anim.enter_from_right, R.anim.exit_to_right);

                t.replace(R.id.calendarView, caldroidFragment);
                t.commit();
                caldroidFragment.setCaldroidListener(new CaldroidListener() {
                    @Override
                    public void onSelectDate(Date date, View view) {
                        caldroidFragment.clearSelectedDates();
                        caldroidFragment.setSelectedDate(date);
                        caldroidFragment.refreshView();
                        Calendar c = Calendar.getInstance();
                        c.setTime(date);
                        adapter.filter_date(c.get(Calendar.YEAR), c.get(Calendar.MONTH), c.get(Calendar.DAY_OF_MONTH));
                    }
                });
                return null;
            }
        }
    }

    public static class CalendarFragment extends CaldroidFragment {

        @Override
        public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
            // TODO Auto-generated method stub
            cal_adapter = new CalendarGridAdapter(getActivity(), month, year,
                    getCaldroidData(), extraData,calendar_count);
            return cal_adapter;
        }

    }


}
