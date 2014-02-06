package com.appkefu.lib.demo.kefu;


import org.jivesoftware.smack.util.StringUtils;

import com.appkefu.lib.interfaces.KFInterfaces;
import com.appkefu.lib.service.KFMainService;
import com.appkefu.lib.service.KFSettingsManager;
import com.appkefu.lib.service.KFXmppManager;
import com.appkefu.lib.ui.activity.KFFAQSectionsActivity;
import com.appkefu.lib.utils.KFSLog;

import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

public class MainActivity extends Activity implements OnClickListener{
 
	/*
	 提示：如果已经运行过旧版的Demo，请先在手机上删除原先的App再重新运行此工程
	 更多使用帮助参见：http://appkefu.com/AppKeFu/tutorial-android.html
	
	 简要使用说明：
	 第1步：到http://appkefu.com/AppKeFu/admin/，注册/创建应用/分配客服，并将获取的appkey填入AnroidManifest.xml
	 		中的com.appkefu.lib.appkey
	 第2步：用真实的客服名初始化mKefuUsername
	 第3步：调用 KFInterfaces.visitorLogin(this); 函数登录
	 第4步：调用chatWithKeFu(mKefuUsername);与客服会话，其中mKefuUsername需要替换为真实客服名
	 第5步：(可选)
      	//设置昵称，否则在客服客户端 看到的会是一串字符串(必须在登录成功之后才能调用，才有效)
      	KFInterfaces.setVisitorNickname("访客1", this);
	 */
	
	private TextView mTitle;
	private Button   mChatBtn;
	private Button 	 mQChatBtn;
	private Button   mFAQBtn;
	
	
	//客服用户名，需要填写为真实的客服用户名，需要到管理后台(http://appkefu.com/AppKeFu/admin/),分配
	private String 			  mKefuUsername;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//与admin会话,实际应用中需要将admin替换为真实的客服用户名			
		mKefuUsername = "admin";
		
		//初始化资源
		initView();
			
		//设置开发者调试模式，默认为true，如要关闭开发者模式，请设置为false
		KFSettingsManager.getSettingsManager(this).setDebugMode(true);
		//第一步：登录
		KFInterfaces.visitorLogin(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		IntentFilter intentFilter = new IntentFilter();
		//监听网络连接变化情况
        intentFilter.addAction(KFMainService.ACTION_XMPP_CONNECTION_CHANGED);
        //监听消息
        intentFilter.addAction(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED);
        
        registerReceiver(mXmppreceiver, intentFilter); 

	} 
	
	@Override
	protected void onResume() {
		super.onResume();
		
		checkKeFuOnlineThread();
	}

	@Override
	protected void onStop() {
		super.onStop();

        unregisterReceiver(mXmppreceiver);
	}
		
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		switch(view.getId()) 
		{
		case R.id.chat_button:
			chatWithKeFu(mKefuUsername);			
			break;
		case R.id.queue_chat_button:
			startQueueChat("demo");
			break;
		case R.id.faq_button:
			Intent intent = new Intent(this, KFFAQSectionsActivity.class);
			startActivity(intent);
			break;
		default:
			break;
		}
	} 
	
	/**
	 * 资源初始化
	 */
	private void initView() 
	{		
		//界面标题
		mTitle = (TextView) findViewById(R.id.demo_title);
		
		//咨询客服button
		mChatBtn = (Button) findViewById(R.id.chat_button);
		mChatBtn.setOnClickListener(this);
		
		//
		mQChatBtn = (Button) findViewById(R.id.queue_chat_button);
		mQChatBtn.setOnClickListener(this);
		
		//
		mFAQBtn = (Button) findViewById(R.id.faq_button);
		mFAQBtn.setOnClickListener(this);
	}
	
	//监听：连接状态、即时通讯消息、客服在线状态
	private BroadcastReceiver mXmppreceiver = new BroadcastReceiver() 
	{
        public void onReceive(Context context, Intent intent) 
        {
            String action = intent.getAction();
            //监听：连接状态
            if (action.equals(KFMainService.ACTION_XMPP_CONNECTION_CHANGED))//监听链接状态
            {
                updateStatus(intent.getIntExtra("new_state", 0));        
            }
            //监听：即时通讯消息
            else if(action.equals(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED))//监听消息
            {
            	//消息内容
            	String body = intent.getStringExtra("body");
            	//消息来自于
            	String from = StringUtils.parseName(intent.getStringExtra("from"));
            	
            	KFSLog.d("body:"+body+" from:"+from);
            }
        }
    };
    
	//启动咨询对话框
	private void chatWithKeFu(String kefuUsername)
	{
		KFInterfaces.startChatWithKeFu(this,
				kefuUsername, //客服用户名
				"您好，我是微客服小秘书，请问有什么可以帮您的?",  //问候语
				"咨询客服");//会话窗口标题
	}
	
	//开启排队会话
	private void startQueueChat(String workgrouName)
	{
		KFInterfaces.startQueueChat(this, workgrouName, "咨询客服");
	}

  //根据监听到的连接变化情况更新界面显示
    private void updateStatus(int status) {

    	switch (status) {
            case KFXmppManager.CONNECTED:
            	mTitle.setText("微客服(客服Demo)");

        		//设置昵称，否则在客服客户端 看到的会是一串字符串(必须在登录成功之后才能调用，才有效)
        		//KFInterfaces.setVisitorNickname("访客1", this);
        		
        		//
        		//Boolean isWorkgroupOnline = KFInterfaces.isWorkgroupOnline(this,"demo");
        		//if(isWorkgroupOnline)
        		//	KFSLog.d("isWorkgroupOnline");
        		//else
        		//	KFSLog.d("is not WorkgroupOnline");

                break;
            case KFXmppManager.DISCONNECTED:
            	KFSLog.d("disconnected");
            	mTitle.setText("微客服(客服Demo)(未连接)");
                break;
            case KFXmppManager.CONNECTING:
            	KFSLog.d("connecting");
            	mTitle.setText("微客服(客服Demo)(登录中...)");
            	break;
            case KFXmppManager.DISCONNECTING:
            	KFSLog.d("connecting");
            	mTitle.setText("微客服(客服Demo)(登出中...)");
                break;
            case KFXmppManager.WAITING_TO_CONNECT:
            case KFXmppManager.WAITING_FOR_NETWORK:
            	KFSLog.d("waiting to connect");
            	mTitle.setText("微客服(客服Demo)(等待中)");
                break;
            default:
                throw new IllegalStateException();
        }
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
    				mChatBtn.setText("咨询客服(在线)");
    			}
    			else
    			{
    				mChatBtn.setText("咨询客服(离线)");
    			}
  			}
    	};
    	
    	new Thread() 
  		{
  			public void run() 
  			{
  				Message msg = new Message();

  				//检测客服是否在线，需要在非主线程中执行
  				msg.obj = KFInterfaces.isKeFuOnline(mKefuUsername);
  				
  				handler.sendMessage(msg);
  			}
  		}.start();
    }
    
}








































