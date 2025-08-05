package com.sun.xml.internal.ws.policy.sourcemodel;

import com.sun.xml.internal.ws.policy.AssertionSet;
import com.sun.xml.internal.ws.policy.NestedPolicy;
import com.sun.xml.internal.ws.policy.Policy;
import com.sun.xml.internal.ws.policy.PolicyAssertion;
import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelGenerator;
import java.util.Iterator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/sourcemodel/NormalizedModelGenerator.class */
class NormalizedModelGenerator extends PolicyModelGenerator {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) NormalizedModelGenerator.class);
    private final PolicyModelGenerator.PolicySourceModelCreator sourceModelCreator;

    NormalizedModelGenerator(PolicyModelGenerator.PolicySourceModelCreator sourceModelCreator) {
        this.sourceModelCreator = sourceModelCreator;
    }

    @Override // com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelGenerator
    public PolicySourceModel translate(Policy policy) throws UnsupportedOperationException, IllegalArgumentException, PolicyException {
        LOGGER.entering(policy);
        PolicySourceModel model = null;
        if (policy == null) {
            LOGGER.fine(LocalizationMessages.WSP_0047_POLICY_IS_NULL_RETURNING());
        } else {
            model = this.sourceModelCreator.create(policy);
            ModelNode rootNode = model.getRootNode();
            ModelNode exactlyOneNode = rootNode.createChildExactlyOneNode();
            Iterator<AssertionSet> it = policy.iterator();
            while (it.hasNext()) {
                AssertionSet set = it.next();
                ModelNode alternativeNode = exactlyOneNode.createChildAllNode();
                Iterator<PolicyAssertion> it2 = set.iterator();
                while (it2.hasNext()) {
                    PolicyAssertion assertion = it2.next();
                    AssertionData data = AssertionData.createAssertionData(assertion.getName(), assertion.getValue(), assertion.getAttributes(), assertion.isOptional(), assertion.isIgnorable());
                    ModelNode assertionNode = alternativeNode.createChildAssertionNode(data);
                    if (assertion.hasNestedPolicy()) {
                        translate(assertionNode, assertion.getNestedPolicy());
                    }
                    if (assertion.hasParameters()) {
                        translate(assertionNode, assertion.getParametersIterator());
                    }
                }
            }
        }
        LOGGER.exiting(model);
        return model;
    }

    @Override // com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelGenerator
    protected ModelNode translate(ModelNode parentAssertion, NestedPolicy policy) throws UnsupportedOperationException {
        ModelNode nestedPolicyRoot = parentAssertion.createChildPolicyNode();
        ModelNode exactlyOneNode = nestedPolicyRoot.createChildExactlyOneNode();
        AssertionSet set = policy.getAssertionSet();
        ModelNode alternativeNode = exactlyOneNode.createChildAllNode();
        translate(alternativeNode, set);
        return nestedPolicyRoot;
    }
}
