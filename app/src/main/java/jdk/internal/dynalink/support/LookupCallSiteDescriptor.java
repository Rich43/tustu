package jdk.internal.dynalink.support;

import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: nashorn.jar:jdk/internal/dynalink/support/LookupCallSiteDescriptor.class */
class LookupCallSiteDescriptor extends DefaultCallSiteDescriptor {
    private final MethodHandles.Lookup lookup;

    LookupCallSiteDescriptor(String[] tokenizedName, MethodType methodType, MethodHandles.Lookup lookup) {
        super(tokenizedName, methodType);
        this.lookup = lookup;
    }

    @Override // jdk.internal.dynalink.support.AbstractCallSiteDescriptor, jdk.internal.dynalink.CallSiteDescriptor
    public MethodHandles.Lookup getLookup() {
        return this.lookup;
    }

    @Override // jdk.internal.dynalink.support.DefaultCallSiteDescriptor, jdk.internal.dynalink.CallSiteDescriptor
    public CallSiteDescriptor changeMethodType(MethodType newMethodType) {
        return new LookupCallSiteDescriptor(getTokenizedName(), newMethodType, this.lookup);
    }
}
