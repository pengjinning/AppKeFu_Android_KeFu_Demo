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
 * ΢�ͷ�(AppKeFu.com)
 * 
 * ΢�ͷ������ɵ���App������߿ͷ�
 * �����׿�App������߿ͷ���֧�����֡����顢ͼƬ���������졣 ��־Ϊ�ƶ��������ṩ��õ����߿ͷ�
 * 
 * �˴��г��Ľӿھ�Ϊ��ѽӿ�
 * 
 * ��������QQȺ:48661516
 * 
 * �ͷ������ĵ��� http://appkefu.com/AppKeFu/tutorial-android.html
 * 
 * @author jack ning, http://github.com/pengjinning
 *
 */
public class FreeAPIDemoActivity extends Activity {
	
	private ListView 			 mApiListView;
	private ArrayList<ApiEntity> mApiArray;
	private ApiAdapter 			 mAdapter;
	private Button				 mBackBtn;
	
	//�ͷ��û�������Ҫ��дΪ��ʵ�Ŀͷ��û�������Ҫ�������̨(http://appkefu.com/AppKeFu/admin/),����
	private String 			  	 mKefuUsername;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_free_apidemo);
		
		//��admin�Ự,ʵ��Ӧ������Ҫ��admin�滻Ϊ��ʵ�Ŀͷ��û���			
		mKefuUsername = "admin";
		
		initView();
	}
	
	@Override
	protected void onResume() {
		super.onResume();
		
		//���ͷ��Ƿ�����
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
	 * ������ѯ�Ի���
	 * @param kefuUsername
	 */
	private void chatWithKeFu(String kefuUsername)
	{
		KFInterfaces.startChatWithKeFu(this,
					kefuUsername, //�ͷ��û���
					getString(R.string.appkefu_greetings), 
					getString(R.string.appkefu_chat));//�Ự���ڱ���
	}
	
	/**
	 * �򿪳�������FAQģ��
	 */
	private void showFAQ()
	{
		KFInterfaces.showFAQ(this);
	}
	
	/**
	 * �Զ����û���ǩ�����ڷÿ�ʶ��
	 */
	private void showTagList()
	{
		Intent intent = new Intent(this, TagListActivity.class);
		startActivity(intent);
	}
	
	/**
	 * ��ȡ�����¼
	 */
	private void showMessageRecords()
	{
		Intent intent = new Intent(this, MessageActivity.class);
		startActivity(intent);
	}
	
	/**
	 * �����ǳƣ������ڿͷ��ͻ��� �����Ļ���һ���ַ���(�����ڵ�¼�ɹ�֮����ܵ��ã�����Ч)
	 */
	private void changeNickname()
	{
		//�����ǳƣ������ڿͷ��ͻ��� �����Ļ���һ���ַ���(�����ڵ�¼�ɹ�֮����ܵ��ã�����Ч)
		KFInterfaces.setVisitorNickname("�ÿ�_΢�ͷ�(�ͷ�Demo)", this);
		
		Toast.makeText(this, "�ǳ��Ѿ��޸�", Toast.LENGTH_SHORT).show();
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

  				//���ͷ��Ƿ����ߣ���Ҫ�ڷ����߳���ִ��
  				msg.obj = KFInterfaces.isKeFuOnlineSync(mKefuUsername);
  				
  				handler.sendMessage(msg);
  			}
  		}
    	.start();
    }

	
}
