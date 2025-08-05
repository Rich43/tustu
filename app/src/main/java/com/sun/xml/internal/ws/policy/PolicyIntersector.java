package com.sun.xml.internal.ws.policy;

import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.internal.ws.policy.privateutil.PolicyLogger;
import com.sun.xml.internal.ws.policy.sourcemodel.wspolicy.NamespaceVersion;
import java.util.ArrayList;
import java.util.Collection;
import java.util.LinkedList;
import java.util.List;
import java.util.Queue;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/PolicyIntersector.class */
public final class PolicyIntersector {
    private static final PolicyIntersector STRICT_INTERSECTOR = new PolicyIntersector(CompatibilityMode.STRICT);
    private static final PolicyIntersector LAX_INTERSECTOR = new PolicyIntersector(CompatibilityMode.LAX);
    private static final PolicyLogger LOGGER = PolicyLogger.getLogger((Class<?>) PolicyIntersector.class);
    private CompatibilityMode mode;

    /* loaded from: rt.jar:com/sun/xml/internal/ws/policy/PolicyIntersector$CompatibilityMode.class */
    enum CompatibilityMode {
        STRICT,
        LAX
    }

    private PolicyIntersector(CompatibilityMode intersectionMode) {
        this.mode = intersectionMode;
    }

    public static PolicyIntersector createStrictPolicyIntersector() {
        return STRICT_INTERSECTOR;
    }

    public static PolicyIntersector createLaxPolicyIntersector() {
        return LAX_INTERSECTOR;
    }

    public Policy intersect(Policy... policies) {
        if (policies == null || policies.length == 0) {
            throw ((IllegalArgumentException) LOGGER.logSevereException(new IllegalArgumentException(LocalizationMessages.WSP_0056_NEITHER_NULL_NOR_EMPTY_POLICY_COLLECTION_EXPECTED())));
        }
        if (policies.length == 1) {
            return policies[0];
        }
        boolean found = false;
        boolean allPoliciesEmpty = true;
        NamespaceVersion latestVersion = null;
        int length = policies.length;
        for (int i2 = 0; i2 < length; i2++) {
            Policy tested = policies[i2];
            if (tested.isEmpty()) {
                found = true;
            } else {
                if (tested.isNull()) {
                    found = true;
                }
                allPoliciesEmpty = false;
            }
            if (latestVersion == null || latestVersion.compareTo(tested.getNamespaceVersion()) < 0) {
                latestVersion = tested.getNamespaceVersion();
            }
            if (found && !allPoliciesEmpty) {
                return Policy.createNullPolicy(latestVersion, null, null);
            }
        }
        NamespaceVersion latestVersion2 = latestVersion != null ? latestVersion : NamespaceVersion.getLatestVersion();
        if (allPoliciesEmpty) {
            return Policy.createEmptyPolicy(latestVersion2, null, null);
        }
        LinkedList linkedList = new LinkedList(policies[0].getContent());
        Queue<AssertionSet> testedAlternatives = new LinkedList<>();
        List<AssertionSet> alternativesToMerge = new ArrayList<>(2);
        for (int i3 = 1; i3 < policies.length; i3++) {
            Collection<AssertionSet> currentAlternatives = policies[i3].getContent();
            testedAlternatives.clear();
            testedAlternatives.addAll(linkedList);
            linkedList.clear();
            while (true) {
                AssertionSet testedAlternative = testedAlternatives.poll();
                if (testedAlternative != null) {
                    for (AssertionSet currentAlternative : currentAlternatives) {
                        if (testedAlternative.isCompatibleWith(currentAlternative, this.mode)) {
                            alternativesToMerge.add(testedAlternative);
                            alternativesToMerge.add(currentAlternative);
                            linkedList.add(AssertionSet.createMergedAssertionSet(alternativesToMerge));
                            alternativesToMerge.clear();
                        }
                    }
                }
            }
        }
        return Policy.createPolicy(latestVersion2, null, null, linkedList);
    }
}
