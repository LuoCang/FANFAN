package com.fanfan.robot.activity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.DividerItemDecoration;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
import android.widget.TextView;

import com.fanfan.novel.activity.VideoDetailActivity;
import com.fanfan.novel.common.activity.BarBaseActivity;
import com.fanfan.novel.common.base.simple.BaseRecyclerAdapter;
import com.fanfan.novel.common.enums.SpecialType;
import com.fanfan.novel.db.manager.VideoDBManager;
import com.fanfan.novel.model.SerialBean;
import com.fanfan.novel.model.VideoBean;
import com.fanfan.novel.presenter.LocalSoundPresenter;
import com.fanfan.novel.presenter.SerialPresenter;
import com.fanfan.novel.presenter.ipresenter.ILocalSoundPresenter;
import com.fanfan.novel.presenter.ipresenter.ISerialPresenter;
import com.fanfan.novel.service.SerialService;
import com.fanfan.novel.service.event.ReceiveEvent;
import com.fanfan.novel.service.event.ServiceToActivityEvent;
import com.fanfan.novel.service.udp.SocketManager;
import com.fanfan.novel.ui.manager.carouse.CarouselLayoutManager;
import com.fanfan.novel.ui.manager.carouse.CarouselZoomPostLayoutListener;
import com.fanfan.novel.ui.manager.carouse.CenterScrollListener;
import com.fanfan.robot.R;
import com.fanfan.robot.adapter.VideoAdapter;
import com.fanfan.robot.adapter.VideoVerticalAdapter;
import com.seabreeze.log.Print;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.net.DatagramPacket;
import java.util.ArrayList;
import java.util.List;
import java.util.Random;

import butterknife.BindView;
import butterknife.ButterKnife;
import butterknife.OnClick;

/**
 * Created by android on 2018/1/6.
 */

public class VideoIntroductionActivity extends BarBaseActivity implements ILocalSoundPresenter.ILocalSoundView, ISerialPresenter.ISerialView {

    @BindView(R.id.iv_list_hd)
    ImageView ivListHd;
    @BindView(R.id.recycler_video)
    RecyclerView recyclerVideo;
    @BindView(R.id.list_vertical)
    RecyclerView listVertical;
    @BindView(R.id.iv_upward)
    ImageView ivUpward;
    @BindView(R.id.iv_down)
    ImageView ivDown;

    public static void newInstance(Context context) {
        Intent intent = new Intent(context, VideoIntroductionActivity.class);
        context.startActivity(intent);
    }

    private VideoDBManager mVideoDBManager;

    private List<VideoBean> videoBeanList = new ArrayList<>();

    private VideoAdapter videoAdapter;
    private VideoVerticalAdapter videoVerticalAdapter;

    private boolean isOpen;

    private CarouselLayoutManager layoutManager;
    private int mCurPosition;

    private LocalSoundPresenter mSoundPresenter;
    private SerialPresenter mSerialPresenter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_video_introduction;
    }

    @Override
    protected void initView() {
        super.initView();

        mSoundPresenter = new LocalSoundPresenter(this);
        mSoundPresenter.start();
        mSerialPresenter = new SerialPresenter(this);
        mSerialPresenter.start();

        isOpen = true;

        initSimpleAdapter();
        initRecyclerView();
    }


    @Override
    protected void initData() {
        mVideoDBManager = new VideoDBManager();

        videoBeanList = mVideoDBManager.loadAll();
        if (videoBeanList != null && videoBeanList.size() > 0) {
            videoAdapter.refreshData(videoBeanList);
        }else{
            isEmpty();
        }
    }


    @Override
    protected void onStart() {
        super.onStart();
        EventBus.getDefault().register(this);
        mSoundPresenter.startRecognizerListener();
    }

    @Override
    protected void onResume() {
        super.onResume();
        mSoundPresenter.buildTts();
        mSoundPresenter.buildIat();
    }

    @Override
    protected void onPause() {
        super.onPause();
        mSoundPresenter.stopTts();
        mSoundPresenter.stopRecognizerListener();
        mSoundPresenter.stopHandler();
    }

    @Override
    protected void onStop() {
        super.onStop();
        EventBus.getDefault().unregister(this);
    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
        mSoundPresenter.finish();
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultEvent(ServiceToActivityEvent event) {
        if (event.isOk()) {
            SerialBean serialBean = event.getBean();
            mSerialPresenter.onDataReceiverd(serialBean);
        } else {
            Print.e("ReceiveEvent error");
        }
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void onResultEvent(ReceiveEvent event) {
        if (event.isOk()) {
            DatagramPacket packet = event.getBean();
            if (!SocketManager.getInstance().isGetTcpIp) {
                SocketManager.getInstance().setUdpIp(packet.getAddress().getHostAddress(), packet.getPort());
            }
            String recvStr = new String(packet.getData(), 0, packet.getLength());
            mSerialPresenter.receiveMotion(SerialService.DEV_BAUDRATE, recvStr);
            Print.e(recvStr);
        } else {
            Print.e("ReceiveEvent error");
        }
    }

    @OnClick({R.id.iv_list_hd, R.id.iv_upward, R.id.iv_down})
    public void onViewClicked(View view) {
        switch (view.getId()) {
            case R.id.iv_list_hd:
                if (isOpen) {
                    isOpen = false;
                    recyclerVideo.setVisibility(View.GONE);
                } else {
                    isOpen = true;
                    recyclerVideo.setVisibility(View.VISIBLE);
                }
                break;
            case R.id.iv_upward:
                upward();
                break;
            case R.id.iv_down:
                down();
                break;
        }
    }

    private void upward() {
        if (mCurPosition == 0) {
            addSpeakAnswer("已是第一个");
        } else {
            mCurPosition--;
            layoutManager.scrollToPosition(mCurPosition);
        }
    }

    private void down() {
        if (mCurPosition == videoBeanList.size() - 1) {
            addSpeakAnswer("已是第最后一个");
        } else {
            mCurPosition++;
            layoutManager.scrollToPosition(mCurPosition);
        }
    }

    private void addSpeakAnswer(String messageContent) {
        mSoundPresenter.doAnswer(messageContent);
    }

    private void addSpeakAnswer(int res) {
        mSoundPresenter.doAnswer(getResources().getString(res));
    }

    private void initSimpleAdapter() {
        videoAdapter = new VideoAdapter(mContext, videoBeanList);
        videoAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                refVideo(videoBeanList.get(position));
            }
        });
        recyclerVideo.setAdapter(videoAdapter);
        recyclerVideo.setLayoutManager(new LinearLayoutManager(mContext));
        recyclerVideo.setItemAnimator(new DefaultItemAnimator());
        recyclerVideo.addItemDecoration(new DividerItemDecoration(mContext, DividerItemDecoration.VERTICAL));
    }

    private void initRecyclerView() {
        videoVerticalAdapter = new VideoVerticalAdapter(mContext, videoBeanList);

        layoutManager = new CarouselLayoutManager(CarouselLayoutManager.VERTICAL, false);
        layoutManager.setPostLayoutListener(new CarouselZoomPostLayoutListener());
        layoutManager.setMaxVisibleItems(3);

        listVertical.setAdapter(videoVerticalAdapter);
        listVertical.setLayoutManager(layoutManager);
        listVertical.setHasFixedSize(true);
        listVertical.addOnScrollListener(new CenterScrollListener());

        videoVerticalAdapter.setOnItemClickListener(new VideoVerticalAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                Print.e("onItemClick : " + position);
                refVideo(videoBeanList.get(position));
            }
        });

        layoutManager.addOnItemSelectionListener(new CarouselLayoutManager.OnCenterItemSelectionListener() {

            @Override
            public void onCenterItemChanged(final int adapterPosition) {
                Print.e("onCenterItemChanged : " + adapterPosition);
                mCurPosition = adapterPosition;
            }
        });
    }

    private void refVideo(VideoBean itemData) {
        VideoBean videoBean = itemData;
        if (videoBean.getVideoUrl() != null) {
            VideoDetailActivity.newInstance(this, videoBean.getVideoUrl());
            return;
        } else {
            addSpeakAnswer(R.string.speakText);
        }
    }

    @Override
    public void showLoading() {

    }

    @Override
    public void dismissLoading() {

    }

    @Override
    public void showMsg(String msg) {
        showToast(msg);
    }

    @Override
    public void showMsg(int msg) {
        showToast(msg);
    }

    @Override
    public Context getContext() {
        return this;
    }

    @Override
    public void spakeMove(SpecialType type, String result) {
        mSoundPresenter.onCompleted();
        switch (type) {
            case Forward:
                mSerialPresenter.receiveMotion(SerialService.DEV_BAUDRATE, "A5038002AA");
                break;
            case Backoff:
                mSerialPresenter.receiveMotion(SerialService.DEV_BAUDRATE, "A5038008AA");
                break;
            case Turnleft:
                mSerialPresenter.receiveMotion(SerialService.DEV_BAUDRATE, "A5038004AA");
                break;
            case Turnright:
                mSerialPresenter.receiveMotion(SerialService.DEV_BAUDRATE, "A5038006AA");
                break;
        }
    }

    @Override
    public void openMap() {
        addSpeakAnswer(R.string.open_map);
    }

    @Override
    public void stopListener() {
        mSoundPresenter.stopTts();
        mSoundPresenter.stopRecognizerListener();
        mSoundPresenter.stopHandler();
    }

    @Override
    public void back() {
        finish();
    }

    @Override
    public void artificial() {
        addSpeakAnswer(R.string.open_artificial);
    }

    @Override
    public void face(SpecialType type, String result) {
        addSpeakAnswer(R.string.open_face);
    }

    @Override
    public void control(SpecialType type, String result) {
        switch (type) {
            case Next:
                upward();
                break;
            case Lase:
                down();
                break;
        }
    }

    @Override
    public void refLocalPage(String result) {
        List<VideoBean> videoBeans = mVideoDBManager.queryLikeVideoByQuestion(result);
        if (videoBeans != null && videoBeans.size() > 0) {
            if (videoBeans.size() == 1) {
                refVideo(videoBeans.get(videoBeans.size() - 1));
            } else {
                VideoBean itemData = videoBeans.get(new Random().nextInt(videoBeans.size()));
                refVideo(itemData);
            }
        } else {
            if (new Random().nextBoolean()) {
                addSpeakAnswer(resFoFinal(R.array.no_result));
            } else {
                addSpeakAnswer(resFoFinal(R.array.no_voice));
            }
        }
    }

    @Override
    public void stopAll() {
        super.stopAll();
        mSoundPresenter.stopTts();
        mSoundPresenter.stopRecognizerListener();
        mSoundPresenter.stopHandler();
    }

    @Override
    public void onMoveStop() {

    }
}