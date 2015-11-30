package com.sere.proxy;

import java.io.Serializable;
import java.util.Arrays;


public class Invocation implements Serializable{
	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;
	
	private Class interfaces; //服务接口
	
	private MethodInfo method; // 调用方法信息
	
	private Object[] params; //请求参数
	
	private Object result;  //调用返回结果
	
	
	public Class getInterfaces() {
		return interfaces;
	}

	public void setInterfaces(Class interfaces) {
		this.interfaces = interfaces;
	}



	public MethodInfo getMethod() {
		return method;
	}

	public void setMethod(MethodInfo method) {
		this.method = method;
	}



	public Object[] getParams() {
		return params;
	}

	public void setParams(Object[] params) {
		this.params = params;
	}

	
	public Object getResult() {
		return result;
	}

	public void setResult(Object result) {
		this.result = result;
	}



	@Override
	public String toString() {
		return interfaces.getName()+"."+method.getMethodName()+"("+Arrays.toString(params)+")";
	}
	
}
