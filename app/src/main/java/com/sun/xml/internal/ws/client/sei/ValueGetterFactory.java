package com.sun.xml.internal.ws.client.sei;

import com.sun.xml.internal.ws.model.ParameterImpl;
import javax.jws.WebParam;

/* loaded from: rt.jar:com/sun/xml/internal/ws/client/sei/ValueGetterFactory.class */
abstract class ValueGetterFactory {
    static final ValueGetterFactory SYNC = new ValueGetterFactory() { // from class: com.sun.xml.internal.ws.client.sei.ValueGetterFactory.1
        @Override // com.sun.xml.internal.ws.client.sei.ValueGetterFactory
        ValueGetter get(ParameterImpl p2) {
            return (p2.getMode() == WebParam.Mode.IN || p2.getIndex() == -1) ? ValueGetter.PLAIN : ValueGetter.HOLDER;
        }
    };
    static final ValueGetterFactory ASYNC = new ValueGetterFactory() { // from class: com.sun.xml.internal.ws.client.sei.ValueGetterFactory.2
        @Override // com.sun.xml.internal.ws.client.sei.ValueGetterFactory
        ValueGetter get(ParameterImpl p2) {
            return ValueGetter.PLAIN;
        }
    };

    abstract ValueGetter get(ParameterImpl parameterImpl);

    ValueGetterFactory() {
    }
}
