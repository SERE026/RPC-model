package com.sere.service;

import com.sere.annotation.Service;

@Service
public class HomeServiceImpl implements HomeService{

	@Override
	public String goHome(String name) {
		
		return name+"，你妈叫你回家。";
	}

	@Override
	public String eat(String restaurant) {
		// TODO Auto-generated method stub
		return "到 ["+restaurant+"] 吃饭";
	}

}
