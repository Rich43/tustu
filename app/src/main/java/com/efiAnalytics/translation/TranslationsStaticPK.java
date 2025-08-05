package com.efianalytics.translation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "translationsStaticPK", propOrder = {"baseLangCode", "baseText", "status", "targetLangCode"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/TranslationsStaticPK.class */
public class TranslationsStaticPK {
    protected String baseLangCode;
    protected String baseText;
    protected String status;
    protected String targetLangCode;

    public String getBaseLangCode() {
        return this.baseLangCode;
    }

    public void setBaseLangCode(String value) {
        this.baseLangCode = value;
    }

    public String getBaseText() {
        return this.baseText;
    }

    public void setBaseText(String value) {
        this.baseText = value;
    }

    public String getStatus() {
        return this.status;
    }

    public void setStatus(String value) {
        this.status = value;
    }

    public String getTargetLangCode() {
        return this.targetLangCode;
    }

    public void setTargetLangCode(String value) {
        this.targetLangCode = value;
    }
}
