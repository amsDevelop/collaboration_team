package com.sinopec.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.Button;

import com.sinopec.activity.R;
import com.sinopec.application.SinoApplication;

public class MenuButtonNoIcon extends Button {
	private final int ICON_LEFT = 0;
	private final int ICON_RIGHT = 1;
	private final int ICON_ABOVE = 2;
	private final int ICON_BELOW = 3;
	private final int ICON_NULL = 4;
	
	private int mRelation = ICON_LEFT;
	private int mIconId;
	private int mTextSize;
	private String mText;
	private boolean mHasIcon;
	private int mRealWidth = 0;
	public MenuButtonNoIcon(Context context, AttributeSet attrs) {
		super(context, attrs);
		TypedArray a = context.obtainStyledAttributes(attrs,
				R.styleable.iconbutton);
		mRelation = a.getInt(R.styleable.iconbutton_relation, ICON_LEFT);
		mIconId = a.getResourceId(R.styleable.iconbutton_icon, R.drawable.icon);
		mText = a.getString(R.styleable.iconbutton_mtext);
		mTextSize = a.getDimensionPixelSize(R.styleable.iconbutton_text_size, 12);
		mHasIcon = a.getBoolean(R.styleable.iconbutton_hasicon, true);
		
		mRealWidth = SinoApplication.screenWidth / 4;
		setWidth(mRealWidth);
	}

	private Paint mPaint = new Paint();
	@Override
	protected void onDraw(Canvas canvas) {
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

}
