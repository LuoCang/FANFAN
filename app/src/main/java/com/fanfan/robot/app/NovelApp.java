package com.fanfan.robot.app;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.multidex.MultiDexApplication;

import com.fanfan.novel.common.Constants;
import com.fanfan.novel.common.lifecycle.Foreground;
import com.fanfan.novel.db.base.BaseManager;
import com.fanfan.novel.service.cache.Config;
import com.fanfan.novel.service.cache.UserInfoCache;
import com.fanfan.novel.utils.CrashHandler;
import com.fanfan.robot.BuildConfig;
import com.fanfan.robot.R;
import com.fanfan.youtu.Youtucode;
import com.iflytek.cloud.SpeechConstant;
import com.iflytek.cloud.SpeechUtility;
import com.seabreeze.log.Print;
import com.seabreeze.log.inner.ConsoleTree;
import com.seabreeze.log.inner.FileTree;
import com.seabreeze.log.inner.LogcatTree;
import com.squareup.leakcanary.LeakCanary;
import com.youdao.sdk.app.YouDaoApplication;

/**
 * Created by android on 2017/12/18.
 */

public class NovelApp extends MultiDexApplication {

    private static NovelApp instance;

    public static NovelApp getInstance() {
        return instance;
    }

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;

//        if (initLeak()) return;
//        CrashHandler.getInstance().init(this);
        initLogger(this);
        Foreground.init(this);
        //初始化数据库
        BaseManager.initOpenHelper(this);
        //初始化讯飞
        initXf();
        //初始化有道
        YouDaoApplication.init(this, getResources().getString(R.string.app_youdao_id));//创建应用，每个应用都会有一个Appid，绑定对应的翻译服务实例，即可使用
        //初始化本地设置
        Config.init(this);
        //初始化网络
        Youtucode.init(this);
        //初始化用户
        UserInfoCache.getUser(this);
        //初始化本机
        RobotInfo.getInstance().init(this);
    }


    private boolean initLeak() {
        if (LeakCanary.isInAnalyzerProcess(this)) {
            return true;
        }
        LeakCanary.install(this);
        return false;
    }


    private void initLogger(@NonNull Context context) {
        if (BuildConfig.DEBUG) {
            Print.getLogConfig().configAllowLog(true).configShowBorders(false);
            Print.plant(new FileTree(this, Constants.PRINT_LOG_PATH));
            Print.plant(new ConsoleTree());
            Print.plant(new LogcatTree());
        }
    }

    private void initXf() {
        StringBuffer param = new StringBuffer();
        param.append("appid=" + getString(R.string.app_id));
        param.append(",");
        // 设置使用v5+
        param.append(SpeechConstant.ENGINE_MODE + "=" + SpeechConstant.MODE_MSC);
        SpeechUtility.createUtility(this, param.toString());
    }

}