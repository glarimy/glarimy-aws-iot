package com.glarimy.emq.iot;

import java.util.StringTokenizer;

import org.eclipse.paho.client.mqttv3.IMqttMessageListener;
import org.eclipse.paho.client.mqttv3.MqttClient;
import org.eclipse.paho.client.mqttv3.MqttConnectOptions;
import org.eclipse.paho.client.mqttv3.MqttMessage;

public class AbstractEMQClient implements MQTTClient {
	String uri = "ws://localhost:8083";
	String clientId;
	String uname;
	String password;
	String subscriptions;
	int delay;
	MqttClient mqtt;

	public AbstractEMQClient(String clientId, String uri, String uname, String pwd, String subscriptions, int delay)
			throws Exception {
		this.clientId = clientId;
		MqttConnectOptions opts = new MqttConnectOptions();
		opts.setUserName("admin");
		opts.setPassword("private".toCharArray());
		mqtt = new MqttClient(uri, clientId);
		mqtt.connect(opts);
		mqtt.subscribe(subscriptions, new IMqttMessageListener() {
			@Override
			public void messageArrived(String topic, MqttMessage message) throws Exception {
				StringTokenizer tst = new StringTokenizer(topic, "/");
				tst.nextToken(); // sharp
				tst.nextToken(); // target client-id
				String job = tst.nextToken(); // job
				String type = tst.nextToken(); // request/response
				if (type.equalsIgnoreCase("response")) {
					System.out.println(clientId + ": Received response on " + topic);
				} else {
					System.out.println(clientId + ": Received request on " + topic);
					StringTokenizer pst = new StringTokenizer(new String(message.getPayload()), ":");
					onCommand(job, pst.nextToken(), pst.nextToken());
				}
			}
		});
	}

	@Override
	public void publish(String topic, String payload, boolean response) throws Exception {

		MqttMessage message = new MqttMessage();
		message.setPayload(payload.getBytes());
		message.setQos(1);
		mqtt.publish(topic, message);
		if (response)
			System.out.println(clientId + ": Sent response on " + topic);
		else
			System.out.println(clientId + ": Sent request on " + topic);
	}

	@Override
	public void onCommand(String job, String returnTopic, String data) throws Exception {
		Thread.sleep(delay);
		this.publish(returnTopic, data, true);
	}
}
