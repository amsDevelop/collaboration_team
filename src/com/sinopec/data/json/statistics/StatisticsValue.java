package com.sinopec.data.json.statistics;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

import java.util.ArrayList;

/**
 * 五、统计
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
		 * ,		
			ND,		--年度
LJYDZCL,		--累计油地质储量
LJQDZCL,		--累计气地质储量
LJNXYDZCL,		--累计凝析油地质储量
BZ		--备注

		 */
		
		
		/**
		 * 年度
		 */
		@JsonFieldName("ND")
		private long year;
		/**
		 * 累计油地质储量
		 */
		@JsonFieldName("LJYDZCL")
		private double storage;
		
		/**
		 * 累计气地质储量
		 */
		@JsonFieldName("LJQDZCL")
		private double storage1; 
		/**
		* 累计凝析油地质储量�
		*/
		@JsonFieldName("LJNXYDZCL")
		private double storage2; 

		/**
		 * 备注ע
		 */
		@JsonFieldName("BZ")
		private String memo;

		@Override
		public String toString() {
			return "DistributeChild [year=" + year + ", storage=" + storage + ", storage1=" + storage1 + ", storage2=" + storage2 + ", memo=" + memo + "]";
		} 
		
		
		
	}
	
}
