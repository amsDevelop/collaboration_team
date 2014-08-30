package com.sinopec.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.app.Activity;
import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.LayoutInflater;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.view.ViewGroup.MarginLayoutParams;
import android.widget.AdapterView;
import android.widget.AdapterView.OnItemSelectedListener;
import android.widget.ArrayAdapter;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.Toast;

import com.lenovo.nova.util.BaseDialogFragment;
import com.lenovo.nova.util.Constant;
import com.lenovo.nova.util.MyLog;
import com.lenovo.nova.util.slog;
import com.lenovo.nova.util.parse.XmlParser;
import com.sinopec.activity.GeologyBean.Bean;
import com.sinopec.activity.GeologyBean.FValues;
import com.sinopec.activity.GeologyBean.GeoCondition;
import com.sinopec.activity.GeologyBean.GeoObject;
import com.sinopec.activity.GeologyBean.SValues;
import com.sinopec.activity.GeologyBean.Values;
import com.sinopec.data.json.TestCustomQuery;
import com.sinopec.data.json.basin.BasinDistributionID;
import com.sinopec.drawtool.DrawTool;
import com.sinopec.util.RelativeUnicode;

public class ConditionQuery extends BaseDialogFragment implements
		OnClickListener {
	GeologyBean bean = null;
	GeoObject geoobj = null;
	/**
	 * 条件
	 */
	GeoCondition condition = null;
	ViewGroup mContainer;
	private Values mValues;
	private FValues mFvalue;
	private SValues svalue;
	private static final String TAG = "ConditionQuery";

	private static final String AND = "并且";
	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	
	private void parser(XmlParser xmlParser, XmlResourceParser xrp, String name,boolean start) {
		if(name == null){
			return;
		}
		
		if (name.equals("PropertySearch")) {
			if(start){
				bean = new GeologyBean();
			}
		}
		if (name.equals("GeoObject")) {
			if(start){
				int count = xrp.getAttributeCount();
				if (count < 2) {
					MyLog.e(TAG, "attribute count error below 2 " + count);
				}
				try {
					String id = xrp.getAttributeValue(0);
					if(id.equals("11")){
						System.out.println("11");
					}
					String attName = xrp.getAttributeValue(1);
					if (bean != null) {
						geoobj = bean.createOneGeoObj(id, attName);
					} else {
						MyLog.e(TAG, "W : bean is null ");
					}
				} catch (Exception e) {
					MyLog.e(TAG, "E : " + e);
					e.printStackTrace();
				}
			}
		}
		if (name.equals("GeoCondition")) {
			if(start){
				int count = xrp.getAttributeCount();
				if (count < 3) {
					MyLog.e(TAG, "attribute count error below 3 " + count);
				}
				
				try {
					String id = xrp.getAttributeValue(0);
					String attName = xrp.getAttributeValue(1);
					String state = xrp.getAttributeValue(2);
					if (geoobj != null) {
						condition = geoobj
								.createOneConditionObj(id, attName, state);
					} else {
						MyLog.e(TAG, "W : geoobj is null ");
					}
				} catch (Exception e) {
					MyLog.e(TAG, "E : " + e);
					e.printStackTrace();
				}
				
			}
		}
		if(isValue(name)){
			if(start){
				try {
					int count = xrp.getAttributeCount();
					if (count < 2) {
						MyLog.e(TAG, "attribute count error below 2 " + count  + xmlParser.debugAttributeValue(xrp));
					}
					String id = xrp.getAttributeValue(0);
					String attName = xrp.getAttributeValue(1);
					if (condition != null) {
						mValues = condition.createOneVaueObj(id, attName);
					}
				} catch (Exception e) {
					e.printStackTrace();
				}
			}else{
				mValues = null;
			}
		}
		
		if(isFvalue(name)){
			if(start){
				int count = xrp.getAttributeCount();
				if (count < 2) {
					MyLog.e(TAG, "attribute count error below 2 " + count);
				}
				String id = xrp.getAttributeValue(0);
				String attName = xrp.getAttributeValue(1);
				if(mValues != null){
					mFvalue =	mValues.createFValue(id, attName);
				}else if(condition != null){
					slog.p(TAG,"mValues is null , use condition " + condition.name + "　" + 
							attName);
					mFvalue = condition.createFVaueObj(id, attName);
				}
			}
		}
		
		if(isSvalue(name)){
			if(start){
				int count = xrp.getAttributeCount();
				if (count < 2) {
					MyLog.e(TAG, "attribute count error below 2 " + count);
				}
				String id = xrp.getAttributeValue(0);
				String attName = xrp.getAttributeValue(1);
				
				if(mFvalue != null){
					svalue = mFvalue.createSValue(id, attName);
				}
			}else{
				svalue = null;
			}
		}
		if(isTvalue(name)){
			if(start){
				int count = xrp.getAttributeCount();
				if (count < 2) {
					MyLog.e(TAG, "attribute count error below 2 " + count);
				}
				String id = xrp.getAttributeValue(0);
				String attName = xrp.getAttributeValue(1);
				
				if(svalue != null){
					svalue.createSValue(id, attName);
				}else{
					MyLog.e(TAG,"error svalue is null " + name);
				}
			}
		}
	}
	ParserTask task ;
	private MarinedbActivity mActivity;
	@Override
	public View onCreateView(LayoutInflater inflater, ViewGroup container,
			Bundle savedInstanceState) {
		View view = inflater.inflate(R.layout.layout_condition_query, null);
		mContainer = (ViewGroup) view
				.findViewById(R.id.id_llayout_item_container);
		view.findViewById(R.id.id_btn_condition_query_addition)
				.setOnClickListener(this);
		view.findViewById(R.id.id_btn_condition_query_subtraction)
				.setOnClickListener(this);
		view.findViewById(R.id.id_btn_condition_query_confirm).setOnClickListener(this);
		slog.p("ConditionQuery  onCreateView");
		return view;
	}

	public void init() {
		XmlResourceParser xrp = getActivity().getResources().getXml(
				R.xml.search_widget_config);
		if(task == null){
			task = new ParserTask();
		}
		task.execute(xrp);
	}

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		mActivity = (MarinedbActivity) getActivity();
		mInstances.clear();
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task != null){
			task.cancel(true);
		}
	}

	public void onClick(View v) {
		switch (v.getId()) {
		case R.id.id_btn_condition_query_addition:
			addItem();
			break;
		case R.id.id_btn_condition_query_subtraction:
			removeItem();
			break;
		case R.id.id_btn_condition_query_confirm:
			//confirm 
			destoryDialogAndCreateQueryUrl();
			break;
		}
	}

	private void destoryDialogAndCreateQueryUrl() {
		StringBuilder sb = new StringBuilder();
		String geologyObject = "";
		for(int i = 0; i < mContainer.getChildCount(); i++){
			QueryItem item = (QueryItem) mContainer.getChildAt(i);
			String orAnd = item.getSpinnerValue(0);
			geologyObject = item.getSpinnerValue(1);
			String condition = item.getSpinnerValue(2);
			String conditionValue = item.getSpinnerValue(3);
			double max = item.getMaxValue();
			double min = item.getMinValue();

			if(conditionValue.equals("特大型")){
				min = 1500;
			}else if(conditionValue.equals("超大型")){
				max = 1500;
				min = 500;
			}else if(conditionValue.equals("大型")){
				max = 500;
				min = 100;
			}else if(conditionValue.equals("中型")){
				max = 100;
				min = 10;
			}else if(conditionValue.equals("小型")){
				max = 10;
			}else if(conditionValue.equals("自定义")){
				
			}
			
			String str = getURLPartByValues(orAnd,geologyObject,
					condition,
					conditionValue,
					max,
					min
					);
			if(str.endsWith("&")){
				str = str.substring(0,str.length() - 1);
			}
			sb.append(str);
		}
		
		String key = sb.toString();
		if(key.startsWith("&")){
			key = key.substring(1);
		}
		
		
		slog.p(TAG, "destoryDialogAndCreateQueryUrl URL is " + key);
		if(geologyObject.equals("盆地")){
			executeNetworkForBasin(key);
		}
		if(geologyObject.equals("油气田")){
			executeNetworkForOil(key);
		}
		if(geologyObject.equals("油气藏")){
			executeNetworkForYouQi(key);
		}
	}


	private void executeNetworkForYouQi(final String key) {
		new Thread(new Runnable() {
			@Override
			public void run() {
			   final ArrayList<BasinDistributionID.DistributeChild> list = 
					   new TestCustomQuery(mActivity).testCustomOilCang(key);
			   draw(list);
			}
		}).start();
	
	}


	private void executeNetworkForOil(final String key) {
		new Thread(new Runnable() {
			@Override
			public void run() {
			   final ArrayList<BasinDistributionID.DistributeChild> list = 
					   new TestCustomQuery(mActivity).testCustomOilTian(key);
			   draw(list);
			}
		}).start();
	}


	private void draw(final ArrayList list){
		   final DrawTool tool =  mActivity.getDrawTool();
		  
		   Long objArray[] = new Long[list.size()];
		   for(int i = 0; i < list.size(); i++){
			   objArray[i] = ((BasinDistributionID.DistributeChild)list.get(i)).basionId;
		   }
		   final String objId = mActivity.whereSelect(objArray);
		   slog.p(TAG,"executeNetworkForBasin objId " + objId);
		   
		   final String layerUrl = getString(R.string.url_basin) + "/0";
		   slog.p(TAG,"executeNetworkForBasin layerUrl " + layerUrl);
		   mActivity.runOnUiThread(new Runnable() {
			
			@Override
			public void run() {
				if(list == null || list.size() < 1){
					
					Toast.makeText(getActivity(), "没有数据", -1).show();
					return ;
				}
				tool.queryAttribute4Query(objId, layerUrl , list);
				dismiss();
			}
		});
	}
	private void executeNetworkForBasin(final String key) {
		new Thread(new Runnable() {
			@Override
			public void run() {
			   final ArrayList<BasinDistributionID.DistributeChild> list = 
					   new TestCustomQuery(getActivity()).testCustomBasin(key);
			   draw(list);
			}
		}).start();
	}


	private String getURLPartByValues(String orAnd, String geologyObject, 
			String condition, String conditionValue,double max ,double min) {
		slog.p(TAG, "getURLPartByValues orAnd " +
			orAnd + "  orAnd " + geologyObject + 
			" condition "  +condition + " conditionValue " + conditionValue +
			"max " + max +" min " + min
			);
		
			StringBuilder sb = new StringBuilder();
			 if(orAnd.equals(AND)){
				 //多条加入的
				 sb.append("&"); 
			 }
			//判断有没有最大值
			if(max >0 || min > 0){
				if(max > 0){
					//add max
					String conditionCode = getEncodeByMap(condition+"最大值");
					sb.append(conditionCode+"="+max);
					 sb.append("&"); 
				}
				
				if(min > 0){
					//add min
					String conditionCode = getEncodeByMap(condition+"最小值");
					sb.append(conditionCode+"="+min);
					sb.append("&");
				}
			}else{
				 //建立key value
				String conditionCode = getEncodeByMap(condition);
				String conditionValueCode = getEncodeByMap(conditionValue);
				sb.append(conditionCode+"="+conditionValueCode);
			}
			slog.p(TAG, "getURLPartByValues url part is " + sb.toString());
			
		return sb.toString();
	}


	private String getEncodeByMap(String condition) {
		String conditionCode;
		conditionCode = RelativeUnicode.mEnCode.get(condition);
		if(conditionCode == null){
			MyLog.e("Errror can not find " + condition + " in mEncode" );
			return null;
		}
		return conditionCode;
	}


	private boolean isTvalue(String name) {
		return name.equals("tvalue");
	}

	private boolean isSvalue(String name) {
		return name.equals("svalue");
	}

	private boolean isFvalue(String name) {
		return name.equals("fvalue");
	}

	private boolean isValue(String name) {
		return name.equals("value");
	}

	private void removeItem() {
		if (mContainer.getChildCount() > 0) {
			int id = mContainer.getChildCount() - 1;
			mInstances.remove(id);
			mContainer.removeViewAt(id);
		}
	}

	private void addItem() {
		QueryItem view  = createItem();
		mInstances.add(view);
		mContainer.addView(view);
	}

	private QueryItem createItem() {
		QueryItem item = new QueryItem(getActivity());
		MarginLayoutParams params = new MarginLayoutParams(
				Constant.WRAP_CONTENT, Constant.WRAP_CONTENT);
		params.topMargin = 20;
		item.setLayoutParams(params);
		return item;
	}

	

	
	
	private final static ArrayList<QueryItem> mInstances = new ArrayList<ConditionQuery.QueryItem>();
	class QueryItem extends LinearLayout implements OnItemSelectedListener {

		Spinner mSpinnerFisrt;
		Spinner mSpinnerGeology;
		Spinner mSpinnerCondition;
		Spinner mSpinnerConditionValue;
		int geologyIndex;
		int conditionIndex;
		int conditionValueIndex;
		private ArrayAdapter<String> adapterGeology;
		private ArrayAdapter<String> adapterCondition;
		private ArrayAdapter<String> adapterConditionValue;
		private EditText mETValue1;
		private EditText mETValue2;

		public QueryItem(Context context) {
			super(context);
			LayoutInflater.from(context).inflate(
					R.layout.layout_condition_query_compotent, this, true);

			mSpinnerFisrt = (Spinner) findViewById(R.id.id_spner_first);
			mSpinnerGeology = (Spinner) findViewById(R.id.id_spner_geology);
			mSpinnerCondition = (Spinner) findViewById(R.id.id_spner_condition);
			mSpinnerConditionValue = (Spinner) findViewById(R.id.id_spner_condition_value);
			mETValue1 = (EditText) findViewById(R.id.id_spner_condition_custom_value1);
			mETValue2 = (EditText) findViewById(R.id.id_spner_condition_custom_value2);
			initFirstValue(context);
			initSpinnerGeologyValue(context);
			initConditions(context);
			initConditiontValue(context);

			mSpinnerFisrt.setOnItemSelectedListener(this);
			mSpinnerGeology.setOnItemSelectedListener(this);
			mSpinnerCondition.setOnItemSelectedListener(this);
			mSpinnerConditionValue.setOnItemSelectedListener(this);
		}

		public double getMaxValue(){
			return getEditValue(mETValue2);
		}
		public double getMinValue(){
			return getEditValue(mETValue1);
		}
		private double getEditValue(EditText etvalue){
			try {
				if(etvalue.getVisibility() == View.VISIBLE){
					double value = Double.parseDouble(etvalue.getText().toString());
					return value;
				}
			} catch (Exception e) {
				slog.e(TAG,"getMaxValue error mETValue1.getText() " + etvalue.getText());
				e.printStackTrace();
			}
			return -1;
		}
		
		public String getSpinnerValue(int index){
			String value = null;
			slog.p(TAG, "getFirstSpinnerValue index is " + index );
			switch(index){
			case 0:
				value = (String)mSpinnerFisrt.getSelectedItem();
				break;
			case 1:
				value = (String)mSpinnerGeology.getSelectedItem();
				break;
			case 2:
				value = (String)mSpinnerCondition.getSelectedItem();
				break;
			case 3:
				value = (String)mSpinnerConditionValue.getSelectedItem();
				break;
			}
			slog.p(TAG, "getFirstSpinnerValue value is " + value );
			return value;
		}
		
		private void initFirstValue(Context context) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					getAdapterLayoutID(), new String[] {
				AND , "或者"});
			mSpinnerFisrt.setAdapter(adapter);
		}

		private void initSpinnerGeologyValue(Context context) {
			slog.p(TAG, "initSpinnerGeologyValue ");
			ArrayList<String> str = createArrayList(bean.geoObjects);
			if (adapterGeology != null) {
				adapterGeology.clear();
				adapterGeology.addAll(str);
				adapterGeology.notifyDataSetChanged();
			} else {
				adapterGeology = new ArrayAdapter<String>(context,
						getAdapterLayoutID(), str);
				mSpinnerGeology.setAdapter(adapterGeology);
			}
		}

		private void initConditions(Context context) {
			slog.p(TAG, "initConditions geologyIndex " + geologyIndex);
			ArrayList<String> str = createArrayList(bean.geoObjects
					.get(geologyIndex).conditions);
			if (adapterCondition != null) {
				adapterCondition.clear();
				adapterCondition.addAll(str);
				adapterCondition.notifyDataSetChanged();
			} else {
				adapterCondition = new ArrayAdapter<String>(context,
						getAdapterLayoutID(), str);
				mSpinnerCondition.setAdapter(adapterCondition);
			}
		}

		private int getAdapterLayoutID() {
			return R.layout.adapter_layout_spinner;
		}

		private void initConditiontValue(Context context) {
			slog.p(TAG, "initConditions geologyIndex " + geologyIndex
					+ " conditionIndex " + conditionIndex);

			ArrayList<Values> arrsys = bean.geoObjects.get(geologyIndex).conditions
					.get(conditionIndex).values;
//			ArrayList<String> str = createArrayList(arrsys);
			
			ArrayList<String> str = new ArrayList<String>();
			
			getAllChildStr(arrsys,str);
			
			if (adapterConditionValue != null) {
				adapterConditionValue.clear();
				adapterConditionValue.addAll(str);
				adapterConditionValue.notifyDataSetChanged();
			} else {
				adapterConditionValue = new ArrayAdapter<String>(context,
						getAdapterLayoutID(), str);
				mSpinnerConditionValue.setAdapter(adapterConditionValue);
			}

		}

		private List getAllChildStr(List arrsys,ArrayList<String> childList) {
			if(arrsys != null){
				for(int i = 0; i < arrsys.size(); i++){
					Bean ben = (Bean) arrsys.get(i);
					String name = ben.getName();
					List<Bean> list = ben.getContainer();
					childList.add(name);
					getAllChildStr(list, childList);
				}
			}
			return childList;
		}

		private ArrayList<String> createArrayList(List<?> list) {
			ArrayList<String> str = new ArrayList<String>();
			for (int i = 0; i < list.size(); i++) {
				str.add(((Bean) list.get(i)).getName());
			}
			return str;
		}

		public void hideFirstSpiner() {
			mSpinnerFisrt.setVisibility(View.INVISIBLE);
		}

		@Override
		public void onItemSelected(AdapterView<?> parent, View view,
				int position, long id) {
			if (parent == mSpinnerGeology) {
				geologyIndex = position;
				initConditions(getActivity());
				initConditions(getActivity());
				initConditiontValue(getActivity());
				changeAllGeology();
			}
			if (parent == mSpinnerCondition) {
				conditionIndex = position;
				initConditions(getActivity());
				initConditiontValue(getActivity());
			}
			if (parent == mSpinnerConditionValue) {
				conditionValueIndex = position;
			}
			String selectItem = null;
			try {
				selectItem = mSpinnerConditionValue.getSelectedItem()
						.toString();
			} catch (Exception e) {
				e.printStackTrace();
			}
			slog.p(TAG, "selectItem " + selectItem);
			if (mETValue1 != null && mETValue2 != null) {
				if (selectItem != null && selectItem.equals("自定义")) {
					mETValue1.setVisibility(View.VISIBLE);
					mETValue2.setVisibility(View.VISIBLE);
				} else {
					mETValue1.setVisibility(View.GONE);
					mETValue2.setVisibility(View.GONE);
				}
			}
		}

		private void changeAllGeology() {
//			for(int i = 0; i  )
		}

		@Override
		public void onNothingSelected(AdapterView<?> parent) {

		}

	}

	
	class ParserTask extends AsyncTask<XmlResourceParser,Void,Void> {
		@Override
		protected Void doInBackground(XmlResourceParser... arg0) {
			
			final XmlParser parser = new XmlParser(arg0[0]) {
				@Override
				protected void onExecueStartTagEvent(XmlResourceParser parser,
						String name) {
					ConditionQuery.this.parser(this,parser, name,true);
				}
				@Override
				protected void onExecuteEndTagEvent(XmlResourceParser parser, String name) {
					ConditionQuery.this.parser(this,parser, name,false);
				}
			};
			parser.parser();
			
			return null;
		}
		
		@Override
		protected void onPostExecute(Void result) {
			super.onPostExecute(result);
			addItem();
			System.out.println(bean);
		}
	}
	
}
