package com.ocr;

import java.util.Properties;

public class OS {

	
	public static void main(String[] args) {
		
		Properties prop = System.getProperties();

		String os = prop.getProperty("os.name");
		System.out.println(os);

		System.out.println(isWindowsXP());
		/*os.startWith("win") || os.startWith("Win") == windows操作系统*/
	}

	public static boolean isWindowsXP() {
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");
		return os.startsWith("win") || os.startsWith("Win");
	}

	public static boolean isLinux() {
		Properties prop = System.getProperties();
		String os = prop.getProperty("os.name");
		return os.startsWith("Linux") || os.startsWith("linux");
	}
}
