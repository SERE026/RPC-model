package com.sere.support;

import java.io.IOException;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.net.Socket;
import java.net.UnknownHostException;

import com.sere.proxy.Invocation;

public class Client {

	private String host;
	
	private int port;
	
	private Socket socket;
	
	private ObjectOutputStream oos;
	
	private ObjectInputStream ois;
	
	
	
	/**
	 * @param host
	 * @param port
	 */
	public Client(String host, int port) {
		super();
		this.host = host;
		this.port = port;
	}

	public String getHost() {
		return host;
	}
	
	public void setHost(String host) {
		this.host = host;
	}
	
	public int getPort() {
		return port;
	}
	
	public void setPort(int port) {
		this.port = port;
	}
	
	public Socket getSocket() {
		return socket;
	}
	
	public void setSocket(Socket socket) {
		this.socket = socket;
	}
	
	public ObjectOutputStream getOos() {
		return oos;
	}
	
	public void setOos(ObjectOutputStream oos) {
		this.oos = oos;
	}
	
	public ObjectInputStream getOis() {
		return ois;
	}
	
	public void setOis(ObjectInputStream ois) {
		this.ois = ois;
	}
	
	/**
	 * 初始化 通讯协议
	 * @throws UnknownHostException
	 * @throws IOException
	 */
	public void init() throws UnknownHostException, IOException {
		socket = new Socket(host, port);
		oos = new ObjectOutputStream(socket.getOutputStream());
	}

	/**
	 * 调用服务端
	 * @param invo
	 * @throws UnknownHostException
	 * @throws IOException
	 * @throws ClassNotFoundException
	 */
	public void call(Invocation invo) throws UnknownHostException, IOException, ClassNotFoundException {
		
		init();
		
		System.out.println("向服务端发送数据");
		
		oos.writeObject(invo);
		oos.flush();
		ois = new ObjectInputStream(socket.getInputStream());
		
		Invocation result = (Invocation) ois.readObject();
		
		invo.setResult(result.getResult());
	}
	
}
