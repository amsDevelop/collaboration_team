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
	/**
	 * 主体的图标
	 */
	private final int ICON_MAIN = 5;
	
	private int mRelation = ICON_LEFT;
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
	}

	private Paint mPaint = new Paint();
	@Override
	protected void onDraw(Canvas canvas) {
		// 图片顶部居中显示
		int spaceIconAndText = 20;
		int bitmapWidth = 0;
		int bitmapHeight = 0;
		//文字宽度( 字号sp 要* dpi 才是真正像素值)
		int textWidth = (int) (mPaint.measureText(mText) * SinoApplication.density);
//		int textHeight =  (int) (mTextSize * SinoApplication.density);
		int textHeight = mTextSize;
		if(mHasIcon){
			bitmapWidth = bitmap.getWidth();
			bitmapHeight = bitmap.getHeight();
		}
		Log.d("sinopec", "mW: "+getMeasuredWidth()+"   width:　"+textWidth+"   bitW:　"+bitmapWidth+"  文字: "+mText);
		
		int iconx = (getMeasuredWidth() - (bitmapWidth + spaceIconAndText + mTextSize)) / 2;
		int icony = (getMeasuredHeight() - bitmapHeight - textHeight) / 2;
		//文字
		int textx = (getMeasuredWidth() - textWidth) / 2;
		int texty = (getMeasuredHeight() - textHeight) / 2;
		
		if(mHasIcon){
			textx = iconx + bitmapWidth + spaceIconAndText;
			texty = (getMeasuredHeight() - textHeight) / 2;
		}
		
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
		
		if(mHasIcon){
			canvas.drawBitmap(bitmap, iconx, icony, null);
		}
		// 坐标需要转换，因为默认情况下Button中的文字居中显示
		// 这里需要让文字在底部显示
		canvas.translate(0, (getMeasuredHeight() / 2) - (int) getTextSize());
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(mTextSize);
		
		canvas.drawText(mText, textx, texty, mPaint);

		//线两头的边距
		int spaceVertical = 15;
		int spaceHorizon = 15;
		
		int lineWidth = 1;
		//左右
		int spaceApartLandR = 5;
		//上下
		int spaceApartTandB = 1;
		//top
		canvas.drawRect(spaceApartLandR + spaceHorizon, spaceApartTandB, getMeasuredWidth() - spaceApartLandR - spaceHorizon - lineWidth, 
				spaceApartTandB + lineWidth, mPaint);
		//bottom
		canvas.drawRect(spaceApartLandR + spaceHorizon, getMeasuredHeight() - spaceApartTandB - lineWidth, getMeasuredWidth() - spaceApartLandR - spaceHorizon - lineWidth, 
				getMeasuredHeight() - spaceApartTandB, mPaint);
		//left
		canvas.drawRect(spaceApartLandR, spaceVertical, spaceApartLandR + lineWidth, getMeasuredHeight() - spaceVertical, mPaint);
		//right
		canvas.drawRect(getMeasuredWidth() - spaceApartLandR - lineWidth, spaceVertical, getMeasuredWidth() - spaceApartLandR, getMeasuredHeight() - spaceVertical, mPaint);
		
		super.onDraw(canvas);
	}

}