package com.exam.pie;





import android.animation.Animator;
import android.animation.AnimatorListenerAdapter;
import android.animation.TimeInterpolator;
import android.animation.ValueAnimator;
import android.annotation.TargetApi;
import android.content.Context;

import android.content.res.Resources;
import android.graphics.Bitmap;
import android.graphics.Bitmap.Config;
import android.graphics.BitmapShader;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Align;
import android.graphics.Paint.Style;
import android.graphics.Path;
import android.graphics.Path.Direction;
import android.graphics.RectF;
import android.graphics.Shader;
import android.graphics.Shader.TileMode;
import android.support.v4.view.ViewPager;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.MeasureSpec;
import android.view.View.OnClickListener;
import android.view.animation.AccelerateDecelerateInterpolator;
import android.widget.Toast;

public class AnimationDoughnutPie extends View
{
  private static final int DEGREE_360 = 360;
private static final String TAG = "AnimationDoughnutPie";
  private DataSource dataSource;
  private float fDensity = 0.0F;
  private float fEndAngle = 0.0F;
  private float fStartAngle = 0.0F;
  private int iCenterWidth = 0;
  private int iDisplayHeight;
  private int iDisplayWidth;
  private int iMargin = 0;
  private int iSelectedIndex = -1;
  private int[] itemColors;
  private float[] itemPercentages;
  private Paint paintPieFill;
  private RectF r = null;
  private Paint selectedSliceFillPaint;
  private Paint selectedSliceStrokeBorderPaint;
  private Paint textPaint;
  private float touchDownX;
  private float touchDownY;
  boolean flag = true;
  private int beforeSelect;
  
  
  private ValueAnimator mAnimator;
  private ValueAnimator mSelectedSlicePaintAnimator;
  private int mAnimationDuration;
  private TimeInterpolator mAnimationInterpolator;
  private int mAnimationStartDelay;
  private float mInsetRatio;
  

@TargetApi(11)
private void animatePieChart()
{
  if (this.mAnimator != null)
    this.mAnimator.cancel();
  this.mAnimator = new ValueAnimator();
  this.mAnimator.setInterpolator(this.mAnimationInterpolator);
  this.mAnimator.setStartDelay(this.mAnimationStartDelay);
  this.mAnimator.setFloatValues(new float[] { 0.0F, 1.0F });
  this.mAnimator.setDuration(this.mAnimationDuration);
  this.mAnimator.addUpdateListener(new ValueAnimator.AnimatorUpdateListener()
  {
    public void onAnimationUpdate(ValueAnimator paramAnonymousValueAnimator)
    {
      AnimationDoughnutPie.this.invalidate();
    }
  });

  Log.i(TAG, " ¹þ¹þ");
  this.mAnimator.setStartDelay(1000);
}


public int getAnimationDuration()
{
  return this.mAnimationDuration;
}

public TimeInterpolator getAnimationInterpolator()
{
  return this.mAnimationInterpolator;
}

public int getAnimationStartDelay()
{
  return this.mAnimationStartDelay;
}

public float getInsetRatio()
{
  return this.mInsetRatio;
}

  public AnimationDoughnutPie(Context paramContext, AttributeSet paramAttributeSet)
  {
    
	  
	  
	  super(paramContext, paramAttributeSet);
    
    AnimationDoughnutPie.this.setOnClickListener(new View.OnClickListener()
    {
      public void onClick(View v)
      {
        AnimationDoughnutPie.this.calculateSelectedSlice(AnimationDoughnutPie.this.touchDownX, AnimationDoughnutPie.this.touchDownY);
//        Log.i("test", "PieChart.this.touchDownX: " + PieChart.this.touchDownX + "  PieChart.this.touchDownY: " + PieChart.this.touchDownY);
        if (AnimationDoughnutPie.this.dataSource != null)
          AnimationDoughnutPie.this.dataSource.onSliceSelected(AnimationDoughnutPie.this, AnimationDoughnutPie.this.iSelectedIndex);
        AnimationDoughnutPie.this.invalidate();
      }
    });
    fnGetDisplayMetrics(paramContext);
    this.iMargin = ((int)fnGetRealPxFromDp(14.0F));
    this.paintPieFill = new Paint(1);
    this.paintPieFill.setStyle(Paint.Style.FILL);
    this.selectedSliceFillPaint = new Paint(1);
    this.selectedSliceFillPaint.setStyle(Paint.Style.FILL);
    this.selectedSliceFillPaint.setColor(1711276032);
    BitmapShader localBitmapShader = new BitmapShader(Bitmap.createBitmap(new int[] { -1, 0, 0, 0, 0, 0, 0, -1, -1, -1, 0, 0, 0, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, -1, -1, 0, 0, 0, 0, 0, 0, 0, -1, -1 }, 8, 8, Bitmap.Config.ARGB_8888), Shader.TileMode.REPEAT, Shader.TileMode.REPEAT);
    this.selectedSliceFillPaint.setShader(localBitmapShader);
    this.selectedSliceStrokeBorderPaint = new Paint(1);
    this.selectedSliceStrokeBorderPaint.setStyle(Paint.Style.STROKE);
    this.selectedSliceStrokeBorderPaint.setStrokeWidth(fnGetRealPxFromDp(1.0F));
    this.selectedSliceStrokeBorderPaint.setColor(-1);
    this.textPaint = new Paint(1);
    this.textPaint.setAntiAlias(true);
    this.textPaint.setTextAlign(Paint.Align.CENTER);
    this.textPaint.setStyle(Paint.Style.FILL);
    this.textPaint.setColor(-16777216);
    this.textPaint.setTextSize(fnGetRealPxFromDp(13.0F));
    this.itemPercentages = new float[0];
    this.itemColors = new int[0];
    this.r = new RectF();
//    this.mSelectedSliceStrokePaint = new Paint(1);
    
    this.mInsetRatio = 0.25F;
    this.mAnimationDuration = 900;
    this.mAnimationStartDelay = 300;
    this.mAnimationInterpolator = new AccelerateDecelerateInterpolator();
    
  }

  private void calculateSelectedSlice(float paramFloat1, float paramFloat2)
  {
    float f1 = 100.0F * ((360.0F + (float)(360.0D * (Math.atan2(paramFloat2 - this.iCenterWidth, paramFloat1 - this.iCenterWidth) / 6.283185307179586D))) % 360.0F) / 360.0F;
    float f2 = 0.0F;
    for (int i = 0; ; i++)
    {
      if (i < this.itemPercentages.length)
      {
        f2 += this.itemPercentages[i];
        if (f2 <= f1)
          continue;
        if (i == this.iSelectedIndex)
          this.iSelectedIndex = -1;
      }
      else
      {
        return;
      }
      this.iSelectedIndex = i;
      return;
    }
  }

  private void fnGetDisplayMetrics(Context paramContext)
  {
    this.fDensity = paramContext.getResources().getDisplayMetrics().density;
  }

  private float fnGetRealPxFromDp(float paramFloat)
  {
    if (this.fDensity != 1.0F)
      paramFloat *= this.fDensity;
    return paramFloat;
  }

  public DataSource getDataSource()
  {
    return this.dataSource;
  }

  protected void onDraw(Canvas paramCanvas)
  {
    super.onDraw(paramCanvas);
    float f1 = 0.0F;
    float f2 = 0.0F;
    this.fStartAngle = 0.0F;
    this.fEndAngle = 0.0F;
    for (int i = 0; i < this.itemPercentages.length; i++)
    {
      this.paintPieFill.setColor(this.dataSource.getColorForItem(this, i));
      this.fEndAngle = this.itemPercentages[i];
      this.fEndAngle = (360.0F * (this.fEndAngle / 100.0F));
      paramCanvas.drawArc(this.r, this.fStartAngle, this.fEndAngle, true, this.paintPieFill);
      if (this.iSelectedIndex == i)
      {
        f1 = this.fStartAngle;
        f2 = this.fEndAngle;       
      }
      this.fStartAngle += this.fEndAngle;
    }
    Path localPath = null;
    float f4 = 0;
    
    if (this.iSelectedIndex >= 0)
    {
		if(flag){
			RectF localRectF1 = this.r;
			Paint localPaint1 = this.selectedSliceFillPaint;
			paramCanvas.drawArc(localRectF1, f1, f2, true, localPaint1);
			RectF localRectF2 = this.r;
			Paint localPaint2 = this.selectedSliceStrokeBorderPaint;
			paramCanvas.drawArc(localRectF2, f1, f2, true, localPaint2);
			beforeSelect = this.iSelectedIndex;
			flag = false;															
		}else {		
			if(beforeSelect == this.iSelectedIndex){
				this.paintPieFill.setColor(this.dataSource.getColorForItem(this, iSelectedIndex));
				paramCanvas.drawArc(this.r, f1, f2, true, this.paintPieFill);	
				flag = true;
			}else {
				RectF localRectF1 = this.r;
				Paint localPaint1 = this.selectedSliceFillPaint;
				paramCanvas.drawArc(localRectF1, f1, f2, true, localPaint1);
				RectF localRectF2 = this.r;
				Paint localPaint2 = this.selectedSliceStrokeBorderPaint;
				paramCanvas.drawArc(localRectF2, f1, f2, true, localPaint2);
				beforeSelect = this.iSelectedIndex;
				flag = false;													
			}
		}    		
    }
    
    animatePieChart();
  }
  
  public void setAnimationInterpolator(TimeInterpolator paramTimeInterpolator)
  {
    this.mAnimationInterpolator = paramTimeInterpolator;
  }
  
  public void setAnimationDuration(int paramInt)
  {
    this.mAnimationDuration = paramInt;
  }
  
  public void setAnimationStartDelay(int paramInt)
  {
    this.mAnimationStartDelay = paramInt;
  }

  public void setInsetRatio(float paramFloat)
  {
    this.mInsetRatio = paramFloat;
  }
  
  protected void onMeasure(int paramInt1, int paramInt2)
  {
    super.onMeasure(paramInt1, paramInt2);
    this.iDisplayWidth = View.MeasureSpec.getSize(paramInt1);
    this.iDisplayHeight = View.MeasureSpec.getSize(paramInt2);
    if (this.iDisplayWidth > this.iDisplayHeight)
      this.iDisplayWidth = this.iDisplayHeight;
    this.iCenterWidth = (this.iDisplayWidth / 2);
    int i = this.iCenterWidth - 2 * this.iMargin;
    this.r.top = (this.iCenterWidth - i);
    this.r.left = (this.iCenterWidth - i);
    this.r.right = (i + this.iCenterWidth);
    this.r.bottom = (i + this.iCenterWidth);
    setMeasuredDimension(this.iDisplayWidth, this.iDisplayWidth);
  }

  @Override
  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    this.touchDownX = paramMotionEvent.getX();
    this.touchDownY = paramMotionEvent.getY();
    return false;
  }

  public void reloadData()
  {
    int i = this.dataSource.getItemCount(this);
    this.itemColors = new int[i];
    this.itemPercentages = new float[i];
    float f = 0.0F;
    for (int j = 0; j < i; j++)
    {
      this.itemColors[j] = this.dataSource.getColorForItem(this, j);
      this.itemPercentages[j] = this.dataSource.getItemValue(this, j);
      f += this.itemPercentages[j];
    }
    for (int k = 0; k < i; k++)
      this.itemPercentages[k] = (100.0F * (this.itemPercentages[k] / f));
    invalidate();
  }

  public void setDataSource(DataSource paramDataSource)
  {
    this.dataSource = paramDataSource;
    reloadData();
  }

  public static abstract interface DataSource
  {
    public abstract int getColorForItem(AnimationDoughnutPie paramPieChart, int paramInt);

    public abstract int getItemCount(AnimationDoughnutPie paramPieChart);

    public abstract float getItemValue(AnimationDoughnutPie paramPieChart, int paramInt);

    public abstract String getTextForItem(AnimationDoughnutPie paramPieChart, int paramInt);

    public abstract void onSliceSelected(AnimationDoughnutPie paramPieChart, int paramInt);
  } 
}