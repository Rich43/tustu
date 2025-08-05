package com.sun.xml.internal.ws.encoding.policy;

import com.sun.xml.internal.ws.api.client.SelectOptimalEncodingFeature;
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
import javax.xml.namespace.QName;
import javax.xml.ws.WebServiceFeature;
import jdk.jfr.Enabled;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/policy/SelectOptimalEncodingFeatureConfigurator.class */
public class SelectOptimalEncodingFeatureConfigurator implements PolicyFeatureConfigurator {
    public static final QName enabled = new QName(Enabled.NAME);

    @Override // com.sun.xml.internal.ws.policy.jaxws.spi.PolicyFeatureConfigurator
    public Collection<WebServiceFeature> getFeatures(PolicyMapKey key, PolicyMap policyMap) throws PolicyException {
        Policy policy;
        Collection<WebServiceFeature> features = new LinkedList<>();
        if (key != null && policyMap != null && null != (policy = policyMap.getEndpointEffectivePolicy(key)) && policy.contains(EncodingConstants.SELECT_OPTIMAL_ENCODING_ASSERTION)) {
            Iterator<AssertionSet> assertions = policy.iterator();
            while (assertions.hasNext()) {
                AssertionSet assertionSet = assertions.next();
                Iterator<PolicyAssertion> policyAssertion = assertionSet.iterator();
                while (policyAssertion.hasNext()) {
                    PolicyAssertion assertion = policyAssertion.next();
                    if (EncodingConstants.SELECT_OPTIMAL_ENCODING_ASSERTION.equals(assertion.getName())) {
                        String value = assertion.getAttributeValue(enabled);
                        boolean isSelectOptimalEncodingEnabled = value == null || Boolean.valueOf(value.trim()).booleanValue();
                        features.add(new SelectOptimalEncodingFeature(isSelectOptimalEncodingEnabled));
                    }
                }
            }
        }
        return features;
    }
}
