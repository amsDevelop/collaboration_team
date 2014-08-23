package com.sinopec.data.json.standardquery;

import java.util.ArrayList;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

/**
 * 2.ȫ���������̼�����Ҳ�ϵ������ռ��������ܴ�������ֲ�
 * @author liuzhaodong
 */
public class DistributeRate extends Bean{

	@ListSaveType(vlaue=RateForOilAndBasin.class)
	public ArrayList<RateForOilAndBasin> mChilds = new ArrayList<DistributeRate.RateForOilAndBasin>();
	
	public RateForOilAndBasin newChildInstance(){
		RateForOilAndBasin instance = new RateForOilAndBasin();
		mChilds.add(instance);
		return instance;
	}
	
	public class RateForOilAndBasin extends Bean{
		/**
		 * �ܴ����Ҳ㴢��
		 */
		@JsonFieldName("ZCLYCCL")
		private int allStorage;
		
		/**
		 *  ̼�������Ҳ㴢��
		 */
		@JsonFieldName("TSYYCCL")
		private int storage2;
		
		/**
		 * ������ر���
		 */
		@JsonFieldName("SSPDBM")
		private long codeBelongToBasin;

		@Override
		public String toString() {
			return "RateForOilAndBasin [allStorage=" + allStorage + ", storage2=" + storage2 + ", codeBelongToBasin=" + codeBelongToBasin + "]";
		}
	}
	
}
