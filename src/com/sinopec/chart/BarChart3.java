package com.sinopec.chart;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.R;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.graphics.drawable.ColorDrawable;
import android.util.Log;
import android.view.View;
import android.widget.LinearLayout;

public class BarChart3 {

	double m_oil=0;
	double m_gas=0;
	double m_coil=0;
	int m_oilColor=Color.GREEN;
	int m_gasColor=Color.RED;
	int m_coilColor=Color.BLUE;
	int m_width=100;
	int m_height=100;//最长的“柱”的高度，其它“柱”按比例配置高度
	float m_textsize=20;//字体大小
	
	public BarChart3() {
		super();
		// TODO Auto-generated constructor stub
	}
	public BarChart3(double oil,double gas,double coil) {
		super();
		// TODO Auto-generated constructor stub
		m_oil=oil;
		m_gas=gas;
		m_coil=coil;
	}
	public BarChart3(double oil,double gas,double coil,int w,int h) {
		super();
		// TODO Auto-generated constructor stub
		m_oil=oil;
		m_gas=gas;
		m_coil=coil; 
		m_width=w;
		m_height=h;
	}

	public GraphicalView GetBarChartView(Context context) {
		double[] x = { 0, 1 };
		double[] oil = { 0, 0 };
		double[] gas = { 0, 0 };
		double[] coil = { 0, 0 };
		double yMax=GetMaxValue()+m_textsize*1.5;
		//GraphicalView gView=new GraphicalView(context, null);
		GraphicalView gView=null;
		
		oil[0]=m_oil;
		gas[0]=m_gas;
		coil[0]=m_coil;

		// Creating XYSeries
		XYSeries oilSeries = new XYSeries("oil");
		XYSeries gasSeries = new XYSeries("gas");
		XYSeries coilSeries = new XYSeries("coil");

		// Adding data to Income and Expense Series
		for (int i = 0; i < x.length; i++) {
			oilSeries.add(i, oil[i]);
			gasSeries.add(i, gas[i]);
			coilSeries.add(i, coil[i]);
		}

		// Creating a dataset to hold each series
		XYMultipleSeriesDataset dataset = new XYMultipleSeriesDataset();
		// Adding Series to the dataset
		dataset.addSeries(oilSeries);
		dataset.addSeries(gasSeries);
		dataset.addSeries(coilSeries);

		// Creating XYSeriesRenderer to customize incomeSeries
		XYSeriesRenderer oilRenderer = new XYSeriesRenderer();
		oilRenderer.setColor(Color.RED);
		oilRenderer.setFillPoints(true);
		oilRenderer.setLineWidth(2);
		oilRenderer.setDisplayChartValues(true);
		oilRenderer.setChartValuesTextSize(m_textsize);

		// Creating XYSeriesRenderer to customize expenseSeries
		XYSeriesRenderer gasRenderer = new XYSeriesRenderer();
		gasRenderer.setColor(Color.GREEN);
		gasRenderer.setFillPoints(true);
		gasRenderer.setLineWidth(2);
		gasRenderer.setDisplayChartValues(true);
		gasRenderer.setChartValuesTextSize(m_textsize);

		// Creating XYSeriesRenderer to customize expenseSeries
		XYSeriesRenderer coilRenderer = new XYSeriesRenderer();
		coilRenderer.setColor(Color.BLUE);
		coilRenderer.setFillPoints(true);
		coilRenderer.setLineWidth(2);
		coilRenderer.setDisplayChartValues(true);
		coilRenderer.setChartValuesTextSize(m_textsize);

		// Creating a XYMultipleSeriesRenderer to customize the whole chart
		XYMultipleSeriesRenderer multiRenderer = new XYMultipleSeriesRenderer();
		multiRenderer.setXLabels(0);
		multiRenderer.setChartTitle("");
		multiRenderer.setXTitle("");
		multiRenderer.setYTitle("");
		multiRenderer.setZoomButtonsVisible(false);
		multiRenderer.setXLabels(0);
		multiRenderer.setYLabels(0);

		// Adding incomeRenderer and expenseRenderer to multipleRenderer
		// Note: The order of adding dataseries to dataset and renderers to
		// multipleRenderer
		// should be same
		multiRenderer.addSeriesRenderer(oilRenderer);
		multiRenderer.addSeriesRenderer(gasRenderer);
		multiRenderer.addSeriesRenderer(coilRenderer);
		// Creating an intent to plot bar chart using dataset and
		multiRenderer.setShowLegend(false);
		//multiRenderer.setShowAxes(false);
		multiRenderer.setPanEnabled(false);//
		multiRenderer.setXAxisMax(0.5);
		multiRenderer.setXAxisMin(-0.5);
		multiRenderer.setYAxisMax(yMax);
		multiRenderer.setZoomEnabled(false, false);//
		multiRenderer.setMargins(new int[] { 0, 0,-25, 0 });//

		gView=ChartFactory.getBarChartView(context, dataset, multiRenderer,Type.DEFAULT);
		
		Log.v("mandy", "m_height: " + m_height + " yMax: " + yMax + " GetMaxValue(): " + GetMaxValue());
		
		
		gView.setLayoutParams(new LinearLayout.LayoutParams(m_width,m_height));
		return gView;
	}

	public Bitmap GetBarChartBitmap(Context context) {
		    GraphicalView gView=GetBarChartView(context);
		
		    Log.v("mandy", "width:" + gView.getLayoutParams().width);
		    Log.v("mandy", "height: " +gView.getLayoutParams().height);
		   Bitmap bitmap = Bitmap.createBitmap(gView.getLayoutParams().width,  gView.getLayoutParams().height, Bitmap.Config.ARGB_4444);
		   
		   BitmapFactory.Options options = new BitmapFactory.Options();
		   options.inSampleSize = 2;
		   
		   
//		   gView.setBackgroundColor(context.getResources().getColor(R.color.holo_orange_light));
		   
		   gView.setBackgroundDrawable(new ColorDrawable(context.getResources().getColor(R.color.transparent)));
//		   bitmap.compress(CompressFormat.PNG, 30, stream);
		   
		    Canvas canvas =  new Canvas(bitmap);
		    canvas.drawColor(Color.TRANSPARENT);
		    gView.draw(canvas);
		
	
		
		return bitmap;	
	}
	
	public double GetMaxValue() {
		double d=m_oil;
		if(m_gas>d) d=m_gas;
		if(m_coil>d) d=m_coil;
		return d;
	}
}
