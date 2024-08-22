package com.axonivy.connector.skribble.mocks;

import java.io.IOException;
import java.io.InputStream;
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

/**
 * A simple REST backend for a third-party service used in tests:<br/>
 * - provides static results and therefore simplifies our tests <br/>
 * - does not require authentication or real-world secrets <br/>
 * - built with ivy standard tools: easy to maintain for everyone <br/>
 */
@Path(SkribbleServiceMock.PATH_SUFFIX)
@PermitAll // allow unauthenticated calls
@Hidden // do not show me on swagger-ui or openapi3 resources.
@SuppressWarnings("all")
public class SkribbleServiceMock {

  static final String PATH_SUFFIX = "mock";

  // URI where this mock can be reached: to be referenced in tests that use it!
  public static final String URI = "{"+IvyDefaultJaxRsTemplates.APP_URL+"}/api/"+PATH_SUFFIX;

  @GET
  @Path("signature-requests")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getAllSignatureRequest(@QueryParam("version") String version) {
    // best practice: record a JSON response of your real service under test
    // store it as JSON file and statically return it
    return Response.ok()
      .entity(load("get_signature-response.json"))
      .build();
  }
  
  @POST
  @Path("signature-requests")
  @Produces(MediaType.APPLICATION_JSON)
  public Response createSignatureRequest(@QueryParam("version") String version) {
    // best practice: record a JSON response of your real service under test
    // store it as JSON file and statically return it
    return Response.ok()
      .entity(load("post_signature-response.json"))
      .build();
  }

  @GET
  @Path("documents/{id}/content") //documents/{id}/content
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDocumentContentDataById(@PathParam("id") String version) {
    // best practice: record a JSON response of your real service under test
    // store it as JSON file and statically return it
    return Response.ok()
      .entity(load("get_document-content-response.json"))
      .build();
  }
  
  @GET
  @Path("documents/{id}")
  @Produces(MediaType.APPLICATION_JSON)
  public Response getDocumentMetaDataById(@PathParam("id") String version) {
    // best practice: record a JSON response of your real service under test
    // store it as JSON file and statically return it
    return Response.ok()
      .entity(load("get_document-meta-response.json"))
      .build();
  }
  
  private static String load(String json) {
    try (var is = SkribbleServiceMock.class.getResourceAsStream("json/"+json)) {
      if (is == null) {
        throw new RuntimeException("The json file '"+json+"' does not exist.");
      }
      return IOUtils.toString(is, StandardCharsets.UTF_8);
    } catch (IOException ex) {
      throw new RuntimeException("Failed to read json "+json, ex);
    }
  }
}
