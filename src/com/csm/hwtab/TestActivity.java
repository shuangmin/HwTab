package com.csm.hwtab;

import com.csm.hwtab.adapter.MyFragmentPagerAdapter;
import com.csm.hwtab.fragment.OneFragment;

import android.os.Bundle;
import android.support.v4.app.FragmentActivity;
import android.support.v4.view.ViewPager;
import android.view.Window;

public class TestActivity extends FragmentActivity{
	private ViewPager mViewPager;
	private TabLinearLayout mTabview;

	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		requestWindowFeature(Window.FEATURE_NO_TITLE);
		setContentView(R.layout.tab);
		initTabsView();
	}

	private void initTabsView() {
		mViewPager = (ViewPager) findViewById(R.id.view_pager);
		mTabview = (TabLinearLayout)findViewById(R.id.tab);
		mTabview.addTab("先秦0", new OneFragment());
		mTabview.addTab("先秦1", new OneFragment());
		mTabview.addTab("先秦2", new OneFragment());
		mTabview.addTab("先秦3", new OneFragment());
		mTabview.addTab("先秦4", new OneFragment());
		mTabview.addTab("先秦5", new OneFragment());
		mTabview.addTab("先秦6", new OneFragment());
		mViewPager.setAdapter(new MyFragmentPagerAdapter(getSupportFragmentManager(), mTabview.getTabs()));
		mTabview.setViewPager(mViewPager);
		mViewPager.setCurrentItem(0);
	}
}
