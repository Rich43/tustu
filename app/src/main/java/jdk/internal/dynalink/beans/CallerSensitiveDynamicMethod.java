package jdk.internal.dynalink.beans;

import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.invoke.MethodType;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Constructor;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import jdk.internal.dynalink.support.Lookup;

/* loaded from: nashorn.jar:jdk/internal/dynalink/beans/CallerSensitiveDynamicMethod.class */
class CallerSensitiveDynamicMethod extends SingleDynamicMethod {
    private final AccessibleObject target;
    private final MethodType type;

    CallerSensitiveDynamicMethod(AccessibleObject target) {
        super(getName(target));
        this.target = target;
        this.type = getMethodType(target);
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static String getName(AccessibleObject accessibleObject) {
        Member m2 = (Member) accessibleObject;
        boolean constructor = m2 instanceof Constructor;
        return getMethodNameWithSignature(getMethodType(accessibleObject), constructor ? m2.getName() : getClassAndMethodName(m2.getDeclaringClass(), m2.getName()), !constructor);
    }

    @Override // jdk.internal.dynalink.beans.SingleDynamicMethod
    MethodType getMethodType() {
        return this.type;
    }

    /* JADX WARN: Multi-variable type inference failed */
    private static MethodType getMethodType(AccessibleObject accessibleObject) {
        Class declaringClass;
        boolean isMethod = accessibleObject instanceof Method;
        Class<?> rtype = isMethod ? ((Method) accessibleObject).getReturnType() : ((Constructor) accessibleObject).getDeclaringClass();
        Class<?>[] ptypes = isMethod ? ((Method) accessibleObject).getParameterTypes() : ((Constructor) accessibleObject).getParameterTypes();
        MethodType type = MethodType.methodType(rtype, ptypes);
        Member m2 = (Member) accessibleObject;
        Class<?>[] clsArr = new Class[1];
        if (isMethod) {
            declaringClass = Modifier.isStatic(m2.getModifiers()) ? Object.class : m2.getDeclaringClass();
        } else {
            declaringClass = StaticClass.class;
        }
        clsArr[0] = declaringClass;
        return type.insertParameterTypes(0, clsArr);
    }

    @Override // jdk.internal.dynalink.beans.SingleDynamicMethod
    boolean isVarArgs() {
        return this.target instanceof Method ? ((Method) this.target).isVarArgs() : ((Constructor) this.target).isVarArgs();
    }

    @Override // jdk.internal.dynalink.beans.SingleDynamicMethod
    MethodHandle getTarget(MethodHandles.Lookup lookup) {
        if (this.target instanceof Method) {
            MethodHandle mh = Lookup.unreflect(lookup, (Method) this.target);
            if (Modifier.isStatic(((Member) this.target).getModifiers())) {
                return StaticClassIntrospector.editStaticMethodHandle(mh);
            }
            return mh;
        }
        return StaticClassIntrospector.editConstructorMethodHandle(Lookup.unreflectConstructor(lookup, (Constructor) this.target));
    }

    @Override // jdk.internal.dynalink.beans.DynamicMethod
    boolean isConstructor() {
        return this.target instanceof Constructor;
    }
}
