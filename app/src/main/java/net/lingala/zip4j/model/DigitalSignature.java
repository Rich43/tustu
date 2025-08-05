package net.lingala.zip4j.model;

/* loaded from: zip4j_1.3.1.jar:net/lingala/zip4j/model/DigitalSignature.class */
public class DigitalSignature {
    private int headerSignature;
    private int sizeOfData;
    private String signatureData;

    public int getHeaderSignature() {
        return this.headerSignature;
    }

    public void setHeaderSignature(int headerSignature) {
        this.headerSignature = headerSignature;
    }

    public int getSizeOfData() {
        return this.sizeOfData;
    }

    public void setSizeOfData(int sizeOfData) {
        this.sizeOfData = sizeOfData;
    }

    public String getSignatureData() {
        return this.signatureData;
    }

    public void setSignatureData(String signatureData) {
        this.signatureData = signatureData;
    }
}
