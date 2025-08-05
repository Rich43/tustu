package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import com.sun.org.apache.xpath.internal.compiler.Keywords;
import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.ResponseWrapper;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "response-wrapper")
@XmlType(name = "")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/XmlResponseWrapper.class */
public class XmlResponseWrapper implements ResponseWrapper {

    @XmlAttribute(name = Keywords.FUNC_LOCAL_PART_STRING)
    protected String localName;

    @XmlAttribute(name = "target-namespace")
    protected String targetNamespace;

    @XmlAttribute(name = "class-name")
    protected String className;

    @XmlAttribute(name = "part-name")
    protected String partName;

    public String getLocalName() {
        if (this.localName == null) {
            return "";
        }
        return this.localName;
    }

    public void setLocalName(String value) {
        this.localName = value;
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

    public String getClassName() {
        if (this.className == null) {
            return "";
        }
        return this.className;
    }

    public void setClassName(String value) {
        this.className = value;
    }

    public String getPartName() {
        return this.partName;
    }

    public void setPartName(String partName) {
        this.partName = partName;
    }

    @Override // javax.xml.ws.ResponseWrapper
    public String localName() {
        return Util.nullSafe(this.localName);
    }

    @Override // javax.xml.ws.ResponseWrapper
    public String targetNamespace() {
        return Util.nullSafe(this.targetNamespace);
    }

    @Override // javax.xml.ws.ResponseWrapper
    public String className() {
        return Util.nullSafe(this.className);
    }

    @Override // javax.xml.ws.ResponseWrapper
    public String partName() {
        return Util.nullSafe(this.partName);
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return ResponseWrapper.class;
    }
}
