package jdk.internal.dynalink.support;

import java.lang.invoke.MethodType;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: nashorn.jar:jdk/internal/dynalink/support/NamedDynCallSiteDescriptor.class */
class NamedDynCallSiteDescriptor extends UnnamedDynCallSiteDescriptor {
    private final String name;

    NamedDynCallSiteDescriptor(String op, String name, MethodType methodType) {
        super(op, methodType);
        this.name = name;
    }

    @Override // jdk.internal.dynalink.support.UnnamedDynCallSiteDescriptor, jdk.internal.dynalink.CallSiteDescriptor
    public int getNameTokenCount() {
        return 3;
    }

    @Override // jdk.internal.dynalink.support.UnnamedDynCallSiteDescriptor, jdk.internal.dynalink.CallSiteDescriptor
    public String getNameToken(int i2) {
        switch (i2) {
            case 0:
                return "dyn";
            case 1:
                return getOp();
            case 2:
                return this.name;
            default:
                throw new IndexOutOfBoundsException(String.valueOf(i2));
        }
    }

    @Override // jdk.internal.dynalink.support.UnnamedDynCallSiteDescriptor, jdk.internal.dynalink.CallSiteDescriptor
    public CallSiteDescriptor changeMethodType(MethodType newMethodType) {
        return CallSiteDescriptorFactory.getCanonicalPublicDescriptor(new NamedDynCallSiteDescriptor(getOp(), this.name, newMethodType));
    }
}
