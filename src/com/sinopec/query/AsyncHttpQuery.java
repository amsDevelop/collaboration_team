package com.sinopec.query;

import java.io.IOException;

import org.apache.http.HttpResponse;
import org.apache.http.client.ClientProtocolException;
import org.apache.http.client.HttpClient;
import org.apache.http.client.methods.HttpGet;
import org.apache.http.impl.client.DefaultHttpClient;
import org.apache.http.util.EntityUtils;

import android.content.Context;
import android.os.Handler;
import android.os.Message;
import android.util.Log;

public class AsyncHttpQuery {

	private  String TAG = AsyncHttpQuery.class.getSimpleName();
	private Context mContext;
	private Handler mHandler;

	public AsyncHttpQuery(Handler handler,Context mContext) {
		this.mHandler = handler;
		this.mContext = mContext;
	}

//	private AsyncHttpClient asyncHttpClient = new AsyncHttpClient() {
//
//		@Override
//		protected AsyncHttpRequest newAsyncHttpRequest(
//				DefaultHttpClient client, HttpContext httpContext,
//				HttpUriRequest uriRequest, String contentType,
//				ResponseHandlerInterface responseHandler, Context context) {
//			// AsyncHttpRequest httpRequest = getHttpRequest(client,
//			// httpContext, uriRequest, contentType, responseHandler, context);
//			// return httpRequest == null
//			// ? super.newAsyncHttpRequest(client, httpContext, uriRequest,
//			// contentType, responseHandler, context)
//			// : httpRequest;
//			return null;
//		}
//	};

	private String url;

	public String getUrl() {
		return url;
	}

	public void setUrl(String url) {
		this.url = url;
	}

//	public AsyncHttpClient getAsyncHttpClient() {
//		return asyncHttpClient;
//	}

//	public void execute(String URL, ResponseHandlerInterface responseHandler) {
//		Log.v("mandy", "asyncHttpClient: " + asyncHttpClient + URL);
//		asyncHttpClient
//				.get(mContext, URL, new Header[1], null, responseHandler);
//	}

	public void execute(final int what, final String URL) {
		Log.i(TAG,"AsyncHttpQuery  what " + what + "   url "+ URL);
		new Thread(new Runnable() {

			@Override
			public void run() {

				HttpGet getMethod = new HttpGet(URL);
				HttpClient httpClient = new DefaultHttpClient();

				try {
					HttpResponse response = httpClient.execute(getMethod); // 发起GET请求

					Log.i("mandy", "resCode = "
							+ response.getStatusLine().getStatusCode()); // 获取响应码
//					Log.i("mandy",
//							"result = "
//									+ EntityUtils.toString(
//											response.getEntity(), "utf-8"));// 获取服务器响应内容
					int statusCode = response.getStatusLine().getStatusCode();
				     String result = EntityUtils.toString(response.getEntity(), "utf-8");
					
					if (statusCode == 200) {
						
						Message message = new Message();
						message.obj = result;
						message.what = what;
						mHandler.sendMessage(message);
					}
					
					
				} catch (ClientProtocolException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				} catch (IOException e) {
					// TODO Auto-generated catch block
					e.printStackTrace();
				}

			}
		}).start();

	}

}
