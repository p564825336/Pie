package com.exam.pie;


import android.content.Context;
import android.util.AttributeSet;
import android.view.GestureDetector;
import android.view.MotionEvent;
import android.widget.ScrollView;

public class CustomScrollView extends ScrollView
{
  private GestureDetector mGestureDetector = new GestureDetector(GlobalVariable.context, new MGestureDetector());
  private OnSingleTapUpListener mOnSingleTapUpListener;

  public CustomScrollView(Context paramContext, AttributeSet paramAttributeSet)
  {
    super(paramContext, paramAttributeSet);
    setFadingEdgeLength(0);
  }

  public boolean onTouchEvent(MotionEvent paramMotionEvent)
  {
    this.mGestureDetector.onTouchEvent(paramMotionEvent);
    return super.onTouchEvent(paramMotionEvent);
  }

  public void setOnSingleTapUpListener(OnSingleTapUpListener paramOnSingleTapUpListener)
  {
    this.mOnSingleTapUpListener = paramOnSingleTapUpListener;
  }

  class MGestureDetector extends GestureDetector.SimpleOnGestureListener
  {
    MGestureDetector()
    {
    }

    public boolean onSingleTapUp(MotionEvent paramMotionEvent)
    {
      if (CustomScrollView.this.mOnSingleTapUpListener != null)
        CustomScrollView.this.mOnSingleTapUpListener.onTapUp(paramMotionEvent);
      return true;
    }
  }

  public static abstract interface OnSingleTapUpListener
  {
    public abstract void onTapUp(MotionEvent paramMotionEvent);
  }
}

 