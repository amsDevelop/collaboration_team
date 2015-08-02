package com.sinopec.drawtool;

import java.math.BigDecimal;
import java.text.DecimalFormat;
import java.util.ArrayList;
import java.util.List;
import java.util.Map;
import java.util.Map.Entry;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.graphics.drawable.BitmapDrawable;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.esri.android.map.Callout;
import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
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
import com.esri.core.map.FeatureSet;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.PictureMarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;
import com.esri.core.tasks.SpatialRelationship;
import com.esri.core.tasks.ags.identify.IdentifyParameters;
import com.esri.core.tasks.ags.identify.IdentifyResult;
import com.esri.core.tasks.ags.query.Query;
import com.lenovo.nova.util.debug.mylog;
import com.sinopec.activity.R;
import com.sinopec.application.SinoApplication;
import com.sinopec.chart.BarChart3;
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
	private MultiPath poly;
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
	public boolean isSelectDraw = false;
	/**
	 * 多选
	 */
	public static final int MULTI_POINT = 9;

	private InterfaceDataCallBack mCallback;
	private ProgressDialog mProgressDialog;
	private Context mContext;

	public DrawTool(MapView mapView, InterfaceDataCallBack callback,
			Callout callout, Context context) {
		mProgressDialog = new ProgressDialog(context);
		this.mContext = context;
		this.mapView = mapView;
		this.mCallback = callback;
		this.tempLayer = new GraphicsLayer();
		// this.tempLayer.set
		this.mapView.addLayer(this.tempLayer);
		drawListener = new DrawTouchListener(this.mapView.getContext(),
				this.mapView);
		defaultListener = new MapOnTouchListener(this.mapView.getContext(),
				this.mapView);
		this.markerSymbol = new SimpleMarkerSymbol(Color.BLUE, 10,
				SimpleMarkerSymbol.STYLE.CIRCLE);

		// new SimpleMarkerSymbol(Color.BLUE, 10,
		// SimpleMarkerSymbol.STYLE.CIRCLE);
		// this.lineSymbol = new SimpleLineSymbol(Color.BLACK, 2);
		lineSymbol = new SimpleLineSymbol(Color.RED, 5,
				SimpleLineSymbol.STYLE.DASH);

		fillSymbol = new SimpleFillSymbol(Color.BLACK);
		fillSymbol.setOutline(new SimpleLineSymbol(Color.RED, 2));
		fillSymbol.setAlpha(100);

		// Log.v("mandy", "x:" + this.mapView.getLocationOnScreen(location));

		// Log.v("mandy", "y:" + this.mapView.getPivotY());
		// this.mapView.getPivotX();
	}

	public void setDrawLayer(GraphicsLayer drawLayer,
			GraphicsLayer drawLayer4HighLight) {
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
			// drawGraphic.setGeometry(this.point);
			// drawGraphic.setSymbol(this.markerSymbol);
			drawGraphic = new Graphic(this.point, markerSymbol);
			// drawGraphic.
			this.tempLayer.addGraphic(drawGraphic);
			break;
		case DrawTool.ENVELOPE:
			this.envelope = new Envelope();
			break;
		case DrawTool.POLYGON:
		case DrawTool.CIRCLE:
		case DrawTool.FREEHAND_POLYGON:
			this.polygon = new Polygon();
			// drawGraphic.setGeometry(this.polygon);
			// drawGraphic.setSymbol(fillSymbol);
			drawGraphic = new Graphic(this.polygon, fillSymbol);
			// drawGraphic.
			this.tempLayer.addGraphic(drawGraphic);

			break;
		case DrawTool.POLYLINE:
		case DrawTool.FREEHAND_POLYLINE:
			this.polyline = new Polyline();
			// drawGraphic.setGeometry(this.polyline);
			// drawGraphic.setSymbol(lineSymbol);
			break;
		}
	}

	public void deactivate() {
		// this.mapView.setOnTouchListener(defaultListener);
		this.tempLayer.removeAll();
		this.active = false;
		this.drawType = -1;
		this.point = null;
		// this.envelope = null;
		// this.polygon = null;
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
		// DrawEvent e = new DrawEvent(this, DrawEvent.DRAW_END,
		// DrawTool.this.drawGraphic);
		// DrawTool.this.notifyEvent(e);
		int type = this.drawType;
		this.deactivate();
		this.activate(type);
	}

	class DrawTouchListener extends MapOnTouchListener {

		private Point startPoint;
		private Point ptStart = null;// 起点
		private Point ptOnly = null;// 纪录一个点
		private Point ptPrevious = null;// 上一个点
		private ArrayList<Point> points = null;// 记录全部点
		private Polygon tempPolygon = null;// 记录绘制过程中的多边形
		private int uid = -1;

		public DrawTouchListener(Context context, MapView view) {
			super(context, view);
			points = new ArrayList<Point>();
		}

		public boolean onTouch(View view, MotionEvent event) {
			// if (mCallout.isShowing()) {
			// mCallout.hide();
			// }
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
					// startPoint = point;
					// envelope.setCoords(point.getX(), point.getY(),
					// point.getX(), point.getY());
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
							|| drawType == FREEHAND_POLYLINE
							|| drawType == CIRCLE || drawType == ANY_POLYGON)) {
				Point point = mapView.toMapPoint(to.getX(), to.getY());
				switch (drawType) {
				case DrawTool.ENVELOPE:

					if (uid == -1) { // first time
						drawLayer.removeAll();
						Graphic g = new Graphic(null, fillSymbol);
						startPoint = mapView.toMapPoint(from.getX(),
								from.getY());
						uid = drawLayer.addGraphic(g);

					} else {
						// gLayer.removeAll();
						Point p2 = mapView.toMapPoint(new Point(to.getX(), to
								.getY()));
						// Envelope envelope = new Envelope();
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
						uid = drawLayer.addGraphic(g);

					} else {
						getCircle(startPoint, radius, polygon);
						drawLayer.updateGraphic(uid, polygon);
					}

					break;
				case DrawTool.ANY_POLYGON:
					if (uid == -1) {
						drawLayer.removeAll();
						poly = new Polygon();
						startPoint = mapView.toMapPoint(from.getX(),
								from.getY());
						poly.startPath((float) startPoint.getX(),
								(float) startPoint.getY());

						poly.lineTo((float) startPoint.getX(),
								(float) startPoint.getY());
						// /*
						// * Create a Graphic and add polyline geometry
						// */
						Graphic graphic = new Graphic(poly, fillSymbol);
						//
						// /*
						// * add the updated graphic to graphics layer
						// */
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
			if (active
					&& (drawType == ENVELOPE || drawType == FREEHAND_POLYGON
							|| drawType == FREEHAND_POLYLINE
							|| drawType == CIRCLE || drawType == ANY_POLYGON)) {
				Point point = mapView.toMapPoint(to.getX(), to.getY());
				switch (drawType) {
				case DrawTool.ENVELOPE:
					
					queryAttribute4Query(envelope);
					
//					String sArea = getAreaString(traslationSpatial(envelope).calculateArea2D());
//					Toast.makeText(mapView.getContext(), sArea + "",s
//							Toast.LENGTH_SHORT).show();

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
//				sendDrawEndEvent();
				this.startPoint = null;
				uid = -1;
				return true;
			}
			return super.onDragPointerUp(from, to);
		}

		public boolean onSingleTap(MotionEvent point) {
			// Point point = mapView.toMapPoint(event.getX(), event.getY());
			// clearGraphic();
			if (active
					&& (drawType == POLYGON || drawType == POLYLINE || drawType == MULTI_POINT)) {
				if (ptStart == null)
					drawLayer.removeAll();// 第一次开始前，清空全部graphic

				Point ptCurrent = mapView.toMapPoint(new Point(point.getX(),
						point.getY()));
				points.add(ptCurrent);// 将当前点加入点集合中
				if (drawType == MULTI_POINT) {
					// 多选
					// Log.d("map",
					// "------点选-多选 ------SingleTap--x:"+point.getX()+"  y: "+point.getY());
					ptOnly = null;
					ptOnly = mapView.toMapPoint(point.getX(), point.getY());

					drawLayer.removeAll();
					queryAttribute4OnlyOnePonit(ptOnly);
				} else {
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

							double len = GeometryEngine.geodesicLength(
									polyline, mapView.getSpatialReference(),
									null);
							String length = "";
							DecimalFormat df = new DecimalFormat("#.00");
							double temp = BigDecimal.valueOf(len)
									.divide(new BigDecimal(1000)).doubleValue();
							length = df.format(temp) + " 千米";
							Toast.makeText(mapView.getContext(),
									"长度： " + length, Toast.LENGTH_SHORT).show();
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
				// tempLayer.postInvalidate();
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

			if (drawType == DrawTool.POLYGON) {
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
				// queryAttribute(polygon);
				queryAttribute4Query(polygon);
				return true;

			}
			return super.onDoubleTap(event);
		}

		@Override
		public void onLongPress(MotionEvent point) {
			if (isSelectDraw) {
				return;
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
		
		// drawLayer.removeAll();
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
				// Log.d("map", "--for---折线长度："+line.calculateLength2D());
				polyline.addSegment(line, false);
			}

			Graphic g = new Graphic(polyline, lineSymbol);
			drawLayer.addGraphic(g);

			double len = 0.0;

			// Log.d("map", "-----折线长度："+polyline.calculateLength2D());
			String length = "";
			DecimalFormat df = new DecimalFormat("#.00");
			if ("KM".equals(tag)) {
				len = 0.0;
				len = GeometryEngine.geodesicLength(polyline, mapView
						.getSpatialReference(), new LinearUnit(
						LinearUnit.Code.KILOMETER));
				length = len + " 千米";
			} else if ("M".equals(tag)) {
				len = 0.0;
				len = GeometryEngine.geodesicLength(polyline, mapView
						.getSpatialReference(), new LinearUnit(
						LinearUnit.Code.METER));
				length = df.format(len) + " 米";
			}

			Toast.makeText(mapView.getContext(), "总长度： " + length,
					Toast.LENGTH_SHORT).show();
		} else if (drawType == DrawTool.POLYGON) {
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
			String sArea = getAreaString(traslationSpatial(polygon).calculateArea2D());
			// GeometryEngine.geodesicLength(polygon,
			// mapView.getSpatialReference(),
			// new LinearUnit(LinearUnit.Code.KILOMETER));

			// GeometryEngine.geodesicArea();
			Toast.makeText(mapView.getContext(), "总面积： " + sArea,
					Toast.LENGTH_SHORT).show();
		} else if (drawType == DrawTool.ENVELOPE) {

			String sArea = getAreaString(traslationSpatial(envelope).calculateArea2D());

			Toast.makeText(mapView.getContext(), "总面积： " + sArea,
					Toast.LENGTH_SHORT).show();
		} else if (drawType == DrawTool.CIRCLE ) {
			
			String sArea = getAreaString(traslationSpatial(polygon).calculateArea2D());

			Toast.makeText(mapView.getContext(), "总面积： " + sArea,
					Toast.LENGTH_SHORT).show();
			
		} else if (drawType == DrawTool.ANY_POLYGON) {
			
			String sArea = getAreaString(traslationSpatial(poly).calculateArea2D());

			Toast.makeText(mapView.getContext(), "总面积： " + sArea,
					Toast.LENGTH_SHORT).show();
			
		}

		// 其他清理工作
		// btnClear.setEnabled(true);
		// drawListener.ptStart = null;
		// drawListener.ptPrevious = null;
		// drawListener.points.clear();
		// drawListener.tempPolygon = null;
	}
    private Geometry traslationSpatial (Geometry geometry) {
    	
    	SpatialReference sr = mapView.getSpatialReference();
		SpatialReference webMercator = SpatialReference
				.create(102100);
//		Polygon polygon = null;
		if (geometry instanceof Polygon) {
			Polygon newPoly = (Polygon) GeometryEngine.project(
					geometry, sr, webMercator);
			return newPoly;
		} else {
			
			Envelope newPoly = (Envelope) GeometryEngine.project(
					geometry, sr, webMercator);
			return newPoly;
		}
		
    	
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
		mIdentifyParameters.setLayers(new int[] { 0, 1, 2, 3, 4, 5, 6, 7 });
		mIdentifyParameters.setLayerMode(IdentifyParameters.TOP_MOST_LAYER);

		mIdentifyParameters.setGeometry(geometry);
		mIdentifyParameters.setSpatialReference(mapView.getSpatialReference());
		mIdentifyParameters.setMapHeight(mapView.getHeight());
		mIdentifyParameters.setMapWidth(mapView.getWidth());
		mIdentifyParameters.setMapExtent(new Envelope());

		// SearchIdentifyTask task = new
		// SearchIdentifyTask(mapView.getContext(),mapView.getLayers()[0].getUrl(),
		SearchIdentifyTask task = new SearchIdentifyTask(mapView.getContext(),
				SinoApplication.currentLayerUrl,
				CommonData.TypeOperateFrameChoos);
		task.execute(mIdentifyParameters);

		task.setFinishListener(new OnFinishListener() {

			@Override
			public void onFinish(ArrayList<IdentifyResult> resultList) {
				// 把之前高亮显示结果清除
				mDrawLayer4HighLight.removeAll();
				StringBuilder sb = new StringBuilder();
				sb.append("查询到 " + resultList.size() + "  ");
				for (int i = 0; i < resultList.size(); i++) {
					IdentifyResult result = resultList.get(i);
					// Map<String, Object> attributes = result.getAttributes();
					// String name = (String) attributes.get("NAME_CN");
					// if(TextUtils.isEmpty(name)){
					// name = result.getValue().toString();
					// }
					// sb.append("名字： "+name + " ; ");
					drawHighLight(result);
				}
				deactivate();
				mCallback.setSearchData(resultList);
				// Toast.makeText(mapView.getContext(), sb.toString() ,
				// Toast.LENGTH_LONG).show();
			}
		});

	}
	//框选查询调用
	public void queryAttribute4Query(Geometry geometry) {
		// Envelope m_WorldEnvelope = new Envelope();
		// m_WorldEnvelope = mapView.getMapBoundaryExtent();
		Query query = new Query();
		query.setGeometry(geometry);
		query.setReturnGeometry(true);
		query.setOutFields(new String[] { "OBJ_NAME_C", "OBJ_ID" });
		// SpatialRelationship.CONTAINS: 框中整个盆地范围，才能查询到
		query.setSpatialRelationship(SpatialRelationship.CONTAINS);
		// Log.d("searchtask", "queryAttribute4Query......SpatialReference: " +
		// query.getSpatialRelationship());
		query.setOutSpatialReference(mapView.getSpatialReference());
		// query.setWhere("NAME_CN='���ľ���' or NAME_CN='��������'");
		SearchQueryTask task = new SearchQueryTask(mapView.getContext(),
				SinoApplication.currentLayerUrl,
				CommonData.TypeOperateFrameChoos, mProgressDialog);
		
		Log.v("mandy", "current url: " + SinoApplication.currentLayerUrl);
		task.execute(query);

		task.setQueryFinishListener(new OnQueryFinishListener() {

			@Override
			public void onFinish(FeatureSet results) {
				SinoApplication.mFeatureSet4Query = results;
				Graphic[] graphics = results.getGraphics();
				// 把之前高亮显示结果清除
				mDrawLayer4HighLight.removeAll();
				if (graphics != null) {
					Log.d("test", "模糊查询  结果个数 : " + graphics.length + " "
							+ results.getObjectIdFieldName());
					// for (Entry<String, Object> ent :
					// results.getFieldAliases().entrySet()) {
					// Log.d("test",
					// "00模糊查询  key: "+ent.getKey()+"  val: "+ent.getValue());
					// }
					for (int i = 0; i < graphics.length; i++) {
						Graphic graphic = graphics[i];
						// Log.d("test",
						// "模糊查询  getAttributes 个数 : "+graphic.getAttributes().entrySet().size());
						for (Entry<String, Object> ent : graphic
								.getAttributes().entrySet()) {
							Log.d("test", "模糊查询  key: " + ent.getKey()
									+ "  val: " + ent.getValue());
						}
						// String name = (String)
						// result.getAttributes().get("NAME_CN");
						// sb.append("名字： "+name + " ; ");
						drawHighLight4Query(graphic);
					}
					mProgressDialog.dismiss();
//					deactivate();
					mCallback.setSearchData4Query(results);
					// Toast.makeText(mapView.getContext(), sb.toString() ,
					// Toast.LENGTH_LONG).show();
				}
			}

		});

	}
    //SELECT DP01. DZDYBM
	//FROM DP01
	//WHERE BITAND(DP01. CJTX, 34537472)>=1  AND DP01.GZDYJBBH=1
	
	
	public void queryAttribute4Query(String objId, String layerUrl,
			final ArrayList resultList) {
		
	
		// Envelope m_WorldEnvelope = new Envelope();
		// m_WorldEnvelope = mapView.getMapBoundaryExtent();
		Query query = new Query();
		// query.setGeometry(geometry);
		// query.setReturnGeometry(true);
		query.setOutFields(new String[] { "OBJECTID", "NAME","OBJ_NAME_C" });
		// SpatialRelationship.CONTAINS: 框中整个盆地范围，才能查询到
		query.setSpatialRelationship(SpatialRelationship.CONTAINS);
		// Log.d("searchtask", "queryAttribute4Query......SpatialReference: " +
		// query.getSpatialRelationship());
		query.setOutSpatialReference(mapView.getSpatialReference());
		query.setWhere(objId);

		Log.v("mandy", "objId: " + objId);

		SearchQueryTask task = new SearchQueryTask(mapView.getContext(),
				layerUrl, CommonData.TypeOperateFrameChoos, mProgressDialog);
		task.execute(query);

		task.setQueryFinishListener(new OnQueryFinishListener() {

			@Override
			public void onFinish(FeatureSet results) {
				SinoApplication.mFeatureSet4Query = results;
				Graphic[] graphics = results.getGraphics();
				
				DrawTool.this.graphics = graphics;
				
				// 把之前高亮显示结果清除
				mDrawLayer4HighLight.removeAll();
				if (graphics != null) {
					Log.d("mandy", "模糊查询  结果个数 : " + graphics.length + " "
							+ results.getDisplayFieldName());
					
					for (int j = 0; j < graphics.length; j++) {
						drawHighLight4Query(graphics[j], Color.RED);
                        mylog.i("DrawTool"," " +graphics[j].getAttributes());
                    }
					
					zoomExtent(graphics);
					mProgressDialog.dismiss();
					// deactivate();
					// mCallback.setSearchData4Query(results);
					// Toast.makeText(mapView.getContext(), sb.toString() ,
					// Toast.LENGTH_LONG).show();
				}
			}

		});

	}

    public void queryAttribute4Query(String objId, String layerUrl,
                                     final ArrayList resultList, final Handler handler) {


        // Envelope m_WorldEnvelope = new Envelope();
        // m_WorldEnvelope = mapView.getMapBoundaryExtent();
        Query query = new Query();
        // query.setGeometry(geometry);
        // query.setReturnGeometry(true);
        query.setOutFields(new String[] { "OBJECTID", "NAME","OBJ_NAME_C" });
        // SpatialRelationship.CONTAINS: 框中整个盆地范围，才能查询到
        query.setSpatialRelationship(SpatialRelationship.CONTAINS);
        // Log.d("searchtask", "queryAttribute4Query......SpatialReference: " +
        // query.getSpatialRelationship());
        query.setOutSpatialReference(mapView.getSpatialReference());
        query.setWhere(objId);

        Log.v("mandy", "objId: " + objId);

        SearchQueryTask task = new SearchQueryTask(mapView.getContext(),
                layerUrl, CommonData.TypeOperateFrameChoos, mProgressDialog);
        task.execute(query);

        task.setQueryFinishListener(new OnQueryFinishListener() {

            @Override
            public void onFinish(FeatureSet results) {


                SinoApplication.mFeatureSet4Query = results;
                Graphic[] graphics = results.getGraphics();


                Message.obtain(handler,0,graphics).sendToTarget();

                DrawTool.this.graphics = graphics;

                // 把之前高亮显示结果清除
                mDrawLayer4HighLight.removeAll();
                if (graphics != null) {
                    Log.d("mandy", "模糊查询  结果个数 : " + graphics.length + " "
                            + results.getDisplayFieldName());

                    for (int j = 0; j < graphics.length; j++) {
                        drawHighLight4Query(graphics[j], Color.RED);
                        mylog.i("DrawTool"," " +graphics[j].getAttributes());
                    }

                    zoomExtent(graphics);
                    mProgressDialog.dismiss();
                    // deactivate();
                    // mCallback.setSearchData4Query(results);
                    // Toast.makeText(mapView.getContext(), sb.toString() ,
                    // Toast.LENGTH_LONG).show();
                }
            }

        });

    }


	public void queryAttribute4Query1(String objId, String layerUrl,String[] result
			) {
		Query query = new Query();
		query.setOutFields(result);
		query.setSpatialRelationship(SpatialRelationship.CONTAINS);
		query.setOutSpatialReference(mapView.getSpatialReference());
		query.setWhere(objId);
		Log.v("mandy", "objId: " + objId);

		SearchQueryTask task = new SearchQueryTask(mapView.getContext(),
				layerUrl, CommonData.TypeOperateFrameChoos, mProgressDialog);
		task.execute(query);
		
		final String attribute = result[0];

		task.setQueryFinishListener(new OnQueryFinishListener() {

			@Override
			public void onFinish(FeatureSet results) {
				SinoApplication.mFeatureSet4Query = results;
				Graphic[] graphics = results.getGraphics();
				
				DrawTool.this.graphics = graphics;
				java.text.DecimalFormat  df   =new  java.text.DecimalFormat("#0.00");  
			
				// 把之前高亮显示结果清除
				mDrawLayer4HighLight.removeAll();
				if (graphics != null) {
					Log.d("mandy", "模糊查询  结果个数 : " + graphics.length);
				
					for (int j = 0; j < graphics.length; j++) {
						int color = Color.RED;
						if (attribute.equals("*")) {
							drawHighLight4Query(graphics[j], color);
						} else {
						Double ff = (Double)graphics[j].getAttributeValue(attribute);
						ff =  Double.valueOf(df.format(ff));
						if (ff >= 0.0 && ff <= 0.25) {
							color = mContext.getResources().getColor(R.color.fire_brick);
						} else if (ff > 0.25 && ff<= 0.5) {
							color = mContext.getResources().getColor(R.color.orange2);
						} else if (ff > 0.5 && ff <= 0.75) {
							color = mContext.getResources().getColor(R.color.yellow2);
						} else if (ff > 0.75 && ff <= 1.0) {
							color = mContext.getResources().getColor(R.color.green);
						}else {
							Log.v("mandy", "red: " + ff);
						}
						
						drawHighLight4Query(graphics[j], color);
						}
					}
					
					zoomExtent(graphics);
					mProgressDialog.dismiss();
					// deactivate();
					// mCallback.setSearchData4Query(results);
					// Toast.makeText(mapView.getContext(), sb.toString() ,
					// Toast.LENGTH_LONG).show();
				}
			}

		});

	}
	public void queryAttribute4Query2(String objId, String layerUrl,String[] result
			) {
		Query query = new Query();
		query.setOutFields(result);
		query.setSpatialRelationship(SpatialRelationship.CONTAINS);
		query.setOutSpatialReference(mapView.getSpatialReference());
		query.setWhere(objId);
		Log.v("mandy", "objId: " + objId);

		SearchQueryTask task = new SearchQueryTask(mapView.getContext(),
				layerUrl, CommonData.TypeOperateFrameChoos, mProgressDialog);
		task.execute(query);
		
		final String attribute = result[0];

		task.setQueryFinishListener(new OnQueryFinishListener() {

			@Override
			public void onFinish(FeatureSet results) {
				SinoApplication.mFeatureSet4Query = results;
				Graphic[] graphics = results.getGraphics();
				
				DrawTool.this.graphics = graphics;
				java.text.DecimalFormat  df   =new  java.text.DecimalFormat("#0.00");  
			
				// 把之前高亮显示结果清除
				mDrawLayer4HighLight.removeAll();
				if (graphics != null) {
					Log.d("mandy", "模糊查询  结果个数 : " + graphics.length);
				
					for (int j = 0; j < graphics.length; j++) {
						
//						Double ff = (Double)graphics[j].getAttributeValue(attribute);
					    Map<String, Object> map =graphics[j].getAttributes();
					    for (Map.Entry<String, Object> string : map.entrySet()) {
							Log.v("mandy", "key: " + string.getKey() + " value: " + string.getValue());
						}
					    
					    Envelope envelope = new Envelope();
						graphics[j].getGeometry().queryEnvelope(envelope);
						BarChart3 b3 = new BarChart3((Double)graphics[j].getAttributeValue("CR_KWNOIL"),
								(Double)graphics[j].getAttributeValue("CR_KWNGAS"), (Double)graphics[j].getAttributeValue("CR_KWNNGL"), 100, 100);
						Bitmap bi = b3.GetBarChartBitmap(mContext);
						PictureMarkerSymbol Symbol = new PictureMarkerSymbol(
								new BitmapDrawable(bi));
					

						Graphic graphic = new Graphic(envelope.getCenter(), Symbol);
						// Graphic graphic = new Graphic(new Point(0,0),Symbol);
						drawLayer.addGraphic(graphic);
					    
						bi.recycle();
					    Log.v("mandy", "finish..................");
					    
						
//						drawHighLight4Query(graphics[j], color);
//						}
					}
					
					zoomExtent(graphics);
					mProgressDialog.dismiss();
					// deactivate();
					// mCallback.setSearchData4Query(results);
					// Toast.makeText(mapView.getContext(), sb.toString() ,
					// Toast.LENGTH_LONG).show();
				}
			}

		});

	}
	
	double minx = -1;
	double miny = -1;
	double maxx = -1;
	double maxy = -1;
	double lastMinx = -1;
	double lastMiny = -1;
	
	double lastMaxx = -1;
	double lastMaxy = -1;
	public void zoomExtent (Graphic[] graphics) {
		Envelope envelopes = new Envelope();
		graphics[0].getGeometry().queryEnvelope(envelopes);
		lastMinx = envelopes.getXMin();
		lastMiny = envelopes.getYMin();
		
		for (int j = 0; j < graphics.length; j++) {
			Envelope envelope = new Envelope();
			graphics[j].getGeometry().queryEnvelope(envelope);
			minx = Math.min(lastMinx, envelope.getXMin());
			lastMinx = minx;
			
			miny = Math.min(lastMiny, envelope.getYMin());
			lastMiny = miny;
			
			maxx = Math.max(lastMaxx, envelope.getXMax());
			lastMaxx = maxx;
			
			maxy = Math.max(lastMaxy, envelope.getYMax());
			lastMaxy = maxy;
			
		}
		Envelope envelope = new Envelope();
		envelope.setYMin(miny);
		envelope.setXMin(minx);
		envelope.setYMax(maxy);
		envelope.setXMax(maxx);
		mapView.setExtent(envelope);
		
	}

	// 多选单点的时候
	public void queryAttribute4OnlyOnePonit(Geometry geometry) {
		IdentifyParameters mIdentifyParameters = new IdentifyParameters();
		mIdentifyParameters.setTolerance(20);
		mIdentifyParameters.setDPI(98);
		mIdentifyParameters.setLayers(new int[] { 0, 1, 2, 3, 4, 5, 6, 7,8,9});
		mIdentifyParameters.setLayerMode(IdentifyParameters.TOP_MOST_LAYER);

		mIdentifyParameters.setGeometry(geometry);
		mIdentifyParameters.setSpatialReference(mapView.getSpatialReference());
		mIdentifyParameters.setMapHeight(mapView.getHeight());
		mIdentifyParameters.setMapWidth(mapView.getWidth());
		mIdentifyParameters.setMapExtent(new Envelope());

		SearchIdentifyTask task = new SearchIdentifyTask(mapView.getContext(),
				SinoApplication.currentLayerUrl4Multi,
				CommonData.TypeOperateMulti);
		task.execute(mIdentifyParameters);

		task.setFinishListener(new OnFinishListener() {

			@Override
			public void onFinish(ArrayList<IdentifyResult> resultList) {
				// 把之前高亮显示结果清除
				// mDrawLayer4HighLight.removeAll();
				StringBuilder sb = new StringBuilder();
				sb.append("多选单点 " + resultList.size() + "  ");
				for (int i = 0; i < resultList.size(); i++) {
					IdentifyResult result = resultList.get(i);
					// drawHighLight(result);
					if (result != null) {
						if (result.getLayerName().equals(
//								SinoApplication.mLayerName)) {
								SinoApplication.mLayerNameEnTag)) {
							objectIsChecked(result);
						} else {
							Toast.makeText(
									mContext,
									mContext.getString(R.string.search_no_result),
									Toast.LENGTH_SHORT).show();
						}
					} else {
						Toast.makeText(mContext,
								mContext.getString(R.string.search_no_result),
								Toast.LENGTH_SHORT).show();
					}
				}
				// deactivate();
				SinoApplication.mResultList4Compared = SinoApplication.mResultListMulti;

				// Toast.makeText(mapView.getContext(), sb.toString() ,
				// Toast.LENGTH_LONG).show();
			}
		});
	}

	/**
	 * 绘制高亮区域
	 * 
	 * @param result
	 */
	private void drawHighLight(IdentifyResult result) {
		if (result != null) {
			Geometry resultLocGeom = result.getGeometry();
			// create marker symbol to represent location
			SimpleFillSymbol resultSymbol = new SimpleFillSymbol(Color.YELLOW);
			// create graphic object for resulting location
			Graphic resultLocation = new Graphic(resultLocGeom, resultSymbol);
			
			// add graphic to location layer
			Log.d("map", " drawHighLight ....uid: " + resultLocation.getUid());
			mDrawLayer4HighLight.addGraphic(resultLocation);
			
		}
	}

	/**
	 * 绘制高亮区域
	 * 
	 * @param result
	 */
	private void drawHighLight4Query(Graphic result) {
		if (result != null) {
			Geometry resultLocGeom = result.getGeometry();
			// create marker symbol to represent location
			SimpleFillSymbol resultSymbol = new SimpleFillSymbol(Color.YELLOW);
			// create graphic object for resulting location
			Graphic resultLocation = new Graphic(resultLocGeom, resultSymbol);
			// add graphic to location layer
			Log.d("map", " drawHighLight ....uid: " + resultLocation.getUid());
			mDrawLayer4HighLight.addGraphic(resultLocation);
			
			// Envelope envelope new Envelope();
			// mapView.centerAt(new
			// Point(resultLocGeom.q.queryEnvelope(envelope)), true);
			// create text symbol for return address
		}
	}

	/**
	 * 绘制高亮区域
	 * 
	 * @param result
	 */
	private void drawHighLight4Query(Graphic result, int color) {
		if (result != null) {
			Geometry resultLocGeom = result.getGeometry();
			// create marker symbol to represent location
			SimpleFillSymbol resultSymbol = new SimpleFillSymbol(color);
			// create graphic object for resulting location
			Graphic resultLocation = new Graphic(resultLocGeom, resultSymbol);
			// add graphic to location layer
			mDrawLayer4HighLight.addGraphic(resultLocation);
		}
	}
	
	private void drawHighLight4Multi(IdentifyResult result) {
		if (result != null) {
			Geometry resultLocGeom = result.getGeometry();
			// create marker symbol to represent location
			SimpleFillSymbol resultSymbol = new SimpleFillSymbol(Color.YELLOW);
			// create graphic object for resulting location
			Graphic resultLocation = new Graphic(resultLocGeom, resultSymbol);
			// add graphic to location layer
			Integer uid = mDrawLayer4HighLight.addGraphic(resultLocation);
			Log.d("map", " drawHighLight ....uid: " + uid);
			SinoApplication.mResultMapMulti.put(result.getValue().toString(),
					uid);
		}
	}

	// TODO:remove不起作用
	private void removeHighLight(IdentifyResult result) {
		if (result != null) {
			Integer uid = SinoApplication.mResultMapMulti.get(result.getValue()
					.toString());
			Log.d("map", " remove ....uid: " + uid);
			if(uid != null){
				mDrawLayer4HighLight.removeGraphic(uid);
			}
		}
	}

	private void objectIsChecked(IdentifyResult result) {
		boolean isCheck = false;
		String resultName = (String) result.getAttributes().get("OBJ_NAME_C");
		for (int i = 0; i < SinoApplication.mResultListMulti.size(); i++) {
			IdentifyResult temp = SinoApplication.mResultListMulti.get(i);
			String tempName = (String) temp.getAttributes().get("OBJ_NAME_C");
			if (resultName.equals(tempName)) {
				SinoApplication.mResultListMulti.remove(i);
				isCheck = true;
				break;
			}
		}
		Log.d("map", " 是否选中.... : " + isCheck + " size: "
				+ SinoApplication.mResultListMulti.size());
		if (isCheck) {
			removeHighLight(result);
		} else {
			SinoApplication.mResultListMulti.add(result);
			drawHighLight4Multi(result);
		}
	}
  private Graphic[] graphics = null; 
  public Graphic[] getQueryGraphics () {
	  
	   return graphics;
  }	


}
