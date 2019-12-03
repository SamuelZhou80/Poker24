package com.samuel.common;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.util.AttributeSet;
import android.widget.EditText;

/**
 * 带下划线的输入框
 * 
 * @author Administrator
 *
 */
public class UnderlineEdit extends EditText {
    private Paint mPaint;

    /**
     * @param context
     * @param attrs
     */
    public UnderlineEdit(Context context, AttributeSet attrs) {
        super(context, attrs);
        mPaint = new Paint();

        mPaint.setStyle(Paint.Style.STROKE);
        mPaint.setColor(Color.BLUE);
    }

    @Override
    public void onDraw(Canvas canvas) {
        super.onDraw(canvas);

        // 画底线
        int yPos = this.getHeight() - 1;
        canvas.drawLine(0, yPos, this.getWidth() - 1, yPos, mPaint);
    }
}
