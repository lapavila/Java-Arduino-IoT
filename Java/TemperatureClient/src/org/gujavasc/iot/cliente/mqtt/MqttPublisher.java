package org.gujavasc.iot.cliente.mqtt;

import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttException;
import org.eclipse.paho.client.mqttv3.MqttMessage;
import org.eclipse.paho.client.mqttv3.persist.MemoryPersistence;
import org.gujavasc.iot.cliente.util.PropertyUtils;

/**
 *
 * @author avila
 */
public class MqttPublisher implements Runnable {

    private String messageContent;

    String topic;
    int qos = 2;
    String broker;
    String clientId = "JavaTemperature";

    public MqttPublisher() {
        this.broker = PropertyUtils.getPropertyString("mqtt.broker");
        this.topic = PropertyUtils.getPropertyString("mqtt.topic.publish");
    }

    public void sendMessage(String messageContent) {
        this.messageContent = messageContent;
        (new Thread(this)).start();
    }

    @Override
    public void run() {
        MemoryPersistence persistence = new MemoryPersistence();
        try {        
            MqttClient sampleClient = new MqttClient(broker, clientId, persistence);
            MqttConnectOptions connOpts = new MqttConnectOptions();
            connOpts.setCleanSession(true);
            sampleClient.connect(connOpts);
            MqttMessage message = new MqttMessage(messageContent.getBytes());
            message.setQos(qos);
            sampleClient.publish(topic, message);
            sampleClient.disconnect();
            System.out.println("Temperature sento to " + broker);
        } catch (MqttException me) {
            System.out.println("reason " + me.getReasonCode());
            System.out.println("msg " + me.getMessage());
            System.out.println("loc " + me.getLocalizedMessage());
            System.out.println("excep " + me);
        }
    }
}
