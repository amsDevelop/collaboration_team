package com.sinopec.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.util.Log;
import android.widget.Button;

import com.sinopec.activity.R;
import com.sinopec.application.SinoApplication;
/**
 * 子菜单用的menubutton
 */
public class MenuChildButton extends Button {
	private Bitmap bitmap;
	private final int ICON_LEFT = 0;
	private final int ICON_RIGHT = 1;
	private final int ICON_ABOVE = 2;
	private final int ICON_BELOW = 3;
	private final int ICON_NULL = 4;
	
	
	private final int FRAME_LEFT = 5;
	private final int FRAME_RIGHT = 6;
	private final int FRAME_TOP = 7;
	private final int FRAME_BOTTOM = 8;
	private final int FRAME_NULL = 9;
	private final int FRAME_ALL = 10;
	/**
	 * 主体的图标
	 */
	private final int ICON_MAIN = 5;
	
	private int mRelation = ICON_LEFT;
	private int mFramePosition = FRAME_LEFT;
	private int mIconId;
	private int mTextSize;
	private String mText;
	private boolean mHasIcon;
	private int mSplitNum;
	private Context mContext;
	
	public MenuChildButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		this.mContext = context;
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.iconbutton);
		TypedArray b = context.obtainStyledAttributes(attrs,
				R.styleable.iconframe);
		mFramePosition = b.getInt(R.styleable.iconframe_frameposition, FRAME_LEFT);
		mRelation = a.getInt(R.styleable.iconbutton_relation, ICON_LEFT);
		mIconId = a.getResourceId(R.styleable.iconbutton_icon, R.drawable.icon);
		mText = a.getString(R.styleable.iconbutton_mtext);
		mTextSize = a.getDimensionPixelSize(R.styleable.iconbutton_text_size, 12);
		mHasIcon = a.getBoolean(R.styleable.iconbutton_hasicon, true);
		mSplitNum = a.getInt(R.styleable.iconbutton_split_number, 1);
		
//		bitmap = BitmapFactory.decodeResource(context.getResources(), mIconId);
//		setWidth(SinoApplication.screenWidth / mSplitNum);
	}
	
	public void setTitle(String title) {
		mText = title;
	}
	
	public void setIcon(int drawableid) {
		mIconId = drawableid;
		bitmap = BitmapFactory.decodeResource(mContext.getResources(), mIconId);
	}

	public void setSplitNumber(int nubmer) {
		mSplitNum = nubmer;
		setWidth(SinoApplication.screenWidth / mSplitNum);
//		setHeight(SinoApplication.screenWidth / mSplitNum);
	}

	private Paint mPaint = new Paint();
	@Override
	protected void onDraw(Canvas canvas) {
		
		if(mHasIcon){
			drawIcon(canvas);
		}else{
			drawNoIcon(canvas);
		}
		//线两头的边距
		int spaceVertical = 0;
		int spaceHorizon = 0;
		
		int lineWidth = 1;
		//左右
		int spaceApartLandR = 0;
		//上下
		int spaceApartTandB = 1;
		
		if(mFramePosition == FRAME_TOP){
			//top
			canvas.drawRect(spaceApartLandR + spaceHorizon, spaceApartTandB, getMeasuredWidth() - spaceApartLandR - spaceHorizon - lineWidth, 
					spaceApartTandB + lineWidth, mPaint);
		}
		
//		if(mFramePosition == FRAME_BOTTOM){
			//bottom
			canvas.drawRect(spaceApartLandR + spaceHorizon, getMeasuredHeight() - spaceApartTandB - lineWidth, getMeasuredWidth() - spaceApartLandR - spaceHorizon - lineWidth, 
					getMeasuredHeight() - spaceApartTandB, mPaint);
//		}
		
		if(mFramePosition == FRAME_LEFT){
			//left
			canvas.drawRect(spaceApartLandR, spaceVertical, spaceApartLandR + lineWidth, getMeasuredHeight() - spaceVertical, mPaint);
		}
		
//		if(mFramePosition == FRAME_RIGHT){
			//right
			canvas.drawRect(getMeasuredWidth() - spaceApartLandR - lineWidth, spaceVertical, getMeasuredWidth() - spaceApartLandR, getMeasuredHeight() - spaceVertical, mPaint);
//		}
		
		super.onDraw(canvas);
	}
	
	public void drawFrame() {
		
	}
	
	private void drawIcon(Canvas canvas){
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(mTextSize);
		int spaceIconAndText = 25;
		int	bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();
		//文字宽度( 字号sp 要* dpi 才是真正像素值)
//		int textWidth = (int) (getTextWidth() * SinoApplication.density);
		int textWidth = (int)getTextWidth();
		Log.d("sinopec", "textWidth is " + textWidth +"  " + mText +"  " + getTextWidth() +"  " + SinoApplication.density);
//		int textHeight =  (int) (mTextSize * SinoApplication.density);
		int textHeight = mTextSize;
//		Log.d("sinopec", "mW: "+getMeasuredWidth()+"   width:　"+textWidth+"   bitW:　"+bitmapWidth+"  文字: "+mText);
		
		int iconx = (getMeasuredWidth() - bitmapWidth) / 2;
		int icony = (getMeasuredHeight() - bitmapHeight - textHeight) / 2;
		
		//文字
		int textx = iconx + bitmapWidth + spaceIconAndText;
		int texty = (getMeasuredHeight() - textHeight) / 2;
		
		switch (mRelation) {
        case ICON_NULL:
            break;
        case ICON_ABOVE:
        	textx = (getMeasuredWidth() - textWidth) /2;
        	texty = icony + bitmapHeight + spaceIconAndText;
        	break;
        case ICON_BELOW:
            break;
        case ICON_LEFT:
            break;
        case ICON_RIGHT: 
            break;

		default:
			break;
		}
		
		canvas.drawBitmap(bitmap, iconx, icony, null);
//		Log.d("sinopec", "mH: "+getMeasuredHeight()+"   tH:　"+textHeight+"   bitH:　"+bitmapHeight+" texty: "+texty);
		canvas.drawText(mText, textx, texty, mPaint);
		Log.d("sinopec","mText is "+ mText+"  textx "+textx+"   texty "+texty +" iconx "+ iconx+
				"  mRelation  " + mRelation+" bitmapWidth "+ bitmapWidth +" getMeasuredWidth()  textWidth " + getMeasuredWidth() +"  " + textWidth);
	}

	private float getTextWidth() {
		return mPaint.measureText(mText);
	}
	
	private void drawNoIcon(Canvas canvas){
		//文字宽度( 字号sp 要* dpi 才是真正像素值)
		int textWidth = (int) (getTextWidth() * SinoApplication.density);
//		int textHeight =  (int) (mTextSize * SinoApplication.density);
		int textHeight = mTextSize;
//		Log.d("sinopec", "mW: "+getMeasuredWidth()+"   width:　"+textWidth+"   bitW:　"+bitmapWidth+"  文字: "+mText);
		
		//文字
		int textx = (getMeasuredWidth() - textWidth) / 2;
		int texty = (getMeasuredHeight() - textHeight) / 2;
		// 坐标需要转换，因为默认情况下Button中的文字居中显示
		// 这里需要让文字在底部显示
//		canvas.translate(0, (getMeasuredHeight() / 2) - (int) getTextSize());
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(mTextSize);
		
//		Log.d("sinopec", "mH: "+getMeasuredHeight()+"   tH:　"+textHeight+"   bitH:　"+bitmapHeight+" texty: "+texty);
		canvas.drawText(mText, textx, texty, mPaint);
		
	}

}
