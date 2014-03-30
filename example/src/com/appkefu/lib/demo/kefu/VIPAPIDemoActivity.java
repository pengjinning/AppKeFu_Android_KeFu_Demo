package com.appkefu.lib.demo.kefu;

import java.util.ArrayList;

import com.appkefu.lib.demo.kefu.adapter.ApiAdapter;
import com.appkefu.lib.demo.kefu.entity.ApiEntity;
import com.appkefu.lib.interfaces.KFCallBack;
import com.appkefu.lib.interfaces.KFInterfaces;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.app.Activity;
import android.content.Intent;

/**
 * 微客服(AppKeFu.com)
 * 
 * 微客服，集成到您App里的在线客服
 * 国内首款App里的在线客服，支持文字、表情、图片、语音聊天。 立志为移动开发者提供最好的在线客服
 * 
 * 技术交流QQ群:48661516
 * 
 * 客服开发文档： http://appkefu.com/AppKeFu/tutorial-android.html
 * 
 * @author jack ning, http://github.com/pengjinning
 * 
 */

/**
 * 使用此DemoActivity中的接口，可能会适当收取费用
 * 如需要自定义会话界面右上角按钮，需要在Activity中实现KFCallBack接口 
 */
public class VIPAPIDemoActivity extends Activity implements KFCallBack{

	private ListView 			 mApiListView;
	private ArrayList<ApiEntity> mApiArray;
	private ApiAdapter 			 mAdapter;
	private Button				 mBackBtn;
	
	private String				 mWorkgroupName;
	
	//客服用户名，需要填写为真实的客服用户名，需要到管理后台(http://appkefu.com/AppKeFu/admin/),分配
	private String 			  	 mKefuUsername;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vipapidemo);
		
		//
		mWorkgroupName = "demo";
		//与admin会话,实际应用中需要将admin替换为真实的客服用户名			
		mKefuUsername = "admin";
		
		initView();
	}
	
	private void initView() {
		
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
		
		mApiListView = (ListView)findViewById(R.id.vip_api_list_view);
		mApiArray = new ArrayList<ApiEntity>();
		
		
		mAdapter = new ApiAdapter(this,  mApiArray);
		mApiListView.setAdapter(mAdapter);
		
		ApiEntity entity = new ApiEntity(1, getString(R.string.appkefu_vip_top_right));
		mApiArray.add(entity);
		entity = new ApiEntity(2, getString(R.string.appkefu_vip_apis));
		mApiArray.add(entity);
		
		mAdapter.notifyDataSetChanged();
		mApiListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index, long id) {
				// TODO Auto-generated method stub
				
				ApiEntity entity = mApiArray.get(index);
				
				switch(entity.getId()) {
				case 1:
					startOneToOneChatVIP(mKefuUsername);
					break;
				case 2:
					startQueueChat(mWorkgroupName);
					break;
				default:
					break;
				};
			}
		});
	}
	
	private void startOneToOneChatVIP(String username)
	{
		KFInterfaces.startChatWithKeFuVIP(
				this,
				this, 
				username, 
				getString(R.string.appkefu_greetings), 
				getString(R.string.appkefu_chat));
	}

	//开启排队会话，此接口尚处于测试阶段，仅限于测试
	private void startQueueChat(String workgrouName)
	{
		KFInterfaces.startQueueChat(
				this, //Context 上下文
				this, //实现了KFCallBack接口的回调对象, 如果设置为null则隐藏会话页面顶部详情
				workgrouName, //工作组ID
				getString(R.string.appkefu_chat)//会话页面顶部标题
				);
	}

	//KFCallBack回调接口
	@Override
	public void OnTopDetailClicked() {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent(this, VIPAPIDemoCallbackActivity.class);
		intent.putExtra("detail", getString(R.string.appkefu_vip_callbacks));
		startActivity(intent);
	} 

}









