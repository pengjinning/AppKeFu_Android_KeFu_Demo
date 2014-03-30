package com.appkefu.lib.demo.kefu;


import org.jivesoftware.smack.util.StringUtils;

import com.appkefu.lib.interfaces.KFInterfaces;
import com.appkefu.lib.service.KFMainService;
import com.appkefu.lib.service.KFXmppManager;

import android.os.Bundle;
import android.view.View;
import android.view.View.OnClickListener;
import android.widget.Button;
import android.widget.TextView;
import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;

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
public class MainActivity extends Activity implements OnClickListener{
  
	/**
	 * 提示：如果已经运行过旧版的Demo，请先在手机上删除原先的App再重新运行此工程
	 * 更多使用帮助参见：http://appkefu.com/AppKeFu/tutorial-android.html
	 * 
	 * 简要使用说明：
	 * 第1步：到http://appkefu.com/AppKeFu/admin/，注册/创建应用/分配客服，并将获取的appkey填入AnroidManifest.xml
	 * 		中的com.appkefu.lib.appkey
	 * 第2步：用真实的客服名初始化mKefuUsername
	 * 第3步：调用 KFInterfaces.visitorLogin(this); 函数登录
	 * 第4步：调用chatWithKeFu(mKefuUsername);与客服会话，其中mKefuUsername需要替换为真实客服名
	 * 第5步：(可选)
     *  	//设置昵称，否则在客服客户端 看到的会是一串字符串(必须在登录成功之后才能调用，才有效)
     *  	KFInterfaces.setVisitorNickname("访客1", this);
	 */            
	 
	private TextView mTitle;
	private Button  mFreeApiBtn;
	private Button  mVIPAPIBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//初始化资源
		initView();
		
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
	protected void onStop() {
		super.onStop();

        unregisterReceiver(mXmppreceiver);
	}
		
	@Override
	public void onClick(View view) {
		// TODO Auto-generated method stub
		Intent intent;
		switch(view.getId()) 
		{
		case R.id.free_api_button:
		    intent = new Intent(this, FreeAPIDemoActivity.class);
			startActivity(intent);
			break;
		case R.id.vip_api_button:
			intent = new Intent(this, VIPAPIDemoActivity.class);
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
		
		mFreeApiBtn = (Button) findViewById(R.id.free_api_button);
		mFreeApiBtn.setOnClickListener(this);
		
		mVIPAPIBtn = (Button) findViewById(R.id.vip_api_button);
		mVIPAPIBtn.setOnClickListener(this);
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
            	
            }
        }
    };
    
   //根据监听到的连接变化情况更新界面显示
    private void updateStatus(int status) {

    	switch (status) {
            case KFXmppManager.CONNECTED:
            	mTitle.setText("微客服(客服Demo)");

        		//设置昵称，否则在客服客户端 看到的会是一串字符串(必须在登录成功之后才能调用，才有效)
        		KFInterfaces.setVisitorNickname("访客_微客服(客服Demo)", this);

                break;
            case KFXmppManager.DISCONNECTED:
            	mTitle.setText("微客服(客服Demo)(未连接)");
                break;
            case KFXmppManager.CONNECTING:
            	mTitle.setText("微客服(客服Demo)(登录中...)");
            	break;
            case KFXmppManager.DISCONNECTING:
            	mTitle.setText("微客服(客服Demo)(登出中...)");
                break;
            case KFXmppManager.WAITING_TO_CONNECT:
            case KFXmppManager.WAITING_FOR_NETWORK:
            	mTitle.setText("微客服(客服Demo)(等待中)");
                break;
            default:
                throw new IllegalStateException();
        }
    }
    
}








































