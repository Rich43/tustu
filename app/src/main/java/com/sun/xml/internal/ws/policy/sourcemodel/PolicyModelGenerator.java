package com.sun.xml.internal.ws.policy.sourcemodel;

import com.sun.xml.internal.ws.policy.AssertionSet;
import com.sun.xml.internal.ws.policy.NestedPolicy;
import com.sun.xml.internal.ws.policy.Policy;
import com.sun.xml.internal.ws.policy.PolicyAssertion;
import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import java.util.Iterator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/PolicyModelGenerator.class */
public abstract class PolicyModelGenerator {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) PolicyModelGenerator.class);

    public abstract PolicySourceModel translate(Policy policy) throws PolicyException;

    protected abstract ModelNode translate(ModelNode modelNode, NestedPolicy nestedPolicy);

    protected PolicyModelGenerator() {
    }

    public static PolicyModelGenerator getGenerator() {
        return getNormalizedGenerator(new PolicySourceModelCreator());
    }

    protected static PolicyModelGenerator getCompactGenerator(PolicySourceModelCreator creator) {
        return new CompactModelGenerator(creator);
    }

    protected static PolicyModelGenerator getNormalizedGenerator(PolicySourceModelCreator creator) {
        return new NormalizedModelGenerator(creator);
    }

    protected void translate(ModelNode node, AssertionSet assertions) throws UnsupportedOperationException, IllegalArgumentException {
        Iterator<PolicyAssertion> it = assertions.iterator();
        while (it.hasNext()) {
            PolicyAssertion assertion = it.next();
            AssertionData data = AssertionData.createAssertionData(assertion.getName(), assertion.getValue(), assertion.getAttributes(), assertion.isOptional(), assertion.isIgnorable());
            ModelNode assertionNode = node.createChildAssertionNode(data);
            if (assertion.hasNestedPolicy()) {
                translate(assertionNode, assertion.getNestedPolicy());
            }
            if (assertion.hasParameters()) {
                translate(assertionNode, assertion.getParametersIterator());
            }
        }
    }

    protected void translate(ModelNode assertionNode, Iterator<PolicyAssertion> assertionParametersIterator) throws UnsupportedOperationException, IllegalArgumentException {
        while (assertionParametersIterator.hasNext()) {
            PolicyAssertion assertionParameter = assertionParametersIterator.next();
            AssertionData data = AssertionData.createAssertionParameterData(assertionParameter.getName(), assertionParameter.getValue(), assertionParameter.getAttributes());
            ModelNode assertionParameterNode = assertionNode.createChildAssertionParameterNode(data);
            if (assertionParameter.hasNestedPolicy()) {
                throw ((IllegalStateException) LOGGER.logSevereException(new IllegalStateException(LocalizationMessages.WSP_0005_UNEXPECTED_POLICY_ELEMENT_FOUND_IN_ASSERTION_PARAM(assertionParameter))));
            }
            if (assertionParameter.hasNestedAssertions()) {
                translate(assertionParameterNode, assertionParameter.getNestedAssertionsIterator());
            }
        }
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/PolicyModelGenerator$PolicySourceModelCreator.class */
    public static class PolicySourceModelCreator {
        protected PolicySourceModelCreator() {
        }

        protected PolicySourceModel create(Policy policy) {
            return PolicySourceModel.createPolicySourceModel(policy.getNamespaceVersion(), policy.getId(), policy.getName());
        }
    }
}
