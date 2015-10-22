package co.mrdavidliu.uwinfosession;

import android.content.Context;
import android.content.res.Resources;
import android.graphics.Color;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

import java.util.Date;
import java.util.HashMap;
import java.util.TreeMap;

import hirondelle.date4j.DateTime;

/**
 * Created by David Liu on 10/20/2015.
 */
public class CalendarGridAdapter extends CaldroidGridAdapter {
    TreeMap<Date,Integer> entries;
    public CalendarGridAdapter(Context context, int month, int year, HashMap<String, Object> caldroidData, HashMap<String, Object> extraData, TreeMap<Date, Integer> calendar_count) {
        super(context, month, year, caldroidData, extraData);
        entries = calendar_count;
    }

    @Override
    public View getView(int position, View convertView, ViewGroup parent) {LayoutInflater inflater = (LayoutInflater) context
            .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View cellView = convertView;

        // Get dateTime of this cell
        DateTime dateTime = this.datetimeList.get(position);
        Resources resources = context.getResources();
        // For reuse
        if (convertView == null) {
            cellView = inflater.inflate(R.layout.calendar_cell, null);

            Date date = new Date(dateTime.getYear(),dateTime.getMonth()-1,dateTime.getDay());
            if(entries.containsKey(date)){
                for(int i = 0; i<entries.get(date); i++){
                    LinearLayout view = new LinearLayout(context);
                    LinearLayout.LayoutParams layoutParams = new LinearLayout.LayoutParams(5, 5);
                    layoutParams.setMargins(1,1,1,1);
                    view.setLayoutParams(layoutParams);
                    view.setOrientation(LinearLayout.HORIZONTAL);
                    view.setBackgroundResource(R.drawable.circle_blue);
                    ((LinearLayout)cellView.findViewById(R.id.dots)).addView(view);
                }
            }
        }


        TextView tv1 = (TextView) cellView.findViewById(R.id.date);

        tv1.setTextColor(Color.BLACK);


        // Set color of the dates in previous / next month
        if (dateTime.getMonth() != month) {
            tv1.setTextColor(resources
                    .getColor(R.color.button_material_dark));
        }

        boolean shouldResetDiabledView = false;
        boolean shouldResetSelectedView = false;

        // Customize for disabled dates and date outside min/max dates
        if ((minDateTime != null && dateTime.lt(minDateTime))
                || (maxDateTime != null && dateTime.gt(maxDateTime))
                || (disableDates != null && disableDates.indexOf(dateTime) != -1)) {

            tv1.setTextColor(CaldroidFragment.disabledTextColor);
            if (CaldroidFragment.disabledBackgroundDrawable == -1) {
                //cellView.setBackgroundResource(com.caldroid.R.drawable.disable_cell);
            } else {
                //cellView.setBackgroundResource(CaldroidFragment.disabledBackgroundDrawable);
            }

            if (dateTime.equals(getToday())) {
                //cellView.setBackgroundResource(com.caldroid.R.drawable.red_border_gray_bg);
            }

        } else {
            shouldResetDiabledView = true;
        }

        // Customize for selected dates
        if (selectedDates != null && selectedDates.indexOf(dateTime) != -1) {

            tv1.setTextColor(context.getResources().getColor(R.color.secondary));

        } else {
            shouldResetSelectedView = true;
        }

        if (shouldResetDiabledView && shouldResetSelectedView) {
            // Customize for today
            if (dateTime.equals(getToday())) {
                ((LinearLayout)cellView.findViewById(R.id.background_circle)).setBackgroundResource(R.drawable.cell_bg_selected);
                tv1.setTextColor(Color.WHITE);
            } else {
                //cellView.setBackgroundResource(com.caldroid.R.drawable.cell_bg);
            }
        }

        tv1.setText("" + dateTime.getDay());

        // Somehow after setBackgroundResource, the padding collapse.
        // This is to recover the padding

        // Set custom color if required
        setCustomResources(dateTime, cellView, tv1);

        return cellView;
    }


}
