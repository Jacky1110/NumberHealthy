package com.jotangi.NumberHealthy.Module;

import android.app.Notification;
import android.app.NotificationChannel;
import android.app.NotificationManager;
import android.app.PendingIntent;
import android.content.Context;
import android.content.Intent;
import android.graphics.BitmapFactory;
import android.os.Build;
import android.util.Log;

/**
 * 使用到Intent時，可能會需要的設置
 *     nextIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TOP);
 *     nextIntent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK);
 *     nextIntent.setFlags(Intent.FLAG_ACTIVITY_CLEAR_TASK);
 * */
public class NotificationMethod {
	private Context mContext;
	private final static String DEFAULT_CHANNEL_ID = "DefaultChannel0";
	private final static String DEFAULT_CHANNEL_NAME = "DefaultChannel";
	private NotificationChannel defaultChannel;
	
	public NotificationMethod(Context context){
		mContext = context;
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			defaultChannel = new NotificationChannel(
					DEFAULT_CHANNEL_ID, DEFAULT_CHANNEL_NAME,
					NotificationManager.IMPORTANCE_HIGH);
			//setDescription==>Sets the user visible description of this channel.
			defaultChannel.setDescription(DEFAULT_CHANNEL_NAME);
			//enableLights==>Sets whether notifications posted to this channel should display notification lights, on devices that support that feature.
			defaultChannel.enableLights(true);
			defaultChannel.enableVibration(true);

			// 取得系統的通知服務
			final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE);
			if(notificationManager!= null){
				notificationManager.createNotificationChannel(defaultChannel);

			}
		}
	}


	public void sendNormalNotification(String title , String content, int iconRes){
		sendNormalNotification(title, content, iconRes, null);
	}

	/**
	 * @param iconRes 圖片res
	 * @param intent 點擊後想前往的頁面
	 * @author H.H LIN <hermitnull@gmail.com>
	 */
	public void sendNormalNotification(String title , String content, int iconRes, Intent intent){
		final int notifyID = mContext.hashCode(); // 通知的識別號碼
		final NotificationManager notificationManager = (NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務

		if(notificationManager != null){
			notificationManager.notify(notifyID, getNormalNotification(title, content, iconRes, intent)); // 發送通知
		}
	}

	/**
	 * @param iconRes 圖片res
	 * @param intent 點擊後想前往的頁面
	 * */
	public Notification getNormalNotification(String title , String content, int iconRes, Intent intent){
		if(intent == null){
			intent = new Intent();
		}

		final int requestCode = mContext.hashCode(); // PendingIntent的Request Code
		// ONE_SHOT：PendingIntent只使用一次；
		// CANCEL_CURRENT：PendingIntent執行前會先結束掉之前的；
		// NO_CREATE：沿用先前的PendingIntent，不建立新的PendingIntent；
		// UPDATE_CURRENT：更新先前PendingIntent所帶的額外資料，並繼續沿用
//		final int flags = PendingIntent.FLAG_CANCEL_CURRENT;
//		final PendingIntent pendingIntent = PendingIntent.getActivity(mContext.getApplicationContext(), requestCode, intent, flags); // 取得PendingIntent

        PendingIntent pendingIntent;
        intent.addFlags(Intent.FLAG_ACTIVITY_SINGLE_TOP | Intent.FLAG_ACTIVITY_CLEAR_TOP);
        pendingIntent = PendingIntent.getActivity(mContext, requestCode, intent, PendingIntent.FLAG_IMMUTABLE);

		final Notification.Builder notificationBuilder = new Notification.Builder(mContext)
				.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),iconRes))
				.setSmallIcon(iconRes)
				.setContentTitle(title)
				.setContentIntent(pendingIntent)
				.setContentText(content);
        notificationBuilder.setChannelId(DEFAULT_CHANNEL_ID);
		return notificationBuilder.build();
	}

	/**
	 * 無設定推播Icon 會有Icon解析度的問題
	 * @param intent 點擊後想前往的頁面
	 * */
	public Notification getNormalNotification(String title , String content, Intent intent){
		if(intent == null){
			intent = new Intent();
		}

		final int requestCode = mContext.hashCode(); // PendingIntent的Request Code
		// ONE_SHOT：PendingIntent只使用一次；
		// CANCEL_CURRENT：PendingIntent執行前會先結束掉之前的；
		// NO_CREATE：沿用先前的PendingIntent，不建立新的PendingIntent；
		// UPDATE_CURRENT：更新先前PendingIntent所帶的額外資料，並繼續沿用
		final int flags = PendingIntent.FLAG_CANCEL_CURRENT;
		final PendingIntent pendingIntent = PendingIntent.getActivity(mContext.getApplicationContext(), requestCode, intent, flags); // 取得PendingIntent

		final Notification.Builder notificationBuilder = new Notification.Builder(mContext)
				.setLargeIcon(BitmapFactory.decodeResource(mContext.getResources(),mContext.getApplicationInfo().icon))
				.setSmallIcon(mContext.getApplicationInfo().icon)
				.setContentTitle(title)
				.setContentIntent(pendingIntent)
				.setContentText(content);
		if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.O) {
			notificationBuilder.setChannelId(DEFAULT_CHANNEL_ID);
		}
		return notificationBuilder.build();
	}


	/**
	 * 
	 * @param iconRes 圖片res
	 * @param progress -1:顯示無進度ProgressBar
	 * @author H.H LIN <hermitnull@gmail.com>
	 */
	public void sendProgressNotification(String title , String content, int iconRes, int progress){
		int notifyID = mContext.hashCode(); // 通知的識別號碼
		int requestCode = notifyID; // PendingIntent的Request Code
		Intent intent = new Intent(); // 目前Activity的Intent
		
		// ONE_SHOT：PendingIntent只使用一次；
		// CANCEL_CURRENT：PendingIntent執行前會先結束掉之前的；
		// NO_CREATE：沿用先前的PendingIntent，不建立新的PendingIntent；
		// UPDATE_CURRENT：更新先前PendingIntent所帶的額外資料，並繼續沿用
		int flags = PendingIntent.FLAG_CANCEL_CURRENT;
		PendingIntent pendingIntent = PendingIntent.getActivity(
				mContext.getApplicationContext(), requestCode,intent , flags); // 取得PendingIntent
		
		NotificationManager notificationManager =
				(NotificationManager) mContext.getSystemService(Context.NOTIFICATION_SERVICE); // 取得系統的通知服務
		Notification notification = new Notification.Builder(mContext)
				.setSmallIcon(iconRes)
				.setContentTitle(title)
				.setContentText(content)
				.setProgress(100, progress, progress == -1? true:false)//true:顯示不確定進度的進度條；false：依照progress顯示進度條
				.setContentIntent(pendingIntent)
				.build(); // 建立通知
		
		notificationManager.notify(notifyID, notification); // 發送通知
	}



}
