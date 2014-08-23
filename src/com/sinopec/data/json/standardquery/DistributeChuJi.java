package com.sinopec.data.json.standardquery;

import java.util.ArrayList;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

/**
 * 5.������ȫ����̼�����Ҵ�����ֲ�
 * @author liuzhaodong
 */
public class DistributeChuJi extends Bean{

	@ListSaveType(vlaue=DistributeChild.class)
	public ArrayList<DistributeChild> mChilds = new ArrayList<DistributeChuJi.DistributeChild>();
	
	public DistributeChild newChildInstance(){
		DistributeChild instance = new DistributeChild();
		mChilds.add(instance);
		return instance;
	}
	
	public class DistributeChild extends Bean{
		/**
		 * ��Դ�������
		 */
		@JsonFieldName("CJCQBM")
		public long codeChuJi;
		/**
		 * ������ر���
		 */
		@JsonFieldName("SSPDBM")
		public long codeBelongToBasin;
		@Override
		public String toString() {
			return "DistributeChild [codeChuJi=" + codeChuJi + ", codeBelongToBasin=" + codeBelongToBasin + "]";
		}
		
		
	}
	
}
