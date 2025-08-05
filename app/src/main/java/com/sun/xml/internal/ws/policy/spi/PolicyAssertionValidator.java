package com.sun.xml.internal.ws.policy.spi;

import com.sun.xml.internal.ws.policy.PolicyAssertion;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/spi/PolicyAssertionValidator.class */
public interface PolicyAssertionValidator {
    Fitness validateClientSide(PolicyAssertion policyAssertion);

    Fitness validateServerSide(PolicyAssertion policyAssertion);

    String[] declareSupportedDomains();

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/spi/PolicyAssertionValidator$Fitness.class */
    public enum Fitness {
        UNKNOWN,
        INVALID,
        UNSUPPORTED,
        SUPPORTED;

        public Fitness combine(Fitness other) {
            if (compareTo(other) < 0) {
                return other;
            }
            return this;
        }
    }
}
