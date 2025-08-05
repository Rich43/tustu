package com.sun.xml.internal.ws.policy.sourcemodel;

import com.sun.xml.internal.ws.policy.AssertionSet;
import com.sun.xml.internal.ws.policy.PolicyAssertion;
import com.sun.xml.internal.ws.policy.spi.AssertionCreationException;
import com.sun.xml.internal.ws.policy.spi.PolicyAssertionCreator;
import java.util.Collection;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/DefaultPolicyAssertionCreator.class */
class DefaultPolicyAssertionCreator implements PolicyAssertionCreator {

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/DefaultPolicyAssertionCreator$DefaultPolicyAssertion.class */
    private static final class DefaultPolicyAssertion extends PolicyAssertion {
        DefaultPolicyAssertion(AssertionData data, Collection<PolicyAssertion> assertionParameters, AssertionSet nestedAlternative) {
            super(data, assertionParameters, nestedAlternative);
        }
    }

    DefaultPolicyAssertionCreator() {
    }

    @Override // com.sun.xml.internal.ws.policy.spi.PolicyAssertionCreator
    public String[] getSupportedDomainNamespaceURIs() {
        return null;
    }

    @Override // com.sun.xml.internal.ws.policy.spi.PolicyAssertionCreator
    public PolicyAssertion createAssertion(AssertionData data, Collection<PolicyAssertion> assertionParameters, AssertionSet nestedAlternative, PolicyAssertionCreator defaultCreator) throws AssertionCreationException {
        return new DefaultPolicyAssertion(data, assertionParameters, nestedAlternative);
    }
}
