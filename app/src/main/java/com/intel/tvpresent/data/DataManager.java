package com.intel.tvpresent.data;

import android.util.Log;

import com.intel.tvpresent.data.local.PreferencesHelper;
import com.intel.tvpresent.data.model.GameLevel;
import com.intel.tvpresent.data.model.Room;
import com.intel.tvpresent.data.model.UserWrapper;
import com.intel.tvpresent.data.remote.AndroidTvService;
import com.intel.tvpresent.data.remote.JMSHelper;
import com.intel.tvpresent.data.remote.PickyHttpImpl;
import com.squareup.okhttp.ResponseBody;
import com.tencent.bugly.beta.Beta;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.io.IOException;
import java.util.List;
import java.util.Map;

import javax.inject.Inject;
import javax.inject.Singleton;

import retrofit.Callback;
import retrofit.GsonConverterFactory;
import retrofit.Response;
import retrofit.Retrofit;
import rx.Observable;
import rx.Observer;
import rx.Subscriber;

@Singleton
public class DataManager {

    private final AndroidTvService mAndroidTvService;
    private final PreferencesHelper mPreferencesHelper;
    private final Room mRoom;
    private JMSHelper mJMSHelper;
    private final String BASE_URL = "https://picky.top";
    private final Retrofit retrofit = new Retrofit.Builder()
            .baseUrl(BASE_URL)
            .addConverterFactory(GsonConverterFactory.create())
            .build();

    @Inject
    public DataManager(PreferencesHelper preferencesHelper,
                       AndroidTvService androidTvService,
                       Observable<JMSHelper> jmsHelperObservable,
                       Room room) {
        mPreferencesHelper = preferencesHelper;
        mAndroidTvService = androidTvService;
        mRoom = room;
        jmsHelperObservable.subscribe(new Observer<JMSHelper>() {
            @Override
            public void onCompleted() {

            }

            @Override
            public void onError(Throwable e) {

            }

            @Override
            public void onNext(JMSHelper jmsHelper) {
                mJMSHelper = jmsHelper;
            }
        });
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public rx.Observable<Room> login() {

        return rx.Observable.create(new rx.Observable.OnSubscribe<Room>() {
            @Override
            public void call(final Subscriber<? super Room> subscriber) {
                PickyHttpImpl pickyHttp = retrofit.create(PickyHttpImpl.class);
                pickyHttp.login(ConstantManager.TOKEN_SZ).enqueue(new Callback<ResponseBody>() {
                    @Override
                    public void onResponse(Response<ResponseBody> response, Retrofit retrofit) {
                        try {
                            String body = response.body().string();
                            if (mRoom.updateInLogin(body)) {
                                subscriber.onNext(mRoom);
                            }
                        } catch (IOException ex) {
                            ex.printStackTrace();
                        }
                    }

                    @Override
                    public void onFailure(Throwable t) {
                        t.printStackTrace();
                    }
                });
            }
        });
    }

    public rx.Observable<Map<GameLevel, List<UserWrapper>>> observableUsers() {
        return rx.Observable.create(new rx.Observable.OnSubscribe<Map<GameLevel, List<UserWrapper>>>() {
            @Override
            public void call(final Subscriber<? super Map<GameLevel, List<UserWrapper>>> subscriber) {
                mJMSHelper.subscribe(new MqttCallback() {
                    private int mLeftRetryTime = 1000;
                    @Override
                    public void connectionLost(Throwable throwable) {
                        while (mLeftRetryTime-- > 0) {
                            Log.i("AndroidTVBoilerPlate", String.format("Left retry time is %d", mLeftRetryTime));
                            mJMSHelper.subscribe(this); // reconnect
                        }
                    }

                    @Override
                    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                        Beta.checkUpgrade(false,false);
                        if (mRoom.updateInPush(new String(mqttMessage.getPayload()))) {
                            subscriber.onNext(mRoom.getUserWrapperList());
                        }
                    }

                    @Override
                    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {

                    }
                });
            }
        });
    }
}
