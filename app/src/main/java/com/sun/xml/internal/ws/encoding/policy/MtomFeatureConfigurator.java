package com.sun.xml.internal.ws.encoding.policy;

import com.sun.xml.internal.ws.policy.AssertionSet;
import com.sun.xml.internal.ws.policy.Policy;
import com.sun.xml.internal.ws.policy.PolicyAssertion;
import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.policy.PolicyMapKey;
import com.sun.xml.internal.ws.policy.jaxws.spi.PolicyFeatureConfigurator;
import java.util.Collection;
import java.util.Iterator;
import java.util.LinkedList;
import javax.xml.ws.WebServiceFeature;
import javax.xml.ws.soap.MTOMFeature;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/policy/MtomFeatureConfigurator.class */
public class MtomFeatureConfigurator implements PolicyFeatureConfigurator {
    @Override // com.sun.xml.internal.ws.policy.jaxws.spi.PolicyFeatureConfigurator
    public Collection<WebServiceFeature> getFeatures(PolicyMapKey key, PolicyMap policyMap) throws PolicyException {
        Policy policy;
        Collection<WebServiceFeature> features = new LinkedList<>();
        if (key != null && policyMap != null && null != (policy = policyMap.getEndpointEffectivePolicy(key)) && policy.contains(EncodingConstants.OPTIMIZED_MIME_SERIALIZATION_ASSERTION)) {
            Iterator<AssertionSet> assertions = policy.iterator();
            while (assertions.hasNext()) {
                AssertionSet assertionSet = assertions.next();
                Iterator<PolicyAssertion> policyAssertion = assertionSet.iterator();
                while (policyAssertion.hasNext()) {
                    PolicyAssertion assertion = policyAssertion.next();
                    if (EncodingConstants.OPTIMIZED_MIME_SERIALIZATION_ASSERTION.equals(assertion.getName())) {
                        features.add(new MTOMFeature(true));
                    }
                }
            }
        }
        return features;
    }
}
