package com.sere.util;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;

import javassist.CannotCompileException;
import javassist.ClassPool;
import javassist.CtClass;
import javassist.CtMethod;
import javassist.CtNewMethod;

import org.objectweb.asm.ClassWriter;
import org.objectweb.asm.MethodVisitor;
import org.objectweb.asm.Opcodes;

/**
 * 功能：生成字节码
 * 1. javassist
 * 2. asm 
 * cglib 采用的是asm
 * httl 采用的是javassist
 * @author sere
 *
 */
public class MyGenerate {
	
	public static void main(String[] args) throws Exception {
		generateClassByASM("C:\\Users\\sere\\Desktop\\Test.class");
		
		generateClassByJavassist("C:\\Users\\sere\\Desktop");
	}
	
	
	/**
	 * 功能：通过ASM 生成字节码
	 * @throws FileNotFoundException 
	 * 
	 */
	public static void generateClassByASM(String path) throws Exception{
		
		ClassWriter classWriter = new ClassWriter(0);  
		
		/**
		 * 通过visit 方法确定类的头部信息
		 * 
		 *  version - the class version.
			access - the class's access flags (see Opcodes). This parameter also indicates if the class is deprecated.
			name - the internal name of the class (see getInternalName).
			signature - the signature of this class. May be null if the class is not a generic one, and does not extend or implement generic classes or interfaces.
			superName - the internal of name of the super class (see getInternalName). For interfaces, the super class is Object. May be null, but only for the Object class.
			interfaces - the internal names of the class's interfaces (see getInternalName). May be null
		 */
		classWriter.visit(Opcodes.V1_7, Opcodes.ACC_PUBLIC, "Test", null,
				"java/lang/Object", null);
		
		
		/**
		 *  Visits a method of the class. This method must return a new MethodVisitor instance (or null) each time it is called, i.e., it should not return a previously returned visitor.
			
			Parameters:
			access - the method's access flags (see Opcodes). This parameter also indicates if the method is synthetic and/or deprecated.
			name - the method's name.
			desc - the method's descriptor (see Type).
			signature - the method's signature. May be null if the method parameters, return type and exceptions do not use generic types.
			exceptions - the internal names of the method's exception classes (see getInternalName). May be null.
			
			Returns:
			an object to visit the byte code of the method, or null if this class visitor is not interested in visiting the code of this method.
		*/
		MethodVisitor mv = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "<init>", "()V", null, null);
		
		/**
		 * Starts the visit of the method's code, if any (i.e. non abstract method).
		 */
		mv.visitCode();
		
		/**
		 *  Visits a type instruction. A type instruction is an instruction that takes the internal name of a class as parameter.
			
			Parameters:
			opcode - the opcode of the type instruction to be visited. This opcode is either NEW, ANEWARRAY, CHECKCAST or INSTANCEOF.
			type - the operand of the instruction to be visited. This operand must be the internal name of an object or array class (see getInternalName).
		 */
		mv.visitVarInsn(Opcodes.AALOAD, 0);
		
		/**
		 *  Visits an invokedynamic instruction.
			
			Parameters:
			name - the method's name.
			desc - the method's descriptor (see Type).
			bsm - the bootstrap method.
			bsmArgs - the bootstrap method constant arguments. Each argument must be an Integer, Float, Long, Double, String, Type or Handle value. This method is allowed to modify the content of the array so a caller should expect that this array may change.
		 */
		mv.visitMethodInsn(Opcodes.INVOKESPECIAL, "java/lang/Object", "<init>","()V");  
		
		/**
		 * opcode - the opcode of the instruction to be visited. 
		 */
		mv.visitInsn(Opcodes.RETURN);
		
		mv.visitMaxs(1, 1);
		mv.visitEnd();
		
		
		// 定义code方法  
        MethodVisitor methodVisitor = classWriter.visitMethod(Opcodes.ACC_PUBLIC, "code", "()V",  
                null, null);  
        methodVisitor.visitCode();  
        methodVisitor.visitFieldInsn(Opcodes.GETSTATIC, "java/lang/System", "out",  
                "Ljava/io/PrintStream;");  
        methodVisitor.visitLdcInsn("I'm a Programmer,Just Coding.....");  
        methodVisitor.visitMethodInsn(Opcodes.INVOKEVIRTUAL, "java/io/PrintStream", "println",  
                "(Ljava/lang/String;)V");  
        methodVisitor.visitInsn(Opcodes.RETURN);  
        methodVisitor.visitMaxs(2, 2);
        methodVisitor.visitEnd();  
        
        classWriter.visitEnd();   
		//classWrite 完成
        byte[] clsByte = classWriter.toByteArray();
        
        FileOutputStream fos = new FileOutputStream(new File(path));
        fos.write(clsByte);
        fos.flush();
        fos.close();
        
	}
	
	/**
	 * Javassist是一个开源的分析、编辑和创建Java字节码的类库。
	 * 是由东京工业大学的数学和计算机科学系的 Shigeru Chiba （千叶 滋）所创建的。它已加入了开放源代码JBoss 应用服务器项目,
	 * 通过使用Javassist对字节码操作为JBoss实现动态AOP框架。javassist是jboss的一个子项目.
	 * 其主要的优点，在于简单，而且快速。直接使用java编码的形式，而不需要了解虚拟机指令，就能动态改变类的结构，或者动态生成类。
	 * @param path
	 * @throws Exception 
	 */
	public static void generateClassByJavassist(String path) throws Exception{
		ClassPool pool = ClassPool.getDefault();
		
		//创建类
		CtClass clazz = pool.makeClass("Demo");
		//创建方法
		CtMethod method = CtNewMethod.make("public void call(){}", clazz);
		
		//插入方法代码
		method.insertBefore("System.out.println(\"I'm a Programmer,Just Coding.....\");");
		
		//将方法添加到类中
		clazz.addMethod(method);
		
		//将class 写入文件
		clazz.writeFile(path);
	}
}
