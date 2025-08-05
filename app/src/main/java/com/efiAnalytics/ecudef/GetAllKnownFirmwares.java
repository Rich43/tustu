package com.efianalytics.ecudef;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAllKnownFirmwares", propOrder = {"uid", "appName", "appEdition"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/ecudef/GetAllKnownFirmwares.class */
public class GetAllKnownFirmwares {
    protected String uid;
    protected String appName;
    protected String appEdition;

    public String getUid() {
        return this.uid;
    }

    public void setUid(String value) {
        this.uid = value;
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
}
