package com.sun.corba.se.impl.oa.poa;

import com.sun.corba.se.impl.orbutil.ORBConstants;
import com.sun.corba.se.spi.extension.CopyObjectPolicy;
import com.sun.corba.se.spi.extension.ServantCachingPolicy;
import com.sun.corba.se.spi.extension.ZeroPortPolicy;
import java.util.BitSet;
import java.util.HashMap;
import java.util.Iterator;
import org.omg.CORBA.Policy;
import org.omg.PortableServer.IdAssignmentPolicy;
import org.omg.PortableServer.IdUniquenessPolicy;
import org.omg.PortableServer.ImplicitActivationPolicy;
import org.omg.PortableServer.LifespanPolicy;
import org.omg.PortableServer.POAPackage.InvalidPolicy;
import org.omg.PortableServer.RequestProcessingPolicy;
import org.omg.PortableServer.ServantRetentionPolicy;
import org.omg.PortableServer.ThreadPolicy;

/* loaded from: rt.jar:com/sun/corba/se/impl/oa/poa/Policies.class */
public final class Policies {
    private static final int MIN_POA_POLICY_ID = 16;
    private static final int MAX_POA_POLICY_ID = 22;
    private static final int POLICY_TABLE_SIZE = 7;
    int defaultObjectCopierFactoryId;
    private HashMap policyMap;
    public static final Policies defaultPolicies = new Policies();
    public static final Policies rootPOAPolicies = new Policies(0, 0, 0, 1, 0, 0, 0);
    private int[] poaPolicyValues;

    private int getPolicyValue(int i2) {
        return this.poaPolicyValues[i2 - 16];
    }

    private void setPolicyValue(int i2, int i3) {
        this.poaPolicyValues[i2 - 16] = i3;
    }

    private Policies(int i2, int i3, int i4, int i5, int i6, int i7, int i8) {
        this.policyMap = new HashMap();
        this.poaPolicyValues = new int[]{i2, i3, i4, i5, i6, i7, i8};
    }

    private Policies() {
        this(0, 0, 0, 1, 1, 0, 0);
    }

    public String toString() {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("Policies[");
        boolean z2 = true;
        Iterator it = this.policyMap.values().iterator();
        while (it.hasNext()) {
            if (z2) {
                z2 = false;
            } else {
                stringBuffer.append(",");
            }
            stringBuffer.append(it.next().toString());
        }
        stringBuffer.append("]");
        return stringBuffer.toString();
    }

    private int getPOAPolicyValue(Policy policy) {
        if (policy instanceof ThreadPolicy) {
            return ((ThreadPolicy) policy).value().value();
        }
        if (policy instanceof LifespanPolicy) {
            return ((LifespanPolicy) policy).value().value();
        }
        if (policy instanceof IdUniquenessPolicy) {
            return ((IdUniquenessPolicy) policy).value().value();
        }
        if (policy instanceof IdAssignmentPolicy) {
            return ((IdAssignmentPolicy) policy).value().value();
        }
        if (policy instanceof ServantRetentionPolicy) {
            return ((ServantRetentionPolicy) policy).value().value();
        }
        if (policy instanceof RequestProcessingPolicy) {
            return ((RequestProcessingPolicy) policy).value().value();
        }
        if (policy instanceof ImplicitActivationPolicy) {
            return ((ImplicitActivationPolicy) policy).value().value();
        }
        return -1;
    }

    private void checkForPolicyError(BitSet bitSet) throws InvalidPolicy {
        short s2 = 0;
        while (true) {
            short s3 = s2;
            if (s3 < bitSet.length()) {
                if (!bitSet.get(s3)) {
                    s2 = (short) (s3 + 1);
                } else {
                    throw new InvalidPolicy(s3);
                }
            } else {
                return;
            }
        }
    }

    private void addToErrorSet(Policy[] policyArr, int i2, BitSet bitSet) {
        for (int i3 = 0; i3 < policyArr.length; i3++) {
            if (policyArr[i3].policy_type() == i2) {
                bitSet.set(i3);
                return;
            }
        }
    }

    Policies(Policy[] policyArr, int i2) throws InvalidPolicy {
        this();
        this.defaultObjectCopierFactoryId = i2;
        if (policyArr == null) {
            return;
        }
        BitSet bitSet = new BitSet(policyArr.length);
        short s2 = 0;
        while (true) {
            short s3 = s2;
            if (s3 >= policyArr.length) {
                break;
            }
            Policy policy = policyArr[s3];
            int pOAPolicyValue = getPOAPolicyValue(policy);
            Integer num = new Integer(policy.policy_type());
            Policy policy2 = (Policy) this.policyMap.get(num);
            if (policy2 == null) {
                this.policyMap.put(num, policy);
            }
            if (pOAPolicyValue >= 0) {
                setPolicyValue(num.intValue(), pOAPolicyValue);
                if (policy2 != null && getPOAPolicyValue(policy2) != pOAPolicyValue) {
                    bitSet.set(s3);
                }
            }
            s2 = (short) (s3 + 1);
        }
        if (!retainServants() && useActiveMapOnly()) {
            addToErrorSet(policyArr, 21, bitSet);
            addToErrorSet(policyArr, 22, bitSet);
        }
        if (isImplicitlyActivated()) {
            if (!retainServants()) {
                addToErrorSet(policyArr, 20, bitSet);
                addToErrorSet(policyArr, 21, bitSet);
            }
            if (!isSystemAssignedIds()) {
                addToErrorSet(policyArr, 20, bitSet);
                addToErrorSet(policyArr, 19, bitSet);
            }
        }
        checkForPolicyError(bitSet);
    }

    public Policy get_effective_policy(int i2) {
        return (Policy) this.policyMap.get(new Integer(i2));
    }

    public final boolean isOrbControlledThreads() {
        return getPolicyValue(16) == 0;
    }

    public final boolean isSingleThreaded() {
        return getPolicyValue(16) == 1;
    }

    public final boolean isTransient() {
        return getPolicyValue(17) == 0;
    }

    public final boolean isPersistent() {
        return getPolicyValue(17) == 1;
    }

    public final boolean isUniqueIds() {
        return getPolicyValue(18) == 0;
    }

    public final boolean isMultipleIds() {
        return getPolicyValue(18) == 1;
    }

    public final boolean isUserAssignedIds() {
        return getPolicyValue(19) == 0;
    }

    public final boolean isSystemAssignedIds() {
        return getPolicyValue(19) == 1;
    }

    public final boolean retainServants() {
        return getPolicyValue(21) == 0;
    }

    public final boolean useActiveMapOnly() {
        return getPolicyValue(22) == 0;
    }

    public final boolean useDefaultServant() {
        return getPolicyValue(22) == 1;
    }

    public final boolean useServantManager() {
        return getPolicyValue(22) == 2;
    }

    public final boolean isImplicitlyActivated() {
        return getPolicyValue(20) == 0;
    }

    public final int servantCachingLevel() {
        ServantCachingPolicy servantCachingPolicy = (ServantCachingPolicy) this.policyMap.get(new Integer(1398079488));
        if (servantCachingPolicy == null) {
            return 0;
        }
        return servantCachingPolicy.getType();
    }

    public final boolean forceZeroPort() {
        ZeroPortPolicy zeroPortPolicy = (ZeroPortPolicy) this.policyMap.get(new Integer(1398079489));
        if (zeroPortPolicy == null) {
            return false;
        }
        return zeroPortPolicy.forceZeroPort();
    }

    public final int getCopierId() {
        CopyObjectPolicy copyObjectPolicy = (CopyObjectPolicy) this.policyMap.get(new Integer(ORBConstants.COPY_OBJECT_POLICY));
        if (copyObjectPolicy != null) {
            return copyObjectPolicy.getValue();
        }
        return this.defaultObjectCopierFactoryId;
    }
}
