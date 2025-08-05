package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import java.lang.annotation.Annotation;
import javax.jws.WebResult;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "web-result")
@XmlType(name = "")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/XmlWebResult.class */
public class XmlWebResult implements WebResult {

    @XmlAttribute(name = "header")
    protected Boolean header;

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "part-name")
    protected String partName;

    @XmlAttribute(name = "target-namespace")
    protected String targetNamespace;

    public boolean isHeader() {
        if (this.header == null) {
            return false;
        }
        return this.header.booleanValue();
    }

    public void setHeader(Boolean value) {
        this.header = value;
    }

    public String getName() {
        if (this.name == null) {
            return "";
        }
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getPartName() {
        if (this.partName == null) {
            return "";
        }
        return this.partName;
    }

    public void setPartName(String value) {
        this.partName = value;
    }

    public String getTargetNamespace() {
        if (this.targetNamespace == null) {
            return "";
        }
        return this.targetNamespace;
    }

    public void setTargetNamespace(String value) {
        this.targetNamespace = value;
    }

    @Override // javax.jws.WebResult
    public String name() {
        return Util.nullSafe(this.name);
    }

    @Override // javax.jws.WebResult
    public String partName() {
        return Util.nullSafe(this.partName);
    }

    @Override // javax.jws.WebResult
    public String targetNamespace() {
        return Util.nullSafe(this.targetNamespace);
    }

    @Override // javax.jws.WebResult
    public boolean header() {
        return ((Boolean) Util.nullSafe((boolean) this.header, false)).booleanValue();
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return WebResult.class;
    }
}
