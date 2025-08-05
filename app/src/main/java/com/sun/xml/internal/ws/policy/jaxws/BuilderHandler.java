package com.sun.xml.internal.ws.policy.jaxws;

import com.sun.xml.internal.ws.api.policy.ModelTranslator;
import com.sun.xml.internal.ws.policy.Policy;
import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.PolicyMapExtender;
import com.sun.xml.internal.ws.policy.PolicySubject;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel;
import com.sun.xml.internal.ws.resources.PolicyMessages;
import java.util.ArrayList;
import java.util.Collection;
import java.util.Map;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/jaxws/BuilderHandler.class */
abstract class BuilderHandler {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) BuilderHandler.class);
    Map<String, PolicySourceModel> policyStore;
    Collection<String> policyURIs;
    Object policySubject;

    protected abstract void doPopulate(PolicyMapExtender policyMapExtender) throws PolicyException;

    BuilderHandler(Collection<String> policyURIs, Map<String, PolicySourceModel> policyStore, Object policySubject) {
        this.policyStore = policyStore;
        this.policyURIs = policyURIs;
        this.policySubject = policySubject;
    }

    final void populate(PolicyMapExtender policyMapExtender) throws PolicyException {
        if (null == policyMapExtender) {
            throw ((PolicyException) LOGGER.logSevereException(new PolicyException(PolicyMessages.WSP_1006_POLICY_MAP_EXTENDER_CAN_NOT_BE_NULL())));
        }
        doPopulate(policyMapExtender);
    }

    final Collection<Policy> getPolicies() throws PolicyException {
        if (null == this.policyURIs) {
            throw ((PolicyException) LOGGER.logSevereException(new PolicyException(PolicyMessages.WSP_1004_POLICY_URIS_CAN_NOT_BE_NULL())));
        }
        if (null == this.policyStore) {
            throw ((PolicyException) LOGGER.logSevereException(new PolicyException(PolicyMessages.WSP_1010_NO_POLICIES_DEFINED())));
        }
        Collection<Policy> result = new ArrayList<>(this.policyURIs.size());
        for (String policyURI : this.policyURIs) {
            PolicySourceModel sourceModel = this.policyStore.get(policyURI);
            if (sourceModel == null) {
                throw ((PolicyException) LOGGER.logSevereException(new PolicyException(PolicyMessages.WSP_1005_POLICY_REFERENCE_DOES_NOT_EXIST(policyURI))));
            }
            result.add(ModelTranslator.getTranslator().translate(sourceModel));
        }
        return result;
    }

    final Collection<PolicySubject> getPolicySubjects() throws PolicyException {
        Collection<Policy> policies = getPolicies();
        Collection<PolicySubject> result = new ArrayList<>(policies.size());
        for (Policy policy : policies) {
            result.add(new PolicySubject(this.policySubject, policy));
        }
        return result;
    }
}
