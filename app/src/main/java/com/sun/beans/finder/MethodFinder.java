package com.sun.beans.finder;

import com.sun.beans.TypeResolver;
import com.sun.beans.util.Cache;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ParameterizedType;
import java.lang.reflect.Type;
import java.util.Arrays;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:com/sun/beans/finder/MethodFinder.class */
public final class MethodFinder extends AbstractFinder<Method> {
    private static final Cache<Signature, Method> CACHE = new Cache<Signature, Method>(Cache.Kind.SOFT, Cache.Kind.SOFT) { // from class: com.sun.beans.finder.MethodFinder.1
        @Override // com.sun.beans.util.Cache
        public Method create(Signature signature) {
            try {
                return MethodFinder.findAccessibleMethod(new MethodFinder(signature.getName(), signature.getArgs()).find(signature.getType().getMethods()));
            } catch (Exception e2) {
                throw new SignatureException(e2);
            }
        }
    };
    private final String name;

    public static Method findMethod(Class<?> cls, String str, Class<?>... clsArr) throws NoSuchMethodException {
        if (str == null) {
            throw new IllegalArgumentException("Method name is not set");
        }
        PrimitiveWrapperMap.replacePrimitivesWithWrappers(clsArr);
        Signature signature = new Signature(cls, str, clsArr);
        try {
            Method method = CACHE.get(signature);
            return (method == null || ReflectUtil.isPackageAccessible(method.getDeclaringClass())) ? method : CACHE.create(signature);
        } catch (SignatureException e2) {
            throw e2.toNoSuchMethodException("Method '" + str + "' is not found");
        }
    }

    public static Method findInstanceMethod(Class<?> cls, String str, Class<?>... clsArr) throws NoSuchMethodException {
        Method methodFindMethod = findMethod(cls, str, clsArr);
        if (Modifier.isStatic(methodFindMethod.getModifiers())) {
            throw new NoSuchMethodException("Method '" + str + "' is static");
        }
        return methodFindMethod;
    }

    public static Method findStaticMethod(Class<?> cls, String str, Class<?>... clsArr) throws NoSuchMethodException {
        Method methodFindMethod = findMethod(cls, str, clsArr);
        if (!Modifier.isStatic(methodFindMethod.getModifiers())) {
            throw new NoSuchMethodException("Method '" + str + "' is not static");
        }
        return methodFindMethod;
    }

    public static Method findAccessibleMethod(Method method) throws NoSuchMethodException {
        Class<?> declaringClass = method.getDeclaringClass();
        if (Modifier.isPublic(declaringClass.getModifiers()) && ReflectUtil.isPackageAccessible(declaringClass)) {
            return method;
        }
        if (Modifier.isStatic(method.getModifiers())) {
            throw new NoSuchMethodException("Method '" + method.getName() + "' is not accessible");
        }
        for (Type type : declaringClass.getGenericInterfaces()) {
            try {
                return findAccessibleMethod(method, type);
            } catch (NoSuchMethodException e2) {
            }
        }
        return findAccessibleMethod(method, declaringClass.getGenericSuperclass());
    }

    private static Method findAccessibleMethod(Method method, Type type) throws NoSuchMethodException, SecurityException {
        String name = method.getName();
        Class<?>[] parameterTypes = method.getParameterTypes();
        if (type instanceof Class) {
            return findAccessibleMethod(((Class) type).getMethod(name, parameterTypes));
        }
        if (type instanceof ParameterizedType) {
            ParameterizedType parameterizedType = (ParameterizedType) type;
            for (Method method2 : ((Class) parameterizedType.getRawType()).getMethods()) {
                if (method2.getName().equals(name)) {
                    Class<?>[] parameterTypes2 = method2.getParameterTypes();
                    if (parameterTypes2.length != parameterTypes.length) {
                        continue;
                    } else {
                        if (Arrays.equals(parameterTypes, parameterTypes2)) {
                            return findAccessibleMethod(method2);
                        }
                        Type[] genericParameterTypes = method2.getGenericParameterTypes();
                        if (parameterTypes.length == genericParameterTypes.length && Arrays.equals(parameterTypes, TypeResolver.erase(TypeResolver.resolve(parameterizedType, genericParameterTypes)))) {
                            return findAccessibleMethod(method2);
                        }
                    }
                }
            }
        }
        throw new NoSuchMethodException("Method '" + name + "' is not accessible");
    }

    private MethodFinder(String str, Class<?>[] clsArr) {
        super(clsArr);
        this.name = str;
    }

    /* JADX INFO: Access modifiers changed from: protected */
    @Override // com.sun.beans.finder.AbstractFinder
    public boolean isValid(Method method) {
        return super.isValid((MethodFinder) method) && method.getName().equals(this.name);
    }
}
