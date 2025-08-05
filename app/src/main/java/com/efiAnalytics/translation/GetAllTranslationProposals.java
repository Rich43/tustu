package com.efianalytics.translation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAllTranslationProposals", propOrder = {"userId", "password", "targetLangCode"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/GetAllTranslationProposals.class */
public class GetAllTranslationProposals {
    protected String userId;
    protected String password;
    protected String targetLangCode;

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

    public String getTargetLangCode() {
        return this.targetLangCode;
    }

    public void setTargetLangCode(String value) {
        this.targetLangCode = value;
    }
}
