package com.sinopec.activity;

import java.util.ArrayList;
import java.util.List;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.ExpandableListView;
import android.widget.ExpandableListView.OnChildClickListener;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

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
		// TODO Auto-generated method stub
		super.onCreate(savedInstanceState);
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
				 Toast.makeText(SelectActivity.this, "I am " + position, Toast.LENGTH_SHORT).show();
			}
			
			
		});
		
		
		 Intent intent = getIntent();
		 if (intent != null) {
		    dataName = intent.getStringExtra("name");
		    titleName.setText(dataName);
		 }
		   initData();
		   expandableListView.setOnChildClickListener(new OnChildClickListener() {
			
			@Override
			public boolean onChildClick(ExpandableListView parent, View v,
					int groupPosition, int childPosition, long id) {
				rightList.removeAll(rightList);
				// TODO Auto-generated method stub
				Log.v("mandy", "groupPosition: " + groupPosition + " childPosition: " + childPosition);
				if (((String)titleName.getText()).contains("/")) {
					titleName.setText(((String)titleName.getText()).subSequence(0, ((String)titleName.getText()).indexOf("/")));
				}
				
				
			   String tileName = titleName.getText() + "/" + childs.get(groupPosition).get(childPosition);
			   titleName.setText(tileName);
			
				for (int i = 0; i < 20; i++) {
					
				    rightList.add(childs.get(groupPosition).get(childPosition) + "(" + i + ")");
				}
				
				adapter.notifyDataSetChanged();
				
				
				return false;
			}
		});   
		   

	}

	private void initData() {

		groups.add(dataName);

		List<String> friends = new ArrayList<String>();
		friends.add(dataName + "1");
		friends.add(dataName + "2");
		friends.add(dataName + "3");
		childs.add(friends);
		
	  myExpanListAdapter = new MyExpandableListAdapter(this, groups, childs);
	  expandableListView.setAdapter(myExpanListAdapter);  
	  
		   


	}

}
