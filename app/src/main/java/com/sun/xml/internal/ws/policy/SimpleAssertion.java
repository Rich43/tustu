package com.sun.xml.internal.ws.policy;

import com.sun.xml.internal.ws.policy.sourcemodel.AssertionData;
import java.util.Collection;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/SimpleAssertion.class */
public abstract class SimpleAssertion extends PolicyAssertion {
    protected SimpleAssertion() {
    }

    protected SimpleAssertion(AssertionData data, Collection<? extends PolicyAssertion> assertionParameters) {
        super(data, assertionParameters);
    }

    @Override // com.sun.xml.internal.ws.policy.PolicyAssertion
    public final boolean hasNestedPolicy() {
        return false;
    }

    @Override // com.sun.xml.internal.ws.policy.PolicyAssertion
    public final NestedPolicy getNestedPolicy() {
        return null;
    }
}
