package com.efianalytics.ecudef;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getEcuDefinition", propOrder = {"uid", "serialSignature", "fileFormat"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/ecudef/GetEcuDefinition.class */
public class GetEcuDefinition {
    protected String uid;
    protected String serialSignature;
    protected String fileFormat;

    public String getUid() {
        return this.uid;
    }

    public void setUid(String value) {
        this.uid = value;
    }

    public String getSerialSignature() {
        return this.serialSignature;
    }

    public void setSerialSignature(String value) {
        this.serialSignature = value;
    }

    public String getFileFormat() {
        return this.fileFormat;
    }

    public void setFileFormat(String value) {
        this.fileFormat = value;
    }
}
