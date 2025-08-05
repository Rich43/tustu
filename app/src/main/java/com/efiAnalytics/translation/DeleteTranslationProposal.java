package com.efianalytics.translation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteTranslationProposal", propOrder = {"userId", "password", "baseText", "baseLangCode", "targetLangCode"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/DeleteTranslationProposal.class */
public class DeleteTranslationProposal {
    protected String userId;
    protected String password;
    protected String baseText;
    protected String baseLangCode;
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

    public String getBaseText() {
        return this.baseText;
    }

    public void setBaseText(String value) {
        this.baseText = value;
    }

    public String getBaseLangCode() {
        return this.baseLangCode;
    }

    public void setBaseLangCode(String value) {
        this.baseLangCode = value;
    }

    public String getTargetLangCode() {
        return this.targetLangCode;
    }

    public void setTargetLangCode(String value) {
        this.targetLangCode = value;
    }
}
