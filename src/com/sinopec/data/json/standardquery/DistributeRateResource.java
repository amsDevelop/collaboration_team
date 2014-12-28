package com.sinopec.data.json.standardquery;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

import java.util.ArrayList;

/**
 * 3.全球含油气盆地碳酸盐岩层系油气资源量占盆地油气总资源量比例分布
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
		 * 盆地总资源量
		 */
		@JsonFieldName("ZCLZYL")
		private double allResCount;
		
		/**
		 *  碳酸盐岩资源量
		 */
		@JsonFieldName("TSYZYL")
		private double rockResCount;
		
		public double getAllResCount() {
			return allResCount;
		}

		public double getRockResCount() {
			return rockResCount;
		}

		/**
		 * 所属盆地编码
		 */
		@JsonFieldName("SSPDBM")
		private long codeBelongToBasin;
		
		public long getCodeBelongToBasin() {
			return codeBelongToBasin;
		}

		@Override
		public String toString() {
			return "DistributeChild [allResCount=" + allResCount + ", rockResCount=" + rockResCount + ", codeBelongToBasin=" + codeBelongToBasin + "]";
		}
	}
	
}
