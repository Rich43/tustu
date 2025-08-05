package com.sun.xml.internal.ws.config.management.policy;

import com.sun.xml.internal.ws.api.config.management.policy.ManagedClientAssertion;
import com.sun.xml.internal.ws.api.config.management.policy.ManagedServiceAssertion;
import com.sun.xml.internal.ws.policy.PolicyAssertion;
import com.sun.xml.internal.ws.policy.PolicyConstants;
import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/config/management/policy/ManagementPolicyValidator.class */
public class ManagementPolicyValidator implements PolicyAssertionValidator {
    @Override // com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator
    public PolicyAssertionValidator.Fitness validateClientSide(PolicyAssertion assertion) {
        QName assertionName = assertion.getName();
        if (ManagedClientAssertion.MANAGED_CLIENT_QNAME.equals(assertionName)) {
            return PolicyAssertionValidator.Fitness.SUPPORTED;
        }
        if (ManagedServiceAssertion.MANAGED_SERVICE_QNAME.equals(assertionName)) {
            return PolicyAssertionValidator.Fitness.UNSUPPORTED;
        }
        return PolicyAssertionValidator.Fitness.UNKNOWN;
    }

    @Override // com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator
    public PolicyAssertionValidator.Fitness validateServerSide(PolicyAssertion assertion) {
        QName assertionName = assertion.getName();
        if (ManagedServiceAssertion.MANAGED_SERVICE_QNAME.equals(assertionName)) {
            return PolicyAssertionValidator.Fitness.SUPPORTED;
        }
        if (ManagedClientAssertion.MANAGED_CLIENT_QNAME.equals(assertionName)) {
            return PolicyAssertionValidator.Fitness.UNSUPPORTED;
        }
        return PolicyAssertionValidator.Fitness.UNKNOWN;
    }

    @Override // com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator
    public String[] declareSupportedDomains() {
        return new String[]{PolicyConstants.SUN_MANAGEMENT_NAMESPACE};
    }
}
