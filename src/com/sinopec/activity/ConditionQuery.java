package com.sinopec.activity;

import java.util.ArrayList;
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
import com.lenovo.nova.util.slog;
import com.lenovo.nova.util.parse.XmlParser;
import com.sinopec.activity.GeologyBean.Bean;
import com.sinopec.activity.GeologyBean.GeoCondition;
import com.sinopec.activity.GeologyBean.GeoObject;
import com.sinopec.activity.GeologyBean.Values;

public class ConditionQuery extends BaseDialogFragment implements
		OnClickListener {
	GeologyBean bean = null;
	GeoObject geoobj = null;
	GeoCondition condition = null;
	ViewGroup mContainer;
	private static final String TAG = "ConditionQuery";

	@Override
	public void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		init();
	}

	private void parser(XmlResourceParser xrp, String name) {
		if (name.equals("PropertySearch")) {
			bean = new GeologyBean();
		}
		if (name.equals("GeoObject")) {
			int count = xrp.getAttributeCount();
			if (count < 2) {
				slog.e(TAG, "attribute count error below 2 " + count);
			}
			try {
				String id = xrp.getAttributeValue(0);
				String attName = xrp.getAttributeValue(1);
				if (bean != null) {
					geoobj = bean.createOneGeoObj(id, attName);
				} else {
					slog.e(TAG, "W : bean is null ");
				}
			} catch (Exception e) {
				slog.e(TAG, "E : " + e);
				e.printStackTrace();
			}
		}
		if (name.equals("GeoCondition")) {
			int count = xrp.getAttributeCount();
			if (count < 3) {
				slog.e(TAG, "attribute count error below 3 " + count);
			}

			try {
				String id = xrp.getAttributeValue(0);
				String attName = xrp.getAttributeValue(1);
				String state = xrp.getAttributeValue(2);
				if (geoobj != null) {
					condition = geoobj
							.createOneConditionObj(id, attName, state);
				} else {
					slog.e(TAG, "W : geoobj is null ");
				}
			} catch (Exception e) {
				slog.e(TAG, "E : " + e);
				e.printStackTrace();
			}
		}
		if (isValue(name) || isFvalue(name) || isSvalue(name) || isTvalue(name)) {
			int count = xrp.getAttributeCount();
			if (count < 2) {
				slog.e(TAG, "attribute count error below 2 " + count);
			}
			try {
				String id = xrp.getAttributeValue(0);
				String attName = xrp.getAttributeValue(1);
				if (condition != null) {
					if (isValue(name)) {
						condition.createOneVaueObj(id, attName);
					}
					if (isFvalue(name)) {
						condition.createFValue(id, attName);
					}
					if (isSvalue(name)) {
						condition.createSValue(id, attName);
					}
					if (isTvalue(name)) {
						condition.createTValue(id, attName);
					}
				} else {
					slog.e(TAG, "W : condition is null ");
				}
			} catch (Exception e) {
				slog.e(TAG, "E : " + e);
				e.printStackTrace();
			}
		}
	}
	ParserTask task ;
	public void init() {
		XmlResourceParser xrp = getActivity().getResources().getXml(
				R.xml.search_widget_config);
		if(task == null){
			task = new ParserTask();
		}
		task.execute(xrp);
	}

	@Override
	public void onDestroy() {
		super.onDestroy();
		if(task != null){
			task.cancel(true);
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

	@Override
	public void onActivityCreated(Bundle savedInstanceState) {
		super.onActivityCreated(savedInstanceState);
		addItem();
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
					android.R.layout.simple_spinner_item, new String[] { "或者",
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
						android.R.layout.simple_spinner_item, str);
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
						android.R.layout.simple_spinner_item, str);
				mSpinnerCondition.setAdapter(adapterCondition);
			}
		}

		private void initConditiontValue(Context context) {
			slog.p(TAG, "initConditions geologyIndex " + geologyIndex
					+ " conditionIndex " + conditionIndex);

			ArrayList<Values> arrsys = bean.geoObjects.get(geologyIndex).conditions
					.get(conditionIndex).values;
			ArrayList<String> str = createArrayList(arrsys);

			if (adapterConditionValue != null) {
				adapterConditionValue.clear();
				adapterConditionValue.addAll(str);
				adapterConditionValue.notifyDataSetChanged();
			} else {
				adapterConditionValue = new ArrayAdapter<String>(context,
						android.R.layout.simple_spinner_item, str);
				mSpinnerConditionValue.setAdapter(adapterConditionValue);
			}

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

			// TODO Auto-generated method stub
			
			XmlParser parser = new XmlParser(arg0[0]) {
				@Override
				protected void onExecueStartTagEvent(XmlResourceParser parser,
						String name) {
					ConditionQuery.this.parser(parser, name);
				}
			};
			parser.parser();
			
			return null;
		}
		
	}
	
}
