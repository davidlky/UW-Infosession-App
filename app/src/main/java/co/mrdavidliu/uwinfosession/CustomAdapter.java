package co.mrdavidliu.uwinfosession;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import org.json.JSONArray;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.Locale;

/**
 * Created by David Liu on 9/25/2015.
 */
public class CustomAdapter extends BaseAdapter {

    Context mContext;
    LayoutInflater inflater;
    private ArrayList<InfoSession> infosessions;
    private ArrayList<InfoSession> infosessions2;

    /**
     * Constructor
     * @param context
     * @param objects
     */
    public CustomAdapter(Context context, ArrayList<InfoSession> objects){
        mContext = context;
        infosessions = objects;
        inflater = LayoutInflater.from(context);
        infosessions2 = new ArrayList<>();
        infosessions2.addAll(infosessions);
    }

    private class ViewHolder{
        TextView date;
        TextView company;
        TextView programs;
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
    public View getView(int position, View convertView, ViewGroup parent) {
        final ViewHolder holder;
        if (convertView == null) {
            holder = new ViewHolder();
            convertView = inflater.inflate(R.layout.list_item, null);
            // Locate the TextViews in listview_item.xml
            holder.company = (TextView) convertView.findViewById(R.id.company_name);
            holder.programs = (TextView) convertView.findViewById(R.id.programs);
            holder.date = (TextView) convertView.findViewById(R.id.date);
            holder.location = (TextView) convertView.findViewById(R.id.location);
            convertView.setTag(holder);
        } else {
            holder = (ViewHolder) convertView.getTag();
        }
        // Set the results into TextViews
        holder.company.setText(infosessions.get(position).employer);
        holder.location.setText(infosessions.get(position).location);
        holder.date.setText(infosessions.get(position).start_time.get(Calendar.DATE)+": "+infosessions.get(position).start_time.getTime() + " - " + infosessions.get(position).end_time.getTime());
        holder.programs.setText(infosessions.get(position).programs);
        // Listen for ListView Item Click
        convertView.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View arg0) {
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
}
