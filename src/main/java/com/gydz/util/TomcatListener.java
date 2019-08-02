package com.gydz.util;

import java.util.TimeZone;

import javax.servlet.ServletContextEvent;
import javax.servlet.ServletContextListener;

import com.sun.jna.Platform;

public class TomcatListener implements ServletContextListener{
	
    @Override
    public void contextInitialized(ServletContextEvent servletContextEvent) {
        System.out.println(servletContextEvent.getServletContext());  
        System.out.println("tomcat inited");   
        if(!Platform.isWindows()){  
            System.out.println(TimeZone.getDefault()); 
            final TimeZone zone = TimeZone.getTimeZone("CST");
            TimeZone.setDefault(zone);
            System.out.println(TimeZone.getDefault());
            System.out.println("JnaUtil open success");  
        }
        System.out.println("before insertTask run");
      try {
    	insertTask.runConnection();
    	//insertTask.runIpmiConnector();
		//insertTask.run();
		 
	} catch (Exception e) {
		// TODO Auto-generated catch block
		e.printStackTrace();
	}
    }

    @Override
    public void contextDestroyed(ServletContextEvent servletContextEvent) {
        System.out.println(servletContextEvent); 
        System.out.println("tomcat closed");  
        if(!Platform.isWindows()){
	        System.out.println("JnaUtil close success");
        }
    }

}
