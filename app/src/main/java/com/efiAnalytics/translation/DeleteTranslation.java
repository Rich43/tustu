package com.efianalytics.translation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "deleteTranslation", propOrder = {"appId", "userId", "password", "baseLangCode", "targetLangCode", "baseText"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/DeleteTranslation.class */
public class DeleteTranslation {
    protected String appId;
    protected String userId;
    protected String password;
    protected String baseLangCode;
    protected String targetLangCode;
    protected String baseText;

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String value) {
        this.appId = value;
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

    public String getBaseText() {
        return this.baseText;
    }

    public void setBaseText(String value) {
        this.baseText = value;
    }
}
