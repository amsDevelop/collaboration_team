package com.sinopec.activity;

import java.util.ArrayList;

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
	}

	class GeoCondition implements Bean {
		String id;
		String name;
		String state;
		ArrayList<Values> values = new ArrayList<Values>();

		public Values createOneVaueObj(String id, String name) {
			Values obj = new Values();
			obj.id = id;
			obj.name = name;
			values.add(obj);
			return obj;
		}

		public Values createFValue(String id2, String attName) {
			FValues obj = new FValues();
			obj.id = id;
			obj.name = name;
			values.add(obj);
			return obj;
		}

		public Values createSValue(String id2, String attName) {
			SValues obj = new SValues();
			obj.id = id;
			obj.name = name;
			values.add(obj);
			return obj;
		}

		public Values createTValue(String id2, String attName) {
			TValues obj = new TValues();
			obj.id = id;
			obj.name = name;
			values.add(obj);
			return obj;
		}

		@Override
		public String getName() {
			return name;
		}
	}

	class Values implements Bean {
		String id;
		String name;

		@Override
		public String getName() {
			return name;
		}
	}

	class FValues extends Values {
	}

	class SValues extends Values {
	}

	class TValues extends Values {
	}

	@Override
	public String toString() {
		return geoObjects.toString();
	}

	interface Bean {
		String getName();
	}
}
