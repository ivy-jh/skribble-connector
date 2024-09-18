package com.axonivy.connector.skribble.demo;

import com.skribble.api.v2.client.CreateSignature;
import com.skribble.api.v2.client.CreateSignatureRequest;
import com.skribble.api.v2.client.Position;
import com.skribble.api.v2.client.SignerIdentityData;
import com.skribble.api.v2.client.VisualSignature;

public class SampleHelper {

  private static final float POSITION_HEIGH = 40;
  private static final float POSITION_WIDTH = 140;
  private static final String POSITION_PAGE = "0";

  public static CreateSignatureRequest createSignatureRequestDocSample(String title, String message) {
    CreateSignatureRequest csr = new CreateSignatureRequest();
    csr.setTitle(title);
    csr.setMessage(message);
    csr.setContent("JVBERi0xLjcNCiW1tbW1DQox..");
    return csr;
  }

  public static CreateSignature createSignature(String accountEmail, boolean notify) {
    CreateSignature cs = new CreateSignature();
    cs.setAccountEmail(accountEmail);
    cs.notify(notify);
    return cs;
  }

  public static VisualSignature createVisualSignature(float positionX, float positionY) {
    VisualSignature vs = new VisualSignature();
    vs.setPosition(
            SampleHelper.createPosition(positionX, positionY, POSITION_PAGE, POSITION_HEIGH, POSITION_WIDTH));
    return vs;
  }

  public static SignerIdentityData createSignerIdentityData(String email, String fistname, String lastname) {
    SignerIdentityData si = new SignerIdentityData();
    si.setEmailAddress(email);
    si.setFirstName(fistname);
    si.setLastName(lastname);
    return si;
  }

  private static Position createPosition(float x, float y, String page, float height, float width) {
    Position po = new Position();
    po.setHeight(height);
    po.setWidth(width);
    po.setX(x);
    po.setY(y);
    po.setPage(page);
    return po;
  }
}
