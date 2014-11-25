/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gujavasc.iot.cliente.mqtt;

import java.util.logging.Level;
import java.util.logging.Logger;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.gujavasc.iot.client.App;
import org.gujavasc.iot.cliente.controller.SerialController;
import org.gujavasc.iot.cliente.util.PropertyUtils;

/**
 *
 * @author avila
 */
public class MqttSubscriber {

    MqttClient mqttClient;
    SerialController serialControler;

    public MqttSubscriber(SerialController serialControler) {
        this.serialControler = serialControler;
    }

    public void start() {
        String mqttBroker = PropertyUtils.getPropertyString("mqtt.broker");
        String mqttTopic = PropertyUtils.getPropertyString("mqtt.topic.subcribe");
        
        try {
            mqttClient = new MqttClient(mqttBroker, MqttClient.generateClientId());
            mqttClient.connect();
            mqttClient.setCallback(new MqttListenter());
            mqttClient.subscribe(mqttTopic);
        } catch (MqttException ex) {
            Logger.getLogger(App.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class MqttListenter implements MqttCallback {

        @Override
        public void connectionLost(Throwable thrwbl) {
            System.out.println("Connection Lost");
        }

        @Override
        public void messageArrived(String string, MqttMessage mm) throws Exception {
            System.out.println("Receive message: " + mm);
            serialControler.writeString(mm.toString());
        }

        @Override
        public void deliveryComplete(IMqttDeliveryToken imdt) {
            System.out.println("Delivery Complete");
        }

    }
}
