package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;

/* loaded from: nashorn.jar:jdk/internal/dynalink/beans/SimpleDynamicMethod.class */
class SimpleDynamicMethod extends SingleDynamicMethod {
    private final MethodHandle target;
    private final boolean constructor;

    SimpleDynamicMethod(MethodHandle target, Class<?> clazz, String name) {
        this(target, clazz, name, false);
    }

    SimpleDynamicMethod(MethodHandle target, Class<?> clazz, String name, boolean constructor) {
        super(getName(target, clazz, name, constructor));
        this.target = target;
        this.constructor = constructor;
    }

    private static String getName(MethodHandle target, Class<?> clazz, String name, boolean constructor) {
        return getMethodNameWithSignature(target.type(), constructor ? name : getClassAndMethodName(clazz, name), !constructor);
    }

    @Override // jdk.internal.dynalink.beans.SingleDynamicMethod
    boolean isVarArgs() {
        return this.target.isVarargsCollector();
    }

    @Override // jdk.internal.dynalink.beans.SingleDynamicMethod
    MethodType getMethodType() {
        return this.target.type();
    }

    @Override // jdk.internal.dynalink.beans.SingleDynamicMethod
    MethodHandle getTarget(MethodHandles.Lookup lookup) {
        return this.target;
    }

    @Override // jdk.internal.dynalink.beans.DynamicMethod
    boolean isConstructor() {
        return this.constructor;
    }
}
