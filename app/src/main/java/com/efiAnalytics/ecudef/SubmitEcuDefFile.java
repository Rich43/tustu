package com.efianalytics.ecudef;

import javax.xml.bind.JAXBElement;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlElementRef;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "submitEcuDefFile", propOrder = {"uid", "serialSignature", "firmwareVersion", "fileData", "fileFormat", "md5Checksum", "appName", "appEdition", "userId", "password"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/ecudef/SubmitEcuDefFile.class */
public class SubmitEcuDefFile {
    protected String uid;
    protected String serialSignature;
    protected String firmwareVersion;

    @XmlElementRef(name = "fileData", type = JAXBElement.class, required = false)
    protected JAXBElement<byte[]> fileData;
    protected String fileFormat;

    @XmlElementRef(name = "md5Checksum", type = JAXBElement.class, required = false)
    protected JAXBElement<byte[]> md5Checksum;
    protected String appName;
    protected String appEdition;
    protected String userId;
    protected String password;

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

    public String getFirmwareVersion() {
        return this.firmwareVersion;
    }

    public void setFirmwareVersion(String value) {
        this.firmwareVersion = value;
    }

    public JAXBElement<byte[]> getFileData() {
        return this.fileData;
    }

    public void setFileData(JAXBElement<byte[]> value) {
        this.fileData = value;
    }

    public String getFileFormat() {
        return this.fileFormat;
    }

    public void setFileFormat(String value) {
        this.fileFormat = value;
    }

    public JAXBElement<byte[]> getMd5Checksum() {
        return this.md5Checksum;
    }

    public void setMd5Checksum(JAXBElement<byte[]> value) {
        this.md5Checksum = value;
    }

    public String getAppName() {
        return this.appName;
    }

    public void setAppName(String value) {
        this.appName = value;
    }

    public String getAppEdition() {
        return this.appEdition;
    }

    public void setAppEdition(String value) {
        this.appEdition = value;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String value) {
        this.userId = value;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(String value) {
        this.password = value;
    }
}
