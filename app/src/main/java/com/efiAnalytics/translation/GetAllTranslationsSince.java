package com.efianalytics.translation;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "getAllTranslationsSince", propOrder = {"appId", "uid", "regKey", "targetLangCode", "sinceDate"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/GetAllTranslationsSince.class */
public class GetAllTranslationsSince {
    protected String appId;
    protected String uid;
    protected String regKey;
    protected String targetLangCode;

    @XmlSchemaType(name = SchemaSymbols.ATTVAL_DATETIME)
    protected XMLGregorianCalendar sinceDate;

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

    public String getTargetLangCode() {
        return this.targetLangCode;
    }

    public void setTargetLangCode(String value) {
        this.targetLangCode = value;
    }

    public XMLGregorianCalendar getSinceDate() {
        return this.sinceDate;
    }

    public void setSinceDate(XMLGregorianCalendar value) {
        this.sinceDate = value;
    }
}
