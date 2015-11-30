package com.sere.service;

public interface HomeService {
	
	/**
	 * 回家
	 * @param name 人
	 * @return
	 */
	public String goHome(String name);
	
	/**
	 * 吃饭
	 * @param restaurant 餐馆
	 * @return
	 */
	public String eat(String restaurant);
}
