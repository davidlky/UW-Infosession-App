package co.mrdavidliu.uwinfosession;

import com.roomorama.caldroid.CaldroidFragment;
import com.roomorama.caldroid.CaldroidGridAdapter;

public class CalendarFragment extends CaldroidFragment {

    @Override
    public CaldroidGridAdapter getNewDatesGridAdapter(int month, int year) {
        // TODO Auto-generated method stub
        return new CalendarGridAdapter(getActivity(), month, year,
                getCaldroidData(), extraData);
    }

}
