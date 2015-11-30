package com.sere.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;

public class Main {
	public static void main(String[] args) {
		InvocationHandler invacationHandler = new InvocationHandler() {

			@Override
			public Object invoke(Object proxy, Method method, Object[] args) throws Throwable {
				System.out.println("hello,word");
				return null;
			}
		};

		Hello hello = (Hello) Proxy.newProxyInstance(Main.class.getClassLoader(),
				new Class[] { Hello.class }, invacationHandler);
		System.out.println(hello.hello());
	}

	interface Hello {
		public String hello();
	}
}
