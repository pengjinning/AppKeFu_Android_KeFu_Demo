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
 * ΢�ͷ�(AppKeFu.com)
 * 
 * ΢�ͷ������ɵ���App������߿ͷ�
 * �����׿�App������߿ͷ���֧�����֡����顢ͼƬ���������졣 ��־Ϊ�ƶ��������ṩ��õ����߿ͷ�
 *  
 * ��������QQȺ:48661516
 * 
 * �ͷ������ĵ��� http://appkefu.com/AppKeFu/tutorial-android.html
 * 
 * @author jack ning, http://github.com/pengjinning
 *
 */
public class MainActivity extends Activity implements OnClickListener{
  
	/**
	 * ��ʾ������Ѿ����й��ɰ��Demo���������ֻ���ɾ��ԭ�ȵ�App���������д˹���
	 * ����ʹ�ð����μ���http://appkefu.com/AppKeFu/tutorial-android.html
	 * 
	 * ��Ҫʹ��˵����
	 * ��1������http://appkefu.com/AppKeFu/admin/��ע��/����Ӧ��/����ͷ���������ȡ��appkey����AnroidManifest.xml
	 * 		�е�com.appkefu.lib.appkey
	 * ��2��������ʵ�Ŀͷ�����ʼ��mKefuUsername
	 * ��3�������� KFInterfaces.visitorLogin(this); ������¼
	 * ��4��������chatWithKeFu(mKefuUsername);��ͷ��Ự������mKefuUsername��Ҫ�滻Ϊ��ʵ�ͷ���
	 * ��5����(��ѡ)
     *  	//�����ǳƣ������ڿͷ��ͻ��� �����Ļ���һ���ַ���(�����ڵ�¼�ɹ�֮����ܵ��ã�����Ч)
     *  	KFInterfaces.setVisitorNickname("�ÿ�1", this);
	 */            
	 
	private TextView mTitle;
	private Button  mFreeApiBtn;
	private Button  mVIPAPIBtn;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_main);

		//��ʼ����Դ
		initView();
		
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
	 * ��Դ��ʼ��
	 */
	private void initView() 
	{		
		//�������
		mTitle = (TextView) findViewById(R.id.demo_title);
		
		mFreeApiBtn = (Button) findViewById(R.id.free_api_button);
		mFreeApiBtn.setOnClickListener(this);
		
		mVIPAPIBtn = (Button) findViewById(R.id.vip_api_button);
		mVIPAPIBtn.setOnClickListener(this);
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
            	
            }
        }
    };
    
   //���ݼ����������ӱ仯������½�����ʾ
    private void updateStatus(int status) {

    	switch (status) {
            case KFXmppManager.CONNECTED:
            	mTitle.setText("΢�ͷ�(�ͷ�Demo)");

        		//�����ǳƣ������ڿͷ��ͻ��� �����Ļ���һ���ַ���(�����ڵ�¼�ɹ�֮����ܵ��ã�����Ч)
        		KFInterfaces.setVisitorNickname("�ÿ�_΢�ͷ�(�ͷ�Demo)", this);

                break;
            case KFXmppManager.DISCONNECTED:
            	mTitle.setText("΢�ͷ�(�ͷ�Demo)(δ����)");
                break;
            case KFXmppManager.CONNECTING:
            	mTitle.setText("΢�ͷ�(�ͷ�Demo)(��¼��...)");
            	break;
            case KFXmppManager.DISCONNECTING:
            	mTitle.setText("΢�ͷ�(�ͷ�Demo)(�ǳ���...)");
                break;
            case KFXmppManager.WAITING_TO_CONNECT:
            case KFXmppManager.WAITING_FOR_NETWORK:
            	mTitle.setText("΢�ͷ�(�ͷ�Demo)(�ȴ���)");
                break;
            default:
                throw new IllegalStateException();
        }
    }
    
}








































