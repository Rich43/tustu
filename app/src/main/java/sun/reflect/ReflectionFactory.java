package sun.reflect;

import java.io.Externalizable;
import java.io.ObjectInputStream;
import java.io.ObjectOutputStream;
import java.io.ObjectStreamClass;
import java.io.OptionalDataException;
import java.io.Serializable;
import java.lang.invoke.MethodHandle;
import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.util.Objects;
import sun.reflect.misc.ReflectUtil;

/* loaded from: rt.jar:sun/reflect/ReflectionFactory.class */
public class ReflectionFactory {
    private static volatile LangReflectAccess langReflectAccess;
    private static volatile Method hasStaticInitializerMethod;
    private static boolean initted = false;
    private static final Permission reflectionFactoryAccessPerm = new RuntimePermission("reflectionFactoryAccess");
    private static final ReflectionFactory soleInstance = new ReflectionFactory();
    private static boolean noInflation = false;
    private static int inflationThreshold = 15;

    private ReflectionFactory() {
    }

    /* loaded from: rt.jar:sun/reflect/ReflectionFactory$GetReflectionFactoryAction.class */
    public static final class GetReflectionFactoryAction implements PrivilegedAction<ReflectionFactory> {
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public ReflectionFactory run2() {
            return ReflectionFactory.getReflectionFactory();
        }
    }

    public static ReflectionFactory getReflectionFactory() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(reflectionFactoryAccessPerm);
        }
        return soleInstance;
    }

    public void setLangReflectAccess(LangReflectAccess langReflectAccess2) {
        langReflectAccess = langReflectAccess2;
    }

    public FieldAccessor newFieldAccessor(Field field, boolean z2) {
        checkInitted();
        return UnsafeFieldAccessorFactory.newFieldAccessor(field, z2);
    }

    public MethodAccessor newMethodAccessor(Method method) {
        checkInitted();
        if (noInflation && !ReflectUtil.isVMAnonymousClass(method.getDeclaringClass())) {
            return new MethodAccessorGenerator().generateMethod(method.getDeclaringClass(), method.getName(), method.getParameterTypes(), method.getReturnType(), method.getExceptionTypes(), method.getModifiers());
        }
        NativeMethodAccessorImpl nativeMethodAccessorImpl = new NativeMethodAccessorImpl(method);
        DelegatingMethodAccessorImpl delegatingMethodAccessorImpl = new DelegatingMethodAccessorImpl(nativeMethodAccessorImpl);
        nativeMethodAccessorImpl.setParent(delegatingMethodAccessorImpl);
        return delegatingMethodAccessorImpl;
    }

    public ConstructorAccessor newConstructorAccessor(Constructor<?> constructor) {
        checkInitted();
        Class<?> declaringClass = constructor.getDeclaringClass();
        if (Modifier.isAbstract(declaringClass.getModifiers())) {
            return new InstantiationExceptionConstructorAccessorImpl(null);
        }
        if (declaringClass == Class.class) {
            return new InstantiationExceptionConstructorAccessorImpl("Can not instantiate java.lang.Class");
        }
        if (Reflection.isSubclassOf(declaringClass, ConstructorAccessorImpl.class)) {
            return new BootstrapConstructorAccessorImpl(constructor);
        }
        if (noInflation && !ReflectUtil.isVMAnonymousClass(constructor.getDeclaringClass())) {
            return new MethodAccessorGenerator().generateConstructor(constructor.getDeclaringClass(), constructor.getParameterTypes(), constructor.getExceptionTypes(), constructor.getModifiers());
        }
        NativeConstructorAccessorImpl nativeConstructorAccessorImpl = new NativeConstructorAccessorImpl(constructor);
        DelegatingConstructorAccessorImpl delegatingConstructorAccessorImpl = new DelegatingConstructorAccessorImpl(nativeConstructorAccessorImpl);
        nativeConstructorAccessorImpl.setParent(delegatingConstructorAccessorImpl);
        return delegatingConstructorAccessorImpl;
    }

    public Field newField(Class<?> cls, String str, Class<?> cls2, int i2, int i3, String str2, byte[] bArr) {
        return langReflectAccess().newField(cls, str, cls2, i2, i3, str2, bArr);
    }

    public Method newMethod(Class<?> cls, String str, Class<?>[] clsArr, Class<?> cls2, Class<?>[] clsArr2, int i2, int i3, String str2, byte[] bArr, byte[] bArr2, byte[] bArr3) {
        return langReflectAccess().newMethod(cls, str, clsArr, cls2, clsArr2, i2, i3, str2, bArr, bArr2, bArr3);
    }

    public Constructor<?> newConstructor(Class<?> cls, Class<?>[] clsArr, Class<?>[] clsArr2, int i2, int i3, String str, byte[] bArr, byte[] bArr2) {
        return langReflectAccess().newConstructor(cls, clsArr, clsArr2, i2, i3, str, bArr, bArr2);
    }

    public MethodAccessor getMethodAccessor(Method method) {
        return langReflectAccess().getMethodAccessor(method);
    }

    public void setMethodAccessor(Method method, MethodAccessor methodAccessor) {
        langReflectAccess().setMethodAccessor(method, methodAccessor);
    }

    public ConstructorAccessor getConstructorAccessor(Constructor<?> constructor) {
        return langReflectAccess().getConstructorAccessor(constructor);
    }

    public void setConstructorAccessor(Constructor<?> constructor, ConstructorAccessor constructorAccessor) {
        langReflectAccess().setConstructorAccessor(constructor, constructorAccessor);
    }

    public Method copyMethod(Method method) {
        return langReflectAccess().copyMethod(method);
    }

    public Field copyField(Field field) {
        return langReflectAccess().copyField(field);
    }

    public <T> Constructor<T> copyConstructor(Constructor<T> constructor) {
        return langReflectAccess().copyConstructor(constructor);
    }

    public byte[] getExecutableTypeAnnotationBytes(Executable executable) {
        return langReflectAccess().getExecutableTypeAnnotationBytes(executable);
    }

    public Constructor<?> newConstructorForSerialization(Class<?> cls, Constructor<?> constructor) {
        if (constructor.getDeclaringClass() == cls) {
            return constructor;
        }
        return generateConstructor(cls, constructor);
    }

    public final Constructor<?> newConstructorForSerialization(Class<?> cls) throws SecurityException {
        Class<?> cls2 = cls;
        while (Serializable.class.isAssignableFrom(cls2)) {
            Class<? super Object> superclass = cls2.getSuperclass();
            cls2 = superclass;
            if (superclass == null) {
                return null;
            }
        }
        try {
            Constructor<?> declaredConstructor = cls2.getDeclaredConstructor(new Class[0]);
            int modifiers = declaredConstructor.getModifiers();
            if ((modifiers & 2) != 0) {
                return null;
            }
            if ((modifiers & 5) == 0) {
                if (!packageEquals(cls, cls2)) {
                    return null;
                }
            }
            return generateConstructor(cls, declaredConstructor);
        } catch (NoSuchMethodException e2) {
            return null;
        }
    }

    private final Constructor<?> generateConstructor(Class<?> cls, Constructor<?> constructor) {
        SerializationConstructorAccessorImpl serializationConstructorAccessorImplGenerateSerializationConstructor = new MethodAccessorGenerator().generateSerializationConstructor(cls, constructor.getParameterTypes(), constructor.getExceptionTypes(), constructor.getModifiers(), constructor.getDeclaringClass());
        Constructor<?> constructorNewConstructor = newConstructor(constructor.getDeclaringClass(), constructor.getParameterTypes(), constructor.getExceptionTypes(), constructor.getModifiers(), langReflectAccess().getConstructorSlot(constructor), langReflectAccess().getConstructorSignature(constructor), langReflectAccess().getConstructorAnnotations(constructor), langReflectAccess().getConstructorParameterAnnotations(constructor));
        setConstructorAccessor(constructorNewConstructor, serializationConstructorAccessorImplGenerateSerializationConstructor);
        constructorNewConstructor.setAccessible(true);
        return constructorNewConstructor;
    }

    public final Constructor<?> newConstructorForExternalization(Class<?> cls) throws SecurityException {
        if (!Externalizable.class.isAssignableFrom(cls)) {
            return null;
        }
        try {
            Constructor<?> constructor = cls.getConstructor(new Class[0]);
            constructor.setAccessible(true);
            return constructor;
        } catch (NoSuchMethodException e2) {
            return null;
        }
    }

    public final MethodHandle readObjectForSerialization(Class<?> cls) {
        return findReadWriteObjectForSerialization(cls, "readObject", ObjectInputStream.class);
    }

    public final MethodHandle readObjectNoDataForSerialization(Class<?> cls) {
        return findReadWriteObjectForSerialization(cls, "readObjectNoData", ObjectInputStream.class);
    }

    public final MethodHandle writeObjectForSerialization(Class<?> cls) {
        return findReadWriteObjectForSerialization(cls, "writeObject", ObjectOutputStream.class);
    }

    private final MethodHandle findReadWriteObjectForSerialization(Class<?> cls, String str, Class<?> cls2) throws SecurityException {
        if (!Serializable.class.isAssignableFrom(cls)) {
            return null;
        }
        try {
            Method declaredMethod = cls.getDeclaredMethod(str, cls2);
            int modifiers = declaredMethod.getModifiers();
            if (declaredMethod.getReturnType() != Void.TYPE || Modifier.isStatic(modifiers) || !Modifier.isPrivate(modifiers)) {
                return null;
            }
            declaredMethod.setAccessible(true);
            return MethodHandles.lookup().unreflect(declaredMethod);
        } catch (IllegalAccessException e2) {
            throw new InternalError("Error", e2);
        } catch (NoSuchMethodException e3) {
            return null;
        }
    }

    public final MethodHandle readResolveForSerialization(Class<?> cls) {
        return getReplaceResolveForSerialization(cls, "readResolve");
    }

    public final MethodHandle writeReplaceForSerialization(Class<?> cls) {
        return getReplaceResolveForSerialization(cls, "writeReplace");
    }

    private MethodHandle getReplaceResolveForSerialization(Class<?> cls, String str) throws SecurityException {
        if (!Serializable.class.isAssignableFrom(cls)) {
            return null;
        }
        Class<?> superclass = cls;
        while (true) {
            Class<?> cls2 = superclass;
            if (cls2 != null) {
                try {
                    Method declaredMethod = cls2.getDeclaredMethod(str, new Class[0]);
                    if (declaredMethod.getReturnType() != Object.class) {
                        return null;
                    }
                    int modifiers = declaredMethod.getModifiers();
                    if (Modifier.isStatic(modifiers) | Modifier.isAbstract(modifiers)) {
                        return null;
                    }
                    if (!(Modifier.isPublic(modifiers) | Modifier.isProtected(modifiers)) && ((Modifier.isPrivate(modifiers) && cls != cls2) || !packageEquals(cls, cls2))) {
                        return null;
                    }
                    try {
                        declaredMethod.setAccessible(true);
                        return MethodHandles.lookup().unreflect(declaredMethod);
                    } catch (IllegalAccessException e2) {
                        throw new InternalError("Error", e2);
                    }
                } catch (NoSuchMethodException e3) {
                    superclass = cls2.getSuperclass();
                }
            } else {
                return null;
            }
        }
    }

    public final boolean hasStaticInitializerForSerialization(Class<?> cls) throws SecurityException {
        Method declaredMethod = hasStaticInitializerMethod;
        if (declaredMethod == null) {
            try {
                declaredMethod = ObjectStreamClass.class.getDeclaredMethod("hasStaticInitializer", Class.class);
                declaredMethod.setAccessible(true);
                hasStaticInitializerMethod = declaredMethod;
            } catch (NoSuchMethodException e2) {
                throw new InternalError("No such method hasStaticInitializer on " + ((Object) ObjectStreamClass.class), e2);
            }
        }
        try {
            return ((Boolean) declaredMethod.invoke(null, cls)).booleanValue();
        } catch (IllegalAccessException | InvocationTargetException e3) {
            throw new InternalError("Exception invoking hasStaticInitializer", e3);
        }
    }

    public final OptionalDataException newOptionalDataExceptionForSerialization(boolean z2) {
        try {
            Constructor declaredConstructor = OptionalDataException.class.getDeclaredConstructor(Boolean.TYPE);
            declaredConstructor.setAccessible(true);
            return (OptionalDataException) declaredConstructor.newInstance(Boolean.valueOf(z2));
        } catch (IllegalAccessException | InstantiationException | NoSuchMethodException | InvocationTargetException e2) {
            throw new InternalError("unable to create OptionalDataException", e2);
        }
    }

    static int inflationThreshold() {
        return inflationThreshold;
    }

    private static void checkInitted() {
        if (initted) {
            return;
        }
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.reflect.ReflectionFactory.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                if (System.out == null) {
                    return null;
                }
                String property = System.getProperty("sun.reflect.noInflation");
                if (property != null && property.equals("true")) {
                    boolean unused = ReflectionFactory.noInflation = true;
                }
                String property2 = System.getProperty("sun.reflect.inflationThreshold");
                if (property2 != null) {
                    try {
                        int unused2 = ReflectionFactory.inflationThreshold = Integer.parseInt(property2);
                    } catch (NumberFormatException e2) {
                        throw new RuntimeException("Unable to parse property sun.reflect.inflationThreshold", e2);
                    }
                }
                boolean unused3 = ReflectionFactory.initted = true;
                return null;
            }
        });
    }

    private static LangReflectAccess langReflectAccess() {
        if (langReflectAccess == null) {
            Modifier.isPublic(1);
        }
        return langReflectAccess;
    }

    private static boolean packageEquals(Class<?> cls, Class<?> cls2) {
        return cls.getClassLoader() == cls2.getClassLoader() && Objects.equals(cls.getPackage(), cls2.getPackage());
    }
}
