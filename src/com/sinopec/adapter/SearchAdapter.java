package com.sinopec.adapter;

import java.util.ArrayList;
import java.util.HashMap;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.TextView;

import com.esri.core.geometry.Geometry;
import com.esri.core.map.Graphic;
import com.esri.core.tasks.ags.find.FindResult;
import com.esri.core.tasks.ags.geocode.LocatorGeocodeResult;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.sinopec.activity.R;
import com.sinopec.application.SinoApplication;

/**
 * 查询结果的adapter
 */
public class SearchAdapter extends BaseAdapter {
	private ArrayList<HashMap<String, Object>> mList;
	private Context mContext;

	public SearchAdapter(Context context, ArrayList<HashMap<String, Object>> list) {
		this.mContext = context;
		this.mList = list;
	}

	@Override
	public int getCount() {
		return mList.size();
	}

	@Override
	public HashMap<String, Object> getItem(int index) {
		return mList.get(index);
	}

	@Override
	public long getItemId(int arg0) {
		return 0;
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		ViewHolder holder = new ViewHolder();
		if (convertView == null) {
			convertView = LayoutInflater.from(mContext).inflate(
					R.layout.search_listview_item, null);

			holder.mName = (TextView) convertView
					.findViewById(R.id.menu_item_name);

			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}

		HashMap<String, Object> map = mList.get(position);
		FindResult result = (FindResult) map.get("FindResult");
		IdentifyResult identifyResult = (IdentifyResult) map.get("IdentifyResult");
		String layerName = identifyResult.getLayerName();
		String name = "";
		if(result == null){
			if("井".equals(layerName)){
				name = SinoApplication.getIdentifyResultNameByType(identifyResult, 3);
			}else{
				name = SinoApplication.getIdentifyResultName(identifyResult);
			}
		}else{
			name = result.getValue();
		}
//		Geometry geometry = result.getGeometry();
//		Map<String, Object> attributes = result.getAttributes();
//		LocatorGeocodeResult result = (LocatorGeocodeResult) map.get("LocatorGeocodeResult");
//		String name = result.getAddress();
		holder.mName.setText(name);
		return convertView;
	}

	public class ViewHolder {
		public TextView mName;
		// ImageView icon;
	}

}
