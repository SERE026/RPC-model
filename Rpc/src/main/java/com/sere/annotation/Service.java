package com.sere.annotation;

import java.lang.annotation.Documented;
import java.lang.annotation.ElementType;
//import java.lang.annotation.Inherited;
import java.lang.annotation.Retention;
import java.lang.annotation.RetentionPolicy;
import java.lang.annotation.Target;

@Target({ElementType.TYPE,ElementType.METHOD,ElementType.FIELD}) //作用于类
@Retention(RetentionPolicy.RUNTIME) //该注解的生命周期，运行时有效
//@Inherited  //该注解对子类起作用，对实现类不起作用
@Documented
public @interface Service  {
	
	/**
	 * 类的全限定名称
	 * @return
	 */
	public String NAME() default "";
	
	/**
	 * 级别
	 * @return
	 */
	public String LEVEL() default "";
	
	
	public enum LEVEL{ CLASS,METHOD,FIELD};
	
	public enum TYPE { CLASS,METHOD,FIELD};
	
}
