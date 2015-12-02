package com.sere.proxy;

import java.lang.reflect.Method;

import com.sere.support.Client;

import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 采用cglib 实现的远程调用
 * @author sere
 *
 */
public class CglibRPCProxyInterceptor  implements MethodInterceptor{

	private String host = "127.0.0.1";
	
	private int port = 20382;
	
	private Class clazz;
	
	
	public CglibRPCProxyInterceptor() {
		super();
	}

	public CglibRPCProxyInterceptor(Class clazz,String host, int port) {
		super();
		this.clazz = clazz;
		this.host = host;
		this.port = port;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] params,
			MethodProxy proxy) throws Throwable {
		
		final Client client = new Client(host,port);
		
		Invocation invocation = new Invocation();
		
		invocation.setInterfaces(clazz); //cglib 直接设置类
		invocation.setMethod(new MethodInfo(method.getName(), method.getParameterTypes()));
		invocation.setParams(params);
		
		client.call(invocation);
//		proxy.invoke(obj, params); 此处不再调用，由服务端调用，实现的RPC
		return invocation.getResult();
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

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	

}
