package com.sere;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.IOException;
import java.io.InputStream;
import java.lang.annotation.Annotation;
import java.lang.reflect.Method;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLClassLoader;
import java.net.URLConnection;
import java.util.Enumeration;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedHashSet;
import java.util.Map;
import java.util.Properties;
import java.util.Set;
import java.util.jar.JarEntry;
import java.util.jar.JarFile;

import com.sere.service.HomeService;

/**
 * 容器启动
 * 
 * @author sere
 *
 */
public class Launcher {

	public static void main(String[] args) throws IOException,
			ClassNotFoundException, InstantiationException, IllegalAccessException {
		Launcher launcher = new Launcher();

		
		String path = launcher.getClassPath("6");
		String[] arrPath = path.split(";");

		
		DynamicClassLoader loader = new DynamicClassLoader(DynamicClassLoader.class.getClassLoader());

		System.out.println("加载HomeServiceImpl类开始：");
		Class clz = loader.loadClass(arrPath[0], "com.sere.service.HomeServiceImpl.class");
		
		// TODO 取得类上的注解 目前容器的启动还差一个扫描class的机制
		Annotation[] annotation = clz.getAnnotations(); 
		
		HomeService homeService = (HomeService) clz.newInstance();
		
		System.out.println("加载HomeServiceImpl类结束："+homeService.eat("钱趣多"));

		System.in.read();
		System.out.println(path);
	}

	private static void test1() {
		String path = "D:\\test.jar";// 外部jar包的路径
		Set<Class<?>> classes = new LinkedHashSet<Class<?>>();// 所有的Class对象
		Map<Class<?>, Annotation[]> classAnnotationMap = new HashMap<Class<?>, Annotation[]>();// 每个Class对象上的注释对象
		Map<Class<?>, Map<Method, Annotation[]>> classMethodAnnoMap = new HashMap<Class<?>, Map<Method, Annotation[]>>();// 每个Class对象中每个方法上的注释对象
		try {
			JarFile jarFile = new JarFile(new File(path));
			URL url = new URL("file:" + path);
			ClassLoader loader = new URLClassLoader(new URL[] { url });// 自己定义的classLoader类，把外部路径也加到load路径里，使系统去该路经load对象
			Enumeration<JarEntry> es = jarFile.entries();
			while (es.hasMoreElements()) {
				JarEntry jarEntry = (JarEntry) es.nextElement();
				String name = jarEntry.getName();
				if (name != null && name.endsWith(".class")) {// 只解析了.class文件，没有解析里面的jar包
					// 默认去系统已经定义的路径查找对象，针对外部jar包不能用
					// Class<?> c =
					// Thread.currentThread().getContextClassLoader().loadClass(name.replace("/",
					// ".").substring(0,name.length() - 6));
					Class<?> c = loader.loadClass(name.replace("/", ".")
							.substring(0, name.length() - 6));// 自己定义的loader路径可以找到
					System.out.println(c);
					classes.add(c);
					Annotation[] classAnnos = c.getDeclaredAnnotations();
					classAnnotationMap.put(c, classAnnos);
					Method[] classMethods = c.getDeclaredMethods();
					Map<Method, Annotation[]> methodAnnoMap = new HashMap<Method, Annotation[]>();
					for (int i = 0; i < classMethods.length; i++) {
						Annotation[] a = classMethods[i]
								.getDeclaredAnnotations();
						methodAnnoMap.put(classMethods[i], a);
					}
					classMethodAnnoMap.put(c, methodAnnoMap);
				}
			}
			System.out.println(classes.size());
		} catch (IOException e) {
			e.printStackTrace();
		} catch (ClassNotFoundException e) {
			e.printStackTrace();
		}
	}

	private static void test2() {
		String path = "D:\\test.jar";// 此jar包里还有别的jar包
		try {
			JarFile jarfile = new JarFile(new File(path));
			Enumeration<JarEntry> es = jarfile.entries();
			while (es.hasMoreElements()) {
				JarEntry je = es.nextElement();
				String name = je.getName();
				if (name.endsWith(".jar")) {// 读取jar包里的jar包
					File f = new File(name);
					JarFile j = new JarFile(f);
					Enumeration<JarEntry> e = j.entries();
					while (e.hasMoreElements()) {
						JarEntry jarEntry = (JarEntry) e.nextElement();
						System.out.println(jarEntry.getName());
						// .........接下去和上面的方法类似
					}
				}
				// System.out.println(je.getName());
				if (je.getName().equals("entity_pk.properties")) {
					InputStream inputStream = jarfile.getInputStream(je);
					Properties properties = new Properties();
					properties.load(inputStream);
					Iterator<Object> ite = properties.keySet().iterator();
					while (ite.hasNext()) {
						Object key = ite.next();
						System.out.println(key + " : " + properties.get(key));
					}
				}
			}
		} catch (IOException e) {
			e.printStackTrace();
		}
	}

	/**
	 * 获取类加载路径
	 * 
	 * @param type
	 *            1: 获取类加载的根路径 <br/>
	 *            2: 获取当前类的所在工程路径 <br/>
	 *            3: 获取项目路径 <br/>
	 *            4: 通过URL获取类加载路径 等同1 <br/>
	 *            5: 获取当前工程路径 <br/>
	 *            6: 获取所有的类路径 包括jar包的路径<br/>
	 * @return
	 * @throws IOException
	 */
	public String getClassPath(String type) throws IOException {

		switch (type) {
		case "1":
			// 第一种：获取类加载的根路径 D:\git\daotie\daotie\target\classes
			File f = new File(this.getClass().getResource("/").getPath());
			System.out.println(f);
			return f.getAbsolutePath();

		case "2":
			// 获取当前类的所在工程路径; 如果不加“/” 获取当前类的加载目录
			// D:\git\daotie\daotie\target\classes\my
			File f2 = new File(this.getClass().getResource("").getPath());
			System.out.println(f2);
			return f2.getAbsolutePath();

		case "3":

			// 第二种：获取项目路径 D:\git\daotie\daotie
			File directory = new File("");// 参数为空
			String courseFile = directory.getCanonicalPath();
			System.out.println(courseFile);
			return courseFile;

		case "4":

			// 第三种： file:/D:/git/daotie/daotie/target/classes/
			URL xmlpath = this.getClass().getClassLoader().getResource("");
			System.out.println(xmlpath);
			return xmlpath.getPath();

		case "5":

			// 第四种： D:\git\daotie\daotie
			System.out.println(System.getProperty("user.dir"));
			return System.getProperty("user.dir");

		case "6":
			/*
			 * 结果： C:\Documents and Settings\Administrator\workspace\projectName
			 * 获取当前工程路径
			 */

			// 第五种： 获取所有的类路径 包括jar包的路径
			System.out.println(System.getProperty("java.class.path"));
			return System.getProperty("java.class.path");

		default:
			return null;
		}

	}
}

class DynamicClassLoader extends ClassLoader {

	public DynamicClassLoader(ClassLoader parent) {
		super(parent);
	}

	@SuppressWarnings("unchecked")
	public Class loadClass(String classPath, String className)
			throws ClassNotFoundException {
		try {
			String url = classPathParser(classPath)
					+ classNameParser(className);
			System.out.println(url);
			URL myUrl = new URL(url);
			URLConnection connection = myUrl.openConnection();
			InputStream input = connection.getInputStream();
			ByteArrayOutputStream buffer = new ByteArrayOutputStream();
			int data = input.read();
			while (data != -1) {
				buffer.write(data);
				data = input.read();
			}
			input.close();
			byte[] classData = buffer.toByteArray();
			return defineClass(noSuffix(className), classData, 0,
					classData.length);
		} catch (MalformedURLException e) {
			e.printStackTrace();
		} catch (IOException e) {
			e.printStackTrace();
		}
		return null;
	}

	private String pathParser(String path) {
		return path.replaceAll("\\\\", "/");
	}

	private String classPathParser(String path) {
		String classPath = pathParser(path);
		if (!classPath.startsWith("file:")) {
			classPath = "file:" + classPath;
		}
		if (!classPath.endsWith("/")) {
			classPath = classPath + "/";
		}
		return classPath;
	}

	private String classNameParser(String className) {
		return className.substring(0, className.lastIndexOf(".")).replaceAll(
				"\\.", "/")
				+ className.substring(className.lastIndexOf("."));
	}

	private String noSuffix(String className) {
		return className.substring(0, className.lastIndexOf("."));
	}

	public static void main(String[] arguments) throws Exception {
		String classPath = "C:\\Documents and Settings\\Administrator\\Workspaces\\MyEclipse7.5\\lhsp\\web\\WEB-INF\\classes";
		String className = "libra.law.util.Test.class";
		new DynamicClassLoader(DynamicClassLoader.class.getClassLoader())
				.loadClass(classPath, className).newInstance();
	}
}
