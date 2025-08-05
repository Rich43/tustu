package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.WebEndpoint;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "web-endpoint")
@XmlType(name = "")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/XmlWebEndpoint.class */
public class XmlWebEndpoint implements WebEndpoint {

    @XmlAttribute(name = "name")
    protected String name;

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    @Override // javax.xml.ws.WebEndpoint
    public String name() {
        return Util.nullSafe(this.name);
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return WebEndpoint.class;
    }
}
