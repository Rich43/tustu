package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.soap.Addressing;
import javax.xml.ws.soap.AddressingFeature;
import jdk.jfr.Enabled;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "addressing")
@XmlType(name = "")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/XmlAddressing.class */
public class XmlAddressing implements Addressing {

    @XmlAttribute(name = Enabled.NAME)
    protected Boolean enabled;

    @XmlAttribute(name = SchemaSymbols.ATTVAL_REQUIRED)
    protected Boolean required;

    public Boolean getEnabled() {
        return Boolean.valueOf(enabled());
    }

    public void setEnabled(Boolean enabled) {
        this.enabled = enabled;
    }

    public Boolean getRequired() {
        return Boolean.valueOf(required());
    }

    public void setRequired(Boolean required) {
        this.required = required;
    }

    @Override // javax.xml.ws.soap.Addressing
    public boolean enabled() {
        return ((Boolean) Util.nullSafe((boolean) this.enabled, true)).booleanValue();
    }

    @Override // javax.xml.ws.soap.Addressing
    public boolean required() {
        return ((Boolean) Util.nullSafe((boolean) this.required, false)).booleanValue();
    }

    @Override // javax.xml.ws.soap.Addressing
    public AddressingFeature.Responses responses() {
        return AddressingFeature.Responses.ALL;
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return Addressing.class;
    }
}
