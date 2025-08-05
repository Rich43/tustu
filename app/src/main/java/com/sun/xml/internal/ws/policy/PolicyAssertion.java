package com.sun.xml.internal.ws.policy;

import com.sun.xml.internal.ws.policy.PolicyIntersector;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import com.sun.xml.internal.ws.policy.sourcemodel.AssertionData;
import com.sun.xml.internal.ws.policy.sourcemodel.ModelNode;
import java.util.Collection;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/PolicyAssertion.class */
public abstract class PolicyAssertion {
    private final AssertionData data;
    private AssertionSet parameters;
    private NestedPolicy nestedPolicy;

    protected PolicyAssertion() {
        this.data = AssertionData.createAssertionData(null);
        this.parameters = AssertionSet.createAssertionSet(null);
    }

    @Deprecated
    protected PolicyAssertion(AssertionData assertionData, Collection<? extends PolicyAssertion> assertionParameters, AssertionSet nestedAlternative) {
        this.data = assertionData;
        if (nestedAlternative != null) {
            this.nestedPolicy = NestedPolicy.createNestedPolicy(nestedAlternative);
        }
        this.parameters = AssertionSet.createAssertionSet(assertionParameters);
    }

    protected PolicyAssertion(AssertionData assertionData, Collection<? extends PolicyAssertion> assertionParameters) {
        if (assertionData == null) {
            this.data = AssertionData.createAssertionData(null);
        } else {
            this.data = assertionData;
        }
        this.parameters = AssertionSet.createAssertionSet(assertionParameters);
    }

    public final QName getName() {
        return this.data.getName();
    }

    public final String getValue() {
        return this.data.getValue();
    }

    public boolean isOptional() {
        return this.data.isOptionalAttributeSet();
    }

    public boolean isIgnorable() {
        return this.data.isIgnorableAttributeSet();
    }

    public final boolean isPrivate() {
        return this.data.isPrivateAttributeSet();
    }

    public final Set<Map.Entry<QName, String>> getAttributesSet() {
        return this.data.getAttributesSet();
    }

    public final Map<QName, String> getAttributes() {
        return this.data.getAttributes();
    }

    public final String getAttributeValue(QName name) {
        return this.data.getAttributeValue(name);
    }

    @Deprecated
    public final boolean hasNestedAssertions() {
        return !this.parameters.isEmpty();
    }

    public final boolean hasParameters() {
        return !this.parameters.isEmpty();
    }

    @Deprecated
    public final Iterator<PolicyAssertion> getNestedAssertionsIterator() {
        return this.parameters.iterator();
    }

    public final Iterator<PolicyAssertion> getParametersIterator() {
        return this.parameters.iterator();
    }

    boolean isParameter() {
        return this.data.getNodeType() == ModelNode.Type.ASSERTION_PARAMETER_NODE;
    }

    public boolean hasNestedPolicy() {
        return getNestedPolicy() != null;
    }

    public NestedPolicy getNestedPolicy() {
        return this.nestedPolicy;
    }

    public <T extends PolicyAssertion> T getImplementation(Class<T> type) {
        if (type.isAssignableFrom(getClass())) {
            return type.cast(this);
        }
        return null;
    }

    public String toString() {
        return toString(0, new StringBuffer()).toString();
    }

    protected StringBuffer toString(int indentLevel, StringBuffer buffer) {
        String indent = PolicyUtils.Text.createIndent(indentLevel);
        String innerIndent = PolicyUtils.Text.createIndent(indentLevel + 1);
        buffer.append(indent).append("Assertion[").append(getClass().getName()).append("] {").append(PolicyUtils.Text.NEW_LINE);
        this.data.toString(indentLevel + 1, buffer);
        buffer.append(PolicyUtils.Text.NEW_LINE);
        if (hasParameters()) {
            buffer.append(innerIndent).append("parameters {").append(PolicyUtils.Text.NEW_LINE);
            Iterator<PolicyAssertion> it = this.parameters.iterator();
            while (it.hasNext()) {
                PolicyAssertion parameter = it.next();
                parameter.toString(indentLevel + 2, buffer).append(PolicyUtils.Text.NEW_LINE);
            }
            buffer.append(innerIndent).append('}').append(PolicyUtils.Text.NEW_LINE);
        } else {
            buffer.append(innerIndent).append("no parameters").append(PolicyUtils.Text.NEW_LINE);
        }
        if (hasNestedPolicy()) {
            getNestedPolicy().toString(indentLevel + 1, buffer).append(PolicyUtils.Text.NEW_LINE);
        } else {
            buffer.append(innerIndent).append("no nested policy").append(PolicyUtils.Text.NEW_LINE);
        }
        buffer.append(indent).append('}');
        return buffer;
    }

    boolean isCompatibleWith(PolicyAssertion assertion, PolicyIntersector.CompatibilityMode mode) {
        boolean result = this.data.getName().equals(assertion.data.getName()) && hasNestedPolicy() == assertion.hasNestedPolicy();
        if (result && hasNestedPolicy()) {
            result = getNestedPolicy().getAssertionSet().isCompatibleWith(assertion.getNestedPolicy().getAssertionSet(), mode);
        }
        return result;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof PolicyAssertion)) {
            return false;
        }
        PolicyAssertion that = (PolicyAssertion) obj;
        boolean result = 1 != 0 && this.data.equals(that.data);
        boolean result2 = result && this.parameters.equals(that.parameters);
        boolean result3 = result2 && (getNestedPolicy() != null ? getNestedPolicy().equals(that.getNestedPolicy()) : that.getNestedPolicy() == null);
        return result3;
    }

    public int hashCode() {
        int result = (37 * 17) + this.data.hashCode();
        return (37 * ((37 * result) + (hasParameters() ? 17 : 0))) + (hasNestedPolicy() ? 17 : 0);
    }
}
