package com.sinopec.activity;

import java.io.InputStream;
import java.io.StringReader;
import java.net.ConnectException;
import java.net.SocketTimeoutException;
import java.util.*;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.text.TextUtils;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.webkit.WebView;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.BaseAdapter;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.lenovo.nova.util.debug.mylog;
import com.lenovo.nova.util.network.NetworkHelper;
import com.sinopec.application.SinoApplication;
import com.sinopec.chart.PolygonLineChart3;
import com.sinopec.common.CommonData;
import com.sinopec.data.json.Constant;
import com.sinopec.data.json.FavoriteBean;
import com.sinopec.query.AsyncHttpQuery;
import com.sinopec.util.FavoriteDao;
import com.sinopec.util.JsonParse;
import com.sinopec.util.SimpleTableView;
import com.sinopec.view.MyExpandableListAdapter;
import org.apache.http.HttpEntity;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONObject;

public class SelectActivity extends Activity {
    /**
     * 动态解析对象属性左侧列表
     */
    private final int HandlerIDTypeProperty = 33;
    private final int IntroduceBasin = 3;
    private final int IntroduceOilGas = 4;
    private final int CountBasin = 5;
    private final int CountOilGas = 6;
    // private ExpandableListView expandableListView = null;
    private List<String> groups = new ArrayList<String>();
    private List<List<String>> childs = new ArrayList<List<String>>();
    private LinearLayout leftLayout;
    private ListView mListView;
    private TextView titleName;
    private TextView mTextViewTitle;
    /**
     * 属性 or 简介 or 统计
     */
    private String dataName;
    private MyExpandableListAdapter myExpanListAdapter;
    private DataAdapter adapter;
    private ImageButton mBtnBack;
    private ViewGroup mContentLayout;
    private ScrollView mContent;
    private AsyncHttpQuery asyncHttpQuery;
    private Context mContext;
    private CheckBox mCheckBoxStore;
    private Handler handler = new Handler() {

        public void handleMessage(android.os.Message msg) {
            switch (msg.what) {
                case 1:
                    dealJson((String) msg.obj);
                    break;
                case 2:
                    dealJson((String) msg.obj);
                    break;
                case HandlerIDTypeProperty:
                    dealJson((String) msg.obj);
                    break;

                case IntroduceBasin:
                case IntroduceOilGas:
                    dealJson4Introduce((String) msg.obj);
                    break;
                case CountBasin:
                case CountOilGas:
                    dealJson4Count((String) msg.obj);
                    break;
            }
        }
    };
    private String mTopicType;
    private List<HashMap<String, Object>> mDataList = new ArrayList<HashMap<String, Object>>();
    private String mID = "";
    private String mInitData = "";

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        requestWindowFeature(Window.FEATURE_NO_TITLE);
        this.mContext = this;
        setContentView(R.layout.select_layout);
        asyncHttpQuery = new AsyncHttpQuery(handler, this);
        mContentLayout = (ViewGroup) findViewById(R.id.content);
        mCheckBoxStore = (CheckBox) findViewById(R.id.checkbox_store);

        leftLayout = (LinearLayout) findViewById(R.id.leftLayout);
        mListView = (ListView) findViewById(R.id.expanList);
        titleName = (TextView) findViewById(R.id.titleName);
        mTextViewTitle = (TextView) findViewById(R.id.title);
        mContent = (ScrollView) findViewById(R.id.scrollview_content);

        mCheckBoxStore.setOnCheckedChangeListener(new OnCheckedChangeListener() {

            @Override
            public void onCheckedChanged(CompoundButton arg0, boolean isCheck) {
                // TODO 收藏的点击处理
                if (isCheck) {

                    if (SinoApplication.mLoginSuccess) {
//						用户已经登陆
                        Toast.makeText(mContext, getString(R.string.stored_ok),
                                Toast.LENGTH_SHORT).show();
                        String projectname = titleName.getText().toString().trim();
                        String userid = SinoApplication.mloginuserid;
//								 fb 测试
                        FavoriteBean fbean = new FavoriteBean(1, userid, projectname, userid);
                        FavoriteDao fdao = new FavoriteDao(SelectActivity.this);
                        fdao.save(fbean);
                    } else {
//						用户未登陆
                        Toast.makeText(mContext, getString(R.string.need_login),
                                Toast.LENGTH_SHORT).show();

                    }


                } else {
                    Toast.makeText(mContext, getString(R.string.stored_no),
                            Toast.LENGTH_SHORT).show();
                }
            }
        });
        mBtnBack = (ImageButton) findViewById(R.id.btn_login_back);
        mBtnBack.setOnClickListener(new OnClickListener() {

            @Override
            public void onClick(View arg0) {
                finish();
            }
        });
        // expandableListView = (ExpandableListView)
        // findViewById(R.id.expanList);
//		leftLayout = (LinearLayout) findViewById(R.id.leftLayout);
//		mListView = (ListView) findViewById(R.id.expanList);
//		titleName = (TextView) findViewById(R.id.titleName);
//		mTextViewTitle = (TextView) findViewById(R.id.title);
//		mContent = (ScrollView) findViewById(R.id.scrollview_content);

        mListView.setOnItemClickListener(new OnItemClickListener() {

            @Override
            public void onItemClick(AdapterView<?> parent, View view,
                                    int position, long id) {
                // for (int i = 0; i < parent.getCount(); i++) {
                // View v = parent.getChildAt(parent.getCount() - 1 - i);
                // if (position == i) {
                // v.setBackgroundColor(Color.RED);
                // } else {
                // v.setBackgroundColor(Color.TRANSPARENT);
                // }
                // }
                String groupName = (String) parent.getAdapter().getItem(
                        position);
                // Log.d("json", "groupName: " + groupName);
                showItemTable(groupName);
                adapter.setSelectItem(position);
                adapter.notifyDataSetInvalidated();
            }

        });

        getData();
//		showView();
    }

    private void getJson4Attribute(String id) {
        // String chenjitixi = "72057594037927935";
        String url = "";
        if (CommonData.TopicBasin.equals(mTopicType)) {
            url = Constant.urlAttributeBasin + id;

            asyncHttpQuery.execute(1, url);
        } else if (CommonData.TopicOilField.equals(mTopicType)
                || CommonData.TopicGasField.equals(mTopicType)) {
            url = Constant.urlAttributeOilGas + id;
            // url = Constant.urlAttributeOilGas + "201102001063";
            asyncHttpQuery.execute(2, url);
        }
        mylog.i("scenic", "url is  " + url + " mTopicType " + mTopicType + " id  " + id);
    }

    private void getJson4Introduce(String id) {
        // String chenjitixi = "72057594037927935";
        String url = "";
        if (CommonData.TopicBasin.equals(mTopicType)) {
            // url = Constant.urlIntroduceBasin + id;
            url = Constant.urlIntroduceBasin + "200700000001";
            asyncHttpQuery.execute(IntroduceBasin, url);
        } else if (CommonData.TopicOilField.equals(mTopicType)
                || CommonData.TopicGasField.equals(mTopicType)) {
            url = Constant.urlIntroduceOilGas + id;
            // url = Constant.urlAttributeOilGas + "201102001063";
            asyncHttpQuery.execute(IntroduceOilGas, url);
        }
    }

    private void getJson4Count(String id) {
        // String chenjitixi = "72057594037927935";
        String url = "";
        if (CommonData.TopicBasin.equals(mTopicType)) {
            // url = Constant.urlIntroduceBasin + id;
            url = Constant.urlCountBasin + "200700000001";
            asyncHttpQuery.execute(IntroduceBasin, url);
        } else if (CommonData.TopicOilField.equals(mTopicType)
                || CommonData.TopicGasField.equals(mTopicType)) {
            url = Constant.urlCountOilGas + id;
            // url = Constant.urlAttributeOilGas + "201102001063";
            asyncHttpQuery.execute(IntroduceOilGas, url);
        }
    }

    private void dealJson(String result) {
        mDataList.clear();
        // Log.d("json", "-------result: " + result);
        JsonParse jsonParse = new JsonParse();
        try {
            mDataList = jsonParse.parseItemsJson(new JsonReader(
                    new StringReader(result)));

        } catch (Exception e) {
            Log.e("json", "-dealJson---属性解析 error: " + e.toString());
        }

        ArrayList<String> list = new ArrayList<String>();
        list = showLeftMenuDynamic();
        initData(list);
        showItemTable(mInitData);
    }

    private ArrayList<String> showLeftMenuDynamic() {
        ArrayList<String> list = new ArrayList<String>();
        if (CommonData.TypeProperty.equals(dataName)) {
            list = initChildMenuData4PropertyDynamic();
        } else if (CommonData.TypeCount.equals(dataName)) {
            list = initChildMenuData4Count();
            showView();
            getJson4Count(mID);
        } else if (CommonData.TypeIntroduce.equals(dataName)) {
            list = initChildMenuData4IntroduceDynamic();
            getJson4Introduce(mID);
        }
        return list;
    }

    private void dealJson4Introduce(String result) {
        mDataList.clear();
        // Log.d("json", "-------result: " + result);
        JsonParse jsonParse = new JsonParse();
        try {
            mDataList = jsonParse.parseItemsJson(new JsonReader(
                    new StringReader(result)));

        } catch (Exception e) {
            Log.e("json", "-dealJson---属性解析 error: " + e.toString());
        }
        showItemTable(mInitData);
    }

    //TODO:
    private void dealJson4Count(String result) {
        mDataList.clear();
        // Log.d("json", "-------result: " + result);
        JsonParse jsonParse = new JsonParse();
        try {
            mDataList = jsonParse.parseItemsJson(new JsonReader(
                    new StringReader(result)));

        } catch (Exception e) {
            Log.e("json", "-dealJson---统计解析 error: " + e.toString());
        }
//		showItemTable(mInitData);
        showView();
    }

    private void showItemTable(String type) {
        if (CommonData.TypeProperty.equals(dataName)) {
            HashMap<String, Object> result = getItemData(type);
            if (result != null) {
                addBasePropertyTable(result.entrySet());
            } else {
                dealNoResult();
            }
        } else if (CommonData.TypeCount.equals(dataName)) {
        } else if (CommonData.TypeIntroduce.equals(dataName)) {
            if (mDataList == null) {
                dealNoResult();
            } else {
                getItemData4Introduce(type);
            }
        }
    }

    private void showView() {
        mContentLayout.removeAllViews();
//		polygonLineChart3 = new PolygonLineChart3();
//		new LinearLayout.LayoutParams(SinoApplication.screenWidth / 2, SinoApplication.screenHeight - 300, Gravity.CENTER);
        mContentLayout.addView(new PolygonLineChart3().getBarChartView(mContext), new LinearLayout.LayoutParams(SinoApplication.screenWidth / 2, SinoApplication.screenHeight - 300, Gravity.CENTER));
//		TextView tView = new TextView(this);
//		tView.setText("厚厚的方会计师的返回");
//		tView.setTextColor(Color.BLACK);
//		tView.setTextSize(25);
//		mContent.addView(tView);
    }

    private void dealNoResult() {
        mContent.removeAllViews();
        Toast.makeText(SelectActivity.this, getString(R.string.search_no_data),
                Toast.LENGTH_SHORT).show();
    }

    private HashMap<String, Object> getItemData(String key) {
        HashMap<String, Object> result = null;
        try {
            for (HashMap<String, Object> hashMap : mDataList) {

                for (Entry<String, Object> hashMaps : hashMap.entrySet()) {
                    // Log.d("json",
                    // "0000 getItemData parent key: "+hashMaps.getKey()+" value: "
                    // + hashMaps.getValue());
                    Log.d("json",
                            "0000 getItemData parent key: " + hashMaps.getKey()
                                    + " 传进的：" + key);
                    if (hashMaps.getValue() instanceof HashMap) {
                        if (hashMaps.getKey().equals(key)) {
                            result = (HashMap<String, Object>) hashMaps
                                    .getValue();
                            Log.d("json",
                                    "11111 getItemData parent key: "
                                            + hashMaps.getKey() + " value: "
                                            + hashMaps.getValue());
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("json", "----属性解析 error: " + e.toString());
        }

        return result;
    }

    private void getItemData4Introduce(String key) {
        try {
            for (HashMap<String, Object> hashMap : mDataList) {
                for (Entry<String, Object> hashMaps : hashMap.entrySet()) {
                    // Log.d("json","88888 key: " + hashMaps.getKey() + " 传进的："
                    // + SinoApplication.mMap4Introduce.get(key));
                    // addIntroduceTable(hashMap.entrySet());
                    for (Entry<String, Object> ent : hashMap.entrySet()) {
                        // stv1.AddRow(new String[] {
                        // SinoApplication.mNameMap4Introduce.get(ent.getKey()),
                        // dealValue(ent.getValue()) });
                        Log.d("json",
                                "88888 key: "
                                        + hashMaps.getKey()
                                        + " 传进的："
                                        + SinoApplication.mMap4Introduce
                                        .get(key));
                        if (SinoApplication.mMap4Introduce.get(key).equals(
                                ent.getKey())) {
                            if (TextUtils.isEmpty((String) ent.getValue())) {
                                dealNoResult();
                            } else {
                                addIntroduceContent((String) ent.getValue());
                            }
                            break;
                        }
                    }
                }
            }
        } catch (Exception e) {
            Log.e("json", "----getItemData4Introduce 错误 error: " + e.toString());
        }
    }

    //                        {
//                            ND: 1990,
//                                    LJYDZCL: 15.23,
//                                LJQDZCL: 532,
//                                LJNXYDZCL: null,
//                                BZ: null
//                        },

    private void getData() {
        Intent intent = getIntent();
        ArrayList<String> list = new ArrayList<String>();
        if (SinoApplication.identifyResult != null) {
            // 来自长按查询
            dataName = intent.getStringExtra("name");
            mTopicType = SinoApplication.identifyResult.getLayerName();
            Map<String, Object> attributes = SinoApplication.identifyResult
                    .getAttributes();
            String name = (String) attributes.get("OBJ_NAME_C");
            titleName.setText(name);
            mID = (String) SinoApplication.identifyResult.getAttributes().get(
                    "OBJ_ID");

        } else {
            if (intent != null) {
                dataName = intent.getStringExtra("name");
                mTopicType = intent.getStringExtra(CommonData.KeyTopicType);
                titleName.setText(intent
                        .getStringExtra(CommonData.KeyTopicName));
            }
            titleName.setText(SinoApplication.mTopicName);
        }

        if (TextUtils.isEmpty(mID)) {
            if (SinoApplication.findResult != null) {
                mID = (String) SinoApplication.findResult.getAttributes().get(
                        "OBJ_ID");
            }
            if (SinoApplication.graphic != null) {
                try {
                    double temp = (Double) SinoApplication.graphic.getAttributes().get(
                            "OBJ_ID");
                    java.text.DecimalFormat df = new java.text.DecimalFormat("#");
                    mID = df.format(temp);
                } catch (Exception e) {
                    mID = (String) SinoApplication.graphic.getAttributes().get(
                            "OBJ_ID");
                }
            }
        }
//		Log.d("data", "-------mType: " + mTopicType);
        Log.d("json", "-------mID: " + mID + "dataName " + dataName);

        if (CommonData.TypeProperty.equals(dataName)) {
//			list = initChildMenuData4Property();
//			list = initChildMenuData4PropertyDynamic();
            getJson4Attribute(mID);
        } else if (CommonData.TypeCount.equals(dataName)) {
            //统计
            list = initChildMenuData4Count();
//			showView();

            showStatistics(mID);

//			getJson4Count(mID);
        } else if (CommonData.TypeIntroduce.equals(dataName)) {
//            list = initChildMenuData4Introduce();
            getJson4Introduce(mID);

            new AsyncTask<Void, Void, List<String>>() {

                @Override
                protected List<String> doInBackground(Void... params) {
                    //200700000120
                    String url =
                            "http://10.225.14.203:8080/peprisapi/docInfoQuery.html?type=1&dzdxbm=200700000120";

                    try {
                        HttpEntity entity = NetworkHelper.newNetworkManager().connect(NetworkHelper.Method.GET, url, null, null).getEntity();
                        String json = EntityUtils.toString(entity);
                        JSONArray array = new JSONArray(json);
                        int jsonSize = array.length();
                        mylog.i("scenic", "JasonArray size " + jsonSize);
                        JSONObject object = array.getJSONObject(0);

                        String first = object.getString("HYQCX");
                        String two = object.getString("QYGZ");


                        //地理位置图
                        String item_3 = "NULL";
                        String item_4 = object.getString("PDGK");
                        String item_5 = object.getString("QBYYQC");
                        String item_6 = object.getString("KTQK");
                        String item_7 = "NULL";
                        if(first.contains("塔里木")){
                            String imageUrl = "file:///android_asset/png_diliweizhi.png";
                            item_3 = "<HTML><IMG src=\""+imageUrl+"\""+"width="+500+"height="+500+"/></HTML>";
                            item_7 = item_3;
                            mylog.i("scenic","html is " + item_3);
                        }
                        List<String> mValues = new ArrayList<String>();
                        mValues.add(first);
                        mValues.add(two);
                        mValues.add(item_3);
                        mValues.add(item_4);
                        mValues.add(item_5);
                        mValues.add(item_6);
                        mValues.add(item_7);
                        return mValues;
                    } catch (SocketTimeoutException e) {
                        e.printStackTrace();
                    } catch (ConnectException e) {
                        e.printStackTrace();
                    } catch (Exception e) {
                        e.printStackTrace();
                    }
                    return null;
                }

                @Override
                protected void onPostExecute(final List<String> aVoid) {
                    ArrayList<String> mData = new ArrayList<String>();
                    mData.add("含油气层系");
                    mData.add("区域构造");
                    mData.add("地理位置图");
                    mData.add("盆地概况");
                    mData.add("圈闭与油气藏");
                    mData.add("勘探情况");
                    mData.add("平面构造图");
                    initData(mData);
                    mListView.setOnItemClickListener(new OnItemClickListener() {
                        @Override
                        public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                            mContentLayout.removeAllViews();
                            WebView webView = new WebView(mContext);

                            String notice1 =
                                    "<html>" +
                                            "<head>" +
                                            "<meta http-equiv='Content-Type' content='text/html; charset=utf-8'/>" +
                                            "<body>" +
                                            aVoid.get(position) +
                                            "</body>" +
                                            "</html>";

//                            webView.loadData(notice1, "text/html", "GBK");
                            webView.loadDataWithBaseURL(null, notice1, "text/html","UTF-8", null);
                            mContentLayout.addView(webView,
                                    new LinearLayout.LayoutParams(SinoApplication.screenWidth / 2, SinoApplication.screenHeight - 300, Gravity.CENTER)
                            );
                        }
                    });
                }

            }.execute();


        }
//		initData(list);
    }

    private void showStatistics(String mID) {
//        200700000120
        final String url = "http://10.225.14.203:8080/peprisapi/statistical.html?dzdybm=" + mID;
        new AsyncTask<Void, Void, List<StaticsChart.StatisticsEntity>>() {

            @Override
            protected List<StaticsChart.StatisticsEntity> doInBackground(Void... params) {
                List<StaticsChart.StatisticsEntity> list = new ArrayList<StaticsChart.StatisticsEntity>();
                try {
                    HttpEntity entity = NetworkHelper.newNetworkManager().connect(NetworkHelper.Method.GET, url, null, null).getEntity();
                    String json = EntityUtils.toString(entity);

                    JSONArray array = new JSONArray(json);
                    for (int i = 0; i < array.length(); i++) {
                        JSONObject obj = array.getJSONObject(i);
                        StaticsChart.StatisticsEntity sEntity = new StaticsChart.StatisticsEntity();
                        sEntity.ND = obj.getInt("ND");
                        sEntity.LJYDZCL = obj.getDouble("LJYDZCL");
                        sEntity.LJQDZCL = obj.getDouble("LJQDZCL");
                        sEntity.BZ = obj.getString("BZ");
                        sEntity.LJNXYDZCL = obj.getString("LJNXYDZCL");
                        list.add(sEntity);

                    }

                    return list;
                } catch (SocketTimeoutException e) {
                    e.printStackTrace();
                } catch (ConnectException e) {
                    e.printStackTrace();
                } catch (Exception e) {
                    e.printStackTrace();
                }
                return null;
            }

            @Override
            protected void onPostExecute(final List<StaticsChart.StatisticsEntity> aVoid) {

//                    new StaticsChart().
                ArrayList<String> data = new ArrayList<String>();
                data.add("累计油地质储量");
                data.add("累计气地质储量");
                initData(data);
                mListView.setOnItemClickListener(new OnItemClickListener() {
                    @Override
                    public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                        mContentLayout.removeAllViews();
                        mContentLayout.addView(
                                new StaticsChart(aVoid, position).getMyView(mContext),
                                new LinearLayout.LayoutParams(SinoApplication.screenWidth / 2,
                                        SinoApplication.screenHeight - 300, Gravity.CENTER));
                    }
                });


            }
        }.execute();

    }

    private ArrayList<String> initChildMenuData4Property() {
        ArrayList<String> list = new ArrayList<String>();
        String[] titils = null;
        if (CommonData.TopicBasin.equals(mTopicType)) {
            titils = new String[]{"含油气盆地基础属性", "含油气盆地地层分层", " 含油气盆地勘探历程",
                    " 含油气盆地勘探成果", "含油气盆地整体资源", "含油气盆地分层资源", "含油气盆地分层地层属性",
                    " 含油气盆地烃源条件", "含油气盆地储集条件", " 含油气盆地盖层条件", "含油气盆地圈闭条件",
                    "含油气盆地保存条件", " 含油气盆地配置条件",};

        } else if (CommonData.TopicOilField.equals(mTopicType)
                || CommonData.TopicGasField.equals(mTopicType)) {
            titils = new String[]{"油气田基础属性", "油气田储量产量", "油气田源储盖条件",
                    "油气田原油性质", "油气田天然气性质", "油气田水性质"};
        } else if (CommonData.TopicOilGasMine.equals(mTopicType)) {
            titils = new String[]{"油气藏基础属性", "油气藏储量产量", "油气藏烃源条件", "油气藏储集条件",
                    "油气藏盖层条件", "油气藏原油性质", "油气藏天然气性质", "油气藏水性质",};
        } else {
            titils = new String[]{"没有结果"};
        }

        mInitData = titils[0];
        for (int i = 0; i < titils.length; i++) {
            list.add(titils[i]);
        }
        return list;

    }

    /**
     * 动态获取左侧菜单项
     *
     * @return
     */
    private ArrayList<String> initChildMenuData4PropertyDynamic() {
        ArrayList<String> list = new ArrayList<String>();
        if (CommonData.TopicBasin.equals(mTopicType)) {
            for (HashMap<String, Object> hashMap : mDataList) {
                for (Entry<String, Object> hashMaps : hashMap.entrySet()) {
                    Log.d("zcn", "888 获取行名称: " + hashMaps.getKey());
                    list.add(hashMaps.getKey());
                }
            }
        } else if (CommonData.TopicOilField.equals(mTopicType)
                || CommonData.TopicGasField.equals(mTopicType)) {
            for (HashMap<String, Object> hashMap : mDataList) {
                for (Entry<String, Object> hashMaps : hashMap.entrySet()) {
                    Log.d("zcn", "999 获取行名称: " + hashMaps.getKey());
                    list.add(hashMaps.getKey());
                }
            }
        } else if (CommonData.TopicOilGasMine.equals(mTopicType)) {
//			titils = new String[] { "油气藏基础属性", "油气藏储量产量", "油气藏烃源条件", "油气藏储集条件",
//					"油气藏盖层条件", "油气藏原油性质", "油气藏天然气性质", "油气藏水性质", };
        } else {
//			titils = new String[] { "没有结果" };
        }

        mInitData = list.get(0);
        return list;
    }

    private ArrayList<String> initChildMenuData4Count() {
        ArrayList<String> list = new ArrayList<String>();
        String[] titils = null;
        if (CommonData.TopicBasin.equals(mTopicType)) {
            titils = new String[]{"历年累计探井数", "历年累计探井进尺", " 历年累计二维地震条数",
                    "历年累计二维地震长度", "历年累计三维地震块数", "历年累计三维地震面积", "历年累计勘探投资",
                    "历年累计发现石油地质储量", "历年累计发现天然气地质储量", "历年累计发现凝析油地质储量"};

        } else if (CommonData.TopicOilField.equals(mTopicType)
                || CommonData.TopicGasField.equals(mTopicType)) {
            titils = new String[]{"历年累计发现石油地质储量", "历年累计发现石油可采储量",
                    "历年累计发现石油产量", "历年累计发现天然气地质储量", "历年累计发现天然气可采储量",
                    "历年累计发现天然气产量", "历年累计发现凝析油地质储量", "历年累计发现凝析油可采储量",
                    "历年累计发现凝析油产量"};
        } else if (CommonData.TopicOilGasMine.equals(mTopicType)) {
            titils = new String[]{"历年累计发现石油地质储量", "历年累计发现石油可采储量",
                    "历年累计发现石油产量", "历年累计发现天然气地质储量", "历年累计发现天然气可采储量",
                    "历年累计发现天然气产量", "历年累计发现凝析油地质储量", "历年累计发现凝析油可采储量",
                    "历年累计发现凝析油产量"};
        }

        mInitData = titils[0];
        for (int i = 0; i < titils.length; i++) {
            list.add(titils[i]);
        }
        return list;
    }

    private ArrayList<String> initChildMenuData4Introduce() {
        ArrayList<String> list = new ArrayList<String>();
        String[] titils = null;

        if (CommonData.TopicBasin.equals(mTopicType)) {
            titils = new String[]{"盆地概况", "区域构造", " 含油气层系", "圈闭与油气藏", "勘探情况",
                    "平面构造图", "年代划分表", "地理位置图"};

        } else if (CommonData.TopicOilField.equals(mTopicType)
                || CommonData.TopicGasField.equals(mTopicType)
                || CommonData.TopicOilGasMine.equals(mTopicType)) {
            titils = new String[]{"油气田概述", "构造特征", "沉积与储层特征", "流体性质", "储量情况",
                    "平面构造图", "剖面图", "综合柱状图", "地理位置图", "地层简表"};
        }

        mInitData = titils[0];
        for (int i = 0; i < titils.length; i++) {
            list.add(titils[i]);
        }

        // for (int i = 0; i < titils.length; i++) {
        // list.add(titils[i]);
        // }
        return list;
    }

    private ArrayList<String> initChildMenuData4IntroduceDynamic() {
        ArrayList<String> list = new ArrayList<String>();

        if (CommonData.TopicBasin.equals(mTopicType)) {
            for (HashMap<String, Object> hashMap : mDataList) {
                for (Entry<String, Object> hashMaps : hashMap.entrySet()) {
                    Log.d("zcn", "555 获取行名称: " + hashMaps.getKey());
                    list.add(hashMaps.getKey());
                }
            }
        } else if (CommonData.TopicOilField.equals(mTopicType)
                || CommonData.TopicGasField.equals(mTopicType)
                || CommonData.TopicOilGasMine.equals(mTopicType)) {
            for (HashMap<String, Object> hashMap : mDataList) {
                for (Entry<String, Object> hashMaps : hashMap.entrySet()) {
                    Log.d("zcn", "555 获取行名称: " + hashMaps.getKey());
                    list.add(hashMaps.getKey());
                }
            }
        }

        mInitData = list.get(0);
        return list;
    }

    private void initData(ArrayList<String> list) {
        mTextViewTitle.setText(dataName);
        adapter = new DataAdapter(this, list);
        mListView.setAdapter(adapter);
        adapter.setSelectItem(0);
    }

    private void addBasePropertyTable(Set<Entry<String, Object>> ents) {
        mContent.removeAllViews();
        SimpleTableView stv1 = new SimpleTableView(this);
        for (Entry<String, Object> ent : ents) {
            stv1.AddRow(new String[]{
                    SinoApplication.mNameMap.get(ent.getKey()),
                    dealValue(ent.getValue())});
        }

        // stv1.AddRow(new
        // Object[]{"1",BitmapFactory.decodeResource(getResources(),
        // R.drawable.ic_launcher)});
        // stv1.AddRow(new String[]{"12","1"});
        // stv1.AddRow(new String[]{"12222","1"});
        // stv1.m_LineColor=Color.RED;

        LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        stv1.setLayoutParams(lp);
        mContent.addView(stv1);
    }

    private void addIntroduceTable(Set<Entry<String, Object>> ents) {
        mContent.removeAllViews();
        SimpleTableView stv1 = new SimpleTableView(this);
        for (Entry<String, Object> ent : ents) {
            stv1.AddRow(new String[]{
                    SinoApplication.mNameMap4Introduce.get(ent.getKey()),
                    dealValue(ent.getValue())});
        }

        LayoutParams lp = new RelativeLayout.LayoutParams(
                LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
        stv1.setLayoutParams(lp);
        mContent.addView(stv1);
    }

    private void addIntroduceContent(String value) {
        mContent.removeAllViews();
        if (value.contains("http")) {
            value = "http://img3.imgtn.bdimg.com/it/u=2534132044,20433587&fm=23&gp=0.jpg";
            ImageView imageView = new ImageView(this);
            mContent.addView(imageView);
            new DownLoadImage(mContent).execute(value);
        } else {
            TextView tView = new TextView(this);
            tView.setText(value);
            tView.setTextColor(Color.BLACK);
            tView.setTextSize(25);
            mContent.addView(tView);
        }

        // LayoutParams lp = new RelativeLayout.LayoutParams(
        // LayoutParams.WRAP_CONTENT, LayoutParams.WRAP_CONTENT);
        // lp.addRule(RelativeLayout.CENTER_IN_PARENT, RelativeLayout.TRUE);
    }

    private String dealValue(Object object) {
        String value = "";
        if (object instanceof HashMap) {
            HashMap<String, Object> map = (HashMap<String, Object>) object;
            StringBuffer sb = new StringBuffer();
            for (Entry<String, Object> ent : map.entrySet()) {
                // keyList.add(ent.getKey());
                sb.append(ent.getKey() + ": " + (String) ent.getValue() + " , ");
            }
            value = sb.toString();
        } else {
            value = (String) object;
        }
        return value;
    }

    private void getFindResultData(StringBuilder sb) {
        Set<Entry<String, Object>> ents = SinoApplication.findResult
                .getAttributes().entrySet();
        for (Entry<String, Object> ent : ents) {
            // sb = new StringBuilder();
            sb.append(ent.getKey());
            sb.append(":  ");
            sb.append(ent.getValue() + "\n");
            // rightList.add(sb.toString());
        }
    }

    private void getIdentifyResultData(StringBuilder sb) {
        Set<Entry<String, Object>> ents = SinoApplication.identifyResult
                .getAttributes().entrySet();
        for (Entry<String, Object> ent : ents) {
            // sb = new StringBuilder();
            sb.append(ent.getKey());
            sb.append(":  ");
            sb.append(ent.getValue() + "\n");
            // rightList.add(sb.toString());
        }
    }

    public class DataAdapter extends BaseAdapter {
        private Context mContext;
        private List<String> mList;
        private int selectItem = -1;

        public DataAdapter(Context mContext, List<String> list) {
            this.mContext = mContext;
            this.mList = list;
        }

        @Override
        public int getCount() {

            return mList.size();
        }

        @Override
        public Object getItem(int position) {
            return mList.get(position);
        }

        @Override
        public long getItemId(int position) {
            return position;
        }

        @Override
        public View getView(int position, View convertView, ViewGroup parent) {
            TextView childTextView = null;
            if (convertView == null) {
                convertView = LayoutInflater.from(mContext).inflate(
                        R.layout.childs, null);
            }

            childTextView = (TextView) convertView
                    .findViewById(R.id.mytextview_childs);
            childTextView.setText(mList.get(position));
            if (position == selectItem) {
                convertView.setBackgroundResource(R.drawable.item_selected_bg);
            } else {
                convertView.setBackgroundResource(R.drawable.spinner_item);
            }
            return convertView;
        }

        public void setSelectItem(int selectItem) {
            this.selectItem = selectItem;
        }

    }

    private class DownLoadImage extends AsyncTask<String, Integer, Bitmap> {
        private ScrollView mScrollView;

        public DownLoadImage(ScrollView scrollView) {
            this.mScrollView = scrollView;
        }

        protected Bitmap doInBackground(String... urls) {
            Log.d("img", "异步加载图片开始！");
            String url = urls[0];// "http://ww3.sinaimg.cn/bmiddle/6e91531djw1e8l3c7wo7xj20f00qo755.jpg";
            System.out.println(url);
            Bitmap tmpBitmap = null;
            try {
                InputStream is = new java.net.URL(url).openStream();
                tmpBitmap = BitmapFactory.decodeStream(is);
                is.close();
            } catch (Exception e) {
                e.printStackTrace();
                Log.e("KK下载图片", e.getMessage());
            }
            return tmpBitmap;
        }

        @Override
        protected void onProgressUpdate(Integer... values) {
            super.onProgressUpdate(values);
        }

        protected void onPostExecute(Bitmap result) {
            mScrollView.removeAllViews();
            ImageView imageView = new ImageView(mContext);
            imageView.setImageBitmap(result);
            mScrollView.addView(imageView);
            Log.d("img", "异步加载图片完成！");
        }
    }

}
