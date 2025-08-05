package com.efianalytics.translation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "translationStaticProposed", propOrder = {"appId", "status", "translatedText", "translationStaticProposedPK"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/TranslationStaticProposed.class */
public class TranslationStaticProposed {
    protected String appId;
    protected String status;
    protected String translatedText;
    protected TranslationStaticProposedPK translationStaticProposedPK;

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String value) {
        this.appId = value;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public String getTranslatedText() {
        return this.translatedText;
    }

    public void setTranslatedText(String value) {
        this.translatedText = value;
    }

    public TranslationStaticProposedPK getTranslationStaticProposedPK() {
        return this.translationStaticProposedPK;
    }

    public void setTranslationStaticProposedPK(TranslationStaticProposedPK value) {
        this.translationStaticProposedPK = value;
    }
}
