package com.sun.xml.internal.ws.addressing;

import javax.xml.bind.annotation.XmlRootElement;
import javax.xml.bind.annotation.XmlValue;
import javax.xml.namespace.QName;

@XmlRootElement(name = "ProblemHeaderQName", namespace = W3CAddressingConstants.WSA_NAMESPACE_NAME)
/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/ProblemHeaderQName.class */
public class ProblemHeaderQName {

    @XmlValue
    private QName value;

    public ProblemHeaderQName() {
    }

    public ProblemHeaderQName(QName name) {
        this.value = name;
    }
}
