package com.efianalytics.translation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getTranslatedStaticText", propOrder = {"appId", "uid", "regKey", "baseText", "baseLangCode", "targetLangCode"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/GetTranslatedStaticText.class */
public class GetTranslatedStaticText {
    protected String appId;
    protected String uid;
    protected String regKey;
    protected String baseText;
    protected String baseLangCode;
    protected String targetLangCode;

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
