package co.mrdavidliu.uwinfosession;

import android.content.ContentResolver;
import android.content.ContentValues;
import android.content.Context;
import android.net.Uri;
import android.provider.CalendarContract;

import java.util.GregorianCalendar;
import java.util.TimeZone;

/**
 * Created by David Liu on 9/26/2015.
 */
public class AddToCalendar {
    String id;

    public AddToCalendar (InfoSession info, Context context){
        // Construct event details

        // Insert Event
        ContentResolver cr = context.getContentResolver();
        ContentValues values = new ContentValues();
        TimeZone timeZone = TimeZone.getDefault();
        values.put(CalendarContract.Events.DTSTART, info.start_time.getTimeInMillis());
        values.put(CalendarContract.Events.DTEND, info.end_time.getTimeInMillis());
        values.put(CalendarContract.Events.EVENT_TIMEZONE, timeZone.getID());
        values.put(CalendarContract.Events.TITLE, info.employer+" - Info Session");
        values.put(CalendarContract.Events.EVENT_LOCATION, info.location);
        values.put(CalendarContract.Events.DESCRIPTION, info.description);
        values.put(CalendarContract.Events.CALENDAR_ID, 1);
        Uri uri = cr.insert(CalendarContract.Events.CONTENT_URI, values);


        // Retrieve ID for new event
        id = uri.getLastPathSegment();
    }
}
