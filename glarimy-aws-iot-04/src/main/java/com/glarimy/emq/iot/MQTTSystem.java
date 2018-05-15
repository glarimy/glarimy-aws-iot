package com.glarimy.emq.iot;

public class MQTTSystem {
	public static void main(String[] args) throws Exception {
		String intranet = "ws://localhost:8083";
		String internet = "a3dyzv7tzx234i.iot.ap-southeast-1.amazonaws.com";
		String uname = "admin";
		String pwd = "public";
		String certFile = "GlarimyMAC.cert.pem";
		String keyFile = "GlarimyMAC.private.key";

		new UniAgentClient("uni-agent", intranet, internet, uname, pwd, certFile, keyFile, "/sharp/+/+/request", 10000);
		new FrameworkLogicClient("framework", internet, certFile, keyFile, "/sharp/+/+/response", 0);

		SRDMClient sclient = new SRDMClient("srdm", intranet, uname, pwd, "/sharp/srdm/+/response", 0);
		MICASClient mclient = new MICASClient("micas", internet, certFile, keyFile, "/sharp/micas/+/response", 0);

		mclient.publish("/sharp/uni-agent/micasjob/request", "/sharp/micas/micasjob/response:MICAS JOB DATA", false);
		sclient.publish("/sharp/uni-agent/srdmjob/request", "/sharp/srdm/srdmjob/response:SRDM JOB DATA", false);
	}
}
