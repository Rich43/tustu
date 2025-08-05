package com.sun.xml.internal.ws.policy.spi;

import com.sun.xml.internal.ws.policy.AssertionSet;
import com.sun.xml.internal.ws.policy.PolicyAssertion;
import com.sun.xml.internal.ws.policy.sourcemodel.AssertionData;
import java.util.Collection;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/spi/PolicyAssertionCreator.class */
public interface PolicyAssertionCreator {
    String[] getSupportedDomainNamespaceURIs();

    PolicyAssertion createAssertion(AssertionData assertionData, Collection<PolicyAssertion> collection, AssertionSet assertionSet, PolicyAssertionCreator policyAssertionCreator) throws AssertionCreationException;
}
