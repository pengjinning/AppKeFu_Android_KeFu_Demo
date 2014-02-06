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
	 ��ʾ������Ѿ����й��ɰ��Demo���������ֻ���ɾ��ԭ�ȵ�App���������д˹���
	 ����ʹ�ð����μ���http://appkefu.com/AppKeFu/tutorial-android.html
	
	 ��Ҫʹ��˵����
	 ��1������http://appkefu.com/AppKeFu/admin/��ע��/����Ӧ��/����ͷ���������ȡ��appkey����AnroidManifest.xml
	 		�е�com.appkefu.lib.appkey
	 ��2��������ʵ�Ŀͷ�����ʼ��mKefuUsername
	 ��3�������� KFInterfaces.visitorLogin(this); ������¼
	 ��4��������chatWithKeFu(mKefuUsername);��ͷ��Ự������mKefuUsername��Ҫ�滻Ϊ��ʵ�ͷ���
	 ��5����(��ѡ)
      	//�����ǳƣ������ڿͷ��ͻ��� �����Ļ���һ���ַ���(�����ڵ�¼�ɹ�֮����ܵ��ã�����Ч)
      	KFInterfaces.setVisitorNickname("�ÿ�1", this);
	 */
	
	private TextView mTitle;
	private Button   mChatBtn;
	private Button 	 mQChatBtn;
	private Button   mFAQBtn;
	
	
	//�ͷ��û�������Ҫ��дΪ��ʵ�Ŀͷ��û�������Ҫ�������̨(http://appkefu.com/AppKeFu/admin/),����
	private String 			  mKefuUsername;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//��admin�Ự,ʵ��Ӧ������Ҫ��admin�滻Ϊ��ʵ�Ŀͷ��û���			
		mKefuUsername = "admin";
		
		//��ʼ����Դ
		initView();
			
		//���ÿ����ߵ���ģʽ��Ĭ��Ϊtrue����Ҫ�رտ�����ģʽ��������Ϊfalse
		KFSettingsManager.getSettingsManager(this).setDebugMode(true);
		//��һ������¼
		KFInterfaces.visitorLogin(this);
	}
	
	@Override
	protected void onStart() {
		super.onStart();

		IntentFilter intentFilter = new IntentFilter();
		//�����������ӱ仯���
        intentFilter.addAction(KFMainService.ACTION_XMPP_CONNECTION_CHANGED);
        //������Ϣ
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
	 * ��Դ��ʼ��
	 */
	private void initView() 
	{		
		//�������
		mTitle = (TextView) findViewById(R.id.demo_title);
		
		//��ѯ�ͷ�button
		mChatBtn = (Button) findViewById(R.id.chat_button);
		mChatBtn.setOnClickListener(this);
		
		//
		mQChatBtn = (Button) findViewById(R.id.queue_chat_button);
		mQChatBtn.setOnClickListener(this);
		
		//
		mFAQBtn = (Button) findViewById(R.id.faq_button);
		mFAQBtn.setOnClickListener(this);
	}
	
	//����������״̬����ʱͨѶ��Ϣ���ͷ�����״̬
	private BroadcastReceiver mXmppreceiver = new BroadcastReceiver() 
	{
        public void onReceive(Context context, Intent intent) 
        {
            String action = intent.getAction();
            //����������״̬
            if (action.equals(KFMainService.ACTION_XMPP_CONNECTION_CHANGED))//��������״̬
            {
                updateStatus(intent.getIntExtra("new_state", 0));        
            }
            //��������ʱͨѶ��Ϣ
            else if(action.equals(KFMainService.ACTION_XMPP_MESSAGE_RECEIVED))//������Ϣ
            {
            	//��Ϣ����
            	String body = intent.getStringExtra("body");
            	//��Ϣ������
            	String from = StringUtils.parseName(intent.getStringExtra("from"));
            	
            	KFSLog.d("body:"+body+" from:"+from);
            }
        }
    };
    
	//������ѯ�Ի���
	private void chatWithKeFu(String kefuUsername)
	{
		KFInterfaces.startChatWithKeFu(this,
				kefuUsername, //�ͷ��û���
				"���ã�����΢�ͷ�С���飬������ʲô���԰�����?",  //�ʺ���
				"��ѯ�ͷ�");//�Ự���ڱ���
	}
	
	//�����ŶӻỰ
	private void startQueueChat(String workgrouName)
	{
		KFInterfaces.startQueueChat(this, workgrouName, "��ѯ�ͷ�");
	}

  //���ݼ����������ӱ仯������½�����ʾ
    private void updateStatus(int status) {

    	switch (status) {
            case KFXmppManager.CONNECTED:
            	mTitle.setText("΢�ͷ�(�ͷ�Demo)");

        		//�����ǳƣ������ڿͷ��ͻ��� �����Ļ���һ���ַ���(�����ڵ�¼�ɹ�֮����ܵ��ã�����Ч)
        		//KFInterfaces.setVisitorNickname("�ÿ�1", this);
        		
        		//
        		//Boolean isWorkgroupOnline = KFInterfaces.isWorkgroupOnline(this,"demo");
        		//if(isWorkgroupOnline)
        		//	KFSLog.d("isWorkgroupOnline");
        		//else
        		//	KFSLog.d("is not WorkgroupOnline");

                break;
            case KFXmppManager.DISCONNECTED:
            	KFSLog.d("disconnected");
            	mTitle.setText("΢�ͷ�(�ͷ�Demo)(δ����)");
                break;
            case KFXmppManager.CONNECTING:
            	KFSLog.d("connecting");
            	mTitle.setText("΢�ͷ�(�ͷ�Demo)(��¼��...)");
            	break;
            case KFXmppManager.DISCONNECTING:
            	KFSLog.d("connecting");
            	mTitle.setText("΢�ͷ�(�ͷ�Demo)(�ǳ���...)");
                break;
            case KFXmppManager.WAITING_TO_CONNECT:
            case KFXmppManager.WAITING_FOR_NETWORK:
            	KFSLog.d("waiting to connect");
            	mTitle.setText("΢�ͷ�(�ͷ�Demo)(�ȴ���)");
                break;
            default:
                throw new IllegalStateException();
        }
    }
    
    
    //���ͷ��Ƿ�����
    private void checkKeFuOnlineThread() 
    {
    	final Handler handler = new Handler() 
    	{
    		public void handleMessage(Message msg) 
  			{
    			Boolean isOnline = (Boolean)msg.obj;
    			if(isOnline)
    			{
    				mChatBtn.setText("��ѯ�ͷ�(����)");
    			}
    			else
    			{
    				mChatBtn.setText("��ѯ�ͷ�(����)");
    			}
  			}
    	};
    	
    	new Thread() 
  		{
  			public void run() 
  			{
  				Message msg = new Message();

  				//���ͷ��Ƿ����ߣ���Ҫ�ڷ����߳���ִ��
  				msg.obj = KFInterfaces.isKeFuOnline(mKefuUsername);
  				
  				handler.sendMessage(msg);
  			}
  		}.start();
    }
    
}








































