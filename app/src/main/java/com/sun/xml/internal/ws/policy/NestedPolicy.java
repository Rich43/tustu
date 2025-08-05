package com.sun.xml.internal.ws.policy;

import java.util.Arrays;
import java.util.Iterator;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/NestedPolicy.class */
public final class NestedPolicy extends Policy {
    private static final String NESTED_POLICY_TOSTRING_NAME = "nested policy";

    private NestedPolicy(AssertionSet set) {
        super(NESTED_POLICY_TOSTRING_NAME, Arrays.asList(set));
    }

    private NestedPolicy(String name, String policyId, AssertionSet set) {
        super(NESTED_POLICY_TOSTRING_NAME, name, policyId, Arrays.asList(set));
    }

    static NestedPolicy createNestedPolicy(AssertionSet set) {
        return new NestedPolicy(set);
    }

    static NestedPolicy createNestedPolicy(String name, String policyId, AssertionSet set) {
        return new NestedPolicy(name, policyId, set);
    }

    public AssertionSet getAssertionSet() {
        Iterator<AssertionSet> iterator = iterator();
        if (iterator.hasNext()) {
            return iterator.next();
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.policy.Policy
    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof NestedPolicy)) {
            return false;
        }
        NestedPolicy that = (NestedPolicy) obj;
        return super.equals(that);
    }

    @Override // com.sun.xml.internal.ws.policy.Policy
    public int hashCode() {
        return super.hashCode();
    }

    @Override // com.sun.xml.internal.ws.policy.Policy
    public String toString() {
        return toString(0, new StringBuffer()).toString();
    }

    @Override // com.sun.xml.internal.ws.policy.Policy
    StringBuffer toString(int indentLevel, StringBuffer buffer) {
        return super.toString(indentLevel, buffer);
    }
}
