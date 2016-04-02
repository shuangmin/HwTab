package com.csm.hwtab;

import com.csm.hwtab.TabLinearLayout.OnTabOperatorListener;
import com.csm.hwtab.adapter.MyFragmentPagerAdapter;
import com.csm.hwtab.fragment.OneFragment;

import android.annotation.SuppressLint;
import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;

public class LinearLayoutTabActivity extends FragmentActivity implements OnTabOperatorListener {
	private ViewPager mViewPager;
	private TabLinearLayout mTabview;
	int mCount = 1;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tab);
		mCount = getIntent().getIntExtra("count", 1);
		initTabsView();
	}

	@SuppressLint("NewApi")
	private void initTabsView() {
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mTabview = (TabLinearLayout)findViewById(R.id.tab);
		for (int i = 0; i < mCount; i++) {
			mTabview.addTab("先秦" + i, new OneFragment());
		}
		mTabview.setOnTabItemClickListener(this);
		showViewPager();
	}

	private void showViewPager() {
		mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), mTabview.getTabs()));
		mTabview.setViewPager(mViewPager);
		mViewPager.setCurrentItem(0);
	}

	@Override
	public void onTabClick(int tabIndex) {
		
	}

	@Override
	public void onTabSelected(int tabIndex) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onTabScrolled(int sourcePosition, float positionOffset, int positionOffsetPixels) {
		// TODO Auto-generated method stub
		
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		// TODO Auto-generated method stub
		
	}
}
