package com.glarimy.emq.iot;

import java.util.StringTokenizer;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.glarimy.aws.iot.utilities.CertificateUtility;
import com.glarimy.aws.iot.utilities.CertificateUtility.KeyStorePasswordPair;
import com.glarimy.emq.iot.AbstractAWSClient.FrameworkLogicTopic;

public class UniAgentClient extends AbstractEMQClient {
	private AWSIotMqttClient aws;

	public UniAgentClient(String clientId, String intranetURI, String internetURI, String uname, String pwd,
			String certFile, String keyFile, String subscriptions, int delay) throws Exception {
		super(clientId, intranetURI, uname, pwd, subscriptions, delay);
		listenAWS(internetURI, certFile, keyFile, subscriptions);
	}

	@Override
	public void onCommand(String job, String returnTopic, String data) throws Exception {
		super.onCommand(job, returnTopic, data);
		aws.publish(returnTopic, data);
	}

	public void listenAWS(String uri, String certFile, String keyFile, String subscriptions) throws Exception {
		KeyStorePasswordPair pair = CertificateUtility.getKeyStorePasswordPair(certFile, keyFile);
		aws = new AWSIotMqttClient(uri, clientId, pair.keyStore, pair.keyPassword);
		aws.connect();
		AWSIotTopic topic = new UniAgentAWSTopic(subscriptions, AWSIotQos.QOS1, this);
		aws.subscribe(topic, true);
	}

	class UniAgentAWSTopic extends AWSIotTopic {
		UniAgentClient client;

		public UniAgentAWSTopic(String topic, AWSIotQos qos, UniAgentClient client) {
			super(topic, qos);
			this.client = client;
		}

		@Override
		public void onMessage(AWSIotMessage message) {
			StringTokenizer tst = new StringTokenizer(message.getTopic(), "/");
			tst.nextToken(); // sharp
			tst.nextToken(); // target client-id
			String job = tst.nextToken(); // job
			String type = tst.nextToken(); // request/response
			if (type.equalsIgnoreCase("response")) {
				System.out.println(clientId + ": Received response on " + topic);
			} else {
				System.out.println(clientId + ": Received request on " + topic);
				StringTokenizer pst = new StringTokenizer(new String(message.getPayload()), ":");
				try {
					client.onCommand(job, pst.nextToken(), pst.nextToken());
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}
	}
}
