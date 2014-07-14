package com.sinopec.view;

import java.util.Locale;

import com.sinopec.activity.R;
import com.sinopec.application.SinoApplication;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

public class MenuButton extends Button {
	private Bitmap bitmap;
	private final int ICON_LEFT = 0;
	private final int ICON_RIGHT = 1;
	private final int ICON_ABOVE = 2;
	private final int ICON_BELOW = 3;
	private final int ICON_NULL = 4;
	private int mRelation = ICON_LEFT;
	private int mIconId;
	private int mTextSize;
	private String mText;
	public MenuButton(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.iconbutton);
		mRelation = a.getInt(R.styleable.iconbutton_relation, ICON_LEFT);
		mIconId = a.getResourceId(R.styleable.iconbutton_icon, R.drawable.icon);
		mText = a.getString(R.styleable.iconbutton_mtext);
		mTextSize = a.getDimensionPixelSize(R.styleable.iconbutton_text_size, 12);
		
		bitmap = BitmapFactory.decodeResource(context.getResources(), mIconId);
		
		setWidth(SinoApplication.screenWidth / 4);
	}

	private Paint mPaint = new Paint();
	@Override
	protected void onDraw(Canvas canvas) {
		// 图片顶部居中显示
		int spaceIconAndText = 3;
		int iconx = (getMeasuredWidth() - (bitmap.getWidth() + spaceIconAndText + mTextSize)) / 2;
		int icony = (getMeasuredHeight() - bitmap.getHeight()) / 2;

		canvas.drawBitmap(bitmap, iconx, icony, null);
		// 坐标需要转换，因为默认情况下Button中的文字居中显示
		// 这里需要让文字在底部显示
//		canvas.translate(0, (this.getMeasuredHeight() / 2)
//				- (int) getTextSize());
		mPaint.setColor(Color.BLACK);
		mPaint.setTextSize(mTextSize);
		
		//文字
		int textx = iconx + bitmap.getWidth() + spaceIconAndText;
		int texty = (getMeasuredHeight() - mTextSize) / 2;;
		canvas.drawText(mText, textx, texty, mPaint);
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

}
