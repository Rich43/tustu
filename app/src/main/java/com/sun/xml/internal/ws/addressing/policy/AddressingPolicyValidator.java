package com.sun.xml.internal.ws.addressing.policy;

import com.sun.xml.internal.ws.addressing.W3CAddressingConstants;
import com.sun.xml.internal.ws.addressing.W3CAddressingMetadataConstants;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.policy.NestedPolicy;
import com.sun.xml.internal.ws.policy.PolicyAssertion;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator;
import java.util.ArrayList;
import java.util.Iterator;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/addressing/policy/AddressingPolicyValidator.class */
public class AddressingPolicyValidator implements PolicyAssertionValidator {
    private static final ArrayList<QName> supportedAssertions = new ArrayList<>();
    private static final PolicyLogger LOGGER;

    static {
        supportedAssertions.add(new QName(AddressingVersion.MEMBER.policyNsUri, W3CAddressingConstants.WSAW_USING_ADDRESSING_NAME));
        supportedAssertions.add(W3CAddressingMetadataConstants.WSAM_ADDRESSING_ASSERTION);
        supportedAssertions.add(W3CAddressingMetadataConstants.WSAM_ANONYMOUS_NESTED_ASSERTION);
        supportedAssertions.add(W3CAddressingMetadataConstants.WSAM_NONANONYMOUS_NESTED_ASSERTION);
        LOGGER = PolicyLogger.getLogger((Class<?>) AddressingPolicyValidator.class);
    }

    @Override // com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator
    public PolicyAssertionValidator.Fitness validateClientSide(PolicyAssertion assertion) {
        return supportedAssertions.contains(assertion.getName()) ? PolicyAssertionValidator.Fitness.SUPPORTED : PolicyAssertionValidator.Fitness.UNKNOWN;
    }

    @Override // com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator
    public PolicyAssertionValidator.Fitness validateServerSide(PolicyAssertion assertion) {
        NestedPolicy nestedPolicy;
        if (!supportedAssertions.contains(assertion.getName())) {
            return PolicyAssertionValidator.Fitness.UNKNOWN;
        }
        if (assertion.getName().equals(W3CAddressingMetadataConstants.WSAM_ADDRESSING_ASSERTION) && (nestedPolicy = assertion.getNestedPolicy()) != null) {
            boolean requiresAnonymousResponses = false;
            boolean requiresNonAnonymousResponses = false;
            Iterator<PolicyAssertion> it = nestedPolicy.getAssertionSet().iterator();
            while (it.hasNext()) {
                PolicyAssertion nestedAsser = it.next();
                if (nestedAsser.getName().equals(W3CAddressingMetadataConstants.WSAM_ANONYMOUS_NESTED_ASSERTION)) {
                    requiresAnonymousResponses = true;
                } else if (nestedAsser.getName().equals(W3CAddressingMetadataConstants.WSAM_NONANONYMOUS_NESTED_ASSERTION)) {
                    requiresNonAnonymousResponses = true;
                } else {
                    LOGGER.warning("Found unsupported assertion:\n" + ((Object) nestedAsser) + "\nnested into assertion:\n" + ((Object) assertion));
                    return PolicyAssertionValidator.Fitness.UNSUPPORTED;
                }
            }
            if (requiresAnonymousResponses && requiresNonAnonymousResponses) {
                LOGGER.warning("Only one among AnonymousResponses and NonAnonymousResponses can be nested in an Addressing assertion");
                return PolicyAssertionValidator.Fitness.INVALID;
            }
        }
        return PolicyAssertionValidator.Fitness.SUPPORTED;
    }

    @Override // com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator
    public String[] declareSupportedDomains() {
        return new String[]{AddressingVersion.MEMBER.policyNsUri, AddressingVersion.W3C.policyNsUri, W3CAddressingMetadataConstants.WSAM_NAMESPACE_NAME};
    }
}
