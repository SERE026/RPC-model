package com.sere;

import com.sere.proxy.RPC;
import com.sere.service.CglibHomeService;
import com.sere.service.HomeService;
import com.sere.service.HomeServiceImpl;


public class MainClient {
	
	private static String host = "127.0.0.1";
	
	private static int port = 20382 ;
	
	public static void main(String[] args) {
		
		
		HomeService homeService = RPC.getJDKProxy(HomeService.class, host, port);
		
		System.out.println(homeService.goHome("大神"));
		
		HomeService localHomeService = RPC.getJDKProxy(HomeService.class,HomeServiceImpl.class);
		
		System.out.println(localHomeService.eat("钱趣多"));
		
		
		HomeService cglibHomeService = RPC.getCglibProxy(HomeService.class, host, port);
		
		System.out.println(cglibHomeService.eat("川菜馆"));
		
		CglibHomeService cgHomeService = RPC.getCglibProxy(CglibHomeService.class);
		
		System.out.println(cgHomeService.work("独狼"));
		
		//Cglib 必须是原始类，不能是接口
		HomeService ihomeService = RPC.getCglibProxy(HomeServiceImpl.class);
		
		System.out.println(ihomeService.goHome("独狼"));
		
	}
}
