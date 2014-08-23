package com.sinopec.data.json.standardquery;

import java.util.ArrayList;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

/**
 * 6.�����͸ǲ�ֲ�
 * @author liuzhaodong
 */
public class DistributeCengGai extends Bean{

	@ListSaveType(vlaue=DistributeChild.class)
	public ArrayList<DistributeChild> mChilds = new ArrayList<DistributeCengGai.DistributeChild>();
	
	public DistributeChild newChildInstance(){
		DistributeChild instance = new DistributeChild();
		mChilds.add(instance);
		return instance;
	}
	
	public class DistributeChild extends Bean{
		/**
		 * ��Դ�������
		 */
		@JsonFieldName("GCQBM")
		public long codeCengGai;
		/**
		 * ������ر���
		 */
		@JsonFieldName("SSPDBM")
		public long codeBelongToBasin;
		@Override
		public String toString() {
			return "DistributeChild [codeCengGai=" + codeCengGai + ", codeBelongToBasin=" + codeBelongToBasin + "]";
		}
		
		
	}
	
}
