package co.mrdavidliu.uwinfosession;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.Html;
import android.view.Menu;
import android.view.MenuItem;
import android.widget.TextView;

import java.text.SimpleDateFormat;

public class InfoSessionActivity extends AppCompatActivity {

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_info_session);
        InfoSession infoSession = (InfoSession) getIntent().getSerializableExtra("infosession");
        ((TextView)findViewById(R.id.company)).setText(infoSession.employer);
        ((TextView)findViewById(R.id.programs)).setText(infoSession.programs.replaceAll(",","\n"));
        ((TextView)findViewById(R.id.time)).setText(Html.fromHtml("<i>" + new SimpleDateFormat("EEE MMM dd'</i><br>'HH:mm - ").format(infoSession.start_time.getTime())
                + new SimpleDateFormat("HH:mm").format(infoSession.end_time.getTime())));
        ((TextView)findViewById(R.id.location)).setText(infoSession.location);
        ((TextView)findViewById(R.id.audience)).setText(infoSession.audience);
        ((TextView)findViewById(R.id.description)).setText(infoSession.description);
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
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.action_settings) {
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
