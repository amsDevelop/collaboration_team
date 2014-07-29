package com.sinopec.activity;

import java.util.ArrayList;
import java.util.List;

import android.annotation.SuppressLint;
import android.app.DialogFragment;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.BaseAdapter;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.CompoundButton;
import android.widget.CompoundButton.OnCheckedChangeListener;
import android.widget.ListView;
import android.widget.TextView;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISLayerInfo;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.sinopec.application.SinoApplication;

@SuppressLint("NewApi")
public class LayerDialog extends DialogFragment implements OnClickListener {
	private String tag = "map";
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

	private List<ArcGISLayerInfo> layerInfos = new ArrayList<ArcGISLayerInfo>();

	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(resID, null);

		mBtn1 = (Button) view.findViewById(R.id.id_btn_layer_1);
		mBtn2 = (Button) view.findViewById(R.id.id_btn_layer_2);
		mBtn3 = (Button) view.findViewById(R.id.id_btn_layer_3);
		mCover = (TextView) view.findViewById(R.id.layer_lstview_cover);

		view.findViewById(R.id.id_btn_operator_1).setOnClickListener(this);
		view.findViewById(R.id.id_btn_operator_2).setOnClickListener(this);
		mBtn3 = (Button) view.findViewById(R.id.id_btn_layer_3);

		mContaner = (ViewGroup) view
				.findViewById(R.id.id_llaout_layer_container);
//		mContaner.setVisibility(View.GONE);
		mCover.setVisibility(View.VISIBLE);
		mListView = (ListView) view.findViewById(R.id.id_lstview_layer_1);
		mListView.setAdapter(new MyAdapter());
		initData();

		return view;
	}

	private void initData() {
		layerInfos.removeAll(layerInfos);
		// TODO Auto-generated method stub
		ArcGISLayerInfo[] arc = mapServiceLayer.getAllLayers();
		if(arc != null){
			Log.d(tag, "-----onCreateView: "+arc.length);
			for (int i = 0; i < arc.length; i++) {
				layerInfos.add(arc[i]);
			}
		}
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
		setStyle(DialogFragment.STYLE_NORMAL, R.style.dialog_theme);
	}

	@Override
	public void onResume() {
		super.onResume();
		Log.d(tag, "-----onResume: "+SinoApplication.layerName);
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

	@Override
	public void onClick(View arg0) {
		switch (arg0.getId()) {
		case R.id.id_btn_layer_1:
			executeLayerSatellite();
			SinoApplication.layerName = SinoApplication.LNsatellite;
			break;
		case R.id.id_btn_layer_2:
			executeLayerGeographic();
			SinoApplication.layerName = SinoApplication.LNgeographic;
			break;
		case R.id.id_btn_layer_3:
			executeOilGas();
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
		mCover.setVisibility(View.VISIBLE);
		mListView.setEnabled(false);
		mBtn1.setSelected(false);
		mBtn2.setSelected(true);
		mBtn3.setSelected(false);
//		dismiss();
		mapView.removeAll();
		ArcGISTiledMapServiceLayer arcGISTiledMapServiceLayer = new ArcGISTiledMapServiceLayer(
				MarinedbActivity.genUrl);
		mapView.addLayer(arcGISTiledMapServiceLayer);
		addDrawLayer();
	}

	private void addDrawLayer() {
		if (mapView.getLayerByID(drawLayer.getID()) != null) {
			mapView.removeLayer(drawLayer);
			mapView.addLayer(drawLayer);
		} else {
			mapView.addLayer(drawLayer);
		}
	}

	private void executeOilGas() {
//		mContaner.setVisibility(View.VISIBLE);
		mCover.setVisibility(View.GONE);
		mListView.setEnabled(true);
		mBtn1.setSelected(false);
		mBtn2.setSelected(false);
		mBtn3.setSelected(true);
		mapView.removeAll();
		ArcGISTiledMapServiceLayer arcGISTiledMapServiceLayer = new ArcGISTiledMapServiceLayer(
				MarinedbActivity.oilUrl);
		mapView.addLayer(arcGISTiledMapServiceLayer);
		addDrawLayer();
	}

	private void executeLayerSatellite() {
		mCover.setVisibility(View.VISIBLE);
		mListView.setEnabled(false);
//		dismiss();
		mapView.removeAll();
		ArcGISTiledMapServiceLayer arcGISTiledMapServiceLayer = new ArcGISTiledMapServiceLayer(
				MarinedbActivity.imageUrl);
		mapView.addLayer(arcGISTiledMapServiceLayer);
		addDrawLayer();
		showAlert();
		mBtn1.setSelected(true);
		mBtn2.setSelected(false);
		mBtn3.setSelected(false);
	}

	class MyAdapter extends BaseAdapter {
		// ArrayList<String> mList = new ArrayList<String>();

		// public MyAdapter() {
		// mList.add("油气");
		// mList.add("盆地");
		// mList.add("石油");
		// }

		@Override
		public int getCount() {
			return layerInfos.size();
		}

		@Override
		public Object getItem(int arg0) {
			return null;
		}

		@Override
		public long getItemId(int arg0) {
			return 0;
		}

		@Override
		public View getView(int position, View convertView, ViewGroup parent) {
			ViewHolder holder = new ViewHolder();
			if (convertView == null) {
				convertView = LayoutInflater.from(getActivity()).inflate(R.layout.layer_list_item, null);

				holder.mName = (TextView) convertView.findViewById(R.id.menu_item_name);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}

			TextView txView = (TextView) convertView.findViewById(R.id.layer_name);
			holder.mCBcur = (CheckBox) convertView.findViewById(R.id.layer_can_operator);
            switch (position) {
			case 0:
				holder.mCBcur.setTag("http://202.204.193.201:6080/arcgis/rest/services/oilreservoirs/MapServer");
				break;
			case 1:
				holder.mCBcur.setTag("http://202.204.193.201:6080/arcgis/rest/services/gasreservoirs/MapServer");
				break;
			case 2:
				holder.mCBcur.setTag("http://202.204.193.201:6080/arcgis/rest/services/well/MapServer");
				break;
			case 3:
				holder.mCBcur.setTag("http://202.204.193.201:6080/arcgis/rest/services/oilfields/MapServer");
				break;
			case 4:
				holder.mCBcur.setTag("http://202.204.193.201:6080/arcgis/rest/services/gasfields/MapServer");
				break;
			case 5:
				holder.mCBcur.setTag("http://202.204.193.201:6080/arcgis/rest/services/basin/MapServer");
				break;
			default:
				break;
			}
			
			
			
			txView.setText(layerInfos.get(position).getName());
			
			holder.mCBcur.setOnCheckedChangeListener(new OnCheckedChangeListener() {
				
				@Override
				public void onCheckedChanged(CompoundButton buttonView, boolean isChecked) {
				      
					if (isChecked) {
						Log.v("mandy", "buttonView.getTag(): " + buttonView.getTag());
//						Log.v("mandy", "holder.mCBcur: " + holder.mCBcur.getTag());
//						mapView.removeAll();
						ArcGISTiledMapServiceLayer arcGISTiledMapServiceLayer = new ArcGISTiledMapServiceLayer(
								"http://cache1.arcgisonline.cn/arcgis/rest/services/ChinaCities_Community_BaseMap_CHN/Beijing_Community_BaseMap_CHN/MapServer");
						mapView.addLayer(arcGISTiledMapServiceLayer);
						
					}
				}
			});
			
//			盆地：http://202.204.193.201:6080/arcgis/rest/services/basin/MapServer
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
			public CheckBox mCBcur;
		}
		

	}

}
