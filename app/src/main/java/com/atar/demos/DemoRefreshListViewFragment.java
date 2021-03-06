/**
 * 
 */
package com.atar.demos;

import java.util.ArrayList;
import java.util.List;

import android.os.Bundle;
import android.os.Message;
import android.reflection.NetWorkMsg;
import android.view.View;

import com.atar.adapters.WonderfulTopicAdapter;
import com.atar.beans.WonderfulTopicBean;
import com.atar.enums.EnumMsgWhat;
import com.atar.fragment.AtarRefreshListFragment;
import com.atar.modles.WonderfulTopicJson;
import com.atar.net.NetWorkInterfaces;
import com.atar.utils.LoadUtil;
import com.google.gson.reflect.TypeToken;

/**
 *****************************************************************************************************************************************************************************
 * 
 * @author :Atar
 * @createTime:2017-8-16上午11:02:22
 * @version:1.0.0
 * @modifyTime:
 * @modifyAuthor:
 * @description:
 *****************************************************************************************************************************************************************************
 */
public class DemoRefreshListViewFragment extends AtarRefreshListFragment {
	private String whichPage = DemoRefreshListViewFragment.class.getSimpleName();

	private List<WonderfulTopicBean> listWonderful = new ArrayList<WonderfulTopicBean>();
	private WonderfulTopicAdapter mWonderfulTopicAdapter = new WonderfulTopicAdapter(listWonderful);
	private int pageNo = 1, tempPageNo = 1;

	@Override
	public void onViewCreated(View view, Bundle savedInstanceState) {
		super.onViewCreated(view, savedInstanceState);
		setAdapter(mWonderfulTopicAdapter);
	}

	@Override
	public void onHandlerData(Message msg) {
		switch (msg.what) {
		case EnumMsgWhat.LOAD_FROM_SQL:
			LoadUtil.QueryDB(this, whichPage, "", "");
			break;
		case EnumMsgWhat.LOAD_FROM_SQL_COMPLETE:
			LoadUtil.loadFromSqlComelete(msg, listWonderful, mWonderfulTopicAdapter, getPullView(), new TypeToken<List<WonderfulTopicBean>>() {
			}.getType());
			break;
		case EnumMsgWhat.REFRESH_PULL_DOWN:
			tempPageNo = 1;
			pageNo = 1;
			sendEmptyMessage(EnumMsgWhat.REFRESH_HANDLER);
			break;
		case EnumMsgWhat.REFRESH_PULL_UP:
			pageNo = tempPageNo;
			pageNo++;
			sendEmptyMessage(EnumMsgWhat.REFRESH_HANDLER);
			break;
		case EnumMsgWhat.REFRESH_HANDLER:
			NetWorkInterfaces.GetWonderTopicList(getActivity(), this, Integer.toString(pageNo));
			break;
		}
	}

	@Override
	public void NetWorkCall(NetWorkMsg msg) {
		super.NetWorkCall(msg);
		switch (msg.what) {
		case EnumMsgWhat.EInterface_Get_Wonder_Topic_List:
			if (msg.obj != null) {
				WonderfulTopicJson mWonderfulTopicJson = (WonderfulTopicJson) msg.obj;
				if (mWonderfulTopicJson != null) {
					if (mWonderfulTopicJson.isStatus(getActivity())) {
						if (mWonderfulTopicJson.getDto() != null && mWonderfulTopicJson.getDto() != null && mWonderfulTopicJson.getDto().size() > 0) {
							tempPageNo = pageNo;
							if (isPullDownRefresh()) {
								LoadUtil.saveToDB(mWonderfulTopicJson.getDto(), whichPage, "", "");
								listWonderful.clear();
							}
							listWonderful.addAll(mWonderfulTopicJson.getDto());
							mWonderfulTopicAdapter.notifyDataSetChanged();
						}
					}
				} else {
					sendEmptyMessage(EnumMsgWhat.REFRESH_COMPLETE);
				}
			}
			break;
		}
	}
}
