package com.sshome.ssmcxf.webservice.impl;
import java.lang.reflect.Method;

import javax.jws.WebService;

import org.springframework.context.ApplicationContext;
import org.springframework.context.support.ClassPathXmlApplicationContext;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.sshome.ssmcxf.webservice.CIWJNWebService;

import net.sf.json.JSONObject;

public class staticAppliceFClass {
	
	public static class AppliceIoc
	{
		static ApplicationContext _context;
		
		public AppliceIoc(){
		}
		
		public static Object enterNoParamWs(Object obj1){
			if (_context==null)
				_context=new ClassPathXmlApplicationContext(new String[] {"config/spring-common.xml"});
			
			try{
				JSONObject json1 = JSONObject.fromObject(obj1);
				Class<?>  cls = _context.getBean(json1.getString("CLASSNAME")).getClass();
				Method m = cls.getDeclaredMethod(json1.getString("METHOD"));
				Object obj = m.invoke(_context.getBean(json1.getString("CLASSNAME")));
				return obj;
			} catch (Exception e){
				e.printStackTrace();
				return null;
			}
			
		}

		public static Object enterTheIDU(String obj1, String obj2) {
			// TODO Auto-generated method stub
			if (_context==null)
				_context=new ClassPathXmlApplicationContext(new String[] {"config/spring-common.xml"});
			try{
				JSONObject json1 = JSONObject.fromObject(obj1);
				Class<?>  cls = _context.getBean(json1.getString("CLASSNAME")).getClass();
				Method m = cls.getDeclaredMethod(json1.getString("METHOD"),new Class[]{String.class,String.class});
				Object obj = m.invoke(_context.getBean(json1.getString("CLASSNAME")),new Object[]{obj1,obj2});
				return obj;
			} catch (Exception e){
				e.printStackTrace();
				return null;
			}
		}

		public static Object enterTheWS(String obj1, String obj2) {
			// TODO Auto-generated method stub
			if (_context==null)
				_context=new ClassPathXmlApplicationContext(new String[] {"config/spring-common.xml"});
			try{
				JSONObject json1 = JSONObject.fromObject(obj1);
				Class<?>  cls = _context.getBean(json1.getString("CLASSNAME")).getClass();
				Method m = cls.getDeclaredMethod(json1.getString("METHOD"),new Class[]{String.class});
				Object obj = m.invoke(_context.getBean(json1.getString("CLASSNAME")),new Object[]{obj2});
				return obj;
			} catch (Exception e){
				e.printStackTrace();
				return null;
			}
		}
		
		public static Object UploadWeldFile(String xmlData) {
			// TODO Auto-generated method stub
			
			return xmlData;
		}
		
	}
		
}
