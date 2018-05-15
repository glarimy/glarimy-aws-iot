package com.glarimy.emq.iot;

public class MICASClient extends AbstractAWSClient {

	public MICASClient(String clientId, String uri, String certFile, String keyFile, String subscriptions, int delay)
			throws Exception {
		super(clientId, uri, certFile, keyFile, subscriptions, delay);
	}

}
