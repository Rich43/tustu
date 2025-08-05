package com.efianalytics.userprofile;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "userChangeResult", propOrder = {"longMessage", "result", "shortMessage"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/userprofile/UserChangeResult.class */
public class UserChangeResult {
    protected String longMessage;
    protected String result;
    protected String shortMessage;

    public String getLongMessage() {
        return this.longMessage;
    }

    public void setLongMessage(String value) {
        this.longMessage = value;
    }

    public String getResult() {
        return this.result;
    }

    public void setResult(String value) {
        this.result = value;
    }

    public String getShortMessage() {
        return this.shortMessage;
    }

    public void setShortMessage(String value) {
        this.shortMessage = value;
    }
}
