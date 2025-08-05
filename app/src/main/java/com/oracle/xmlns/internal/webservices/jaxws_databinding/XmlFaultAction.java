package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.FaultAction;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "fault-action")
@XmlType(name = "")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/XmlFaultAction.class */
public class XmlFaultAction implements FaultAction {

    @XmlAttribute(name = "className", required = true)
    protected String className;

    @XmlAttribute(name = "value")
    protected String value;

    public String getClassName() {
        return this.className;
    }

    public void setClassName(String value) {
        this.className = value;
    }

    public String getValue() {
        return Util.nullSafe(this.value);
    }

    public void setValue(String value) {
        this.value = value;
    }

    @Override // javax.xml.ws.FaultAction
    public Class<? extends Exception> className() {
        return Util.findClass(this.className);
    }

    @Override // javax.xml.ws.FaultAction
    public String value() {
        return Util.nullSafe(this.value);
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return FaultAction.class;
    }
}
