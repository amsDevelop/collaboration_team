package com.sinopec.activity;

import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.Map.Entry;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.view.Window;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.sinopec.application.SinoApplication;
import com.sinopec.common.CommonData;
import com.sinopec.view.MyExpandableListAdapter;
import com.sinopec.view.MyListAdapter;

public class SelectActivity extends Activity {
	private ExpandableListView expandableListView = null;
	private List<String> groups = new ArrayList<String>();
	private List<List<String>> childs = new ArrayList<List<String>>();
	private LinearLayout leftLayout;
	private ListView list;
	private TextView titleName;
	private String dataName;
	private MyExpandableListAdapter myExpanListAdapter;
	private MyListAdapter adapter;
	private List<String> rightList = new ArrayList<String>();

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.select_layout);
		expandableListView = (ExpandableListView) findViewById(R.id.expanList);
		leftLayout = (LinearLayout) findViewById(R.id.leftLayout);
		list = (ListView) findViewById(R.id.listView);
		titleName = (TextView) findViewById(R.id.titleName);

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
					Set<Entry<String, Object>> ents = SinoApplication.findResult.getAttributes().entrySet();
					StringBuilder sb = null;
					for (Entry<String, Object> ent : ents) {
						sb = new StringBuilder();
						sb.append(ent.getKey());
						sb.append(":  ");
						sb.append(ent.getValue());
						rightList.add(sb.toString());
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

	private String mType;

	private void getData() {
		Intent intent = getIntent();
		mType = intent.getStringExtra(CommonData.KeyTopicType);
		Log.d("map", "-------mType: " + mType);
		String[] titils = null;
		if (CommonData.TopicBasin.equals(mType)) {
			titils = new String[] { "含油气盆地基础属性", "含油气盆地地层分层", " 含油气盆地勘探历程",
					" 含油气盆地勘探成果", "含油气盆地整体资源", "含油气盆地分层资源", "含油气盆地分层地层属性",
					" 含油气盆地烃源条件", "含油气盆地储集条件", " 含油气盆地盖层条件", "含油气盆地圈闭条件",
					"含油气盆地保存条件", " 含油气盆地配置条件", };

		} else if (CommonData.TopicOilGasField.equals(mType)) {
			titils = new String[] { "油气田基础属性", "油气田储量产量", "油气田源储盖条件",
					"油气田原油性质", "油气田天然气性质", "油气田水性质"  };
		} else if (CommonData.TopicOilGasMine.equals(mType)) {
			titils = new String[] { "油气藏基础属性", "油气藏储量产量", "油气藏烃源条件",
					"油气藏储集条件", "油气藏盖层条件", "油气藏原油性质", "油气藏天然气性质",
					"油气藏水性质", };
		}
		
		ArrayList<String> list = new ArrayList<String>();
		for (int i = 0; i < titils.length; i++) {
			list.add(titils[i]);
		}
		initData(list);
		if (intent != null) {
			dataName = intent.getStringExtra("name");
			titleName.setText(dataName);
		}
	}

	private void initData(ArrayList<String> list) {

		groups.add(dataName);

		childs.add(list);

		myExpanListAdapter = new MyExpandableListAdapter(this, groups, childs);
		expandableListView.setAdapter(myExpanListAdapter);

	}

}
