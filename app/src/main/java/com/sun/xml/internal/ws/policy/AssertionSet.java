package com.sun.xml.internal.ws.policy;

import com.sun.xml.internal.ws.policy.PolicyIntersector;
import com.sun.xml.internal.ws.policy.privateutil.LocalizationMessages;
import com.sun.xml.internal.ws.policy.privateutil.PolicyUtils;
import java.util.Collection;
import java.util.Collections;
import java.util.Comparator;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/policy/AssertionSet.class */
public final class AssertionSet implements Iterable<PolicyAssertion>, Comparable<AssertionSet> {
    private static final AssertionSet EMPTY_ASSERTION_SET;
    private static final Comparator<PolicyAssertion> ASSERTION_COMPARATOR;
    private final List<PolicyAssertion> assertions;
    private final Set<QName> vocabulary;
    private final Collection<QName> immutableVocabulary;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !AssertionSet.class.desiredAssertionStatus();
        EMPTY_ASSERTION_SET = new AssertionSet((List<PolicyAssertion>) Collections.unmodifiableList(new LinkedList()));
        ASSERTION_COMPARATOR = new Comparator<PolicyAssertion>() { // from class: com.sun.xml.internal.ws.policy.AssertionSet.1
            @Override // java.util.Comparator
            public int compare(PolicyAssertion pa1, PolicyAssertion pa2) {
                if (pa1.equals(pa2)) {
                    return 0;
                }
                int result = PolicyUtils.Comparison.QNAME_COMPARATOR.compare(pa1.getName(), pa2.getName());
                if (result != 0) {
                    return result;
                }
                int result2 = PolicyUtils.Comparison.compareNullableStrings(pa1.getValue(), pa2.getValue());
                if (result2 != 0) {
                    return result2;
                }
                int result3 = PolicyUtils.Comparison.compareBoolean(pa1.hasNestedAssertions(), pa2.hasNestedAssertions());
                if (result3 != 0) {
                    return result3;
                }
                int result4 = PolicyUtils.Comparison.compareBoolean(pa1.hasNestedPolicy(), pa2.hasNestedPolicy());
                if (result4 != 0) {
                    return result4;
                }
                return Math.round(Math.signum(pa1.hashCode() - pa2.hashCode()));
            }
        };
    }

    private AssertionSet(List<PolicyAssertion> list) {
        this.vocabulary = new TreeSet(PolicyUtils.Comparison.QNAME_COMPARATOR);
        this.immutableVocabulary = Collections.unmodifiableCollection(this.vocabulary);
        if (!$assertionsDisabled && list == null) {
            throw new AssertionError((Object) LocalizationMessages.WSP_0037_PRIVATE_CONSTRUCTOR_DOES_NOT_TAKE_NULL());
        }
        this.assertions = list;
    }

    private AssertionSet(Collection<AssertionSet> alternatives) {
        this.vocabulary = new TreeSet(PolicyUtils.Comparison.QNAME_COMPARATOR);
        this.immutableVocabulary = Collections.unmodifiableCollection(this.vocabulary);
        this.assertions = new LinkedList();
        for (AssertionSet alternative : alternatives) {
            addAll(alternative.assertions);
        }
    }

    private boolean add(PolicyAssertion assertion) {
        if (assertion == null || this.assertions.contains(assertion)) {
            return false;
        }
        this.assertions.add(assertion);
        this.vocabulary.add(assertion.getName());
        return true;
    }

    private boolean addAll(Collection<? extends PolicyAssertion> assertions) {
        boolean result = true;
        if (assertions != null) {
            for (PolicyAssertion assertion : assertions) {
                result &= add(assertion);
            }
        }
        return result;
    }

    Collection<PolicyAssertion> getAssertions() {
        return this.assertions;
    }

    Collection<QName> getVocabulary() {
        return this.immutableVocabulary;
    }

    boolean isCompatibleWith(AssertionSet alternative, PolicyIntersector.CompatibilityMode mode) {
        boolean result = mode == PolicyIntersector.CompatibilityMode.LAX || this.vocabulary.equals(alternative.vocabulary);
        boolean result2 = result && areAssertionsCompatible(alternative, mode);
        boolean result3 = result2 && alternative.areAssertionsCompatible(this, mode);
        return result3;
    }

    private boolean areAssertionsCompatible(AssertionSet alternative, PolicyIntersector.CompatibilityMode mode) {
        for (PolicyAssertion thisAssertion : this.assertions) {
            if (mode == PolicyIntersector.CompatibilityMode.STRICT || !thisAssertion.isIgnorable()) {
                for (PolicyAssertion thatAssertion : alternative.assertions) {
                    if (thisAssertion.isCompatibleWith(thatAssertion, mode)) {
                        break;
                    }
                }
                return false;
            }
        }
        return true;
    }

    public static AssertionSet createMergedAssertionSet(Collection<AssertionSet> alternatives) {
        if (alternatives == null || alternatives.isEmpty()) {
            return EMPTY_ASSERTION_SET;
        }
        AssertionSet result = new AssertionSet(alternatives);
        Collections.sort(result.assertions, ASSERTION_COMPARATOR);
        return result;
    }

    public static AssertionSet createAssertionSet(Collection<? extends PolicyAssertion> assertions) {
        if (assertions == null || assertions.isEmpty()) {
            return EMPTY_ASSERTION_SET;
        }
        AssertionSet result = new AssertionSet((List<PolicyAssertion>) new LinkedList());
        result.addAll(assertions);
        Collections.sort(result.assertions, ASSERTION_COMPARATOR);
        return result;
    }

    public static AssertionSet emptyAssertionSet() {
        return EMPTY_ASSERTION_SET;
    }

    @Override // java.lang.Iterable, java.util.List
    public Iterator<PolicyAssertion> iterator() {
        return this.assertions.iterator();
    }

    public Collection<PolicyAssertion> get(QName name) {
        List<PolicyAssertion> matched = new LinkedList<>();
        if (this.vocabulary.contains(name)) {
            for (PolicyAssertion assertion : this.assertions) {
                if (assertion.getName().equals(name)) {
                    matched.add(assertion);
                }
            }
        }
        return matched;
    }

    public boolean isEmpty() {
        return this.assertions.isEmpty();
    }

    public boolean contains(QName assertionName) {
        return this.vocabulary.contains(assertionName);
    }

    @Override // java.lang.Comparable
    public int compareTo(AssertionSet that) {
        if (equals(that)) {
            return 0;
        }
        Iterator<QName> vIterator2 = that.getVocabulary().iterator();
        for (QName entry1 : getVocabulary()) {
            if (vIterator2.hasNext()) {
                QName entry2 = vIterator2.next();
                int result = PolicyUtils.Comparison.QNAME_COMPARATOR.compare(entry1, entry2);
                if (result != 0) {
                    return result;
                }
            } else {
                return 1;
            }
        }
        if (vIterator2.hasNext()) {
            return -1;
        }
        Iterator<PolicyAssertion> pIterator2 = that.getAssertions().iterator();
        for (PolicyAssertion pa1 : getAssertions()) {
            if (pIterator2.hasNext()) {
                PolicyAssertion pa2 = pIterator2.next();
                int result2 = ASSERTION_COMPARATOR.compare(pa1, pa2);
                if (result2 != 0) {
                    return result2;
                }
            } else {
                return 1;
            }
        }
        if (pIterator2.hasNext()) {
            return -1;
        }
        return 1;
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof AssertionSet)) {
            return false;
        }
        AssertionSet that = (AssertionSet) obj;
        boolean result = 1 != 0 && this.vocabulary.equals(that.vocabulary);
        boolean result2 = result && this.assertions.size() == that.assertions.size() && this.assertions.containsAll(that.assertions);
        return result2;
    }

    public int hashCode() {
        int result = (37 * 17) + this.vocabulary.hashCode();
        return (37 * result) + this.assertions.hashCode();
    }

    public String toString() {
        return toString(0, new StringBuffer()).toString();
    }

    StringBuffer toString(int indentLevel, StringBuffer buffer) {
        String indent = PolicyUtils.Text.createIndent(indentLevel);
        String innerIndent = PolicyUtils.Text.createIndent(indentLevel + 1);
        buffer.append(indent).append("assertion set {").append(PolicyUtils.Text.NEW_LINE);
        if (this.assertions.isEmpty()) {
            buffer.append(innerIndent).append("no assertions").append(PolicyUtils.Text.NEW_LINE);
        } else {
            for (PolicyAssertion assertion : this.assertions) {
                assertion.toString(indentLevel + 1, buffer).append(PolicyUtils.Text.NEW_LINE);
            }
        }
        buffer.append(indent).append('}');
        return buffer;
    }
}
