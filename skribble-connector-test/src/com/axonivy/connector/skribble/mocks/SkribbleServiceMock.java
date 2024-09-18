package com.axonivy.connector.skribble.mocks;

import java.io.IOException;
import java.nio.charset.StandardCharsets;

import javax.annotation.security.PermitAll;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import org.apache.commons.io.IOUtils;

import ch.ivyteam.ivy.rest.client.config.IvyDefaultJaxRsTemplates;
import io.swagger.v3.oas.annotations.Hidden;

@Path(SkribbleServiceMock.PATH_SUFFIX)
@PermitAll // allow unauthenticated calls
@Hidden // do not show me on swagger-ui or openapi3 resources.
@SuppressWarnings("all")
public class SkribbleServiceMock {

  static final String PATH_SUFFIX = "mock";

  // URI where this mock can be reached: to be referenced in tests that use it!
  public static final String URI = "{" + IvyDefaultJaxRsTemplates.APP_URL + "}/api/" + PATH_SUFFIX;

  @GET
  @Path("v2/signature-requests")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllSignatureRequest(@QueryParam("version") String version) {
    return Response.ok()
            .entity(load("get_signature-response.json"))
            .build();
  }

  @POST
  @Path("v2/signature-requests")
  @Produces(MediaType.APPLICATION_JSON)
  public Response createSignatureRequest(@QueryParam("version") String version) {
    return Response.ok()
            .entity(load("post_signature-response.json"))
            .build();
  }

  @GET
  @Path("v2/documents/{id}/content")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDocumentContentDataById(@PathParam("id") String version) {
    return Response.ok()
            .entity(load("get_document-content-response.json"))
            .build();
  }

  @GET
  @Path("v2/documents/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDocumentMetaDataById(@PathParam("id") String version) {
    return Response.ok()
            .entity(load("get_document-meta-response.json"))
            .build();
  }

  private static String load(String json) {
    try (var is = SkribbleServiceMock.class.getResourceAsStream("json/" + json)) {
      if (is == null) {
        throw new RuntimeException("The json file '" + json + "' does not exist.");
      }
      return IOUtils.toString(is, StandardCharsets.UTF_8);
    } catch (IOException ex) {
      throw new RuntimeException("Failed to read json " + json, ex);
    }
  }
}
