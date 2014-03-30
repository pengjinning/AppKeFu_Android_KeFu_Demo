package com.appkefu.lib.demo.kefu;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;

public class VIPAPIDemoCallbackActivity extends Activity {

	private Button				 mBackBtn;
	private TextView			 mDetailTextView;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vipapidemo_callback);
		
		String detail = getIntent().getStringExtra("detail");
		mDetailTextView = (TextView) findViewById(R.id.detail_textview);
		mDetailTextView.setText(detail);
		
		mBackBtn = (Button) findViewById(R.id.reback_btn);
		mBackBtn.setOnClickListener(new OnClickListener() {
			
			@Override
			public void onClick(View view) {
				// TODO Auto-generated method stub
				switch(view.getId()) {
				case R.id.reback_btn:
					finish();
					break;
				default:
					break;
				}
			}
		});
		
	}


}
