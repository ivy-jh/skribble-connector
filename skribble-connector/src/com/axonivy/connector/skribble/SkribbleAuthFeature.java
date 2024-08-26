package com.axonivy.connector.skribble;

import java.io.IOException;
import java.util.UUID;

import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.MediaType;

import com.fasterxml.jackson.databind.JsonNode;
import com.skribble.api.v2.client.AccessLoginBody;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.rest.client.FeatureConfig;

public class SkribbleAuthFeature implements Feature {

	private static String sessionToken = null;
	private static boolean isSessionTokenAlive = false;
	
  @Override
  public boolean configure(FeatureContext context) {
    context.register(new AuthFilter(), Priorities.AUTHENTICATION);
    return true;
  }

  private static class AuthFilter implements ClientRequestFilter {

    @Override
    public void filter(ClientRequestContext ctxt) throws IOException {
      if (ctxt.getUri().toASCIIString().contains("/login") || ctxt.getUri().toASCIIString().contains("/health") ) {
    	  return;
      }
    	
      var config = new FeatureConfig(ctxt.getConfiguration(), SkribbleAuthFeature.class);
      String key = config.readMandatory("AUTH.key");
      
      var login = new AccessLoginBody();
      login.setApiKey(key);
      login.setUsername(config.readMandatory("AUTH.username"));
      
      
      try {
    	  sessionToken = (String) IApplication.current().getAttribute("skribble.auth.sessionToken");
          Ivy.log().info("sessionToken from Application: "+sessionToken);
    	  
          var get = Ivy.rest().client(UUID.fromString("6c92d3d7-cb33-4cfa-964a-8242ad165742"))
        		  	.path("/management/health").request()
        		  	.accept("Accept: */*")
        		  	.header("Authorization", "Bearer "+sessionToken)
		  			.get();
          
          Ivy.log().info("get: "+get.readEntity(String.class));
          
          isSessionTokenAlive = get.getStatus() == 200 ? true: false;
          
          Ivy.log().info("is Token alive: "+isSessionTokenAlive);
          
      }catch(Exception e){
    	  isSessionTokenAlive = false;
      }
      
      //var sessionToken = "";   //config.readMandatory("AUTH.sessionToken"); ivy.session. // ivyapp
      
      if(sessionToken == null || !isSessionTokenAlive) {
    	  sessionToken = Ivy.rest().client(UUID.fromString("6c92d3d7-cb33-4cfa-964a-8242ad165742"))
    		  		.path("v2/access/login").request()
    		  		.post(Entity.entity(login, MediaType.APPLICATION_JSON), String.class);
    	  
    	  Ivy.log().info("got token "+sessionToken);
    	  IApplication.current().setAttribute("skribble.auth.sessionToken",sessionToken );
    	  
      }
      
      ctxt.getHeaders().putSingle("Authorization", "Bearer "+sessionToken);
    }
    
    

  }

}

