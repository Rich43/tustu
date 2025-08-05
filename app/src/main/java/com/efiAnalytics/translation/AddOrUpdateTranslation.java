package com.efianalytics.translation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "addOrUpdateTranslation", propOrder = {"userId", "password", "appId", "uid", "regKey", "source", "status", "baseLangCode", "targetLangCode", "baseText", "translatedText"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/AddOrUpdateTranslation.class */
public class AddOrUpdateTranslation {
    protected String userId;
    protected String password;
    protected String appId;
    protected String uid;
    protected String regKey;
    protected String source;
    protected String status;
    protected String baseLangCode;
    protected String targetLangCode;
    protected String baseText;
    protected String translatedText;

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

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String value) {
        this.appId = value;
    }

    public String getUid() {
        return this.uid;
    }

    public void setUid(String value) {
        this.uid = value;
    }

    public String getRegKey() {
        return this.regKey;
    }

    public void setRegKey(String value) {
        this.regKey = value;
    }

    public String getSource() {
        return this.source;
    }

    public void setSource(String value) {
        this.source = value;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String value) {
        this.status = value;
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

    public String getTranslatedText() {
        return this.translatedText;
    }

    public void setTranslatedText(String value) {
        this.translatedText = value;
    }
}
