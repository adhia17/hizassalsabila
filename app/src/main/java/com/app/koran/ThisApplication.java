package com.app.koran;

import android.app.Application;
import android.os.Build;
import android.os.Handler;
import android.text.TextUtils;

import com.app.koran.connection.API;
import com.app.koran.connection.RestAdapter;
import com.app.koran.connection.callbacks.CallbackDevice;
import com.app.koran.data.AppConfig;
import com.app.koran.data.SharedPref;
import com.app.koran.model.DeviceInfo;
import com.app.koran.utils.NetworkCheck;
import com.app.koran.utils.Tools;
import com.google.android.gms.analytics.GoogleAnalytics;
import com.google.android.gms.analytics.HitBuilders;
import com.google.android.gms.analytics.StandardExceptionParser;
import com.google.android.gms.analytics.Tracker;
import com.google.firebase.iid.FirebaseInstanceId;

import io.realm.Realm;
import io.realm.RealmConfiguration;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ThisApplication extends Application {

    private Call<CallbackDevice> callbackCall = null;
    private static ThisApplication mInstance;
    private SharedPref sharedPref;
    private Tracker tracker;

    Handler handler = new Handler();
    Runnable runnable = new Runnable() {
        @Override
        public void run() {
            obtainFirebaseToken();
        }
    };

    @Override
    public void onCreate() {
        super.onCreate();
        mInstance = this;
        sharedPref = new SharedPref(this);

        // obtain regId & registering device to server
        obtainFirebaseToken();

        // init realm database
        RealmConfiguration realmConfiguration = new RealmConfiguration.Builder(this)
                .name("koran.realm")
                .schemaVersion(0)
                .deleteRealmIfMigrationNeeded()
                .build();
        Realm.setDefaultConfiguration(realmConfiguration);

        // activate analytics tracker
        getGoogleAnalyticsTracker();

        // get enabled controllers
        Tools.requestInfoApi(this);

    }

    public static synchronized ThisApplication getInstance() {
        return mInstance;
    }

    private void obtainFirebaseToken() {
        String token = FirebaseInstanceId.getInstance().getToken();
        if (TextUtils.isEmpty(token)) {
            handler.removeCallbacks(runnable);
            handler.postDelayed(runnable, 5 * 1000);
        } else {
            sendRegistrationToServer(token);
        }
    }

    private void sendRegistrationToServer(String token) {
        if (NetworkCheck.isConnect(this) && !TextUtils.isEmpty(token) && sharedPref.isOpenAppCounterReach()) {
            API api = RestAdapter.createAPI();
            DeviceInfo deviceInfo = new DeviceInfo();
            deviceInfo.regid = token;
            deviceInfo.device_name = Tools.getDeviceName();
            deviceInfo.serial = Build.SERIAL;
            deviceInfo.os_version = Tools.getAndroidVersion();

            callbackCall = api.registerDevice(deviceInfo);
            callbackCall.enqueue(new Callback<CallbackDevice>() {
                @Override
                public void onResponse(Call<CallbackDevice> call, Response<CallbackDevice> response) {
                    CallbackDevice resp = response.body();
                    if (resp != null && resp.status.equals("success")) {
                        sharedPref.setOpenAppCounter(0);
                    }
                }

                @Override
                public void onFailure(Call<CallbackDevice> call, Throwable t) {
                }

            });
        }
    }

    /**
     * --------------------------------------------------------------------------------------------
     * For Google Analytics
     */
    public synchronized Tracker getGoogleAnalyticsTracker() {
        if (tracker == null) {
            GoogleAnalytics analytics = GoogleAnalytics.getInstance(this);
            analytics.setDryRun(!AppConfig.ENABLE_ANALYTICS);
            tracker = analytics.newTracker(R.xml.app_tracker);
        }
        return tracker;
    }

    public void trackScreenView(String screenName) {
        Tracker t = getGoogleAnalyticsTracker();
        // Set screen name.
        t.setScreenName(screenName);
        // Send a screen view.
        t.send(new HitBuilders.ScreenViewBuilder().build());
        GoogleAnalytics.getInstance(this).dispatchLocalHits();
    }

    public void trackException(Exception e) {
        if (e != null) {
            Tracker t = getGoogleAnalyticsTracker();
            t.send(new HitBuilders.ExceptionBuilder()
                    .setDescription(new StandardExceptionParser(this, null).getDescription(Thread.currentThread().getName(), e))
                    .setFatal(false)
                    .build()
            );
        }
    }

    public void trackEvent(String category, String action, String label) {
        Tracker t = getGoogleAnalyticsTracker();
        // Build and send an Event.
        t.send(new HitBuilders.EventBuilder().setCategory(category).setAction(action).setLabel(label).build());
    }

    /** ---------------------------------------- End of analytics --------------------------------- */
}
