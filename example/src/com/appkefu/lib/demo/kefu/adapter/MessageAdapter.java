package com.appkefu.lib.demo.kefu.adapter;

import java.util.List;

import com.appkefu.lib.demo.kefu.R;
import com.appkefu.lib.interfaces.KFIMInterfaces;
import com.appkefu.lib.service.KFSettingsManager;
import com.appkefu.lib.ui.entity.KFChatEntity;
import com.appkefu.lib.ui.entity.KFConversationEntity;
import com.appkefu.lib.utils.KFImageUtils;
import com.appkefu.lib.utils.KFLog;
import com.appkefu.lib.xmpp.XmppVCard;

import android.annotation.SuppressLint;
import android.content.Context;
import android.graphics.Bitmap;
import android.graphics.Color;
import android.os.Handler;
import android.os.Message;
import android.util.Log;
import android.view.ContextMenu;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.view.ContextMenu.ContextMenuInfo;
import android.view.View.OnCreateContextMenuListener;
import android.widget.BaseAdapter;
import android.widget.ImageView;
import android.widget.TextView;

/**
 * 
 * @author Administrator
 *
 */
public class MessageAdapter extends BaseAdapter {

	private static final String TAG = MessageAdapter.class.getSimpleName();
	
	private Context context;
	private List<KFChatEntity> list;
	private LayoutInflater inflater;

	public MessageAdapter(Context context, List<KFChatEntity> list) {
		// TODO Auto-generated constructor stub
		this.context = context;
		this.list = list;
		inflater = LayoutInflater.from(context);
	}

	@Override
	public int getCount() {
		// TODO Auto-generated method stub
		return list.size();
	}

	@Override
	public Object getItem(int position) {
		// TODO Auto-generated method stub
		return list.get(position);
	}

	@Override
	public long getItemId(int position) {
		// TODO Auto-generated method stub
		return position;
	}

	// 通过对象移除
	public void remove(KFConversationEntity entity) {
		list.remove(entity);
	}

	@Override
	public View getView(int position, View convertView, ViewGroup parent) {
		// TODO Auto-generated method stub
		
		ViewHolder holder;
		if (convertView == null) {
			convertView = inflater.inflate(R.layout.message_list_item, null);
			holder = new ViewHolder();
			holder.icon = (ImageView) convertView.findViewById(R.id.recent_userhead);
			holder.name = (TextView) convertView.findViewById(R.id.recent_name);
			holder.date = (TextView) convertView.findViewById(R.id.recent_time);
			holder.msg = (TextView) convertView.findViewById(R.id.recent_msg);
			convertView.setTag(holder);
		} else {
			holder = (ViewHolder) convertView.getTag();
		}
		
		final KFChatEntity entity = list.get(position);

		KFLog.d("conv.id "+entity.getId()+ " conv.name:"+entity.getName());

		//首先获取昵称，如果昵称未设置，则显示对方用户名
		if(entity.isSend() == 1)//自己发送的消息
		{
			holder.name.setText("自己发送的消息");
			
			//设置自己的头像
			updateVCardThread(KFSettingsManager.getSettingsManager(context).getUsername(), holder.icon);
		}
		else
		{//收到对方的消息
			String nickname = KFIMInterfaces.getOtherNickname(entity.getName());
			if(nickname == null)
			{
				nickname = entity.getName();
			}
			holder.name.setText("收到来自:"+nickname+"的消息");
			
			//设置对方的头像
			updateVCardThread(entity.getName(), holder.icon);
		}

		holder.name.setTextColor(Color.BLACK);
		holder.date.setText(entity.getDate());
		holder.msg.setText("消息ID:"+entity.getId()+" 消息内容:"+entity.getText());
		
		if(entity.getText().endsWith(".amr"))
		{
			//语音
		}
		
		convertView.setOnCreateContextMenuListener(new OnCreateContextMenuListener(){

			@Override
			public void onCreateContextMenu(ContextMenu menu, View v,
					ContextMenuInfo menuInfo) {
				// TODO Auto-generated method stub
				//Log.d(TAG, "setOnCreateContextMenuListener");								
			}
		});
		
		return convertView;
	}

	static class ViewHolder {
		public ImageView icon;
		public TextView name;
		public TextView date;
		public TextView msg;
	}
	
	@SuppressLint("HandlerLeak")
	private void updateVCardThread(final String username, final ImageView avatar) {
		
		final Handler handler = new Handler() 
		{
			public void handleMessage(Message msg) 
			{
				if(msg.what == 1)
				{
					String avatarPath = (String)msg.obj;
					Bitmap bitmap = KFImageUtils.getBitmapByPath(avatarPath);
					if(bitmap != null)
						avatar.setImageBitmap(bitmap);
					else
						avatar.setImageResource(R.drawable.app_panel_friendcard_icon);
				}
				
			}
		};
		
		new Thread() 
		{
			public void run() 
			{
				Message msg = new Message();			
				
				//获取头像地址
				String avatarPath = XmppVCard.getOthersAvatarPath(username);
		
				msg.what = 1; 
				msg.obj = avatarPath; 
								
				handler.sendMessage(msg);
			}
		}.start();
		
	}

}

















