package com.sere.proxy;

import java.lang.reflect.Method;

import com.sere.support.Client;

import net.sf.cglib.proxy.InterfaceMaker;
import net.sf.cglib.proxy.MethodInterceptor;
import net.sf.cglib.proxy.MethodProxy;

/**
 * 采用cglib 实现的远程调用
 * @author sere
 *
 */
public class CglibProxyInterceptor  implements MethodInterceptor{
	
	private Class clazz;
	
	
	public CglibProxyInterceptor() {
		super();
	}

	public CglibProxyInterceptor(Class clazz) {
		super();
		this.clazz = clazz;
	}

	@Override
	public Object intercept(Object obj, Method method, Object[] params,
			MethodProxy proxy) throws Throwable {
		
		//前处理  -- 可以作为分布式事务  注册XAResource
		doBefore();
		
		//调用invoke 方法会造成无线循环
		//调用原始对象方法
		Object result = proxy.invokeSuper(obj, params);
		
		//后处理
		doAfter();
		
		return result;
	}

	private void doAfter() {
		// TODO Auto-generated method stub
		System.out.println("------事务提交成功---------");
	}

	private void doBefore() {
		// TODO Auto-generated method stub
		System.out.println("-------开始往XAResource 注册事务-------");
	}

	public Class getClazz() {
		return clazz;
	}

	public void setClazz(Class clazz) {
		this.clazz = clazz;
	}
	

}
