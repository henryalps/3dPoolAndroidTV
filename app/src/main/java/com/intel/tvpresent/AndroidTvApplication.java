package com.intel.tvpresent;

import android.app.Application;
import android.content.Context;

import com.intel.tvpresent.injection.component.ApplicationComponent;
import com.intel.tvpresent.injection.component.DaggerApplicationComponent;
import com.intel.tvpresent.injection.module.ApplicationModule;
import com.liulishuo.filedownloader.FileDownloader;
import com.wenming.library.LogReport;
import com.wenming.library.save.imp.CrashWriter;
import com.wenming.library.upload.email.EmailReporter;

import timber.log.Timber;

public class AndroidTvApplication extends Application {

    ApplicationComponent mApplicationComponent;

    @Override
    public void onCreate() {
        super.onCreate();
        if (BuildConfig.DEBUG) Timber.plant(new Timber.DebugTree());

        mApplicationComponent = DaggerApplicationComponent.builder()
                .applicationModule(new ApplicationModule(this))
                .build();
        initCrashReport();
        FileDownloader.setupOnApplicationOnCreate(this);
    }

    public static AndroidTvApplication get(Context context) {
        return (AndroidTvApplication) context.getApplicationContext();
    }

    // Needed to replace the component with a test specific one
    public void setComponent(ApplicationComponent applicationComponent) {
        mApplicationComponent = applicationComponent;
    }

    public ApplicationComponent getComponent() {
        if (mApplicationComponent == null) {
            mApplicationComponent = DaggerApplicationComponent.builder()
                    .applicationModule(new ApplicationModule(this))
                    .build();
        }
        return mApplicationComponent;
    }
    private void initCrashReport() {
        LogReport.getInstance()
                .setCacheSize(30 * 1024 * 1024)//支持设置缓存大小，超出后清空
                .setLogDir(getApplicationContext(), "sdcard/" + this.getString(this.getApplicationInfo().labelRes) + "/")//定义路径为：sdcard/[app name]/
                .setWifiOnly(true)//设置只在Wifi状态下上传，设置为false为Wifi和移动网络都上传
                .setLogSaver(new CrashWriter(getApplicationContext()))//支持自定义保存崩溃信息的样式
                //.setEncryption(new AESEncode()) //支持日志到AES加密或者DES加密，默认不开启
                .init(getApplicationContext());
        initEmailReporter();
    }

    /**
     * 使用EMAIL发送日志
     */
    private void initEmailReporter() {
        EmailReporter email = new EmailReporter(this);
        email.setReceiver("604941957@qq.com");//收件人
        email.setSender("uvwxyz0@126.com");//发送人邮箱
        email.setSendPassword("M61xx6UaWpcOizf");//邮箱的客户端授权码，注意不是邮箱密码
        email.setSMTPHost("smtp.126.com");//SMTP地址
        email.setPort("465");//SMTP 端口
        LogReport.getInstance().setUploadType(email);
    }

}
