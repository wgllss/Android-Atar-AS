package com.atar.push;
///**
// *****************************************************************************************************************************************************************************
// * 
// * @author :fengguangjing
// * @createTime:2016-4-6下午2:38:15
// * @version:3.13
// * @modifyTime:
// * @modifyAuthor:
// * @description:
// *****************************************************************************************************************************************************************************
// */
//package com..push;
//
//import android.common.ApplicationManagement;
//import android.common.ShowLog;
//import android.content.BroadcastReceiver;
//import android.content.Context;
//import android.content.Intent;
//
//import com..net.TPYTransferSyncImpl;
//
///**
// ***************************************************************************************************************************************************************************** 
// * 
// * @author :fengguangjing
// * @createTime:2016-4-6下午2:38:15
// * @version:3.13
// * @modifyTime:
// * @modifyAuthor:
// * @description:
// ***************************************************************************************************************************************************************************** 
// */
//public class AppAddedReceiver extends BroadcastReceiver {
//
//	@Override
//	public void onReceive(Context context, Intent intent) {
//		/**
//		 * get the id of download which have download success, if the id is my
//		 * id and it's status is successful, then install it
//		 **/
//		String action = intent.getAction();
//		if (action != null) {
//			if (action.equals(Intent.ACTION_PACKAGE_ADDED)) {
//				ShowLog.i("AppAddedReceiver", action);
//				String packageName = intent.getDataString().substring(8);
//				if (packageName.equals(TPYTransferSyncImpl.UrlTpyPackageName)) {
//					ApplicationManagement.startAppLaunchIntentForPackage(context, packageName);
//				}
//			} 
//		}
//
//	}
//
// }