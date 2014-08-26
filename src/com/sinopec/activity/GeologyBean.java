package com.sinopec.activity;

import java.util.ArrayList;
import java.util.List;

public class GeologyBean {
	ArrayList<GeoObject> geoObjects = new ArrayList<GeoObject>();

	public GeoObject createOneGeoObj(String id, String name) {
		GeoObject obj = new GeoObject();
		obj.id = id;
		obj.name = name;
		geoObjects.add(obj);
		return obj;
	}

	class GeoObject implements Bean {
		String id;
		String name;
		ArrayList<GeoCondition> conditions = new ArrayList<GeoCondition>();

		public GeoCondition createOneConditionObj(String id, String name,
				String state) {
			GeoCondition obj = new GeoCondition();
			obj.id = id;
			obj.name = name;
			obj.state = state;
			conditions.add(obj);
			return obj;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public List getContainer() {
			return conditions;
		}
	}

	class GeoCondition implements Bean {
		String id;
		String name;
		String state;
		ArrayList<Values> values = new ArrayList<Values>();
		public FValues createFVaueObj(String id, String name) {
			FValues obj = new FValues();
			obj.id = id;
			obj.name = name;
			values.add(obj);
			return obj;
		}
		public Values createOneVaueObj(String id, String name) {
			Values obj = new Values();
			obj.id = id;
			obj.name = name;
			values.add(obj);
			return obj;
		}

		@Override
		public String getName() {
			return name;
		}

		@Override
		public List getContainer() {
			return values;
		}
	}

	class Values implements Bean {
		private String id;
		private String name;
		ArrayList<FValues> mFvalue = new ArrayList<GeologyBean.FValues>();
		@Override
		public String getName() {
			return name;
		}
		
		public FValues createFValue(String id, String name) {
			FValues obj = new FValues();
			obj.id = id;
			obj.name = name;
			mFvalue.add(obj);
			return obj;
		}
		@Override
		public List getContainer() {
			return mFvalue;
		}
	}

	class FValues extends Values  implements Bean{
		String id;
		String name;
		ArrayList<SValues> values = new ArrayList<GeologyBean.SValues>();
		@Override
		public String getName() {
			return name;
		}
		
		public SValues createSValue(String id, String name) {
			SValues obj = new SValues();
			obj.id = id;
			obj.name = name;
			values.add(obj);
			return obj;
		}
		@Override
		public List getContainer() {
			return values;
		}
	}

	class SValues  implements Bean{
		ArrayList<TValues> values = new ArrayList<GeologyBean.TValues>();
		String id;
		String name;
		@Override
		public String getName() {
			return name;
		}
		public TValues createSValue(String id, String name) {
			TValues obj = new TValues();
			obj.id = id;
			obj.name = name;
			values.add(obj);
			return obj;
		}
		@Override
		public List getContainer() {
			return values;
		}
	}

	class TValues  implements Bean{
		String id;
		String name;
		@Override
		public String getName() {
			return name;
		}
		@Override
		public List getContainer() {
			return null;
		}
	}

	@Override
	public String toString() {
		return geoObjects.toString();
	}

	interface Bean {
		String getName();
		List getContainer();
	}
}
