package com.sinopec.data.json;

import android.content.Context;
import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.JsonToBeanParser;
import com.lenovo.nova.util.parse.JsonToBeanParser.OnJSONFillBeanHelper;
import com.sinopec.data.json.standardquery.DistributeCengGai;
import org.json.JSONArray;


/**
 * ．属性查询
 * @author liuzhaodong
 */
public class TestAttributeQuery extends TestQueryGuDing {

	public TestAttributeQuery(Context context) {
		super(context);
		// TODO Auto-generated constructor stub
	}

	/**
	 * 1.盆地
	 */
	public void testDistributeCengGai() {
		String basinId = "201102001130";
		String url = Constant.baseURL +
				"peprisapi/basinAttribute.html?basinId=" + basinId
				;
			
		JSONArray jsonArray = getJsonArray(url);
		
		final DistributeCengGai instance = new DistributeCengGai();
		JsonToBeanParser.getInstance().fillBeanWithJson(instance.mChilds, jsonArray, new OnJSONFillBeanHelper() {
			@Override
			public Bean getNewBean() {
				return instance.newChildInstance();
			}
	
		});
		System.out.println(instance.mChilds);
	}
	
}
