/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gujavasc.iot.server;

import java.io.IOException;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.logging.Level;
import java.util.logging.Logger;

import javax.websocket.OnClose;
import javax.websocket.OnError;
import javax.websocket.OnMessage;
import javax.websocket.OnOpen;
import javax.websocket.Session;
import javax.websocket.server.ServerEndpoint;
import org.eclipse.paho.client.mqttv3.IMqttDeliveryToken;
import org.eclipse.paho.client.mqttv3.MqttCallback;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;

@ServerEndpoint(value = "/temperature")
public class TemperatureEndpoint implements MqttCallback {

    MqttClient client;

    private static final Set<TemperatureEndpoint> connections = new CopyOnWriteArraySet<>();

    private Session session;

    public TemperatureEndpoint() {
    }

    @OnOpen
    public void start(Session session) {
        try {
            client = new MqttClient("tcp://localhost:1883", MqttClient.generateClientId());
            client.connect();
            client.setCallback(this);
            client.subscribe("java/temperature");
        } catch (MqttException ex) {
            Logger.getLogger(TemperatureEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
        this.session = session;
        connections.add(this);
    }

    @OnClose
    public void end() {
        connections.remove(this);
        try {
            client.close();
        } catch (MqttException ex) {
            Logger.getLogger(TemperatureEndpoint.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    @OnMessage
    public void incoming(String message) {
        MQTTpublisher publisher = new MQTTpublisher();
        publisher.sendMessage(message);
    }

    @OnError
    public void onError(Throwable t) throws Throwable {
    }

    private static void broadcast(String msg) {
        connections.stream().forEach((client) -> {
            try {
                synchronized (client) {
                    client.session.getBasicRemote().sendText(msg);
                }
            } catch (IOException e) {
                connections.remove(client);
                try {
                    client.session.close();
                } catch (IOException e1) {
                    // Ignore
                }
            }
        });
    }

    @Override
    public void connectionLost(Throwable thrwbl) {
        broadcast(thrwbl.getMessage());
    }

    @Override
    public void messageArrived(String string, MqttMessage mm) throws Exception {
        broadcast(mm.toString());
    }

    @Override
    public void deliveryComplete(IMqttDeliveryToken imdt) {
        throw new UnsupportedOperationException("Not supported yet."); //To change body of generated methods, choose Tools | Templates.
    }
}
