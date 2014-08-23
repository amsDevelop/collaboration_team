package com.sinopec.data.json.standardquery;

import java.util.ArrayList;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

/**
 * 所属盆地
 * 全球海相碳酸盐岩含油气盆地分布
 * @author liuzhaodong
 *
 */
public class BasinBelonToRoot extends Bean{

	@ListSaveType(vlaue=BasinBelongTo.class)
	public ArrayList<BasinBelongTo> mBasinBelongTo = new ArrayList<BasinBelonToRoot.BasinBelongTo>();
	
	public BasinBelongTo newBasinBelongTo(){
		BasinBelongTo instance = new BasinBelongTo();
		mBasinBelongTo.add(instance);
		return instance;
	}
	
	public class BasinBelongTo extends Bean{
		@JsonFieldName("SSPDBM")
		private long beLongToId;
	}
	
}
