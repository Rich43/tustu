package sun.security.provider.certpath;

import java.security.cert.PolicyNode;
import java.security.cert.PolicyQualifierInfo;
import java.util.Collections;
import java.util.HashSet;
import java.util.Iterator;
import java.util.Set;
import sun.security.pkcs11.wrapper.Constants;

/* loaded from: rt.jar:sun/security/provider/certpath/PolicyNodeImpl.class */
final class PolicyNodeImpl implements PolicyNode {
    private static final String ANY_POLICY = "2.5.29.32.0";
    private PolicyNodeImpl mParent;
    private HashSet<PolicyNodeImpl> mChildren;
    private String mValidPolicy;
    private HashSet<PolicyQualifierInfo> mQualifierSet;
    private boolean mCriticalityIndicator;
    private HashSet<String> mExpectedPolicySet;
    private boolean mOriginalExpectedPolicySet;
    private int mDepth;
    private boolean isImmutable;

    PolicyNodeImpl(PolicyNodeImpl policyNodeImpl, String str, Set<PolicyQualifierInfo> set, boolean z2, Set<String> set2, boolean z3) {
        this.isImmutable = false;
        this.mParent = policyNodeImpl;
        this.mChildren = new HashSet<>();
        if (str != null) {
            this.mValidPolicy = str;
        } else {
            this.mValidPolicy = "";
        }
        if (set != null) {
            this.mQualifierSet = new HashSet<>(set);
        } else {
            this.mQualifierSet = new HashSet<>();
        }
        this.mCriticalityIndicator = z2;
        if (set2 != null) {
            this.mExpectedPolicySet = new HashSet<>(set2);
        } else {
            this.mExpectedPolicySet = new HashSet<>();
        }
        this.mOriginalExpectedPolicySet = !z3;
        if (this.mParent != null) {
            this.mDepth = this.mParent.getDepth() + 1;
            this.mParent.addChild(this);
        } else {
            this.mDepth = 0;
        }
    }

    PolicyNodeImpl(PolicyNodeImpl policyNodeImpl, PolicyNodeImpl policyNodeImpl2) {
        this(policyNodeImpl, policyNodeImpl2.mValidPolicy, policyNodeImpl2.mQualifierSet, policyNodeImpl2.mCriticalityIndicator, policyNodeImpl2.mExpectedPolicySet, false);
    }

    @Override // java.security.cert.PolicyNode
    public PolicyNode getParent() {
        return this.mParent;
    }

    @Override // java.security.cert.PolicyNode
    public Iterator<PolicyNodeImpl> getChildren() {
        return Collections.unmodifiableSet(this.mChildren).iterator();
    }

    @Override // java.security.cert.PolicyNode
    public int getDepth() {
        return this.mDepth;
    }

    @Override // java.security.cert.PolicyNode
    public String getValidPolicy() {
        return this.mValidPolicy;
    }

    @Override // java.security.cert.PolicyNode
    public Set<PolicyQualifierInfo> getPolicyQualifiers() {
        return Collections.unmodifiableSet(this.mQualifierSet);
    }

    @Override // java.security.cert.PolicyNode
    public Set<String> getExpectedPolicies() {
        return Collections.unmodifiableSet(this.mExpectedPolicySet);
    }

    @Override // java.security.cert.PolicyNode
    public boolean isCritical() {
        return this.mCriticalityIndicator;
    }

    public String toString() {
        StringBuilder sb = new StringBuilder(asString());
        Iterator<PolicyNodeImpl> it = this.mChildren.iterator();
        while (it.hasNext()) {
            sb.append((Object) it.next());
        }
        return sb.toString();
    }

    boolean isImmutable() {
        return this.isImmutable;
    }

    void setImmutable() {
        if (this.isImmutable) {
            return;
        }
        Iterator<PolicyNodeImpl> it = this.mChildren.iterator();
        while (it.hasNext()) {
            it.next().setImmutable();
        }
        this.isImmutable = true;
    }

    private void addChild(PolicyNodeImpl policyNodeImpl) {
        if (this.isImmutable) {
            throw new IllegalStateException("PolicyNode is immutable");
        }
        this.mChildren.add(policyNodeImpl);
    }

    void addExpectedPolicy(String str) {
        if (this.isImmutable) {
            throw new IllegalStateException("PolicyNode is immutable");
        }
        if (this.mOriginalExpectedPolicySet) {
            this.mExpectedPolicySet.clear();
            this.mOriginalExpectedPolicySet = false;
        }
        this.mExpectedPolicySet.add(str);
    }

    void prune(int i2) {
        if (this.isImmutable) {
            throw new IllegalStateException("PolicyNode is immutable");
        }
        if (this.mChildren.size() == 0) {
            return;
        }
        Iterator<PolicyNodeImpl> it = this.mChildren.iterator();
        while (it.hasNext()) {
            PolicyNodeImpl next = it.next();
            next.prune(i2);
            if (next.mChildren.size() == 0 && i2 > this.mDepth + 1) {
                it.remove();
            }
        }
    }

    void deleteChild(PolicyNode policyNode) {
        if (this.isImmutable) {
            throw new IllegalStateException("PolicyNode is immutable");
        }
        this.mChildren.remove(policyNode);
    }

    PolicyNodeImpl copyTree() {
        return copyTree(null);
    }

    private PolicyNodeImpl copyTree(PolicyNodeImpl policyNodeImpl) {
        PolicyNodeImpl policyNodeImpl2 = new PolicyNodeImpl(policyNodeImpl, this);
        Iterator<PolicyNodeImpl> it = this.mChildren.iterator();
        while (it.hasNext()) {
            it.next().copyTree(policyNodeImpl2);
        }
        return policyNodeImpl2;
    }

    Set<PolicyNodeImpl> getPolicyNodes(int i2) {
        HashSet hashSet = new HashSet();
        getPolicyNodes(i2, hashSet);
        return hashSet;
    }

    private void getPolicyNodes(int i2, Set<PolicyNodeImpl> set) {
        if (this.mDepth == i2) {
            set.add(this);
            return;
        }
        Iterator<PolicyNodeImpl> it = this.mChildren.iterator();
        while (it.hasNext()) {
            it.next().getPolicyNodes(i2, set);
        }
    }

    Set<PolicyNodeImpl> getPolicyNodesExpected(int i2, String str, boolean z2) {
        if (str.equals(ANY_POLICY)) {
            return getPolicyNodes(i2);
        }
        return getPolicyNodesExpectedHelper(i2, str, z2);
    }

    private Set<PolicyNodeImpl> getPolicyNodesExpectedHelper(int i2, String str, boolean z2) {
        HashSet hashSet = new HashSet();
        if (this.mDepth < i2) {
            Iterator<PolicyNodeImpl> it = this.mChildren.iterator();
            while (it.hasNext()) {
                hashSet.addAll(it.next().getPolicyNodesExpectedHelper(i2, str, z2));
            }
        } else if (z2) {
            if (this.mExpectedPolicySet.contains(ANY_POLICY)) {
                hashSet.add(this);
            }
        } else if (this.mExpectedPolicySet.contains(str)) {
            hashSet.add(this);
        }
        return hashSet;
    }

    Set<PolicyNodeImpl> getPolicyNodesValid(int i2, String str) {
        HashSet hashSet = new HashSet();
        if (this.mDepth < i2) {
            Iterator<PolicyNodeImpl> it = this.mChildren.iterator();
            while (it.hasNext()) {
                hashSet.addAll(it.next().getPolicyNodesValid(i2, str));
            }
        } else if (this.mValidPolicy.equals(str)) {
            hashSet.add(this);
        }
        return hashSet;
    }

    private static String policyToString(String str) {
        if (str.equals(ANY_POLICY)) {
            return "anyPolicy";
        }
        return str;
    }

    String asString() {
        if (this.mParent == null) {
            return "anyPolicy  ROOT\n";
        }
        StringBuilder sb = new StringBuilder();
        int depth = getDepth();
        for (int i2 = 0; i2 < depth; i2++) {
            sb.append(Constants.INDENT);
        }
        sb.append(policyToString(getValidPolicy()));
        sb.append("  CRIT: ");
        sb.append(isCritical());
        sb.append("  EP: ");
        Iterator<String> it = getExpectedPolicies().iterator();
        while (it.hasNext()) {
            sb.append(policyToString(it.next()));
            sb.append(" ");
        }
        sb.append(" (");
        sb.append(getDepth());
        sb.append(")\n");
        return sb.toString();
    }
}
