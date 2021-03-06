package com.arun.a85mm.fragment;

import android.os.Bundle;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.RelativeLayout;

import com.arun.a85mm.R;
import com.arun.a85mm.adapter.MessageAdapter;
import com.arun.a85mm.bean.MessageItem;
import com.arun.a85mm.bean.MessageItemBean;
import com.arun.a85mm.common.Constant;
import com.arun.a85mm.event.UpdateSendMsg;
import com.arun.a85mm.helper.RandomColorHelper;
import com.arun.a85mm.presenter.MessagePresenter;
import com.arun.a85mm.refresh.SwipeToLoadLayout;
import com.arun.a85mm.utils.FullyLinearLayoutManager;
import com.arun.a85mm.view.CommonView4;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.List;

/**
 * Created by wy on 2017/6/7.
 */

public class MessageFragment extends BaseFragment implements CommonView4<List<MessageItemBean>> {
    private static final String KEY_MSG_TYPE = "type";
    private RecyclerView recyclerView;
    private SwipeToLoadLayout swipeToLoadLayout;
    private MessageAdapter messageAdapter;
    private MessagePresenter messagePresenter;
    private List<MessageItem> messages = new ArrayList<>();
    private int msgType;
    private int lastMsgId = 0;
    private RelativeLayout layout_no_data;
    private boolean isHaveMore = true;

    public static MessageFragment getInstance(int msgType) {
        MessageFragment messageFragment = new MessageFragment();
        Bundle bundle = new Bundle();
        bundle.putInt(KEY_MSG_TYPE, msgType);
        messageFragment.setArguments(bundle);
        return messageFragment;
    }

    @Override
    protected int preparedCreate(Bundle savedInstanceState) {
        EventBus.getDefault().register(this);
        return R.layout.layout_message;
    }

    @Override
    protected void initView() {
        if (getArguments() != null) {
            msgType = getArguments().getInt(KEY_MSG_TYPE);
        }

        recyclerView = (RecyclerView) findViewById(R.id.swipe_target);
        swipeToLoadLayout = (SwipeToLoadLayout) findViewById(R.id.swipeToLoad);
        layout_no_data = (RelativeLayout) findViewById(R.id.layout_no_data);

        FullyLinearLayoutManager layoutManager = new FullyLinearLayoutManager(getActivity());
        recyclerView.setLayoutManager(layoutManager);
        messageAdapter = new MessageAdapter(getActivity(), messages, msgType);
        recyclerView.setAdapter(messageAdapter);

        setRefresh(swipeToLoadLayout);
        setRecyclerViewScrollListener(recyclerView);
    }

    @Override
    protected void initData() {
        messagePresenter = new MessagePresenter(getActivity());
        messagePresenter.attachView(this);
        requestData();
    }

    private void requestData() {
        setHaveData();
        if (messagePresenter != null) {
            isHaveMore = true;
            lastMsgId = 0;
            setLoading(true);
            messagePresenter.getMessageList(msgType, lastMsgId);
        }
    }

    @Override
    public void reloadData() {
        requestData();
    }

    @Override
    public void setLoadMore() {
        if (isHaveMore) {
            if (messagePresenter != null) {
                setLoading(true);
                messagePresenter.getMessageList(msgType, lastMsgId);
            }
        }
    }

    private void setHaveData() {
        layout_no_data.setVisibility(View.GONE);
        swipeToLoadLayout.setVisibility(View.VISIBLE);
    }

    public void setNoData() {
        layout_no_data.setVisibility(View.VISIBLE);
        swipeToLoadLayout.setVisibility(View.GONE);
    }

    public void setHaveMore(boolean isHaveMore) {
        this.isHaveMore = isHaveMore;
    }

    @Override
    public void refresh(List<MessageItemBean> data) {
        if (data != null && data.size() > 0) {
            messages.clear();
            formatData(data);
        }
    }

    @Override
    public void refreshMore(List<MessageItemBean> data) {
        if (data != null && data.size() > 0) {
            formatData(data);
        }
    }

    @Override
    public void refresh(int type, Object data) {

    }

    private void formatData(List<MessageItemBean> data) {
        for (int i = 0; i < data.size(); i++) {
            MessageItemBean bean = data.get(i);
            if (bean != null) {
                MessageItem itemTop = new MessageItem();
                itemTop.type = Constant.MESSAGE_LIST_TOP;
                itemTop.content = bean.content;
                itemTop.sender = bean.senderId;
                itemTop.receiver = bean.receiverId;
                itemTop.msgId = bean.id;
                itemTop.sendTime = bean.sendTime;
                messages.add(itemTop);
                if (bean.imageList != null) {
                    for (int j = 0; j < bean.imageList.size(); j++) {
                        MessageItem image = bean.imageList.get(j);
                        if (image != null) {
                            MessageItem itemImage = new MessageItem();
                            itemImage.type = Constant.MESSAGE_LIST_IMAGE;
                            itemImage.width = image.width;
                            itemImage.height = image.height;
                            itemImage.imageUrl = image.imageUrl;
                            itemImage.backgroundColor = RandomColorHelper.getRandomColor();
                            messages.add(itemImage);
                        }
                    }
                }
                if (i == data.size() - 1) {
                    lastMsgId = bean.id;
                }
            }
        }
        messageAdapter.notifyDataSetChanged();
    }

    @Override
    public void onRefreshComplete() {
        setLoading(false);
        if (swipeToLoadLayout != null) {
            swipeToLoadLayout.setRefreshing(false);
        }
    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        EventBus.getDefault().unregister(this);
        if (messagePresenter != null) {
            messagePresenter.detachView();
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void updateSendMsg(UpdateSendMsg updateSendMsg) {
        if (msgType == 1) {
            requestData();
        }
    }
}
