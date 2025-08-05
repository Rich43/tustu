package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodType;
import java.util.LinkedList;
import java.util.List;
import jdk.internal.dynalink.support.TypeUtilities;

/* loaded from: nashorn.jar:jdk/internal/dynalink/beans/ApplicableOverloadedMethods.class */
class ApplicableOverloadedMethods {
    private final List<SingleDynamicMethod> methods = new LinkedList();
    private final boolean varArgs;
    static final ApplicabilityTest APPLICABLE_BY_SUBTYPING = new ApplicabilityTest() { // from class: jdk.internal.dynalink.beans.ApplicableOverloadedMethods.1
        @Override // jdk.internal.dynalink.beans.ApplicableOverloadedMethods.ApplicabilityTest
        boolean isApplicable(MethodType callSiteType, SingleDynamicMethod method) {
            MethodType methodType = method.getMethodType();
            int methodArity = methodType.parameterCount();
            if (methodArity != callSiteType.parameterCount()) {
                return false;
            }
            for (int i2 = 1; i2 < methodArity; i2++) {
                if (!TypeUtilities.isSubtype(callSiteType.parameterType(i2), methodType.parameterType(i2))) {
                    return false;
                }
            }
            return true;
        }
    };
    static final ApplicabilityTest APPLICABLE_BY_METHOD_INVOCATION_CONVERSION = new ApplicabilityTest() { // from class: jdk.internal.dynalink.beans.ApplicableOverloadedMethods.2
        @Override // jdk.internal.dynalink.beans.ApplicableOverloadedMethods.ApplicabilityTest
        boolean isApplicable(MethodType callSiteType, SingleDynamicMethod method) {
            MethodType methodType = method.getMethodType();
            int methodArity = methodType.parameterCount();
            if (methodArity != callSiteType.parameterCount()) {
                return false;
            }
            for (int i2 = 1; i2 < methodArity; i2++) {
                if (!TypeUtilities.isMethodInvocationConvertible(callSiteType.parameterType(i2), methodType.parameterType(i2))) {
                    return false;
                }
            }
            return true;
        }
    };
    static final ApplicabilityTest APPLICABLE_BY_VARIABLE_ARITY = new ApplicabilityTest() { // from class: jdk.internal.dynalink.beans.ApplicableOverloadedMethods.3
        @Override // jdk.internal.dynalink.beans.ApplicableOverloadedMethods.ApplicabilityTest
        boolean isApplicable(MethodType callSiteType, SingleDynamicMethod method) {
            if (!method.isVarArgs()) {
                return false;
            }
            MethodType methodType = method.getMethodType();
            int methodArity = methodType.parameterCount();
            int fixArity = methodArity - 1;
            int callSiteArity = callSiteType.parameterCount();
            if (fixArity > callSiteArity) {
                return false;
            }
            for (int i2 = 1; i2 < fixArity; i2++) {
                if (!TypeUtilities.isMethodInvocationConvertible(callSiteType.parameterType(i2), methodType.parameterType(i2))) {
                    return false;
                }
            }
            Class<?> varArgType = methodType.parameterType(fixArity).getComponentType();
            for (int i3 = fixArity; i3 < callSiteArity; i3++) {
                if (!TypeUtilities.isMethodInvocationConvertible(callSiteType.parameterType(i3), varArgType)) {
                    return false;
                }
            }
            return true;
        }
    };

    ApplicableOverloadedMethods(List<SingleDynamicMethod> methods, MethodType callSiteType, ApplicabilityTest test) {
        for (SingleDynamicMethod m2 : methods) {
            if (test.isApplicable(callSiteType, m2)) {
                this.methods.add(m2);
            }
        }
        this.varArgs = test == APPLICABLE_BY_VARIABLE_ARITY;
    }

    List<SingleDynamicMethod> getMethods() {
        return this.methods;
    }

    List<SingleDynamicMethod> findMaximallySpecificMethods() {
        return MaximallySpecific.getMaximallySpecificMethods(this.methods, this.varArgs);
    }

    /* loaded from: nashorn.jar:jdk/internal/dynalink/beans/ApplicableOverloadedMethods$ApplicabilityTest.class */
    static abstract class ApplicabilityTest {
        abstract boolean isApplicable(MethodType methodType, SingleDynamicMethod singleDynamicMethod);

        ApplicabilityTest() {
        }
    }
}
