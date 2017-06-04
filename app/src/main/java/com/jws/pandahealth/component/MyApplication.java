package com.jws.pandahealth.component;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.os.Parcelable;
import android.support.multidex.MultiDexApplication;
import android.util.DisplayMetrics;
import android.view.Display;
import android.view.WindowManager;

import com.jws.pandahealth.R;
import com.jws.pandahealth.base.util.CrashHandlerUtil;
import com.jws.pandahealth.base.util.DateUtil;
import com.jws.pandahealth.base.util.LogUtil;
import com.jws.pandahealth.base.util.MD5Util;
import com.jws.pandahealth.base.util.SystemUtil;
import com.jws.pandahealth.base.util.ToastUtil;
import com.jws.pandahealth.component.app.base.AppComponent;
import com.jws.pandahealth.component.app.base.AppModule;
import com.jws.pandahealth.component.app.base.DaggerAppComponent;
import com.jws.pandahealth.component.app.model.bean.UserInfo;
import com.jws.pandahealth.component.services.easemob.EaseMobHelper;
import com.jws.pandahealth.component.services.util.JLog;

import java.util.HashSet;
import java.util.Set;
import java.util.WeakHashMap;

import io.realm.Realm;

//import com.github.moduth.blockcanary.BlockCanary;
//import com.squareup.leakcanary.LeakCanary;
/**
 * Created by jws on 2016/8/2.
 *
 */
public class MyApplication extends MultiDexApplication{

    private static MyApplication instance;
    private static UserInfo currentUserInfo;
    private Set<Activity> allActivities;
    //当前用户
    public static int SCREEN_WIDTH = -1;
    public static int SCREEN_HEIGHT = -1;
    public static float DIMEN_RATE = -1.0F;
    public static int DIMEN_DPI = -1;

    @Override
    public void onCreate() {
        super.onCreate();
        instance = this;
        String processName = SystemUtil.getProcessName(instance,
                android.os.Process.myPid());
        if (processName != null && processName.equals(getPackageName())) {// 本进程
            LogUtil.e("initCashHandler processName -->" + processName);
            //初始化日志输入
            LogUtil.initLogger();
            //初始化错误收集
            CrashHandlerUtil.initCashHandler(instance);
            //初始化屏幕宽高
            initDisplayMetricsSize();
            //初始化数据库
            initSQLAndIcon();
            //初始化第3方服务
            initService();
            //初始化桌面图标
            initAppIcon();
        }
    }

    /**
     * 	初始化屏幕宽高
     */
    private void initDisplayMetricsSize() {
        WindowManager windowManager = (WindowManager)this.getSystemService(Context.WINDOW_SERVICE);
        DisplayMetrics dm = new DisplayMetrics();
        Display display = windowManager.getDefaultDisplay();
        display.getMetrics(dm);
        DIMEN_RATE = dm.density / 1.0F;
        DIMEN_DPI = dm.densityDpi;
        SCREEN_WIDTH = dm.widthPixels;
        SCREEN_HEIGHT = dm.heightPixels;
        if(SCREEN_WIDTH > SCREEN_HEIGHT) {
            int t = SCREEN_HEIGHT;
            SCREEN_HEIGHT = SCREEN_WIDTH;
            SCREEN_WIDTH = t;
        }
    }

    /**
     * 首次安装创建数据库
     */
    private void initSQLAndIcon() {
        Realm.init(this);
    }



    /**
     * 启动后台Services
     */
    private void initService() {
        //initCashHandler EaseMob
        EaseMobHelper.getInstence().init(this);
        //ViewTarget.setTagId(R.id.glide_tag);

        //JPUSH

        //umeng

        //调试开启服务
            //初始化内存泄漏检测
//            LeakCanary.install(this);
            //初始化过度绘制检测
//            BlockCanary.install(this, new AppBlockCanaryContext()).start();
//            JLog.e("is debugging");
            /**
             * Chrome上调试Android程序Realm数据库
             *
             */
        /*    Stetho.initialize(
                    Stetho.newInitializerBuilder(this)
                            .enableDumpapp(Stetho.defaultDumperPluginsProvider(this))
                            .enableWebKitInspector(RealmInspectorModulesProvider.builder(this)*//*.databaseNamePattern(Pattern.compile(".+\\.realm"))*//*.build())
                            .build());*/
    }


    /**
     * 自动创建桌面图标
     */
    public void initAppIcon() {
        Intent shortcutintent = new Intent(
                "com.android.launcher.action.INSTALL_SHORTCUT");
        // 不允许重复创建
        shortcutintent.putExtra("duplicate", false);
        // 需要现实的名称
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_NAME,
                getString(R.string.app_name));
        // 快捷图片
        Parcelable icon = Intent.ShortcutIconResource.fromContext(
                getApplicationContext(), R.mipmap.ic_launcher);
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_ICON_RESOURCE, icon);
        // 点击快捷图片，运行的程序主入口
        shortcutintent.putExtra(Intent.EXTRA_SHORTCUT_INTENT,
                new Intent(getApplicationContext(), getClass()));
        // 发送广播
        sendBroadcast(shortcutintent);
    }

    /**
     * application instance
     */
    public static synchronized MyApplication getInstance() {
        return instance;
    }

    /**
     * Toast
     */
    public synchronized static void showToast(String msg){
        ToastUtil.show(msg);
    }

    public synchronized static void showToast(int msg){
        ToastUtil.show(msg);
    }

    /**
     * activity task
     */
    public void addActivity(Activity act) {
        if (allActivities == null) {
            allActivities = new HashSet<>();
        }
        allActivities.add(act);
    }

    public void removeActivity(Activity act) {
        if (allActivities != null) {
            allActivities.remove(act);
        }
    }

    public void exitApp() {
        if (allActivities != null) {
            synchronized (allActivities) {
                for (Activity act : allActivities) {
                    act.finish();
                }
            }
        }
        android.os.Process.killProcess(android.os.Process.myPid());
        System.exit(0);
    }


    public static AppComponent getAppComponent(){
        return DaggerAppComponent.builder()
                .appModule(new AppModule(instance))
                .build();
    }

    /**
     * 公共数据请求Map
     *
     */
    public static WeakHashMap<String, String> getHttpDataMap() {
        WeakHashMap<String, String> map = new WeakHashMap<>();
        map.put("ver", String.valueOf(AppConfig.VERSION_CODE));
        map.put("device_type", AppConfig.DEVICE_TYPE);
        map.put("device_code", AppConfig.DEVICE_CODE);
        map.put("secret",
                MD5Util.getMD5(AppConfig.DEVICE_CODE + DateUtil.getCurrentDate()
                        + AppConfig.PUBLIC_KEY));
        if(getCurrentUser() != null) {
            map.put("token", getCurrentUser().token);
        }
        return map;
    }

    /**
     * 获取当前用户
     **/
    public static synchronized UserInfo getCurrentUser() {
        if (currentUserInfo == null){
            currentUserInfo = getAppComponent().appRealmHelper().queryUserInfo();
            JLog.e("Thread getCurrentUser:" + Thread.currentThread());
        }
        return currentUserInfo;
    }

    public static void clearCurrentUser() {
        currentUserInfo = null;
    }

}
