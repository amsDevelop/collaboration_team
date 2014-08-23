package com.sinopec.data.json.standardquery;

import java.util.ArrayList;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

/**
 * �������
 * ȫ����̼�����Һ�������طֲ�
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
