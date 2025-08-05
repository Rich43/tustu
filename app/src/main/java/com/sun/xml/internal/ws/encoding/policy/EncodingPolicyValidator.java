package com.sun.xml.internal.ws.encoding.policy;

import com.sun.xml.internal.ws.policy.PolicyAssertion;
import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator;
import java.util.ArrayList;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/encoding/policy/EncodingPolicyValidator.class */
public class EncodingPolicyValidator implements PolicyAssertionValidator {
    private static final ArrayList<QName> serverSideSupportedAssertions = new ArrayList<>(3);
    private static final ArrayList<QName> clientSideSupportedAssertions = new ArrayList<>(4);

    static {
        serverSideSupportedAssertions.add(EncodingConstants.OPTIMIZED_MIME_SERIALIZATION_ASSERTION);
        serverSideSupportedAssertions.add(EncodingConstants.UTF816FFFE_CHARACTER_ENCODING_ASSERTION);
        serverSideSupportedAssertions.add(EncodingConstants.OPTIMIZED_FI_SERIALIZATION_ASSERTION);
        clientSideSupportedAssertions.add(EncodingConstants.SELECT_OPTIMAL_ENCODING_ASSERTION);
        clientSideSupportedAssertions.addAll(serverSideSupportedAssertions);
    }

    @Override // com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator
    public PolicyAssertionValidator.Fitness validateClientSide(PolicyAssertion assertion) {
        return clientSideSupportedAssertions.contains(assertion.getName()) ? PolicyAssertionValidator.Fitness.SUPPORTED : PolicyAssertionValidator.Fitness.UNKNOWN;
    }

    @Override // com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator
    public PolicyAssertionValidator.Fitness validateServerSide(PolicyAssertion assertion) {
        QName assertionName = assertion.getName();
        if (serverSideSupportedAssertions.contains(assertionName)) {
            return PolicyAssertionValidator.Fitness.SUPPORTED;
        }
        if (clientSideSupportedAssertions.contains(assertionName)) {
            return PolicyAssertionValidator.Fitness.UNSUPPORTED;
        }
        return PolicyAssertionValidator.Fitness.UNKNOWN;
    }

    @Override // com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator
    public String[] declareSupportedDomains() {
        return new String[]{EncodingConstants.OPTIMIZED_MIME_NS, EncodingConstants.ENCODING_NS, EncodingConstants.SUN_ENCODING_CLIENT_NS, EncodingConstants.SUN_FI_SERVICE_NS};
    }
}
