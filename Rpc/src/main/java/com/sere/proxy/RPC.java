package com.sere.proxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import com.sere.support.Client;
import com.sere.support.Listener;
import com.sere.support.Server;

public class RPC {
	
	
	public static <T> T getProxy(final Class<T> clazz,String host,int port){
		
		final Client client = new Client(host,port);
		
		InvocationHandler handler = new InvocationHandler(){

			@Override
			public Object invoke(Object proxy, Method method, Object[] args)
					throws Throwable {
				// TODO Auto-generated method stub
				Invocation invocation = new Invocation();
				
				invocation.setInterfaces(clazz);
				invocation.setMethod(new MethodInfo(method.getName(), method.getParameterTypes()));
				invocation.setParams(args);
				
				client.call(invocation);
				
				return invocation.getResult();
			}
			
		};
		T t = (T) Proxy.newProxyInstance(RPC.class.getClassLoader(), new Class[]{clazz}, handler);
		return t;
	}
	
	
	
	public static class RPCServer implements Server{
		
		private int port = 20382;
		private Listener listener; 
		private boolean isRuning = true;
		
		/**
		 * 类容器
		 *  key 为接口  ： value 为实现类
		 */
		private Map<String ,Object> serviceEngine = new ConcurrentHashMap<String, Object>();
		
		@Override
		public void stop() {
			this.setRuning(false);
		}

		@Override
		public void start() {
			// TODO Auto-generated method stub
			System.out.println("启动服务器");
			listener = new Listener(this);
			this.isRuning = true;
			listener.start();
		}

		@Override
		public void register(Class interfaceDefiner, Class classImpl) {
			// TODO 将服务注册到容器中
			try {
				if(!this.serviceEngine.containsKey(interfaceDefiner)){
					this.serviceEngine.put(interfaceDefiner.getName(), classImpl.newInstance());
				}
			} catch (InstantiationException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			} catch (IllegalAccessException e) {
				// TODO Auto-generated catch block
				e.printStackTrace();
			}
			
			
		}

		@Override
		public void call(Invocation invo) {
			// TODO Auto-generated method stub
			String interfaceDefiner = invo.getInterfaces().getName();
			Object IServer = this.serviceEngine.get(interfaceDefiner);
			if(IServer!=null){
				MethodInfo method = invo.getMethod();
				try {
					Method m = IServer.getClass().getMethod(method.getMethodName(),  method.getParams());
					
					Object result = m.invoke(IServer, invo.getParams());
					
					invo.setResult(result);
					
				} catch (NoSuchMethodException | IllegalAccessException | IllegalArgumentException
						| InvocationTargetException |SecurityException e) {
					e.printStackTrace();
				}
			}
		}

		@Override
		public boolean isRunning() {
			return isRuning;
		}

		@Override
		public int getPort() {
			return port;
		}
		
		
		public Listener getListener() {
			return listener;
		}

		public void setListener(Listener listener) {
			this.listener = listener;
		}

		public boolean isRuning() {
			return isRuning;
		}

		public void setRuning(boolean isRuning) {
			this.isRuning = isRuning;
		}

		public void setPort(int port) {
			this.port = port;
		}
		
	}
}



