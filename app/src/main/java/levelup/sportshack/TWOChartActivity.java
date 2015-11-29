package levelup.sportshack;

import android.app.Activity;
import android.app.AlertDialog;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
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
import java.util.Random;

import levelup.sportshack.Logic.ParseReceiver;

/**
 * Created by Andy W on 2015-11-28.
 */
public class TWOChartActivity extends Activity {
    BarChart barChart;
    ParsePushBroadcastReceiver customReceiver;
    BroadcastReceiver answerReceiver;
    int[] points = {0,0,0,0};
    int game_id, numberofanswers, timer = 5;
    String object_id;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);
        Intent previousIntent = getIntent();
        Bundle previousBunde = previousIntent.getExtras();
        game_id = previousBunde.getInt("game_id");
        numberofanswers = previousBunde.getInt("numberofanswers");
        object_id = previousBunde.getString("objectID");
        // timer = previousBunde.getInt("currentTime");

        // Add in the chart, and the properties
        barChart = (BarChart) findViewById(R.id.chart);
        barChart.setDrawGridBackground(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getLegend().setEnabled(false);

        XAxis xAxis = barChart.getXAxis();
        xAxis.setPosition(XAxis.XAxisPosition.BOTTOM);

        YAxis leftAxis = barChart.getAxisLeft();
        leftAxis.setDrawLabels(false);

        YAxis rightAxis = barChart.getAxisRight();
        rightAxis.setDrawLabels(false);


        // Set yes/no (Barlist)
        ArrayList<String> barlist = new ArrayList<>();
        barlist.add("Yes");
        barlist.add("No");

        // Set dataset (actual data)
        final ArrayList<BarEntry> heightVal = new ArrayList<>();

        // BarEntry
        final BarEntry barEntry0 = new BarEntry(0, 0);
        final BarEntry barEntry1 = new BarEntry(0, 1);
        heightVal.add(barEntry0);
        heightVal.add(barEntry1);
        if (numberofanswers == 4){
            final BarEntry barEntry2= new BarEntry(0, 2);
            final BarEntry barEntry3 = new BarEntry(0, 3);
            heightVal.add(barEntry2);
            heightVal.add(barEntry3);
        }

        // Grab from heightVal
        final BarDataSet barDataSet = new BarDataSet(heightVal, "Question"); // TODO Change 'Question' to something meaningful

        // Create bar array, set barDataSet with the yVals with the {BarEntry} in
        final ArrayList<BarDataSet> barArray = new ArrayList<>();
        barArray.add(barDataSet);

        final BarData barData = new BarData(barlist, barArray);
        barChart.setAutoScaleMinMaxEnabled(true);
        barChart.setData(barData);

        // On run action: Grab data from the servers!
        ParseQuery<ParseObject> query = ParseQuery.getQuery("Questionbank");
        query.whereEqualTo("game_id", game_id);
        query.whereEqualTo("objectId", object_id);
        query.findInBackground(new FindCallback<ParseObject>() {
            @Override
            public void done(List<ParseObject> objects, ParseException e) {
                if (e == null) {
                    for (ParseObject object : objects) {
                        Log.e("currentQuestion", object.getString("question"));
                        points[0] = object.getInt("point0");
                        points[1] = object.getInt("point1");
                        points[2] = object.getInt("point2");
                        points[3] = object.getInt("point3");

                        for (int i = 0; i < objects.size() + 1; i++) {
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

                        // ANIMATION MAY NOT WORK barChart.animateY(500, Easing.EasingOption.EaseInCirc);
                        barChart.notifyDataSetChanged();
                        barChart.invalidate();
                    }
                }
            }
        });

        // Add timer
        new CountDownTimer(timer*1000, 250){
            @Override
            public void onTick(long millisUntilFinished) {
                Random r = new Random();
                int i1 = (r.nextInt(1 ) + 1);
                int floatentry = 0;
                if (i1 == 0){
                    floatentry = points[0];
                    points[0] += i1;
                } else if (i1 == 1){
                    floatentry = points[1];
                    points[1] += i1;
                }

                BarEntry barEntry = new BarEntry(0,0);
                // Update the thing
                barEntry.setVal(floatentry + i1);
                barEntry.setXIndex(i1);
                // heightVal at 0 = NO, at 1 = YES
                heightVal.set(i1, barEntry);
                // ANIMATION MAY NOT WORK barChart.animateY(500, Easing.EasingOption.EaseInCirc);
                barChart.notifyDataSetChanged();
                barChart.invalidate();

            }

            @Override
            public void onFinish() {
                // TODO make the stupid thing go red!
                // For now we pop the thing up
                barDataSet.getColors().remove(0);
                barDataSet.setColors(new int[]{R.color.helpBlue, R.color.red});
                barChart.notifyDataSetChanged();
                barChart.invalidate();

                new AlertDialog.Builder(TWOChartActivity.this)
                        .setTitle("Congratulations!")
                        .setNeutralButton("Continue", new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                if (RoadActivity.triggered == false){
                                    // continue
                                    RoadActivity.triggered = true;
                                    Intent intent = new Intent(getApplicationContext(), ProgressActivity.class);
                                    startActivity(intent);
                                    dialog.dismiss();
                                    finish();
                                }
                            }
                        })
                        .show();
                Log.e("Timer", "finished");
            }
        }.start();

        // Build the Receiver also known as ParseReceiver which extends from ParsePushBroadcastReceiver!!!
        customReceiver = new ParseReceiver() {
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
                // ANIMATION MAY NOT WORK barChart.animateY(500, Easing.EasingOption.EaseInCirc);
                barChart.notifyDataSetChanged();
                barChart.invalidate();
            }
        };

        // Register the receiver from ParseReceiver - which extends from ParsePushBroadcastReceiver -- CUSTOM RECEIVER!
        registerReceiver(customReceiver, new IntentFilter("broadcast"));
    }

    @Override
    public void onBackPressed() {
        finish();
    }
}
