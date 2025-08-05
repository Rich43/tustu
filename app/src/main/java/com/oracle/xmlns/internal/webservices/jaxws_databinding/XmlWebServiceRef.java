package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import com.sun.xml.internal.ws.addressing.W3CAddressingMetadataConstants;
import java.lang.annotation.Annotation;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAttribute;
import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlType;
import javax.xml.ws.Service;
import javax.xml.ws.WebServiceRef;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlRootElement(name = "web-service-ref")
@XmlType(name = "")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/XmlWebServiceRef.class */
public class XmlWebServiceRef implements WebServiceRef {

    @XmlAttribute(name = "name")
    protected String name;

    @XmlAttribute(name = "type")
    protected String type;

    @XmlAttribute(name = "mappedName")
    protected String mappedName;

    @XmlAttribute(name = "value")
    protected String value;

    @XmlAttribute(name = W3CAddressingMetadataConstants.WSAM_WSDLI_ATTRIBUTE_LOCALNAME)
    protected String wsdlLocation;

    @XmlAttribute(name = "lookup")
    protected String lookup;

    public String getName() {
        return this.name;
    }

    public void setName(String value) {
        this.name = value;
    }

    public String getType() {
        return this.type;
    }

    public void setType(String value) {
        this.type = value;
    }

    public String getMappedName() {
        return this.mappedName;
    }

    public void setMappedName(String value) {
        this.mappedName = value;
    }

    public String getValue() {
        return this.value;
    }

    public void setValue(String value) {
        this.value = value;
    }

    public String getWsdlLocation() {
        return this.wsdlLocation;
    }

    public void setWsdlLocation(String value) {
        this.wsdlLocation = value;
    }

    public String getLookup() {
        return this.lookup;
    }

    public void setLookup(String lookup) {
        this.lookup = lookup;
    }

    @Override // javax.xml.ws.WebServiceRef
    public String name() {
        return Util.nullSafe(this.name);
    }

    @Override // javax.xml.ws.WebServiceRef
    public Class<?> type() {
        if (this.type == null) {
            return Object.class;
        }
        return Util.findClass(this.type);
    }

    @Override // javax.xml.ws.WebServiceRef
    public String mappedName() {
        return Util.nullSafe(this.mappedName);
    }

    @Override // javax.xml.ws.WebServiceRef
    public Class<? extends Service> value() {
        if (this.value == null) {
            return Service.class;
        }
        return Util.findClass(this.value);
    }

    @Override // javax.xml.ws.WebServiceRef
    public String wsdlLocation() {
        return Util.nullSafe(this.wsdlLocation);
    }

    @Override // javax.xml.ws.WebServiceRef
    public String lookup() {
        return Util.nullSafe(this.lookup);
    }

    @Override // java.lang.annotation.Annotation
    public Class<? extends Annotation> annotationType() {
        return WebServiceRef.class;
    }
}
