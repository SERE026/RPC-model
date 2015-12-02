package com.sere.proxy;


import java.lang.reflect.InvocationHandler;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;

import net.sf.cglib.proxy.Enhancer;
import net.sf.cglib.proxy.MethodInterceptor;

import com.sere.service.CglibHomeService;
import com.sere.support.Client;
import com.sere.support.Listener;
import com.sere.support.Server;

public class RPC {
	
	/**
	 * 采用jdk 动态代理实现的RPC
	 * 
	 * @param clazz 需要调用的服务
	 * @param host 可信任的ip
	 * @param port 可信任的端口
	 * @return
	 */
	public static <T> T getJDKProxy(final Class<T> clazz,String host,int port){
		
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
	
	/**
	 * 采用jdk 动态代理的调用
	 * 
	 * @param clazz 需要调用的服务
	 * @return
	 */
	public static <T> T getJDKProxy(Class clazz,Class clazzImp){
		
		try {
			final T obj = (T) clazzImp.newInstance();
		
			InvocationHandler handler = new InvocationHandler(){
				
				/**
				 * 如果invoke 调用proxy 的非final 方法会造成无线循环
				 */
				@Override
				public Object invoke(Object proxy, Method method, Object[] args)
						throws Throwable {
					//前处理
					doBefore();
					
					Object result = method.invoke(obj, args);
					System.out.println("返回值为：>"+result);
					
					//后处理
					doAfter();
					
					return result;
				}
	
				private void doAfter() {
					// TODO Auto-generated method stub
					System.out.println("-----后处理开始-----");
				}
	
				private void doBefore() {
					// TODO Auto-generated method stub
					System.out.println("-----前处理开始-----");
				}
				
			};
			T t = (T) Proxy.newProxyInstance(RPC.class.getClassLoader(), new Class[]{clazz}, handler);
			return t;
		} catch (InstantiationException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		} catch (IllegalAccessException e) {
			// TODO Auto-generated catch block
			e.printStackTrace();
			return null;
		}
	}
	
	/**
	 * 功能：实现远程调用方法
	 * 采用cglib 动态代理创建代理类
	 * @param clazz 需要调用的类
	 * @param methodInterceptor 代理类
	 * @return
	 */
	public static <T> T getCglibProxy(final Class<T> clazz,String host,int port){
		
		Enhancer enhancer = new Enhancer();
		
        enhancer.setSuperclass(clazz);
        
        enhancer.setCallback(new CglibRPCProxyInterceptor(clazz,host,port));
        
        return (T) enhancer.create();
	}
	
	/**
	 * 功能：实现拦截处理 ，调用此方法
	 * 采用cglib 动态代理创建代理类
	 * @param clazz 需要调用的类
	 * @param methodInterceptor 代理类
	 * @return
	 */
	public static <T> T getCglibProxy(final Class<T> clazz){
		
		Enhancer enhancer = new Enhancer();
		
        enhancer.setSuperclass(clazz);
        
        enhancer.setCallback(new CglibProxyInterceptor(clazz));
        
        return (T) enhancer.create();
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
		public void register(Class clazz) {
			// TODO 将服务注册到容器中
			try {
				if(!this.serviceEngine.containsKey(clazz)){
					this.serviceEngine.put(clazz.getName(), clazz.newInstance());
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



