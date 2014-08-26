package com.sinopec.data.json.basin;

import java.util.ArrayList;
import java.util.List;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

public class BasinBaseAttributeRoot extends Bean{
	@JsonFieldName("油气田基础属性")
	@ListSaveType(vlaue = BasinBaseAttributeValue.class)
	public List<BasinBaseAttributeValue> list = new ArrayList<BasinBaseAttributeValue>();
}
