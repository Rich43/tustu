package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "soap-binding-parameter-style")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/SoapBindingParameterStyle.class */
public enum SoapBindingParameterStyle {
    BARE,
    WRAPPED;

    public String value() {
        return name();
    }

    public static SoapBindingParameterStyle fromValue(String v2) {
        return valueOf(v2);
    }
}
