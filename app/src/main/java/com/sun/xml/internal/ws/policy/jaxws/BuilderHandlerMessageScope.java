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

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/jaxws/BuilderHandlerMessageScope.class */
final class BuilderHandlerMessageScope extends BuilderHandler {
    private final QName service;
    private final QName port;
    private final QName operation;
    private final QName message;
    private final Scope scope;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/jaxws/BuilderHandlerMessageScope$Scope.class */
    enum Scope {
        InputMessageScope,
        OutputMessageScope,
        FaultMessageScope
    }

    BuilderHandlerMessageScope(Collection<String> policyURIs, Map<String, PolicySourceModel> policyStore, Object policySubject, Scope scope, QName service, QName port, QName operation, QName message) {
        super(policyURIs, policyStore, policySubject);
        this.service = service;
        this.port = port;
        this.operation = operation;
        this.scope = scope;
        this.message = message;
    }

    /* JADX WARN: Code restructure failed: missing block: B:54:0x00bf, code lost:
    
        r0 = true;
     */
    /* JADX WARN: Removed duplicated region for block: B:66:0x00ec  */
    /* JADX WARN: Removed duplicated region for block: B:77:0x0115  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean equals(java.lang.Object r4) {
        /*
            Method dump skipped, instructions count: 281
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.ws.policy.jaxws.BuilderHandlerMessageScope.equals(java.lang.Object):boolean");
    }

    public int hashCode() {
        int hashCode = (31 * ((31 * ((31 * 19) + (this.policySubject == null ? 0 : this.policySubject.hashCode()))) + (this.message == null ? 0 : this.message.hashCode()))) + (this.scope == null ? 0 : this.scope.hashCode());
        if (this.scope != Scope.FaultMessageScope) {
            hashCode = (31 * ((31 * ((31 * hashCode) + (this.service == null ? 0 : this.service.hashCode()))) + (this.port == null ? 0 : this.port.hashCode()))) + (this.operation == null ? 0 : this.operation.hashCode());
        }
        return hashCode;
    }

    @Override // com.sun.xml.internal.ws.policy.jaxws.BuilderHandler
    protected void doPopulate(PolicyMapExtender policyMapExtender) throws IllegalArgumentException, PolicyException {
        PolicyMapKey mapKey;
        if (Scope.FaultMessageScope == this.scope) {
            mapKey = PolicyMap.createWsdlFaultMessageScopeKey(this.service, this.port, this.operation, this.message);
        } else {
            mapKey = PolicyMap.createWsdlMessageScopeKey(this.service, this.port, this.operation);
        }
        if (Scope.InputMessageScope == this.scope) {
            for (PolicySubject subject : getPolicySubjects()) {
                policyMapExtender.putInputMessageSubject(mapKey, subject);
            }
            return;
        }
        if (Scope.OutputMessageScope == this.scope) {
            for (PolicySubject subject2 : getPolicySubjects()) {
                policyMapExtender.putOutputMessageSubject(mapKey, subject2);
            }
            return;
        }
        if (Scope.FaultMessageScope == this.scope) {
            for (PolicySubject subject3 : getPolicySubjects()) {
                policyMapExtender.putFaultMessageSubject(mapKey, subject3);
            }
        }
    }
}
