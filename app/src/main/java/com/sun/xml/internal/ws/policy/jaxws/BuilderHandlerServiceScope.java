package com.sun.xml.internal.ws.policy.jaxws;

import com.sun.xml.internal.ws.policy.PolicyException;
import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.policy.PolicyMapExtender;
import com.sun.xml.internal.ws.policy.PolicyMapKey;
import com.sun.xml.internal.ws.policy.PolicySubject;
import com.sun.xml.internal.ws.policy.sourcemodel.PolicySourceModel;
import java.util.Collection;
import java.util.Map;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/jaxws/BuilderHandlerServiceScope.class */
final class BuilderHandlerServiceScope extends BuilderHandler {
    private final QName service;

    BuilderHandlerServiceScope(Collection<String> policyURIs, Map<String, PolicySourceModel> policyStore, Object policySubject, QName service) {
        super(policyURIs, policyStore, policySubject);
        this.service = service;
    }

    @Override // com.sun.xml.internal.ws.policy.jaxws.BuilderHandler
    protected void doPopulate(PolicyMapExtender policyMapExtender) throws IllegalArgumentException, PolicyException {
        PolicyMapKey mapKey = PolicyMap.createWsdlServiceScopeKey(this.service);
        for (PolicySubject subject : getPolicySubjects()) {
            policyMapExtender.putServiceSubject(mapKey, subject);
        }
    }

    public String toString() {
        return this.service.toString();
    }
}
