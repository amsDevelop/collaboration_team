package com.sinopec.data.json.standardquery;

import java.util.ArrayList;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

/**
 * 4.分层系全球海相碳酸盐岩烃源岩分布
 * @author liuzhaodong
 */
public class DistributeJingRockYuanYan extends Bean{

	@ListSaveType(vlaue=DistributeChild.class)
	public ArrayList<DistributeChild> mChilds = new ArrayList<DistributeJingRockYuanYan.DistributeChild>();
	
	public DistributeChild newChildInstance(){
		DistributeChild instance = new DistributeChild();
		mChilds.add(instance);
		return instance;
	}
	
	public class DistributeChild extends Bean{
		/**
		 * 烃源岩区编码
		 */
		@JsonFieldName("TYYQBM")
		private long codeRockJingYuan;
		/**
		 * 所属盆地编码
		 */
		@JsonFieldName("SSPDBM")
		private long codeBelongToBasin;
		@Override
		public String toString() {
			return "DistributeChild [codeRockJingYuan=" + codeRockJingYuan + ", codeBelongToBasin=" + codeBelongToBasin + "]";
		}
	}
	
}
