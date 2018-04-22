package com.intel.tvpresent.util;

import android.content.Context;
import android.util.Log;

import com.intel.tvpresent.callback.ConnectCallBackHandler;
import com.intel.tvpresent.callback.MqttCallbackHandler;
import com.intel.tvpresent.data.ConstantManager;

import org.eclipse.paho.android.service.MqttAndroidClient;
import org.eclipse.paho.client.mqttv3.IMqttActionListener;
import org.eclipse.paho.client.mqttv3.IMqttToken;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;

import java.net.InetAddress;
import java.net.URL;
import java.util.UUID;

public class MqttInstance {
    private static String clientID = UUID.randomUUID().toString();
    private static final String host = "picky.top";
    private static final String port = "1883";
    private static final String topic = ConstantManager.TOKEN + ".ranklist";

    private static MqttInstance instance;

    public static MqttInstance getInstance(Context context) {
        if (null == instance) instance = new MqttInstance(context);
        return instance;
    }

    private MqttInstance(Context context) {
        startConnect(context);
    }

    private MqttAndroidClient client;

    private void startConnect(final Context context, final String clientID, String serverIP, String port) {
        //服务器地址
        String uri ="tcp://";
        uri=uri+serverIP+":"+port;
        Log.d(context.getPackageName(), uri+"  "+clientID);
        /**
         * 连接的选项
         */
        MqttConnectOptions conOpt = new MqttConnectOptions();
        /**设计连接超时时间*/
        conOpt.setConnectionTimeout(3000);
        /**设计心跳间隔时间1秒*/
        conOpt.setKeepAliveInterval(5);
        conOpt.setUserName("admin");
        conOpt.setPassword("admin".toCharArray());
        /**
         * 创建连接对象
         */
        client = new MqttAndroidClient(context, uri, clientID);
        /**
         * 连接后设计一个回调
         */
        client.setCallback(new MqttCallbackHandler(context, clientID));
        /**
         * 开始连接服务器，参数：ConnectionOptions,  IMqttActionListener
         */
        try {
            client.connect(conOpt, null, new IMqttActionListener() {
                @Override
                public void onSuccess(IMqttToken iMqttToken) {
                    try {
                        client.subscribe(topic, 0, null, new ConnectCallBackHandler(context));
                    } catch (Exception ex) {
                        ex.printStackTrace();
                    }
                }

                @Override
                public void onFailure(IMqttToken iMqttToken, Throwable throwable) {

                }
            });
        } catch (MqttException e) {
            e.printStackTrace();
        }
    }

    private void startConnect(Context context) {
        try {
            String ip = InetAddress.getByName(new URL("http://" + host).getHost()).getHostAddress();
            startConnect(context, clientID, ip, port);
        } catch (Exception ex) {
            ex.printStackTrace();
        }
    }
}
