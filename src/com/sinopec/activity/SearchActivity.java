package com.sinopec.activity;

import java.lang.ref.WeakReference;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.Window;
import android.widget.BaseAdapter;
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.symbol.TextSymbol;
import com.esri.core.tasks.geocode.Locator;
import com.esri.core.tasks.geocode.LocatorFindParameters;
import com.esri.core.tasks.geocode.LocatorGeocodeResult;
import com.sinopec.view.MenuButtonNoIcon;

public class SearchActivity extends Activity implements OnClickListener {
	private ListView mListView;
	private Context mContext;
	private MenuButtonNoIcon mConfirm;
	private ViewGroup mViewGroup;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.mContext = this;
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_search);
		initView();
		initData();
	}
	
	private ImageButton mBtnBack;
	private void initView() {
		mConfirm = (MenuButtonNoIcon) findViewById(R.id.btn_search_confirm);
		mConfirm.setOnClickListener(this);
		mListView = (ListView) findViewById(R.id.search_listview);
		mViewGroup = (ViewGroup) findViewById(R.id.search_listview_layout);
		mBtnBack = (ImageButton) findViewById(R.id.btn_login_back);
		mBtnBack.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View arg0) {
				finish();
			}
		});
		
	}
	
	private ArrayList<HashMap<String, Object>> mList = new ArrayList<HashMap<String, Object>>();
	private MyAdapter mAdapter;
	private void initData() {
		mAdapter = new MyAdapter(mContext, mList);
		mListView.setAdapter(mAdapter);
		
	}
	
	class MyAdapter extends BaseAdapter {
		private ArrayList<HashMap<String, Object>> mList;
		private Context mContext;
		public MyAdapter(Context context, ArrayList<HashMap<String, Object>> list ) {
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
				convertView = LayoutInflater.from(mContext).inflate(R.layout.search_listview_item, null);

				holder.mName = (TextView) convertView.findViewById(R.id.menu_item_name);

				convertView.setTag(holder);
			} else {
				holder = (ViewHolder) convertView.getTag();
			}
			
			HashMap<String, Object> map = mList.get(position);
			String name = (String) map.get("name");
			holder.mName.setText(name);
			return convertView;
		}
		
		private class ViewHolder {
			public TextView mName;
//			ImageView icon;
		}
		
	}

	@Override
	public void onClick(View v) {
		switch(v.getId()){
		case R.id.btn_search_confirm:
			for (int i = 0; i < 12; i++) {
				HashMap<String, Object> map = new HashMap<String, Object>();
				map.put("name", "名字"+ i);
				mList.add(map);
			}
			mAdapter.notifyDataSetChanged();
			mViewGroup.setVisibility(View.VISIBLE);
			break;
		case R.id.id_btn_operator_2:
			break;
		}
	}
	
	private void search(String key) {
	}
	
	
    Locator locator;
    LocatorGeocodeResult geocodeResult;
    Callout locationCallout;
    GeocoderTask mWorker;
    private ProgressDialog dialog;

    Point mLocation = null;
	 /*
     * AsyncTask to geocode an address to a point location Draw resulting point
     * location on the map with matching address
     */
    private class GeocoderTask extends
            AsyncTask<LocatorFindParameters, Void, List<LocatorGeocodeResult>> {

        WeakReference<SearchActivity> mActivity;

        GeocoderTask(SearchActivity activity) {
            mActivity = new WeakReference<SearchActivity>(activity);
        }

        // The result of geocode task is passed as a parameter to map the
        // results
        protected void onPostExecute(List<LocatorGeocodeResult> result) {
            if (result == null || result.size() == 0) {
                // update UI with notice that no results were found
                Toast toast = Toast.makeText(SearchActivity.this,
                        "No result found.", Toast.LENGTH_LONG);
                toast.show();
            } else {
                // update global result
                geocodeResult = result.get(0);
                // show progress dialog while geocoding address
                dialog = ProgressDialog.show(mContext, "Geocoder",
                        "Searching for address ...");
                // get return geometry from geocode result
                Geometry resultLocGeom = geocodeResult.getLocation();
                // create marker symbol to represent location
                SimpleMarkerSymbol resultSymbol = new SimpleMarkerSymbol(
                        Color.BLUE, 20, SimpleMarkerSymbol.STYLE.CIRCLE);
                // create graphic object for resulting location
                Graphic resultLocation = new Graphic(resultLocGeom,
                        resultSymbol);
                //TODO:add graphic to location layer
//                locationLayer.addGraphic(resultLocation);

                // // create callout for return address
                // locationCallout = mMapView.getCallout();
                // String place = geocodeResult.getAddress();
                // locationCallout.setContent(loadView(place));
                // locationCallout.show();

                // create text symbol for return address
                TextSymbol resultAddress = new TextSymbol(12,
                        geocodeResult.getAddress(), Color.BLACK);
                // create offset for text
                resultAddress.setOffsetX(10);
                resultAddress.setOffsetY(50);
                // create a graphic object for address text
                Graphic resultText = new Graphic(resultLocGeom, resultAddress);
                // TODO: add address text graphic to location graphics layer
//                locationLayer.addGraphic(resultText);
                // TODO:zoom to geocode result

//                mMapView.zoomToResolution(geocodeResult.getLocation(), 2);
                // create a runnable to be added to message queue
                handler.post(new MyRunnable());
            }
        }

        // invoke background thread to perform geocode task
        @Override
        protected List<LocatorGeocodeResult> doInBackground(
                LocatorFindParameters... params) {

            // create results object and set to null
            List<LocatorGeocodeResult> results = null;
            // set the geocode service
            locator = Locator.createOnlineLocator(getResources()
                    .getString(R.string.geocode_url));
            try {

                // pass address to find method to return point representing
                // address
                results = locator.find(params[0]);
            } catch (Exception e) {
                e.printStackTrace();
            }
            // return the resulting point(s)
            return results;
        }

    }
    
    /*
     * Dismiss dialog when geocode task completes
     */
    public class MyRunnable implements Runnable {
        public void run() {
            dialog.dismiss();
        }
    }
    
    private Handler handler = new Handler();

}
