package com.sinopec.data.json.standardquery;

import java.util.ArrayList;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

/**
 * 4.�ֲ�ϵȫ����̼��������Դ�ҷֲ�
 * @author liuzhaodong
 */
public class DistributeJingRockYuanYan extends Bean{

	@ListSaveType(vlaue=DistributeChild.class)
	public ArrayList<DistributeChild> mChilds = new ArrayList<DistributeJingRockYuanYan.DistributeChild>();
	
	public DistributeChild newChildInstance(){
		DistributeChild instance = new DistributeChild();
		mChilds.add(instance);
		return instance;
	}
	
	public class DistributeChild extends Bean{
		/**
		 * ��Դ�������
		 */
		@JsonFieldName("TYYQBM")
		private long codeRockJingYuan;
		/**
		 * ������ر���
		 */
		@JsonFieldName("SSPDBM")
		private long codeBelongToBasin;
		@Override
		public String toString() {
			return "DistributeChild [codeRockJingYuan=" + codeRockJingYuan + ", codeBelongToBasin=" + codeBelongToBasin + "]";
		}
	}
	
}
