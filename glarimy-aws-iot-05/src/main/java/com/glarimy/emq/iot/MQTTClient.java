package com.glarimy.emq.iot;

public interface MQTTClient {
	public void publish(String topic, String payload, boolean response) throws Exception;

	public void onCommand(String job, String returnTopic, String data) throws Exception;
}
