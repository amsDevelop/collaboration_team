package com.sinopec.activity;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.View.OnTouchListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.Layer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.sinopec.application.SinoApplication;
import com.sinopec.common.OilGasData;

@SuppressLint("NewApi")
public class LayerDialog extends DialogFragment implements OnClickListener {
	int resID = R.layout.layout_layer;
	Button mBtn1 = null;
	Button mBtn2 = null;
	Button mBtn3 = null;
	private ViewGroup mContaner;
	private ListView mListView;
	private TextView mCover;
	private MapView mapView;
	private ArcGISTiledMapServiceLayer mapServiceLayer;
	private GraphicsLayer drawLayer;

//	private List<ArcGISLayerInfo> layerInfos = new ArrayList<ArcGISLayerInfo>();
	private MyAdapter mOilGasAdapter;
	
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(resID, null);
		layerSatellite = mapView.getLayerByURL(SinoApplication.imageUrl);
		layerGeographic = mapView.getLayerByURL(SinoApplication.genUrl);
//		layerName = new String[6]; 
		mBtn1 = (Button) view.findViewById(R.id.id_btn_layer_1);
		mBtn2 = (Button) view.findViewById(R.id.id_btn_layer_2);
		mBtn3 = (Button) view.findViewById(R.id.id_btn_layer_3);
		mCover = (TextView) view.findViewById(R.id.layer_lstview_cover);
		mCover.setOnTouchListener(new OnTouchListener() {
			
			@Override
			public boolean onTouch(View arg0, MotionEvent arg1) {
				return true;
			}
		});
		view.findViewById(R.id.id_btn_operator_1).setOnClickListener(this);
		view.findViewById(R.id.id_btn_operator_2).setOnClickListener(this);
		mBtn3 = (Button) view.findViewById(R.id.id_btn_layer_3);

		mContaner = (ViewGroup) view
				.findViewById(R.id.id_llaout_layer_container);
//		mContaner.setVisibility(View.GONE);
		mCover.setVisibility(View.VISIBLE);
		mListView = (ListView) view.findViewById(R.id.id_lstview_layer_1);
		mOilGasAdapter = new MyAdapter();
		mListView.setAdapter(mOilGasAdapter);
		return view;
	}
	
	public void setMapView(MapView mapView) {
		this.mapView = mapView;
	}

	public void setMapServiceLayer(ArcGISTiledMapServiceLayer mapServiceLayer) {
		this.mapServiceLayer = mapServiceLayer;
	}

	public void setDrawLayer(GraphicsLayer drawLayer) {
		this.drawLayer = drawLayer;
	}

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setStyle(DialogFragment.STYLE_NORMAL, R.style.scenic_util_dialog_theme);
	}

	@Override
	public void onResume() {
		super.onResume();
//		Log.d(tag, "-----onResume: "+SinoApplication.layerName);
		if(SinoApplication.LNsatellite.equals(SinoApplication.layerName)){
			executeLayerSatellite();
		}else if(SinoApplication.LNgeographic.equals(SinoApplication.layerName)){
			executeLayerGeographic();
		}else{
			executeOilGas();
		}
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);

		mBtn1.setOnClickListener(this);
		mBtn2.setOnClickListener(this);
		mBtn3.setOnClickListener(this);
	}

	
	private Layer layerSatellite = null;
	private Layer layerGeographic = null;
	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.id_btn_layer_1:
			executeLayerSatellite();
//			mapView.removeAll();
//			ArcGISTiledMapServiceLayer layerSatellite = new ArcGISTiledMapServiceLayer(
//					SinoApplication.imageUrl);
//			mapView.addLayer(layerSatellite);
//			addDrawLayer();
//			layerSatellite.setVisible(true);
//			layerGeographic.setVisible(false);
			SinoApplication.layerName = SinoApplication.LNsatellite;
			break;
		case R.id.id_btn_layer_2:
			executeLayerGeographic();
//			mapView.removeAll();
//			ArcGISTiledMapServiceLayer layerGeographic = new ArcGISTiledMapServiceLayer(
//					SinoApplication.genUrl);
//			mapView.addLayer(layerGeographic);
//			layerSatellite.setVisible(false);
//			layerGeographic.setVisible(true);
			SinoApplication.currentLayerUrl = SinoApplication.genUrl;
			SinoApplication.layerName = SinoApplication.LNgeographic;
			break;
		case R.id.id_btn_layer_3:
			executeOilGas();
//			layerSatellite.setVisible(false);
//			layerGeographic.setVisible(true);
//			mapView.removeAll();
//			ArcGISTiledMapServiceLayer layerOilGas = new ArcGISTiledMapServiceLayer(
//					SinoApplication.oilUrl);
			SinoApplication.currentLayerUrl = SinoApplication.oilUrl;
//			mapView.addLayer(layerOilGas);
//			addDrawLayer();
			SinoApplication.layerName = SinoApplication.LNoilGas;
			
		
			break;

		case R.id.id_btn_operator_1:
			dismiss();
			showAlert();
			break;

		case R.id.id_btn_operator_2:
			dismiss();
			break;

		}

	}
   
	void showAlert() {
		// Toast.makeText(getActivity(), "功能暂时未支持", -1).show();
	}

	private void executeLayerGeographic() {
		// executeLayer1();
//		mBtn1.setSelected(false);
		if(mBtn2.isSelected()){
			layerGeographic.setVisible(false);
			mBtn2.setSelected(false);
		}else{
			layerGeographic.setVisible(true);
			mBtn2.setSelected(true);
		}
//		mBtn3.setSelected(false);
//		dismiss();

	}

	private void executeOilGas() {
//		mContaner.setVisibility(View.VISIBLE);
//		mBtn1.setSelected(false);
//		mBtn2.setSelected(false);
		if(mBtn3.isSelected()){
			mBtn3.setSelected(false);
			mListView.setEnabled(false);
			mCover.setVisibility(View.VISIBLE);
		}else{
			mBtn3.setSelected(true);
			mListView.setEnabled(true);
			mCover.setVisibility(View.GONE);
		}

	}

	private void executeLayerSatellite() {
//		dismiss();
		showAlert();
		if(mBtn1.isSelected()){
			layerSatellite.setVisible(false);
			mBtn1.setSelected(false);
		}else{
			layerSatellite.setVisible(true);
			mBtn1.setSelected(true);
		}
//		mBtn2.setSelected(false);
//		mBtn3.setSelected(false);
	}

	class MyAdapter extends BaseAdapter {
		
		@Override
		public int getCount() {
			return SinoApplication.mOilGasData.size();
		}

		@Override
		public OilGasData getItem(int position) {
			return SinoApplication.mOilGasData.get(position);
		}

		@Override
		public long getItemId(int position) {
			return position;
		}

		@Override
		public View getView(final int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.layer_list_item, null);
				
				holder.mName = (TextView) convertView.findViewById(R.id.layer_name);
				holder.mCBshow = (CheckBox) convertView.findViewById(R.id.layer_show);
				holder.mCBcur = (CheckBox) convertView.findViewById(R.id.layer_can_operator);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			OilGasData data = SinoApplication.mOilGasData.get(position);
			holder.mCBshow.setTag(data.getUrl());
			holder.mCBcur.setTag(data.getUrl());

			holder.mCBshow.setChecked(data.isVisible());
			holder.mCBcur.setChecked(data.isChecked());
			
			holder.mName.setText(data.getName());
			holder.mName.setBackgroundColor(data.getColor());
			
			holder.mCBshow.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isVisible) {
				     //处理是否可见
					String url = buttonView.getTag().toString();
					if(mapView.getLayerByURL(url) != null){
						mapView.getLayerByURL(url).setVisible(isVisible);
						resetVisibleData(url, isVisible);
					}
				}
			});
			holder.mCBcur.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
					String url = buttonView.getTag().toString();
					OilGasData data = SinoApplication.mOilGasData.get(position);
					String url4search = data.getSearchUrl();
//					Log.d("map", " url: " + data.getUrl() + " name: " + data.getName() +" id: " + data.getId());
					
					
					if (isChecked) {
						Log.d("map", " 选中当前图层 url: " + url + " name: " + data.getName() +" id: " + data.getId());
//						Log.d("map", "buttonView.getTag(): " + buttonView.getTag());
//						mapView.removeAll();
//						ArcGISTiledMapServiceLayer layer = new ArcGISTiledMapServiceLayer(url);
						SinoApplication.currentLayerUrl4Multi = data.getUrl();
						SinoApplication.currentLayerUrl = url4search;
						SinoApplication.mLayerName = data.getName();
//						mapView.addLayer(layer);
//							addDrawLayer();
						resetCheckData(url, isChecked);
					}
				}
			});
			
//			     盆地：http://202.204.193.201:6080/arcgis/rest/services/basin/MapServer
//				油藏：http://202.204.193.201:6080/arcgis/rest/services/oilreservoirs/MapServer
//				气藏：http://202.204.193.201:6080/arcgis/rest/services/gasreservoirs/MapServer
//				油田：http://202.204.193.201:6080/arcgis/rest/services/oilfields/MapServer
//				气田：http://202.204.193.201:6080/arcgis/rest/services/gasfields/MapServer
//				井位：http://202.204.193.201:6080/arcgis/rest/services/well/MapServer
//			

			return convertView;
		}
		
		private class ViewHolder {
			public TextView mName;
			public CheckBox mCBshow;
			public CheckBox mCBcur;
		}
		

		public void resetCheckData(String tag, boolean isChecked) {
			for (int i = 0; i < SinoApplication.mOilGasData.size(); i++) {
				OilGasData data = SinoApplication.mOilGasData.get(i);
				String url = data.getUrl();
				if(tag.equals(url)){
					SinoApplication.mOilGasData.get(i).setChecked(true);
				}else{
					SinoApplication.mOilGasData.get(i).setChecked(false);
				}
			}
			
			reShow();
		}
		
		public void resetVisibleData(String tag, boolean isVisible) {
			for (int i = 0; i < SinoApplication.mOilGasData.size(); i++) {
				OilGasData data = SinoApplication.mOilGasData.get(i);
				String url = data.getUrl();
				if(tag.equals(url)){
					SinoApplication.mOilGasData.get(i).setVisible(isVisible);
					break;
				}
			}
			reShow();
		}
		
	}

	//重新显示
	public void reShow() {
		mOilGasAdapter.notifyDataSetChanged();
	}
}
