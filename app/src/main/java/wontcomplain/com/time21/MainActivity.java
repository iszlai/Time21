package wontcomplain.com.time21;

import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import org.joda.time.DateTime;
import org.joda.time.Interval;
import org.joda.time.Period;

import java.util.Date;
import java.util.Timer;
import java.util.TimerTask;

public class MainActivity extends AppCompatActivity {

    private static final String TAG ="TWENTY1" ;
    private static final String PREFS_NAME = "TWENTY1";
    DateTime targetDate;
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "Application has started " );
        settings = getSharedPreferences(PREFS_NAME, 0);
        targetDate=new DateTime(settings.getString("targetDate", new DateTime().plusDays(21).toString()));

    }

    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        // Inflate the menu; this adds items to the action bar if it is present.
        getMenuInflater().inflate(R.menu.menu_main, menu);
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

    public void startCounter(View view){
        //view.setBackgroundColor(Color.BLACK);
        DateTime today=new DateTime();
        Log.i(TAG, "today " + today.toString());
        Log.i(TAG, "target " + targetDate.toString());
        Period interval = new Period(today,targetDate);

        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {
                DateTime today = new DateTime();
                final Period interval = new Period(today, targetDate);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateTime(interval, getTimerTextViews());
                    }
                });

            }
        }, 0, 1000);
        Button button = (Button) view;
        button.setVisibility(View.INVISIBLE);
        Button stop =(Button)findViewById(R.id.stopButton);
        stop.setVisibility(View.VISIBLE);
        // updateTime(interval,getTimerTextViews());
    }

    public TimeTextViews getTimerTextViews (){
        TextView day =(TextView)findViewById(R.id.dayLabel);
        TextView hour=(TextView)findViewById(R.id.hourLabel);
        TextView minute=(TextView)findViewById(R.id.minuteLabel);
        TextView second=(TextView)findViewById(R.id.secondLabel);
        return new TimeTextViews(day,hour,minute,second);
    }

    public static void updateTime(Period interval,TimeTextViews views){
        String days=Integer.valueOf(interval.getWeeks() * 7 + interval.getDays()).toString();
        Log.i(TAG, "d: " + days);
        views.getDay().setText(days);

        String hours=Integer.valueOf(interval.getHours()).toString();
        Log.i(TAG, "h: " + hours);
        views.getHour().setText(hours);
        String minutes=Integer.valueOf(interval.getMinutes()).toString();
        Log.i(TAG, "m: " + minutes);
        views.getMinute().setText(minutes);
        String seconds=Integer.valueOf(interval.getSeconds()).toString();
        Log.i(TAG, "s: " + seconds);
        views.getSecond().setText(seconds);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop called");
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("targetDate",targetDate.toString());
        edit.commit();
    }

    public void resetCounter(View view) {
        targetDate=new DateTime().plusDays(21);
        SharedPreferences.Editor edit = settings.edit();
        edit.putString("targetDate",targetDate.toString());
        edit.commit();


    }
}
