package co.mrdavidliu.uwinfosession;

import android.content.Context;
import android.content.Intent;
import android.text.Html;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.SectionIndexer;
import android.widget.TextView;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Calendar;
import java.util.GregorianCalendar;
import java.util.Locale;
import java.util.TreeMap;

/**
 * Created by David Liu on 9/25/2015.
 */
public class CustomAdapter extends BaseAdapter implements SectionIndexer {

    Context mContext;
    LayoutInflater inflater;
    ArrayList<InfoSession> infosessions;
    ArrayList<InfoSession> infosessions2;
    TreeMap<String,Integer> dates;
    TreeMap<Character,Integer>  alphabet;
    Character[] alphabet_array;
    String[] dates_array;
    int currentSort = 1;

    /**
     * Constructor
     * @param context
     * @param objects
     */
    public CustomAdapter(Context context, ArrayList<InfoSession> objects,TreeMap<String,Integer> dates, TreeMap<Character,Integer> alphabet){
        mContext = context;
        infosessions = objects;
        inflater = LayoutInflater.from(context);
        infosessions2 = new ArrayList<>();
        this.dates = dates;
        this.alphabet = alphabet;
        dates_array = dates.keySet().toArray(new String[0]);
        alphabet_array = alphabet.keySet().toArray(new Character[0]);
        infosessions2.addAll(infosessions);
    }

    @Override
    public Object[] getSections() {
        return (currentSort ==0)?alphabet.keySet().toArray():dates.keySet().toArray();
    }

    @Override
    public int getPositionForSection(int sectionIndex) {
        if(sectionIndex!=-1&&sectionIndex!=0) {
            if (currentSort == 1) {
                return dates.get(dates_array[sectionIndex-1]) - 1;
            } else {
                return alphabet.get(alphabet_array[sectionIndex-1]) - 1;
            }
        }else{
            return 0;
        }
    }

    @Override
    public int getSectionForPosition(int position) {
        if(currentSort ==1){
            return Arrays.asList(dates_array).indexOf(new SimpleDateFormat("MMM").format(infosessions.get(position).start_time.getTime()));
        }else{
            return Arrays.asList(alphabet_array).indexOf(infosessions.get(position).employer.charAt(0));
        }
    }

    private class ViewHolder{
        TextView date;
        TextView company;
        TextView location;
    }

    @Override
    public int getCount() {
        return infosessions.size();
    }

    @Override
    public Object getItem(int position) {
        return infosessions.get(position);
    }

    @Override
    public long getItemId(int position) {
        return infosessions.get(position).id;
    }

    @Override
    public View getView(final int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, null);
            // Locate the TextViews in listview_item.xml
            holder.company = (TextView) convertView.findViewById(R.id.company_name);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.location = (TextView) convertView.findViewById(R.id.location);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Set the results into TextViews
        holder.company.setText(infosessions.get(position).employer);
        holder.location.setText(infosessions.get(position).location);
        holder.date.setText(Html.fromHtml("<i>" + new SimpleDateFormat("EEE, MMM dd'</i><br>'HH:mm - ").format(infosessions.get(position).start_time.getTime())
                + new SimpleDateFormat("HH:mm").format(infosessions.get(position).end_time.getTime())));
        // Listen for ListView Item Click
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
                Intent intent = new Intent(mContext, InfoSessionActivity.class);
                intent.putExtra("infosession", infosessions.get(position));
                mContext.startActivity(intent);
            }
        });

        return convertView;
    }



    // Filter by company
    public void filter_company(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        infosessions.clear();
        if (charText.length() == 0) {
            infosessions.addAll(infosessions2);
        } else {
            for (InfoSession info : infosessions2) {
                if (info.employer.toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    infosessions.add(info);
                }
            }
        }
        notifyDataSetChanged();
    }


    // Filter by company
    public void filter_date(int year, int month, int day) {
        infosessions.clear();
        if (year == 0) {
            infosessions.addAll(infosessions2);
        } else {
            for (InfoSession info : infosessions2) {
                if (year ==-1){
                    infosessions.add(info);
                }else if (info.start_time.get(Calendar.YEAR)==year&&info.start_time.get(Calendar.MONTH)==month&&info.start_time.get(Calendar.DAY_OF_MONTH)==day){
                    infosessions.add(info);
                }
            }
        }
        notifyDataSetChanged();
    }


    // Filter by program
    public void filter_program(String charText) {
        charText = charText.toLowerCase(Locale.getDefault());
        infosessions.clear();
        if (charText.length() == 0) {
            infosessions.addAll(infosessions2);
        } else {
            for (InfoSession info : infosessions2) {
                if (info.programs.toLowerCase(Locale.getDefault())
                        .contains(charText)) {
                    infosessions.add(info);
                }
            }
        }
        notifyDataSetChanged();
    }

    public int getPositionForDate(GregorianCalendar date){
        for(int  i = dates.get(new SimpleDateFormat("MMM").format(date.getTime())); i>0; i--){
            if(infosessions.get(i).start_time.get(Calendar.DAY_OF_MONTH)==date.get(Calendar.DAY_OF_MONTH)-1){
                return i+1;
            }
        }
        return 0;
    }
}
