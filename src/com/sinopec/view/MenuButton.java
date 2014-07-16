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

public class MenuButton extends Button {
	private Bitmap bitmap;
	private final int ICON_LEFT = 0;
	private final int ICON_RIGHT = 1;
	private final int ICON_ABOVE = 2;
	private final int ICON_BELOW = 3;
	private final int ICON_NULL = 4;
	/**
	 * 主体的图标
	 */
	private final int ICON_MAIN = 5;
	
	private int mRelation = ICON_LEFT;
	private int mIconId;
	private int mTextSize;
	private String mText;
	private boolean mHasIcon;
	private int mRealWidth = 0;
	public MenuButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.iconbutton);
		mRelation = a.getInt(R.styleable.iconbutton_relation, ICON_LEFT);
		mIconId = a.getResourceId(R.styleable.iconbutton_icon, R.drawable.icon);
		mText = a.getString(R.styleable.iconbutton_mtext);
		mTextSize = a.getDimensionPixelSize(R.styleable.iconbutton_text_size, 12);
		mHasIcon = a.getBoolean(R.styleable.iconbutton_hasicon, true);
		
		bitmap = BitmapFactory.decodeResource(context.getResources(), mIconId);
		
		mRealWidth = SinoApplication.screenWidth / 4;
		setWidth(mRealWidth);
	}

	private Paint mPaint = new Paint();
	@Override
	protected void onDraw(Canvas canvas) {
		if(mHasIcon){
			drawIcon(canvas);
		}else{
			drawNoIcon(canvas);
		}
		//画边框
		int spaceVertical = 15;
		switch (mRelation) {
        case ICON_NULL:
            break;
        case ICON_ABOVE:
        	break;
        case ICON_BELOW:
            break;
        case ICON_LEFT:
        	canvas.drawRect(1, spaceVertical, 2, getMeasuredHeight() - spaceVertical, mPaint);
            break;
        case ICON_RIGHT:
        	canvas.drawRect(getMeasuredWidth() - 5, spaceVertical, getMeasuredWidth() - 4, getMeasuredHeight() - spaceVertical, mPaint);
            break;

		default:
			break;
		}
		
		super.onDraw(canvas);
	}
	
	private void drawNoIcon(Canvas canvas){
		int spaceIconAndText = 3;
		int bitmapWidth = 0;
		int bitmapHeight = 0;
		if(mHasIcon){
			bitmapWidth = bitmap.getWidth();
			bitmapHeight = bitmap.getHeight();
		}
		
		//文字宽度( 字号sp 要* dpi 才是真正像素值)
		int textWidth = (int) (mPaint.measureText(mText) * SinoApplication.density);
//		int textHeight =  (int) (mTextSize * SinoApplication.density);
		int textHeight = mTextSize;
		
		int iconx = (getMeasuredWidth() - (bitmapWidth + spaceIconAndText + mTextSize)) / 2;
		int icony = (getMeasuredHeight() - bitmapHeight) / 2;
		if(mHasIcon){
			canvas.drawBitmap(bitmap, iconx, icony, null);
		}
		// 坐标需要转换，因为默认情况下Button中的文字居中显示
		// 这里需要让文字在底部显示
//		canvas.translate(0, (this.getMeasuredHeight() / 2)
//				- (int) getTextSize());
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(mTextSize);
		
		//文字
		int textx = 0;
		int texty = 0;
		if(mHasIcon){
			textx = iconx + bitmapWidth + spaceIconAndText;
			texty = (getMeasuredHeight() - textHeight) / 2;
		}else{
			textx = (getMeasuredWidth() - textWidth) / 2;
			texty = (getMeasuredHeight() - textHeight) / 2;
		}
		
		Log.d("sinopec", "mH: "+getMeasuredHeight()+"   tH:　"+textHeight+"   bitH:　"+bitmapHeight+" texty: "+texty);
		canvas.drawText(mText, textx, texty, mPaint);
		
	}
	
	private void drawIcon(Canvas canvas){
		int spaceIconAndText = 3;
		int bitmapWidth = bitmap.getWidth();
		int bitmapHeight = bitmap.getHeight();
		
		//文字宽度( 字号sp 要* dpi 才是真正像素值)
		int textWidth = (int) (mPaint.measureText(mText) * SinoApplication.density);
//		int textHeight =  (int) (mTextSize * SinoApplication.density);
		int textHeight = mTextSize;
		
		int iconx = (getMeasuredWidth() - (bitmapWidth + spaceIconAndText + mTextSize)) / 2;
		int icony = (getMeasuredHeight() - bitmapHeight) / 2;
		canvas.drawBitmap(bitmap, iconx, icony, null);
		// 坐标需要转换，因为默认情况下Button中的文字居中显示
		// 这里需要让文字在底部显示
//		canvas.translate(0, (this.getMeasuredHeight() / 2)
//				- (int) getTextSize());
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(mTextSize);
		
		//文字
		int textx = iconx + bitmapWidth + spaceIconAndText;
		int texty = (getMeasuredHeight() - textHeight) / 2;
		
		Log.d("sinopec", "mH: "+getMeasuredHeight()+"   tH:　"+textHeight+"   bitH:　"+bitmapHeight+" texty: "+texty);
		canvas.drawText(mText, textx, texty + 28, mPaint);
	}

}
