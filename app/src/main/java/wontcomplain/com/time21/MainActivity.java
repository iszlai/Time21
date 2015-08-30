package wontcomplain.com.time21;

import android.app.Activity;
import android.content.SharedPreferences;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.ImageButton;
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
    private static final String TARGET_DATE ="targetDate" ;
    private static final String PREFS_NAME = "TWENTY1";
    DateTime targetDate;
    SharedPreferences settings;
    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        Log.i(TAG, "Application has started ");
        settings = getSharedPreferences(PREFS_NAME, 0);
        boolean isFirstTimeLaunch=!settings.contains(TARGET_DATE);
        if(isFirstTimeLaunch){
            targetDate = new DateTime(settings.getString(TARGET_DATE, new DateTime().plusDays(21).toString()));
            DateTime today = new DateTime();
            final Period interval = new Period(today, targetDate);
            updateTime(interval,getTimerTextViews(),this);
        }else {
            targetDate = new DateTime(settings.getString(TARGET_DATE, new DateTime().plusDays(21).toString()));
            DateTime today = new DateTime();
            final Period interval = new Period(today, targetDate);
            updateTime(interval,getTimerTextViews(),this);
            startCounter(findViewById(R.id.startButton));
        }
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
        final Activity ctx=this;
        Timer timer=new Timer();
        timer.scheduleAtFixedRate(new TimerTask() {
            @Override
            public void run() {

                DateTime today = new DateTime();
                final Period interval = new Period(today, targetDate);
                runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        updateTime(interval, getTimerTextViews(),ctx);
                    }
                });

            }
        }, 0, 1000);
        ImageButton button = (ImageButton) view;
        button.setVisibility(View.INVISIBLE);
        ImageButton stop =(ImageButton)findViewById(R.id.stopButton);
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

    public static void updateTime(Period interval,TimeTextViews views,Activity ctx){
        String days= getDays(interval, ctx);
        views.getDay().setText(days);
        String hours= getHours(interval, ctx);
        views.getHour().setText(hours);
        String minutes= getMinutes(interval, ctx);
        views.getMinute().setText(minutes);
        String seconds= getSeconds(interval, ctx);
        views.getSecond().setText(seconds);
        Log.i(TAG, "updateTime "+days+hours+minutes+seconds);
    }

    private static String getDays(Period interval, Activity ctx) {
        return extract((interval.getWeeks() * 7 + interval.getDays()),ctx,R.string.days);
    }

    private static String getSeconds(Period interval, Activity ctx) {
        return extract(interval.getSeconds(),ctx,R.string.seconds);
    }

    private static String getMinutes(Period interval, Activity ctx) {
        return extract(interval.getMinutes(),ctx,R.string.minutes);
    }

    private static String getHours(Period interval, Activity ctx) {
        return extract(interval.getHours(),ctx,R.string.hours);
    }

    private static String extract(int value, Activity ctx,int stringId) {
        return Integer.valueOf(value).toString()+" "+ctx.getString(stringId);
    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.i(TAG, "onStop called");
        SharedPreferences.Editor edit = settings.edit();
        edit.putString(TARGET_DATE, targetDate.toString());
        edit.commit();



    }

    public void resetCounter(View view) {
        targetDate=new DateTime().plusDays(21);
        SharedPreferences.Editor edit = settings.edit();
        edit.putString(TARGET_DATE,targetDate.toString());
        edit.commit();


    }
}
