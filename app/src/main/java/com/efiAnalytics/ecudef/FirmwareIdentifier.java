package com.efianalytics.ecudef;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "firmwareIdentifier", propOrder = {"firmwareVersionInfo", "serialSignature"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/ecudef/FirmwareIdentifier.class */
public class FirmwareIdentifier {
    protected String firmwareVersionInfo;
    protected String serialSignature;

    public String getFirmwareVersionInfo() {
        return this.firmwareVersionInfo;
    }

    public void setFirmwareVersionInfo(String value) {
        this.firmwareVersionInfo = value;
    }

    public String getSerialSignature() {
        return this.serialSignature;
    }

    public void setSerialSignature(String value) {
        this.serialSignature = value;
    }
}
