package com.sere.support;

import com.sere.proxy.Invocation;

public interface Server {
	/**
	 * 停止服务
	 */
	public void stop();
	
	/**
	 * 启动服务
	 */
	public void start();
	
	/**
	 * 注册服务
	 * @param interfaceDefiner 接口 
	 * @param classImpl 实现类
	 */
	public void register(Class interfaceDefiner,Class classImpl);
	
	/**
	 * 远程调用服务
	 * @param invo
	 */
	public void call(Invocation invo);
	
	/**
	 * 服务状态 是否运行
	 * @return
	 */
	public boolean isRunning();
	
	/**
	 * 服务端口
	 * @return
	 */
	public int getPort();
}
