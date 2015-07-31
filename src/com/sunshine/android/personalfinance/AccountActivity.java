package com.sunshine.android.personalfinance;

import java.util.ArrayList;
import java.util.List;

import com.sunshine.android.personal.fragment.ExpendFragment;
import com.sunshine.android.personal.fragment.IncomeFragment;

import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentActivity;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentStatePagerAdapter;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Display;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup.LayoutParams;
import android.view.Window;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class AccountActivity extends FragmentActivity implements OnClickListener {
	
	//两个导航按钮
	LinearLayout id_ll_expand;
	LinearLayout id_ll_income;
	//页面容器的ViewPager
	ViewPager mViewPager;
	//页面集合
	List<Fragment> fragmentList;
	//两个Fragment页面
	ExpendFragment expendFragment;
	IncomeFragment incomeFragment;
	private TextView mExTextView;
	private TextView mInTextView;
	private TextView topTextView;
	private ImageView topButton;
	//当前所处的页面
	private int mCurrentPageIndex;
	//覆盖层
	private ImageView mTabline;
	private int mScreen1_2;
	//屏幕宽度
	int screenWidth;
	//当前选中的项
	int currenttab=-1;
	@Override
	protected void onCreate(Bundle arg0) {
		// TODO Auto-generated method stub
		super.onCreate(arg0);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.activity_account);
		topButton = (ImageView) findViewById(R.id.topButton);
		mExTextView = (TextView) findViewById(R.id.id_tv_expand);
		mInTextView = (TextView) findViewById(R.id.id_tv_income);
		id_ll_expand = (LinearLayout) findViewById(R.id.id_ll_expand);
		id_ll_income = (LinearLayout) findViewById(R.id.id_ll_income);
		topButton.setOnClickListener(this);
		id_ll_expand.setOnClickListener(this);
		id_ll_income.setOnClickListener(this);
		topTextView = (TextView) findViewById(R.id.topTv);
		topTextView.setText("记账");
		initTabLine();
		mViewPager=(ViewPager) findViewById(R.id.viewpager);
		
		fragmentList=new ArrayList<Fragment>();
		expendFragment = new ExpendFragment();
		incomeFragment = new IncomeFragment();
		
		fragmentList.add(expendFragment);
		fragmentList.add(incomeFragment);
		
		
		mViewPager.setAdapter(new MyFrageStatePagerAdapter(getSupportFragmentManager()));
		
		mViewPager.setOnPageChangeListener(new OnPageChangeListener() {
			
			@Override
			public void onPageSelected(int position) {
				// TODO Auto-generated method stub
				resetTextView();
				switch (position)
				{
				case 0:
					mExTextView.setTextColor(Color.parseColor("#008000"));
					break;
				case 1:
					mInTextView.setTextColor(Color.parseColor("#008000"));
					break;

				}

				mCurrentPageIndex = position;
			}
			
			@Override
			public void onPageScrolled(int position, float positionOffset,
					int positionOffsetPx) {
				// TODO Auto-generated method stub
				Log.e("TAG", position + " , " + positionOffset + " , "
						+ positionOffsetPx);

				LinearLayout.LayoutParams lp = (android.widget.LinearLayout.LayoutParams) mTabline
						.getLayoutParams();

				if (mCurrentPageIndex == 0 && position == 0)// 0->1
				{
					lp.leftMargin = (int) (positionOffset * mScreen1_2 + mCurrentPageIndex
							* mScreen1_2);
				} else if (mCurrentPageIndex == 1 && position == 0)// 1->0
				{
					lp.leftMargin = (int) (mCurrentPageIndex * mScreen1_2 + (positionOffset - 1)
							* mScreen1_2);
				} 
				mTabline.setLayoutParams(lp);
			}
			
			@Override
			public void onPageScrollStateChanged(int arg0) {
				// TODO Auto-generated method stub
				
			}
		});
	}
	class MyFrageStatePagerAdapter extends FragmentStatePagerAdapter
	{

		public MyFrageStatePagerAdapter(FragmentManager fm) 
		{
			super(fm);
		}

		@Override
		public Fragment getItem(int position) {
			Log.d("AccountActivity", String.valueOf(position));
			return fragmentList.get(position);
		}

		@Override
		public int getCount() {
			return fragmentList.size();
		}
		
	}
	//初始化指示器
	private void initTabLine()
	{
		mTabline = (ImageView) findViewById(R.id.id_iv_tabline);
		Display display = getWindow().getWindowManager().getDefaultDisplay();
		DisplayMetrics outMetrics = new DisplayMetrics();
		display.getMetrics(outMetrics);
		mScreen1_2 = outMetrics.widthPixels / 2;
		LayoutParams lp = mTabline.getLayoutParams();
		lp.width = mScreen1_2;
		mTabline.setLayoutParams(lp);
	}
	
	protected void resetTextView()
	{
		mExTextView.setTextColor(Color.BLACK);
		mInTextView.setTextColor(Color.BLACK);
	}
	


	@Override
	public void onClick(View v) {
		// TODO Auto-generated method stub
		switch (v.getId()) {
		case R.id.id_ll_expand:
			changeView(0);	
			Log.d("AccountActivity", "这是支出页");
			break;
		case R.id.id_ll_income:
			changeView(1);	
			Log.d("AccountActivity", "这是收入页");
			break;
		case R.id.topButton:
			Intent intent = new Intent(AccountActivity.this, MainActivity.class);
			startActivity(intent);
		default:
			break;
		}		
	}
	private void changeView(int desTab)
	{
		mViewPager.setCurrentItem(desTab, true);
	}
	

}
