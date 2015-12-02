package com.sere;

import com.sere.proxy.RPC;
import com.sere.service.CglibHomeService;
import com.sere.service.HomeService;
import com.sere.service.HomeServiceImpl;
import com.sere.support.Server;

/**
 * 服务端进行服务注册
 * @author sere
 *
 */
public class MainServer {
	
	public static void main(String[] args) {
		Server server = new RPC.RPCServer();
		server.register(HomeService.class, HomeServiceImpl.class);
		server.register(CglibHomeService.class);
		server.start();
	}
}
