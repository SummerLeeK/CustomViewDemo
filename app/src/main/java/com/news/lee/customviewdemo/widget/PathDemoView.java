package com.news.lee.customviewdemo.widget;


import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Path;
import android.graphics.Rect;
import android.util.AttributeSet;
import android.util.Log;
import android.util.TypedValue;
import android.view.View;

import com.news.lee.customviewdemo.Data;

import java.util.ArrayList;

/**
 * Created by u on 2017/6/13.
 */

public class PathDemoView extends View {

    private Paint mPaint;
    private Paint textPaint;
    private Paint dataPaint;

    private Path path;

    private Path linePath;

    private Path dataPath;

    private int width;

    private int height;

    private int points = 5;

    private int rings = 5;

    private float radius;

    private float center;

    private float angle;

    private ArrayList<Data> datas;

    private int size = (int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 200, getResources().getDisplayMetrics());

    private Rect rect;

    private String tips = "没有数据";

    public PathDemoView(Context context) {
        super(context);
    }

    public PathDemoView(Context context, AttributeSet attrs) {
        super(context, attrs);

    }

    public void setTips(String tips) {
        this.tips = tips;
    }

    public void setDatas(ArrayList<com.news.lee.customviewdemo.Data> datas) {
        this.datas = datas;
        points = datas.size();
        postInvalidate();
    }

    @Override
    protected void onDraw(Canvas canvas) {
//        Log.i("info", "onDraw");
        center = Math.min(width, height) / 2;
        radius = center * 0.7f;

        if (datas != null) {
            drawPolygon(canvas);
            drawTipsTitle(canvas);
            drawData(canvas);
        } else {
            drawNull(canvas);
        }

    }

    @Override
    protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
        int widthMode = MeasureSpec.getMode(widthMeasureSpec);
        int widthSize = MeasureSpec.getSize(widthMeasureSpec);

        int heightMode = MeasureSpec.getMode(heightMeasureSpec);
        int heightSize = MeasureSpec.getSize(heightMeasureSpec);

        int width;
        int height;

        if (widthMode == MeasureSpec.EXACTLY) {
            width = widthSize;
        } else {
            width = size + getPaddingRight() + getPaddingLeft();
            if (widthMode == MeasureSpec.AT_MOST) {
                width = Math.min(width, widthSize);
            }
        }

        if (heightMode == MeasureSpec.EXACTLY) {
            height = heightSize;
        } else {
            height = size + getPaddingTop() + getPaddingBottom();
            if (heightMode == MeasureSpec.AT_MOST) {
                height = Math.min(height, heightSize);
            }
        }
//        Log.i("info", "width" + width + "height" + height);
        setMeasuredDimension(width, height);
    }

    @Override
    protected void onSizeChanged(int w, int h, int oldw, int oldh) {
        super.onSizeChanged(w, h, oldw, oldh);
        width = w;
        height = h;
    }


    /**
     * 绘制正多边形
     *
     * @param canvas
     */
    private void drawPolygon(Canvas canvas) {
        mPaint = new Paint();
        path = new Path();
        linePath = new Path();
        mPaint.setColor(Color.BLACK);
        mPaint.setStyle(Paint.Style.FILL_AND_STROKE);
        mPaint.setAntiAlias(true);

        float r = radius / rings;

        mPaint.setStyle(Paint.Style.STROKE);
        for (int i = 0; i < rings; i++) {
            float pathRadius = r * (i + 1);
            angle = 360 / points;
            path.reset();
            for (int j = 0; j < points; j++) {
                if (j == 0) {
                    if (i == rings - 1) {
                        linePath.moveTo(center, center);
                        linePath.lineTo(center + pathRadius, center);
                        canvas.drawPath(linePath, mPaint);
                    }
                    path.moveTo(center + pathRadius, center);
                } else {
                    //根据半径，计算出蜘蛛丝上每个点的坐标
//                    Log.i("info", "cos(angle*j)" + "angle*j" + angle * j + Math.cos(Math.toRadians(angle * j)) + "Math.sin(angle*j)" + Math.sin(Math.toRadians(angle * j)));
                    float x = (float) (center + pathRadius * Math.cos(Math.toRadians(angle * j)));
                    float y = (float) (center + pathRadius * Math.sin(Math.toRadians(angle * j)));
                    path.lineTo(x, y);
                    if (i == rings - 1) {
                        linePath.moveTo(center, center);
                        linePath.lineTo(x, y);
                        canvas.drawPath(linePath, mPaint);
                    }

                }
            }
            path.close();//闭合路径
            canvas.drawPath(path, mPaint);
        }
    }

    /**
     * 绘制提示文字
     *
     * @param canvas
     */
    private void drawTipsTitle(Canvas canvas) {
        Rect textRect = new Rect();
        textPaint = new Paint();
        textPaint.setAntiAlias(true);
        textPaint.setColor(Color.BLACK);
        textPaint.setTextAlign(Paint.Align.LEFT);
        textPaint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        Paint.FontMetrics fontMetrics = textPaint.getFontMetrics();
        float textHeight = fontMetrics.descent - fontMetrics.ascent;
        float textRadius = radius + textHeight / 2;
//        Log.i("font","fontMetrics.top"+fontMetrics.top+"fontMetrics.ascent"+fontMetrics.ascent+"fontMetrics.bottom"+fontMetrics.bottom+"fontMetrics.descent"+fontMetrics.descent);

        for (int i = 0; i < datas.size(); i++) {
            com.news.lee.customviewdemo.Data data = datas.get(i);
            textPaint.getTextBounds(data.text, 0, data.text.length(), textRect);
//            Log.i("info","rect.width()"+textRect.width()+"textPaint.measureText(data.text)"+textPaint.measureText(data.text));
            float x;
            float y;
            if (i == 0) {
                x = center + radius + textHeight / 2;
                y = center + fontMetrics.bottom;
                canvas.drawText(data.text, x, y, textPaint);
            } else {
                x = (float) (center + textRadius * Math.cos(Math.toRadians(angle * i)));
                y = (float) (center + textRadius * Math.sin(Math.toRadians(angle * i)));

                if (x - center > 0 && y - center > 0 && x - center != textRadius && y - textRadius != center) {
//                    Log.i("data", "i" + i + "x" + x + "y" + y);
                    x = x + fontMetrics.bottom;
                    y = y + textHeight;
//                    Log.i("data", "第一象限i" + i + "x" + x + "y" + y);
                } else if (x - center > 0 && y - center < 0 && x - center != textRadius && y + textRadius != center) {
                    //第二象限
                    x = x + textRect.height() / 2;
                    y = y - textHeight / 2;
//                    Log.i("data", "第二象限 i" + i + "x" + x + "y" + y);
                } else if (x - center < 0 && y - center > 0 && x + textRadius != center && y - textRadius != center) {
                    //第四象限
                    x = x - textRect.width();
                    y = y + textHeight;
                    Log.i("data", "第四象限 i" + i + "x" + x + "y" + y);
                } else if (x - center < 0 && y - center < 0 && x + textRadius != center && y + textRadius != center) {
                    //第三象限
                    x = (float) (x - textRect.width() / 1.2);
                    y = y - fontMetrics.bottom;
//                    Log.i("data", "第三象限 i" + i + "x" + x + "y" + y);
                } else if (x - center == textRadius || y - center == textRadius || x + textRadius == center || y + textRadius == center) {
                    //在X轴Y轴直线上
                    if ((x - center == textRadius || x + textRadius == center)) {
                        //在X轴上
                        if (x > center) {
                            x = center + radius + textHeight / 2;
                            y = center + fontMetrics.bottom;
                        } else {
                            x = x - textPaint.measureText(data.text);
                            y = center + fontMetrics.bottom;
                        }
                    } else if (y - center == textRadius || y + textRadius == center) {
                        //在Y轴上
                        if (y > center) {
                            y = y + textHeight;
                            x = x-textPaint.measureText(data.text)/2;
                        } else {
                            y = y-textHeight/2;
                            x = x-textPaint.measureText(data.text)/2;
                        }
                    }
                }
            }
            canvas.drawText(data.text, 0, data.text.length(), x, y, textPaint);
        }
    }

    /**
     * 绘制数据
     *
     * @param canvas
     */
    private void drawData(Canvas canvas) {
        dataPaint = new Paint();
        dataPaint.setStyle(Paint.Style.STROKE);
        dataPaint.setStrokeWidth(5);
        dataPaint.setColor(Color.RED);
        dataPath = new Path();
        dataPath.reset();
        for (int i = 0; i < datas.size(); i++) {
            com.news.lee.customviewdemo.Data data = datas.get(i);
            float x;
            float y;
            float dataPointX;
            float dataPointY;
            Log.i("data", data.size + "");
            if (i == 0) {
                x = center + radius;
                y = center;
                Log.i("data", "x" + x + "y" + y);
                dataPointX = center + (radius) * (data.size / 100);
                dataPointY = y;
                dataPath.moveTo(dataPointX, dataPointY);
//                canvas.drawCircle(dataPointX,dataPointY,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()),dataPaint);
//                Log.i("data", "dataPointX" + dataPointX + "dataPointY" + dataPointY);

            } else {
                x = (float) (center + radius * Math.cos(Math.toRadians(angle * i)));
                y = (float) (center + radius * Math.sin(Math.toRadians(angle * i)));
//                Log.i("data", "x" + x + "y" + y);
                if (y - radius == 0) {
                    //平行与X轴
                    dataPointX = center + (x - center) * (data.size / 100);
                    dataPointY = center;
                } else if (x - radius == 0) {
                    //平行于Y轴
                    dataPointX = center;
                    dataPointY = center + (y - center) * (data.size / 100);
                } else {
                    //相似三角形
                    dataPointX = center + (x - center) * (data.size / 100);
                    dataPointY = center + (y - center) * (data.size / 100);
                }

                dataPath.lineTo(dataPointX, dataPointY);
//                canvas.drawCircle(dataPointX,dataPointY,(int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_DIP, 2, getResources().getDisplayMetrics()),dataPaint);
//                Log.i("data", "dataPointX" + dataPointX + "dataPointY" + dataPointY);
            }
        }

        dataPath.close();
        canvas.drawPath(dataPath, dataPaint);

    }

    /**
     * 数据为空时的提示文字
     *
     * @param canvas
     */
    private void drawNull(Canvas canvas) {
        textPaint = new Paint();
        textPaint.setColor(Color.BLACK);
        textPaint.setTextSize((int) TypedValue.applyDimension(TypedValue.COMPLEX_UNIT_SP, 10, getResources().getDisplayMetrics()));
        canvas.drawText(tips, 0, tips.length(), radius, radius, textPaint);

    }

}
