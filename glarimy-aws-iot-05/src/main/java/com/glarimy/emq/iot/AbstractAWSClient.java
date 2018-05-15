package com.glarimy.emq.iot;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.glarimy.aws.iot.utilities.CertificateUtility;
import com.glarimy.aws.iot.utilities.CertificateUtility.KeyStorePasswordPair;

public class AbstractAWSClient implements MQTTClient {
	String clientId;
	int delay;
	AWSIotMqttClient mqtt;

	public AbstractAWSClient(String clientId, String uri, String certFile, String keyFile, String subscriptions,
			int delay) throws Exception {
		this.clientId = clientId;
		AWSIotTopic topic = new FrameworkLogicTopic(subscriptions, AWSIotQos.QOS1);
		KeyStorePasswordPair pair = CertificateUtility.getKeyStorePasswordPair(certFile, keyFile);
		mqtt = new AWSIotMqttClient(uri, clientId, pair.keyStore, pair.keyPassword);
		mqtt.connect();
		mqtt.subscribe(topic, true);

	}

	@Override
	public void publish(String topic, String payload, boolean response) throws Exception {
		mqtt.publish(topic, payload);
		if (response)
			System.out.println(clientId + ": Sent response on " + topic);
		else
			System.out.println(clientId + ": Sent request on " + topic);
	}

	@Override
	public void onCommand(String job, String returnTopic, String data) {

	}

	class FrameworkLogicTopic extends AWSIotTopic {

		public FrameworkLogicTopic(String topic, AWSIotQos qos) {
			super(topic, qos);

		}

		@Override
		public void onMessage(AWSIotMessage message) {
			System.out.println(clientId + ": Received response on " + message.getTopic());
		}
	}
}
