package com.efianalytics.translation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "mutationResult", propOrder = {"longMessage", "returnCode", "shortMessage"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/MutationResult.class */
public class MutationResult {
    protected String longMessage;
    protected int returnCode;
    protected String shortMessage;

    public String getLongMessage() {
        return this.longMessage;
    }

    public void setLongMessage(String value) {
        this.longMessage = value;
    }

    public int getReturnCode() {
        return this.returnCode;
    }

    public void setReturnCode(int value) {
        this.returnCode = value;
    }

    public String getShortMessage() {
        return this.shortMessage;
    }

    public void setShortMessage(String value) {
        this.shortMessage = value;
    }
}
