package com.sun.xml.internal.ws.api.policy.subject;

import com.sun.istack.internal.logging.Logger;
import com.sun.xml.internal.ws.resources.BindingApiMessages;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/policy/subject/BindingSubject.class */
public class BindingSubject {
    private static final Logger LOGGER = Logger.getLogger(BindingSubject.class);
    private final QName name;
    private final WsdlMessageType messageType;
    private final WsdlNameScope nameScope;
    private final BindingSubject parent;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/policy/subject/BindingSubject$WsdlMessageType.class */
    private enum WsdlMessageType {
        NO_MESSAGE,
        INPUT,
        OUTPUT,
        FAULT
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/policy/subject/BindingSubject$WsdlNameScope.class */
    private enum WsdlNameScope {
        SERVICE,
        ENDPOINT,
        OPERATION,
        MESSAGE
    }

    BindingSubject(QName name, WsdlNameScope scope, BindingSubject parent) {
        this(name, WsdlMessageType.NO_MESSAGE, scope, parent);
    }

    BindingSubject(QName name, WsdlMessageType messageType, WsdlNameScope scope, BindingSubject parent) {
        this.name = name;
        this.messageType = messageType;
        this.nameScope = scope;
        this.parent = parent;
    }

    public static BindingSubject createBindingSubject(QName bindingName) {
        return new BindingSubject(bindingName, WsdlNameScope.ENDPOINT, null);
    }

    public static BindingSubject createOperationSubject(QName bindingName, QName operationName) {
        BindingSubject bindingSubject = createBindingSubject(bindingName);
        return new BindingSubject(operationName, WsdlNameScope.OPERATION, bindingSubject);
    }

    public static BindingSubject createInputMessageSubject(QName bindingName, QName operationName, QName messageName) {
        BindingSubject operationSubject = createOperationSubject(bindingName, operationName);
        return new BindingSubject(messageName, WsdlMessageType.INPUT, WsdlNameScope.MESSAGE, operationSubject);
    }

    public static BindingSubject createOutputMessageSubject(QName bindingName, QName operationName, QName messageName) {
        BindingSubject operationSubject = createOperationSubject(bindingName, operationName);
        return new BindingSubject(messageName, WsdlMessageType.OUTPUT, WsdlNameScope.MESSAGE, operationSubject);
    }

    public static BindingSubject createFaultMessageSubject(QName bindingName, QName operationName, QName messageName) {
        if (messageName == null) {
            throw ((IllegalArgumentException) LOGGER.logSevereException(new IllegalArgumentException(BindingApiMessages.BINDING_API_NO_FAULT_MESSAGE_NAME())));
        }
        BindingSubject operationSubject = createOperationSubject(bindingName, operationName);
        return new BindingSubject(messageName, WsdlMessageType.FAULT, WsdlNameScope.MESSAGE, operationSubject);
    }

    public QName getName() {
        return this.name;
    }

    public BindingSubject getParent() {
        return this.parent;
    }

    public boolean isBindingSubject() {
        return this.nameScope == WsdlNameScope.ENDPOINT && this.parent == null;
    }

    public boolean isOperationSubject() {
        if (this.nameScope == WsdlNameScope.OPERATION && this.parent != null) {
            return this.parent.isBindingSubject();
        }
        return false;
    }

    public boolean isMessageSubject() {
        if (this.nameScope == WsdlNameScope.MESSAGE && this.parent != null) {
            return this.parent.isOperationSubject();
        }
        return false;
    }

    public boolean isInputMessageSubject() {
        return isMessageSubject() && this.messageType == WsdlMessageType.INPUT;
    }

    public boolean isOutputMessageSubject() {
        return isMessageSubject() && this.messageType == WsdlMessageType.OUTPUT;
    }

    public boolean isFaultMessageSubject() {
        return isMessageSubject() && this.messageType == WsdlMessageType.FAULT;
    }

    public boolean equals(Object that) {
        if (this == that) {
            return true;
        }
        if (that == null || !(that instanceof BindingSubject)) {
            return false;
        }
        BindingSubject thatSubject = (BindingSubject) that;
        boolean isEqual = 1 != 0 && (this.name != null ? this.name.equals(thatSubject.name) : thatSubject.name == null);
        boolean isEqual2 = isEqual && this.messageType.equals(thatSubject.messageType);
        boolean isEqual3 = isEqual2 && this.nameScope.equals(thatSubject.nameScope);
        boolean isEqual4 = isEqual3 && (this.parent != null ? this.parent.equals(thatSubject.parent) : thatSubject.parent == null);
        return isEqual4;
    }

    public int hashCode() {
        int result = (29 * 23) + (this.name == null ? 0 : this.name.hashCode());
        return (29 * ((29 * ((29 * result) + this.messageType.hashCode())) + this.nameScope.hashCode())) + (this.parent == null ? 0 : this.parent.hashCode());
    }

    public String toString() {
        StringBuilder result = new StringBuilder("BindingSubject[");
        result.append((Object) this.name).append(", ").append((Object) this.messageType);
        result.append(", ").append((Object) this.nameScope).append(", ").append((Object) this.parent);
        return result.append("]").toString();
    }
}
