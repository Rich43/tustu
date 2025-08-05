package com.sun.xml.internal.ws.api.policy;

import com.sun.xml.internal.ws.policy.Policy;
import com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelGenerator;
import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/policy/ModelGenerator.class */
public abstract class ModelGenerator extends PolicyModelGenerator {
    private static final SourceModelCreator CREATOR = new SourceModelCreator();

    private ModelGenerator() {
    }

    public static PolicyModelGenerator getGenerator() {
        return PolicyModelGenerator.getCompactGenerator(CREATOR);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/policy/ModelGenerator$SourceModelCreator.class */
    protected static class SourceModelCreator extends PolicyModelGenerator.PolicySourceModelCreator {
        protected SourceModelCreator() {
        }

        @Override // com.sun.xml.internal.ws.policy.sourcemodel.PolicyModelGenerator.PolicySourceModelCreator
        protected PolicySourceModel create(Policy policy) {
            return SourceModel.createPolicySourceModel(policy.getNamespaceVersion(), policy.getId(), policy.getName());
        }
    }
}
