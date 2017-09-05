package com.intel.tvpresent.callback;

import android.content.Context;
import android.util.Log;

import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class MqttCallbackHandler implements MqttCallback {

    private Context context;
    private String clientId;

    public MqttCallbackHandler(Context context,String clientId) {
        this.context=context;
        this.clientId=clientId;
    }

    @Override
    public void connectionLost(Throwable throwable) {
        Log.d("MqttCallbackHandler","MqttCallbackHandler/connectionLost");
    }

    /**
     *
     * @param s  主题
     * @param mqttMessage  内容信息
     * @throws Exception
     */
    @Override
    public void messageArrived(String s, MqttMessage mqttMessage) throws Exception {
        Log.d("MqttCallbackHandler","MqttCallbackHandler/messageArrived="+s);
        Log.d("MqttCallbackHandler","message="+new String(mqttMessage.getPayload()));
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken iMqttDeliveryToken) {
        Log.d("MqttCallbackHandler","MqttCallbackHandler/deliveryComplete");
    }

}
