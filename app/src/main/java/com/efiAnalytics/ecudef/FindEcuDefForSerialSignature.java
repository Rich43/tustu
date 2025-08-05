package com.efianalytics.ecudef;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "findEcuDefForSerialSignature", propOrder = {"serialSignature", "uid"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/ecudef/FindEcuDefForSerialSignature.class */
public class FindEcuDefForSerialSignature {
    protected String serialSignature;
    protected String uid;

    public String getSerialSignature() {
        return this.serialSignature;
    }

    public void setSerialSignature(String value) {
        this.serialSignature = value;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String value) {
        this.uid = value;
    }
}
