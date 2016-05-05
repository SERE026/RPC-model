package com.ocr;

import java.io.File;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


public class TesseractDemo {

	private static Logger LOG = LoggerFactory.getLogger(TesseractDemo.class);
	
	public static void main(String[] args) throws Exception {
	
		String imgpath = args[0]; //"C:\\Users\\sere\\Desktop\\tess4j.png"
		//String imgpath = "C:\\Users\\sere\\Desktop\\tess4j.png";
	    
	    //String dataPath = Tesseract.class.getResource("/tessdata").toString();
	   // String dataPath = "D:\\qqd_new_workspace\\un_company_maven\\RPC-model\\Rpc\\src\\main\\resources\\tessdata";
	   // String dataPath = args[1];
	   // instance.setDatapath(dataPath);
	    //System.out.println(dataPath);
	    
	    File file = new File(imgpath);  
        String recognizeText = new OCREngine().ocrParse(file);  
        LOG.info("OCR读取出来的信息为：{} \t",recognizeText);  
        
        System.out.println("OCR读取出来的信息为：{} \t"+recognizeText);
	}
}

