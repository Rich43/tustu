package com.sun.xml.internal.ws.policy;

import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import java.util.ArrayList;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;
import java.util.Set;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/PolicyMap.class */
public final class PolicyMap implements Iterable<Policy> {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) PolicyMap.class);
    private static final PolicyMapKeyHandler serviceKeyHandler = new PolicyMapKeyHandler() { // from class: com.sun.xml.internal.ws.policy.PolicyMap.1
        @Override // com.sun.xml.internal.ws.policy.PolicyMapKeyHandler
        public boolean areEqual(PolicyMapKey key1, PolicyMapKey key2) {
            return key1.getService().equals(key2.getService());
        }

        @Override // com.sun.xml.internal.ws.policy.PolicyMapKeyHandler
        public int generateHashCode(PolicyMapKey key) {
            int result = (37 * 17) + key.getService().hashCode();
            return result;
        }
    };
    private static final PolicyMapKeyHandler endpointKeyHandler = new PolicyMapKeyHandler() { // from class: com.sun.xml.internal.ws.policy.PolicyMap.2
        @Override // com.sun.xml.internal.ws.policy.PolicyMapKeyHandler
        public boolean areEqual(PolicyMapKey key1, PolicyMapKey key2) {
            boolean retVal = 1 != 0 && key1.getService().equals(key2.getService());
            boolean retVal2 = retVal && (key1.getPort() != null ? key1.getPort().equals(key2.getPort()) : key2.getPort() == null);
            return retVal2;
        }

        @Override // com.sun.xml.internal.ws.policy.PolicyMapKeyHandler
        public int generateHashCode(PolicyMapKey key) {
            int result = (37 * 17) + key.getService().hashCode();
            return (37 * result) + (key.getPort() == null ? 0 : key.getPort().hashCode());
        }
    };
    private static final PolicyMapKeyHandler operationAndInputOutputMessageKeyHandler = new PolicyMapKeyHandler() { // from class: com.sun.xml.internal.ws.policy.PolicyMap.3
        @Override // com.sun.xml.internal.ws.policy.PolicyMapKeyHandler
        public boolean areEqual(PolicyMapKey key1, PolicyMapKey key2) {
            boolean retVal = 1 != 0 && key1.getService().equals(key2.getService());
            boolean retVal2 = retVal && (key1.getPort() != null ? key1.getPort().equals(key2.getPort()) : key2.getPort() == null);
            boolean retVal3 = retVal2 && (key1.getOperation() != null ? key1.getOperation().equals(key2.getOperation()) : key2.getOperation() == null);
            return retVal3;
        }

        @Override // com.sun.xml.internal.ws.policy.PolicyMapKeyHandler
        public int generateHashCode(PolicyMapKey key) {
            int result = (37 * 17) + key.getService().hashCode();
            return (37 * ((37 * result) + (key.getPort() == null ? 0 : key.getPort().hashCode()))) + (key.getOperation() == null ? 0 : key.getOperation().hashCode());
        }
    };
    private static final PolicyMapKeyHandler faultMessageHandler = new PolicyMapKeyHandler() { // from class: com.sun.xml.internal.ws.policy.PolicyMap.4
        @Override // com.sun.xml.internal.ws.policy.PolicyMapKeyHandler
        public boolean areEqual(PolicyMapKey key1, PolicyMapKey key2) {
            boolean retVal = 1 != 0 && key1.getService().equals(key2.getService());
            boolean retVal2 = retVal && (key1.getPort() != null ? key1.getPort().equals(key2.getPort()) : key2.getPort() == null);
            boolean retVal3 = retVal2 && (key1.getOperation() != null ? key1.getOperation().equals(key2.getOperation()) : key2.getOperation() == null);
            boolean retVal4 = retVal3 && (key1.getFaultMessage() != null ? key1.getFaultMessage().equals(key2.getFaultMessage()) : key2.getFaultMessage() == null);
            return retVal4;
        }

        @Override // com.sun.xml.internal.ws.policy.PolicyMapKeyHandler
        public int generateHashCode(PolicyMapKey key) {
            int result = (37 * 17) + key.getService().hashCode();
            return (37 * ((37 * ((37 * result) + (key.getPort() == null ? 0 : key.getPort().hashCode()))) + (key.getOperation() == null ? 0 : key.getOperation().hashCode()))) + (key.getFaultMessage() == null ? 0 : key.getFaultMessage().hashCode());
        }
    };
    private static final PolicyMerger merger = PolicyMerger.getMerger();
    private final ScopeMap serviceMap = new ScopeMap(merger, serviceKeyHandler);
    private final ScopeMap endpointMap = new ScopeMap(merger, endpointKeyHandler);
    private final ScopeMap operationMap = new ScopeMap(merger, operationAndInputOutputMessageKeyHandler);
    private final ScopeMap inputMessageMap = new ScopeMap(merger, operationAndInputOutputMessageKeyHandler);
    private final ScopeMap outputMessageMap = new ScopeMap(merger, operationAndInputOutputMessageKeyHandler);
    private final ScopeMap faultMessageMap = new ScopeMap(merger, faultMessageHandler);

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/PolicyMap$ScopeType.class */
    enum ScopeType {
        SERVICE,
        ENDPOINT,
        OPERATION,
        INPUT_MESSAGE,
        OUTPUT_MESSAGE,
        FAULT_MESSAGE
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/PolicyMap$ScopeMap.class */
    private static final class ScopeMap implements Iterable<Policy> {
        private final Map<PolicyMapKey, PolicyScope> internalMap = new HashMap();
        private final PolicyMapKeyHandler scopeKeyHandler;
        private final PolicyMerger merger;

        ScopeMap(PolicyMerger merger, PolicyMapKeyHandler scopeKeyHandler) {
            this.merger = merger;
            this.scopeKeyHandler = scopeKeyHandler;
        }

        Policy getEffectivePolicy(PolicyMapKey key) throws PolicyException {
            PolicyScope scope = this.internalMap.get(createLocalCopy(key));
            if (scope == null) {
                return null;
            }
            return scope.getEffectivePolicy(this.merger);
        }

        void putSubject(PolicyMapKey key, PolicySubject subject) {
            PolicyMapKey localKey = createLocalCopy(key);
            PolicyScope scope = this.internalMap.get(localKey);
            if (scope == null) {
                List<PolicySubject> list = new LinkedList<>();
                list.add(subject);
                this.internalMap.put(localKey, new PolicyScope(list));
                return;
            }
            scope.attach(subject);
        }

        void setNewEffectivePolicy(PolicyMapKey key, Policy newEffectivePolicy) {
            PolicySubject subject = new PolicySubject(key, newEffectivePolicy);
            PolicyMapKey localKey = createLocalCopy(key);
            PolicyScope scope = this.internalMap.get(localKey);
            if (scope == null) {
                List<PolicySubject> list = new LinkedList<>();
                list.add(subject);
                this.internalMap.put(localKey, new PolicyScope(list));
            } else {
                scope.dettachAllSubjects();
                scope.attach(subject);
            }
        }

        Collection<PolicyScope> getStoredScopes() {
            return this.internalMap.values();
        }

        Set<PolicyMapKey> getAllKeys() {
            return this.internalMap.keySet();
        }

        private PolicyMapKey createLocalCopy(PolicyMapKey key) {
            if (key == null) {
                throw ((IllegalArgumentException) PolicyMap.LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0045_POLICY_MAP_KEY_MUST_NOT_BE_NULL())));
            }
            PolicyMapKey localKeyCopy = new PolicyMapKey(key);
            localKeyCopy.setHandler(this.scopeKeyHandler);
            return localKeyCopy;
        }

        @Override // java.lang.Iterable, java.util.List
        public Iterator<Policy> iterator() {
            return new Iterator<Policy>() { // from class: com.sun.xml.internal.ws.policy.PolicyMap.ScopeMap.1
                private final Iterator<PolicyMapKey> keysIterator;

                {
                    this.keysIterator = ScopeMap.this.internalMap.keySet().iterator();
                }

                @Override // java.util.Iterator
                public boolean hasNext() {
                    return this.keysIterator.hasNext();
                }

                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.util.Iterator
                public Policy next() {
                    PolicyMapKey key = this.keysIterator.next();
                    try {
                        return ScopeMap.this.getEffectivePolicy(key);
                    } catch (PolicyException e2) {
                        throw ((IllegalStateException) PolicyMap.LOGGER.logSevereException(new IllegalStateException(LocalizationMessages.WSP_0069_EXCEPTION_WHILE_RETRIEVING_EFFECTIVE_POLICY_FOR_KEY(key), e2)));
                    }
                }

                @Override // java.util.Iterator
                public void remove() {
                    throw ((UnsupportedOperationException) PolicyMap.LOGGER.logSevereException(new UnsupportedOperationException(LocalizationMessages.WSP_0034_REMOVE_OPERATION_NOT_SUPPORTED())));
                }
            };
        }

        public boolean isEmpty() {
            return this.internalMap.isEmpty();
        }

        public String toString() {
            return this.internalMap.toString();
        }
    }

    private PolicyMap() {
    }

    public static PolicyMap createPolicyMap(Collection<? extends PolicyMapMutator> mutators) {
        PolicyMap result = new PolicyMap();
        if (mutators != null && !mutators.isEmpty()) {
            for (PolicyMapMutator mutator : mutators) {
                mutator.connect(result);
            }
        }
        return result;
    }

    public Policy getServiceEffectivePolicy(PolicyMapKey key) throws PolicyException {
        return this.serviceMap.getEffectivePolicy(key);
    }

    public Policy getEndpointEffectivePolicy(PolicyMapKey key) throws PolicyException {
        return this.endpointMap.getEffectivePolicy(key);
    }

    public Policy getOperationEffectivePolicy(PolicyMapKey key) throws PolicyException {
        return this.operationMap.getEffectivePolicy(key);
    }

    public Policy getInputMessageEffectivePolicy(PolicyMapKey key) throws PolicyException {
        return this.inputMessageMap.getEffectivePolicy(key);
    }

    public Policy getOutputMessageEffectivePolicy(PolicyMapKey key) throws PolicyException {
        return this.outputMessageMap.getEffectivePolicy(key);
    }

    public Policy getFaultMessageEffectivePolicy(PolicyMapKey key) throws PolicyException {
        return this.faultMessageMap.getEffectivePolicy(key);
    }

    public Collection<PolicyMapKey> getAllServiceScopeKeys() {
        return this.serviceMap.getAllKeys();
    }

    public Collection<PolicyMapKey> getAllEndpointScopeKeys() {
        return this.endpointMap.getAllKeys();
    }

    public Collection<PolicyMapKey> getAllOperationScopeKeys() {
        return this.operationMap.getAllKeys();
    }

    public Collection<PolicyMapKey> getAllInputMessageScopeKeys() {
        return this.inputMessageMap.getAllKeys();
    }

    public Collection<PolicyMapKey> getAllOutputMessageScopeKeys() {
        return this.outputMessageMap.getAllKeys();
    }

    public Collection<PolicyMapKey> getAllFaultMessageScopeKeys() {
        return this.faultMessageMap.getAllKeys();
    }

    void putSubject(ScopeType scopeType, PolicyMapKey key, PolicySubject subject) {
        switch (scopeType) {
            case SERVICE:
                this.serviceMap.putSubject(key, subject);
                return;
            case ENDPOINT:
                this.endpointMap.putSubject(key, subject);
                return;
            case OPERATION:
                this.operationMap.putSubject(key, subject);
                return;
            case INPUT_MESSAGE:
                this.inputMessageMap.putSubject(key, subject);
                return;
            case OUTPUT_MESSAGE:
                this.outputMessageMap.putSubject(key, subject);
                return;
            case FAULT_MESSAGE:
                this.faultMessageMap.putSubject(key, subject);
                return;
            default:
                throw ((IllegalArgumentException) LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0002_UNRECOGNIZED_SCOPE_TYPE(scopeType))));
        }
    }

    void setNewEffectivePolicyForScope(ScopeType scopeType, PolicyMapKey key, Policy newEffectivePolicy) throws IllegalArgumentException {
        if (scopeType == null || key == null || newEffectivePolicy == null) {
            throw ((IllegalArgumentException) LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0062_INPUT_PARAMS_MUST_NOT_BE_NULL())));
        }
        switch (scopeType) {
            case SERVICE:
                this.serviceMap.setNewEffectivePolicy(key, newEffectivePolicy);
                return;
            case ENDPOINT:
                this.endpointMap.setNewEffectivePolicy(key, newEffectivePolicy);
                return;
            case OPERATION:
                this.operationMap.setNewEffectivePolicy(key, newEffectivePolicy);
                return;
            case INPUT_MESSAGE:
                this.inputMessageMap.setNewEffectivePolicy(key, newEffectivePolicy);
                return;
            case OUTPUT_MESSAGE:
                this.outputMessageMap.setNewEffectivePolicy(key, newEffectivePolicy);
                return;
            case FAULT_MESSAGE:
                this.faultMessageMap.setNewEffectivePolicy(key, newEffectivePolicy);
                return;
            default:
                throw ((IllegalArgumentException) LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0002_UNRECOGNIZED_SCOPE_TYPE(scopeType))));
        }
    }

    public Collection<PolicySubject> getPolicySubjects() {
        List<PolicySubject> subjects = new LinkedList<>();
        addSubjects(subjects, this.serviceMap);
        addSubjects(subjects, this.endpointMap);
        addSubjects(subjects, this.operationMap);
        addSubjects(subjects, this.inputMessageMap);
        addSubjects(subjects, this.outputMessageMap);
        addSubjects(subjects, this.faultMessageMap);
        return subjects;
    }

    public boolean isInputMessageSubject(PolicySubject subject) {
        for (PolicyScope scope : this.inputMessageMap.getStoredScopes()) {
            if (scope.getPolicySubjects().contains(subject)) {
                return true;
            }
        }
        return false;
    }

    public boolean isOutputMessageSubject(PolicySubject subject) {
        for (PolicyScope scope : this.outputMessageMap.getStoredScopes()) {
            if (scope.getPolicySubjects().contains(subject)) {
                return true;
            }
        }
        return false;
    }

    public boolean isFaultMessageSubject(PolicySubject subject) {
        for (PolicyScope scope : this.faultMessageMap.getStoredScopes()) {
            if (scope.getPolicySubjects().contains(subject)) {
                return true;
            }
        }
        return false;
    }

    public boolean isEmpty() {
        return this.serviceMap.isEmpty() && this.endpointMap.isEmpty() && this.operationMap.isEmpty() && this.inputMessageMap.isEmpty() && this.outputMessageMap.isEmpty() && this.faultMessageMap.isEmpty();
    }

    private void addSubjects(Collection<PolicySubject> subjects, ScopeMap scopeMap) {
        for (PolicyScope scope : scopeMap.getStoredScopes()) {
            Collection<PolicySubject> scopedSubjects = scope.getPolicySubjects();
            subjects.addAll(scopedSubjects);
        }
    }

    public static PolicyMapKey createWsdlServiceScopeKey(QName service) throws IllegalArgumentException {
        if (service == null) {
            throw ((IllegalArgumentException) LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0031_SERVICE_PARAM_MUST_NOT_BE_NULL())));
        }
        return new PolicyMapKey(service, null, null, serviceKeyHandler);
    }

    public static PolicyMapKey createWsdlEndpointScopeKey(QName service, QName port) throws IllegalArgumentException {
        if (service == null || port == null) {
            throw ((IllegalArgumentException) LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0033_SERVICE_AND_PORT_PARAM_MUST_NOT_BE_NULL(service, port))));
        }
        return new PolicyMapKey(service, port, null, endpointKeyHandler);
    }

    public static PolicyMapKey createWsdlOperationScopeKey(QName service, QName port, QName operation) throws IllegalArgumentException {
        return createOperationOrInputOutputMessageKey(service, port, operation);
    }

    public static PolicyMapKey createWsdlMessageScopeKey(QName service, QName port, QName operation) throws IllegalArgumentException {
        return createOperationOrInputOutputMessageKey(service, port, operation);
    }

    public static PolicyMapKey createWsdlFaultMessageScopeKey(QName service, QName port, QName operation, QName fault) throws IllegalArgumentException {
        if (service == null || port == null || operation == null || fault == null) {
            throw ((IllegalArgumentException) LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0030_SERVICE_PORT_OPERATION_FAULT_MSG_PARAM_MUST_NOT_BE_NULL(service, port, operation, fault))));
        }
        return new PolicyMapKey(service, port, operation, fault, faultMessageHandler);
    }

    private static PolicyMapKey createOperationOrInputOutputMessageKey(QName service, QName port, QName operation) {
        if (service == null || port == null || operation == null) {
            throw ((IllegalArgumentException) LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0029_SERVICE_PORT_OPERATION_PARAM_MUST_NOT_BE_NULL(service, port, operation))));
        }
        return new PolicyMapKey(service, port, operation, operationAndInputOutputMessageKeyHandler);
    }

    public String toString() {
        StringBuffer result = new StringBuffer();
        if (null != this.serviceMap) {
            result.append("\nServiceMap=").append((Object) this.serviceMap);
        }
        if (null != this.endpointMap) {
            result.append("\nEndpointMap=").append((Object) this.endpointMap);
        }
        if (null != this.operationMap) {
            result.append("\nOperationMap=").append((Object) this.operationMap);
        }
        if (null != this.inputMessageMap) {
            result.append("\nInputMessageMap=").append((Object) this.inputMessageMap);
        }
        if (null != this.outputMessageMap) {
            result.append("\nOutputMessageMap=").append((Object) this.outputMessageMap);
        }
        if (null != this.faultMessageMap) {
            result.append("\nFaultMessageMap=").append((Object) this.faultMessageMap);
        }
        return result.toString();
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<Policy> iterator() {
        return new Iterator<Policy>() { // from class: com.sun.xml.internal.ws.policy.PolicyMap.5
            private final Iterator<Iterator<Policy>> mainIterator;
            private Iterator<Policy> currentScopeIterator;

            {
                Collection<Iterator<Policy>> scopeIterators = new ArrayList<>(6);
                scopeIterators.add(PolicyMap.this.serviceMap.iterator());
                scopeIterators.add(PolicyMap.this.endpointMap.iterator());
                scopeIterators.add(PolicyMap.this.operationMap.iterator());
                scopeIterators.add(PolicyMap.this.inputMessageMap.iterator());
                scopeIterators.add(PolicyMap.this.outputMessageMap.iterator());
                scopeIterators.add(PolicyMap.this.faultMessageMap.iterator());
                this.mainIterator = scopeIterators.iterator();
                this.currentScopeIterator = this.mainIterator.next();
            }

            @Override // java.util.Iterator
            public boolean hasNext() {
                while (!this.currentScopeIterator.hasNext()) {
                    if (this.mainIterator.hasNext()) {
                        this.currentScopeIterator = this.mainIterator.next();
                    } else {
                        return false;
                    }
                }
                return true;
            }

            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.util.Iterator
            public Policy next() {
                if (!hasNext()) {
                    throw ((NoSuchElementException) PolicyMap.LOGGER.logSevereException(new NoSuchElementException(LocalizationMessages.WSP_0054_NO_MORE_ELEMS_IN_POLICY_MAP())));
                }
                return this.currentScopeIterator.next();
            }

            @Override // java.util.Iterator
            public void remove() {
                throw ((UnsupportedOperationException) PolicyMap.LOGGER.logSevereException(new UnsupportedOperationException(LocalizationMessages.WSP_0034_REMOVE_OPERATION_NOT_SUPPORTED())));
            }
        };
    }
}
