/*
 * To change this license header, choose License Headers in Project Properties.
 * To change this template file, choose Tools | Templates
 * and open the template in the editor.
 */
package org.gujavasc.iot.cliente.controller;

import java.util.logging.Level;
import java.util.logging.Logger;
import jssc.SerialPort;
import jssc.SerialPortEvent;
import jssc.SerialPortEventListener;
import jssc.SerialPortException;
import org.gujavasc.iot.cliente.mqtt.MqttPublisher;
import org.gujavasc.iot.cliente.util.PropertyUtils;

/**
 *
 * @author avila
 */
public class SerialController {

    SerialPort serialPort;
    MqttPublisher publisher;

    public SerialController(MqttPublisher publisher) {
        this.publisher = publisher;
    }

    public void start() {
        String portAddress = PropertyUtils.getPropertyString("sensor.port");
        serialPort = new SerialPort(portAddress);
        try {
            serialPort.openPort();
            serialPort.setParams(9600, 8, 1, 0);
            serialPort.addEventListener(new SerialPortReader());
        } catch (SerialPortException ex) {
            System.out.println(ex);
        }
    }

    public void writeString(String message) {
        try {
            serialPort.writeString(message);
        } catch (SerialPortException ex) {
            Logger.getLogger(SerialController.class.getName()).log(Level.SEVERE, null, ex);
        }
    }

    public class SerialPortReader implements SerialPortEventListener {

        String value = "";

        @Override
        public void serialEvent(SerialPortEvent event) {
            try {
                byte b = serialPort.readBytes(1)[0];
                if ((char) b == '\n') {
                    if (!value.isEmpty()) {
                        sendMessage(value);
                    }
                    value = "";
                } else if ((char) b != '\n') {
                    value += (char) b;
                }
            } catch (SerialPortException ex) {
                System.out.println(ex);
            }
        }

        private void sendMessage(String message) {
            publisher.sendMessage(message);
        }
    }
}
