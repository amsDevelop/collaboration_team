package com.sinopec.drawtool;

import java.util.ArrayList;

import android.content.Context;
import android.graphics.Color;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import com.esri.android.map.GraphicsLayer;
import com.esri.android.map.MapOnTouchListener;
import com.esri.android.map.MapView;
import com.esri.android.map.event.OnSingleTapListener;
import com.esri.core.geometry.Envelope;
import com.esri.core.geometry.Line;
import com.esri.core.geometry.MultiPath;
import com.esri.core.geometry.Point;
import com.esri.core.geometry.Polygon;
import com.esri.core.geometry.Polyline;
import com.esri.core.geometry.Segment;
import com.esri.core.map.Graphic;
import com.esri.core.symbol.FillSymbol;
import com.esri.core.symbol.LineSymbol;
import com.esri.core.symbol.MarkerSymbol;
import com.esri.core.symbol.SimpleFillSymbol;
import com.esri.core.symbol.SimpleLineSymbol;
import com.esri.core.symbol.SimpleMarkerSymbol;

/**
 * 
 * @author ropp gispace@yeah.net
 * 
 *         ��ͼʵ���֧࣬�ֻ��㡢���Ρ��ߡ�����Ρ�Բ���ֻ��ߡ��ֻ�����Σ������ø���ͼ�ε�symbol��
 */
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

	public static final int POINT = 1;
	public static final int ENVELOPE = 2;
	public static final int POLYLINE = 3;
	public static final int POLYGON = 4;
	public static final int CIRCLE = 5;
	public static final int ANY_POLYGON = 6;
	public static final int FREEHAND_POLYGON = 7;
	public static final int FREEHAND_POLYLINE = 8;

	private static final int TEMP_LAYER_ID = -999999;

	public DrawTool(MapView mapView) {
		this.mapView = mapView;
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
		
		this.fillSymbol = new SimpleFillSymbol(Color.BLACK);
		this.fillSymbol.setAlpha(90);
		
	
		
//		Log.v("mandy", "x:" + this.mapView.getLocationOnScreen(location));
		
//		Log.v("mandy", "y:" + this.mapView.getPivotY());
//		this.mapView.getPivotX();
	}
    
	
	public void setDrawLayer(GraphicsLayer drawLayer) {
		this.drawLayer = drawLayer;
	}


	public void activate(int drawType) {
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
		   tempLayer.removeAll();
			this.envelope = new Envelope();
//			drawGraphic.setGeometry(this.envelope);
//			drawGraphic.setSymbol(fillSymbol);
//			
			drawGraphic = new Graphic(this.envelope, fillSymbol);
			
////			drawGraphic.
			this.tempLayer.addGraphic(drawGraphic);
			
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
		this.mapView.setOnTouchListener(defaultListener);
		this.tempLayer.removeAll();
		this.active = false;
		this.drawType = -1;
		this.point = null;
		this.envelope = null;
		this.polygon = null;
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
		
		DrawEvent e = new DrawEvent(this, DrawEvent.DRAW_END,
				DrawTool.this.drawGraphic);
		DrawTool.this.notifyEvent(e);
		if (envelope != null) {
		String sArea = getAreaString(envelope.calculateArea2D());

		Toast.makeText(mapView.getContext(), "总面积： " + sArea,
				Toast.LENGTH_SHORT).show();
		}
		int type = this.drawType;
		this.deactivate();
		this.activate(type);
	}

	class DrawTouchListener extends MapOnTouchListener {

		private Point startPoint;
		private Point ptStart = null;// 起点
		private Point ptPrevious = null;// 上一个点
		private ArrayList<Point> points = null;// 记录全部点
		private Polygon tempPolygon = null;// 记录绘制过程中的多边形
		MultiPath poly;

		public DrawTouchListener(Context context, MapView view) {
			super(context, view);
			points = new ArrayList<Point>();
		}

		public boolean onTouch(View view, MotionEvent event) {
			
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
					Log.v("mandy", "envelop..............");
					startPoint = point;
					envelope.setCoords(point.getX(), point.getY(),
							point.getX(), point.getY());
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
				Log.v("mandy", "onDragPointerMove move....");
				switch (drawType) {
				case DrawTool.ENVELOPE:
					envelope.setXMin(startPoint.getX() > point.getX() ? point
							.getX() : startPoint.getX());
					envelope.setYMin(startPoint.getY() > point.getY() ? point
							.getY() : startPoint.getY());
					envelope.setXMax(startPoint.getX() < point.getX() ? point
							.getX() : startPoint.getX());
					envelope.setYMax(startPoint.getY() < point.getY() ? point
							.getY() : startPoint.getY());
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
					Point hh = mapView.toMapPoint(((float)(startPoint.getX() + point.getX()/2)), (float)(startPoint.getY() + point.getY()/2));
					
					getCircle(hh, radius, polygon);
					break;
				case DrawTool.ANY_POLYGON:
					Log.v("mandy", "any polygone.....");
					if (startPoint == null) {
						drawLayer.removeAll();
						// poly = type.equalsIgnoreCase("POLYLINE") ? new Polyline()
						// : new Polygon();
						poly = new Polygon();
						startPoint = mapView.toMapPoint(from.getX(), from.getY());
						poly.startPath((float) startPoint.getX(),
								(float) startPoint.getY());

						/*
						 * Create a Graphic and add polyline geometry
						 */
						Graphic graphic = new Graphic(startPoint, fillSymbol);

						/*
						 * add the updated graphic to graphics layer
						 */
						drawLayer.addGraphic(graphic);
					}

					poly.lineTo((float) point.getX(), (float) point.getY());
					break;
					
				}
//				tempLayer.postInvalidate();
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
//					DrawEvent e = new DrawEvent();
					DrawTool.this.notifyClear();
					Log.v("mandy", "startposition: " + startPoint);
					envelope.setXMin(startPoint.getX() > point.getX() ? point
							.getX() : startPoint.getX());
					envelope.setYMin(startPoint.getY() > point.getY() ? point
							.getY() : startPoint.getY());
					envelope.setXMax(startPoint.getX() < point.getX() ? point
							.getX() : startPoint.getX());
					envelope.setYMax(startPoint.getY() < point.getY() ? point
							.getY() : startPoint.getY());
					break;
				case DrawTool.FREEHAND_POLYGON:
					polygon.lineTo(point);
					break;
				case DrawTool.FREEHAND_POLYLINE:
					polyline.lineTo(point);
					break;
				case DrawTool.CIRCLE:
					DrawTool.this.notifyClear();
					double radius = Math.sqrt(Math.pow(startPoint.getX()
							- point.getX(), 2)
							+ Math.pow(startPoint.getY() - point.getY(), 2));
					
					getCircle(startPoint, radius, polygon);
					break;
				case DrawTool.ANY_POLYGON:
					poly.lineTo((float) startPoint.getX(),
							(float) startPoint.getY());
					drawLayer.removeAll();
					drawLayer.addGraphic(new Graphic(poly, fillSymbol));
					// points.add(startPoint);
					startPoint = null;
					break;
				}
				sendDrawEndEvent();
				this.startPoint = null;
				return true;
			}
			return super.onDragPointerUp(from, to);
		}

		public boolean onSingleTap(MotionEvent point) {
//			Point point = mapView.toMapPoint(event.getX(), event.getY());
//			 clearGraphic();
			if (active && (drawType==POLYGON || drawType==POLYLINE)) {
				if (ptStart == null)
					drawLayer.removeAll();// 第一次开始前，清空全部graphic
				
				Point ptCurrent = mapView.toMapPoint(new Point(point.getX(), point
						.getY()));
				points.add(ptCurrent);// 将当前点加入点集合中

				if (ptStart == null) {// 画线或多边形的第一个点
					ptStart = ptCurrent;

					// 绘制第一个点
					Graphic graphic = new Graphic(ptStart, markerSymbol);
					drawLayer.addGraphic(graphic);
				} else {// 画线或多边形的其他点
						// 绘制其他点
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

						// 计算当前线段的长度
						String length = Double.toString(Math.round(line
								.calculateLength2D())) + " 米";

						Toast.makeText(mapView.getContext(), "长度： " + length,
								Toast.LENGTH_SHORT).show();
					} else {
//						tempPolygon = null;
						// 绘制临时多边形
						if (tempPolygon == null)
							tempPolygon = new Polygon();
						tempPolygon.addSegment(line, false);

						drawLayer.removeAll();
						Graphic g = new Graphic(tempPolygon, fillSymbol);
						drawLayer.addGraphic(g);
//
//						// 计算当前面积
//						String sArea = getAreaString(tempPolygon
//								.calculateArea2D());
//
//						Toast.makeText(map.getContext(), "面积： " + sArea,
//								Toast.LENGTH_SHORT).show();
					}
				}

				ptPrevious = ptCurrent;
//				tempLayer.postInvalidate();
				return true;
			}
			return false;
		}

		private void clearGraphic() {

			ptStart = null;
			ptPrevious = null;
			points.clear();
			tempPolygon = null;

		}
		public boolean onDoubleTap(MotionEvent event) {
//			Point point = mapView.toMapPoint(event.getX(), event.getY());
//			if (active &&(drawType==POLYGON || drawType==POLYLINE)) {
//				switch (drawType) {
//				case DrawTool.POLYGON:
//					polygon.lineTo(point);
//					break;
//				case DrawTool.POLYLINE:
//					polyline.lineTo(point);
//					break;
//				}
//				sendDrawEndEvent();
//				this.startPoint = null;
//			}
			return super.onDoubleTap(event);
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
	public void calculateAreaAndLength() {
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

				polyline.addSegment(line, false);
			}

			Graphic g = new Graphic(polyline, lineSymbol);
			drawLayer.addGraphic(g);

			// 计算总长度
			String length = Double.toString(Math.round(polyline
					.calculateLength2D())) + " 米";

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

			Toast.makeText(mapView.getContext(), "总面积： " + sArea,
					Toast.LENGTH_SHORT).show();
		} else if (drawType == DrawTool.ENVELOPE) {
		
			String sArea = getAreaString(envelope.calculateArea2D());

			Toast.makeText(mapView.getContext(), "总面积： " + sArea,
					Toast.LENGTH_SHORT).show();
		}

		// 其他清理工作
		// btnClear.setEnabled(true);
		drawListener.ptStart = null;
		drawListener.ptPrevious = null;
		drawListener.points.clear();
		drawListener.tempPolygon = null;
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

	
}
