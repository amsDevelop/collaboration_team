package com.sinopec.chart;

import java.text.NumberFormat;

import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.BarChart.Type;
import org.achartengine.model.CategorySeries;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.model.XYSeries;
import org.achartengine.renderer.DefaultRenderer;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;
import org.achartengine.renderer.XYSeriesRenderer;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.Paint.Align;
import android.view.View;
import android.view.ViewGroup.LayoutParams;
import android.widget.LinearLayout;

public class PieChart3 {
	double m_oil=0;//ʯ��
	double m_gas=0;//��Ȼ��
	double m_coil=0;//������
	int m_oilColor=Color.GREEN;
	int m_gasColor=Color.RED;
	int m_coilColor=Color.BLUE;
	int m_diameter=100;//ͼ��ֱ��
	float m_textsize=20;//�����С
	
	public PieChart3() {
		super();
		// TODO Auto-generated constructor stub
	}
	public PieChart3(double oil,double gas,double coil) {
		super();
		// TODO Auto-generated constructor stub
		m_oil=oil;
		m_gas=gas;
		m_coil=coil;
	}
	public PieChart3(double oil,double gas,double coil,int d) {
		super();
		// TODO Auto-generated constructor stub
		m_oil=oil;
		m_gas=gas;
		m_coil=coil; 
		m_diameter=d;
	}

	public GraphicalView GetPieChartView(Context context) {

		double[] data = new double[] { m_oil, m_gas, m_coil };
		int Margin=0;
		GraphicalView gView=null;
		int[] COLORS = new int[] {m_oilColor,m_gasColor,m_coilColor};
		DefaultRenderer dRenderer=new DefaultRenderer();
		CategorySeries cSeries = new CategorySeries("");
		double VALUE = m_oil+m_gas+m_coil;//�ܺ�
		
		for (int i = 0; i < data.length; i++) {
			cSeries.add("" + (i + 1), data[i] / VALUE);// ����������ƺͶ�Ӧ����ֵ
			SimpleSeriesRenderer renderer = new SimpleSeriesRenderer();
			if (i < COLORS.length) {
				renderer.setColor(COLORS[i]);// �������������ɫ
			} else {
				renderer.setColor(COLORS[0]);// �������������ɫ
			}
			renderer.setChartValuesFormat(NumberFormat.getPercentInstance());// ���ðٷֱ�
			//renderer.setChartValuesTextSize(50);
			renderer.setChartValuesTextAlign(Align.CENTER);			
			//dRenderer.setChartTitleTextSize(20);// ���ñ�ͼ�����С
			dRenderer.addSeriesRenderer(renderer);// �����µ��������ӵ�DefaultRenderer��
		}
		
		dRenderer.setLabelsTextSize(m_textsize);
		dRenderer.setLabelsColor(Color.BLACK);
		dRenderer.setDisplayValues(true);
		dRenderer.setShowLabels(false);
		dRenderer.setShowLegend(false);
		dRenderer.setMargins(new int[]{Margin,Margin,Margin,Margin});
		dRenderer.setPanEnabled(false);
		dRenderer.setZoomEnabled(false);


		gView=ChartFactory.getPieChartView(context,cSeries, dRenderer);
		gView.setLayoutParams(new LayoutParams(m_diameter*4/3, m_diameter*4/3));
		
		return gView;
	}

	public Bitmap GetBarChartBitmap(Context context) {
		GraphicalView gView=GetPieChartView(context);
		
		int left,right,top,bottom,w,h;
		w= gView.getLayoutParams().width;
		h=gView.getLayoutParams().height;
		left=w/2-m_diameter/2;
		right=w/2+m_diameter/2;
		top=h/2-m_diameter/2;
		bottom=h/2+m_diameter/2;
        //Enable the cache 
		gView.setDrawingCacheEnabled(true); 
        //Set the layout manually
		gView.layout(0, 0,w, h); 
        //Set the quality to high 
		gView.setDrawingCacheQuality(View.DRAWING_CACHE_QUALITY_HIGH); 
        //Build the cache, get the bitmap and close the cache 
		gView.buildDrawingCache(true); 
		//Bitmap bi=gView.toBitmap();
		
		return Bitmap.createBitmap(gView.getDrawingCache(),left,top,m_diameter,m_diameter);	
	}
	
	public double GetMaxValue() {
		double d=m_oil;
		if(m_gas>d) d=m_gas;
		if(m_coil>d) d=m_coil;
		return d;
	}
	
}
