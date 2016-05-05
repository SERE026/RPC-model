package com.sere.test;

import java.lang.reflect.InvocationHandler;
import java.lang.reflect.Method;
import java.lang.reflect.Proxy;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

public class Main {
	public static void main(String[] args) {
		//testProxy();
		
		/*Integer a = 100;
		Integer b = 100;
		
		Integer c = 1000;
		Integer d = 100;
		
		System.out.println(a==b);
		System.out.println(c & d);*/
		String str = "2016-10-01 23:59:59";
		Date date = convertDate(str);
		
		System.out.println("============="+date);
	}
	
	public static Date convertDate(String str) {
		if (str != null && !"".equals(str)) {
			SimpleDateFormat format = new SimpleDateFormat(
					"yyyy-MM-dd HH:mm:ss");
			Date date = null;
			try {
				date = format.parse(str);
			} catch (ParseException e) {
				e.printStackTrace();
			}
			return date;
		}
		return null;
	}
	
	public static void testProxy(){
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
