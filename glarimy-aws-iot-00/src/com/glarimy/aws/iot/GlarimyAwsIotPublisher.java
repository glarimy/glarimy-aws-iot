package com.glarimy.aws.iot;

import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

import com.amazonaws.services.iot.client.AWSIotMqttClient;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;
import com.glarimy.aws.iot.utilities.CertificateUtility;
import com.glarimy.aws.iot.utilities.CertificateUtility.KeyStorePasswordPair;

public class GlarimyAwsIotPublisher {
	public static void main(String[] args) throws Exception {
		String clientEndpoint = "a3dyzv7tzx234i.iot.ap-southeast-1.amazonaws.com";
		String clientId = "sdk-java";

		String certificateFile = "GlarimyMAC.cert.pem";
		String privateKeyFile = "GlarimyMAC.private.key";

		KeyStorePasswordPair pair = CertificateUtility.getKeyStorePasswordPair(certificateFile, privateKeyFile);

		AWSIotMqttClient mqtt = new AWSIotMqttClient(clientEndpoint, clientId, pair.keyStore, pair.keyPassword);
		mqtt.connect();

		ScheduledExecutorService service = Executors.newSingleThreadScheduledExecutor();
		service.scheduleAtFixedRate(new Runnable() {

			@Override
			public void run() {
				try {
					mqtt.publish("com/glarimy/aws/iot", "Message from Glarimy on com/glarimy/aws/iot");
					mqtt.publish("com/glarimy/aws", "Message from Glarimy on com/glarimy/aws");
				} catch (Exception e) {
					e.printStackTrace();
				}
			}
		}, 0, 1, TimeUnit.SECONDS);
		AWSIotTopic aws = new GlarimyAwsIotTopic("com/glarimy/aws", AWSIotQos.QOS0);
		AWSIotTopic iot = new GlarimyAwsIotTopic("com/glarimy/aws/iot", AWSIotQos.QOS0);

		mqtt.subscribe(aws, true);
		mqtt.subscribe(iot, true);

	}
}
