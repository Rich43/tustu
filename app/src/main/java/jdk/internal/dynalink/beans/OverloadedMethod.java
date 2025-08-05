package jdk.internal.dynalink.beans;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.concurrent.ConcurrentHashMap;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.support.Lookup;
import jdk.internal.dynalink.support.TypeUtilities;

/* loaded from: nashorn.jar:jdk/internal/dynalink/beans/OverloadedMethod.class */
class OverloadedMethod {
    private final Map<ClassString, MethodHandle> argTypesToMethods = new ConcurrentHashMap();
    private final OverloadedDynamicMethod parent;
    private final MethodType callSiteType;
    private final MethodHandle invoker;
    private final LinkerServices linkerServices;
    private final ArrayList<MethodHandle> fixArgMethods;
    private final ArrayList<MethodHandle> varArgMethods;
    private static final MethodHandle SELECT_METHOD = Lookup.findOwnSpecial(MethodHandles.lookup(), "selectMethod", MethodHandle.class, Object[].class);
    private static final MethodHandle THROW_NO_SUCH_METHOD = Lookup.findOwnSpecial(MethodHandles.lookup(), "throwNoSuchMethod", Void.TYPE, Class[].class);
    private static final MethodHandle THROW_AMBIGUOUS_METHOD = Lookup.findOwnSpecial(MethodHandles.lookup(), "throwAmbiguousMethod", Void.TYPE, Class[].class, List.class);

    OverloadedMethod(List<MethodHandle> methodHandles, OverloadedDynamicMethod parent, MethodType callSiteType, LinkerServices linkerServices) {
        this.parent = parent;
        Class<?> commonRetType = getCommonReturnType(methodHandles);
        this.callSiteType = callSiteType.changeReturnType(commonRetType);
        this.linkerServices = linkerServices;
        this.fixArgMethods = new ArrayList<>(methodHandles.size());
        this.varArgMethods = new ArrayList<>(methodHandles.size());
        int argNum = callSiteType.parameterCount();
        for (MethodHandle mh : methodHandles) {
            if (mh.isVarargsCollector()) {
                MethodHandle asFixed = mh.asFixedArity();
                if (argNum == asFixed.type().parameterCount()) {
                    this.fixArgMethods.add(asFixed);
                }
                this.varArgMethods.add(mh);
            } else {
                this.fixArgMethods.add(mh);
            }
        }
        this.fixArgMethods.trimToSize();
        this.varArgMethods.trimToSize();
        MethodHandle bound = SELECT_METHOD.bindTo(this);
        MethodHandle collecting = SingleDynamicMethod.collectArguments(bound, argNum).asType(callSiteType.changeReturnType(MethodHandle.class));
        this.invoker = linkerServices.asTypeLosslessReturn(MethodHandles.foldArguments(MethodHandles.exactInvoker(this.callSiteType), collecting), callSiteType);
    }

    MethodHandle getInvoker() {
        return this.invoker;
    }

    private MethodHandle selectMethod(Object[] args) throws NoSuchMethodException, RuntimeException {
        Class<?>[] argTypes = new Class[args.length];
        for (int i2 = 0; i2 < argTypes.length; i2++) {
            Object arg = args[i2];
            argTypes[i2] = arg == null ? ClassString.NULL_CLASS : arg.getClass();
        }
        ClassString classString = new ClassString(argTypes);
        MethodHandle method = this.argTypesToMethods.get(classString);
        if (method == null) {
            List<MethodHandle> methods = classString.getMaximallySpecifics(this.fixArgMethods, this.linkerServices, false);
            if (methods.isEmpty()) {
                methods = classString.getMaximallySpecifics(this.varArgMethods, this.linkerServices, true);
            }
            switch (methods.size()) {
                case 0:
                    method = getNoSuchMethodThrower(argTypes);
                    break;
                case 1:
                    method = SingleDynamicMethod.getInvocation(methods.get(0), this.callSiteType, this.linkerServices);
                    break;
                default:
                    method = getAmbiguousMethodThrower(argTypes, methods);
                    break;
            }
            if (classString.isVisibleFrom(this.parent.getClassLoader())) {
                this.argTypesToMethods.put(classString, method);
            }
        }
        return method;
    }

    private MethodHandle getNoSuchMethodThrower(Class<?>[] argTypes) {
        return adaptThrower(MethodHandles.insertArguments(THROW_NO_SUCH_METHOD, 0, this, argTypes));
    }

    private void throwNoSuchMethod(Class<?>[] argTypes) throws NoSuchMethodException {
        if (this.varArgMethods.isEmpty()) {
            throw new NoSuchMethodException("None of the fixed arity signatures " + getSignatureList(this.fixArgMethods) + " of method " + this.parent.getName() + " match the argument types " + argTypesString(argTypes));
        }
        throw new NoSuchMethodException("None of the fixed arity signatures " + getSignatureList(this.fixArgMethods) + " or the variable arity signatures " + getSignatureList(this.varArgMethods) + " of the method " + this.parent.getName() + " match the argument types " + argTypesString(argTypes));
    }

    private MethodHandle getAmbiguousMethodThrower(Class<?>[] argTypes, List<MethodHandle> methods) {
        return adaptThrower(MethodHandles.insertArguments(THROW_AMBIGUOUS_METHOD, 0, this, argTypes, methods));
    }

    private MethodHandle adaptThrower(MethodHandle rawThrower) {
        return MethodHandles.dropArguments(rawThrower, 0, this.callSiteType.parameterList()).asType(this.callSiteType);
    }

    private void throwAmbiguousMethod(Class<?>[] argTypes, List<MethodHandle> methods) throws NoSuchMethodException {
        String arity = methods.get(0).isVarargsCollector() ? Constants.ELEMNAME_VARIABLE_STRING : "fixed";
        throw new NoSuchMethodException("Can't unambiguously select between " + arity + " arity signatures " + getSignatureList(methods) + " of the method " + this.parent.getName() + " for argument types " + argTypesString(argTypes));
    }

    private static String argTypesString(Class<?>[] classes) {
        StringBuilder b2 = new StringBuilder().append('[');
        appendTypes(b2, classes, false);
        return b2.append(']').toString();
    }

    private static String getSignatureList(List<MethodHandle> methods) {
        StringBuilder b2 = new StringBuilder().append('[');
        Iterator<MethodHandle> it = methods.iterator();
        if (it.hasNext()) {
            appendSig(b2, it.next());
            while (it.hasNext()) {
                appendSig(b2.append(", "), it.next());
            }
        }
        return b2.append(']').toString();
    }

    private static void appendSig(StringBuilder b2, MethodHandle m2) {
        b2.append('(');
        appendTypes(b2, m2.type().parameterArray(), m2.isVarargsCollector());
        b2.append(')');
    }

    private static void appendTypes(StringBuilder b2, Class<?>[] classes, boolean varArg) {
        int l2 = classes.length;
        if (!varArg) {
            if (l2 > 1) {
                b2.append(classes[1].getCanonicalName());
                for (int i2 = 2; i2 < l2; i2++) {
                    b2.append(", ").append(classes[i2].getCanonicalName());
                }
                return;
            }
            return;
        }
        for (int i3 = 1; i3 < l2 - 1; i3++) {
            b2.append(classes[i3].getCanonicalName()).append(", ");
        }
        b2.append(classes[l2 - 1].getComponentType().getCanonicalName()).append("...");
    }

    private static Class<?> getCommonReturnType(List<MethodHandle> methodHandles) {
        Iterator<MethodHandle> it = methodHandles.iterator();
        Class<?> clsReturnType = it.next().type().returnType();
        while (true) {
            Class<?> retType = clsReturnType;
            if (it.hasNext()) {
                clsReturnType = TypeUtilities.getCommonLosslessConversionType(retType, it.next().type().returnType());
            } else {
                return retType;
            }
        }
    }
}
