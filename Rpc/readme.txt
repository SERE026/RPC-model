1. jdk 动态代理必须实现接口 
2. cglib 代理的必须是原始类 ，在MethodInterceptor 中methodProxy.invokeSuper(obj,args) 执行的是原始类（实现类）的方法 
3. cglib的官方网站：  http://cglib.sourceforge.net/
   cglib目前的最新版本应该是3.2，公司普遍使用的版本也是这个
        官网的samples :  http://cglib.sourceforge.net/xref/samples/
     
4. java 中class结构 
   http://blog.csdn.net/zhangjg_blog/article/details/21486985
   http://www.blogjava.net/hello-yun/archive/2014/09/28/418365.html
5. ASM
   http://www.blogjava.net/hello-yun/archive/2014/09/28/418365.html
   ASM api ： http://tool.oschina.net/apidocs/apidoc?api=asm