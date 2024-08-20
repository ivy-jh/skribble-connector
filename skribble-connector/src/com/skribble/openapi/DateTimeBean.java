package com.skribble.openapi;


import java.util.Date;

import javax.faces.bean.ManagedBean;
import javax.faces.bean.ViewScoped;

import ch.ivyteam.ivy.environment.Ivy;


@ViewScoped
@ManagedBean
public class DateTimeBean{
	
    public Date getJavaDateTime(String instantStr) {
		
    	Date date = null;
    	try {
    		date = Date.from(java.time.Instant.parse(instantStr));
    		
	   	}catch(Exception e){
	   		Ivy.log().warn(e);
	   	}

    	return date;
    		
    }      
}
