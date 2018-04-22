package com.intel.tvpresent.data.remote;

import android.content.Context;

import com.intel.tvpresent.data.ConstantManager;
import com.intel.tvpresent.injection.ApplicationContext;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;

import java.net.InetAddress;
import java.net.URL;
import java.util.UUID;

import javax.inject.Inject;
import javax.inject.Singleton;

/**
 * Created by henryalps on 2017/9/6.
 */

@Singleton
public class JMSHelper {
    private final MqttAndroidClient client;
    private static String clientID = UUID.randomUUID().toString();
    private static final String host = "picky.top";
    private static final String port = "1883";
    private static final String topic = ConstantManager.TOKEN + ".ranklist";

    private MqttConnectOptions conOpt;
    @Inject
    public JMSHelper(@ApplicationContext Context context) {
        String uri = "";
        try {
            String serverIP = InetAddress.getByName(new URL("http://" + host).getHost()).getHostAddress();

            uri="tcp://" + serverIP + ":"+port;

            conOpt = new MqttConnectOptions();
            conOpt.setConnectionTimeout(3000);
            conOpt.setKeepAliveInterval(5);

            conOpt.setUserName("admin");
            conOpt.setPassword("admin".toCharArray());
        } catch (Exception ex) {
            ex.printStackTrace();
        }
        client = new MqttAndroidClient(context, uri, clientID, new MemoryPersistence());
    }

    public void subscribe(final MqttCallback callback) {
        client.setCallback(callback);
        try {
            client.connect(conOpt, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    try {
                        client.subscribe(topic, 0, null, new IMqttActionListener() {
                            @Override
                            public void onSuccess(IMqttToken iMqttToken) {
                                System.out.println("mqtt subscribe success");
                            }

                            @Override
                            public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                                System.out.println("mqtt subscribe failed");
                            }
                        });
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {
                    System.out.println(throwable.getMessage());
                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }
}
