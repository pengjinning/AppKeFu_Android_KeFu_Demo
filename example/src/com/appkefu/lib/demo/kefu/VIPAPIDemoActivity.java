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

/**
 * ʹ�ô�DemoActivity�еĽӿڣ����ܻ��ʵ���ȡ����
 * ����Ҫ�Զ���Ự�������Ͻǰ�ť����Ҫ��Activity��ʵ��KFCallBack�ӿ� 
 */
public class VIPAPIDemoActivity extends Activity implements KFCallBack{

	private ListView 			 mApiListView;
	private ArrayList<ApiEntity> mApiArray;
	private ApiAdapter 			 mAdapter;
	private Button				 mBackBtn;
	
	private String				 mWorkgroupName;
	
	//�ͷ��û�������Ҫ��дΪ��ʵ�Ŀͷ��û�������Ҫ�������̨(http://appkefu.com/AppKeFu/admin/),����
	private String 			  	 mKefuUsername;
	
	@Override
	protected void onCreate(Bundle savedInstanceState) {
		super.onCreate(savedInstanceState);
		setContentView(R.layout.activity_vipapidemo);
		
		//
		mWorkgroupName = "demo";
		//��admin�Ự,ʵ��Ӧ������Ҫ��admin�滻Ϊ��ʵ�Ŀͷ��û���			
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

	//�����ŶӻỰ���˽ӿ��д��ڲ��Խ׶Σ������ڲ���
	private void startQueueChat(String workgrouName)
	{
		KFInterfaces.startQueueChat(
				this, //Context ������
				this, //ʵ����KFCallBack�ӿڵĻص�����, �������Ϊnull�����ػỰҳ�涥������
				workgrouName, //������ID
				getString(R.string.appkefu_chat)//�Ựҳ�涥������
				);
	}

	//KFCallBack�ص��ӿ�
	@Override
	public void OnTopDetailClicked() {
		// TODO Auto-generated method stub
		
		Intent intent = new Intent(this, VIPAPIDemoCallbackActivity.class);
		intent.putExtra("detail", getString(R.string.appkefu_vip_callbacks));
		startActivity(intent);
	} 

}









