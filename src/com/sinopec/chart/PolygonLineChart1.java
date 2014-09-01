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
import org.achartengine.util.MathHelper;

import com.sinopec.application.SinoApplication;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.widget.LinearLayout;

public class PolygonLineChart1 extends AbstractDemoChart {
	private static final long HOUR = 3600 * 1000;

	private static final long DAY = HOUR * 24;

	private static final int HOURS = 24;

	public GraphicalView getBarChartView(Context context) {
		GraphicalView gView = null;
		String[] titles = new String[] { "Inside", "Outside" };
		long now = Math.round(new Date().getTime() / DAY) * DAY;
		List<Date[]> x = new ArrayList<Date[]>();
		for (int i = 0; i < titles.length; i++) {
			Date[] dates = new Date[HOURS];
			for (int j = 0; j < HOURS; j++) {
				dates[j] = new Date(now - (HOURS - j) * HOUR);
			}
			x.add(dates);
		}
		List<double[]> values = new ArrayList<double[]>();

		values.add(new double[] { 21.2, 21.5, 21.7, 21.5, 21.4, 21.4, 21.3,
				21.1, 20.6, 20.3, 20.2, 19.9, 19.7, 19.6, 19.9, 20.3, 20.6,
				20.9, 21.2, 21.6, 21.9, 22.1, 21.7, 21.5 });
		values.add(new double[] { 1.9, 1.2, 0.9, 0.5, 0.1, -0.5, -0.6,
				MathHelper.NULL_VALUE, MathHelper.NULL_VALUE, -1.8, -0.3, 1.4,
				3.4, 4.9, 7.0, 6.4, 3.4, 2.0, 1.5, 0.9, -0.5,
				MathHelper.NULL_VALUE, -1.9, -2.5, -4.3 });

		int[] colors = new int[] { Color.GREEN, Color.BLUE };
		PointStyle[] styles = new PointStyle[] { PointStyle.CIRCLE,
				PointStyle.DIAMOND };
		XYMultipleSeriesRenderer renderer = buildRenderer(colors, styles);
		int length = renderer.getSeriesRendererCount();
		for (int i = 0; i < length; i++) {
			((XYSeriesRenderer) renderer.getSeriesRendererAt(i))
					.setFillPoints(true);
		}
		setChartSettings(renderer, "Sensor temperature", "Hour",
				"Celsius degrees", x.get(0)[0].getTime(),
				x.get(0)[HOURS - 1].getTime(), -5, 30, Color.LTGRAY,
				Color.LTGRAY);
		renderer.setXLabels(10);
		renderer.setYLabels(10);
		renderer.setShowGrid(true);
		renderer.setXLabelsAlign(Align.CENTER);
		renderer.setYLabelsAlign(Align.RIGHT);
		gView = ChartFactory
				.getBarChartView(context,
						buildDateDataset(titles, x, values), renderer,
						Type.DEFAULT);
		// gView.setBackgroundColor(Color.WHITE);
		gView.setBackgroundColor(Color.TRANSPARENT);
		gView.setLayoutParams(new LinearLayout.LayoutParams(
				SinoApplication.screenWidth / 2,
				SinoApplication.screenHeight - 300));
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
