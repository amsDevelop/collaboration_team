package com.sinopec.activity;

import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.Paint;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.ListView;
import com.lenovo.nova.util.adapter.SimpleAdapter;
import com.lenovo.nova.util.debug.mylog;
import com.lenovo.nova.util.io.StreamUtil;
import com.lenovo.nova.widget.dialog.BaseDialogFragment;
import org.achartengine.ChartFactory;
import org.achartengine.chart.BarChart;
import org.achartengine.chartdemo.demo.chart.AbstractDemoChart;
import org.achartengine.model.XYMultipleSeriesDataset;
import org.achartengine.renderer.XYMultipleSeriesRenderer;

import java.io.IOException;
import java.io.InputStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

/**
 * Created by scenic on 2015/5/31.
 */
public class StaticsChina extends BaseDialogFragment {

    private static final String TAG = "StaticsChina";

    private List<String> mTitle = new ArrayList<String>();
    private ListView listView;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public void onActivityCreated(Bundle savedInstanceState) {
        super.onActivityCreated(savedInstanceState);

        try {
            InputStream in = getActivity().getAssets().open("statistics_china_title");
            StreamUtil.streamToString(in, new StreamUtil.OnReadStreamHelper() {
                @Override
                public boolean isTerminal() {
                    return false;
                }

                @Override
                public void onReaderLine(String line) {
                    mylog.i(TAG, "line " + line);
                    mTitle.add(line);

                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }

        SimpleAdapter<String> adapter = new SimpleAdapter<String>(getActivity(), mTitle);
        listView.setAdapter(adapter);
        listView.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                Intent intent = new MySignChart(position).execute(getActivity());
                startActivity(intent);
            }
        });

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container, Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.statistics_layout_list, null);
        listView = (ListView) view.findViewById(R.id.id_list);
        return view;
//                super.onCreateView(inflater, container, savedInstanceState);
    }


    class MySignChart extends AbstractDemoChart {

        int number;

        List<String> xValue = new ArrayList<String>();
        List<Double> y1Value = new ArrayList<Double>();
        List<Double> y2Value = new ArrayList<Double>();
        public MySignChart(int position) {
            this.number = position;

            try {
                InputStream in = getActivity().getAssets().
                        open("statistics_china/data_" + position);
                StreamUtil.streamToString(in, new StreamUtil.OnReadStreamHelper() {
                    @Override
                    public boolean isTerminal() {
                        return false;
                    }

                    @Override
                    public void onReaderLine(String line) {
                        mylog.i(TAG,"line " +line );
                        String[] temp = line.split(":");
                        xValue.add(temp[0].trim());
                        y1Value.add(Double.parseDouble(temp[1].trim()));

                        try {
                            y2Value.add(Double.parseDouble(temp[2].trim()));
                        } catch (Exception e) {

                        }
                    }
                });
            } catch (IOException e) {

            }

        }

        @Override
        public String getName() {
            return mTitle.get(number);
        }

        @Override
        public String getDesc() {
            return null;
        }

        @Override
        public Intent execute(Context context) {
            XYMultipleSeriesRenderer renderer = getRender();
            XYMultipleSeriesDataset dataset = getDataSet(renderer);

            return ChartFactory.getBarChartIntent(context,
                    dataset, renderer,
                    BarChart.Type.STACKED);//构建Intent, buildBarDataset是调用AbstractDemochart中的方法.

        }

        private XYMultipleSeriesRenderer getRender() {
            int[] colors = new int[]{Color.BLUE};//两种柱子的颜色

            if(y2Value.size() != 0){
                colors = new int[]{Color.BLUE,Color.CYAN};
            }

            XYMultipleSeriesRenderer renderer = buildBarRenderer(colors);//调用AbstractDemoChart中的方法构建renderer.

            double maxValue = getMaxValue();

            setChartSettings(renderer, "Monthly sales in the last 2 years",
                    "Month", "Units sold",
                    0.5,
                    12.5,
                    0, maxValue,
                    Color.GRAY, Color.LTGRAY);//调用AbstractDemoChart中的方法设置renderer的一些属性.
            renderer.getSeriesRendererAt(0).setDisplayChartValues(true);//设置柱子上是否显示数量值

            renderer.setXLabels(12);//X轴的近似坐标数
            renderer.setYLabels(5);//Y轴的近似坐标数
            renderer.setXLabelsAlign(Paint.Align.LEFT);//刻度线与X轴坐标文字左侧对齐
            renderer.setYLabelsAlign(Paint.Align.LEFT);//Y轴与Y轴坐标文字左对齐
            renderer.setPanEnabled(true, false);//允许左右拖动,但不允许上下拖动.
            // renderer.setZoomEnabled(false);
            renderer.setZoomRate(1.1f);//放大的倍率
            renderer.setBarSpacing(0.5f);//柱子间宽度
            return renderer;
        }

        private XYMultipleSeriesDataset getDataSet(XYMultipleSeriesRenderer renderer) {
            String[] titles = new String[]{"图1"};//图例
            if(y2Value.size() != 0){
                titles = new String[]{"图1","图2"};//图例
            }
            List<double[]> values = new ArrayList<double[]>();

            {
                double[] newData = new double[y1Value.size()];
                for (int i = 0; i < newData.length; i++) {
                    newData[i] = y1Value.get(i);
                }
                values.add(newData);//第一种柱子的数值
            }

            {
                if(y2Value.size()!= 0){
                    double[] newData = new double[y2Value.size()];
                    for (int i = 0; i < newData.length; i++) {
                        newData[i] = y2Value.get(i);
                    }
                    values.add(newData);//第一种柱子的数值
                }
            }



            for (int i = 0; i < xValue.size(); i++) {
                renderer.addXTextLabel(i, xValue.get(i));
            }

            mylog.i(TAG,"xValue " + xValue);

            return buildBarDataset(titles, values);
        }

        public double getMaxValue() {
            List<Double> temp = new ArrayList<Double>(y1Value);
            Collections.sort(temp);
            mylog.i(TAG,temp +"");
            double max1 = temp.get(temp.size() - 1);


            temp = new ArrayList<Double>(y2Value);
            Collections.sort(temp);
            mylog.i(TAG,temp +"");
            double max2 = 0;
            try {
                max2 = temp.get(temp.size() - 1);
            } catch (Exception e) {

            }

            return Math.max(max1,max2);
        }
    }


}
