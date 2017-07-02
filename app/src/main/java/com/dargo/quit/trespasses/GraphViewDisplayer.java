package com.dargo.quit.trespasses;


import android.content.Context;
import android.graphics.Color;

import com.dargo.quit.habits.Habit;
import com.dargo.quit.trespass_counters.ConstSQLiteTrespassCounters;
import com.dargo.quit.trespass_counters.TrespassCounter;
import com.jjoe64.graphview.GraphView;
import com.jjoe64.graphview.ValueDependentColor;
import com.jjoe64.graphview.helper.DateAsXAxisLabelFormatter;
import com.jjoe64.graphview.series.BarGraphSeries;
import com.jjoe64.graphview.series.DataPoint;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

public class GraphViewDisplayer {

  private final GraphView graph;
  private final Context context;

  public GraphViewDisplayer(GraphView graph, Context context) {
    this.graph = graph;
    this.context = context;
  }

  public void display(Habit defaultHabit) {
    BarGraphSeries<DataPoint> series = makeSeries(defaultHabit);
    setYAxisLabels(graph, series);
    setXAxisLabels(graph, context, series);
    graph.getGridLabelRenderer().setHumanRounding(false);
  }

  private BarGraphSeries<DataPoint> makeSeries(Habit defaultHabit) {
    BarGraphSeries<DataPoint> series = createSeries(defaultHabit);

    graph.removeAllSeries();
    graph.addSeries(series);
    setSeriesColor(series);


    series.setSpacing(50);
    series.setDrawValuesOnTop(true);
    return series;
  }

  private void setXAxisLabels(GraphView graph, Context context, BarGraphSeries<DataPoint> series) {
    graph.getGridLabelRenderer().setNumHorizontalLabels(7);
    graph.getGridLabelRenderer().setHorizontalLabelsAngle(30);

    graph.getGridLabelRenderer().setLabelFormatter(new DateAsXAxisLabelFormatter(context));

    Date first = new Date((long) series.getHighestValueX());
    Date last = new Date((long) series.getLowestValueX());
    graph.getViewport().setMinX(first.getTime() - 24*60*60*1000);
    graph.getViewport().setMaxX(last.getTime() + 24*60*60*1000);
    graph.getViewport().setXAxisBoundsManual(true);
  }

  private void setYAxisLabels(GraphView graph, BarGraphSeries<DataPoint> series) {
    int maxYValue = (int) series.getHighestValueY()+1;
    int maxLabel = 10;
    int interval = 1;

    while (interval * 10 < maxYValue) {
      interval++;
      maxLabel = interval * 10;
    }

    graph.getGridLabelRenderer().setNumVerticalLabels(11);
    graph.getViewport().setMinY(0.0);
    graph.getViewport().setMaxY(maxLabel);
    graph.getViewport().setYAxisBoundsManual(true);
  }

  private void setSeriesColor(BarGraphSeries<DataPoint> series) {
    series.setValueDependentColor(new ValueDependentColor<DataPoint>() {
      @Override
      public int get(DataPoint data) {
        return Color.rgb((int) data.getX()*255/4, (int) Math.abs(data.getY()*255/6), 100);
      }
    });
  }

  private BarGraphSeries<DataPoint> createSeries(Habit defaultHabit) {
    List<DataPoint> dataPoints = new ArrayList<>();
    for (TrespassCounter trespass :
            new ConstSQLiteTrespassCounters(context).trespassesPerDayFor(defaultHabit)) {
      dataPoints.add(new DataPoint(trespass.getDateOfDay().getTime(), trespass.getTrespassesPerDay()));
    }
    DataPoint[] dataPointsArray = new DataPoint[dataPoints.size()];
    dataPointsArray = dataPoints.toArray(dataPointsArray);
    return new BarGraphSeries<>(dataPointsArray);
  }
}
