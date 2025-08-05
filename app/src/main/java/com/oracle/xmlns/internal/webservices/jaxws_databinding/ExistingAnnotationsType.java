package com.oracle.xmlns.internal.webservices.jaxws_databinding;

import javax.xml.bind.annotation.XmlEnum;
import javax.xml.bind.annotation.XmlType;

@XmlEnum
@XmlType(name = "existing-annotations-type")
/* loaded from: rt.jar:com/oracle/xmlns/internal/webservices/jaxws_databinding/ExistingAnnotationsType.class */
public enum ExistingAnnotationsType {
    MERGE("merge"),
    IGNORE("ignore");

    private final String value;

    ExistingAnnotationsType(String v2) {
        this.value = v2;
    }

    public String value() {
        return this.value;
    }

    public static ExistingAnnotationsType fromValue(String v2) {
        for (ExistingAnnotationsType c2 : values()) {
            if (c2.value.equals(v2)) {
                return c2;
            }
        }
        throw new IllegalArgumentException(v2);
    }
}
