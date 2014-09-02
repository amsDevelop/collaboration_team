package com.sinopec.chart;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.PointStyle;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.chartdemo.demo.chart.AbstractDemoChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import com.sinopec.application.SinoApplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.widget.LinearLayout;

public class PolygonLineChart3 extends AbstractDemoChart{
	private Context mContext;
	public PolygonLineChart3(Context context){
		this.mContext = context;
	}
	
	public PolygonLineChart3(){
	}
	
	public GraphicalView getBarChartView(Context context) {
		GraphicalView gView=null;
		String[] titles = new String[] { "New tickets", "Fixed tickets" };
	    List<Date[]> dates = new ArrayList<Date[]>();
	    List<double[]> values = new ArrayList<double[]>();
	    int length = titles.length;
	    for (int i = 0; i < length; i++) {
	      dates.add(new Date[12]);
	      dates.get(i)[0] = new Date(108, 9, 1);
	      dates.get(i)[1] = new Date(108, 9, 8);
	      dates.get(i)[2] = new Date(108, 9, 15);
	      dates.get(i)[3] = new Date(108, 9, 22);
	      dates.get(i)[4] = new Date(108, 9, 29);
	      dates.get(i)[5] = new Date(108, 10, 5);
	      dates.get(i)[6] = new Date(108, 10, 12);
	      dates.get(i)[7] = new Date(108, 10, 19);
	      dates.get(i)[8] = new Date(108, 10, 26);
	      dates.get(i)[9] = new Date(108, 11, 3);
	      dates.get(i)[10] = new Date(108, 11, 10);
	      dates.get(i)[11] = new Date(108, 11, 17);
	    }
	    values.add(new double[] { 142, 123, 142, 152, 149, 122, 110, 120, 125, 155, 146, 150 });
	    values.add(new double[] { 102, 90, 112, 105, 125, 112, 125, 112, 105, 115, 116, 135 });
	    length = values.get(0).length;
	    int[] colors = new int[] { Color.BLUE, Color.GREEN };
	    PointStyle[] styles = new PointStyle[] { PointStyle.POINT, PointStyle.POINT };
	    XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
	    setChartSettings(renderer, "Project work status", "Date", "Tickets", dates.get(0)[0].getTime(),
	        dates.get(0)[11].getTime(), 50, 190, Color.GRAY, Color.LTGRAY);
	    renderer.setXLabels(0);
	    renderer.setYLabels(10);
	    renderer.addYTextLabel(100, "test");
	    length = renderer.getSeriesRendererCount();
	    for (int i = 0; i < length; i++) {
	      SimpleSeriesRenderer seriesRenderer = renderer.getSeriesRendererAt(i);
	      seriesRenderer.setDisplayChartValues(true);
	    }
	    renderer.setXRoundedLabels(true);
	    gView= ChartFactory.getLineChartView(context,  buildDateDataset(titles, dates, values), renderer);
//		gView=ChartFactory.getBarChartView(context, buildDateDataset(titles, dates, values), renderer,Type.DEFAULT);
//		gView.setBackgroundColor(Color.WHITE);
		gView.setBackgroundColor(Color.TRANSPARENT);
		gView.setLayoutParams(new LinearLayout.LayoutParams(SinoApplication.screenWidth / 2, SinoApplication.screenHeight - 300));
		return gView;
	}

	@Override
	public String getName() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public String getDesc() {
		// TODO Auto-generated method stub
		return null;
	}

	@Override
	public Intent execute(Context context) {
		// TODO Auto-generated method stub
		return null;
	}
}
