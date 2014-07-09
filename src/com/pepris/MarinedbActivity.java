package com.pepris;

import java.util.HashMap;
import java.util.Map;

import android.app.Activity;
import android.app.AlertDialog;
import android.app.Dialog; 
import android.content.DialogInterface;
import android.content.DialogInterface.OnClickListener;
import android.graphics.drawable.Drawable;
import android.os.Bundle;
import android.os.Handler;
import android.view.Display;
import android.view.KeyEvent;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.view.animation.Animation;
import android.view.animation.TranslateAnimation;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.view.animation.Animation;
import android.view.animation.Animation.AnimationListener;
import android.view.animation.TranslateAnimation;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapView;
import com.esri.android.map.ags.ArcGISFeatureLayer;
import com.esri.android.map.ags.ArcGISTiledMapServiceLayer;
import com.esri.android.map.ags.ArcGISFeatureLayer.MODE;
import com.esri.android.map.ags.ArcGISFeatureLayer.Options;
import com.esri.android.map.event.OnLongPressListener;
import com.esri.android.map.event.OnPanListener;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.android.map.event.OnZoomListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.Point;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.Symbol;

/**
 * 主程序
 * @author 唐先明
 *
 */
public class MarinedbActivity extends Activity implements OnClickListener{
	
	//泡泡变量
    private int screenWidth = 0;
    private int screenHeight = 0;	
    //Splash变量
    private LinearLayout splash = null;
    private static final int STOPSPLASH = 0;
    private static final long SPLASHTIME = 5000*1;//1000;
    private Handler splashHandler = new Handler() { 
	    public void handleMessage(android.os.Message msg) { 
	         switch (msg.what) { 
	         case STOPSPLASH: 
	            	//android.os.SystemClock.sleep(4000);
	        	 	android.os.SystemClock.sleep(1000); 
	                splash.setVisibility(View.GONE); 
	                break; 
	         } 
	         super.handleMessage(msg); 
	    } 
	}; 
	
	//泡泡变量
	private LinearLayout.LayoutParams popParams = null;
	private View popView = null;
	private Button poptitle = null;
	Callout callout = null;
	private ImageView imageAnim = null;
	String name2 = null;
	String Point_X = null;
	String Point_Y = null;
	String address2 = null;
	String telephone2 = null;
	double x1; 
	double y1;
	private Graphic LongPressPOI = null;
	private double curX;
	private double curY;
	private String curname = null;
	private String curadd = null;
	private Point anchorPt = null;
	
	private MapView map = null; 
	ArcGISFeatureLayer fLayer = null;
	private GraphicsLayer gLayer = null;

    /** Called when the activity is first created. */
    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.marinedb);
        
        //获取屏幕宽和高
        WindowManager windowManager = getWindowManager();
        Display display = windowManager.getDefaultDisplay();
        screenWidth = display.getWidth();
        screenHeight = display.getHeight();
        
	    splash = (LinearLayout) findViewById(R.id.splashscreen);
	    TextView mySpace = (TextView) findViewById(R.id.mySpace);
	    mySpace.setHeight((int)(screenHeight*2/3));
	    android.os.Message msg = new android.os.Message(); 
	    msg.what = STOPSPLASH; 
	    splashHandler.sendMessageDelayed(msg, SPLASHTIME);
	    
		map = (MapView)findViewById(R.id.map);	
		map.setExtent(new Envelope(-10868502.895856911, 4470034.144641369,
				-10837928.084542884, 4492965.25312689), 0);
		ArcGISTiledMapServiceLayer tms = new ArcGISTiledMapServiceLayer(
				"http://services.arcgisonline.com/ArcGIS/rest/services/World_Street_Map/MapServer");
		map.addLayer(tms);
		
		Options o = new Options();
		o.mode = MODE.ONDEMAND;
		o.outFields = new String[] { "FIELD_KID", "APPROXACRE", "FIELD_NAME",
				"STATUS", "PROD_GAS", "PROD_OIL", "ACTIVEPROD", "CUMM_OIL",
				"MAXOILWELL", "LASTOILPRO", "LASTOILWEL", "LASTODATE",
				"CUMM_GAS", "MAXGASWELL", "LASTGASPRO", "LASTGASWEL",
				"LASTGDATE", "AVGDEPTH", "AVGDEPTHSL", "FIELD_TYPE",
				"FIELD_KIDN" };
		//http://10.225.14.201:6080/arcgis/rest/services/marinegeo/MapServer
		fLayer = new ArcGISFeatureLayer(
				"http://sampleserver3.arcgisonline.com/ArcGIS/rest/services/Petroleum/KSPetro/MapServer/1",
				o);
		map.addLayer(fLayer);
		
        //泡泡代码        
		popView = View.inflate(this, R.layout.paopao, null);		
		ImageView imageSMS = (ImageView)popView.findViewById(R.id.tvImageSMS);	
		ImageView imageNavi = (ImageView)popView.findViewById(R.id.tvImageNavi);	
		poptitle = (Button)popView.findViewById(R.id.tvTitlePaopao);
		Drawable drawable1 = getResources().getDrawable(R.drawable.info1);
		imageSMS.setBackgroundDrawable(drawable1);
		imageSMS.getBackground().setAlpha(100);
		Drawable drawable2 = getResources().getDrawable(R.drawable.info3);
		imageNavi.setBackgroundDrawable(drawable2);
		imageNavi.getBackground().setAlpha(100);		
		poptitle.getBackground().setAlpha(100);
		
		imageAnim = (ImageView)findViewById(R.id.poiAnim);
		imageAnim.setVisibility(View.INVISIBLE);
		callout = map.getCallout();		
		callout.setContent(popView);
		callout.setStyle(R.layout.calloutwindow);
		callout.setMaxWidth(screenWidth-10);
		callout.setMaxHeight(50);
		
		map.setClickable(true);//设置地图可点击
		
		MarinedbActivity.this.map.setOnSingleTapListener(new OnSingleTapListener() {//单击事件
			@Override
			public void onSingleTap(float x, float y) {
				if(callout.isShowing())
					callout.hide();				
			}
		});
		
		MarinedbActivity.this.map.setOnLongPressListener(new OnLongPressListener(){
			public void onLongPress(float x, float y)
			{
				if(!map.isLoaded()){
					return;
				}  
        		Point pt = MarinedbActivity.this.map.toMapPoint(x, y);         		
	    		x1 = pt.getX();
	    		y1 = pt.getY();	    	
	    		name2 = "未知地名";
	    		address2 = "未知地址";
        		poptitle.setText("查询中,请稍侯...");
        		
        		try{
		    	curX = x1;
		    	curY = y1;
		    	curname = name2;
		    	curadd = address2;
		    	Point pxPt = MarinedbActivity.this.map.toScreenPoint(new Point(x1, y1)); 
		    	int pxx = 0;
		    	int pxy = 0;	    		
		    		
		    	pxx = (int)(pxPt.getX()-33/2+10);	//图标宽度(像素)
		    	pxy = (int)(pxPt.getY()-34/2); 		//图标高度(像素)
		    	anchorPt = MarinedbActivity.this.map.toMapPoint(pxx,pxy);        			
				//添加图标
			    if(LongPressPOI!=null)
			    {
			    	MarinedbActivity.this.gLayer.removeGraphic(LongPressPOI.getUid());
			    }	
			    Animation poiAnimation = new TranslateAnimation(pxx, pxx, 0, y);
			    poiAnimation.setDuration(500);
			    imageAnim.startAnimation(poiAnimation);
			    //MarinedbActivity.this.gLayer.postInvalidate();
			    poiAnimation.setAnimationListener(new AnimationListener() {
					
					@Override
					public void onAnimationStart(Animation animation) {
						// TODO Auto-generated method stub
						imageAnim.setVisibility(View.VISIBLE);
					}
					
					@Override
					public void onAnimationRepeat(Animation animation) {
						// TODO Auto-generated method stub
						
					}
					
					@Override
					public void onAnimationEnd(Animation animation) {
						// TODO Auto-generated method stub
						imageAnim.setVisibility(View.INVISIBLE);
		        		
						callout.setCoordinates(anchorPt);
		        		if(!callout.isShowing())
		        			callout.show();	    		
		        		callout.refresh();
		 						
					}
				});					

			} catch (Exception e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			}//onLongPress
		});
    }

	@Override 
	protected void onDestroy() { 
		super.onDestroy();
 }
	@Override
	protected void onPause() {
		super.onPause();
		map.pause();
 }
	@Override 	protected void onResume() {
		super.onResume(); 
		map.unpause();
	}

	public void onClick(DialogInterface arg0, int arg1) {
		// TODO Auto-generated method stub
		
	}


}