package com.sinopec.drawtool;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map.Entry;

import android.content.Context;
import android.graphics.Color;
import android.text.TextUtils;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.core.geometry.AreaUnit;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Geometry;
import com.esri.core.geometry.GeometryEngine;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.LinearUnit;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.SpatialReference;
import com.esri.core.geometry.Transformation2D;
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.ags.identify.IdentifyParameters;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.esri.core.tasks.ags.query.Query;
import com.sinopec.application.SinoApplication;
import com.sinopec.common.CommonData;
import com.sinopec.common.InterfaceDataCallBack;
import com.sinopec.task.SearchIdentifyTask;
import com.sinopec.task.SearchIdentifyTask.OnFinishListener;
import com.sinopec.task.SearchQueryTask;
import com.sinopec.task.SearchQueryTask.OnQueryFinishListener;


public class DrawTool extends Subject {

	private MapView mapView;
	private GraphicsLayer tempLayer;
	private MarkerSymbol markerSymbol;
	private LineSymbol lineSymbol;
	private FillSymbol fillSymbol;
	private int drawType;
	private boolean active;
	private Point point;
	private Envelope envelope;
	private Polyline polyline;
	private Polygon polygon;
	private DrawTouchListener drawListener;
	private MapOnTouchListener defaultListener;
	private Graphic drawGraphic;
	private GraphicsLayer drawLayer;
	private GraphicsLayer mDrawLayer4HighLight;
	private List<Line> lines = new ArrayList<Line>();

	public static final int POINT = 1;
	public static final int ENVELOPE = 2;
	public static final int POLYLINE = 3;
	public static final int POLYGON = 4;
	public static final int CIRCLE = 5;
	public static final int ANY_POLYGON = 6;
	public static final int FREEHAND_POLYGON = 7;
	public static final int FREEHAND_POLYLINE = 8;
	public  boolean isSelectDraw = false;
	/**
	 * 多选
	 */
	public static final int MULTI_POINT = 9;

	private InterfaceDataCallBack mCallback;
	public DrawTool(MapView mapView, InterfaceDataCallBack callback, Callout callout) {
		this.mapView = mapView;
		this.mCallback = callback;
		this.mCallout = callout;
		this.tempLayer = new GraphicsLayer();
//		this.tempLayer.set
		this.mapView.addLayer(this.tempLayer);
		drawListener = new DrawTouchListener(this.mapView.getContext(),
				this.mapView);
		defaultListener = new MapOnTouchListener(this.mapView.getContext(),
				this.mapView);
		this.markerSymbol = new SimpleMarkerSymbol(Color.BLUE, 10,
				SimpleMarkerSymbol.STYLE.CIRCLE);
		
//		new SimpleMarkerSymbol(Color.BLUE, 10,
//				SimpleMarkerSymbol.STYLE.CIRCLE);
//		this.lineSymbol = new SimpleLineSymbol(Color.BLACK, 2);
		lineSymbol = new SimpleLineSymbol(Color.RED, 5,
				SimpleLineSymbol.STYLE.DASH);
		
		fillSymbol = new SimpleFillSymbol(Color.BLACK);
		fillSymbol.setOutline(new SimpleLineSymbol(Color.RED, 2));
		fillSymbol.setAlpha(100);
		
	
		
//		Log.v("mandy", "x:" + this.mapView.getLocationOnScreen(location));
		
//		Log.v("mandy", "y:" + this.mapView.getPivotY());
//		this.mapView.getPivotX();
	}
    
	
	public void setDrawLayer(GraphicsLayer drawLayer, GraphicsLayer drawLayer4HighLight) {
		this.drawLayer = drawLayer;
		this.mDrawLayer4HighLight = drawLayer4HighLight;
	}


	public void activate(int drawType) {
		isSelectDraw = true;
		
		if (this.mapView == null)
			return;
		this.mapView.setOnTouchListener(drawListener);
		this.drawType = drawType;
		this.active = true;
		
		switch (this.drawType) {
		case DrawTool.POINT:
			this.point = new Point();
//			drawGraphic.setGeometry(this.point);
//			drawGraphic.setSymbol(this.markerSymbol);
			drawGraphic = new Graphic(this.point, markerSymbol);
//			drawGraphic.
			this.tempLayer.addGraphic(drawGraphic);
			break;
		case DrawTool.ENVELOPE:
			this.envelope = new Envelope();
			break;
		case DrawTool.POLYGON:
		case DrawTool.CIRCLE:
		case DrawTool.FREEHAND_POLYGON:
			this.polygon = new Polygon();
//			drawGraphic.setGeometry(this.polygon);
//			drawGraphic.setSymbol(fillSymbol);
			drawGraphic = new Graphic(this.polygon, fillSymbol);
//			drawGraphic.
			this.tempLayer.addGraphic(drawGraphic);
			
			break;
		case DrawTool.POLYLINE:
		case DrawTool.FREEHAND_POLYLINE:
			this.polyline = new Polyline();
//			drawGraphic.setGeometry(this.polyline);
//			drawGraphic.setSymbol(lineSymbol);
			break;
		}
	}

	public void deactivate() {
//		this.mapView.setOnTouchListener(defaultListener);
		this.tempLayer.removeAll();
		this.active = false;
		this.drawType = -1;
		this.point = null;
//		this.envelope = null;
//		this.polygon = null;
		this.polyline = null;
		this.drawGraphic = null;
		drawListener.clearGraphic();
	}

	public MarkerSymbol getMarkerSymbol() {
		return markerSymbol;
	}

	public void setMarkerSymbol(MarkerSymbol markerSymbol) {
		this.markerSymbol = markerSymbol;
	}

	public LineSymbol getLineSymbol() {
		return lineSymbol;
	}

	public void setLineSymbol(LineSymbol lineSymbol) {
		this.lineSymbol = lineSymbol;
	}

	public FillSymbol getFillSymbol() {
		return fillSymbol;
	}

	public void setFillSymbol(FillSymbol fillSymbol) {
		this.fillSymbol = fillSymbol;
	}

	private void sendDrawEndEvent() {
//		
//		DrawEvent e = new DrawEvent(this, DrawEvent.DRAW_END,
//				DrawTool.this.drawGraphic);
//		DrawTool.this.notifyEvent(e);
		int type = this.drawType;
		this.deactivate();
		this.activate(type);
	}

	private Callout mCallout;
	class DrawTouchListener extends MapOnTouchListener {

		private Point startPoint;
		private Point ptStart = null;// 起点
		private Point ptOnly = null;// 纪录一个点
		private Point ptPrevious = null;// 上一个点
		private ArrayList<Point> points = null;// 记录全部点
		private Polygon tempPolygon = null;// 记录绘制过程中的多边形
		MultiPath poly;
		private int uid = -1;

		public DrawTouchListener(Context context, MapView view) {
			super(context, view);
			points = new ArrayList<Point>();
		}

		public boolean onTouch(View view, MotionEvent event) {
//			if (mCallout.isShowing()) {
//				mCallout.hide();
//			}
			if (active
					&& (drawType == POINT || drawType == ENVELOPE
							|| drawType == CIRCLE
							|| drawType == FREEHAND_POLYLINE || drawType == FREEHAND_POLYGON)
					&& event.getAction() == MotionEvent.ACTION_DOWN) {
				Point point = mapView.toMapPoint(event.getX(), event.getY());
				switch (drawType) {
				case DrawTool.POINT:
					DrawTool.this.point.setXY(point.getX(), point.getY());
					sendDrawEndEvent();
					break;
				case DrawTool.ENVELOPE:
//					startPoint = point;
//					envelope.setCoords(point.getX(), point.getY(),
//							point.getX(), point.getY());
					break;
				case DrawTool.CIRCLE:
					startPoint = point;
					break;
				case DrawTool.FREEHAND_POLYGON:
					polygon.startPath(point);
					break;
				case DrawTool.FREEHAND_POLYLINE:
					polyline.startPath(point);
					break;
				}
				tempLayer.removeAll();
			}
			return super.onTouch(view, event);
		}
        
		public boolean onDragPointerMove(MotionEvent from, MotionEvent to) {
			if (active
					&& (drawType == ENVELOPE || drawType == FREEHAND_POLYGON
							|| drawType == FREEHAND_POLYLINE || drawType == CIRCLE || drawType == ANY_POLYGON)) {
				Point point = mapView.toMapPoint(to.getX(), to.getY());
				switch (drawType) {
				case DrawTool.ENVELOPE:
					
					if (uid == -1) { // first time
						drawLayer.removeAll();
						Graphic g = new Graphic(null, fillSymbol);
					  	startPoint = mapView.toMapPoint(from.getX(), from.getY());
						uid  = drawLayer.addGraphic(g);

					} else { 
//						gLayer.removeAll();
						Point p2 = mapView.toMapPoint(new Point(to.getX(), to.getY()));
//						Envelope envelope = new Envelope();
						envelope.merge(startPoint);
						envelope.merge(p2);
						drawLayer.updateGraphic(uid, envelope);
					}
					
					break;
				case DrawTool.FREEHAND_POLYGON:
					polygon.lineTo(point);
					break;
				case DrawTool.FREEHAND_POLYLINE:
					polyline.lineTo(point);
					break;
				case DrawTool.CIRCLE:
					
					double radius = Math.sqrt(Math.pow(startPoint.getX()
							- point.getX(), 2)
							+ Math.pow(startPoint.getY() - point.getY(), 2));
					
					if (uid == -1) { // first time
						drawLayer.removeAll();
						getCircle(startPoint, radius, polygon);
						Graphic g = new Graphic(null, fillSymbol);
						uid  = drawLayer.addGraphic(g);

					} else { 
						getCircle(startPoint, radius, polygon);
						drawLayer.updateGraphic(uid, polygon);
					}
					
					break;
				case DrawTool.ANY_POLYGON:
					if (uid == -1) {
						drawLayer.removeAll();
						poly = new Polygon();
						startPoint = mapView.toMapPoint(from.getX(), from.getY());
						poly.startPath((float) startPoint.getX(),
								(float) startPoint.getY());
						
						poly.lineTo((float) startPoint.getX(),
								(float) startPoint.getY());
//						/*
//						 * Create a Graphic and add polyline geometry
//						 */
						Graphic graphic = new Graphic(poly, fillSymbol);
//
//						/*
//						 * add the updated graphic to graphics layer
//						 */
						uid = drawLayer.addGraphic(graphic);
						
					} else {
						poly.lineTo((float) point.getX(), (float) point.getY());
						drawLayer.updateGraphic(uid, poly);
						
					} 
					break;
				}
				return true;
			}
			return super.onDragPointerMove(from, to);
		}
   
		public boolean onDragPointerUp(MotionEvent from, MotionEvent to) {
			if (active && (drawType == ENVELOPE || drawType == FREEHAND_POLYGON
					|| drawType == FREEHAND_POLYLINE || drawType == CIRCLE || drawType == ANY_POLYGON)) {
				Point point = mapView.toMapPoint(to.getX(), to.getY());
				switch (drawType) {
				case DrawTool.ENVELOPE:
//					String sArea = getAreaString(envelope.calculateArea2D());
//					Toast.makeText(mapView.getContext(), "总面积： " + sArea,
//							Toast.LENGTH_SHORT).show();
					String sArea = getAreaString(envelope.calculateArea2D());
					Toast.makeText(mapView.getContext(), sArea, Toast.LENGTH_SHORT).show();
					
					queryAttribute4Query(envelope);
					
					break;
				case DrawTool.FREEHAND_POLYGON:
					polygon.lineTo(point);
					break;
				case DrawTool.FREEHAND_POLYLINE:
					polyline.lineTo(point);
					break;
				case DrawTool.CIRCLE:
					queryAttribute4Query(polygon);
					break;
				case DrawTool.ANY_POLYGON:
					queryAttribute4Query(poly);
					break;
				}
				sendDrawEndEvent();
				this.startPoint = null;
				uid = -1;
				return true;
			}
			return super.onDragPointerUp(from, to);
		}

		public boolean onSingleTap(MotionEvent point) {
//			Point point = mapView.toMapPoint(event.getX(), event.getY());
//			 clearGraphic();
			if (active && (drawType==POLYGON || drawType==POLYLINE ||drawType == MULTI_POINT)) {
				if (ptStart == null)
					drawLayer.removeAll();// 第一次开始前，清空全部graphic
				
				Point ptCurrent = mapView.toMapPoint(new Point(point.getX(), point
						.getY()));
				points.add(ptCurrent);// 将当前点加入点集合中
				if (drawType == MULTI_POINT) {
					//多选
//					Log.d("map", "------点选-多选 ------SingleTap--x:"+point.getX()+"  y: "+point.getY());
					ptOnly = null;
					ptOnly = mapView.toMapPoint(point.getX(), point.getY());
					
					drawLayer.removeAll();
					queryAttribute4OnlyOnePonit(ptOnly);
				}else{
					if (ptStart == null) {// 画线或多边形的第一个点
						ptStart = ptCurrent;
						
						markerSymbol = new SimpleMarkerSymbol(Color.BLUE, 10,
								SimpleMarkerSymbol.STYLE.CIRCLE);
						// 绘制第一个点
						Graphic graphic = new Graphic(ptStart, markerSymbol);
						drawLayer.addGraphic(graphic);
					} else {// 画线或多边形的其他点
						// 绘制其他点
						markerSymbol = new SimpleMarkerSymbol(Color.BLUE, 15,
								SimpleMarkerSymbol.STYLE.DIAMOND);
						Graphic graphic = new Graphic(ptCurrent, markerSymbol);
						drawLayer.addGraphic(graphic);
	
						// 生成当前线段（由当前点和上一个点构成）
						Line line = new Line();
						line.setStart(ptPrevious);
						line.setEnd(ptCurrent);
	
						if (drawType == DrawTool.POLYLINE) {
							// 绘制当前线段
							Polyline polyline = new Polyline();
							polyline.addSegment(line, true);
							Graphic g = new Graphic(polyline, lineSymbol);
							drawLayer.addGraphic(g);
	
							double len = GeometryEngine.geodesicLength(polyline, mapView.getSpatialReference(),
									null);
							String length = "";
							DecimalFormat df = new DecimalFormat("#.00");
								double temp = BigDecimal.valueOf(len).divide(new BigDecimal(1000)).doubleValue();
								length = df.format(temp)+ " 千米";
							Toast.makeText(mapView.getContext(), "长度： " + length,
									Toast.LENGTH_SHORT).show();
						} else {
                         
							if (tempPolygon == null)
								tempPolygon = new Polygon();
							
							if (uid == -1) {
								tempPolygon.addSegment(line, false);
								Graphic g = new Graphic(tempPolygon, fillSymbol);
								uid = drawLayer.addGraphic(g);
							
								
							} else {
								tempPolygon.addSegment(line, false);
								drawLayer.updateGraphic(uid, tempPolygon);
							}
							
	
							
						}
						lines.add(line);
					}
				}
				ptPrevious = ptCurrent;
//				tempLayer.postInvalidate();
				return true;
			}
			return false;
		}

		private void clearGraphic() {
			ptOnly = null;
			ptStart = null;
			ptPrevious = null;
			points.clear();
			tempPolygon = null;
			uid = -1;
			isSelectDraw = false;

		}
		public boolean onDoubleTap(MotionEvent event) {

			 if (drawType == DrawTool.POLYGON || drawType == DrawTool.ANY_POLYGON){
					Polygon polygon = new Polygon();

					Point startPoint = null;
					Point endPoint = null;
					// 绘制完整的多边形
					for (int i = 1; i < drawListener.points.size(); i++) {
						startPoint = drawListener.points.get(i - 1);
						endPoint = drawListener.points.get(i);
                        
						Line line = new Line();
						line.setStart(startPoint);
						line.setEnd(endPoint);

						polygon.addSegment(line, false);
					}

					Graphic g = new Graphic(polygon, fillSymbol);
					drawLayer.addGraphic(g);
//					queryAttribute(polygon);
					queryAttribute4Query(polygon);
					return true;
			
			 }
			return super.onDoubleTap(event);
		}
		
		@Override
		public void onLongPress(MotionEvent point) {
		   if (isSelectDraw) {
			    return ;
		   }
		   super.onLongPress(point);
		}

		private void getCircle(Point center, double radius, Polygon circle) {
			circle.setEmpty();
			Point[] points = getPoints(center, radius);
			circle.startPath(points[0]);
			for (int i = 1; i < points.length; i++)
				circle.lineTo(points[i]);
		}

		private Point[] getPoints(Point center, double radius) {
			Point[] points = new Point[50];
			double sin;
			double cos;
			double x;
			double y;
			for (double i = 0; i < 50; i++) {
				sin = Math.sin(Math.PI * 2 * i / 50);
				cos = Math.cos(Math.PI * 2 * i / 50);
				x = center.getX() + radius * sin;
				y = center.getY() + radius * cos;
				points[(int) i] = new Point(x, y);
			}
			return points;
		}
	}
	
	public void calculateAreaAndLength(String tag) {
//		drawLayer.removeAll();
		if (DrawTool.POLYLINE == drawType) {
			Polyline polyline = new Polyline();
			Point startPoint = null;
			Point endPoint = null;

			// 绘制完整的线段
			for (int i = 1; i < drawListener.points.size(); i++) {
				startPoint = drawListener.points.get(i - 1);
				endPoint = drawListener.points.get(i);

				Line line = new Line();
				line.setStart(startPoint);
				line.setEnd(endPoint);
//				Log.d("map", "--for---折线长度："+line.calculateLength2D());
				polyline.addSegment(line, false);
			}

			Graphic g = new Graphic(polyline, lineSymbol);
			drawLayer.addGraphic(g);

			double len = 0.0;
			
//			Log.d("map", "-----折线长度："+polyline.calculateLength2D());
			String length = "";
			DecimalFormat df = new DecimalFormat("#.00");
			if("KM".equals(tag)){
				len = 0.0;
				len = GeometryEngine.geodesicLength(polyline, mapView.getSpatialReference(),
						new LinearUnit(LinearUnit.Code.KILOMETER));
				length = len + " 千米";
			}else if("M".equals(tag)){
				len = 0.0;
				len = GeometryEngine.geodesicLength(polyline, mapView.getSpatialReference(),
						new LinearUnit(LinearUnit.Code.METER));
				length = df.format(len)+ " 米";
			}
			
			Toast.makeText(mapView.getContext(), "总长度： " + length,
					Toast.LENGTH_SHORT).show();
		} else if (drawType == DrawTool.POLYGON || drawType == DrawTool.ANY_POLYGON){
			Polygon polygon = new Polygon();

			Point startPoint = null;
			Point endPoint = null;
			// 绘制完整的多边形
			for (int i = 1; i < drawListener.points.size(); i++) {
				startPoint = drawListener.points.get(i - 1);
				endPoint = drawListener.points.get(i);

				Line line = new Line();
				line.setStart(startPoint);
				line.setEnd(endPoint);

				polygon.addSegment(line, false);
			}

			Graphic g = new Graphic(polygon, fillSymbol);
			drawLayer.addGraphic(g);

			// 计算总面积
			String sArea = getAreaString(polygon.calculateArea2D());
//			GeometryEngine.geodesicLength(polygon, mapView.getSpatialReference(),
//					new LinearUnit(LinearUnit.Code.KILOMETER));
			
//			GeometryEngine.geodesicArea();
			Toast.makeText(mapView.getContext(), "总面积： " + sArea,
					Toast.LENGTH_SHORT).show();
		} else if (drawType == DrawTool.ENVELOPE) {
		
			String sArea = getAreaString(envelope.calculateArea2D());

			Toast.makeText(mapView.getContext(), "总面积： " + sArea,
					Toast.LENGTH_SHORT).show();
		}

		// 其他清理工作
		// btnClear.setEnabled(true);
//		drawListener.ptStart = null;
//		drawListener.ptPrevious = null;
//		drawListener.points.clear();
//		drawListener.tempPolygon = null;
	}
	private String getAreaString(double dValue) {
		long area = Math.abs(Math.round(dValue));
		String sArea = "";
		// 顺时针绘制多边形，面积为正，逆时针绘制，则面积为负
		if (area >= 1000000) {
			double dArea = area / 1000000.0;
			sArea = Double.toString(dArea) + " 平方公里";
		} else
			sArea = Double.toString(area) + " 平方米";

		return sArea;
	}


	public void queryAttribute(Geometry geometry) {
		
		IdentifyParameters mIdentifyParameters = new IdentifyParameters();
		  mIdentifyParameters.setTolerance(20);
		  mIdentifyParameters.setDPI(98);
		  mIdentifyParameters.setLayers(new int[]{0,1,2,3,4,5,6,7}); 
		  mIdentifyParameters.setLayerMode(IdentifyParameters.TOP_MOST_LAYER); 

		  mIdentifyParameters.setGeometry(geometry);
		  mIdentifyParameters.setSpatialReference(mapView.getSpatialReference());         
		  mIdentifyParameters.setMapHeight(mapView.getHeight());
		  mIdentifyParameters.setMapWidth(mapView.getWidth());
		  mIdentifyParameters.setMapExtent(new Envelope());
		  
//			SearchIdentifyTask task = new SearchIdentifyTask(mapView.getContext(),mapView.getLayers()[0].getUrl(),
		SearchIdentifyTask task = new SearchIdentifyTask(mapView.getContext(), SinoApplication.currentLayerUrl,
					CommonData.TypeOperateFrameChoos);
		    task.execute(mIdentifyParameters); 

	      task.setFinishListener(new OnFinishListener() {
			
			@Override
			public void onFinish(ArrayList<IdentifyResult> resultList) {
				//把之前高亮显示结果清除
				mDrawLayer4HighLight.removeAll();
				StringBuilder sb = new StringBuilder();
				sb.append("查询到 " + resultList.size()+"  ");
				for (int i = 0; i < resultList.size(); i++) {
					IdentifyResult result = resultList.get(i);
//					Map<String, Object> attributes = result.getAttributes();
//					String name = (String) attributes.get("NAME_CN");
//					if(TextUtils.isEmpty(name)){
//						name = result.getValue().toString();
//					}
//					sb.append("名字： "+name + " ; ");
					drawHighLight(result);
				}
				deactivate();
				mCallback.setSearchData(resultList);
//				Toast.makeText(mapView.getContext(), sb.toString() , Toast.LENGTH_LONG).show();
			}
		});
		
	}
	
	
	public void queryAttribute4Query(Geometry geometry) {
//		Envelope m_WorldEnvelope = new Envelope();
//		m_WorldEnvelope = mapView.getMapBoundaryExtent();
		Query query = new Query();
		query.setGeometry(geometry);
		query.setReturnGeometry(true);
		query.setOutFields(new String[]{"OBJ_NAME_C"});
		//SpatialRelationship.CONTAINS: 框中整个盆地范围，才能查询到
		query.setSpatialRelationship(SpatialRelationship.CONTAINS);
//		Log.d("searchtask", "queryAttribute4Query......SpatialReference: " + query.getSpatialRelationship());
		query.setOutSpatialReference(mapView.getSpatialReference());
//		query.setWhere("NAME_CN='���ľ���' or NAME_CN='��������'");
		SearchQueryTask task = new SearchQueryTask(mapView.getContext(),
				SinoApplication.currentLayerUrl,
				CommonData.TypeOperateFrameChoos);
		task.execute(query);

		task.setQueryFinishListener(new OnQueryFinishListener() {

			@Override
			public void onFinish(FeatureSet results) {
				SinoApplication.mFeatureSet4Query = results;
				Graphic[] graphics = results.getGraphics();
				// 把之前高亮显示结果清除
				mDrawLayer4HighLight.removeAll();
				if(graphics != null){
					Log.d("test", "模糊查询  结果个数 : "+graphics.length+" "+results.getObjectIdFieldName());
//					for (Entry<String, Object> ent : results.getFieldAliases().entrySet()) {
//						Log.d("test", "00模糊查询  key: "+ent.getKey()+"  val: "+ent.getValue());
//					}
					for (int i = 0; i < graphics.length; i++) {
						Graphic graphic = graphics[i];
//						Log.d("test", "模糊查询  getAttributes 个数 : "+graphic.getAttributes().entrySet().size());
						for (Entry<String, Object> ent : graphic.getAttributes().entrySet()) {
							Log.d("test", "模糊查询  key: "+ent.getKey()+"  val: "+ent.getValue());
						}
	//					 String name = (String) result.getAttributes().get("NAME_CN");
	//					 sb.append("名字： "+name + " ; ");
						drawHighLight4Query(graphic);
					}
					deactivate();
					mCallback.setSearchData4Query(results);
					// Toast.makeText(mapView.getContext(), sb.toString() ,
					// Toast.LENGTH_LONG).show();
				}
			}

		});

	}
	
	//多选单点的时候
	public void queryAttribute4OnlyOnePonit(Geometry geometry) {
		IdentifyParameters mIdentifyParameters = new IdentifyParameters();
		mIdentifyParameters.setTolerance(20);
		mIdentifyParameters.setDPI(98);
		mIdentifyParameters.setLayers(new int[]{0,1,2,3,4,5,6,7}); 
		mIdentifyParameters.setLayerMode(IdentifyParameters.TOP_MOST_LAYER); 
		
		mIdentifyParameters.setGeometry(geometry);
		mIdentifyParameters.setSpatialReference(mapView.getSpatialReference());         
		mIdentifyParameters.setMapHeight(mapView.getHeight());
		mIdentifyParameters.setMapWidth(mapView.getWidth());
		mIdentifyParameters.setMapExtent(new Envelope());
		
		SearchIdentifyTask task = new SearchIdentifyTask(mapView.getContext(), SinoApplication.currentLayerUrl4Multi,
				CommonData.TypeOperateMulti);
		task.execute(mIdentifyParameters); 
		
		task.setFinishListener(new OnFinishListener() {
			
			@Override
			public void onFinish(ArrayList<IdentifyResult> resultList) {
				//把之前高亮显示结果清除
//				mDrawLayer4HighLight.removeAll();
				StringBuilder sb = new StringBuilder();
				sb.append("多选单点 " + resultList.size()+"  ");
				for (int i = 0; i < resultList.size(); i++) {
					IdentifyResult result = resultList.get(i);
//					drawHighLight(result);
					objectIsChecked(result);
				}
//				deactivate();
				SinoApplication.mResultList4Compared = SinoApplication.mResultListMulti;
				
//				Toast.makeText(mapView.getContext(), sb.toString() , Toast.LENGTH_LONG).show();
			}
		});
	}

	/**
	 * 绘制高亮区域
	 * @param result
	 */
	private void drawHighLight(IdentifyResult result) {
		if(result != null){
	        Geometry resultLocGeom = result.getGeometry();
	        // create marker symbol to represent location
	   	 	SimpleFillSymbol resultSymbol = new SimpleFillSymbol(Color.YELLOW);
	        // create graphic object for resulting location
	        Graphic resultLocation = new Graphic(resultLocGeom, resultSymbol);
	        // add graphic to location layer
	        Log.d("map", " drawHighLight ....uid: "+resultLocation.getUid());
	        mDrawLayer4HighLight.addGraphic(resultLocation);
	        // create text symbol for return address
//	        TextSymbol resultAddress = new TextSymbol(12, result.getAddress(), Color.BLACK);
//	        // create offset for text
//	        resultAddress.setOffsetX(10);
//	        resultAddress.setOffsetY(50);
	        // create a graphic object for address text
//	        Graphic resultText = new Graphic(resultLocGeom, resultAddress);
	        // add address text graphic to location graphics layer
//	        drawLayer.addGraphic(resultText);
	        // zoom to geocode result
//	        map.zoomToResolution(result.getLocation(), 2);
		}
	}
	
	/**
	 * 绘制高亮区域
	 * @param result
	 */
	private void drawHighLight4Query(Graphic result) {
		if(result != null){
			Geometry resultLocGeom = result.getGeometry();
			// create marker symbol to represent location
			SimpleFillSymbol resultSymbol = new SimpleFillSymbol(Color.YELLOW);
			// create graphic object for resulting location
			Graphic resultLocation = new Graphic(resultLocGeom, resultSymbol);
			// add graphic to location layer
			Log.d("map", " drawHighLight ....uid: "+resultLocation.getUid());
			mDrawLayer4HighLight.addGraphic(resultLocation);
			// create text symbol for return address
		}
	}
	
	private void drawHighLight4Multi(IdentifyResult result) {
		if(result != null){
			Geometry resultLocGeom = result.getGeometry();
			// create marker symbol to represent location
			SimpleFillSymbol resultSymbol = new SimpleFillSymbol(Color.YELLOW);
			// create graphic object for resulting location
			Graphic resultLocation = new Graphic(resultLocGeom, resultSymbol);
			// add graphic to location layer
			Integer uid = mDrawLayer4HighLight.addGraphic(resultLocation);
			Log.d("map", " drawHighLight ....uid: "+uid);
			SinoApplication.mResultMapMulti.put(result.getValue().toString(), uid);
		}
	}
	
	//TODO:remove不起作用 
	private void removeHighLight(IdentifyResult result) {
		if(result != null){
			Integer uid = SinoApplication.mResultMapMulti.get(result.getValue().toString());
			Log.d("map", " remove ....uid: "+uid);
			mDrawLayer4HighLight.removeGraphic(uid);
		}
	}
	
	private void objectIsChecked(IdentifyResult result) {
		boolean isCheck = false;
		for (int i = 0; i < SinoApplication.mResultList4Compared.size(); i++) {
			IdentifyResult temp = SinoApplication.mResultList4Compared.get(i);
			
			if(result.getValue().toString().equals(temp.getValue().toString())){
				SinoApplication.mResultList4Compared.remove(i);
				isCheck = true;
				break;
			}
		}
		Log.d("map", " 是否选中.... : "+isCheck+" size: "+SinoApplication.mResultList4Compared.size());
		if(isCheck){
			removeHighLight(result);
		}else{
			SinoApplication.mResultListMulti.add(result);
			drawHighLight4Multi(result);
		}
	}
	
}
