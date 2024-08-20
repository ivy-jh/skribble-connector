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

import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.rest.client.FeatureConfig;

public class SkribbleAuthFeature implements Feature {

  @Override
  public boolean configure(FeatureContext context) {
    context.register(new AuthFilter(), Priorities.AUTHENTICATION);
    return true;
  }

  private static class AuthFilter implements ClientRequestFilter {

    @Override
    public void filter(ClientRequestContext ctxt) throws IOException {
      if (ctxt.getUri().toASCIIString().contains("/login")) {
    	  return;
      }
    	
      var config = new FeatureConfig(ctxt.getConfiguration(), SkribbleAuthFeature.class);
      String key = config.readMandatory("AUTH.key");
      
      var login = new AccessLoginBody();
      login.setApiKey(key);
      login.setUsername(config.readMandatory("AUTH.username"));
      
      var token = "";//config.readMandatory("AUTH.sessionToken");
      
      if(token.isEmpty()) {
    	  token = Ivy.rest().client(UUID.fromString("6c92d3d7-cb33-4cfa-964a-8242ad165742"))
    		  		.path("/access/login").request()
    		  		.post(Entity.entity(login, MediaType.APPLICATION_JSON), String.class);
    	  
    	  Ivy.log().info("got token "+token);
    	  Ivy.var().set("ivy.var.skribbleConnector.sessionToken", token);
    	  
      }
      
      ctxt.getHeaders().putSingle("Authorization", "Bearer "+token);
    }
    
    

  }

}

