package com.sun.xml.internal.ws.policy;

import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/PolicyMerger.class */
public final class PolicyMerger {
    private static final PolicyMerger merger = new PolicyMerger();

    private PolicyMerger() {
    }

    public static PolicyMerger getMerger() {
        return merger;
    }

    public Policy merge(Collection<Policy> policies) {
        if (policies == null || policies.isEmpty()) {
            return null;
        }
        if (policies.size() == 1) {
            return policies.iterator().next();
        }
        Collection<Collection<AssertionSet>> alternativeSets = new LinkedList<>();
        StringBuilder id = new StringBuilder();
        NamespaceVersion mergedVersion = policies.iterator().next().getNamespaceVersion();
        for (Policy policy : policies) {
            alternativeSets.add(policy.getContent());
            if (mergedVersion.compareTo(policy.getNamespaceVersion()) < 0) {
                mergedVersion = policy.getNamespaceVersion();
            }
            String policyId = policy.getId();
            if (policyId != null) {
                if (id.length() > 0) {
                    id.append('-');
                }
                id.append(policyId);
            }
        }
        Collection<Collection<AssertionSet>> combinedAlternatives = PolicyUtils.Collections.combine(null, alternativeSets, false);
        if (combinedAlternatives == null || combinedAlternatives.isEmpty()) {
            return Policy.createNullPolicy(mergedVersion, null, id.length() == 0 ? null : id.toString());
        }
        Collection<AssertionSet> mergedSetList = new ArrayList<>(combinedAlternatives.size());
        for (Collection<AssertionSet> toBeMerged : combinedAlternatives) {
            mergedSetList.add(AssertionSet.createMergedAssertionSet(toBeMerged));
        }
        return Policy.createPolicy(mergedVersion, null, id.length() == 0 ? null : id.toString(), mergedSetList);
    }
}
