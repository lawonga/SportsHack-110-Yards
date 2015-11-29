package levelup.sportshack;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.FragmentManager;
import android.content.DialogInterface;
import android.content.Intent;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.util.Log;

import com.daimajia.easing.quad.QuadEaseIn;
import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.components.YAxis;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

import levelup.sportshack.Dialogs.RewardDialog;
import levelup.sportshack.Dialogs.WelcomeDialog;

/**
 * Created by Andy W on 2015-11-29.
 */
public class ProgressActivity extends Activity {
    BarChart barChart;
    int timer = 5;
    boolean openedcheck = false;
    @Override
    protected void onPostCreate(Bundle savedInstanceState) {
        super.onPostCreate(savedInstanceState);
        setContentView(R.layout.progress_activity);
        barChart = (BarChart)findViewById(R.id.chart);

        // Set yes/no (Barlist)
        ArrayList<String> barlist = new ArrayList<>();
        barlist.add("Score");

        // Set dataset (actual data)
        final ArrayList<BarEntry> heightVal = new ArrayList<>();


        final BarEntry barEntry0 = new BarEntry(45, 0);
        heightVal.add(barEntry0);

        final BarDataSet barDataSet = new BarDataSet(heightVal, "Score"); // TODO Change 'Question' to something meaningful
        final ArrayList<BarDataSet> barArray = new ArrayList<>();
        barArray.add(barDataSet);
        final BarData barData = new BarData(barlist, barArray);
        barDataSet.setColor(getResources().getColor(R.color.whiteBackground));
        barChart.setDrawGridBackground(false);
        barChart.getXAxis().setDrawGridLines(false);
        barChart.getAxisLeft().setDrawGridLines(false);
        barChart.getAxisRight().setDrawGridLines(false);
        barChart.getLegend().setEnabled(false);
        barChart.getXAxis().setPosition(XAxis.XAxisPosition.BOTTOM);
        barChart.animateY(500, Easing.EasingOption.EaseInQuad);
        barChart.setVisibleYRangeMaximum(110, YAxis.AxisDependency.LEFT);
        barChart.setVisibleYRangeMaximum(110, YAxis.AxisDependency.RIGHT);
        barChart.setScaleYEnabled(false);
        barChart.setDescription("");
        barChart.setMaxVisibleValueCount(110);
        barChart.setData(barData);


        new CountDownTimer(timer*1000, 1000){
            @Override
            public void onTick(long millisUntilFinished) {
                // NOTHING
            }

            @Override
            public void onFinish() {
                if (!openedcheck){
                    FragmentManager fragmentManager = getFragmentManager();
                    RewardDialog rewardDialog = new RewardDialog();
                    rewardDialog.show(fragmentManager, "new_team");
                    openedcheck = true;
                }
            }
        }.start();



    }
}
