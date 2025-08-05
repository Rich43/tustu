package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import com.sun.xml.internal.ws.wsdl.parser.WSDLConstants;
import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.WebFault;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "web-fault")
@XmlType(name = "")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/XmlWebFault.class */
public class XmlWebFault implements WebFault {

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = WSDLConstants.ATTR_TNS)
    protected String targetNamespace;

    @XmlAttribute(name = "faultBean")
    protected String faultBean;

    @XmlAttribute(name = "messageName")
    protected String messageName;

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getTargetNamespace() {
        return this.targetNamespace;
    }

    public void setTargetNamespace(String value) {
        this.targetNamespace = value;
    }

    public String getFaultBean() {
        return this.faultBean;
    }

    public void setFaultBean(String value) {
        this.faultBean = value;
    }

    @Override // javax.xml.ws.WebFault
    public String name() {
        return Util.nullSafe(this.name);
    }

    @Override // javax.xml.ws.WebFault
    public String targetNamespace() {
        return Util.nullSafe(this.targetNamespace);
    }

    @Override // javax.xml.ws.WebFault
    public String faultBean() {
        return Util.nullSafe(this.faultBean);
    }

    @Override // javax.xml.ws.WebFault
    public String messageName() {
        return Util.nullSafe(this.messageName);
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return WebFault.class;
    }
}
