package com.sun.xml.internal.ws.policy;

import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import com.sun.xml.internal.ws.policy.spi.PolicyAssertionValidator;
import java.util.Collection;
import java.util.LinkedList;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/AssertionValidationProcessor.class */
public class AssertionValidationProcessor {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) AssertionValidationProcessor.class);
    private final Collection<PolicyAssertionValidator> validators;

    private AssertionValidationProcessor() throws PolicyException {
        this(null);
    }

    protected AssertionValidationProcessor(Collection<PolicyAssertionValidator> policyValidators) throws PolicyException {
        this.validators = new LinkedList();
        for (PolicyAssertionValidator validator : (PolicyAssertionValidator[]) PolicyUtils.ServiceProvider.load(PolicyAssertionValidator.class)) {
            this.validators.add(validator);
        }
        if (policyValidators != null) {
            for (PolicyAssertionValidator validator2 : policyValidators) {
                this.validators.add(validator2);
            }
        }
        if (this.validators.size() == 0) {
            throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0076_NO_SERVICE_PROVIDERS_FOUND(PolicyAssertionValidator.class.getName()))));
        }
    }

    public static AssertionValidationProcessor getInstance() throws PolicyException {
        return new AssertionValidationProcessor();
    }

    public PolicyAssertionValidator.Fitness validateClientSide(PolicyAssertion assertion) throws PolicyException {
        PolicyAssertionValidator.Fitness assertionFitness = PolicyAssertionValidator.Fitness.UNKNOWN;
        for (PolicyAssertionValidator validator : this.validators) {
            assertionFitness = assertionFitness.combine(validator.validateClientSide(assertion));
            if (assertionFitness == PolicyAssertionValidator.Fitness.SUPPORTED) {
                break;
            }
        }
        return assertionFitness;
    }

    public PolicyAssertionValidator.Fitness validateServerSide(PolicyAssertion assertion) throws PolicyException {
        PolicyAssertionValidator.Fitness assertionFitness = PolicyAssertionValidator.Fitness.UNKNOWN;
        for (PolicyAssertionValidator validator : this.validators) {
            assertionFitness = assertionFitness.combine(validator.validateServerSide(assertion));
            if (assertionFitness == PolicyAssertionValidator.Fitness.SUPPORTED) {
                break;
            }
        }
        return assertionFitness;
    }
}
