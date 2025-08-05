package jdk.internal.dynalink.support;

import java.lang.invoke.MethodType;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: nashorn.jar:jdk/internal/dynalink/support/DefaultCallSiteDescriptor.class */
class DefaultCallSiteDescriptor extends AbstractCallSiteDescriptor {
    private final String[] tokenizedName;
    private final MethodType methodType;

    DefaultCallSiteDescriptor(String[] tokenizedName, MethodType methodType) {
        this.tokenizedName = tokenizedName;
        this.methodType = methodType;
    }

    @Override // jdk.internal.dynalink.CallSiteDescriptor
    public int getNameTokenCount() {
        return this.tokenizedName.length;
    }

    @Override // jdk.internal.dynalink.CallSiteDescriptor
    public String getNameToken(int i2) {
        try {
            return this.tokenizedName[i2];
        } catch (ArrayIndexOutOfBoundsException e2) {
            throw new IllegalArgumentException(e2.getMessage());
        }
    }

    String[] getTokenizedName() {
        return this.tokenizedName;
    }

    @Override // jdk.internal.dynalink.CallSiteDescriptor
    public MethodType getMethodType() {
        return this.methodType;
    }

    @Override // jdk.internal.dynalink.CallSiteDescriptor
    public CallSiteDescriptor changeMethodType(MethodType newMethodType) {
        return CallSiteDescriptorFactory.getCanonicalPublicDescriptor(new DefaultCallSiteDescriptor(this.tokenizedName, newMethodType));
    }
}
