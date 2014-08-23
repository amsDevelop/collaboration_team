package com.sinopec.data.json.standardquery;

import java.util.ArrayList;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

/**
 * 2.全球含油气盆地碳酸盐岩层系油气储量占盆地油气总储量比例分布
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
		 * 总储量岩层储量
		 */
		@JsonFieldName("ZCLYCCL")
		private int allStorage;
		
		/**
		 *  碳酸盐岩岩层储量
		 */
		@JsonFieldName("TSYYCCL")
		private int storage2;
		
		/**
		 * 所属盆地编码
		 */
		@JsonFieldName("SSPDBM")
		private long codeBelongToBasin;

		@Override
		public String toString() {
			return "RateForOilAndBasin [allStorage=" + allStorage + ", storage2=" + storage2 + ", codeBelongToBasin=" + codeBelongToBasin + "]";
		}
	}
	
}
