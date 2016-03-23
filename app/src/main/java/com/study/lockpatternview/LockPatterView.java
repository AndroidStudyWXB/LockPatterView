package com.study.lockpatternview;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Point;
import android.util.AttributeSet;
import android.view.View;

/**
 * Created by WXB506 on 2016/3/23.
 */
public class LockPatterView extends View{

    private Point[][] points = new Point[3][3];
    private float width, height, offsetX, offsetY;

    /*
    *  构造函数
    */
    public LockPatterView(Context context, AttributeSet attrs, int defStyle) {
        super(context, attrs, defStyle);
    }

    public LockPatterView(Context context, AttributeSet attrs) {
        super(context, attrs);
    }

    public LockPatterView(Context context) {
        super(context);
    }

    /*
    * 绘制9宫格
    */
    private boolean isInit;

    @Override
    protected void onDraw(Canvas canvas) {
        // 如果没有初始化，需要首先初始化点
        if(!isInit) {
            initPoints();
        }

        // 绘制9个点
        points2Canvas(canvas);
    }

    /*
    *
    */
    private void initPoints() {

    }

    /*
    *
    */
    private void points2Canvas(Canvas canvas) {

    }
}
