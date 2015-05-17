package com.sinopec.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.View;
import android.widget.LinearLayout;
import com.lenovo.nova.util.debug.mylog;
import com.sinopec.application.SinoApplication;
import org.achartengine.ChartFactory;
import org.achartengine.GraphicalView;
import org.achartengine.chart.AbstractChart;
import org.achartengine.chart.BarChart;
import org.achartengine.chart.PointStyle;
import org.achartengine.chartdemo.demo.chart.AbstractDemoChart;
import org.achartengine.renderer.SimpleSeriesRenderer;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Created by scenic on 2015/5/10.
 */
public class StaticsChart extends AbstractDemoChart {

    List<StatisticsEntity> data;
    public static final int FLAG_YD = 0;
    public static final int FLAG_QD = 1;
    int flag;
    private double XMin;
    private double XMax;
    private double Ymin;
    private double YMax;

    public StaticsChart(List<StatisticsEntity> aVoid,int flag) {
        this.data = aVoid;
        this.flag = flag;
    }

    public static class StatisticsEntity{
        int ND;

        /**
         * 累计油地质储量
         */
        double LJYDZCL;

        /**
         * 累计气地质储量
         */
        double LJQDZCL;

        String LJNXYDZCL;
        String BZ;
    }


    @Override
    public String getName() {
        return null;
    }

    @Override
    public String getDesc() {
        return null;
    }

    @Override
    public Intent execute(Context context) {
        return null;
    }

    public View getMyView(Context context) {
        List<double[]> values = new ArrayList<double[]>();
        double[] firstValue = new double[data.size()];
        String[] titles = new String[] { "累计油地质储量", "累计气地质储量" };//图例



        for (int i = 0; i < firstValue.length; i++) {
            switch (flag){
                case FLAG_YD:

                    titles = new String[] { "累计油地质储量"};//图例
                    firstValue[i] = data.get(i).LJYDZCL;

                    break;
                case FLAG_QD:
                    titles = new String[] { "累计气地质储量"};//图例
                    firstValue[i] = data.get(i).LJQDZCL;
                    break;
            }


        }

//        XMin = data.get(0).ND;
//        XMax = data.get(data.size() - 1).ND;


        XMin = 0;
        XMax = 30;

        Ymin = firstValue[0];
        YMax = firstValue[firstValue.length - 1];

        mylog.i("scenic","xMin " + XMin + " XMax " + XMax + " YMin " + Ymin + "  YMax " + YMax);

        values.add(firstValue);//第一种柱子的数值
//        values.add(secondValue);

//        values.add(new double[] { 5230, 7300, 9240, 10540, 7900, 9200, 12030, 11200, 9500, 10500,
//                11600, 13500 });//第二中柱子的数值

        int[] colors = new int[] { Color.BLUE/*, Color.CYAN*/ };//两种柱子的颜色
        XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);//调用AbstractDemoChart中的方法构建renderer.
        setChartSettings(renderer, "Data value", "Year", "Value", XMin,
                XMax, Ymin, YMax, Color.GRAY, Color.LTGRAY);//调用AbstractDemoChart中的方法设置renderer的一些属性.
        renderer.getSeriesRendererAt(0).setDisplayChartValues(true);//设置柱子上是否显示数量值
//        renderer.getSeriesRendererAt(1).setDisplayChartValues(true);//设置柱子上是否显示数量值
        renderer.setXLabels(12);//X轴的近似坐标数
        renderer.setYLabels(10);//Y轴的近似坐标数
        renderer.setXLabelsAlign(Paint.Align.LEFT);//刻度线与X轴坐标文字左侧对齐
        renderer.setYLabelsAlign(Paint.Align.LEFT);//Y轴与Y轴坐标文字左对齐
        renderer.setPanEnabled(true, false);//允许左右拖动,但不允许上下拖动.
        // renderer.setZoomEnabled(false);
        renderer.setZoomRate(1.1f);//放大的倍率
        renderer.setBarSpacing(0.5f);//柱子间宽度
        renderer.setAxisTitleTextSize(30);
        renderer.setAxisTitleTextSize(20);
        for (int i = 0; i < data.size(); i++) {
            renderer.addXTextLabel(i,data.get(0).ND+"");
        }

        Intent intent = ChartFactory.getBarChartIntent(context,
                buildBarDataset(titles, values),
                renderer,
                BarChart.Type.STACKED);//构建Intent, buildBarDataset是调用AbstractDemochart中的方法.

        Bundle extras = intent.getExtras();
        AbstractChart mChart = (AbstractChart) extras.getSerializable("chart");
        GraphicalView mView = new GraphicalView(context, mChart);

        return mView;
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

}
