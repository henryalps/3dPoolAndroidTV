package com.intel.tvpresent.data;

import com.intel.tvpresent.data.local.PreferencesHelper;
import com.intel.tvpresent.data.model.Room;
import com.intel.tvpresent.data.model.UserWrapper;
import com.intel.tvpresent.data.remote.AndroidTvService;
import com.intel.tvpresent.data.remote.JMSHelper;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

import java.util.List;

import javax.inject.Inject;
import javax.inject.Singleton;

import rx.Subscriber;

@Singleton
public class DataManager {

    private final AndroidTvService mAndroidTvService;
    private final PreferencesHelper mPreferencesHelper;
    private final JMSHelper mJMSHelper;
    private final Room mRoom;

    @Inject
    public DataManager(PreferencesHelper preferencesHelper,
                       AndroidTvService androidTvService,
                       JMSHelper jmsHelper,
                       Room room) {
        mPreferencesHelper = preferencesHelper;
        mAndroidTvService = androidTvService;
        mJMSHelper = jmsHelper;
        mRoom = room;
    }

    public PreferencesHelper getPreferencesHelper() {
        return mPreferencesHelper;
    }

    public rx.Observable<List<UserWrapper>> observableUsers() {
        return rx.Observable.create(new rx.Observable.OnSubscribe<List<UserWrapper>>() {
            @Override
            public void call(final Subscriber<? super List<UserWrapper>> subscriber) {
                mJMSHelper.subscribe(new MqttCallback() {
                    @Override
                    public void connectionLost(Throwable throwable) {

                    }

                    @Override
                    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
                        if (mRoom.update(new String(mqttMessage.getPayload()))) {
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
