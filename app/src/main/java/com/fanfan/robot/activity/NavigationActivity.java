package com.fanfan.robot.activity;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

import com.afollestad.materialdialogs.MaterialDialog;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.bumptech.glide.request.RequestOptions;
import com.fanfan.novel.activity.DataNavigationActivity;
import com.fanfan.novel.common.Constants;
import com.fanfan.novel.common.activity.BarBaseActivity;
import com.fanfan.novel.common.base.simple.BaseRecyclerAdapter;
import com.fanfan.novel.common.enums.SpecialType;
import com.fanfan.novel.db.manager.NavigationDBManager;
import com.fanfan.novel.db.manager.VoiceDBManager;
import com.fanfan.novel.model.NavigationBean;
import com.fanfan.novel.model.SerialBean;
import com.fanfan.novel.model.VoiceBean;
import com.fanfan.novel.presenter.LocalSoundPresenter;
import com.fanfan.novel.presenter.SerialPresenter;
import com.fanfan.novel.presenter.ipresenter.ILocalSoundPresenter;
import com.fanfan.novel.presenter.ipresenter.ISerialPresenter;
import com.fanfan.novel.service.SerialService;
import com.fanfan.novel.service.event.ReceiveEvent;
import com.fanfan.novel.service.event.ServiceToActivityEvent;
import com.fanfan.novel.service.udp.SocketManager;
import com.fanfan.novel.ui.RangeClickImageView;
import com.fanfan.robot.R;
import com.fanfan.robot.adapter.NavigationAdapter;
import com.fanfan.robot.adapter.VoiceAdapter;
import com.fanfan.youtu.api.base.Constant;
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

import static com.fanfan.novel.ui.RangeClickImageView.getPointX;
import static com.fanfan.novel.ui.RangeClickImageView.getPointY;

/**
 * Created by android on 2018/1/6.
 */

public class NavigationActivity extends BarBaseActivity implements ILocalSoundPresenter.ILocalSoundView,
        ISerialPresenter.ISerialView, RangeClickImageView.OnResourceReadyListener, RangeClickImageView.OnRangeClickListener {

//    @BindView(R.id.iv_navigation)
    RangeClickImageView ivNavigation;
    @BindView(R.id.recycler_view)
    RecyclerView recyclerView;
    @BindView(R.id.iv_navigation_image)
    ImageView ivNavigationImage;

    public static void newInstance(Activity context) {
        Intent intent = new Intent(context, NavigationActivity.class);
        context.startActivity(intent);
        context.overridePendingTransition(R.anim.fade_in, R.anim.fade_out);
    }

    private LocalSoundPresenter mSoundPresenter;
    private SerialPresenter mSerialPresenter;

    private NavigationDBManager mNavigationDBManager;

    private String fileName = "image_navigation.png";

    private NavigationBean mNavigationBean;

    private List<NavigationBean> navigationBeanList = new ArrayList<>();

    private NavigationAdapter navigationAdapter;

    @Override
    protected int getLayoutId() {
        return R.layout.activity_navigation;
    }

    @Override
    protected void initView() {
        super.initView();

        mSoundPresenter = new LocalSoundPresenter(this);
        mSoundPresenter.start();
        mSerialPresenter = new SerialPresenter(this);
        mSerialPresenter.start();


//        initImage();
        initSimpleAdapter();
    }

    @Override
    protected void initData() {
        mNavigationDBManager = new NavigationDBManager();

        navigationBeanList = mNavigationDBManager.loadAll();
        if (navigationBeanList != null && navigationBeanList.size() > 0) {
            navigationAdapter.refreshData(navigationBeanList);
            navigationAdapter.notifyClick(0);
            Glide.with(mContext).load(navigationBeanList.get(0).getImgUrl())
                    .apply(new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.video_image))
                    .into(ivNavigationImage);
        } else {
            isEmpty();
        }
    }

    private void initImage() {

        ivNavigation.setFileName(fileName, (int) (Constants.displayWidth), (int) (Constants.displayHeight * 0.7));
        ivNavigation.setOnResourceReadyListener(this);
        ivNavigation.setOnRangeClickListener(this);
    }

    private void initSimpleAdapter() {
        navigationAdapter = new NavigationAdapter(mContext, navigationBeanList);
        navigationAdapter.setOnItemClickListener(new BaseRecyclerAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(View view, int position) {
                refVoice(navigationBeanList.get(position), position);
            }
        });
        recyclerView.setAdapter(navigationAdapter);
        recyclerView.setLayoutManager(new GridLayoutManager(this, 2));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
    }

    private void refVoice(NavigationBean itemData, int position) {
        navigationAdapter.notifyClick(position);
        refLocalPage(itemData.getTitle());
        Glide.with(mContext).load(itemData.getImgUrl())
                .apply(new RequestOptions().skipMemoryCache(true).diskCacheStrategy(DiskCacheStrategy.NONE).error(R.mipmap.video_image))
                .into(ivNavigationImage);
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
            showToast("导航有消息");
            SerialBean serialBean = event.getBean();
            mSerialPresenter.onDataReceiverd(serialBean);
        } else {
            Print.e("ReceiveEvent error");
        }
    }

    @Subscribe(threadMode = ThreadMode.BACKGROUND)
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

//    @Override
//    public boolean onCreateOptionsMenu(Menu menu) {
//        super.onCreateOptionsMenu(menu);
//        getMenuInflater().inflate(R.menu.home_black, menu);
//        return true;
//    }
//
//    @Override
//    public boolean onOptionsItemSelected(MenuItem item) {
//        switch (item.getItemId()) {
//            case R.id.home:
//                new MaterialDialog.Builder(this)
//                        .title("选择导航图")
//                        .content("目前只支持此张地图")
//                        .items(Constants.NAVIGATIONS)
//                        .itemsCallback(new MaterialDialog.ListCallback() {
//                            @Override
//                            public void onSelection(MaterialDialog dialog, View itemView, int position, CharSequence text) {
//                                fileName = text + ".png";
//                                initImage();
//                            }
//                        })
//                        .show();
//                break;
//        }
//        return super.onOptionsItemSelected(item);
//    }

    private void addSpeakAnswer(String messageContent) {
        mSoundPresenter.doAnswer(messageContent);
    }

    private void addSpeakAnswer(int res) {
        mSoundPresenter.doAnswer(getResources().getString(res));
    }

    private void refNavigation(NavigationBean itemData, int position) {
        mNavigationBean = itemData;
        addSpeakAnswer(itemData.getGuide());
        if (itemData.getNavigationData() != null) {
            mSerialPresenter.receiveMotion(SerialService.CRUISE_BAUDRATE, itemData.getNavigationData());
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
        addSpeakAnswer(R.string.open_control);
    }

    @Override
    public void refLocalPage(String result) {
        List<NavigationBean> navigationBeans = mNavigationDBManager.queryNavigationByQuestion(result);
        if (navigationBeans != null && navigationBeans.size() > 0) {
            NavigationBean itemData = null;
            if (navigationBeans.size() == 1) {
                itemData = navigationBeans.get(navigationBeans.size() - 1);
            } else {
                itemData = navigationBeans.get(new Random().nextInt(navigationBeans.size()));
            }
            int index = navigationBeans.indexOf(itemData);
            refNavigation(itemData, index);
        } else {
            addSpeakAnswer("点击地图上地点，我也可以带你去");
        }
    }

    @Override
    public void stopAll() {
        super.stopAll();
        mSoundPresenter.stopTts();
        mSoundPresenter.stopRecognizerListener();
        mSoundPresenter.stopHandler();
        addSpeakAnswer("你好，这里是导航页面，点击地图上地点可到达指定区域");
    }

    @Override
    public void onMoveStop() {
        if (mNavigationBean != null) {
            Print.e(mNavigationBean.getDatail());
            addSpeakAnswer(mNavigationBean.getDatail());
            mSerialPresenter.receiveMotion(SerialService.DEV_BAUDRATE, "A50C8001AA");
        }
    }

    @Override
    public void onClickImage(View view, Object tag, int x, int y) {
        refLocalPage((String) tag);
    }

    @Override
    public boolean onResourceReady(int realWidth, int realHeight, int ivWidth, int ivHeight) {
        List<NavigationBean> beanAll = mNavigationDBManager.loadAll();
        if (beanAll != null) {
            for (int i = 0; i < beanAll.size(); i++) {
                NavigationBean bean = beanAll.get(i);
                if (!bean.getImgUrl().endsWith(fileName)) {
                    beanAll.remove(i);
                    i--;
                }
            }
            for (NavigationBean bean : beanAll) {
                Print.e("bean" + bean.getPosX());
                Print.e("bean" + bean.getPosY());
                ivNavigation.setClickRange(bean.getTitle(), bean.getPosX(), bean.getPosY());
            }
        }
        return false;
    }

}
