package com.efianalytics.ecudef;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "ecuDefSubmissionResponse", propOrder = {"md5Checksum", "received", "responseMessage"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/ecudef/EcuDefSubmissionResponse.class */
public class EcuDefSubmissionResponse {
    protected String md5Checksum;
    protected boolean received;
    protected String responseMessage;

    public String getMd5Checksum() {
        return this.md5Checksum;
    }

    public void setMd5Checksum(String value) {
        this.md5Checksum = value;
    }

    public boolean isReceived() {
        return this.received;
    }

    public void setReceived(boolean value) {
        this.received = value;
    }

    public String getResponseMessage() {
        return this.responseMessage;
    }

    public void setResponseMessage(String value) {
        this.responseMessage = value;
    }
}
