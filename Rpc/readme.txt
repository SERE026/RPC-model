1. jdk 动态代理必须实现接口 
2. cglib 代理的必须是原始类 ，在MethodInterceptor 中methodProxy.invokeSuper(obj,args) 执行的是原始类（实现类）的方法 