/*
 * Copyright 2017 GcsSloop
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *    http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 *
 * Last modified 2017-03-29 05:11:04
 *
 * GitHub:  https://github.com/GcsSloop
 * Website: http://www.gcssloop.com
 * Weibo:   http://weibo.com/GcsSloop
 */

package com.fanfan.novel.utils;

import android.app.Activity;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.os.Build;
import android.support.annotation.ColorInt;
import android.support.annotation.IntRange;
import android.support.annotation.NonNull;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.WindowManager;

import com.fanfan.robot.R;
import com.fanfan.robot.app.NovelApp;

import java.util.ArrayList;
import java.util.List;

public class AppUtil {

    /**
     * 获取当前程序包名
     *
     * @param context 上下文
     * @return 程序包名
     */
    public static String getPackageName(Context context) {
        return context.getPackageName();
    }

    /**
     * 获取程序版本信息
     *
     * @param context 上下文
     * @return 版本名称
     */
    public static String getVersionName(Context context) {
        String versionName = null;
        String pkName = context.getPackageName();
        try {
            versionName = context.getPackageManager().getPackageInfo(pkName, 0).versionName;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionName;
    }

    /**
     * 获取程序版本号
     *
     * @param context 上下文
     * @return 版本号
     */
    public static int getVersionCode(Context context) {
        int versionCode = -1;
        String pkName = context.getPackageName();
        try {
            versionCode = context.getPackageManager().getPackageInfo(pkName, 0).versionCode;
        } catch (PackageManager.NameNotFoundException e) {
            Log.e("VersionInfo", "Exception", e);
        }
        return versionCode;
    }

    /**
     * 判断是否安装某个应用
     *
     * @param context     上下文
     * @param packageName 包名
     * @return 是否安装
     */
    public static boolean isAvailable(Context context, String packageName) {
        final PackageManager packageManager = context.getPackageManager();//获取packagemanager
        List<PackageInfo> pInfo = packageManager.getInstalledPackages(0);//获取所有已安装程序的包信息
        //从pinfo中将包名字逐一取出，压入pName list中
        if (pInfo != null) {
            for (int i = 0; i < pInfo.size(); i++) {
                String pn = pInfo.get(i).packageName;
                if (pn.equals(packageName))
                    return true;
            }
        }
        return false;
    }

    public static final int DEFAULT_STATUS_BAR_ALPHA = 112;

    public static void setColor(Activity activity, @ColorInt int color) {
        setColor(activity, color, DEFAULT_STATUS_BAR_ALPHA);
    }

    public static void setColor(Activity activity, @ColorInt int color, @IntRange(from = 0, to = 255) int statusBarAlpha) {
        if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.LOLLIPOP) {
            activity.getWindow().addFlags(WindowManager.LayoutParams.FLAG_DRAWS_SYSTEM_BAR_BACKGROUNDS);
            activity.getWindow().clearFlags(WindowManager.LayoutParams.FLAG_TRANSLUCENT_STATUS);
            activity.getWindow().setStatusBarColor(calculateStatusColor(color, statusBarAlpha));
        }
    }

    private static int calculateStatusColor(@ColorInt int color, int alpha) {
        if (alpha == 0) {
            return color;
        }
        float a = 1 - alpha / 255f;
        int red = color >> 16 & 0xff;
        int green = color >> 8 & 0xff;
        int blue = color & 0xff;
        red = (int) (red * a + 0.5);
        green = (int) (green * a + 0.5);
        blue = (int) (blue * a + 0.5);
        return 0xff << 24 | red << 16 | green << 8 | blue;
    }

    public static String words2Contents() {
        StringBuilder sb = new StringBuilder();
        List<String> words = getLocalStrings();
        for (String anArrStandard : words) {
            sb.append(anArrStandard).append("\n");
        }
        return sb.toString();
    }

    @NonNull
    public static List<String> getLocalStrings() {

        List<String> words = new ArrayList<>();
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.FanFan));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Video));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Problem));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.MultiMedia));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Seting_up));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Public_num));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Navigation));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Face));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Map));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Logout));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.StopListener));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Back));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Forward));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Backoff));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Turnleft));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Turnright));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Artificial));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Face_check_in));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Instagram));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Witness_contrast));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Face_lifting_area));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Next));
        words.add(NovelApp.getInstance().getApplicationContext().getResources().getString(R.string.Lase));
        return words;
    }
}
