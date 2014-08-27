package com.sinopec.activity;

import java.io.IOException;
import java.io.StringReader;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;
import java.util.Set;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.os.Handler;
import android.util.JsonReader;
import android.util.Log;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.ImageButton;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.RelativeLayout;
import android.widget.RelativeLayout.LayoutParams;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinopec.application.SinoApplication;
import com.sinopec.common.CommonData;
import com.sinopec.data.json.Constant;
import com.sinopec.query.AsyncHttpQuery;
import com.sinopec.util.JsonParse;
import com.sinopec.util.SimpleTableView;
import com.sinopec.view.MyExpandableListAdapter;
import com.sinopec.view.MyListAdapter;

public class SelectActivity extends Activity {
	private ExpandableListView expandableListView = null;
	private List<String> groups = new ArrayList<String>();
	private List<List<String>> childs = new ArrayList<List<String>>();
	private LinearLayout leftLayout;
	private ListView list;
	private TextView titleName;
	/**
	 * 属性 or 简介 or 统计
	 */
	private String dataName;
	private MyExpandableListAdapter myExpanListAdapter;
	private MyListAdapter adapter;
	private List<String> rightList = new ArrayList<String>();
	private ImageButton mBtnBack;
	private ViewGroup mContentLayout;
	private ScrollView mContent;
	private AsyncHttpQuery asyncHttpQuery;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_layout);
		asyncHttpQuery = new AsyncHttpQuery(handler, this);
		mContentLayout = (ViewGroup) findViewById(R.id.content);
		mBtnBack = (ImageButton) findViewById(R.id.btn_login_back);
		mBtnBack.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		expandableListView = (ExpandableListView) findViewById(R.id.expanList);
		leftLayout = (LinearLayout) findViewById(R.id.leftLayout);
		list = (ListView) findViewById(R.id.listView);
		titleName = (TextView) findViewById(R.id.titleName);
		mContent = (ScrollView) findViewById(R.id.scrollview_content);
		adapter = new MyListAdapter(this, rightList);
		list.setAdapter(adapter);

		list.setOnItemClickListener(new OnItemClickListener() {

			@Override
			public void onItemClick(AdapterView<?> parent, View view,
					int position, long id) {
				Toast.makeText(SelectActivity.this, "文件预览正在研发中，请稍后试用！",
						Toast.LENGTH_SHORT).show();
			}

		});

		getData();
		expandableListView.expandGroup(0);
		for (int i = 0; i < 20; i++) {

			rightList.add(childs.get(0).get(0) + "(" + i + ")");
		}
		expandableListView.setOnChildClickListener(new OnChildClickListener() {

			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				rightList.removeAll(rightList);
//				Log.v("mandy", "groupPosition: " + groupPosition
//						+ " childPosition: " + childPosition);
				if (((String) titleName.getText()).contains("/")) {
					titleName.setText(((String) titleName.getText())
							.subSequence(0,
									((String) titleName.getText()).indexOf("/")));
				}

				String tileName = titleName.getText() + "/"
						+ childs.get(groupPosition).get(childPosition);
//				titleName.setText(tileName);
				String groupName = childs.get(groupPosition).get(childPosition);
				Log.v("map", "groupPosition: " + groupName);
				if(groupName.contains("基础属性")){
					StringBuilder sb = new StringBuilder();
					if(SinoApplication.findResult != null){
//						getFindResultData(sb);
						addBasePropertyTable(SinoApplication.findResult.getAttributes().entrySet());
					}else if(SinoApplication.identifyResult != null){
						addBasePropertyTable(SinoApplication.identifyResult.getAttributes().entrySet());
//						getIdentifyResultData(sb);
//						showBaseProperty(sb.toString());
					}
					
				}else{
					for (int i = 0; i < 20; i++) {
						rightList.add(childs.get(groupPosition).get(childPosition)
								+ "(" + i + ")");
					}
				}


				adapter.notifyDataSetChanged();

				return false;
			}
		});
		
	}
	
	private Handler handler = new Handler() {

		public void handleMessage(android.os.Message msg) {
			StringBuilder builder;
			switch (msg.what) {
			case 1:
				dealJson((String)msg.obj);
				break;
			case 2:
				dealJson((String)msg.obj);
				break;
			}
		}
	};

	private String mTopicType;
	private void getJson(String id) {
//		String chenjitixi = "72057594037927935";
		String url = Constant.urlAttributeOilGas + id;
		if (CommonData.TopicBasin.equals(mTopicType)) {
			url = Constant.urlAttributeBasin + id;
			asyncHttpQuery.execute(1, url);
		} else if (CommonData.TopicOilField.equals(mTopicType) ||
				CommonData.TopicGasField.equals(mTopicType)) {
//			url = Constant.urlAttributeOilGas + id;
			url = Constant.urlAttributeOilGas + "201102001063";
			asyncHttpQuery.execute(2, url);
		}
	}
	
	private void dealJson(String result){
//		Log.d("json", "-------result: " + result);
		JsonParse jsonParse = new JsonParse();
		try {
			List<HashMap<String, Object>> list = jsonParse
					.parseItemsJson(new JsonReader(new StringReader(result)));
			Log.d("json", "共有多少条数据: " + list.size());

			for (HashMap<String, Object> hashMap : list) {

				for (Entry<String, Object> hashMaps : hashMap
						.entrySet()) {

					Log.d("json", "parent key and value: " + hashMaps.getKey()
							+ ": " + hashMaps.getValue());
					
					if (hashMaps instanceof HashMap) {

					for (Map.Entry<String, Object> hashMap3 : ((HashMap<String, Object>) hashMaps
							.getValue()).entrySet()) {

						Log.d("json",
								"child key and value: " + hashMap3.getKey()
										+ ": " + hashMap3.getValue());

					}
					} else { //否则就是list
						
//						for (int i = 0; i < (ArrayList)hashMaps.size(); i++) {
//							
//						}
						
					}

				}
				//
			}
		} catch (Exception e) {
			Log.e("json", "----属性解析 error: "+e.toString());
		}
//		  List<HashMap<String, HashMap<String, Object>>> list = jsonParse.parseItemsJson(new JsonReader(new StringReader(result)));
//		  HashMap<String, HashMap<String, Object>> map = jsonParse.parseItemJson(new JsonReader(new StringReader(result)));
//		  Log.d("json", "key and value: " + list.size());
//		  for (int i = 0; i < list.size(); i++) {
//			  HashMap<String, HashMap<String, Object>> map = list.get(i); 
//		  }
		  
//		  String item = "油气田基础属性";
//		  if("油气田基础属性".equals(item)){
//			  
//		  }
//		  Set<Entry<String, HashMap<String, Object>>> entrySet = map.entrySet();
//		  for (Entry<String, HashMap<String, Object>> entry : entrySet) {
//			  Log.d("json", "-------result: " + entry.getKey());
//		  }
//				Log.d("data",
//						"模糊查询  key: " + ent.getKey() + "  val: " + ent.getValue());
//			}
//		  for (HashMap<String, HashMap<String, String>> hashMap : list) {
//			  
//			  for (Map.Entry<String, HashMap<String, String>> hashMaps : hashMap.entrySet()) {
//				     Log.d("json", "key and value: " + hashMaps.getKey() + ": " + hashMaps.getValue());
//				    for (Map.Entry<String, String>  hashMap3 : hashMaps.getValue().entrySet()) {
//				    	 Log.d("json", "other key and value: " + hashMap3.getKey() + ": " + hashMap3.getValue());
//					}
//			}
//		}
	}

	private String mID = "";
	private void getData() {
		Intent intent = getIntent();
		ArrayList<String> list = new ArrayList<String>();
		if(SinoApplication.identifyResult != null){
			//来自长按查询
			dataName = intent.getStringExtra("name");
			mTopicType = SinoApplication.identifyResult.getLayerName();
			Map<String, Object> attributes = SinoApplication.identifyResult.getAttributes();
			String name = (String) attributes.get("OBJ_NAME_C");
			titleName.setText(name);
			mID = (String) SinoApplication.identifyResult.getAttributes().get("OBJ_ID");

			Log.d("json", "-------mID: " + mID);
		}else{
			if (intent != null) {
				dataName = intent.getStringExtra("name");
				mTopicType = intent.getStringExtra(CommonData.KeyTopicType);
				titleName.setText(intent.getStringExtra(CommonData.KeyTopicName));
			}	
		}
		Log.d("data", "-------mType: " + mTopicType);
		if (CommonData.TypeProperty.equals(dataName)) {
			list = initChildMenuData4Property();
		} else if (CommonData.TypeCount.equals(dataName)) {
			list = initChildMenuData4Count();
		} else if (CommonData.TypeIntroduce.equals(dataName)) {
			list = initChildMenuData4Introduce();
		}
		initData(list);
		getJson(mID);
	}
	
	private ArrayList<String> initChildMenuData4Property() {
		ArrayList<String> list = new ArrayList<String>();
		String[] titils = null;
		if (CommonData.TopicBasin.equals(mTopicType)) {
			titils = new String[] { "含油气盆地基础属性", "含油气盆地地层分层", " 含油气盆地勘探历程",
					" 含油气盆地勘探成果", "含油气盆地整体资源", "含油气盆地分层资源", "含油气盆地分层地层属性",
					" 含油气盆地烃源条件", "含油气盆地储集条件", " 含油气盆地盖层条件", "含油气盆地圈闭条件",
					"含油气盆地保存条件", " 含油气盆地配置条件", };

		} else if (CommonData.TopicOilField.equals(mTopicType) ||
				CommonData.TopicGasField.equals(mTopicType)) {
			titils = new String[] { "油气田基础属性", "油气田储量产量", "油气田源储盖条件",
					"油气田原油性质", "油气田天然气性质", "油气田水性质"  };
		} else if (CommonData.TopicOilGasMine.equals(mTopicType)) {
			titils = new String[] { "油气藏基础属性", "油气藏储量产量", "油气藏烃源条件",
					"油气藏储集条件", "油气藏盖层条件", "油气藏原油性质", "油气藏天然气性质",
					"油气藏水性质", };
		}else{
			titils = new String[]{"没有结果"};
		}
		for (int i = 0; i < titils.length; i++) {
			list.add(titils[i]);
		}
		return list;
		
	}
	private ArrayList<String> initChildMenuData4Count() {
		ArrayList<String> list = new ArrayList<String>();
		String[] titils = null;
		if (CommonData.TopicBasin.equals(mTopicType)) {
			titils = new String[] { "历年累计探井数", "历年累计探井进尺", " 历年累计二维地震条数",
					"历年累计二维地震长度", "历年累计三维地震块数", "历年累计三维地震面积", "历年累计勘探投资",
					"历年累计发现石油地质储量", "历年累计发现天然气地质储量", "历年累计发现凝析油地质储量"};
			
		} else if (CommonData.TopicOilField.equals(mTopicType) ||
				CommonData.TopicGasField.equals(mTopicType)) {
			titils = new String[] { "历年累计发现石油地质储量", "历年累计发现石油可采储量", "历年累计发现石油产量",
					"历年累计发现天然气地质储量", "历年累计发现天然气可采储量", "历年累计发现天然气产量",
					"历年累计发现凝析油地质储量", "历年累计发现凝析油可采储量", "历年累计发现凝析油产量"};
		} else if (CommonData.TopicOilGasMine.equals(mTopicType)) {
			titils = new String[] { "历年累计发现石油地质储量", "历年累计发现石油可采储量", "历年累计发现石油产量",
					"历年累计发现天然气地质储量", "历年累计发现天然气可采储量", "历年累计发现天然气产量", "历年累计发现凝析油地质储量",
					"历年累计发现凝析油可采储量", "历年累计发现凝析油产量"};
		}
		for (int i = 0; i < titils.length; i++) {
			list.add(titils[i]);
		}
		return list;
	}
	
	private ArrayList<String> initChildMenuData4Introduce() {
		ArrayList<String> list = new ArrayList<String>();
		String[] titils = null;

		if (CommonData.TopicBasin.equals(mTopicType)) {
			titils = new String[] { "盆地概况", "区域构造", " 含油气层系",
					"圈闭与油气藏", "勘探情况", "平面构造图", "年代划分表",
					"地理位置图"};
			
		} else if (CommonData.TopicOilField.equals(mTopicType) ||
				CommonData.TopicGasField.equals(mTopicType) || CommonData.TopicOilGasMine.equals(mTopicType)) {
			titils = new String[] { "油气田概述", "构造特征", "沉积与储层特征",
					"流体性质", "储量情况", "平面构造图",
					"剖面图", "综合柱状图", "地理位置图", "地层简表"};
		} 
		for (int i = 0; i < titils.length; i++) {
			list.add(titils[i]);
		}
		
		for (int i = 0; i < titils.length; i++) {
			list.add(titils[i]);
		}
		return list;
	}

	private void initData(ArrayList<String> list) {

		groups.add(dataName);

		childs.add(list);

		myExpanListAdapter = new MyExpandableListAdapter(this, groups, childs);
		expandableListView.setAdapter(myExpanListAdapter);

	}
	
	private TextView mTVBaseProperty;
	private void showBaseProperty(String content) {
		mContentLayout.removeAllViews();
		if(mTVBaseProperty == null){
			mTVBaseProperty = new TextView(this);
		}
		mTVBaseProperty.setText(content);
		mTVBaseProperty.setGravity(Gravity.CENTER);
		mContentLayout.addView(mTVBaseProperty);
		
	}
	
	private void addBasePropertyTable(Set<Entry<String, Object>> ents) {
		SimpleTableView stv1=new SimpleTableView(this);
		ArrayList<String> keyList = new ArrayList<String>();
		ArrayList<String> valList = new ArrayList<String>();
		for (Entry<String, Object> ent : ents) {
			keyList.add(ent.getKey());
			valList.add((String) ent.getValue());
			stv1.AddRow(new String[]{ent.getKey(),(String) ent.getValue()});
		}
		
//		stv1.AddRow(new Object[]{"1",BitmapFactory.decodeResource(getResources(), R.drawable.ic_launcher)});
//		stv1.AddRow(new String[]{"12","1"});
//		stv1.AddRow(new String[]{"12222","1"});
		//stv1.m_LineColor=Color.RED;
		
		LayoutParams lp= new RelativeLayout.LayoutParams(LayoutParams.WRAP_CONTENT,LayoutParams.WRAP_CONTENT); 
		lp.addRule(RelativeLayout.CENTER_IN_PARENT,RelativeLayout.TRUE);		
		stv1.setLayoutParams(lp);
		mContent.addView(stv1);
//		mContentLayout.addView(mContent);
//		mContentLayout.addView(stv1);
	}
	
	private void getFindResultData(StringBuilder sb) {
		Set<Entry<String, Object>> ents = SinoApplication.findResult.getAttributes().entrySet();
		for (Entry<String, Object> ent : ents) {
//			sb = new StringBuilder();
			sb.append(ent.getKey());
			sb.append(":  ");
			sb.append(ent.getValue() + "\n");
//			rightList.add(sb.toString());
		}
	}
	
	private void getIdentifyResultData(StringBuilder sb) {
		Set<Entry<String, Object>> ents = SinoApplication.identifyResult.getAttributes().entrySet();
		for (Entry<String, Object> ent : ents) {
//			sb = new StringBuilder();
			sb.append(ent.getKey());
			sb.append(":  ");
			sb.append(ent.getValue() + "\n");
//			rightList.add(sb.toString());
		}
	}

}
