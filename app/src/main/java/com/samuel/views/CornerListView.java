package com.samuel.views;

import android.content.Context;
import android.graphics.Color;
import android.util.AttributeSet;
import android.view.MotionEvent;
import android.view.View;
import android.widget.AdapterView;
import android.widget.ListAdapter;
import android.widget.ListView;

import com.samuel.mytools.R;

public class CornerListView extends ListView {

    public CornerListView(Context context) {
        super(context);
        init();

    }

    public CornerListView(Context context, AttributeSet attrs) {
        super(context, attrs);
        init();

    }

    public CornerListView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
        init();
    }

    private void init() {
        setCacheColorHint(Color.TRANSPARENT);
    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        ListAdapter listAdapter = this.getAdapter();
        if (listAdapter == null) {
            return;
        }

        int totalHeight = 0;
        int dataCount = listAdapter.getCount();
        for (int i = 0; i < dataCount; i++) {
            View item = listAdapter.getView(i, null, this);
            if (item != null) {
                totalHeight += 200;
            }
        }

        if (dataCount > 0) {
            int dividerHeight = this.getDividerHeight() * (dataCount - 1);
            int height = totalHeight + dividerHeight;
            int heightSpec = MeasureSpec.makeMeasureSpec(height, MeasureSpec.AT_MOST);
            super.onMeasure(widthMeasureSpec, heightSpec);
        } else {
            super.onMeasure(widthMeasureSpec, heightMeasureSpec);
        }
    }

   

    @Override
    public boolean onInterceptTouchEvent(MotionEvent ev) {

        switch (ev.getAction()) {

        case MotionEvent.ACTION_DOWN:
            int x = (int) ev.getX();
            int y = (int) ev.getY();
            // 根据按下的点获取相应的item位置索引
            int itemPosition = pointToPosition(x, y);
            // 最后一项item的索引
            // int last = getAdapter().getCount() - 1;

            if (itemPosition != AdapterView.INVALID_POSITION) {

                if (itemPosition == 0) {// 第一项
                    // 只有一项
                    if (itemPosition == getAdapter().getCount() - 1) {
                         setSelector(R.color.transparent);
                    } else {
                        setSelector(R.color.transparent);
                    }
                } else if (itemPosition == getAdapter().getCount() - 1) {// 最后一项
                     setSelector(R.color.transparent);
                } else {// 中间项
                    setSelector(R.color.transparent);
                }

            }

            break;

        case MotionEvent.ACTION_MOVE:
            break;

        case MotionEvent.ACTION_UP:
            break;

        }

        return super.onInterceptTouchEvent(ev);
    }

}
