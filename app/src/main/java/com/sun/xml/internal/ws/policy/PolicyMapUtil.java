package com.sun.xml.internal.ws.policy;

import com.sun.xml.internal.ws.policy.PolicyMap;
import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.subject.PolicyMapKeyConverter;
import com.sun.xml.internal.ws.policy.subject.WsdlBindingSubject;
import java.util.Collection;
import java.util.HashMap;
import java.util.Iterator;
import java.util.LinkedList;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/PolicyMapUtil.class */
public class PolicyMapUtil {
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) PolicyMapUtil.class);
    private static final PolicyMerger MERGER = PolicyMerger.getMerger();

    private PolicyMapUtil() {
    }

    public static void rejectAlternatives(PolicyMap map) throws PolicyException {
        Iterator<Policy> it = map.iterator();
        while (it.hasNext()) {
            Policy policy = it.next();
            if (policy.getNumberOfAssertionSets() > 1) {
                throw ((PolicyException) LOGGER.logSevereException(new PolicyException(LocalizationMessages.WSP_0035_RECONFIGURE_ALTERNATIVES(policy.getIdOrName()))));
            }
        }
    }

    public static void insertPolicies(PolicyMap policyMap, Collection<PolicySubject> policySubjects, QName serviceName, QName portName) throws IllegalArgumentException, PolicyException {
        LOGGER.entering(policyMap, policySubjects, serviceName, portName);
        HashMap<WsdlBindingSubject, Collection<Policy>> subjectToPolicies = new HashMap<>();
        for (PolicySubject subject : policySubjects) {
            Object actualSubject = subject.getSubject();
            if (actualSubject instanceof WsdlBindingSubject) {
                WsdlBindingSubject wsdlSubject = (WsdlBindingSubject) actualSubject;
                Collection<Policy> subjectPolicies = new LinkedList<>();
                subjectPolicies.add(subject.getEffectivePolicy(MERGER));
                Collection<Policy> existingPolicies = subjectToPolicies.put(wsdlSubject, subjectPolicies);
                if (existingPolicies != null) {
                    subjectPolicies.addAll(existingPolicies);
                }
            }
        }
        PolicyMapKeyConverter converter = new PolicyMapKeyConverter(serviceName, portName);
        for (WsdlBindingSubject wsdlSubject2 : subjectToPolicies.keySet()) {
            PolicySubject newSubject = new PolicySubject(wsdlSubject2, subjectToPolicies.get(wsdlSubject2));
            PolicyMapKey mapKey = converter.getPolicyMapKey(wsdlSubject2);
            if (wsdlSubject2.isBindingSubject()) {
                policyMap.putSubject(PolicyMap.ScopeType.ENDPOINT, mapKey, newSubject);
            } else if (wsdlSubject2.isBindingOperationSubject()) {
                policyMap.putSubject(PolicyMap.ScopeType.OPERATION, mapKey, newSubject);
            } else if (wsdlSubject2.isBindingMessageSubject()) {
                switch (wsdlSubject2.getMessageType()) {
                    case INPUT:
                        policyMap.putSubject(PolicyMap.ScopeType.INPUT_MESSAGE, mapKey, newSubject);
                        break;
                    case OUTPUT:
                        policyMap.putSubject(PolicyMap.ScopeType.OUTPUT_MESSAGE, mapKey, newSubject);
                        break;
                    case FAULT:
                        policyMap.putSubject(PolicyMap.ScopeType.FAULT_MESSAGE, mapKey, newSubject);
                        break;
                }
            }
        }
        LOGGER.exiting();
    }
}
