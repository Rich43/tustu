package java.lang;

import com.sun.org.apache.xalan.internal.templates.Constants;
import java.io.InputStream;
import java.io.ObjectStreamField;
import java.io.Serializable;
import java.lang.ClassValue;
import java.lang.annotation.Annotation;
import java.lang.ref.SoftReference;
import java.lang.reflect.AnnotatedElement;
import java.lang.reflect.AnnotatedType;
import java.lang.reflect.Array;
import java.lang.reflect.Constructor;
import java.lang.reflect.Executable;
import java.lang.reflect.Field;
import java.lang.reflect.GenericArrayType;
import java.lang.reflect.GenericDeclaration;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.Proxy;
import java.lang.reflect.Type;
import java.lang.reflect.TypeVariable;
import java.net.URL;
import java.security.AccessController;
import java.security.Permissions;
import java.security.PrivilegedAction;
import java.security.ProtectionDomain;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collection;
import java.util.HashMap;
import java.util.HashSet;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.Objects;
import java.util.Set;
import javafx.fxml.FXMLLoader;
import sun.misc.Unsafe;
import sun.misc.VM;
import sun.reflect.CallerSensitive;
import sun.reflect.ConstantPool;
import sun.reflect.Reflection;
import sun.reflect.ReflectionFactory;
import sun.reflect.annotation.AnnotationParser;
import sun.reflect.annotation.AnnotationSupport;
import sun.reflect.annotation.AnnotationType;
import sun.reflect.annotation.TypeAnnotationParser;
import sun.reflect.generics.factory.CoreReflectionFactory;
import sun.reflect.generics.factory.GenericsFactory;
import sun.reflect.generics.repository.ClassRepository;
import sun.reflect.generics.repository.ConstructorRepository;
import sun.reflect.generics.repository.MethodRepository;
import sun.reflect.generics.scope.ClassScope;
import sun.reflect.misc.ReflectUtil;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/lang/Class.class */
public final class Class<T> implements Serializable, GenericDeclaration, Type, AnnotatedElement {
    private static final int ANNOTATION = 8192;
    private static final int ENUM = 16384;
    private static final int SYNTHETIC = 4096;
    private volatile transient Constructor<T> cachedConstructor;
    private volatile transient Class<?> newInstanceCallerCache;
    private transient String name;
    private final ClassLoader classLoader;
    private static ProtectionDomain allPermDomain;
    private static boolean useCaches;
    private volatile transient SoftReference<ReflectionData<T>> reflectionData;
    private volatile transient ClassRepository genericInfo;
    private static final long serialVersionUID = 3206093459760846163L;
    private static final ObjectStreamField[] serialPersistentFields;
    private static ReflectionFactory reflectionFactory;
    private static boolean initted;
    private volatile transient AnnotationData annotationData;
    private volatile transient AnnotationType annotationType;
    transient ClassValue.ClassValueMap classValueMap;
    private volatile transient int classRedefinedCount = 0;
    private volatile transient T[] enumConstants = null;
    private volatile transient Map<String, T> enumConstantDirectory = null;

    private static native void registerNatives();

    private static native Class<?> forName0(String str, boolean z2, ClassLoader classLoader, Class<?> cls) throws ClassNotFoundException;

    public native boolean isInstance(Object obj);

    public native boolean isAssignableFrom(Class<?> cls);

    public native boolean isInterface();

    public native boolean isArray();

    public native boolean isPrimitive();

    private native String getName0();

    public native Class<? super T> getSuperclass();

    private native Class<?>[] getInterfaces0();

    public native Class<?> getComponentType();

    public native int getModifiers();

    public native Object[] getSigners();

    native void setSigners(Object[] objArr);

    private native Object[] getEnclosingMethod0();

    private native Class<?> getDeclaringClass0();

    private native ProtectionDomain getProtectionDomain0();

    static native Class<?> getPrimitiveClass(String str);

    private native String getGenericSignature0();

    native byte[] getRawAnnotations();

    native byte[] getRawTypeAnnotations();

    native ConstantPool getConstantPool();

    /* JADX INFO: Access modifiers changed from: private */
    public native Field[] getDeclaredFields0(boolean z2);

    private native Method[] getDeclaredMethods0(boolean z2);

    private native Constructor<T>[] getDeclaredConstructors0(boolean z2);

    private native Class<?>[] getDeclaredClasses0();

    private static native boolean desiredAssertionStatus0(Class<?> cls);

    static {
        registerNatives();
        useCaches = true;
        serialPersistentFields = new ObjectStreamField[0];
        initted = false;
    }

    private Class(ClassLoader classLoader) {
        this.classLoader = classLoader;
    }

    public String toString() {
        return (isInterface() ? "interface " : isPrimitive() ? "" : "class ") + getName();
    }

    public String toGenericString() {
        if (isPrimitive()) {
            return toString();
        }
        StringBuilder sb = new StringBuilder();
        int modifiers = getModifiers() & Modifier.classModifiers();
        if (modifiers != 0) {
            sb.append(Modifier.toString(modifiers));
            sb.append(' ');
        }
        if (isAnnotation()) {
            sb.append('@');
        }
        if (isInterface()) {
            sb.append("interface");
        } else if (isEnum()) {
            sb.append("enum");
        } else {
            sb.append(Constants.ATTRNAME_CLASS);
        }
        sb.append(' ');
        sb.append(getName());
        TypeVariable<Class<T>>[] typeParameters = getTypeParameters();
        if (typeParameters.length > 0) {
            boolean z2 = true;
            sb.append('<');
            for (TypeVariable<Class<T>> typeVariable : typeParameters) {
                if (!z2) {
                    sb.append(',');
                }
                sb.append(typeVariable.getTypeName());
                z2 = false;
            }
            sb.append('>');
        }
        return sb.toString();
    }

    @CallerSensitive
    public static Class<?> forName(String str) throws ClassNotFoundException {
        Class<?> callerClass = Reflection.getCallerClass();
        return forName0(str, true, ClassLoader.getClassLoader(callerClass), callerClass);
    }

    @CallerSensitive
    public static Class<?> forName(String str, boolean z2, ClassLoader classLoader) throws ClassNotFoundException {
        Class<?> callerClass = null;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            callerClass = Reflection.getCallerClass();
            if (VM.isSystemDomainLoader(classLoader) && !VM.isSystemDomainLoader(ClassLoader.getClassLoader(callerClass))) {
                securityManager.checkPermission(SecurityConstants.GET_CLASSLOADER_PERMISSION);
            }
        }
        return forName0(str, z2, classLoader, callerClass);
    }

    @CallerSensitive
    public T newInstance() throws IllegalAccessException, InstantiationException {
        Class<?> callerClass;
        if (System.getSecurityManager() != null) {
            checkMemberAccess(0, Reflection.getCallerClass(), false);
        }
        if (this.cachedConstructor == null) {
            if (this == Class.class) {
                throw new IllegalAccessException("Can not call newInstance() on the Class for java.lang.Class");
            }
            try {
                final Constructor<T> constructor0 = getConstructor0(new Class[0], 1);
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.lang.Class.1
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public Void run() {
                        constructor0.setAccessible(true);
                        return null;
                    }
                });
                this.cachedConstructor = constructor0;
            } catch (NoSuchMethodException e2) {
                throw ((InstantiationException) new InstantiationException(getName()).initCause(e2));
            }
        }
        Constructor<T> constructor = this.cachedConstructor;
        int modifiers = constructor.getModifiers();
        if (!Reflection.quickCheckMemberAccess(this, modifiers) && this.newInstanceCallerCache != (callerClass = Reflection.getCallerClass())) {
            Reflection.ensureMemberAccess(callerClass, this, null, modifiers);
            this.newInstanceCallerCache = callerClass;
        }
        try {
            return constructor.newInstance((Object[]) null);
        } catch (InvocationTargetException e3) {
            Unsafe.getUnsafe().throwException(e3.getTargetException());
            return null;
        }
    }

    public boolean isAnnotation() {
        return (getModifiers() & 8192) != 0;
    }

    public boolean isSynthetic() {
        return (getModifiers() & 4096) != 0;
    }

    public String getName() {
        String str = this.name;
        if (str == null) {
            String name0 = getName0();
            str = name0;
            this.name = name0;
        }
        return str;
    }

    @CallerSensitive
    public ClassLoader getClassLoader() {
        ClassLoader classLoader0 = getClassLoader0();
        if (classLoader0 == null) {
            return null;
        }
        if (System.getSecurityManager() != null) {
            ClassLoader.checkClassLoaderPermission(classLoader0, Reflection.getCallerClass());
        }
        return classLoader0;
    }

    ClassLoader getClassLoader0() {
        return this.classLoader;
    }

    /* JADX WARN: Multi-variable type inference failed */
    @Override // java.lang.reflect.GenericDeclaration
    public TypeVariable<Class<T>>[] getTypeParameters() {
        ClassRepository genericInfo = getGenericInfo();
        if (genericInfo != null) {
            return genericInfo.getTypeParameters();
        }
        return new TypeVariable[0];
    }

    public Type getGenericSuperclass() {
        ClassRepository genericInfo = getGenericInfo();
        if (genericInfo == null) {
            return getSuperclass();
        }
        if (isInterface()) {
            return null;
        }
        return genericInfo.getSuperclass();
    }

    public Package getPackage() {
        return Package.getPackage((Class<?>) this);
    }

    public Class<?>[] getInterfaces() {
        ReflectionData<T> reflectionData = reflectionData();
        if (reflectionData == null) {
            return getInterfaces0();
        }
        Class<?>[] interfaces0 = reflectionData.interfaces;
        if (interfaces0 == null) {
            interfaces0 = getInterfaces0();
            reflectionData.interfaces = interfaces0;
        }
        return (Class[]) interfaces0.clone();
    }

    public Type[] getGenericInterfaces() {
        ClassRepository genericInfo = getGenericInfo();
        return genericInfo == null ? getInterfaces() : genericInfo.getSuperInterfaces();
    }

    @CallerSensitive
    public Method getEnclosingMethod() throws SecurityException {
        EnclosingMethodInfo enclosingMethodInfo = getEnclosingMethodInfo();
        if (enclosingMethodInfo == null || !enclosingMethodInfo.isMethod()) {
            return null;
        }
        MethodRepository methodRepositoryMake = MethodRepository.make(enclosingMethodInfo.getDescriptor(), getFactory());
        Class<?> cls = toClass(methodRepositoryMake.getReturnType());
        Type[] parameterTypes = methodRepositoryMake.getParameterTypes();
        Class[] clsArr = new Class[parameterTypes.length];
        for (int i2 = 0; i2 < clsArr.length; i2++) {
            clsArr[i2] = toClass(parameterTypes[i2]);
        }
        Class<?> enclosingClass = enclosingMethodInfo.getEnclosingClass();
        enclosingClass.checkMemberAccess(1, Reflection.getCallerClass(), true);
        for (Method method : enclosingClass.getDeclaredMethods()) {
            if (method.getName().equals(enclosingMethodInfo.getName())) {
                Class<?>[] parameterTypes2 = method.getParameterTypes();
                if (parameterTypes2.length == clsArr.length) {
                    boolean z2 = true;
                    int i3 = 0;
                    while (true) {
                        if (i3 >= parameterTypes2.length) {
                            break;
                        }
                        if (parameterTypes2[i3].equals(clsArr[i3])) {
                            i3++;
                        } else {
                            z2 = false;
                            break;
                        }
                    }
                    if (z2 && method.getReturnType().equals(cls)) {
                        return method;
                    }
                } else {
                    continue;
                }
            }
        }
        throw new InternalError("Enclosing method not found");
    }

    private EnclosingMethodInfo getEnclosingMethodInfo() {
        Object[] enclosingMethod0 = getEnclosingMethod0();
        if (enclosingMethod0 == null) {
            return null;
        }
        return new EnclosingMethodInfo(enclosingMethod0);
    }

    /* loaded from: rt.jar:java/lang/Class$EnclosingMethodInfo.class */
    private static final class EnclosingMethodInfo {
        private Class<?> enclosingClass;
        private String name;
        private String descriptor;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !Class.class.desiredAssertionStatus();
        }

        private EnclosingMethodInfo(Object[] objArr) {
            if (objArr.length != 3) {
                throw new InternalError("Malformed enclosing method information");
            }
            try {
                this.enclosingClass = (Class) objArr[0];
                if (!$assertionsDisabled && this.enclosingClass == null) {
                    throw new AssertionError();
                }
                this.name = (String) objArr[1];
                this.descriptor = (String) objArr[2];
                if (!$assertionsDisabled && ((this.name == null || this.descriptor == null) && this.name != this.descriptor)) {
                    throw new AssertionError();
                }
            } catch (ClassCastException e2) {
                throw new InternalError("Invalid type in enclosing method information", e2);
            }
        }

        boolean isPartial() {
            return this.enclosingClass == null || this.name == null || this.descriptor == null;
        }

        boolean isConstructor() {
            return !isPartial() && com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME.equals(this.name);
        }

        boolean isMethod() {
            return (isPartial() || isConstructor() || com.sun.org.apache.bcel.internal.Constants.STATIC_INITIALIZER_NAME.equals(this.name)) ? false : true;
        }

        Class<?> getEnclosingClass() {
            return this.enclosingClass;
        }

        String getName() {
            return this.name;
        }

        String getDescriptor() {
            return this.descriptor;
        }
    }

    private static Class<?> toClass(Type type) {
        if (type instanceof GenericArrayType) {
            return Array.newInstance(toClass(((GenericArrayType) type).getGenericComponentType()), 0).getClass();
        }
        return (Class) type;
    }

    @CallerSensitive
    public Constructor<?> getEnclosingConstructor() throws SecurityException {
        EnclosingMethodInfo enclosingMethodInfo = getEnclosingMethodInfo();
        if (enclosingMethodInfo == null || !enclosingMethodInfo.isConstructor()) {
            return null;
        }
        Type[] parameterTypes = ConstructorRepository.make(enclosingMethodInfo.getDescriptor(), getFactory()).getParameterTypes();
        Class[] clsArr = new Class[parameterTypes.length];
        for (int i2 = 0; i2 < clsArr.length; i2++) {
            clsArr[i2] = toClass(parameterTypes[i2]);
        }
        Class<?> enclosingClass = enclosingMethodInfo.getEnclosingClass();
        enclosingClass.checkMemberAccess(1, Reflection.getCallerClass(), true);
        for (Constructor<?> constructor : enclosingClass.getDeclaredConstructors()) {
            Class<?>[] parameterTypes2 = constructor.getParameterTypes();
            if (parameterTypes2.length == clsArr.length) {
                boolean z2 = true;
                int i3 = 0;
                while (true) {
                    if (i3 >= parameterTypes2.length) {
                        break;
                    }
                    if (parameterTypes2[i3].equals(clsArr[i3])) {
                        i3++;
                    } else {
                        z2 = false;
                        break;
                    }
                }
                if (z2) {
                    return constructor;
                }
            }
        }
        throw new InternalError("Enclosing constructor not found");
    }

    @CallerSensitive
    public Class<?> getDeclaringClass() throws SecurityException {
        Class<?> declaringClass0 = getDeclaringClass0();
        if (declaringClass0 != null) {
            declaringClass0.checkPackageAccess(ClassLoader.getClassLoader(Reflection.getCallerClass()), true);
        }
        return declaringClass0;
    }

    @CallerSensitive
    public Class<?> getEnclosingClass() throws SecurityException {
        Class<?> declaringClass;
        EnclosingMethodInfo enclosingMethodInfo = getEnclosingMethodInfo();
        if (enclosingMethodInfo == null) {
            declaringClass = getDeclaringClass();
        } else {
            Class<?> enclosingClass = enclosingMethodInfo.getEnclosingClass();
            if (enclosingClass == this || enclosingClass == null) {
                throw new InternalError("Malformed enclosing method information");
            }
            declaringClass = enclosingClass;
        }
        if (declaringClass != null) {
            declaringClass.checkPackageAccess(ClassLoader.getClassLoader(Reflection.getCallerClass()), true);
        }
        return declaringClass;
    }

    public String getSimpleName() throws SecurityException {
        if (isArray()) {
            return getComponentType().getSimpleName() + "[]";
        }
        String simpleBinaryName = getSimpleBinaryName();
        if (simpleBinaryName == null) {
            String name = getName();
            return name.substring(name.lastIndexOf(".") + 1);
        }
        int length = simpleBinaryName.length();
        if (length < 1 || simpleBinaryName.charAt(0) != '$') {
            throw new InternalError("Malformed class name");
        }
        int i2 = 1;
        while (i2 < length && isAsciiDigit(simpleBinaryName.charAt(i2))) {
            i2++;
        }
        return simpleBinaryName.substring(i2);
    }

    @Override // java.lang.reflect.Type
    public String getTypeName() {
        if (isArray()) {
            Class<T> componentType = this;
            int i2 = 0;
            while (componentType.isArray()) {
                try {
                    i2++;
                    componentType = componentType.getComponentType();
                } catch (Throwable th) {
                }
            }
            StringBuilder sb = new StringBuilder();
            sb.append(componentType.getName());
            for (int i3 = 0; i3 < i2; i3++) {
                sb.append("[]");
            }
            return sb.toString();
        }
        return getName();
    }

    private static boolean isAsciiDigit(char c2) {
        return '0' <= c2 && c2 <= '9';
    }

    public String getCanonicalName() throws SecurityException {
        if (isArray()) {
            String canonicalName = getComponentType().getCanonicalName();
            if (canonicalName != null) {
                return canonicalName + "[]";
            }
            return null;
        }
        if (isLocalOrAnonymousClass()) {
            return null;
        }
        Class<?> enclosingClass = getEnclosingClass();
        if (enclosingClass == null) {
            return getName();
        }
        String canonicalName2 = enclosingClass.getCanonicalName();
        if (canonicalName2 == null) {
            return null;
        }
        return canonicalName2 + "." + getSimpleName();
    }

    public boolean isAnonymousClass() {
        return "".equals(getSimpleName());
    }

    public boolean isLocalClass() {
        return isLocalOrAnonymousClass() && !isAnonymousClass();
    }

    public boolean isMemberClass() {
        return (getSimpleBinaryName() == null || isLocalOrAnonymousClass()) ? false : true;
    }

    private String getSimpleBinaryName() throws SecurityException {
        Class<?> enclosingClass = getEnclosingClass();
        if (enclosingClass == null) {
            return null;
        }
        try {
            return getName().substring(enclosingClass.getName().length());
        } catch (IndexOutOfBoundsException e2) {
            throw new InternalError("Malformed class name", e2);
        }
    }

    private boolean isLocalOrAnonymousClass() {
        return getEnclosingMethodInfo() != null;
    }

    @CallerSensitive
    public Class<?>[] getClasses() {
        checkMemberAccess(0, Reflection.getCallerClass(), false);
        return (Class[]) AccessController.doPrivileged(new PrivilegedAction<Class<?>[]>() { // from class: java.lang.Class.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Class<?>[] run() throws SecurityException {
                ArrayList arrayList = new ArrayList();
                Class superclass = Class.this;
                while (true) {
                    Class cls = superclass;
                    if (cls != null) {
                        Class<?>[] declaredClasses = cls.getDeclaredClasses();
                        for (int i2 = 0; i2 < declaredClasses.length; i2++) {
                            if (Modifier.isPublic(declaredClasses[i2].getModifiers())) {
                                arrayList.add(declaredClasses[i2]);
                            }
                        }
                        superclass = cls.getSuperclass();
                    } else {
                        return (Class[]) arrayList.toArray(new Class[0]);
                    }
                }
            }
        });
    }

    @CallerSensitive
    public Field[] getFields() throws SecurityException {
        checkMemberAccess(0, Reflection.getCallerClass(), true);
        return copyFields(privateGetPublicFields(null));
    }

    @CallerSensitive
    public Method[] getMethods() throws SecurityException {
        checkMemberAccess(0, Reflection.getCallerClass(), true);
        return copyMethods(privateGetPublicMethods());
    }

    @CallerSensitive
    public Constructor<?>[] getConstructors() throws SecurityException {
        checkMemberAccess(0, Reflection.getCallerClass(), true);
        return copyConstructors(privateGetDeclaredConstructors(true));
    }

    @CallerSensitive
    public Field getField(String str) throws NoSuchFieldException, SecurityException {
        checkMemberAccess(0, Reflection.getCallerClass(), true);
        Field field0 = getField0(str);
        if (field0 == null) {
            throw new NoSuchFieldException(str);
        }
        return field0;
    }

    @CallerSensitive
    public Method getMethod(String str, Class<?>... clsArr) throws NoSuchMethodException, SecurityException {
        checkMemberAccess(0, Reflection.getCallerClass(), true);
        Method method0 = getMethod0(str, clsArr, true);
        if (method0 == null) {
            throw new NoSuchMethodException(getName() + "." + str + argumentTypesToString(clsArr));
        }
        return method0;
    }

    @CallerSensitive
    public Constructor<T> getConstructor(Class<?>... clsArr) throws NoSuchMethodException, SecurityException {
        checkMemberAccess(0, Reflection.getCallerClass(), true);
        return getConstructor0(clsArr, 0);
    }

    @CallerSensitive
    public Class<?>[] getDeclaredClasses() throws SecurityException {
        checkMemberAccess(1, Reflection.getCallerClass(), false);
        return getDeclaredClasses0();
    }

    @CallerSensitive
    public Field[] getDeclaredFields() throws SecurityException {
        checkMemberAccess(1, Reflection.getCallerClass(), true);
        return copyFields(privateGetDeclaredFields(false));
    }

    @CallerSensitive
    public Method[] getDeclaredMethods() throws SecurityException {
        checkMemberAccess(1, Reflection.getCallerClass(), true);
        return copyMethods(privateGetDeclaredMethods(false));
    }

    @CallerSensitive
    public Constructor<?>[] getDeclaredConstructors() throws SecurityException {
        checkMemberAccess(1, Reflection.getCallerClass(), true);
        return copyConstructors(privateGetDeclaredConstructors(false));
    }

    @CallerSensitive
    public Field getDeclaredField(String str) throws NoSuchFieldException, SecurityException {
        checkMemberAccess(1, Reflection.getCallerClass(), true);
        Field fieldSearchFields = searchFields(privateGetDeclaredFields(false), str);
        if (fieldSearchFields == null) {
            throw new NoSuchFieldException(str);
        }
        return fieldSearchFields;
    }

    @CallerSensitive
    public Method getDeclaredMethod(String str, Class<?>... clsArr) throws NoSuchMethodException, SecurityException {
        checkMemberAccess(1, Reflection.getCallerClass(), true);
        Method methodSearchMethods = searchMethods(privateGetDeclaredMethods(false), str, clsArr);
        if (methodSearchMethods == null) {
            throw new NoSuchMethodException(getName() + "." + str + argumentTypesToString(clsArr));
        }
        return methodSearchMethods;
    }

    @CallerSensitive
    public Constructor<T> getDeclaredConstructor(Class<?>... clsArr) throws NoSuchMethodException, SecurityException {
        checkMemberAccess(1, Reflection.getCallerClass(), true);
        return getConstructor0(clsArr, 1);
    }

    public InputStream getResourceAsStream(String str) {
        String strResolveName = resolveName(str);
        ClassLoader classLoader0 = getClassLoader0();
        if (classLoader0 == null) {
            return ClassLoader.getSystemResourceAsStream(strResolveName);
        }
        return classLoader0.getResourceAsStream(strResolveName);
    }

    public URL getResource(String str) {
        String strResolveName = resolveName(str);
        ClassLoader classLoader0 = getClassLoader0();
        if (classLoader0 == null) {
            return ClassLoader.getSystemResource(strResolveName);
        }
        return classLoader0.getResource(strResolveName);
    }

    public ProtectionDomain getProtectionDomain() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(SecurityConstants.GET_PD_PERMISSION);
        }
        ProtectionDomain protectionDomain0 = getProtectionDomain0();
        if (protectionDomain0 == null) {
            if (allPermDomain == null) {
                Permissions permissions = new Permissions();
                permissions.add(SecurityConstants.ALL_PERMISSION);
                allPermDomain = new ProtectionDomain(null, permissions);
            }
            protectionDomain0 = allPermDomain;
        }
        return protectionDomain0;
    }

    private void checkMemberAccess(int i2, Class<?> cls, boolean z2) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            ClassLoader classLoader = ClassLoader.getClassLoader(cls);
            ClassLoader classLoader0 = getClassLoader0();
            if (i2 != 0 && classLoader != classLoader0) {
                securityManager.checkPermission(SecurityConstants.CHECK_MEMBER_ACCESS_PERMISSION);
            }
            checkPackageAccess(classLoader, z2);
        }
    }

    private void checkPackageAccess(ClassLoader classLoader, boolean z2) {
        String name;
        int iLastIndexOf;
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            if (ReflectUtil.needsPackageAccessCheck(classLoader, getClassLoader0()) && (iLastIndexOf = (name = getName()).lastIndexOf(46)) != -1) {
                String strSubstring = name.substring(0, iLastIndexOf);
                if (!Proxy.isProxyClass(this) || ReflectUtil.isNonPublicProxyClass(this)) {
                    securityManager.checkPackageAccess(strSubstring);
                }
            }
            if (z2 && Proxy.isProxyClass(this)) {
                ReflectUtil.checkProxyPackageAccess(classLoader, getInterfaces());
            }
        }
    }

    private String resolveName(String str) {
        Class cls;
        if (str == null) {
            return str;
        }
        if (!str.startsWith("/")) {
            Class componentType = this;
            while (true) {
                cls = componentType;
                if (!cls.isArray()) {
                    break;
                }
                componentType = cls.getComponentType();
            }
            String name = cls.getName();
            int iLastIndexOf = name.lastIndexOf(46);
            if (iLastIndexOf != -1) {
                str = name.substring(0, iLastIndexOf).replace('.', '/') + "/" + str;
            }
        } else {
            str = str.substring(1);
        }
        return str;
    }

    /* loaded from: rt.jar:java/lang/Class$Atomic.class */
    private static class Atomic {
        private static final Unsafe unsafe = Unsafe.getUnsafe();
        private static final long reflectionDataOffset;
        private static final long annotationTypeOffset;
        private static final long annotationDataOffset;

        private Atomic() {
        }

        static {
            Field[] declaredFields0 = Class.class.getDeclaredFields0(false);
            reflectionDataOffset = objectFieldOffset(declaredFields0, "reflectionData");
            annotationTypeOffset = objectFieldOffset(declaredFields0, "annotationType");
            annotationDataOffset = objectFieldOffset(declaredFields0, "annotationData");
        }

        private static long objectFieldOffset(Field[] fieldArr, String str) {
            Field fieldSearchFields = Class.searchFields(fieldArr, str);
            if (fieldSearchFields == null) {
                throw new Error("No " + str + " field found in java.lang.Class");
            }
            return unsafe.objectFieldOffset(fieldSearchFields);
        }

        static <T> boolean casReflectionData(Class<?> cls, SoftReference<ReflectionData<T>> softReference, SoftReference<ReflectionData<T>> softReference2) {
            return unsafe.compareAndSwapObject(cls, reflectionDataOffset, softReference, softReference2);
        }

        static <T> boolean casAnnotationType(Class<?> cls, AnnotationType annotationType, AnnotationType annotationType2) {
            return unsafe.compareAndSwapObject(cls, annotationTypeOffset, annotationType, annotationType2);
        }

        static <T> boolean casAnnotationData(Class<?> cls, AnnotationData annotationData, AnnotationData annotationData2) {
            return unsafe.compareAndSwapObject(cls, annotationDataOffset, annotationData, annotationData2);
        }
    }

    /* loaded from: rt.jar:java/lang/Class$ReflectionData.class */
    private static class ReflectionData<T> {
        volatile Field[] declaredFields;
        volatile Field[] publicFields;
        volatile Method[] declaredMethods;
        volatile Method[] publicMethods;
        volatile Constructor<T>[] declaredConstructors;
        volatile Constructor<T>[] publicConstructors;
        volatile Field[] declaredPublicFields;
        volatile Method[] declaredPublicMethods;
        volatile Class<?>[] interfaces;
        final int redefinedCount;

        ReflectionData(int i2) {
            this.redefinedCount = i2;
        }
    }

    private ReflectionData<T> reflectionData() {
        ReflectionData<T> reflectionData;
        SoftReference<ReflectionData<T>> softReference = this.reflectionData;
        int i2 = this.classRedefinedCount;
        if (useCaches && softReference != null && (reflectionData = softReference.get()) != null && reflectionData.redefinedCount == i2) {
            return reflectionData;
        }
        return newReflectionData(softReference, i2);
    }

    private ReflectionData<T> newReflectionData(SoftReference<ReflectionData<T>> softReference, int i2) {
        ReflectionData<T> reflectionData;
        if (!useCaches) {
            return null;
        }
        while (true) {
            ReflectionData<T> reflectionData2 = new ReflectionData<>(i2);
            if (Atomic.casReflectionData(this, softReference, new SoftReference(reflectionData2))) {
                return reflectionData2;
            }
            softReference = this.reflectionData;
            i2 = this.classRedefinedCount;
            if (softReference != null && (reflectionData = softReference.get()) != null && reflectionData.redefinedCount == i2) {
                return reflectionData;
            }
        }
    }

    private GenericsFactory getFactory() {
        return CoreReflectionFactory.make(this, ClassScope.make(this));
    }

    private ClassRepository getGenericInfo() {
        ClassRepository classRepositoryMake = this.genericInfo;
        if (classRepositoryMake == null) {
            String genericSignature0 = getGenericSignature0();
            if (genericSignature0 == null) {
                classRepositoryMake = ClassRepository.NONE;
            } else {
                classRepositoryMake = ClassRepository.make(genericSignature0, getFactory());
            }
            this.genericInfo = classRepositoryMake;
        }
        if (classRepositoryMake != ClassRepository.NONE) {
            return classRepositoryMake;
        }
        return null;
    }

    static byte[] getExecutableTypeAnnotationBytes(Executable executable) {
        return getReflectionFactory().getExecutableTypeAnnotationBytes(executable);
    }

    private Field[] privateGetDeclaredFields(boolean z2) {
        checkInitted();
        ReflectionData<T> reflectionData = reflectionData();
        if (reflectionData != null) {
            Field[] fieldArr = z2 ? reflectionData.declaredPublicFields : reflectionData.declaredFields;
            if (fieldArr != null) {
                return fieldArr;
            }
        }
        Field[] fieldArrFilterFields = Reflection.filterFields(this, getDeclaredFields0(z2));
        if (reflectionData != null) {
            if (z2) {
                reflectionData.declaredPublicFields = fieldArrFilterFields;
            } else {
                reflectionData.declaredFields = fieldArrFilterFields;
            }
        }
        return fieldArrFilterFields;
    }

    private Field[] privateGetPublicFields(Set<Class<?>> set) {
        Class<? super T> superclass;
        Field[] fieldArr;
        checkInitted();
        ReflectionData<T> reflectionData = reflectionData();
        if (reflectionData != null && (fieldArr = reflectionData.publicFields) != null) {
            return fieldArr;
        }
        ArrayList arrayList = new ArrayList();
        if (set == null) {
            set = new HashSet();
        }
        addAll(arrayList, privateGetDeclaredFields(true));
        for (Class<?> cls : getInterfaces()) {
            if (!set.contains(cls)) {
                set.add(cls);
                addAll(arrayList, cls.privateGetPublicFields(set));
            }
        }
        if (!isInterface() && (superclass = getSuperclass()) != null) {
            addAll(arrayList, superclass.privateGetPublicFields(set));
        }
        Field[] fieldArr2 = new Field[arrayList.size()];
        arrayList.toArray(fieldArr2);
        if (reflectionData != null) {
            reflectionData.publicFields = fieldArr2;
        }
        return fieldArr2;
    }

    private static void addAll(Collection<Field> collection, Field[] fieldArr) {
        for (Field field : fieldArr) {
            collection.add(field);
        }
    }

    private Constructor<T>[] privateGetDeclaredConstructors(boolean z2) {
        Constructor<T>[] declaredConstructors0;
        checkInitted();
        ReflectionData<T> reflectionData = reflectionData();
        if (reflectionData != null) {
            Constructor<T>[] constructorArr = z2 ? reflectionData.publicConstructors : reflectionData.declaredConstructors;
            if (constructorArr != null) {
                return constructorArr;
            }
        }
        if (isInterface()) {
            declaredConstructors0 = new Constructor[0];
        } else {
            declaredConstructors0 = getDeclaredConstructors0(z2);
        }
        if (reflectionData != null) {
            if (z2) {
                reflectionData.publicConstructors = declaredConstructors0;
            } else {
                reflectionData.declaredConstructors = declaredConstructors0;
            }
        }
        return declaredConstructors0;
    }

    private Method[] privateGetDeclaredMethods(boolean z2) {
        checkInitted();
        ReflectionData<T> reflectionData = reflectionData();
        if (reflectionData != null) {
            Method[] methodArr = z2 ? reflectionData.declaredPublicMethods : reflectionData.declaredMethods;
            if (methodArr != null) {
                return methodArr;
            }
        }
        Method[] methodArrFilterMethods = Reflection.filterMethods(this, getDeclaredMethods0(z2));
        if (reflectionData != null) {
            if (z2) {
                reflectionData.declaredPublicMethods = methodArrFilterMethods;
            } else {
                reflectionData.declaredMethods = methodArrFilterMethods;
            }
        }
        return methodArrFilterMethods;
    }

    /* loaded from: rt.jar:java/lang/Class$MethodArray.class */
    static class MethodArray {
        private Method[] methods;
        private int length;
        private int defaults;

        MethodArray() {
            this(20);
        }

        MethodArray(int i2) {
            if (i2 < 2) {
                throw new IllegalArgumentException("Size should be 2 or more");
            }
            this.methods = new Method[i2];
            this.length = 0;
            this.defaults = 0;
        }

        boolean hasDefaults() {
            return this.defaults != 0;
        }

        void add(Method method) {
            if (this.length == this.methods.length) {
                this.methods = (Method[]) Arrays.copyOf(this.methods, 2 * this.methods.length);
            }
            Method[] methodArr = this.methods;
            int i2 = this.length;
            this.length = i2 + 1;
            methodArr[i2] = method;
            if (method != null && method.isDefault()) {
                this.defaults++;
            }
        }

        void addAll(Method[] methodArr) {
            for (Method method : methodArr) {
                add(method);
            }
        }

        void addAll(MethodArray methodArray) {
            for (int i2 = 0; i2 < methodArray.length(); i2++) {
                add(methodArray.get(i2));
            }
        }

        void addIfNotPresent(Method method) {
            for (int i2 = 0; i2 < this.length; i2++) {
                Method method2 = this.methods[i2];
                if (method2 == method) {
                    return;
                }
                if (method2 != null && method2.equals(method)) {
                    return;
                }
            }
            add(method);
        }

        void addAllIfNotPresent(MethodArray methodArray) {
            for (int i2 = 0; i2 < methodArray.length(); i2++) {
                Method method = methodArray.get(i2);
                if (method != null) {
                    addIfNotPresent(method);
                }
            }
        }

        void addInterfaceMethods(Method[] methodArr) {
            for (Method method : methodArr) {
                if (!Modifier.isStatic(method.getModifiers())) {
                    add(method);
                }
            }
        }

        int length() {
            return this.length;
        }

        Method get(int i2) {
            return this.methods[i2];
        }

        Method getFirst() {
            for (Method method : this.methods) {
                if (method != null) {
                    return method;
                }
            }
            return null;
        }

        void removeByNameAndDescriptor(Method method) {
            for (int i2 = 0; i2 < this.length; i2++) {
                Method method2 = this.methods[i2];
                if (method2 != null && matchesNameAndDescriptor(method2, method)) {
                    remove(i2);
                }
            }
        }

        private void remove(int i2) {
            if (this.methods[i2] != null && this.methods[i2].isDefault()) {
                this.defaults--;
            }
            this.methods[i2] = null;
        }

        private boolean matchesNameAndDescriptor(Method method, Method method2) {
            return method.getReturnType() == method2.getReturnType() && method.getName() == method2.getName() && Class.arrayContentsEq(method.getParameterTypes(), method2.getParameterTypes());
        }

        void compactAndTrim() {
            int i2 = 0;
            for (int i3 = 0; i3 < this.length; i3++) {
                Method method = this.methods[i3];
                if (method != null) {
                    if (i3 != i2) {
                        this.methods[i2] = method;
                    }
                    i2++;
                }
            }
            if (i2 != this.methods.length) {
                this.methods = (Method[]) Arrays.copyOf(this.methods, i2);
            }
        }

        void removeLessSpecifics() {
            Method method;
            if (!hasDefaults()) {
                return;
            }
            for (int i2 = 0; i2 < this.length; i2++) {
                Method method2 = get(i2);
                if (method2 != null && method2.isDefault()) {
                    for (int i3 = 0; i3 < this.length; i3++) {
                        if (i2 != i3 && (method = get(i3)) != null && matchesNameAndDescriptor(method2, method) && hasMoreSpecificClass(method2, method)) {
                            remove(i3);
                        }
                    }
                }
            }
        }

        Method[] getArray() {
            return this.methods;
        }

        static boolean hasMoreSpecificClass(Method method, Method method2) {
            Class<?> declaringClass = method.getDeclaringClass();
            Class<?> declaringClass2 = method2.getDeclaringClass();
            return declaringClass != declaringClass2 && declaringClass2.isAssignableFrom(declaringClass);
        }
    }

    private Method[] privateGetPublicMethods() {
        Class<? super T> superclass;
        Method[] methodArr;
        checkInitted();
        ReflectionData<T> reflectionData = reflectionData();
        if (reflectionData != null && (methodArr = reflectionData.publicMethods) != null) {
            return methodArr;
        }
        MethodArray methodArray = new MethodArray();
        methodArray.addAll(privateGetDeclaredMethods(true));
        MethodArray methodArray2 = new MethodArray();
        for (Class<?> cls : getInterfaces()) {
            methodArray2.addInterfaceMethods(cls.privateGetPublicMethods());
        }
        if (!isInterface() && (superclass = getSuperclass()) != null) {
            MethodArray methodArray3 = new MethodArray();
            methodArray3.addAll(superclass.privateGetPublicMethods());
            for (int i2 = 0; i2 < methodArray3.length(); i2++) {
                Method method = methodArray3.get(i2);
                if (method != null && !Modifier.isAbstract(method.getModifiers()) && !method.isDefault()) {
                    methodArray2.removeByNameAndDescriptor(method);
                }
            }
            methodArray3.addAll(methodArray2);
            methodArray2 = methodArray3;
        }
        for (int i3 = 0; i3 < methodArray.length(); i3++) {
            methodArray2.removeByNameAndDescriptor(methodArray.get(i3));
        }
        methodArray.addAllIfNotPresent(methodArray2);
        methodArray.removeLessSpecifics();
        methodArray.compactAndTrim();
        Method[] array = methodArray.getArray();
        if (reflectionData != null) {
            reflectionData.publicMethods = array;
        }
        return array;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static Field searchFields(Field[] fieldArr, String str) {
        String strIntern = str.intern();
        for (int i2 = 0; i2 < fieldArr.length; i2++) {
            if (fieldArr[i2].getName() == strIntern) {
                return getReflectionFactory().copyField(fieldArr[i2]);
            }
        }
        return null;
    }

    private Field getField0(String str) throws NoSuchFieldException {
        Class<? super T> superclass;
        Field field0;
        Field fieldSearchFields = searchFields(privateGetDeclaredFields(true), str);
        if (fieldSearchFields != null) {
            return fieldSearchFields;
        }
        for (Class<?> cls : getInterfaces()) {
            Field field02 = cls.getField0(str);
            if (field02 != null) {
                return field02;
            }
        }
        if (!isInterface() && (superclass = getSuperclass()) != null && (field0 = superclass.getField0(str)) != null) {
            return field0;
        }
        return null;
    }

    private static Method searchMethods(Method[] methodArr, String str, Class<?>[] clsArr) {
        Method method = null;
        String strIntern = str.intern();
        for (Method method2 : methodArr) {
            if (method2.getName() == strIntern && arrayContentsEq(clsArr, method2.getParameterTypes()) && (method == null || method.getReturnType().isAssignableFrom(method2.getReturnType()))) {
                method = method2;
            }
        }
        return method == null ? method : getReflectionFactory().copyMethod(method);
    }

    private Method getMethod0(String str, Class<?>[] clsArr, boolean z2) {
        MethodArray methodArray = new MethodArray(2);
        Method methodPrivateGetMethodRecursive = privateGetMethodRecursive(str, clsArr, z2, methodArray);
        if (methodPrivateGetMethodRecursive != null) {
            return methodPrivateGetMethodRecursive;
        }
        methodArray.removeLessSpecifics();
        return methodArray.getFirst();
    }

    private Method privateGetMethodRecursive(String str, Class<?>[] clsArr, boolean z2, MethodArray methodArray) {
        Class<? super T> superclass;
        Method method0;
        Method methodSearchMethods = searchMethods(privateGetDeclaredMethods(true), str, clsArr);
        if (methodSearchMethods != null && (z2 || !Modifier.isStatic(methodSearchMethods.getModifiers()))) {
            return methodSearchMethods;
        }
        if (!isInterface() && (superclass = getSuperclass()) != null && (method0 = superclass.getMethod0(str, clsArr, true)) != null) {
            return method0;
        }
        for (Class<?> cls : getInterfaces()) {
            Method method02 = cls.getMethod0(str, clsArr, false);
            if (method02 != null) {
                methodArray.add(method02);
            }
        }
        return null;
    }

    private Constructor<T> getConstructor0(Class<?>[] clsArr, int i2) throws NoSuchMethodException {
        for (Constructor<T> constructor : privateGetDeclaredConstructors(i2 == 0)) {
            if (arrayContentsEq(clsArr, constructor.getParameterTypes())) {
                return getReflectionFactory().copyConstructor(constructor);
            }
        }
        throw new NoSuchMethodException(getName() + ".<init>" + argumentTypesToString(clsArr));
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static boolean arrayContentsEq(Object[] objArr, Object[] objArr2) {
        if (objArr == null) {
            return objArr2 == null || objArr2.length == 0;
        }
        if (objArr2 == null) {
            return objArr.length == 0;
        }
        if (objArr.length != objArr2.length) {
            return false;
        }
        for (int i2 = 0; i2 < objArr.length; i2++) {
            if (objArr[i2] != objArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    private static Field[] copyFields(Field[] fieldArr) {
        Field[] fieldArr2 = new Field[fieldArr.length];
        ReflectionFactory reflectionFactory2 = getReflectionFactory();
        for (int i2 = 0; i2 < fieldArr.length; i2++) {
            fieldArr2[i2] = reflectionFactory2.copyField(fieldArr[i2]);
        }
        return fieldArr2;
    }

    private static Method[] copyMethods(Method[] methodArr) {
        Method[] methodArr2 = new Method[methodArr.length];
        ReflectionFactory reflectionFactory2 = getReflectionFactory();
        for (int i2 = 0; i2 < methodArr.length; i2++) {
            methodArr2[i2] = reflectionFactory2.copyMethod(methodArr[i2]);
        }
        return methodArr2;
    }

    private static <U> Constructor<U>[] copyConstructors(Constructor<U>[] constructorArr) {
        Constructor<U>[] constructorArr2 = (Constructor[]) constructorArr.clone();
        ReflectionFactory reflectionFactory2 = getReflectionFactory();
        for (int i2 = 0; i2 < constructorArr2.length; i2++) {
            constructorArr2[i2] = reflectionFactory2.copyConstructor(constructorArr2[i2]);
        }
        return constructorArr2;
    }

    private static String argumentTypesToString(Class<?>[] clsArr) {
        StringBuilder sb = new StringBuilder();
        sb.append("(");
        if (clsArr != null) {
            for (int i2 = 0; i2 < clsArr.length; i2++) {
                if (i2 > 0) {
                    sb.append(", ");
                }
                Class<?> cls = clsArr[i2];
                sb.append(cls == null ? FXMLLoader.NULL_KEYWORD : cls.getName());
            }
        }
        sb.append(")");
        return sb.toString();
    }

    public boolean desiredAssertionStatus() {
        ClassLoader classLoader = getClassLoader();
        if (classLoader == null) {
            return desiredAssertionStatus0(this);
        }
        synchronized (classLoader.assertionLock) {
            if (classLoader.classAssertionStatus != null) {
                return classLoader.desiredAssertionStatus(getName());
            }
            return desiredAssertionStatus0(this);
        }
    }

    public boolean isEnum() {
        return (getModifiers() & 16384) != 0 && getSuperclass() == Enum.class;
    }

    private static ReflectionFactory getReflectionFactory() {
        if (reflectionFactory == null) {
            reflectionFactory = (ReflectionFactory) AccessController.doPrivileged(new ReflectionFactory.GetReflectionFactoryAction());
        }
        return reflectionFactory;
    }

    private static void checkInitted() {
        if (initted) {
            return;
        }
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.lang.Class.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public Void run() {
                if (System.out == null) {
                    return null;
                }
                String property = System.getProperty("sun.reflect.noCaches");
                if (property != null && property.equals("true")) {
                    boolean unused = Class.useCaches = false;
                }
                boolean unused2 = Class.initted = true;
                return null;
            }
        });
    }

    public T[] getEnumConstants() {
        T[] enumConstantsShared = getEnumConstantsShared();
        if (enumConstantsShared != null) {
            return (T[]) ((Object[]) enumConstantsShared.clone());
        }
        return null;
    }

    T[] getEnumConstantsShared() {
        if (this.enumConstants == null) {
            if (!isEnum()) {
                return null;
            }
            try {
                final Method method = getMethod("values", new Class[0]);
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.lang.Class.4
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    public Void run() {
                        method.setAccessible(true);
                        return null;
                    }
                });
                this.enumConstants = (T[]) ((Object[]) method.invoke(null, new Object[0]));
            } catch (IllegalAccessException | NoSuchMethodException | InvocationTargetException e2) {
                return null;
            }
        }
        return this.enumConstants;
    }

    Map<String, T> enumConstantDirectory() {
        if (this.enumConstantDirectory == null) {
            T[] enumConstantsShared = getEnumConstantsShared();
            if (enumConstantsShared == null) {
                throw new IllegalArgumentException(getName() + " is not an enum type");
            }
            HashMap map = new HashMap(2 * enumConstantsShared.length);
            for (Enum r0 : enumConstantsShared) {
                map.put(r0.name(), r0);
            }
            this.enumConstantDirectory = map;
        }
        return this.enumConstantDirectory;
    }

    /* JADX WARN: Multi-variable type inference failed */
    public T cast(Object obj) {
        if (obj != 0 && !isInstance(obj)) {
            throw new ClassCastException(cannotCastMsg(obj));
        }
        return obj;
    }

    private String cannotCastMsg(Object obj) {
        return "Cannot cast " + obj.getClass().getName() + " to " + getName();
    }

    /* JADX WARN: Multi-variable type inference failed */
    public <U> Class<? extends U> asSubclass(Class<U> cls) {
        if (cls.isAssignableFrom(this)) {
            return this;
        }
        throw new ClassCastException(toString());
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <A extends Annotation> A getAnnotation(Class<A> cls) {
        Objects.requireNonNull(cls);
        return (A) annotationData().annotations.get(cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public boolean isAnnotationPresent(Class<? extends Annotation> cls) {
        return super.isAnnotationPresent(cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <A extends Annotation> A[] getAnnotationsByType(Class<A> cls) {
        Objects.requireNonNull(cls);
        return (A[]) AnnotationSupport.getAssociatedAnnotations(annotationData().declaredAnnotations, this, cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public Annotation[] getAnnotations() {
        return AnnotationParser.toArray(annotationData().annotations);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <A extends Annotation> A getDeclaredAnnotation(Class<A> cls) {
        Objects.requireNonNull(cls);
        return (A) annotationData().declaredAnnotations.get(cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public <A extends Annotation> A[] getDeclaredAnnotationsByType(Class<A> cls) {
        Objects.requireNonNull(cls);
        return (A[]) AnnotationSupport.getDirectlyAndIndirectlyPresent(annotationData().declaredAnnotations, cls);
    }

    @Override // java.lang.reflect.AnnotatedElement
    public Annotation[] getDeclaredAnnotations() {
        return AnnotationParser.toArray(annotationData().declaredAnnotations);
    }

    /* loaded from: rt.jar:java/lang/Class$AnnotationData.class */
    private static class AnnotationData {
        final Map<Class<? extends Annotation>, Annotation> annotations;
        final Map<Class<? extends Annotation>, Annotation> declaredAnnotations;
        final int redefinedCount;

        AnnotationData(Map<Class<? extends Annotation>, Annotation> map, Map<Class<? extends Annotation>, Annotation> map2, int i2) {
            this.annotations = map;
            this.declaredAnnotations = map2;
            this.redefinedCount = i2;
        }
    }

    private AnnotationData annotationData() {
        AnnotationData annotationData;
        AnnotationData annotationDataCreateAnnotationData;
        do {
            annotationData = this.annotationData;
            int i2 = this.classRedefinedCount;
            if (annotationData != null && annotationData.redefinedCount == i2) {
                return annotationData;
            }
            annotationDataCreateAnnotationData = createAnnotationData(i2);
        } while (!Atomic.casAnnotationData(this, annotationData, annotationDataCreateAnnotationData));
        return annotationDataCreateAnnotationData;
    }

    private AnnotationData createAnnotationData(int i2) {
        Map<? extends Class<? extends Annotation>, ? extends Annotation> annotations = AnnotationParser.parseAnnotations(getRawAnnotations(), getConstantPool(), this);
        Class<? super T> superclass = getSuperclass();
        Map<Class<? extends Annotation>, Annotation> linkedHashMap = null;
        if (superclass != null) {
            Map<Class<? extends Annotation>, Annotation> map = superclass.annotationData().annotations;
            for (Map.Entry<Class<? extends Annotation>, Annotation> entry : map.entrySet()) {
                Class<? extends Annotation> key = entry.getKey();
                if (AnnotationType.getInstance(key).isInherited()) {
                    if (linkedHashMap == null) {
                        linkedHashMap = new LinkedHashMap(((Math.max(annotations.size(), Math.min(12, annotations.size() + map.size())) * 4) + 2) / 3);
                    }
                    linkedHashMap.put(key, entry.getValue());
                }
            }
        }
        if (linkedHashMap == null) {
            linkedHashMap = annotations;
        } else {
            linkedHashMap.putAll(annotations);
        }
        return new AnnotationData(linkedHashMap, annotations, i2);
    }

    boolean casAnnotationType(AnnotationType annotationType, AnnotationType annotationType2) {
        return Atomic.casAnnotationType(this, annotationType, annotationType2);
    }

    AnnotationType getAnnotationType() {
        return this.annotationType;
    }

    Map<Class<? extends Annotation>, Annotation> getDeclaredAnnotationMap() {
        return annotationData().declaredAnnotations;
    }

    public AnnotatedType getAnnotatedSuperclass() {
        if (this == Object.class || isInterface() || isArray() || isPrimitive() || this == Void.TYPE) {
            return null;
        }
        return TypeAnnotationParser.buildAnnotatedSuperclass(getRawTypeAnnotations(), getConstantPool(), this);
    }

    public AnnotatedType[] getAnnotatedInterfaces() {
        return TypeAnnotationParser.buildAnnotatedInterfaces(getRawTypeAnnotations(), getConstantPool(), this);
    }
}
