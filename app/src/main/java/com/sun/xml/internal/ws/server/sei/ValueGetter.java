package com.sun.xml.internal.ws.server.sei;

import com.sun.xml.internal.ws.model.ParameterImpl;
import javax.jws.WebParam;
import javax.xml.ws.Holder;

/* loaded from: rt.jar:com/sun/xml/internal/ws/server/sei/ValueGetter.class */
public enum ValueGetter {
    PLAIN { // from class: com.sun.xml.internal.ws.server.sei.ValueGetter.1
        @Override // com.sun.xml.internal.ws.server.sei.ValueGetter
        public Object get(Object parameter) {
            return parameter;
        }
    },
    HOLDER { // from class: com.sun.xml.internal.ws.server.sei.ValueGetter.2
        @Override // com.sun.xml.internal.ws.server.sei.ValueGetter
        public Object get(Object parameter) {
            if (parameter == null) {
                return null;
            }
            return ((Holder) parameter).value;
        }
    };

    public abstract Object get(Object obj);

    public static ValueGetter get(ParameterImpl p2) {
        if (p2.getMode() == WebParam.Mode.IN || p2.getIndex() == -1) {
            return PLAIN;
        }
        return HOLDER;
    }
}
