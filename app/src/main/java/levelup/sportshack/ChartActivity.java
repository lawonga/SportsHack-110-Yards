package levelup.sportshack;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.util.Log;
import android.widget.Toast;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;
import com.parse.FindCallback;
import com.parse.ParseException;
import com.parse.ParseObject;
import com.parse.ParsePushBroadcastReceiver;
import com.parse.ParseQuery;

import java.util.ArrayList;
import java.util.List;

import levelup.sportshack.Logic.CustomReceiver;

/**
 * Created by Andy W on 2015-11-28.
 */
public class ChartActivity extends Activity {
    BarChart barChart;
    ParsePushBroadcastReceiver customReceiver;
    int[] points = {0,0,0,0};
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);

        // Add in the chart, and the properties
        barChart = (BarChart) findViewById(R.id.chart);
        barChart.setDrawGridBackground(true);

        // Set yes/no (Barlist)
        ArrayList<String> barlist = new ArrayList<>();
        barlist.add("Yes");
        barlist.add("No");

        // Set dataset (actual data)
        final ArrayList<BarEntry> heightVal = new ArrayList<>();

        // BarEntry
        final BarEntry barrEntry0 = new BarEntry(0, 0);
        final BarEntry barEntry1 = new BarEntry(0, 1);

        heightVal.add(barrEntry0);
        heightVal.add(barEntry1);

        // Grab from heightVal
        BarDataSet barDataSet = new BarDataSet(heightVal, "Question"); // TODO Change 'Question' to something meaningful

        // Create bar array, set barDataSet with the yVals with the {BarEntry} in
        ArrayList<BarDataSet> barArray = new ArrayList<>();
        barArray.add(barDataSet);

        BarData barData = new BarData(barlist, barArray);

        barChart.setAutoScaleMinMaxEnabled(true);
        barChart.setData(barData);

        // On run action: Grab data from the servers!
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Questionbank");
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        points[0] = object.getInt("point0");
                        points[1] = object.getInt("point1");
                        points[2] = object.getInt("point2");
                        points[3] = object.getInt("point3");

                        for (int i = 0; i < 4; i++) {
                            BarEntry barEntry = new BarEntry(0, 0);
                            barEntry.setVal(points[i]);
                            barEntry.setXIndex(i);
                            heightVal.add(i, barEntry);
                        }

                        int highestvalue = points[0];
                        for (int i = 1; i < points.length; i++) {
                            if (points[i] > highestvalue) {
                                highestvalue = points[i];
                            }
                        }

                        barChart.notifyDataSetChanged();
                        barChart.invalidate();
                    }
                }
            }
        });

        // Build the Receiver also known as CustomReceiver which extends from ParsePushBroadcastReceiver!!!
        customReceiver = new CustomReceiver() {
            @Override
            public void onReceive(Context context, Intent intent) {
                Bundle bundle = intent.getExtras();
                int pointschanged = bundle.getInt("point");
                int answerchanged = bundle.getInt("answer");
                int floatentry = 0;
                if (answerchanged == 0){
                    floatentry = points[0];
                    points[0] += pointschanged;
                } else if (answerchanged == 1){
                    floatentry = points[1];
                    points[1] += pointschanged;
                }
                BarEntry barEntry = new BarEntry(0,0);
                // Update the thing
                barEntry.setVal(floatentry + pointschanged);
                barEntry.setXIndex(answerchanged);
                // heightVal at 0 = NO, at 1 = YES
                heightVal.set(answerchanged, barEntry);
                barChart.notifyDataSetChanged();
                barChart.invalidate();
            }
        };

        // Register the receiver from CustomReceiver - which extends from ParsePushBroadcastReceiver -- CUSTOM RECEIVER!
        registerReceiver(customReceiver, new IntentFilter("broadcast"));
    }

}
