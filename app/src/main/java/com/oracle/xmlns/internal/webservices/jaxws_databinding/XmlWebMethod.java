package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import java.lang.annotation.Annotation;
import javax.jws.WebMethod;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "web-method")
@XmlType(name = "")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/XmlWebMethod.class */
public class XmlWebMethod implements WebMethod {

    @XmlAttribute(name = "action")
    protected String action;

    @XmlAttribute(name = "exclude")
    protected Boolean exclude;

    @XmlAttribute(name = "operation-name")
    protected String operationName;

    public String getAction() {
        if (this.action == null) {
            return "";
        }
        return this.action;
    }

    public void setAction(String value) {
        this.action = value;
    }

    public boolean isExclude() {
        if (this.exclude == null) {
            return false;
        }
        return this.exclude.booleanValue();
    }

    public void setExclude(Boolean value) {
        this.exclude = value;
    }

    public String getOperationName() {
        if (this.operationName == null) {
            return "";
        }
        return this.operationName;
    }

    public void setOperationName(String value) {
        this.operationName = value;
    }

    @Override // javax.jws.WebMethod
    public String operationName() {
        return Util.nullSafe(this.operationName);
    }

    @Override // javax.jws.WebMethod
    public String action() {
        return Util.nullSafe(this.action);
    }

    @Override // javax.jws.WebMethod
    public boolean exclude() {
        return ((Boolean) Util.nullSafe((boolean) this.exclude, false)).booleanValue();
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return WebMethod.class;
    }
}
