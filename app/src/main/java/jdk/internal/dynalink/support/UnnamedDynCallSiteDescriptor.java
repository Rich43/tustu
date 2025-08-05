package jdk.internal.dynalink.support;

import java.lang.invoke.MethodType;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: nashorn.jar:jdk/internal/dynalink/support/UnnamedDynCallSiteDescriptor.class */
class UnnamedDynCallSiteDescriptor extends AbstractCallSiteDescriptor {
    private final MethodType methodType;
    private final String op;

    UnnamedDynCallSiteDescriptor(String op, MethodType methodType) {
        this.op = op;
        this.methodType = methodType;
    }

    @Override // jdk.internal.dynalink.CallSiteDescriptor
    public int getNameTokenCount() {
        return 2;
    }

    String getOp() {
        return this.op;
    }

    @Override // jdk.internal.dynalink.CallSiteDescriptor
    public String getNameToken(int i2) {
        switch (i2) {
            case 0:
                return "dyn";
            case 1:
                return this.op;
            default:
                throw new IndexOutOfBoundsException(String.valueOf(i2));
        }
    }

    @Override // jdk.internal.dynalink.CallSiteDescriptor
    public MethodType getMethodType() {
        return this.methodType;
    }

    @Override // jdk.internal.dynalink.CallSiteDescriptor
    public CallSiteDescriptor changeMethodType(MethodType newMethodType) {
        return CallSiteDescriptorFactory.getCanonicalPublicDescriptor(new UnnamedDynCallSiteDescriptor(this.op, newMethodType));
    }
}
