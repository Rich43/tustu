package com.efianalytics.translation;

import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "translationStaticProposedPK", propOrder = {"baseLangCode", "baseText", "targetLangCode", "userId"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/TranslationStaticProposedPK.class */
public class TranslationStaticProposedPK {
    protected String baseLangCode;
    protected String baseText;
    protected String targetLangCode;
    protected String userId;

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

    public String getTargetLangCode() {
        return this.targetLangCode;
    }

    public void setTargetLangCode(String value) {
        this.targetLangCode = value;
    }

    public String getUserId() {
        return this.userId;
    }

    public void setUserId(String value) {
        this.userId = value;
    }
}
