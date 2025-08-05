package com.sun.xml.internal.ws.policy;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/PolicyMapKeyHandler.class */
interface PolicyMapKeyHandler {
    boolean areEqual(PolicyMapKey policyMapKey, PolicyMapKey policyMapKey2);

    int generateHashCode(PolicyMapKey policyMapKey);
}
