package com.dmitryk.mymotiv.view;

import android.content.Context;
import android.content.res.TypedArray;
import android.graphics.Canvas;
import android.graphics.Paint;
import android.graphics.RectF;
import android.util.AttributeSet;
import android.view.View;

import com.dmitryk.mymotiv.R;

public class CircularProgress extends View {

  private Paint mainPaint;
  private Paint secondaryPaint;

  private RectF rectF = new RectF();

  private int progress = 0;
  private int maxProgress;
  private int mainColor;
  private int secondaryColor;
  private float strokeWidth;

  public CircularProgress(Context context, AttributeSet attrs) {
    super(context, attrs);
    TypedArray ta = context.obtainStyledAttributes(attrs, R.styleable.CircularProgress, 0, 0);
    try {
      strokeWidth = ta.getDimension(R.styleable.CircularProgress_stroke_width, 20.0f);
      mainColor = ta.getColor(R.styleable.CircularProgress_main_color, 0xfff);
      secondaryColor = ta.getColor(R.styleable.CircularProgress_secondary_color, 0xbdbdbd);
    } finally {
      ta.recycle();
    }
    initPaints();
  }

  private void initPaints() {

    mainPaint = new Paint();
    mainPaint.setColor(mainColor);
    mainPaint.setStyle(Paint.Style.STROKE);
    mainPaint.setAntiAlias(true);
    mainPaint.setStrokeWidth(strokeWidth);
    mainPaint.setStyle(Paint.Style.STROKE);

    secondaryPaint = new Paint();
    secondaryPaint.setColor(secondaryColor);
    secondaryPaint.setStyle(Paint.Style.STROKE);
    secondaryPaint.setAntiAlias(true);
    secondaryPaint.setStrokeWidth(strokeWidth);

  }


  @Override
  public void invalidate() {
    initPaints();
    super.invalidate();
  }

  @Override
  protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
    rectF.set(strokeWidth, strokeWidth, MeasureSpec.getSize(widthMeasureSpec) - strokeWidth,
            MeasureSpec.getSize(heightMeasureSpec) - strokeWidth);
    setMeasuredDimension(widthMeasureSpec, heightMeasureSpec);
  }

  public void setMaxProgress(int maxProgress) {
    this.maxProgress = maxProgress;
    invalidate();
  }

  public void setProgress(int progress) {
    this.progress = progress;
    invalidate();
  }

  private float getProgressAngle() {
    return progress / (float) maxProgress * 360f;
  }

  @Override
  protected void onDraw(Canvas canvas) {
    super.onDraw(canvas);

    canvas.drawArc(rectF, getProgressAngle(), 360 - getProgressAngle(), false, secondaryPaint);
    canvas.drawArc(rectF, 0, getProgressAngle(), false, mainPaint);
  }
}