package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "soap-binding-use")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/SoapBindingUse.class */
public enum SoapBindingUse {
    LITERAL,
    ENCODED;

    public String value() {
        return name();
    }

    public static SoapBindingUse fromValue(String v2) {
        return valueOf(v2);
    }
}
