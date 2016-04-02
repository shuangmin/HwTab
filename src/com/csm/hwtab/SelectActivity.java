package com.csm.hwtab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.LinearLayout;

public class SelectActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		int type = getIntent().getIntExtra("type", 1);
		final Class<?> clazz = (type == 1 ? LinearLayoutTabActivity.class : HorizontalScrollViewTabActivity.class);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		for (int i = 1; i < 10; i++) 
		{
			Button button = new Button(this);
			button.setTag(i);
			button.setText("有" + i + "个页面的情况");
			linearLayout.addView(button, lParams);
			button.setOnClickListener(new OnClickListener() {
				
				@Override
				public void onClick(View v) {
					Intent intent = new Intent();
					intent.setClass(SelectActivity.this, clazz);
					intent.putExtra("count", (Integer) v.getTag());
					startActivity(intent);
				}
			});
		}
		setContentView(linearLayout);
	}
}
