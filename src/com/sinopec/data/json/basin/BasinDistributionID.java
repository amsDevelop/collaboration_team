package com.sinopec.data.json.basin;

import java.util.ArrayList;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

/**
 * 4.分层系全球海相碳酸盐岩烃源岩分布
 * @author liuzhaodong
 */
public class BasinDistributionID extends Bean{

	@ListSaveType(vlaue=DistributeChild.class)
	public ArrayList<DistributeChild> mChilds = new ArrayList<BasinDistributionID.DistributeChild>();
	
	public DistributeChild newChildInstance(){
		DistributeChild instance = new DistributeChild();
		mChilds.add(instance);
		return instance;
	}
	
	/**
	 * 全球海相碳酸盐岩含油气盆地分布
	 * @author liuzhaodong
	 *
	 */
	public class DistributeChild extends Bean{
		@JsonFieldName("DZDYBM")
		public long basionId;

		@Override
		public String toString() {
			return "DistributeChild [basionId=" + basionId + "]";
		}
	}
	
}
