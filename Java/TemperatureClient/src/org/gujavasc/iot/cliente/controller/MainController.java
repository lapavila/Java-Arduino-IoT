/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gujavasc.iot.cliente.controller;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.gujavasc.iot.cliente.mqtt.MqttPublisher;
import org.gujavasc.iot.cliente.mqtt.MqttSubscriber;

/**
 *
 * @author avila
 */
public class MainController {

    static MqttClient mqttClient;
    
    public void start() {
        
        MqttPublisher publisher = new MqttPublisher();
        
        SerialController serialController = new SerialController(publisher);
        serialController.start();
        
        MqttSubscriber subscriber = new MqttSubscriber(serialController);
        subscriber.start();
    }
}
