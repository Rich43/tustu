package com.sun.xml.internal.ws.runtime.config;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import javax.xml.bind.annotation.XmlAccessType;
import javax.xml.bind.annotation.XmlAccessorType;
import javax.xml.bind.annotation.XmlAnyAttribute;
import javax.xml.bind.annotation.XmlAnyElement;
import javax.xml.bind.annotation.XmlElement;
import javax.xml.bind.annotation.XmlSchemaType;
import javax.xml.bind.annotation.XmlType;
import javax.xml.namespace.QName;

@XmlAccessorType(XmlAccessType.FIELD)
@XmlType(name = "tubelineMappingCType", propOrder = {"endpointRef", "tubelineRef", "any"})
/* loaded from: rt.jar:com/sun/xml/internal/ws/runtime/config/TubelineMapping.class */
public class TubelineMapping {

    @XmlSchemaType(name = SchemaSymbols.ATTVAL_ANYURI)
    @XmlElement(name = "endpoint-ref", required = true)
    protected String endpointRef;

    @XmlSchemaType(name = SchemaSymbols.ATTVAL_ANYURI)
    @XmlElement(name = "tubeline-ref", required = true)
    protected String tubelineRef;

    @XmlAnyElement(lax = true)
    protected List<Object> any;

    @XmlAnyAttribute
    private Map<QName, String> otherAttributes = new HashMap();

    public String getEndpointRef() {
        return this.endpointRef;
    }

    public void setEndpointRef(String value) {
        this.endpointRef = value;
    }

    public String getTubelineRef() {
        return this.tubelineRef;
    }

    public void setTubelineRef(String value) {
        this.tubelineRef = value;
    }

    public List<Object> getAny() {
        if (this.any == null) {
            this.any = new ArrayList();
        }
        return this.any;
    }

    public Map<QName, String> getOtherAttributes() {
        return this.otherAttributes;
    }
}
