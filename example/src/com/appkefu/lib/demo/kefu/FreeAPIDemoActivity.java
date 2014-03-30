package com.appkefu.lib.demo.kefu;

import java.util.ArrayList;

import com.appkefu.lib.demo.kefu.adapter.ApiAdapter;
import com.appkefu.lib.demo.kefu.entity.ApiEntity;
import com.appkefu.lib.interfaces.KFInterfaces;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.AdapterView;
import android.widget.Button;
import android.widget.ListView;
import android.widget.AdapterView.OnItemClickListener;
import android.widget.Toast;
import android.app.Activity;
import android.content.Intent;

/**
 * 微客服(AppKeFu.com)
 * 
 * 微客服，集成到您App里的在线客服
 * 国内首款App里的在线客服，支持文字、表情、图片、语音聊天。 立志为移动开发者提供最好的在线客服
 * 
 * 此处列出的接口均为免费接口
 * 
 * 技术交流QQ群:48661516
 * 
 * 客服开发文档： http://appkefu.com/AppKeFu/tutorial-android.html
 * 
 * @author jack ning, http://github.com/pengjinning
 *
 */
public class FreeAPIDemoActivity extends Activity {
	
	private ListView 			 mApiListView;
	private ArrayList<ApiEntity> mApiArray;
	private ApiAdapter 			 mAdapter;
	private Button				 mBackBtn;
	
	//客服用户名，需要填写为真实的客服用户名，需要到管理后台(http://appkefu.com/AppKeFu/admin/),分配
	private String 			  	 mKefuUsername;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_free_apidemo);
		
		//与admin会话,实际应用中需要将admin替换为真实的客服用户名			
		mKefuUsername = "admin";
		
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//检测客服是否在线
		checkKeFuOnlineThread();
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
		
		mApiListView = (ListView)findViewById(R.id.free_api_list_view);
		mApiArray = new ArrayList<ApiEntity>();
		
		mAdapter = new ApiAdapter(this,  mApiArray);
		mApiListView.setAdapter(mAdapter);
		
		ApiEntity entity = new ApiEntity(1, getString(R.string.appkefu_free_apis));
		mApiArray.add(entity);
			entity = new ApiEntity(2, getString(R.string.appkefu_free_faq));
		mApiArray.add(entity);
			entity = new ApiEntity(3, getString(R.string.appkefu_free_tags));
		mApiArray.add(entity);
			entity = new ApiEntity(4, getString(R.string.kefu_message_records));
		mApiArray.add(entity);
			entity = new ApiEntity(5, getString(R.string.kefu_change_nickname));
		mApiArray.add(entity);
			
		mAdapter.notifyDataSetChanged();
		mApiListView.setOnItemClickListener(new OnItemClickListener(){

			@Override
			public void onItemClick(AdapterView<?> arg0, View view, int index, long id) {
				// TODO Auto-generated method stub
				
				ApiEntity entity = mApiArray.get(index);
				
				switch(entity.getId()) {
				case 1:
					chatWithKeFu(mKefuUsername);
					break;
				case 2:
					showFAQ();
					break;
				case 3:
					showTagList();
					break;
				case 4:
					showMessageRecords();
					break;
				case 5:
					changeNickname();
					break;
				default:
					break;
				};
			}
		});
	}

	/**
	 * 启动咨询对话框
	 * @param kefuUsername
	 */
	private void chatWithKeFu(String kefuUsername)
	{
		KFInterfaces.startChatWithKeFu(this,
					kefuUsername, //客服用户名
					getString(R.string.appkefu_greetings), 
					getString(R.string.appkefu_chat));//会话窗口标题
	}
	
	/**
	 * 打开常见问题FAQ模块
	 */
	private void showFAQ()
	{
		KFInterfaces.showFAQ(this);
	}
	
	/**
	 * 自定义用户标签，便于访客识别
	 */
	private void showTagList()
	{
		Intent intent = new Intent(this, TagListActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 获取聊天记录
	 */
	private void showMessageRecords()
	{
		Intent intent = new Intent(this, MessageActivity.class);
		startActivity(intent);
	}
	
	/**
	 * 设置昵称，否则在客服客户端 看到的会是一串字符串(必须在登录成功之后才能调用，才有效)
	 */
	private void changeNickname()
	{
		//设置昵称，否则在客服客户端 看到的会是一串字符串(必须在登录成功之后才能调用，才有效)
		KFInterfaces.setVisitorNickname("访客_微客服(客服Demo)", this);
		
		Toast.makeText(this, "昵称已经修改", Toast.LENGTH_SHORT).show();
	}
	
    //检测客服是否在线
    private void checkKeFuOnlineThread() 
    {
    	final Handler handler = new Handler() 
    	{
    		public void handleMessage(Message msg) 
  			{
    			Boolean isOnline = (Boolean)msg.obj;
    			if(isOnline)
    			{
    				mApiArray.get(0).setApiName(getString(R.string.kefu_online));
    			}
    			else
    			{
    				mApiArray.get(0).setApiName(getString(R.string.kefu_offline));
    			}

    			mAdapter.notifyDataSetChanged();
  			}
    	};
    	
    	new Thread() 
  		{
  			public void run() 
  			{
  				Message msg = new Message();

  				//检测客服是否在线，需要在非主线程中执行
  				msg.obj = KFInterfaces.isKeFuOnlineSync(mKefuUsername);
  				
  				handler.sendMessage(msg);
  			}
  		}
    	.start();
    }

	
}
