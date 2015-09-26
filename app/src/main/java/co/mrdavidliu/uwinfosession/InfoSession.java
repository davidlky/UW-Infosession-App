package co.mrdavidliu.uwinfosession;

import java.io.Serializable;
import java.util.Comparator;
import java.util.GregorianCalendar;

/**
 * Created by David Liu on 9/25/2015.
 */
public class InfoSession implements Serializable{
    public int id;
    public String employer, location, website, audience, programs, description;
    public GregorianCalendar start_time, end_time;
}
