package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.lang.annotation.Annotation;
import javax.jws.HandlerChain;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "handler-chain")
@XmlType(name = "")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/XmlHandlerChain.class */
public class XmlHandlerChain implements HandlerChain {

    @XmlAttribute(name = DeploymentDescriptorParser.ATTR_FILE)
    protected String file;

    public String getFile() {
        return this.file;
    }

    public void setFile(String value) {
        this.file = value;
    }

    @Override // javax.jws.HandlerChain
    public String file() {
        return Util.nullSafe(this.file);
    }

    @Override // javax.jws.HandlerChain
    public String name() {
        return "";
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return HandlerChain.class;
    }
}
