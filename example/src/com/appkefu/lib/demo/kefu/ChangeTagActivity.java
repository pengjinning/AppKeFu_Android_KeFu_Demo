package com.appkefu.lib.demo.kefu;

import com.appkefu.lib.interfaces.KFInterfaces;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;
import android.app.Activity;

public class ChangeTagActivity extends Activity implements OnClickListener{

	private Button   changeBtn;
	private TextView profileFieldTextView;
	
	private String  profileField;
	private String 	profileValue;
	 
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_change_tag);
		
		changeBtn = (Button) findViewById(R.id.change_profile_btn);
		changeBtn.setOnClickListener(this);
		
		profileFieldTextView = (TextView) findViewById(R.id.change_profile_user_edit);
		
		profileField = getIntent().getStringExtra("profileField");
		profileValue = getIntent().getStringExtra("value");
		
		profileFieldTextView.setText(profileValue);
	}
	
	public void changeProfile()
	{		
		profileValue = profileFieldTextView.getText().toString();
		if(profileValue.length() <= 0)
		{
			Toast.makeText(this, "不能为空", Toast.LENGTH_SHORT).show();
			return;
		}	
		
		changeBtn.setEnabled(false);

		//设置Tag
		if(profileField.equals("NICKNAME"))
		{
			KFInterfaces.setTagNickname(profileValue);
		}
		else if(profileField.equals("SEX"))
		{
			KFInterfaces.setTagSex(profileValue);
		}
		else if(profileField.equals("LANGUAGE"))
		{
			KFInterfaces.setTagLanguage(profileValue);
		}
		else if(profileField.equals("CITY"))
		{
			KFInterfaces.setTagCity(profileValue);
		}
		else if(profileField.equals("PROVINCE"))
		{
			KFInterfaces.setTagProvince(profileValue);
		}
		else if(profileField.equals("COUNTRY"))
		{
			KFInterfaces.setTagCountry(profileValue);
		}
		else if(profileField.equals("OTHER"))
		{
			KFInterfaces.setTagOther(profileValue);
		}

		finish();
	}

	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()) {
		case R.id.change_profile_reback_btn:
			finish();
			break;
		case R.id.change_profile_btn:
			changeProfile();
			break;
		default:
			break;
		}	
	}


}
