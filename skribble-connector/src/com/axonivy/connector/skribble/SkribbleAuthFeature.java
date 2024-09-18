package com.axonivy.connector.skribble;

import java.io.IOException;
import java.time.Instant;
import java.util.UUID;

import javax.ws.rs.Priorities;
import javax.ws.rs.client.ClientRequestContext;
import javax.ws.rs.client.ClientRequestFilter;
import javax.ws.rs.client.Entity;
import javax.ws.rs.core.Feature;
import javax.ws.rs.core.FeatureContext;
import javax.ws.rs.core.MediaType;

import com.auth0.jwt.JWT;
import com.skribble.api.v2.client.AccessLoginBody;

import ch.ivyteam.ivy.application.IApplication;
import ch.ivyteam.ivy.environment.Ivy;
import ch.ivyteam.ivy.rest.client.FeatureConfig;

public class SkribbleAuthFeature implements Feature {

  @Override
  public boolean configure(FeatureContext context) {
    context.register(new AuthFilter(), Priorities.AUTHENTICATION);
    return true;
  }

  private static class AuthFilter implements ClientRequestFilter {

    private static final String SKRIBBLE_AUTH_SESSION_TOKEN = "skribble.auth.sessionToken";

    @Override
    public void filter(ClientRequestContext ctxt) throws IOException {
      if (ctxt.getUri().toASCIIString().contains("/login")) {
        return;
      }

      var sessionToken = (String) IApplication.current().getAttribute(SKRIBBLE_AUTH_SESSION_TOKEN);

      if (sessionToken != null) {
        var decoded = new JWT().decodeJwt(sessionToken);
        var expiry = decoded.getExpiresAtAsInstant();
        if (expiry.isBefore(Instant.now())) {
          sessionToken = null;
        }
      }

      if (sessionToken == null) {
        sessionToken = login(ctxt);
        IApplication.current().setAttribute(SKRIBBLE_AUTH_SESSION_TOKEN, sessionToken);
      }
      ctxt.getHeaders().putSingle("Authorization", "Bearer " + sessionToken);
    }

    private String login(ClientRequestContext ctxt) {

      var config = new FeatureConfig(ctxt.getConfiguration(), SkribbleAuthFeature.class);
      String key = config.readMandatory("AUTH.key");

      var login = new AccessLoginBody();
      login.setApiKey(key);
      login.setUsername(config.readMandatory("AUTH.username"));

      return Ivy.rest().client(UUID.fromString("6c92d3d7-cb33-4cfa-964a-8242ad165742"))
              .path("v2/access/login")
              .request().post(Entity.entity(login, MediaType.APPLICATION_JSON), String.class);
    }
  }
}
