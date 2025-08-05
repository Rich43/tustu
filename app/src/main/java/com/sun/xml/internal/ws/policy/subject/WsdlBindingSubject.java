package com.sun.xml.internal.ws.policy.subject;

import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/subject/WsdlBindingSubject.class */
public class WsdlBindingSubject {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) WsdlBindingSubject.class);
    private final QName name;
    private final WsdlMessageType messageType;
    private final WsdlNameScope nameScope;
    private final WsdlBindingSubject parent;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/subject/WsdlBindingSubject$WsdlMessageType.class */
    public enum WsdlMessageType {
        NO_MESSAGE,
        INPUT,
        OUTPUT,
        FAULT
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/subject/WsdlBindingSubject$WsdlNameScope.class */
    public enum WsdlNameScope {
        SERVICE,
        ENDPOINT,
        OPERATION,
        MESSAGE
    }

    WsdlBindingSubject(QName name, WsdlNameScope scope, WsdlBindingSubject parent) {
        this(name, WsdlMessageType.NO_MESSAGE, scope, parent);
    }

    WsdlBindingSubject(QName name, WsdlMessageType messageType, WsdlNameScope scope, WsdlBindingSubject parent) {
        this.name = name;
        this.messageType = messageType;
        this.nameScope = scope;
        this.parent = parent;
    }

    public static WsdlBindingSubject createBindingSubject(QName bindingName) {
        return new WsdlBindingSubject(bindingName, WsdlNameScope.ENDPOINT, null);
    }

    public static WsdlBindingSubject createBindingOperationSubject(QName bindingName, QName operationName) {
        WsdlBindingSubject bindingSubject = createBindingSubject(bindingName);
        return new WsdlBindingSubject(operationName, WsdlNameScope.OPERATION, bindingSubject);
    }

    public static WsdlBindingSubject createBindingMessageSubject(QName bindingName, QName operationName, QName messageName, WsdlMessageType messageType) {
        if (messageType == null) {
            throw ((IllegalArgumentException) LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0083_MESSAGE_TYPE_NULL())));
        }
        if (messageType == WsdlMessageType.NO_MESSAGE) {
            throw ((IllegalArgumentException) LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0084_MESSAGE_TYPE_NO_MESSAGE())));
        }
        if (messageType == WsdlMessageType.FAULT && messageName == null) {
            throw ((IllegalArgumentException) LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0085_MESSAGE_FAULT_NO_NAME())));
        }
        WsdlBindingSubject operationSubject = createBindingOperationSubject(bindingName, operationName);
        return new WsdlBindingSubject(messageName, messageType, WsdlNameScope.MESSAGE, operationSubject);
    }

    public QName getName() {
        return this.name;
    }

    public WsdlMessageType getMessageType() {
        return this.messageType;
    }

    public WsdlBindingSubject getParent() {
        return this.parent;
    }

    public boolean isBindingSubject() {
        return this.nameScope == WsdlNameScope.ENDPOINT && this.parent == null;
    }

    public boolean isBindingOperationSubject() {
        if (this.nameScope == WsdlNameScope.OPERATION && this.parent != null) {
            return this.parent.isBindingSubject();
        }
        return false;
    }

    public boolean isBindingMessageSubject() {
        if (this.nameScope == WsdlNameScope.MESSAGE && this.parent != null) {
            return this.parent.isBindingOperationSubject();
        }
        return false;
    }

    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || !(that instanceof WsdlBindingSubject)) {
            return false;
        }
        WsdlBindingSubject thatSubject = (WsdlBindingSubject) that;
        boolean isEqual = 1 != 0 && (this.name != null ? this.name.equals(thatSubject.name) : thatSubject.name == null);
        boolean isEqual2 = isEqual && this.messageType.equals(thatSubject.messageType);
        boolean isEqual3 = isEqual2 && this.nameScope.equals(thatSubject.nameScope);
        boolean isEqual4 = isEqual3 && (this.parent != null ? this.parent.equals(thatSubject.parent) : thatSubject.parent == null);
        return isEqual4;
    }

    public int hashCode() {
        int result = (31 * 23) + (this.name == null ? 0 : this.name.hashCode());
        return (31 * ((31 * ((31 * result) + this.messageType.hashCode())) + this.nameScope.hashCode())) + (this.parent == null ? 0 : this.parent.hashCode());
    }

    public String toString() {
        StringBuilder result = new StringBuilder("WsdlBindingSubject[");
        result.append((Object) this.name).append(", ").append((Object) this.messageType);
        result.append(", ").append((Object) this.nameScope).append(", ").append((Object) this.parent);
        return result.append("]").toString();
    }
}
