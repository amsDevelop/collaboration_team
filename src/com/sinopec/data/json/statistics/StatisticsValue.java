package com.sinopec.data.json.statistics;

import java.util.ArrayList;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

/**
 * 4.�ֲ�ϵȫ����̼��������Դ�ҷֲ�
 * @author liuzhaodong
 */
public class StatisticsValue extends Bean{

	@ListSaveType(vlaue=DistributeChild.class)
	public ArrayList<DistributeChild> mChilds = new ArrayList<StatisticsValue.DistributeChild>();
	
	public DistributeChild newChildInstance(){
		DistributeChild instance = new DistributeChild();
		mChilds.add(instance);
		return instance;
	}
	
	public class DistributeChild extends Bean{
		/**
		 * ,		--���
		LJYDZCL,		--�ۼ��͵��ʴ���
		LJQDZCL,		--�ۼ�����ʴ���
		LJNXYDZCL,		--�ۼ������͵��ʴ���
		BZ		--��ע

		 */
		
		
		/**
		 * ���
		 */
		@JsonFieldName("ND")
		private long year;
		/**
		 * �ۼ��͵��ʴ���
		 */
		@JsonFieldName("LJYDZCL")
		private double storage;
		
		/**
		 * �ۼ�����ʴ���
		 */
		@JsonFieldName("LJQDZCL")
		private double storage1; 
		/**
		* �ۼ������͵��ʴ���
		*/
		@JsonFieldName("LJNXYDZCL")
		private double storage2; 

		/**
		 * ��ע
		 */
		@JsonFieldName("BZ")
		private String memo;

		@Override
		public String toString() {
			return "DistributeChild [year=" + year + ", storage=" + storage + ", storage1=" + storage1 + ", storage2=" + storage2 + ", memo=" + memo + "]";
		} 
		
		
		
	}
	
}
