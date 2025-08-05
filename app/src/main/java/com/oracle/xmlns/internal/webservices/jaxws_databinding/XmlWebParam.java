package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.annotation.Annotation;
import javax.jws.WebParam;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "web-param")
@XmlType(name = "")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/XmlWebParam.class */
public class XmlWebParam implements WebParam {

    @XmlAttribute(name = "header")
    protected Boolean header;

    @XmlAttribute(name = Constants.ATTRNAME_MODE)
    protected WebParamMode mode;

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

    public WebParamMode getMode() {
        if (this.mode == null) {
            return WebParamMode.IN;
        }
        return this.mode;
    }

    public void setMode(WebParamMode value) {
        this.mode = value;
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

    @Override // javax.jws.WebParam
    public String name() {
        return Util.nullSafe(this.name);
    }

    @Override // javax.jws.WebParam
    public String partName() {
        return Util.nullSafe(this.partName);
    }

    @Override // javax.jws.WebParam
    public String targetNamespace() {
        return Util.nullSafe(this.targetNamespace);
    }

    @Override // javax.jws.WebParam
    public WebParam.Mode mode() {
        return (WebParam.Mode) Util.nullSafe((Enum) this.mode, WebParam.Mode.IN);
    }

    @Override // javax.jws.WebParam
    public boolean header() {
        return ((Boolean) Util.nullSafe((boolean) this.header, false)).booleanValue();
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return WebParam.class;
    }
}
