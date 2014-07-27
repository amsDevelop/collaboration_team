package com.sinopec.view;

import java.util.List;

import com.sinopec.activity.R;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseExpandableListAdapter;
import android.widget.TextView;

public class MyExpandableListAdapter extends BaseExpandableListAdapter {

	private Context context;

	private List<String> groups;

	private List<List<String>> childs;

	public MyExpandableListAdapter(Context context, List<String> groups,
			List<List<String>> childs) {
		this.context = context;
		this.groups = groups;
		this.childs = childs;
	}

	public Object getChild(int arg0, int arg1) {
		return childs.get(arg0).get(arg1);
	}

	public long getChildId(int groupPosition, int childPosition) {
		return childPosition;
	}

	public View getChildView(int groupPosition, int childPosition,
			boolean isLastChild, View convertView, ViewGroup parent) {
		TextView childTextView = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.childs,
					null);
		}
		childTextView = (TextView) convertView
				.findViewById(R.id.mytextview_childs);
		childTextView.setText(childs.get(groupPosition).get(childPosition));
		return convertView;
	}

	public int getChildrenCount(int groupPosition) {
		return childs.get(groupPosition).size();
	}

	public Object getGroup(int groupPosition) {
		return groups.get(groupPosition);
	}

	public int getGroupCount() {
		return groups.size();
	}

	public long getGroupId(int groupPosition) {
		return groupPosition;
	}

	public View getGroupView(int groupPosition, boolean isExpanded,
			View convertView, ViewGroup parent) {
		TextView groupTextView = null;
		if (convertView == null) {
			convertView = LayoutInflater.from(context).inflate(R.layout.groups,
					null);
		}
		groupTextView = (TextView) convertView
				.findViewById(R.id.mytextview_groups);
		groupTextView.setText(groups.get(groupPosition));
		return convertView;
	}

	public boolean hasStableIds() {
		return false;
	}

	public boolean isChildSelectable(int groupPosition, int childPosition) {
		return true;
	}

}
