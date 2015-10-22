package co.mrdavidliu.uwinfosession;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import java.text.SimpleDateFormat;

public class InfoSessionActivity extends AppCompatActivity {

    InfoSession infoSession;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_session);
        infoSession = (InfoSession) getIntent().getSerializableExtra("infosession");
        ((TextView)findViewById(R.id.company)).setText(infoSession.employer);
        ((TextView)findViewById(R.id.programs)).setText(infoSession.programs.replaceAll(",","\n"));
        ((TextView)findViewById(R.id.time)).setText(Html.fromHtml("<i>" + new SimpleDateFormat("EEE MMM dd'</i><br>'HH:mm - ").format(infoSession.start_time.getTime())
                + new SimpleDateFormat("HH:mm").format(infoSession.end_time.getTime())));
        ((TextView)findViewById(R.id.location)).setText(infoSession.location);
        ((TextView)findViewById(R.id.audience)).setText(infoSession.audience);
        ((TextView)findViewById(R.id.description)).setText(infoSession.description.replaceAll("br[ ]?/","\n"));
        setTitle(infoSession.employer);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setDisplayShowHomeEnabled(true);
    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_info_session, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        switch (item.getItemId()) {
            case android.R.id.home:
                // app icon in action bar clicked; goto parent activity.
                this.finish();
                return true;
            case(R.id.action_settings):
                new AddToCalendar(infoSession,this);
                Toast.makeText(this,"Event was Added to Calendar",Toast.LENGTH_SHORT).show();
                return true;
            default:
                return super.onOptionsItemSelected(item);
        }

    }
}
