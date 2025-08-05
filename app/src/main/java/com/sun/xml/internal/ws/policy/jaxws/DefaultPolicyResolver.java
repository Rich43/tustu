package com.sun.xml.internal.ws.policy.jaxws;

import com.sun.xml.internal.ws.api.policy.AlternativeSelector;
import com.sun.xml.internal.ws.api.policy.PolicyResolver;
import com.sun.xml.internal.ws.api.policy.ValidationProcessor;
import com.sun.xml.internal.ws.policy.AssertionSet;
import com.sun.xml.internal.ws.policy.EffectivePolicyModifier;
import com.sun.xml.internal.ws.policy.Policy;
import com.sun.xml.internal.ws.policy.PolicyAssertion;
import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator;
import com.sun.xml.internal.ws.resources.PolicyMessages;
import java.util.Iterator;
import javax.xml.ws.WebServiceException;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/jaxws/DefaultPolicyResolver.class */
public class DefaultPolicyResolver implements PolicyResolver {
    @Override // com.sun.xml.internal.ws.api.policy.PolicyResolver
    public PolicyMap resolve(PolicyResolver.ServerContext context) throws PolicyException {
        PolicyMap map = context.getPolicyMap();
        if (map != null) {
            validateServerPolicyMap(map);
        }
        return map;
    }

    @Override // com.sun.xml.internal.ws.api.policy.PolicyResolver
    public PolicyMap resolve(PolicyResolver.ClientContext context) {
        PolicyMap map = context.getPolicyMap();
        if (map != null) {
            map = doAlternativeSelection(map);
        }
        return map;
    }

    private void validateServerPolicyMap(PolicyMap policyMap) throws PolicyException {
        try {
            ValidationProcessor validationProcessor = ValidationProcessor.getInstance();
            Iterator<Policy> it = policyMap.iterator();
            while (it.hasNext()) {
                Policy policy = it.next();
                Iterator<AssertionSet> it2 = policy.iterator();
                while (it2.hasNext()) {
                    AssertionSet assertionSet = it2.next();
                    Iterator<PolicyAssertion> it3 = assertionSet.iterator();
                    while (it3.hasNext()) {
                        PolicyAssertion assertion = it3.next();
                        PolicyAssertionValidator.Fitness validationResult = validationProcessor.validateServerSide(assertion);
                        if (validationResult != PolicyAssertionValidator.Fitness.SUPPORTED) {
                            throw new PolicyException(PolicyMessages.WSP_1015_SERVER_SIDE_ASSERTION_VALIDATION_FAILED(assertion.getName(), validationResult));
                        }
                    }
                }
            }
        } catch (PolicyException e2) {
            throw new WebServiceException(e2);
        }
    }

    private PolicyMap doAlternativeSelection(PolicyMap policyMap) {
        EffectivePolicyModifier modifier = EffectivePolicyModifier.createEffectivePolicyModifier();
        modifier.connect(policyMap);
        try {
            AlternativeSelector.doSelection(modifier);
            return policyMap;
        } catch (PolicyException e2) {
            throw new WebServiceException(e2);
        }
    }
}
