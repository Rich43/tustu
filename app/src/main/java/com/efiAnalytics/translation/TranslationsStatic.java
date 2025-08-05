package com.efianalytics.translation;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.datatype.XMLGregorianCalendar;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "translationsStatic", propOrder = {"appId", SchemaSymbols.ATTVAL_DATETIME, "translatedText", "translationSource", "translationsStaticPK"})
/* loaded from: efiaServicesClient.jar:com/efianalytics/translation/TranslationsStatic.class */
public class TranslationsStatic {
    protected String appId;

    @XmlSchemaType(name = SchemaSymbols.ATTVAL_DATETIME)
    protected XMLGregorianCalendar dateTime;
    protected String translatedText;
    protected String translationSource;
    protected TranslationsStaticPK translationsStaticPK;

    public String getAppId() {
        return this.appId;
    }

    public void setAppId(String value) {
        this.appId = value;
    }

    public XMLGregorianCalendar getDateTime() {
        return this.dateTime;
    }

    public void setDateTime(XMLGregorianCalendar value) {
        this.dateTime = value;
    }

    public String getTranslatedText() {
        return this.translatedText;
    }

    public void setTranslatedText(String value) {
        this.translatedText = value;
    }

    public String getTranslationSource() {
        return this.translationSource;
    }

    public void setTranslationSource(String value) {
        this.translationSource = value;
    }

    public TranslationsStaticPK getTranslationsStaticPK() {
        return this.translationsStaticPK;
    }

    public void setTranslationsStaticPK(TranslationsStaticPK value) {
        this.translationsStaticPK = value;
    }
}
