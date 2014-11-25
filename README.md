Java-Arduino-IoT
================

Java Arduino e Internet of Things

Demo da palestra Java, Arduino e a internet das coisas.

* **Arduino**: possui o sketch arduino para a leitura da temperatura e controle do Led/Rele
* **Java**: possui dois projetos:
 * **Temperature Client**: Comunica com o Arduino via serial, recebe o sinal de leitura das temperaturas
 * **TemperatureServer**: recebe as leituras e exibe via websocket

Itens necessários para operacao:

* Java JDK 1.7+ : http://www.oracle.com/technetwork/java/javase/downloads/index.html
* Tomcat 8.0.14+: http://tomcat.apache.org/
* Mosquitto MQTT server: http://www.mosquitto.org



