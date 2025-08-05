package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "soap-binding-style")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/SoapBindingStyle.class */
public enum SoapBindingStyle {
    DOCUMENT,
    RPC;

    public String value() {
        return name();
    }

    public static SoapBindingStyle fromValue(String v2) {
        return valueOf(v2);
    }
}
