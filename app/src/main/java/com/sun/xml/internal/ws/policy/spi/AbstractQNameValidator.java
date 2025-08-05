package com.sun.xml.internal.ws.policy.spi;

import com.sun.xml.internal.ws.policy.PolicyAssertion;
import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator;
import java.util.Collection;
import java.util.HashSet;
import java.util.Set;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/spi/AbstractQNameValidator.class */
public abstract class AbstractQNameValidator implements PolicyAssertionValidator {
    private final Set<String> supportedDomains = new HashSet();
    private final Collection<QName> serverAssertions;
    private final Collection<QName> clientAssertions;

    protected AbstractQNameValidator(Collection<QName> serverSideAssertions, Collection<QName> clientSideAssertions) {
        if (serverSideAssertions != null) {
            this.serverAssertions = new HashSet(serverSideAssertions);
            for (QName assertion : this.serverAssertions) {
                this.supportedDomains.add(assertion.getNamespaceURI());
            }
        } else {
            this.serverAssertions = new HashSet(0);
        }
        if (clientSideAssertions != null) {
            this.clientAssertions = new HashSet(clientSideAssertions);
            for (QName assertion2 : this.clientAssertions) {
                this.supportedDomains.add(assertion2.getNamespaceURI());
            }
            return;
        }
        this.clientAssertions = new HashSet(0);
    }

    @Override // com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator
    public String[] declareSupportedDomains() {
        return (String[]) this.supportedDomains.toArray(new String[this.supportedDomains.size()]);
    }

    @Override // com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator
    public PolicyAssertionValidator.Fitness validateClientSide(PolicyAssertion assertion) {
        return validateAssertion(assertion, this.clientAssertions, this.serverAssertions);
    }

    @Override // com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator
    public PolicyAssertionValidator.Fitness validateServerSide(PolicyAssertion assertion) {
        return validateAssertion(assertion, this.serverAssertions, this.clientAssertions);
    }

    private PolicyAssertionValidator.Fitness validateAssertion(PolicyAssertion assertion, Collection<QName> thisSideAssertions, Collection<QName> otherSideAssertions) {
        QName assertionName = assertion.getName();
        if (thisSideAssertions.contains(assertionName)) {
            return PolicyAssertionValidator.Fitness.SUPPORTED;
        }
        if (otherSideAssertions.contains(assertionName)) {
            return PolicyAssertionValidator.Fitness.UNSUPPORTED;
        }
        return PolicyAssertionValidator.Fitness.UNKNOWN;
    }
}
