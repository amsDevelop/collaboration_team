package com.sinopec.data.json.standardquery;

import java.util.ArrayList;

import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.anntation.json.JsonFieldName;
import com.lenovo.nova.util.parse.anntation.json.ListSaveType;

/**
 * 6.分类型盖层分布
 * @author liuzhaodong
 */
public class DistributeCengGai extends Bean{

	@ListSaveType(vlaue=DistributeChild.class)
	public ArrayList<DistributeChild> mChilds = new ArrayList<DistributeCengGai.DistributeChild>();
	
	public DistributeChild newChildInstance(){
		DistributeChild instance = new DistributeChild();
		mChilds.add(instance);
		return instance;
	}
	
	public class DistributeChild extends Bean{
		/**
		 * 烃源岩区编码
		 */
		@JsonFieldName("GCQBM")
		public long codeCengGai;
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
			return "DistributeChild [codeCengGai=" + codeCengGai + ", codeBelongToBasin=" + codeBelongToBasin + "]";
		}
		
		
	}
	
}
