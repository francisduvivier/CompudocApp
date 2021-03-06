package duviwin.compudocapp.usage_stats;

import android.content.Context;

import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.Tracker;

import java.util.Map;

/**
 * Created by Francis Duvivier on 7/26/2015.
 */
public class MyTracker {
    public static GoogleAnalytics analytics;

    public static Tracker tracker; // Replace with actual tracker/property Id
    public static void startAnalytics(Context context){
        analytics=GoogleAnalytics.getInstance(context);

        //The following should always be changed together with the build-number in build.gradle
//        tracker = analytics.newTracker("UA-65610134-1");//for my own usage
        tracker = analytics.newTracker("UA-65610134-2");//for play store usage
        tracker.enableExceptionReporting(true);
        tracker.enableAutoActivityTracking(true);
    }

    public static void send(String cat, String action, String label) {
        Map<String, String> event;
        if (label == null) {
            event = new HitBuilders.EventBuilder()
                    .setCategory(cat)
                    .setAction(action)
                    .build();
        } else {
            event = new HitBuilders.EventBuilder()
                    .setCategory(cat)
                    .setAction(action)
                    .build();
        }
        tracker.send(event);
    }

}
