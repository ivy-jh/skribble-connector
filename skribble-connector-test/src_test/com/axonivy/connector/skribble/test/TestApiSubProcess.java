package com.axonivy.connector.skribble.test;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import com.axonivy.connector.skribble.demo.SampleHelper;
import com.axonivy.connector.skribble.documents.DocumentsData;
import com.axonivy.connector.skribble.mocks.SkribbleServiceMock;
import com.axonivy.connector.skribble.signaturerequest.SignatureRequestData;
import com.skribble.api.v2.client.CreateSignature;
import com.skribble.api.v2.client.CreateSignatureRequest;

import ch.ivyteam.ivy.bpm.engine.client.BpmClient;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmElement;
import ch.ivyteam.ivy.bpm.engine.client.element.BpmProcess;
import ch.ivyteam.ivy.bpm.exec.client.IvyProcessTest;
import ch.ivyteam.ivy.environment.AppFixture;
import ch.ivyteam.ivy.rest.client.mapper.JsonFeature;
import ch.ivyteam.ivy.rest.client.security.CsrfHeaderFeature;

@IvyProcessTest(enableWebServer = true)
class TestApiSubProcess {

  private static final BpmProcess SIGNATURE_REQUEST = BpmProcess.name("SignatureRequest");
  private static final BpmProcess DOCUMENTS = BpmProcess.name("Documents");

  private interface Start {

    BpmElement GET_ALL_SIGNATURE_REQUEST = SIGNATURE_REQUEST
            .elementName("getAll(SignatureRequestSearchParameters)");
    BpmElement CREATE_SIGNATURE_REQUEST = SIGNATURE_REQUEST.elementName("create(CreateSignatureRequest)");
    BpmElement GET_DOCUMENT_CONTENT = DOCUMENTS.elementName("getContentData(String)");
    BpmElement GET_DOCUMENT_META = DOCUMENTS.elementName("getMetaData(String)");
  }

  @BeforeEach
  void setup(AppFixture fixture) {
    fixture.config("RestClients.Skribble.Url", SkribbleServiceMock.URI);
    fixture.config("RestClients.Skribble.Features",
            List.of(JsonFeature.class.getName(), CsrfHeaderFeature.class.getName()));
  }

  @Test
  void callSubProcess_getAllSignatureRequest(BpmClient bpmClient) {
    var result = bpmClient.start().subProcess(Start.GET_ALL_SIGNATURE_REQUEST).execute();
    SignatureRequestData data = result.data().last();
    assertThat(data.getSignatureRequests()).hasSize(1);
    assertThat(data.getSignatureRequests().get(0).getSignatures()).hasSize(2);
    assertThat(
            data.getSignatureRequests().get(0).getSignatures().get(0).getSignerIdentityData().getFirstName())
                    .contains("Max");
  }

  @Test
  void callSubProcess_createSignatureRequest(BpmClient bpmClient) {
    CreateSignatureRequest sample = SampleHelper.createSignatureRequestDocSample("Test-Title",
            "Test-message");
    CreateSignature cs = SampleHelper.createSignature("max.muster@yxz.com", false);
    cs.setSignerIdentityData(SampleHelper.createSignerIdentityData("max.muster@yxz.com", "Max", "Muster"));
    cs.setVisualSignature(SampleHelper.createVisualSignature(100, 100));
    sample.addSignaturesItem(cs);
    var result = bpmClient.start().subProcess(Start.CREATE_SIGNATURE_REQUEST).withParam("create", sample)
            .execute();
    SignatureRequestData data = result.data().last();
    assertThat(data.getSignatureRequest().getDocumentId().startsWith("6a41f"));
  }

  @Test
  void callSubProcess_getDocumentContent(BpmClient bpmClient) {
    var result = bpmClient.start().subProcess(Start.GET_DOCUMENT_CONTENT)
            .withParam("documentId", "20c535e0-4260-f52a-b2ba-a45eb280d9a3").execute();
    DocumentsData data = result.data().last();
    assertThat(data.getContent()).startsWith("JVBER");
  }

  @Test
  void callSubProcess_getDocumentMeta(BpmClient bpmClient) {
    var result = bpmClient.start().subProcess(Start.GET_DOCUMENT_META)
            .withParam("documentId", "20c535e0-4260-f52a-b2ba-a45eb280d9a3").execute();
    DocumentsData data = result.data().last();
    assertThat(data.getDocument().getTitle()).startsWith("Test-Title");
  }
}
