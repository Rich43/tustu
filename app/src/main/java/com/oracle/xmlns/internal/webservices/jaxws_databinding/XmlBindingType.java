package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.BindingType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "binding-type")
@XmlType(name = "")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/XmlBindingType.class */
public class XmlBindingType implements BindingType {

    @XmlAttribute(name = "value")
    protected String value;

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override // javax.xml.ws.BindingType
    public String value() {
        return Util.nullSafe(this.value);
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return BindingType.class;
    }
}
