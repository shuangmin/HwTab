package com.csm.hwtab;

import android.app.Activity;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.LinearLayout;

public class MainActivity extends Activity {
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		LinearLayout linearLayout = new LinearLayout(this);
		linearLayout.setOrientation(LinearLayout.VERTICAL);
		LinearLayout.LayoutParams lParams = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT,
				ViewGroup.LayoutParams.WRAP_CONTENT);
		Button button = new Button(this);
		button.setTag(1);
		button.setText("用LinearLayout实现的导航Tab");
		linearLayout.addView(button, lParams);
		button.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SelectActivity.class);
				intent.putExtra("type", (Integer) v.getTag());
				startActivity(intent);
			}
		});

		Button button2 = new Button(this);
		button2.setTag(2);
		button2.setText("用HorizontalScrollView实现的导航Tab");
		linearLayout.addView(button2, lParams);
		button2.setOnClickListener(new OnClickListener() {

			@Override
			public void onClick(View v) {
				Intent intent = new Intent();
				intent.setClass(MainActivity.this, SelectActivity.class);
				intent.putExtra("type", (Integer) v.getTag());
				startActivity(intent);
			}
		});

		setContentView(linearLayout);
	}
}
