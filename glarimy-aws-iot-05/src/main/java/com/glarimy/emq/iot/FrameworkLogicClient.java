package com.glarimy.emq.iot;

public class FrameworkLogicClient extends AbstractAWSClient {

	public FrameworkLogicClient(String clientId, String uri, String certFile, String keyFile, String subscriptions, int delay)
			throws Exception {
		super(clientId, uri, certFile, keyFile, subscriptions, delay);
	}

}
