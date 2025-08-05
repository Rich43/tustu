package com.efianalytics.ecudef;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "clientEcuDefinitionFile", propOrder = {"fileData", "fileFormat", "md5CheckSum", "serialSignature"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/ecudef/ClientEcuDefinitionFile.class */
public class ClientEcuDefinitionFile {
    protected byte[] fileData;
    protected String fileFormat;
    protected byte[] md5CheckSum;
    protected String serialSignature;

    public byte[] getFileData() {
        return this.fileData;
    }

    public void setFileData(byte[] value) {
        this.fileData = value;
    }

    public String getFileFormat() {
        return this.fileFormat;
    }

    public void setFileFormat(String value) {
        this.fileFormat = value;
    }

    public byte[] getMd5CheckSum() {
        return this.md5CheckSum;
    }

    public void setMd5CheckSum(byte[] value) {
        this.md5CheckSum = value;
    }

    public String getSerialSignature() {
        return this.serialSignature;
    }

    public void setSerialSignature(String value) {
        this.serialSignature = value;
    }
}
