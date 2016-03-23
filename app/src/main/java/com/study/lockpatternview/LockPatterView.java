package com.study.lockpatternview;

import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.BitmapFactory;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.Point;
import android.util.AttributeSet;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by WXB506 on 2016/3/23.
 */
public class LockPatterView extends View{

    private static final int POINT_SIZE = 5;

    private Point[][] points = new Point[3][3];

    private float width, height, offsetX, offsetY, bitmapR;

    private Bitmap bitmap_pressed, bitmap_normal, bitmap_error, bitmap_line, bitmap_line_error;

    private Paint paint = new Paint(Paint.ANTI_ALIAS_FLAG);

    private float moveX, moveY;

    private boolean isSelect, isFinish, movePoint;

    private List<Point> pointList = new ArrayList<Point>();

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
        // step1 : get width and height of layout
        width  = getWidth();
        height = getHeight();

        // step2 : determine horizontal or vertical direction
        if(height > width) {
            offsetX = 0;
            offsetY = (height - width) / 2;
            height  = width;
        } else {
            offsetX = (width - height) / 2;
            offsetY = 0;
            width   = height;
        }

        // step3 : image resource
        bitmap_normal  = BitmapFactory.decodeResource(getResources(), R.drawable.btn_circle_normal);
        bitmap_pressed = BitmapFactory.decodeResource(getResources(), R.drawable.btn_circle_pressed);
        bitmap_error   = BitmapFactory.decodeResource(getResources(), R.drawable.btn_circle_selected);
        bitmap_line    = BitmapFactory.decodeResource(getResources(), R.drawable.btn_line);
        bitmap_line_error = BitmapFactory.decodeResource(getResources(), R.drawable.btn_line_error);

        // step4 : create points
        points[0][0] = new Point(offsetX + width / 4,    offsetY + width / 4);
        points[0][1] = new Point(offsetX + width / 2,    offsetY + width / 4);
        points[0][2] = new Point(offsetX + width / 4 *3, offsetY + width / 4);

        points[1][0] = new Point(offsetX + width / 4,    offsetY + width / 4 * 2);
        points[1][1] = new Point(offsetX + width / 2,    offsetY + width / 4 * 2);
        points[1][2] = new Point(offsetX + width / 4 *3, offsetY + width / 4 * 2);

        points[2][0] = new Point(offsetX + width / 4,    offsetY + width / 4 * 3);
        points[2][1] = new Point(offsetX + width / 2,    offsetY + width / 4 * 3);
        points[2][2] = new Point(offsetX + width / 4 *3, offsetY + width / 4 * 3);

        // step5 : get bitmap radius
        bitmapR = bitmap_normal.getWidth() / 2;

        // step6 : set init flag
        isInit = true;
    }

    /*
    * draw point
    */
    private void points2Canvas(Canvas canvas) {
        // i is index of row
        for(int i = 0; i < points.length; i++) {
            // j is index of col
            for(int j = 0; j < points[i].length; j++) {
                Point point = points[i][j];

                switch(point.state) {
                    case Point.STATE_ERROR:
                        canvas.drawBitmap(bitmap_error,
                                point.x - bitmapR, point.y - bitmapR, paint);
                        break;
                    case Point.STATE_NORMAL:
                        canvas.drawBitmap(bitmap_normal,
                                point.x - bitmapR, point.y - bitmapR, paint);
                        break;
                    case Point.STATE_PRESSED:
                        canvas.drawBitmap(bitmap_pressed,
                                point.x - bitmapR, point.y - bitmapR, paint);
                        break;
                    default:
                        canvas.drawBitmap(bitmap_normal,
                                point.x - bitmapR, point.y - bitmapR, paint);
                        break;
                }
            }
        }
    }

    /*
    * self define point
    */
    public static class Point {
        public static final int STATE_NORMAL  = 0;
        public static final int STATE_PRESSED = 1;
        public static final int STATE_ERROR   = 2;
        public float x, y;
        public int index = 0, state = 0;

        // constructor
        public Point() {
        }
        public Point(float x, float y) {
            this.x = x;
            this.y = y;
        }
        // distance
        public static double distance(Point a, Point b) {
            return Math.sqrt(Math.abs(a.x - b.x) * Math.abs(a.x - b.x)
                    + Math.abs(a.y - b.y) * Math.abs(a.y - b.y));
        }

        // overlay
        public static boolean with(float pointX, float pointY, float r, float moveX, float moveY) {
            return Math.sqrt((pointX - moveX) * (pointX - moveX)
                    + (pointY - moveY) * (pointY - moveY)) < r;
        }
    }

    /*
    *  onTouch event
    */
    @Override
    public boolean onTouchEvent(MotionEvent event) {
        // step1 : get touch point
        moveX = event.getX();
        moveY = event.getY();

        Point point = null;

        switch (event.getAction()) {
            case MotionEvent.ACTION_DOWN:
                resetPoint();
                point = checkSelectPoint();
                if(point != null) {
                    isSelect = true;
                }
                break;
            case MotionEvent.ACTION_MOVE:
                if(isSelect) {
                    point = checkSelectPoint();
                }
                break;
            case MotionEvent.ACTION_UP:
                isFinish = true;
                isSelect = false;
                break;
        }
        // select multiple time
        if(!isFinish && isSelect && points != null) {
            if(crossPoint(point)) {
                movePoint = true;
            } else {
                point.state = Point.STATE_PRESSED;
                pointList.add(point);
            }
        }

        // finish
        if(isFinish) {
            if(pointList.size() == 1) {
                resetPoint();
            } else if(pointList.size() < POINT_SIZE && pointList.size() > 2) {
                errorPoint();
            } else {

            }
        }

        Log.d("test", "fuck");
        return true;
    }

    /*
    * check selected
    */
    private Point checkSelectPoint() {
        for(int i = 0; i < points.length; i++) {
            for(int j = 0; j < points[i].length; j++) {
                Point point = points[i][j];
                if(Point.with(point.x, point.y, bitmapR, moveX, moveY)) {
                    return point;
                }
            }
        }
        return null;
    }

    /*
    *  corsspoint
    **/
    private boolean crossPoint(Point point) {
        if(pointList.contains(point)) {
            return true;
        } else {
            return false;
        }
    }

    /*
    *  reDraw
    */
    public void resetPoint() {
        pointList.clear();
    }
    /*
    * error draw
    */
    public void errorPoint() {
        for(Point point : pointList) {
            point.state = Point.STATE_ERROR;
        }
    }
}
