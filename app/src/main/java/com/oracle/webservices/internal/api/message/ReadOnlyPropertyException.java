package com.oracle.webservices.internal.api.message;

/* loaded from: rt.jar:com/oracle/webservices/internal/api/message/ReadOnlyPropertyException.class */
public class ReadOnlyPropertyException extends IllegalArgumentException {
    private final String propertyName;

    public ReadOnlyPropertyException(String propertyName) {
        super(propertyName + " is a read-only property.");
        this.propertyName = propertyName;
    }

    public String getPropertyName() {
        return this.propertyName;
    }
}
