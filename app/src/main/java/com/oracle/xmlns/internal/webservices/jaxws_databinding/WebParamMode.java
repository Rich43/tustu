package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "web-param-mode")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/WebParamMode.class */
public enum WebParamMode {
    IN,
    OUT,
    INOUT;

    public String value() {
        return name();
    }

    public static WebParamMode fromValue(String v2) {
        return valueOf(v2);
    }
}
