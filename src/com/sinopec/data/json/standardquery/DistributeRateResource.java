package com.sinopec.data.json.standardquery;

import java.util.ArrayList;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

/**
 * 2.ȫ���������̼�����Ҳ�ϵ������ռ��������ܴ�������ֲ�
 * @author liuzhaodong
 */
public class DistributeRateResource extends Bean{

	@ListSaveType(vlaue=DistributeChild.class)
	public ArrayList<DistributeChild> mChilds = new ArrayList<DistributeRateResource.DistributeChild>();
	
	public DistributeChild newChildInstance(){
		DistributeChild instance = new DistributeChild();
		mChilds.add(instance);
		return instance;
	}
	
	public class DistributeChild extends Bean{
		/**
		 * �������Դ��
		 */
		@JsonFieldName("ZCLZYL")
		private int allResCount;
		
		/**
		 *  ̼��������Դ��
		 */
		@JsonFieldName("TSYZYL")
		private int rockResCount;
		
		/**
		 * ������ر���
		 */
		@JsonFieldName("SSPDBM")
		private long codeBelongToBasin;

		@Override
		public String toString() {
			return "DistributeChild [allResCount=" + allResCount + ", rockResCount=" + rockResCount + ", codeBelongToBasin=" + codeBelongToBasin + "]";
		}
	}
	
}
