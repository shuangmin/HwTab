package com.csm.hwtab;

import java.util.ArrayList;
import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

import android.content.Context;
import android.graphics.Canvas;
import android.graphics.Color;
import android.graphics.Paint;
import android.graphics.Paint.Cap;
import android.graphics.Paint.Style;
import android.support.v4.app.Fragment;
import android.support.v4.view.ViewPager;
import android.support.v4.view.ViewPager.OnPageChangeListener;
import android.util.AttributeSet;
import android.util.DisplayMetrics;
import android.util.TypedValue;
import android.view.Gravity;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.FrameLayout;
import android.widget.HorizontalScrollView;
import android.widget.LinearLayout;
import android.widget.TextView;

public class TabHorizontalScrollView extends HorizontalScrollView implements OnClickListener, OnPageChangeListener {
	/**
	 * 最大可显示的Tab数，可写成自定义属性在xml里面配置
	 */
	private static final int MAX_VISIBLE_TAB_COUNTS = 5;
	/**
	 * 实际可见的Tab数，比如有8个Tab,超过8个，为了保证效果，最多显示5个
	 * mRealVisibleTabCounts为5，如果只有3个tab小于5个，则mRealVisibleTabCounts为3
	 * 并根据这个去计算每个tab的宽度，屏幕宽度除以mRealVisibleTabCounts嘛
	 */
	private int mRealVisibleTabCounts;
	/**
	 * 小圆点滑动偏移量
	 */
	private float mOffset;

	/**
	 * 绘制小圆点画笔
	 */
	private Paint mPaint;

	/**
	 * 小圆点半径
	 */
	private int mCircleRadius;
	/**
	 * 屏幕宽度
	 */
	private int mScreenWidth;

	/**
	 * 存放Tab的集合 Key:导航栏的标题 Fragment:导航页
	 */
	private Map<String, Fragment> mTabs;

	private Context mContext;

	private ViewPager mViewPager;
	/**
	 * TabHorizontalScrollView下面用于存放Tab的布局，
	 * 因为HorizontalScrollView只能包含一个子控件，所以需要包含一个LinearLayout
	 */
	private LinearLayout mSecondlayout;

	public TabHorizontalScrollView(Context context, AttributeSet attrs) {
		this(context, attrs, -1);
	}

	public TabHorizontalScrollView(Context context) {
		this(context, null);
	}

	public TabHorizontalScrollView(Context context, AttributeSet attrs, int defStyle) {
		super(context, attrs, defStyle);
		init();
	}

	private void init() {
		mContext = getContext();
		mSecondlayout = new LinearLayout(mContext);
		FrameLayout.LayoutParams lParams = new LayoutParams(ViewGroup.LayoutParams.WRAP_CONTENT,
				ViewGroup.LayoutParams.WRAP_CONTENT,Gravity.CENTER_VERTICAL);
		mSecondlayout.setLayoutParams(lParams);
		mSecondlayout.setOrientation(LinearLayout.HORIZONTAL);
		mSecondlayout.setGravity(Gravity.CENTER_VERTICAL);
		addView(mSecondlayout, lParams);
		mPaint = new Paint();
		mPaint.setAntiAlias(true);
		mPaint.setDither(true);
		mPaint.setStyle(Style.FILL);
		mPaint.setColor(Color.WHITE);
		mPaint.setStrokeCap(Cap.ROUND);

		DisplayMetrics metrics = getResources().getDisplayMetrics();
		mScreenWidth = metrics.widthPixels;
		mCircleRadius = (int) getResources().getDimension(R.dimen.radius);
	}

	@Override
	protected void onMeasure(int widthMeasureSpec, int heightMeasureSpec) {
		super.onMeasure(widthMeasureSpec, heightMeasureSpec);

		int tabCount = mSecondlayout.getChildCount();

		if (tabCount == 0) {
			return;
		}

		mRealVisibleTabCounts = MAX_VISIBLE_TAB_COUNTS < tabCount ? MAX_VISIBLE_TAB_COUNTS : tabCount;
		int averageWidth = mScreenWidth / mRealVisibleTabCounts;
		// 为每一个子view重新分配mScreenWidth / mRealVisibleTabCounts大小的宽度
		for (int i = 0; i < tabCount; i++) {
			View view = mSecondlayout.getChildAt(i);
			LinearLayout.LayoutParams params = (android.widget.LinearLayout.LayoutParams) view.getLayoutParams();
			params.weight = 0;
			params.width = averageWidth;
			view.setLayoutParams(params);
		}
	}

	/**
	 * 获得Tabs列表
	 * 
	 * @return
	 */
	public List<Fragment> getTabs() {
		return new ArrayList(mTabs.values());
	}

	/**
	 * 添加一个Tab项
	 * 
	 * @param title
	 * @param tab
	 */
	public void addTab(String title, Fragment tab) {
		if (mTabs == null) {
			mTabs = new LinkedHashMap<>();
		}
		mTabs.put(title, tab);
		mSecondlayout.addView(createTabItem(title, mTabs.size() - 1));
	}

	public void setViewPager(ViewPager viewPager) {
		mViewPager = viewPager;
		mViewPager.setOnPageChangeListener(this);
	}

	/**
	 * 创建一个Tab视图
	 * 
	 * @param title
	 *            Fragment对应的标题
	 * @param index
	 *            该Fragment对应的索引下标
	 * @return 已经构造好的tab
	 */
	private View createTabItem(String title, int index) {
		TextView tv_title = new TextView(mContext);
		tv_title.setText(title);
		tv_title.setTag(index);
		tv_title.setGravity(Gravity.CENTER);
		tv_title.setTextColor(Color.WHITE);
		tv_title.setTextSize(TypedValue.COMPLEX_UNIT_SP, 18);
		tv_title.setOnClickListener(this);
		return tv_title;
	}

	@Override
	public void onClick(View v) {
		int tabIndex = (Integer) v.getTag();
		if (onTabOperatorListener != null) {
			onTabOperatorListener.onTabClick(tabIndex);
		}
		mViewPager.setCurrentItem(tabIndex);
	}

	@Override
	public void onPageScrollStateChanged(int state) {
		if (null != onTabOperatorListener) {
			onTabOperatorListener.onPageScrollStateChanged(state);
		}
	}

	@Override
	public void onPageScrolled(int sourcePosition, float positionOffset, int positionOffsetPixels) {
		mRealVisibleTabCounts = MAX_VISIBLE_TAB_COUNTS < mSecondlayout.getChildCount() ? MAX_VISIBLE_TAB_COUNTS
				: mSecondlayout.getChildCount();
		int tabWidth = getWidth() / mRealVisibleTabCounts;
		// 计算小圆点的偏移量，可以画图理解
		mOffset = (int) (sourcePosition * tabWidth + positionOffset * tabWidth + tabWidth / 2);

		// 如果滚动到最后一个，则同时要向左滚动tab内容，这种情况是：原点在向右移动，同时整体又向做滑动，两个滑动相互抵消，相当于原点没有移动
		if (mSecondlayout.getChildCount() > mRealVisibleTabCounts && positionOffset > 0
				&& sourcePosition >= mRealVisibleTabCounts - 1) {
			scrollTo((int) ((sourcePosition + 1 - mRealVisibleTabCounts) * tabWidth + tabWidth * positionOffset), 0);
		}
		invalidate();

		if (null != onTabOperatorListener) {
			onTabOperatorListener.onTabScrolled(sourcePosition, positionOffset, positionOffsetPixels);
		}
	}

	@Override
	public void onPageSelected(int index) {
		if (null != onTabOperatorListener) {
			onTabOperatorListener.onTabSelected(index);
		}
		
		if(index == 0)
		{
			scrollTo(0, 0);
		}
	}

	/**
	 * Tab操作相关的监听器
	 * 
	 * @author rander
	 */
	public interface OnTabOperatorListener {
		void onTabClick(int tabIndex);

		void onTabSelected(int tabIndex);

		void onTabScrolled(int sourcePosition, float positionOffset, int positionOffsetPixels);

		void onPageScrollStateChanged(int state);
	}

	public OnTabOperatorListener onTabOperatorListener;

	public void setOnTabItemClickListener(OnTabOperatorListener onTabItemClickListener) {
		this.onTabOperatorListener = onTabItemClickListener;
	}

	@Override
	protected void dispatchDraw(Canvas canvas) {
		super.dispatchDraw(canvas);
		if (mSecondlayout.getChildCount() <= 1) {
			return;
		}
		canvas.drawCircle(mOffset, getHeight() - mCircleRadius * 2, mCircleRadius, mPaint);
	}

}
