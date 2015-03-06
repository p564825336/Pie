package com.exam.pie;

import java.util.ArrayList;


import java.util.Iterator;
import java.util.Map.Entry;
import java.util.Set;
import java.util.TreeMap;

import com.exam.pie.AnimationDoughnutPie.DataSource;

import android.annotation.SuppressLint;
import android.app.Activity;
import android.content.Context;
import android.graphics.Color;
import android.os.Bundle;
import android.util.Log;
import android.view.MotionEvent;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.animation.AnticipateOvershootInterpolator;
import android.widget.RelativeLayout;
import android.widget.TextView;

public class MainActivity extends Activity implements DataSource{

	RelativeLayout mLinear;
	
	
	private TreeMap<String, Integer> mPositiveBehaviorsMap = new TreeMap<String, Integer>();
	private TreeMap<String, Integer> mNegativeBehaviorsMap = new TreeMap<String, Integer>();
	private String[] mPositiveBehaviorNames;
	private String[] mNegativeBehaviorNames;
	private ArrayList<Integer> mPositiveBehaviorValue = new ArrayList<Integer>();
	private ArrayList<Integer> mNegativeBehaviorValue = new ArrayList<Integer>();
	
	private int mTotalPositivePoints = 0;
	private int mTotalNegativePoints = 0;
	  
	private AnimationDoughnutPie outer_pie_chart;
	private AnimationDoughnutPie inner_pie_chart;
	private TextView top_text;
	private TextView bottom_text;
	
	Context context = MainActivity.this;
	private CustomScrollView scroll_view;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		this.setTitle("绘制饼图...");
		
		GlobalVariable.context = context;
		initData();
		
		setContentView(R.layout.activity_report_doughnut_teacher);
		
		initView();
		
//		reloadData();
	}

	/*private void reloadData() {
		 this.outer_pie_chart.reloadData();
		 this.inner_pie_chart.reloadData();
	}*/

	@SuppressLint("NewApi")
	private void initView() {
		outer_pie_chart = (AnimationDoughnutPie)findViewById(R.id.outer_pie_chart);
		inner_pie_chart = (AnimationDoughnutPie) findViewById(R.id.inner_pie_chart);
		top_text = (TextView) findViewById(R.id.top_text);
		bottom_text = (TextView) findViewById(R.id.bottom_text);
		scroll_view = (CustomScrollView) findViewById(R.id.scroll_view);
		outer_pie_chart.setDataSource(this);
		outer_pie_chart.setEnabled(true);
		inner_pie_chart.setDataSource(this);
		inner_pie_chart.setEnabled(false);
		
		this.outer_pie_chart.setAnimationDuration(6000);
	    this.outer_pie_chart.setAnimationStartDelay(6000);
	    this.outer_pie_chart.setInsetRatio(0.35F);
		this.outer_pie_chart.setAnimationInterpolator(new AnticipateOvershootInterpolator());
		
		scroll_view.setOnSingleTapUpListener(new CustomScrollView.OnSingleTapUpListener() {		
			@Override
			public void onTapUp(MotionEvent paramMotionEvent) {
				int[] arrayOfInt = new int[2];
				outer_pie_chart.getLocationInWindow(arrayOfInt);
		        int i = outer_pie_chart.getWidth();
		        int j = outer_pie_chart.getHeight();
		        float f1 = paramMotionEvent.getRawX();
		        float f2 = paramMotionEvent.getRawY();
		        
		        if ((f1 >= arrayOfInt[0]) && (f1 <= i + arrayOfInt[0]) && (f2 >= arrayOfInt[1]) && (f2 <= j + arrayOfInt[1]))
		        	outer_pie_chart.performClick();
			}
		});
	}

	/**
	 * 初始化数据
	 */
	private void initData() {
		mPositiveBehaviorsMap.put("表扬1", 5);
		mPositiveBehaviorsMap.put("表扬2", 7);
		mPositiveBehaviorsMap.put("表扬3", 9);
		mPositiveBehaviorsMap.put("表扬4", 10);
		mPositiveBehaviorsMap.put("表扬5", 10);
		
		mNegativeBehaviorsMap.put("批评1", 4);
		mNegativeBehaviorsMap.put("批评2", 3);
		mNegativeBehaviorsMap.put("批评3", 5);
		mNegativeBehaviorsMap.put("批评4", 5);
		
		mPositiveBehaviorNames = new String[mPositiveBehaviorsMap.size()];
		mNegativeBehaviorNames = new String[mNegativeBehaviorsMap.size()];
		
		mPositiveBehaviorsMap.keySet().toArray(mPositiveBehaviorNames);
		mNegativeBehaviorsMap.keySet().toArray(mNegativeBehaviorNames);
		
		Set<Entry<String,Integer>> entrySet2 = mNegativeBehaviorsMap.entrySet();
		Iterator<Entry<String, Integer>> iterator2 = entrySet2.iterator();
		while (iterator2.hasNext()) {
			Entry<String, Integer> next = iterator2.next();
			Integer value = next.getValue();
			mNegativeBehaviorValue.add(value);
			mTotalNegativePoints += value;
		}
				
		Set<Entry<String,Integer>> entrySet = mPositiveBehaviorsMap.entrySet();
		Iterator<Entry<String, Integer>> iterator = entrySet.iterator();
		
		while (iterator.hasNext()) {
			Entry<String, Integer> next = iterator.next();
			Integer value = next.getValue();
			mPositiveBehaviorValue.add(value);
			mTotalPositivePoints += value;			
		}			
	}

	@Override
	public int getColorForItem(AnimationDoughnutPie paramPieChart, int paramInt) {
		if(this.mPositiveBehaviorsMap.size() + this.mNegativeBehaviorsMap.size() == 0){
			return getResources().getColor(R.color.default_circle_indicator_stroke_color);
		}
		
		if(paramPieChart == this.inner_pie_chart){
			if(paramInt == 0){
				return getResources().getColor(R.color.outer_circle_positive_color_one);
			} else {
				return getResources().getColor(R.color.outer_circle_negative_color_one);
			}
		}
		
		int[] arrayOfInt1 = { getResources().getColor(R.color.outer_circle_positive_color_one), getResources().getColor(R.color.outer_circle_positive_color_two), getResources().getColor(R.color.outer_circle_positive_color_three) };
		int[] arrayOfInt2 = { getResources().getColor(R.color.outer_circle_negative_color_one), getResources().getColor(R.color.outer_circle_negative_color_two), getResources().getColor(R.color.outer_circle_negative_color_three) };
		int i = this.mPositiveBehaviorsMap.size();
		int j = this.mNegativeBehaviorsMap.size();
		
		if(paramPieChart == this.outer_pie_chart){
			if (paramInt < i)
		        return RotatedValueUtil.getRotatedValue(paramInt, i + j, arrayOfInt1);
		    if (paramInt - i < j)
		       return RotatedValueUtil.getRotatedValue(paramInt, i + j, arrayOfInt2);
		}
		
	    return 0;
	}


	@Override
	public int getItemCount(AnimationDoughnutPie paramPieChart) {		
		int i = 0;
		
		if(paramPieChart == this.inner_pie_chart){
			i = 2;
		}
		
		if(paramPieChart == this.outer_pie_chart){
			i = mPositiveBehaviorsMap.size() + mNegativeBehaviorsMap.size();
			if(i > 0){
				return i;
			}else {
				return 1;
			}
		}
		
		return i;
	}


	@Override
	public float getItemValue(AnimationDoughnutPie paramPieChart, int paramInt) {
		if(this.mPositiveBehaviorsMap.size() + this.mNegativeBehaviorsMap.size() == 0)
			return 1;
		if(paramPieChart == this.inner_pie_chart){
			if(paramInt == 0){
				return this.mTotalPositivePoints;
			}else {
				return this.mTotalNegativePoints;
			}
		}
		
		if(paramPieChart == this.outer_pie_chart){
			if(paramInt < mPositiveBehaviorsMap.size()){
				return mPositiveBehaviorValue.get(paramInt);
			}else {
				return mNegativeBehaviorValue.get(paramInt-mPositiveBehaviorValue.size());
			}
		}
		return 1;
	}


	@Override
	public String getTextForItem(AnimationDoughnutPie paramPieChart,
			int paramInt) {

		return "";
	}


	@Override
	public void onSliceSelected(AnimationDoughnutPie paramPieChart, int paramInt) {
		if(this.mPositiveBehaviorsMap.size() + this.mNegativeBehaviorsMap.size() == 0){
			return ;
		}
		
		String str = "";
	    if (paramInt >= 0 && paramInt < this.mPositiveBehaviorNames.length){
	    	str = this.mPositiveBehaviorNames[paramInt];	    	
	    }
	    
	    if (this.mPositiveBehaviorNames.length <= paramInt &&  paramInt< this.mPositiveBehaviorNames.length + this.mNegativeBehaviorNames.length){
	    	str = this.mNegativeBehaviorNames[(paramInt - this.mPositiveBehaviorNames.length)];	    	
	    }
	    
	    float f = 100.0F * (getItemValue(paramPieChart, paramInt) / (this.mTotalPositivePoints + this.mTotalNegativePoints));
	    
	    Log.i("test", "点击位置: " + paramInt + "    " + str + ": " + f + "%");
	    top_text.setText(Integer.valueOf((int)f) + "%");
	    bottom_text.setText(str);   
	}
}
