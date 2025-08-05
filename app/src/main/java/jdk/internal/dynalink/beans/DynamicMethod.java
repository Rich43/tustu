package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.linker.LinkerServices;

/* loaded from: nashorn.jar:jdk/internal/dynalink/beans/DynamicMethod.class */
abstract class DynamicMethod {
    private final String name;

    abstract MethodHandle getInvocation(CallSiteDescriptor callSiteDescriptor, LinkerServices linkerServices);

    abstract SingleDynamicMethod getMethodForExactParamTypes(String str);

    abstract boolean contains(SingleDynamicMethod singleDynamicMethod);

    DynamicMethod(String name) {
        this.name = name;
    }

    String getName() {
        return this.name;
    }

    static String getClassAndMethodName(Class<?> clazz, String name) throws SecurityException {
        String clazzName = clazz.getCanonicalName();
        return (clazzName == null ? clazz.getName() : clazzName) + "." + name;
    }

    public String toString() {
        return "[" + getClass().getName() + " " + getName() + "]";
    }

    boolean isConstructor() {
        return false;
    }
}
