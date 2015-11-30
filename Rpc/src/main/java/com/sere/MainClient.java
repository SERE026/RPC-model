package com.sere;

import com.sere.proxy.RPC;
import com.sere.service.HomeService;


public class MainClient {
	
	private static String host = "127.0.0.1";
	
	private static int port = 20382 ;
	
	public static void main(String[] args) {
		
		
		HomeService homeService = RPC.getProxy(HomeService.class, host, port);
		
		System.out.println(homeService.goHome("大神"));
		
		System.out.println(homeService.eat("钱趣多"));
	
	}
}
