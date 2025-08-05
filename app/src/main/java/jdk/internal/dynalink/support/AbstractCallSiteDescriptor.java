package jdk.internal.dynalink.support;

import java.lang.invoke.MethodHandles;
import java.util.Objects;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: nashorn.jar:jdk/internal/dynalink/support/AbstractCallSiteDescriptor.class */
public abstract class AbstractCallSiteDescriptor implements CallSiteDescriptor {
    @Override // jdk.internal.dynalink.CallSiteDescriptor
    public String getName() {
        return appendName(new StringBuilder(getNameLength())).toString();
    }

    @Override // jdk.internal.dynalink.CallSiteDescriptor
    public MethodHandles.Lookup getLookup() {
        return MethodHandles.publicLookup();
    }

    public boolean equals(Object obj) {
        return (obj instanceof CallSiteDescriptor) && equals((CallSiteDescriptor) obj);
    }

    public boolean equals(CallSiteDescriptor csd) {
        if (csd == null) {
            return false;
        }
        if (csd == this) {
            return true;
        }
        int ntc = getNameTokenCount();
        if (ntc != csd.getNameTokenCount()) {
            return false;
        }
        int i2 = ntc;
        do {
            int i3 = i2;
            i2--;
            if (i3 <= 0) {
                if (!getMethodType().equals((Object) csd.getMethodType())) {
                    return false;
                }
                return lookupsEqual(getLookup(), csd.getLookup());
            }
        } while (Objects.equals(getNameToken(i2), csd.getNameToken(i2)));
        return false;
    }

    public int hashCode() {
        MethodHandles.Lookup lookup = getLookup();
        int h2 = lookup.lookupClass().hashCode() + (31 * lookup.lookupModes());
        int c2 = getNameTokenCount();
        for (int i2 = 0; i2 < c2; i2++) {
            h2 = (h2 * 31) + getNameToken(i2).hashCode();
        }
        return (h2 * 31) + getMethodType().hashCode();
    }

    public String toString() {
        String mt = getMethodType().toString();
        String l2 = getLookup().toString();
        StringBuilder b2 = new StringBuilder(l2.length() + 1 + mt.length() + getNameLength());
        return appendName(b2).append(mt).append("@").append(l2).toString();
    }

    private int getNameLength() {
        int c2 = getNameTokenCount();
        int l2 = 0;
        for (int i2 = 0; i2 < c2; i2++) {
            l2 += getNameToken(i2).length();
        }
        return (l2 + c2) - 1;
    }

    private StringBuilder appendName(StringBuilder b2) {
        b2.append(getNameToken(0));
        int c2 = getNameTokenCount();
        for (int i2 = 1; i2 < c2; i2++) {
            b2.append(':').append(getNameToken(i2));
        }
        return b2;
    }

    private static boolean lookupsEqual(MethodHandles.Lookup l1, MethodHandles.Lookup l2) {
        if (l1 == l2) {
            return true;
        }
        return l1.lookupClass() == l2.lookupClass() && l1.lookupModes() == l2.lookupModes();
    }
}
