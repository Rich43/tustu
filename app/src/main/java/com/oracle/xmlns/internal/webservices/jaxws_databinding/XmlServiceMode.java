package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.Service;
import javax.xml.ws.ServiceMode;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "service-mode")
@XmlType(name = "")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/XmlServiceMode.class */
public class XmlServiceMode implements ServiceMode {

    @XmlAttribute(name = "value")
    protected String value;

    public String getValue() {
        if (this.value == null) {
            return "PAYLOAD";
        }
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override // javax.xml.ws.ServiceMode
    public Service.Mode value() {
        return Service.Mode.valueOf((String) Util.nullSafe(this.value, "PAYLOAD"));
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return ServiceMode.class;
    }
}
