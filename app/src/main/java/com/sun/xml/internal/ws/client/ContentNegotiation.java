package com.sun.xml.internal.ws.client;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/ContentNegotiation.class */
public enum ContentNegotiation {
    none,
    pessimistic,
    optimistic;

    public static final String PROPERTY = "com.sun.xml.internal.ws.client.ContentNegotiation";

    public static ContentNegotiation obtainFromSystemProperty() {
        try {
            String value = System.getProperty(PROPERTY);
            if (value == null) {
                return none;
            }
            return valueOf(value);
        } catch (Exception e2) {
            return none;
        }
    }
}
