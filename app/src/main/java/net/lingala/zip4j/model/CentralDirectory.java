package net.lingala.zip4j.model;

import java.util.ArrayList;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/model/CentralDirectory.class */
public class CentralDirectory {
    private ArrayList fileHeaders;
    private DigitalSignature digitalSignature;

    public ArrayList getFileHeaders() {
        return this.fileHeaders;
    }

    public void setFileHeaders(ArrayList fileHeaders) {
        this.fileHeaders = fileHeaders;
    }

    public DigitalSignature getDigitalSignature() {
        return this.digitalSignature;
    }

    public void setDigitalSignature(DigitalSignature digitalSignature) {
        this.digitalSignature = digitalSignature;
    }
}
