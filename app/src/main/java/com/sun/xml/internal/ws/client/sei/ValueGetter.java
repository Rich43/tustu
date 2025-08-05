package com.sun.xml.internal.ws.client.sei;

import javax.xml.ws.Holder;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ValueGetter.class */
enum ValueGetter {
    PLAIN { // from class: com.sun.xml.internal.ws.client.sei.ValueGetter.1
        @Override // com.sun.xml.internal.ws.client.sei.ValueGetter
        Object get(Object parameter) {
            return parameter;
        }
    },
    HOLDER { // from class: com.sun.xml.internal.ws.client.sei.ValueGetter.2
        @Override // com.sun.xml.internal.ws.client.sei.ValueGetter
        Object get(Object parameter) {
            if (parameter == null) {
                return null;
            }
            return ((Holder) parameter).value;
        }
    };

    abstract Object get(Object obj);
}
