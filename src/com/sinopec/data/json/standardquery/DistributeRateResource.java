package com.sinopec.data.json.standardquery;

import java.util.ArrayList;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

/**
 * 2.全球含油气盆地碳酸盐岩层系油气储量占盆地油气总储量比例分布
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
		private int allResCount;
		
		/**
		 *  碳酸盐岩资源量
		 */
		@JsonFieldName("TSYZYL")
		private int rockResCount;
		
		/**
		 * 所属盆地编码
		 */
		@JsonFieldName("SSPDBM")
		private long codeBelongToBasin;

		@Override
		public String toString() {
			return "DistributeChild [allResCount=" + allResCount + ", rockResCount=" + rockResCount + ", codeBelongToBasin=" + codeBelongToBasin + "]";
		}
	}
	
}
