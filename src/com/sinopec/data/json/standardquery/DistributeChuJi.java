package com.sinopec.data.json.standardquery;

import java.util.ArrayList;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

/**
 * 5.分类型全球海相碳酸盐岩储集层分布
 * @author liuzhaodong
 */
public class DistributeChuJi extends Bean{

	@ListSaveType(vlaue=DistributeChild.class)
	public ArrayList<DistributeChild> mChilds = new ArrayList<DistributeChuJi.DistributeChild>();
	
	public DistributeChild newChildInstance(){
		DistributeChild instance = new DistributeChild();
		mChilds.add(instance);
		return instance;
	}
	
	public class DistributeChild extends Bean{
		/**
		 * 烃源岩区编码
		 */
		@JsonFieldName("CJCQBM")
		public long codeChuJi;
		/**
		 * 所属盆地编码
		 */
		@JsonFieldName("SSPDBM")
		public long codeBelongToBasin;
		
		
		public long getCodeBelongToBasin() {
			return codeBelongToBasin;
		}

		@Override
		public String toString() {
			return "DistributeChild [codeChuJi=" + codeChuJi + ", codeBelongToBasin=" + codeBelongToBasin + "]";
		}
		
		
	}
	
}
