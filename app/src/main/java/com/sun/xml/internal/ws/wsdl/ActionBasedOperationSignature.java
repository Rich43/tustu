package com.sun.xml.internal.ws.wsdl;

import com.sun.istack.internal.NotNull;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/wsdl/ActionBasedOperationSignature.class */
public class ActionBasedOperationSignature {
    private final String action;
    private final QName payloadQName;

    public ActionBasedOperationSignature(@NotNull String action, @NotNull QName payloadQName) {
        this.action = action;
        this.payloadQName = payloadQName;
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (o2 == null || getClass() != o2.getClass()) {
            return false;
        }
        ActionBasedOperationSignature that = (ActionBasedOperationSignature) o2;
        return this.action.equals(that.action) && this.payloadQName.equals(that.payloadQName);
    }

    public int hashCode() {
        int result = this.action.hashCode();
        return (31 * result) + this.payloadQName.hashCode();
    }
}
