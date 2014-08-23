package com.sinopec.data.json;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.ParseException;
import org.apache.http.util.EntityUtils;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.test.AndroidTestCase;

import com.lenovo.nova.util.slog;
import com.lenovo.nova.util.network.NetworkManager;
import com.lenovo.nova.util.parse.Bean;
import com.lenovo.nova.util.parse.JsonToBeanParser;
import com.lenovo.nova.util.parse.JsonToBeanParser.OnJSONFillBeanHelper;
import com.sinopec.data.json.basin.BasinDistributionID;
import com.sinopec.data.json.standardquery.BasinBelonToRoot;
import com.sinopec.data.json.standardquery.DistributeCengGai;
import com.sinopec.data.json.standardquery.DistributeChuJi;
import com.sinopec.data.json.standardquery.DistributeJingRockYuanYan;
import com.sinopec.data.json.standardquery.DistributeRate;
import com.sinopec.data.json.standardquery.DistributeRateResource;

/**
 * �ġ��̶�ͳ��
 * 1��ȫ���������̼�����Ҳ�ϵ����������Դ���ֲ�
 * @author liuzhaodong
 *
 */
public class TestGuDingStatistics extends TestQueryGuDing {

	/**
	 * 1��ȫ���������̼�����Ҳ�ϵ����������Դ���ֲ�
	 *  lrr ��������Դ������   
		lrr=1 ��ʾ��Դ����    
		lrr=2 ��ʾ̽������     
		lrr=3 ��ʾ������Դ��

		yqlx �������� ��124Ϊʯ����Ȼ��
		tsyy ̼�����ұ��룺1095216660480
		hx  ������룺35183298347008

	 */
	public void testStatistics() {
		String lrr = "1";
		String yqlx = "124";
		String tsyy = "1095216660480";
		String hx = "35183298347008";
		String url = Constant.baseURL +
				"peprisapi/oilGasDFR.html?lrr="+ lrr +
				"&yqlx=" + yqlx +
				"&tsyy=" + tsyy +
				"&hx=" + hx
				;
		slog.p("url " + url);
		JSONArray jsonArray = getJsonArray(url);
		
		final BasinDistributionID instance = new BasinDistributionID();
		JsonToBeanParser.getInstance().fillBeanWithJson(instance.mChilds, jsonArray, new OnJSONFillBeanHelper() {
			@Override
			public Bean getNewBean() {
				return instance.newChildInstance();
			}
	
		});
		System.out.println(instance.mChilds);
	}
}
