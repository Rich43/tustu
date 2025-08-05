package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodType;
import java.util.LinkedList;
import java.util.List;
import jdk.internal.dynalink.linker.LinkerServices;
import jdk.internal.dynalink.support.Guards;
import jdk.internal.dynalink.support.TypeUtilities;

/* loaded from: nashorn.jar:jdk/internal/dynalink/beans/ClassString.class */
final class ClassString {
    static final Class<?> NULL_CLASS = new Object() { // from class: jdk.internal.dynalink.beans.ClassString.1
    }.getClass();
    private final Class<?>[] classes;
    private int hashCode;

    ClassString(Class<?>[] classes) {
        this.classes = classes;
    }

    ClassString(MethodType type) {
        this(type.parameterArray());
    }

    public boolean equals(Object other) {
        if (!(other instanceof ClassString)) {
            return false;
        }
        Class<?>[] otherClasses = ((ClassString) other).classes;
        if (otherClasses.length != this.classes.length) {
            return false;
        }
        for (int i2 = 0; i2 < otherClasses.length; i2++) {
            if (otherClasses[i2] != this.classes[i2]) {
                return false;
            }
        }
        return true;
    }

    public int hashCode() {
        if (this.hashCode == 0) {
            int h2 = 0;
            for (int i2 = 0; i2 < this.classes.length; i2++) {
                h2 ^= this.classes[i2].hashCode();
            }
            this.hashCode = h2;
        }
        return this.hashCode;
    }

    boolean isVisibleFrom(ClassLoader classLoader) {
        for (int i2 = 0; i2 < this.classes.length; i2++) {
            if (!Guards.canReferenceDirectly(classLoader, this.classes[i2].getClassLoader())) {
                return false;
            }
        }
        return true;
    }

    List<MethodHandle> getMaximallySpecifics(List<MethodHandle> methods, LinkerServices linkerServices, boolean varArg) {
        return MaximallySpecific.getMaximallySpecificMethodHandles(getApplicables(methods, linkerServices, varArg), varArg, this.classes, linkerServices);
    }

    LinkedList<MethodHandle> getApplicables(List<MethodHandle> methods, LinkerServices linkerServices, boolean varArg) {
        LinkedList<MethodHandle> list = new LinkedList<>();
        for (MethodHandle member : methods) {
            if (isApplicable(member, linkerServices, varArg)) {
                list.add(member);
            }
        }
        return list;
    }

    private boolean isApplicable(MethodHandle method, LinkerServices linkerServices, boolean varArg) {
        Class<?>[] formalTypes = method.type().parameterArray();
        int cl = this.classes.length;
        int fl = formalTypes.length - (varArg ? 1 : 0);
        if (varArg) {
            if (cl < fl) {
                return false;
            }
        } else if (cl != fl) {
            return false;
        }
        for (int i2 = 1; i2 < fl; i2++) {
            if (!canConvert(linkerServices, this.classes[i2], formalTypes[i2])) {
                return false;
            }
        }
        if (varArg) {
            Class<?> varArgType = formalTypes[fl].getComponentType();
            for (int i3 = fl; i3 < cl; i3++) {
                if (!canConvert(linkerServices, this.classes[i3], varArgType)) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    private static boolean canConvert(LinkerServices ls, Class<?> from, Class<?> to) {
        return from == NULL_CLASS ? !to.isPrimitive() : ls == null ? TypeUtilities.isMethodInvocationConvertible(from, to) : ls.canConvert(from, to);
    }
}
