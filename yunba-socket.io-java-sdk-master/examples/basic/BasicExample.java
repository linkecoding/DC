package basic;
/*
 * socket.io-java-client Test.java
 *
 * Copyright (c) 2012, Enno Boland
 * socket.io-java-client is a implementation of the socket.io protocol in Java.
 *
 * See LICENSE file for more information
 */
import io.socket.IOAcknowledge;
import io.socket.IOCallback;
import io.socket.SocketIO;
import io.socket.SocketIOException;

import org.json.JSONException;
import org.json.JSONObject;

import java.lang.Exception;
import java.lang.Object;
import java.lang.System;
import java.util.List;
import java.nio.charset.Charset;
import java.io.PrintWriter;
import java.io.IOException;
import java.nio.file.Paths;
import java.util.UUID;

public class BasicExample implements IOCallback {
	private SocketIO socket;

	/* 从 yunba.io 获取应用的 appkey */
	private static String APPKEY = "5a3deb9dc58f91fa31b3d004";

	/**
	 * @param args
	 */
	public static void main(String[] args) {
		try {
			new BasicExample();
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public BasicExample() throws Exception {
		socket = new SocketIO();
		socket.connect("http://sock.yunba.io:3000/", this);
	}

	@Override
	public void onMessage(JSONObject json, IOAcknowledge ack) {
		try {
			System.out.println("Server said:" + json.toString(2));
		} catch (JSONException e) {
			e.printStackTrace();
		}
	}

	@Override
	public void onMessage(String data, IOAcknowledge ack) {
		System.out.println("Server said: " + data);
	}

	@Override
	public void onError(SocketIOException socketIOException) {
		System.out.println("an Error occured");
		socketIOException.printStackTrace();
	}

	@Override
	public void onDisconnect() {
		System.out.println("Connection terminated.");
	}

	@Override
	public void onConnect() {
		System.out.println("Connection established");
	}

	@Override
	public void on(String event, IOAcknowledge ack, Object... args) {
		System.out.println("Server triggered event '" + event + "'");

		try {
			if (event.equals("socketconnectack")) {
				onSocketConnectAck();
			} else if (event.equals("connack")) {
				onConnAck((JSONObject)args[0]);
			} else if (event.equals("puback")) {
				onPubAck((JSONObject)args[0]);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
	}

	public void onSocketConnectAck() throws Exception {
		System.out.println("onSocketConnectAck");

		String customId = "";
		try {
			List<String> file = java.nio.file.Files.readAllLines(Paths.get("customid.dat"), Charset.defaultCharset());
			customId = file.get(0);
		} catch (Exception e) {
			customId = UUID.randomUUID().toString();
		}

		// emit connect
		socket.emit("connect", new JSONObject("{'appkey': '" + APPKEY + "', 'customid': '" + customId + "'}"));

		try {
			PrintWriter writer = new PrintWriter("customid.dat", Charset.defaultCharset().name());
			writer.println(customId);
			writer.close();
		} catch (Exception e) {}
	}

	public void onConnAck(JSONObject json) throws Exception {
		System.out.println("onConnAck success " + json.get("success"));

		socket.emit("publish", new JSONObject("{'topic': 't1', 'msg': 'hello form java socket.io client', 'qos': 1}"));
	}

	public void onPubAck(JSONObject json) throws Exception {
		System.out.println("conPubAck success " + json.get("success") + " msg id " + json.get("messageId"));
	}
}
