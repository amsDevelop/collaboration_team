package com.sinopec.data.json.basin;

import java.util.ArrayList;
import java.util.List;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

public class BasinBaseValueRoot extends Bean {
	@JsonFieldName("盆地基础属性")
	@ListSaveType(vlaue = BasinBaseValue.class)
	private List<BasinBaseValue> list = new ArrayList<BasinBaseValue>();
}
