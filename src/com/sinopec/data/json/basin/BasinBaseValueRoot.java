package com.sinopec.data.json.basin;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

import java.util.ArrayList;
import java.util.List;

public class BasinBaseValueRoot extends Bean {
	@JsonFieldName("盆地基础属性")
	@ListSaveType(vlaue = BasinBaseValue.class)
	public List<BasinBaseValue> list = new ArrayList<BasinBaseValue>();
}
