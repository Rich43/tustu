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
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/jaxws/BuilderHandlerEndpointScope.class */
final class BuilderHandlerEndpointScope extends BuilderHandler {
    private final QName service;
    private final QName port;

    BuilderHandlerEndpointScope(Collection<String> policyURIs, Map<String, PolicySourceModel> policyStore, Object policySubject, QName service, QName port) {
        super(policyURIs, policyStore, policySubject);
        this.service = service;
        this.port = port;
    }

    @Override // com.sun.xml.internal.ws.policy.jaxws.BuilderHandler
    protected void doPopulate(PolicyMapExtender policyMapExtender) throws IllegalArgumentException, PolicyException {
        PolicyMapKey mapKey = PolicyMap.createWsdlEndpointScopeKey(this.service, this.port);
        for (PolicySubject subject : getPolicySubjects()) {
            policyMapExtender.putEndpointSubject(mapKey, subject);
        }
    }

    public String toString() {
        return new StringBuffer(this.service.toString()).append(CallSiteDescriptor.TOKEN_DELIMITER).append(this.port.toString()).toString();
    }
}
