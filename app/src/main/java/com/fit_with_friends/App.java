package com.fit_with_friends;


import android.annotation.SuppressLint;
import android.app.Application;
import android.content.Context;
import android.content.pm.PackageInfo;
import android.content.pm.PackageManager;
import android.content.pm.Signature;
import android.support.multidex.MultiDex;
import android.util.Base64;
import android.util.Log;
import com.fit_with_friends.fitWithFriends.module.*;
import com.fit_with_friends.fitWithFriends.utils.AppLifecycleHandler;
import com.fit_with_friends.fitWithFriends.utils.image.ImageLoaderUtils;
import com.nostra13.universalimageloader.core.ImageLoader;
import org.acra.ACRA;
import org.acra.ReportingInteractionMode;
import org.acra.annotation.ReportsCrashes;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

@ReportsCrashes(mailTo = "bishnoisushil81@gmail.com",
        mode = ReportingInteractionMode.DIALOG,
        resDialogText = R.string.crash_dialog_text,
        resDialogOkToast = R.string.crash_dialog_ok_toast)

public class App extends Application implements AppLifecycleHandler.AppLifecycleDelegates {

    private static App instance;
    private AppComponent component;

    public App() {

    }

    public static App getInstance() {
        return instance;
    }

    @Override
    protected void attachBaseContext(Context base) {
        super.attachBaseContext(base);
        MultiDex.install(this);
    }

    private void initImageLoader(Context context) {
        ImageLoader.getInstance().init(ImageLoaderUtils.getImageLoaderConfiguration(context));
    }

    @SuppressLint("PackageManagerGetSignatures")
    @Override
    public void onCreate() {
        super.onCreate();
        ACRA.init(this);
        instance = this;
        AppLifecycleHandler lifecycleHandler = new AppLifecycleHandler(this);
        registerLifecycleHandler(lifecycleHandler);
        initImageLoader(this);
        setupGraph();

        PackageInfo info;
        try {
            info = getPackageManager().getPackageInfo("com.roomer_resume", PackageManager.GET_SIGNATURES);
            for (Signature signature : info.signatures) {
                MessageDigest md;
                md = MessageDigest.getInstance("SHA");
                md.update(signature.toByteArray());
                String something = new String(Base64.encode(md.digest(), 0));
                //String something = new String(Base64.encodeBytes(md.digest()));
                Log.e("hash key", something);
            }
        } catch (PackageManager.NameNotFoundException e1) {
            Log.e("name not found", e1.toString());
        } catch (NoSuchAlgorithmException e) {
            Log.e("no such an algorithm", e.toString());
        } catch (Exception e) {
            Log.e("exception", e.toString());
        }
    }


    private void registerLifecycleHandler(AppLifecycleHandler lifeCycleHandler) {
        registerActivityLifecycleCallbacks(lifeCycleHandler);
        registerComponentCallbacks(lifeCycleHandler);
    }

    private void setupGraph() {
        component = DaggerAppComponent.builder().
                appModule(new AppModule(this)).
                daoModule(new DaoModule()).mapperModule
                (new MapperModule()).repositoryModule(new RepositoryModule()).
                serviceModule(new ServiceModule()).build();
        component.inject(this);
    }


    public AppComponent component() {
        return component;
    }

    public static App get(Context context) {
        return (App) context.getApplicationContext();
    }

    @Override
    public void onAppBackgrounded() {

    }

    @Override
    public void onAppForegrounded() {

    }
}