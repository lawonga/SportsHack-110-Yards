package levelup.sportshack.Dialogs;

import android.app.DialogFragment;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;

import com.github.mikephil.charting.animation.Easing;
import com.github.mikephil.charting.charts.PieChart;
import com.github.mikephil.charting.components.Legend;
import com.github.mikephil.charting.components.XAxis;
import com.github.mikephil.charting.data.Entry;
import com.github.mikephil.charting.data.PieData;
import com.github.mikephil.charting.data.PieDataSet;
import com.github.mikephil.charting.formatter.PercentFormatter;

import java.util.ArrayList;

import levelup.sportshack.FOURQuestionsActivity;
import levelup.sportshack.R;

/**
 * Created by Andy W on 2015-11-29.
 */
public class HistoricalStatsDialog extends DialogFragment {
    TextView close;
    PieChart pieChart;
    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.historical_card, container);
        close = (TextView)view.findViewById(R.id.close_popup_reward);
        pieChart = (PieChart)view.findViewById(R.id.chart);
        pieChart.setDescription("");
        pieChart.setUsePercentValues(true);
        pieChart.setDrawHoleEnabled(true);
        pieChart.setHoleColorTransparent(true);
        pieChart.setTransparentCircleColor(Color.WHITE);
        pieChart.setTransparentCircleAlpha(110);
        pieChart.setHoleRadius(58f);
        pieChart.setTransparentCircleRadius(61f);
        pieChart.setDrawCenterText(false);
        pieChart.setRotationAngle(0);
        pieChart.setRotationEnabled(false);
        pieChart.setHighlightPerTapEnabled(true);
        pieChart.animateY(1400, Easing.EasingOption.EaseInOutQuad);
        Legend legend = pieChart.getLegend();
        legend.setEnabled(false);

        // For stat
        ArrayList<Entry> yVals = new ArrayList<>();
        yVals.add(new Entry((float) (85), 0));
        yVals.add(new Entry((float) (15), 0));

        // For description
        ArrayList<String> xVals = new ArrayList<>();
        //HACK to get the text invisible
        xVals.add("");
        xVals.add("");

        // Dataset
        PieDataSet dataSet = new PieDataSet(yVals, "Historical Results");
        dataSet.setColors(new int[] {getResources().getColor(R.color.yellowBackground), getResources().getColor(R.color.whiteBackground)});

        // PieData
        PieData pieData = new PieData(xVals, dataSet);
        pieData.setDrawValues(false);
        pieChart.setData(pieData);
        pieChart.invalidate();



        close.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                dismiss();
            }
        });

        return view;
    }
}
