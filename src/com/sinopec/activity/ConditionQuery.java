package com.sinopec.activity;

import java.util.ArrayList;
import java.util.Collection;
import java.util.List;

import android.content.Context;
import android.content.res.XmlResourceParser;
import android.os.AsyncTask;
import android.os.Bundle;
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
		}
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
			mContainer.removeViewAt(mContainer.getChildCount() - 1);
		}
	}

	private void addItem() {
		mContainer.addView(createItem());
	}

	private QueryItem createItem() {
		QueryItem item = new QueryItem(getActivity());
		MarginLayoutParams params = new MarginLayoutParams(
				Constant.WRAP_CONTENT, Constant.WRAP_CONTENT);
		params.topMargin = 20;
		item.setLayoutParams(params);
		return item;
	}

	

	
	
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

		private void initFirstValue(Context context) {
			ArrayAdapter<String> adapter = new ArrayAdapter<String>(context,
					getAdapterLayoutID(), new String[] { "或者",
							"并且" });
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
