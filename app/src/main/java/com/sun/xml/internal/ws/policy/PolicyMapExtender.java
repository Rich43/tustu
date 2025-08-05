package com.sun.xml.internal.ws.policy;

import com.sun.xml.internal.ws.policy.PolicyMap;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/PolicyMapExtender.class */
public final class PolicyMapExtender extends PolicyMapMutator {
    private PolicyMapExtender() {
    }

    public static PolicyMapExtender createPolicyMapExtender() {
        return new PolicyMapExtender();
    }

    public void putServiceSubject(PolicyMapKey key, PolicySubject subject) {
        getMap().putSubject(PolicyMap.ScopeType.SERVICE, key, subject);
    }

    public void putEndpointSubject(PolicyMapKey key, PolicySubject subject) {
        getMap().putSubject(PolicyMap.ScopeType.ENDPOINT, key, subject);
    }

    public void putOperationSubject(PolicyMapKey key, PolicySubject subject) {
        getMap().putSubject(PolicyMap.ScopeType.OPERATION, key, subject);
    }

    public void putInputMessageSubject(PolicyMapKey key, PolicySubject subject) {
        getMap().putSubject(PolicyMap.ScopeType.INPUT_MESSAGE, key, subject);
    }

    public void putOutputMessageSubject(PolicyMapKey key, PolicySubject subject) {
        getMap().putSubject(PolicyMap.ScopeType.OUTPUT_MESSAGE, key, subject);
    }

    public void putFaultMessageSubject(PolicyMapKey key, PolicySubject subject) {
        getMap().putSubject(PolicyMap.ScopeType.FAULT_MESSAGE, key, subject);
    }
}
