package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.text.Collator;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Iterator;
import java.util.LinkedList;
import java.util.List;
import jdk.internal.dynalink.CallSiteDescriptor;
import jdk.internal.dynalink.beans.ApplicableOverloadedMethods;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.support.TypeUtilities;

/* loaded from: nashorn.jar:jdk/internal/dynalink/beans/OverloadedDynamicMethod.class */
class OverloadedDynamicMethod extends DynamicMethod {
    private final LinkedList<SingleDynamicMethod> methods;
    private final ClassLoader classLoader;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !OverloadedDynamicMethod.class.desiredAssertionStatus();
    }

    OverloadedDynamicMethod(Class<?> clazz, String name) {
        this(new LinkedList(), clazz.getClassLoader(), getClassAndMethodName(clazz, name));
    }

    private OverloadedDynamicMethod(LinkedList<SingleDynamicMethod> methods, ClassLoader classLoader, String name) {
        super(name);
        this.methods = methods;
        this.classLoader = classLoader;
    }

    @Override // jdk.internal.dynalink.beans.DynamicMethod
    SingleDynamicMethod getMethodForExactParamTypes(String paramTypes) {
        LinkedList<SingleDynamicMethod> matchingMethods = new LinkedList<>();
        Iterator<SingleDynamicMethod> it = this.methods.iterator();
        while (it.hasNext()) {
            SingleDynamicMethod method = it.next();
            SingleDynamicMethod matchingMethod = method.getMethodForExactParamTypes(paramTypes);
            if (matchingMethod != null) {
                matchingMethods.add(matchingMethod);
            }
        }
        switch (matchingMethods.size()) {
            case 0:
                return null;
            case 1:
                return matchingMethods.getFirst();
            default:
                throw new BootstrapMethodError("Can't choose among " + ((Object) matchingMethods) + " for argument types " + paramTypes + " for method " + getName());
        }
    }

    @Override // jdk.internal.dynalink.beans.DynamicMethod
    public MethodHandle getInvocation(CallSiteDescriptor callSiteDescriptor, LinkerServices linkerServices) {
        MethodType callSiteType = callSiteDescriptor.getMethodType();
        ApplicableOverloadedMethods subtypingApplicables = getApplicables(callSiteType, ApplicableOverloadedMethods.APPLICABLE_BY_SUBTYPING);
        ApplicableOverloadedMethods methodInvocationApplicables = getApplicables(callSiteType, ApplicableOverloadedMethods.APPLICABLE_BY_METHOD_INVOCATION_CONVERSION);
        ApplicableOverloadedMethods variableArityApplicables = getApplicables(callSiteType, ApplicableOverloadedMethods.APPLICABLE_BY_VARIABLE_ARITY);
        List<SingleDynamicMethod> maximallySpecifics = subtypingApplicables.findMaximallySpecificMethods();
        if (maximallySpecifics.isEmpty()) {
            maximallySpecifics = methodInvocationApplicables.findMaximallySpecificMethods();
            if (maximallySpecifics.isEmpty()) {
                maximallySpecifics = variableArityApplicables.findMaximallySpecificMethods();
            }
        }
        List<SingleDynamicMethod> invokables = (List) this.methods.clone();
        invokables.removeAll(subtypingApplicables.getMethods());
        invokables.removeAll(methodInvocationApplicables.getMethods());
        invokables.removeAll(variableArityApplicables.getMethods());
        Iterator<SingleDynamicMethod> it = invokables.iterator();
        while (it.hasNext()) {
            SingleDynamicMethod m2 = it.next();
            if (!isApplicableDynamically(linkerServices, callSiteType, m2)) {
                it.remove();
            }
        }
        if (invokables.isEmpty() && maximallySpecifics.size() > 1) {
            throw new BootstrapMethodError("Can't choose among " + ((Object) maximallySpecifics) + " for argument types " + ((Object) callSiteType));
        }
        invokables.addAll(maximallySpecifics);
        switch (invokables.size()) {
            case 0:
                return null;
            case 1:
                return invokables.iterator().next().getInvocation(callSiteDescriptor, linkerServices);
            default:
                List<MethodHandle> methodHandles = new ArrayList<>(invokables.size());
                MethodHandles.Lookup lookup = callSiteDescriptor.getLookup();
                for (SingleDynamicMethod method : invokables) {
                    methodHandles.add(method.getTarget(lookup));
                }
                return new OverloadedMethod(methodHandles, this, callSiteType, linkerServices).getInvoker();
        }
    }

    @Override // jdk.internal.dynalink.beans.DynamicMethod
    public boolean contains(SingleDynamicMethod m2) {
        Iterator<SingleDynamicMethod> it = this.methods.iterator();
        while (it.hasNext()) {
            SingleDynamicMethod method = it.next();
            if (method.contains(m2)) {
                return true;
            }
        }
        return false;
    }

    @Override // jdk.internal.dynalink.beans.DynamicMethod
    public boolean isConstructor() {
        if ($assertionsDisabled || !this.methods.isEmpty()) {
            return this.methods.getFirst().isConstructor();
        }
        throw new AssertionError();
    }

    @Override // jdk.internal.dynalink.beans.DynamicMethod
    public String toString() {
        List<String> names = new ArrayList<>(this.methods.size());
        int len = 0;
        Iterator<SingleDynamicMethod> it = this.methods.iterator();
        while (it.hasNext()) {
            SingleDynamicMethod m2 = it.next();
            String name = m2.getName();
            len += name.length();
            names.add(name);
        }
        Collator collator = Collator.getInstance();
        collator.setStrength(1);
        Collections.sort(names, collator);
        String className = getClass().getName();
        int totalLength = className.length() + len + (2 * names.size()) + 3;
        StringBuilder b2 = new StringBuilder(totalLength);
        b2.append('[').append(className).append('\n');
        Iterator<String> it2 = names.iterator();
        while (it2.hasNext()) {
            b2.append(' ').append(it2.next()).append('\n');
        }
        b2.append(']');
        if ($assertionsDisabled || b2.length() == totalLength) {
            return b2.toString();
        }
        throw new AssertionError();
    }

    ClassLoader getClassLoader() {
        return this.classLoader;
    }

    private static boolean isApplicableDynamically(LinkerServices linkerServices, MethodType callSiteType, SingleDynamicMethod m2) {
        MethodType methodType = m2.getMethodType();
        boolean varArgs = m2.isVarArgs();
        int fixedArgLen = methodType.parameterCount() - (varArgs ? 1 : 0);
        int callSiteArgLen = callSiteType.parameterCount();
        if (varArgs) {
            if (callSiteArgLen < fixedArgLen) {
                return false;
            }
        } else if (callSiteArgLen != fixedArgLen) {
            return false;
        }
        for (int i2 = 1; i2 < fixedArgLen; i2++) {
            if (!isApplicableDynamically(linkerServices, callSiteType.parameterType(i2), methodType.parameterType(i2))) {
                return false;
            }
        }
        if (!varArgs) {
            return true;
        }
        Class<?> varArgArrayType = methodType.parameterType(fixedArgLen);
        Class<?> varArgType = varArgArrayType.getComponentType();
        if (fixedArgLen == callSiteArgLen - 1) {
            Class<?> callSiteArgType = callSiteType.parameterType(fixedArgLen);
            return isApplicableDynamically(linkerServices, callSiteArgType, varArgArrayType) || isApplicableDynamically(linkerServices, callSiteArgType, varArgType);
        }
        for (int i3 = fixedArgLen; i3 < callSiteArgLen; i3++) {
            if (!isApplicableDynamically(linkerServices, callSiteType.parameterType(i3), varArgType)) {
                return false;
            }
        }
        return true;
    }

    private static boolean isApplicableDynamically(LinkerServices linkerServices, Class<?> callSiteType, Class<?> methodType) {
        return TypeUtilities.isPotentiallyConvertible(callSiteType, methodType) || linkerServices.canConvert(callSiteType, methodType);
    }

    private ApplicableOverloadedMethods getApplicables(MethodType callSiteType, ApplicableOverloadedMethods.ApplicabilityTest test) {
        return new ApplicableOverloadedMethods(this.methods, callSiteType, test);
    }

    public void addMethod(SingleDynamicMethod method) {
        if (!$assertionsDisabled && !constructorFlagConsistent(method)) {
            throw new AssertionError();
        }
        this.methods.add(method);
    }

    private boolean constructorFlagConsistent(SingleDynamicMethod method) {
        return this.methods.isEmpty() || this.methods.getFirst().isConstructor() == method.isConstructor();
    }
}
