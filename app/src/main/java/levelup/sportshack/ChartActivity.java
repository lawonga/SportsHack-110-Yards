package levelup.sportshack;

import android.app.Activity;
import android.os.Bundle;

import com.github.mikephil.charting.charts.BarChart;
import com.github.mikephil.charting.data.BarData;
import com.github.mikephil.charting.data.BarDataSet;
import com.github.mikephil.charting.data.BarEntry;

import java.util.ArrayList;

/**
 * Created by Andy W on 2015-11-28.
 */
public class ChartActivity extends Activity {
    BarChart barChart;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.chart_activity);
        // Add in the chart, and the properties
        barChart = (BarChart)findViewById(R.id.chart);
        barChart.setDrawGridBackground(true);

        // Set yes/no (Barlist)
        ArrayList<String> barlist = new ArrayList<>();
        barlist.add("Yes");
        barlist.add("No");

        // Set dataset (actual data)
        ArrayList<BarEntry> yVal = new ArrayList<>();

        // BarEntry
        BarEntry barEntryNo = new BarEntry(32, 0);
        BarEntry barEntryYes = new BarEntry(72, 1);

        yVal.add(barEntryNo);
        yVal.add(barEntryYes);

        // Grab from yVal
        BarDataSet barDataSet = new BarDataSet(yVal, "Question"); // TODO Change 'Question' to something meaningful

        // Create bar array, set barDataSet with the yVals with the {BarEntry} in
        ArrayList<BarDataSet> barArray = new ArrayList<>();
        barArray.add(barDataSet);

        BarData barData = new BarData(barlist, barArray);


        barChart.setData(barData);

        try {
            Thread.sleep(1000);
        } catch (InterruptedException e) {
            e.printStackTrace();
        }

        barEntryNo.setVal(80);
        // yVal at 0 = NO, at 1 = YES
        yVal.set(0, barEntryNo);

        barChart.notifyDataSetChanged();
    }

}
