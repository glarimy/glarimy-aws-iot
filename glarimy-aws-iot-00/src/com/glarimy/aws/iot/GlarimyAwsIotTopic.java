package com.glarimy.aws.iot;

import java.util.Date;

import com.amazonaws.services.iot.client.AWSIotMessage;
import com.amazonaws.services.iot.client.AWSIotQos;
import com.amazonaws.services.iot.client.AWSIotTopic;

public class GlarimyAwsIotTopic extends AWSIotTopic {

	public GlarimyAwsIotTopic(String topic, AWSIotQos qos) {
		super(topic, qos);

	}

	@Override
	public void onMessage(AWSIotMessage message) {
		System.out.println("[Date: " + new Date() + "][Topic:" + message.getTopic() + " ][Message:"
				+ message.getStringPayload() + " ]");
	}
}