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
        yVal.add(new BarEntry(52, 0));
        yVal.add(new BarEntry(72, 1));

        // Grab from yVal
        BarDataSet barDataSet = new BarDataSet(yVal, "Question"); // TODO Change 'Question' to something meaningful

        // Create bar array, set barDataSet with the yVals with the {BarEntry} in
        ArrayList<BarDataSet> barArray = new ArrayList<>();
        barArray.add(barDataSet);

        BarData barData = new BarData(barlist, barArray);


        barChart.setData(barData);
    }

}
