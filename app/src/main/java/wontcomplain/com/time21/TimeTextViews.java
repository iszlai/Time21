package wontcomplain.com.time21;

import android.widget.TextView;

/**
 * Created by Lehel on 8/26/2015.
 */
public class TimeTextViews {
    private TextView day;
    private TextView hour;
    private TextView minute;
    private TextView second;

    public TimeTextViews(TextView day,TextView hour,TextView minute,TextView second){

        this.day = day;
        this.hour = hour;
        this.minute = minute;
        this.second = second;
    }

    public TextView getDay() {
        return day;
    }

    public TextView getHour() {
        return hour;
    }

    public TextView getMinute() {
        return minute;
    }

    public TextView getSecond() {
        return second;
    }
}
