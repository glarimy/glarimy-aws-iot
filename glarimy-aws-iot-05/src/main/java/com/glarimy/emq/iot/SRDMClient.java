package com.glarimy.emq.iot;

public class SRDMClient extends AbstractEMQClient {

	public SRDMClient(String clientId, String uri, String uname, String pwd, String subscriptions, int delay)
			throws Exception {
		super(clientId, uri, uname, pwd, subscriptions, delay);
	}

}
