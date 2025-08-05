package java.lang.invoke;

import com.sun.org.apache.bcel.internal.Constants;
import java.lang.invoke.LambdaForm;
import java.lang.invoke.MemberName;
import java.lang.invoke.MethodHandleImpl;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.lang.reflect.ReflectPermission;
import java.security.Permission;
import java.util.Arrays;
import java.util.BitSet;
import java.util.Iterator;
import java.util.List;
import java.util.concurrent.ConcurrentHashMap;
import sun.invoke.util.ValueConversions;
import sun.invoke.util.VerifyAccess;
import sun.invoke.util.Wrapper;
import sun.misc.VM;
import sun.reflect.CallerSensitive;
import sun.reflect.Reflection;
import sun.reflect.misc.ReflectUtil;
import sun.security.util.SecurityConstants;

/* loaded from: rt.jar:java/lang/invoke/MethodHandles.class */
public class MethodHandles {
    private static final MemberName.Factory IMPL_NAMES;
    private static final Permission ACCESS_PERMISSION;
    private static final MethodHandle[] IDENTITY_MHS;
    private static final MethodHandle[] ZERO_MHS;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MethodHandles.class.desiredAssertionStatus();
        IMPL_NAMES = MemberName.getFactory();
        MethodHandleImpl.initStatics();
        ACCESS_PERMISSION = new ReflectPermission("suppressAccessChecks");
        IDENTITY_MHS = new MethodHandle[Wrapper.values().length];
        ZERO_MHS = new MethodHandle[Wrapper.values().length];
    }

    private MethodHandles() {
    }

    @CallerSensitive
    public static Lookup lookup() {
        return new Lookup(Reflection.getCallerClass());
    }

    public static Lookup publicLookup() {
        return Lookup.PUBLIC_LOOKUP;
    }

    public static <T extends Member> T reflectAs(Class<T> cls, MethodHandle methodHandle) {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(ACCESS_PERMISSION);
        }
        Lookup lookup = Lookup.IMPL_LOOKUP;
        return (T) lookup.revealDirect(methodHandle).reflectAs(cls, lookup);
    }

    /* loaded from: rt.jar:java/lang/invoke/MethodHandles$Lookup.class */
    public static final class Lookup {
        private final Class<?> lookupClass;
        private final int allowedModes;
        public static final int PUBLIC = 1;
        public static final int PRIVATE = 2;
        public static final int PROTECTED = 4;
        public static final int PACKAGE = 8;
        private static final int ALL_MODES = 15;
        private static final int TRUSTED = -1;
        static final Lookup PUBLIC_LOOKUP;
        static final Lookup IMPL_LOOKUP;
        private static final boolean ALLOW_NESTMATE_ACCESS = false;
        static ConcurrentHashMap<MemberName, DirectMethodHandle> LOOKASIDE_TABLE;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !MethodHandles.class.desiredAssertionStatus();
            MethodHandles.IMPL_NAMES.getClass();
            PUBLIC_LOOKUP = new Lookup(Object.class, 1);
            IMPL_LOOKUP = new Lookup(Object.class, -1);
            LOOKASIDE_TABLE = new ConcurrentHashMap<>();
        }

        private static int fixmods(int i2) {
            int i3 = i2 & 7;
            if (i3 != 0) {
                return i3;
            }
            return 8;
        }

        public Class<?> lookupClass() {
            return this.lookupClass;
        }

        private Class<?> lookupClassOrNull() {
            if (this.allowedModes == -1) {
                return null;
            }
            return this.lookupClass;
        }

        public int lookupModes() {
            return this.allowedModes & 15;
        }

        Lookup(Class<?> cls) {
            this(cls, 15);
            checkUnprivilegedlookupClass(cls, 15);
        }

        private Lookup(Class<?> cls, int i2) {
            this.lookupClass = cls;
            this.allowedModes = i2;
        }

        public Lookup in(Class<?> cls) {
            cls.getClass();
            if (this.allowedModes == -1) {
                return new Lookup(cls, 15);
            }
            if (cls == this.lookupClass) {
                return this;
            }
            int i2 = this.allowedModes & 11;
            if ((i2 & 8) != 0 && !VerifyAccess.isSamePackage(this.lookupClass, cls)) {
                i2 &= -11;
            }
            if ((i2 & 2) != 0 && !VerifyAccess.isSamePackageMember(this.lookupClass, cls)) {
                i2 &= -3;
            }
            if ((i2 & 1) != 0 && !VerifyAccess.isClassAccessible(cls, this.lookupClass, this.allowedModes)) {
                i2 = 0;
            }
            checkUnprivilegedlookupClass(cls, i2);
            return new Lookup(cls, i2);
        }

        private static void checkUnprivilegedlookupClass(Class<?> cls, int i2) {
            String name = cls.getName();
            if (name.startsWith("java.lang.invoke.")) {
                throw MethodHandleStatics.newIllegalArgumentException("illegal lookupClass: " + ((Object) cls));
            }
            if (i2 == 15 && cls.getClassLoader() == null) {
                if (name.startsWith("java.") || (name.startsWith("sun.") && !name.startsWith("sun.invoke.") && !name.equals("sun.reflect.ReflectionFactory"))) {
                    throw MethodHandleStatics.newIllegalArgumentException("illegal lookupClass: " + ((Object) cls));
                }
            }
        }

        public String toString() {
            String name = this.lookupClass.getName();
            switch (this.allowedModes) {
                case -1:
                    return "/trusted";
                case 0:
                    return name + "/noaccess";
                case 1:
                    return name + "/public";
                case 2:
                case 3:
                case 4:
                case 5:
                case 6:
                case 7:
                case 8:
                case 10:
                case 12:
                case 13:
                case 14:
                default:
                    String str = name + "/" + Integer.toHexString(this.allowedModes);
                    if ($assertionsDisabled) {
                        return str;
                    }
                    throw new AssertionError((Object) str);
                case 9:
                    return name + "/package";
                case 11:
                    return name + "/private";
                case 15:
                    return name;
            }
        }

        public MethodHandle findStatic(Class<?> cls, String str, MethodType methodType) throws IllegalAccessException, NoSuchMethodException {
            MemberName memberNameResolveOrFail = resolveOrFail((byte) 6, cls, str, methodType);
            return getDirectMethod((byte) 6, cls, memberNameResolveOrFail, findBoundCallerClass(memberNameResolveOrFail));
        }

        public MethodHandle findVirtual(Class<?> cls, String str, MethodType methodType) throws IllegalAccessException, NoSuchMethodException {
            MethodHandle methodHandleFindVirtualForMH;
            if (cls == MethodHandle.class && (methodHandleFindVirtualForMH = findVirtualForMH(str, methodType)) != null) {
                return methodHandleFindVirtualForMH;
            }
            byte b2 = cls.isInterface() ? (byte) 9 : (byte) 5;
            MemberName memberNameResolveOrFail = resolveOrFail(b2, cls, str, methodType);
            return getDirectMethod(b2, cls, memberNameResolveOrFail, findBoundCallerClass(memberNameResolveOrFail));
        }

        private MethodHandle findVirtualForMH(String str, MethodType methodType) {
            if ("invoke".equals(str)) {
                return MethodHandles.invoker(methodType);
            }
            if ("invokeExact".equals(str)) {
                return MethodHandles.exactInvoker(methodType);
            }
            if ($assertionsDisabled || !MemberName.isMethodHandleInvokeName(str)) {
                return null;
            }
            throw new AssertionError();
        }

        public MethodHandle findConstructor(Class<?> cls, MethodType methodType) throws IllegalAccessException, NoSuchMethodException {
            if (cls.isArray()) {
                throw new NoSuchMethodException("no constructor for array class: " + cls.getName());
            }
            return getDirectConstructor(cls, resolveOrFail((byte) 8, cls, Constants.CONSTRUCTOR_NAME, methodType));
        }

        public MethodHandle findSpecial(Class<?> cls, String str, MethodType methodType, Class<?> cls2) throws IllegalAccessException, NoSuchMethodException {
            checkSpecialCaller(cls2);
            Lookup lookupIn = in(cls2);
            MemberName memberNameResolveOrFail = lookupIn.resolveOrFail((byte) 7, cls, str, methodType);
            return lookupIn.getDirectMethod((byte) 7, cls, memberNameResolveOrFail, findBoundCallerClass(memberNameResolveOrFail));
        }

        public MethodHandle findGetter(Class<?> cls, String str, Class<?> cls2) throws IllegalAccessException, NoSuchFieldException {
            return getDirectField((byte) 1, cls, resolveOrFail((byte) 1, cls, str, cls2));
        }

        public MethodHandle findSetter(Class<?> cls, String str, Class<?> cls2) throws IllegalAccessException, NoSuchFieldException {
            return getDirectField((byte) 3, cls, resolveOrFail((byte) 3, cls, str, cls2));
        }

        public MethodHandle findStaticGetter(Class<?> cls, String str, Class<?> cls2) throws IllegalAccessException, NoSuchFieldException {
            return getDirectField((byte) 2, cls, resolveOrFail((byte) 2, cls, str, cls2));
        }

        public MethodHandle findStaticSetter(Class<?> cls, String str, Class<?> cls2) throws IllegalAccessException, NoSuchFieldException {
            return getDirectField((byte) 4, cls, resolveOrFail((byte) 4, cls, str, cls2));
        }

        public MethodHandle bind(Object obj, String str, MethodType methodType) throws IllegalAccessException, NoSuchMethodException {
            Class<?> cls = obj.getClass();
            MemberName memberNameResolveOrFail = resolveOrFail((byte) 7, cls, str, methodType);
            return getDirectMethodNoRestrict((byte) 7, cls, memberNameResolveOrFail, findBoundCallerClass(memberNameResolveOrFail)).bindArgumentL(0, obj).setVarargs(memberNameResolveOrFail);
        }

        public MethodHandle unreflect(Method method) throws IllegalAccessException {
            MethodHandle methodHandleUnreflectForMH;
            if (method.getDeclaringClass() == MethodHandle.class && (methodHandleUnreflectForMH = unreflectForMH(method)) != null) {
                return methodHandleUnreflectForMH;
            }
            MemberName memberName = new MemberName(method);
            byte referenceKind = memberName.getReferenceKind();
            if (referenceKind == 7) {
                referenceKind = 5;
            }
            if ($assertionsDisabled || memberName.isMethod()) {
                return (method.isAccessible() ? IMPL_LOOKUP : this).getDirectMethodNoSecurityManager(referenceKind, memberName.getDeclaringClass(), memberName, findBoundCallerClass(memberName));
            }
            throw new AssertionError();
        }

        private MethodHandle unreflectForMH(Method method) {
            if (MemberName.isMethodHandleInvokeName(method.getName())) {
                return MethodHandleImpl.fakeMethodHandleInvoke(new MemberName(method));
            }
            return null;
        }

        public MethodHandle unreflectSpecial(Method method, Class<?> cls) throws IllegalAccessException {
            checkSpecialCaller(cls);
            Lookup lookupIn = in(cls);
            MemberName memberName = new MemberName(method, true);
            if ($assertionsDisabled || memberName.isMethod()) {
                return lookupIn.getDirectMethodNoSecurityManager((byte) 7, memberName.getDeclaringClass(), memberName, findBoundCallerClass(memberName));
            }
            throw new AssertionError();
        }

        public MethodHandle unreflectConstructor(Constructor<?> constructor) throws IllegalAccessException {
            MemberName memberName = new MemberName(constructor);
            if ($assertionsDisabled || memberName.isConstructor()) {
                return (constructor.isAccessible() ? IMPL_LOOKUP : this).getDirectConstructorNoSecurityManager(memberName.getDeclaringClass(), memberName);
            }
            throw new AssertionError();
        }

        public MethodHandle unreflectGetter(Field field) throws IllegalAccessException {
            return unreflectField(field, false);
        }

        private MethodHandle unreflectField(Field field, boolean z2) throws IllegalAccessException {
            MemberName memberName = new MemberName(field, z2);
            if (!$assertionsDisabled && (!z2 ? !MethodHandleNatives.refKindIsGetter(memberName.getReferenceKind()) : !MethodHandleNatives.refKindIsSetter(memberName.getReferenceKind()))) {
                throw new AssertionError();
            }
            return (field.isAccessible() ? IMPL_LOOKUP : this).getDirectFieldNoSecurityManager(memberName.getReferenceKind(), field.getDeclaringClass(), memberName);
        }

        public MethodHandle unreflectSetter(Field field) throws IllegalAccessException {
            return unreflectField(field, true);
        }

        public MethodHandleInfo revealDirect(MethodHandle methodHandle) {
            MemberName memberNameInternalMemberName = methodHandle.internalMemberName();
            if (memberNameInternalMemberName == null || (!memberNameInternalMemberName.isResolved() && !memberNameInternalMemberName.isMethodHandleInvoke())) {
                throw MethodHandleStatics.newIllegalArgumentException("not a direct method handle");
            }
            Class<?> declaringClass = memberNameInternalMemberName.getDeclaringClass();
            byte referenceKind = memberNameInternalMemberName.getReferenceKind();
            if (!$assertionsDisabled && !MethodHandleNatives.refKindIsValid(referenceKind)) {
                throw new AssertionError();
            }
            if (referenceKind == 7 && !methodHandle.isInvokeSpecial()) {
                referenceKind = 5;
            }
            if (referenceKind == 5 && declaringClass.isInterface()) {
                referenceKind = 9;
            }
            try {
                checkAccess(referenceKind, declaringClass, memberNameInternalMemberName);
                checkSecurityManager(declaringClass, memberNameInternalMemberName);
                if (this.allowedModes != -1 && memberNameInternalMemberName.isCallerSensitive()) {
                    Class<?> clsInternalCallerClass = methodHandle.internalCallerClass();
                    if (!hasPrivateAccess() || clsInternalCallerClass != lookupClass()) {
                        throw new IllegalArgumentException("method handle is caller sensitive: " + ((Object) clsInternalCallerClass));
                    }
                }
                return new InfoFromMemberName(this, memberNameInternalMemberName, referenceKind);
            } catch (IllegalAccessException e2) {
                throw new IllegalArgumentException(e2);
            }
        }

        MemberName resolveOrFail(byte b2, Class<?> cls, String str, Class<?> cls2) throws IllegalAccessException, NoSuchFieldException {
            checkSymbolicClass(cls);
            str.getClass();
            cls2.getClass();
            return MethodHandles.IMPL_NAMES.resolveOrFail(b2, new MemberName(cls, str, cls2, b2), lookupClassOrNull(), NoSuchFieldException.class);
        }

        MemberName resolveOrFail(byte b2, Class<?> cls, String str, MethodType methodType) throws IllegalAccessException, NoSuchMethodException {
            checkSymbolicClass(cls);
            str.getClass();
            methodType.getClass();
            checkMethodName(b2, str);
            return MethodHandles.IMPL_NAMES.resolveOrFail(b2, new MemberName(cls, str, methodType, b2), lookupClassOrNull(), NoSuchMethodException.class);
        }

        MemberName resolveOrFail(byte b2, MemberName memberName) throws ReflectiveOperationException {
            checkSymbolicClass(memberName.getDeclaringClass());
            memberName.getName().getClass();
            memberName.getType().getClass();
            return MethodHandles.IMPL_NAMES.resolveOrFail(b2, memberName, lookupClassOrNull(), ReflectiveOperationException.class);
        }

        void checkSymbolicClass(Class<?> cls) throws IllegalAccessException {
            cls.getClass();
            Class<?> clsLookupClassOrNull = lookupClassOrNull();
            if (clsLookupClassOrNull != null && !VerifyAccess.isClassAccessible(cls, clsLookupClassOrNull, this.allowedModes)) {
                throw new MemberName(cls).makeAccessException("symbolic reference class is not public", this);
            }
        }

        void checkMethodName(byte b2, String str) throws NoSuchMethodException {
            if (str.startsWith("<") && b2 != 8) {
                throw new NoSuchMethodException("illegal method name: " + str);
            }
        }

        Class<?> findBoundCallerClass(MemberName memberName) throws IllegalAccessException {
            Class<?> cls = null;
            if (MethodHandleNatives.isCallerSensitive(memberName)) {
                if (hasPrivateAccess()) {
                    cls = this.lookupClass;
                } else {
                    throw new IllegalAccessException("Attempt to lookup caller-sensitive method using restricted lookup object");
                }
            }
            return cls;
        }

        private boolean hasPrivateAccess() {
            return (this.allowedModes & 2) != 0;
        }

        void checkSecurityManager(Class<?> cls, MemberName memberName) {
            SecurityManager securityManager = System.getSecurityManager();
            if (securityManager == null || this.allowedModes == -1) {
                return;
            }
            boolean zHasPrivateAccess = hasPrivateAccess();
            if (!zHasPrivateAccess || !VerifyAccess.classLoaderIsAncestor(this.lookupClass, cls)) {
                ReflectUtil.checkPackageAccess(cls);
            }
            if (memberName.isPublic()) {
                return;
            }
            if (!zHasPrivateAccess) {
                securityManager.checkPermission(SecurityConstants.CHECK_MEMBER_ACCESS_PERMISSION);
            }
            Class<?> declaringClass = memberName.getDeclaringClass();
            if (!zHasPrivateAccess && declaringClass != cls) {
                ReflectUtil.checkPackageAccess(declaringClass);
            }
        }

        void checkMethod(byte b2, Class<?> cls, MemberName memberName) throws IllegalAccessException {
            String str;
            boolean z2 = b2 == 6;
            if (memberName.isConstructor()) {
                str = "expected a method, not a constructor";
            } else if (!memberName.isMethod()) {
                str = "expected a method";
            } else if (z2 != memberName.isStatic()) {
                str = z2 ? "expected a static method" : "expected a non-static method";
            } else {
                checkAccess(b2, cls, memberName);
                return;
            }
            throw memberName.makeAccessException(str, this);
        }

        void checkField(byte b2, Class<?> cls, MemberName memberName) throws IllegalAccessException {
            boolean z2 = !MethodHandleNatives.refKindHasReceiver(b2);
            if (z2 == memberName.isStatic()) {
                checkAccess(b2, cls, memberName);
                return;
            }
            throw memberName.makeAccessException(z2 ? "expected a static field" : "expected a non-static field", this);
        }

        void checkAccess(byte b2, Class<?> cls, MemberName memberName) throws IllegalAccessException {
            if (!$assertionsDisabled && (!memberName.referenceKindIsConsistentWith(b2) || !MethodHandleNatives.refKindIsValid(b2) || MethodHandleNatives.refKindIsField(b2) != memberName.isField())) {
                throw new AssertionError();
            }
            int i2 = this.allowedModes;
            if (i2 == -1) {
                return;
            }
            int modifiers = memberName.getModifiers();
            if (Modifier.isProtected(modifiers) && b2 == 5 && memberName.getDeclaringClass() == Object.class && memberName.getName().equals("clone") && cls.isArray()) {
                modifiers ^= 5;
            }
            if (Modifier.isProtected(modifiers) && b2 == 8) {
                modifiers ^= 4;
            }
            if (Modifier.isFinal(modifiers) && MethodHandleNatives.refKindIsSetter(b2)) {
                throw memberName.makeAccessException("unexpected set of a final field", this);
            }
            if (Modifier.isPublic(modifiers) && Modifier.isPublic(cls.getModifiers()) && i2 != 0) {
                return;
            }
            int iFixmods = fixmods(modifiers);
            if ((iFixmods & i2) != 0) {
                if (VerifyAccess.isMemberAccessible(cls, memberName.getDeclaringClass(), modifiers, lookupClass(), i2)) {
                    return;
                }
            } else if ((iFixmods & 4) != 0 && (i2 & 8) != 0 && VerifyAccess.isSamePackage(memberName.getDeclaringClass(), lookupClass())) {
                return;
            }
            throw memberName.makeAccessException(accessFailedMessage(cls, memberName), this);
        }

        String accessFailedMessage(Class<?> cls, MemberName memberName) {
            Class<?> declaringClass = memberName.getDeclaringClass();
            int modifiers = memberName.getModifiers();
            boolean z2 = Modifier.isPublic(declaringClass.getModifiers()) && (declaringClass == cls || Modifier.isPublic(cls.getModifiers()));
            if (!z2 && (this.allowedModes & 8) != 0) {
                z2 = VerifyAccess.isClassAccessible(declaringClass, lookupClass(), 15) && (declaringClass == cls || VerifyAccess.isClassAccessible(cls, lookupClass(), 15));
            }
            if (!z2) {
                return "class is not public";
            }
            if (Modifier.isPublic(modifiers)) {
                return "access to public member failed";
            }
            if (Modifier.isPrivate(modifiers)) {
                return "member is private";
            }
            if (Modifier.isProtected(modifiers)) {
                return "member is protected";
            }
            return "member is private to package";
        }

        private void checkSpecialCaller(Class<?> cls) throws IllegalAccessException {
            if (this.allowedModes == -1) {
                return;
            }
            if (!hasPrivateAccess() || cls != lookupClass()) {
                throw new MemberName(cls).makeAccessException("no private access for invokespecial", this);
            }
        }

        private boolean restrictProtectedReceiver(MemberName memberName) {
            if (!memberName.isProtected() || memberName.isStatic() || this.allowedModes == -1 || memberName.getDeclaringClass() == lookupClass() || VerifyAccess.isSamePackage(memberName.getDeclaringClass(), lookupClass())) {
                return false;
            }
            return true;
        }

        private MethodHandle restrictReceiver(MemberName memberName, DirectMethodHandle directMethodHandle, Class<?> cls) throws IllegalAccessException {
            if (!$assertionsDisabled && memberName.isStatic()) {
                throw new AssertionError();
            }
            if (!memberName.getDeclaringClass().isAssignableFrom(cls)) {
                throw memberName.makeAccessException("caller class must be a subclass below the method", cls);
            }
            MethodType methodTypeType = directMethodHandle.type();
            if (methodTypeType.parameterType(0) == cls) {
                return directMethodHandle;
            }
            MethodType methodTypeChangeParameterType = methodTypeType.changeParameterType(0, cls);
            if (!$assertionsDisabled && directMethodHandle.isVarargsCollector()) {
                throw new AssertionError();
            }
            if ($assertionsDisabled || directMethodHandle.viewAsTypeChecks(methodTypeChangeParameterType, true)) {
                return directMethodHandle.copyWith(methodTypeChangeParameterType, directMethodHandle.form);
            }
            throw new AssertionError();
        }

        private MethodHandle getDirectMethod(byte b2, Class<?> cls, MemberName memberName, Class<?> cls2) throws IllegalAccessException {
            return getDirectMethodCommon(b2, cls, memberName, true, true, cls2);
        }

        private MethodHandle getDirectMethodNoRestrict(byte b2, Class<?> cls, MemberName memberName, Class<?> cls2) throws IllegalAccessException {
            return getDirectMethodCommon(b2, cls, memberName, true, false, cls2);
        }

        private MethodHandle getDirectMethodNoSecurityManager(byte b2, Class<?> cls, MemberName memberName, Class<?> cls2) throws IllegalAccessException {
            return getDirectMethodCommon(b2, cls, memberName, false, true, cls2);
        }

        private MethodHandle getDirectMethodCommon(byte b2, Class<?> cls, MemberName memberName, boolean z2, boolean z3, Class<?> cls2) throws IllegalAccessException {
            MemberName memberNameResolveOrNull;
            checkMethod(b2, cls, memberName);
            if (z2) {
                checkSecurityManager(cls, memberName);
            }
            if (!$assertionsDisabled && memberName.isMethodHandleInvoke()) {
                throw new AssertionError();
            }
            if (b2 == 7 && cls != lookupClass() && !cls.isInterface() && cls != lookupClass().getSuperclass() && cls.isAssignableFrom(lookupClass())) {
                if (!$assertionsDisabled && memberName.getName().equals(Constants.CONSTRUCTOR_NAME)) {
                    throw new AssertionError();
                }
                Class<?> clsLookupClass = lookupClass();
                do {
                    clsLookupClass = clsLookupClass.getSuperclass();
                    memberNameResolveOrNull = MethodHandles.IMPL_NAMES.resolveOrNull(b2, new MemberName(clsLookupClass, memberName.getName(), memberName.getMethodType(), (byte) 7), lookupClassOrNull());
                    if (memberNameResolveOrNull != null) {
                        break;
                    }
                } while (cls != clsLookupClass);
                if (memberNameResolveOrNull == null) {
                    throw new InternalError(memberName.toString());
                }
                memberName = memberNameResolveOrNull;
                cls = clsLookupClass;
                checkMethod(b2, cls, memberName);
            }
            DirectMethodHandle directMethodHandleMake = DirectMethodHandle.make(b2, cls, memberName);
            MethodHandle methodHandleRestrictReceiver = directMethodHandleMake;
            if (z3 && (b2 == 7 || (MethodHandleNatives.refKindHasReceiver(b2) && restrictProtectedReceiver(memberName)))) {
                methodHandleRestrictReceiver = restrictReceiver(memberName, directMethodHandleMake, lookupClass());
            }
            return maybeBindCaller(memberName, methodHandleRestrictReceiver, cls2).setVarargs(memberName);
        }

        private MethodHandle maybeBindCaller(MemberName memberName, MethodHandle methodHandle, Class<?> cls) throws IllegalAccessException {
            if (this.allowedModes == -1 || !MethodHandleNatives.isCallerSensitive(memberName)) {
                return methodHandle;
            }
            Class<?> cls2 = this.lookupClass;
            if (!hasPrivateAccess()) {
                cls2 = cls;
            }
            return MethodHandleImpl.bindCaller(methodHandle, cls2);
        }

        private MethodHandle getDirectField(byte b2, Class<?> cls, MemberName memberName) throws IllegalAccessException {
            return getDirectFieldCommon(b2, cls, memberName, true);
        }

        private MethodHandle getDirectFieldNoSecurityManager(byte b2, Class<?> cls, MemberName memberName) throws IllegalAccessException {
            return getDirectFieldCommon(b2, cls, memberName, false);
        }

        private MethodHandle getDirectFieldCommon(byte b2, Class<?> cls, MemberName memberName, boolean z2) throws IllegalAccessException {
            checkField(b2, cls, memberName);
            if (z2) {
                checkSecurityManager(cls, memberName);
            }
            DirectMethodHandle directMethodHandleMake = DirectMethodHandle.make(cls, memberName);
            if (MethodHandleNatives.refKindHasReceiver(b2) && restrictProtectedReceiver(memberName)) {
                return restrictReceiver(memberName, directMethodHandleMake, lookupClass());
            }
            return directMethodHandleMake;
        }

        private MethodHandle getDirectConstructor(Class<?> cls, MemberName memberName) throws IllegalAccessException {
            return getDirectConstructorCommon(cls, memberName, true);
        }

        private MethodHandle getDirectConstructorNoSecurityManager(Class<?> cls, MemberName memberName) throws IllegalAccessException {
            return getDirectConstructorCommon(cls, memberName, false);
        }

        private MethodHandle getDirectConstructorCommon(Class<?> cls, MemberName memberName, boolean z2) throws IllegalAccessException {
            if (!$assertionsDisabled && !memberName.isConstructor()) {
                throw new AssertionError();
            }
            checkAccess((byte) 8, cls, memberName);
            if (z2) {
                checkSecurityManager(cls, memberName);
            }
            if ($assertionsDisabled || !MethodHandleNatives.isCallerSensitive(memberName)) {
                return DirectMethodHandle.make(memberName).setVarargs(memberName);
            }
            throw new AssertionError();
        }

        MethodHandle linkMethodHandleConstant(byte b2, Class<?> cls, String str, Object obj) throws ReflectiveOperationException {
            MethodHandle methodHandleFindVirtualForMH;
            if (!(obj instanceof Class) && !(obj instanceof MethodType)) {
                throw new InternalError("unresolved MemberName");
            }
            MemberName memberName = new MemberName(b2, cls, str, obj);
            DirectMethodHandle directMethodHandle = LOOKASIDE_TABLE.get(memberName);
            if (directMethodHandle != null) {
                checkSymbolicClass(cls);
                return directMethodHandle;
            }
            if (cls == MethodHandle.class && b2 == 5 && (methodHandleFindVirtualForMH = findVirtualForMH(memberName.getName(), memberName.getMethodType())) != null) {
                return methodHandleFindVirtualForMH;
            }
            MemberName memberNameResolveOrFail = resolveOrFail(b2, memberName);
            MethodHandle directMethodForConstant = getDirectMethodForConstant(b2, cls, memberNameResolveOrFail);
            if ((directMethodForConstant instanceof DirectMethodHandle) && canBeCached(b2, cls, memberNameResolveOrFail)) {
                MemberName memberNameInternalMemberName = directMethodForConstant.internalMemberName();
                if (memberNameInternalMemberName != null) {
                    memberNameInternalMemberName = memberNameInternalMemberName.asNormalOriginal();
                }
                if (memberName.equals(memberNameInternalMemberName)) {
                    LOOKASIDE_TABLE.put(memberNameInternalMemberName, (DirectMethodHandle) directMethodForConstant);
                }
            }
            return directMethodForConstant;
        }

        private boolean canBeCached(byte b2, Class<?> cls, MemberName memberName) {
            if (b2 == 7 || !Modifier.isPublic(cls.getModifiers()) || !Modifier.isPublic(memberName.getDeclaringClass().getModifiers()) || !memberName.isPublic() || memberName.isCallerSensitive()) {
                return false;
            }
            ClassLoader classLoader = cls.getClassLoader();
            if (!VM.isSystemDomainLoader(classLoader)) {
                ClassLoader systemClassLoader = ClassLoader.getSystemClassLoader();
                boolean z2 = false;
                while (true) {
                    if (systemClassLoader == null) {
                        break;
                    }
                    if (classLoader == systemClassLoader) {
                        z2 = true;
                        break;
                    }
                    systemClassLoader = systemClassLoader.getParent();
                }
                if (!z2) {
                    return false;
                }
            }
            try {
                checkSecurityManager(cls, MethodHandles.publicLookup().resolveOrFail(b2, new MemberName(b2, cls, memberName.getName(), memberName.getType())));
                return true;
            } catch (ReflectiveOperationException | SecurityException e2) {
                return false;
            }
        }

        private MethodHandle getDirectMethodForConstant(byte b2, Class<?> cls, MemberName memberName) throws ReflectiveOperationException {
            if (MethodHandleNatives.refKindIsField(b2)) {
                return getDirectFieldNoSecurityManager(b2, cls, memberName);
            }
            if (MethodHandleNatives.refKindIsMethod(b2)) {
                return getDirectMethodNoSecurityManager(b2, cls, memberName, this.lookupClass);
            }
            if (b2 == 8) {
                return getDirectConstructorNoSecurityManager(cls, memberName);
            }
            throw MethodHandleStatics.newIllegalArgumentException("bad MethodHandle constant #" + ((Object) memberName));
        }
    }

    public static MethodHandle arrayElementGetter(Class<?> cls) throws IllegalArgumentException {
        return MethodHandleImpl.makeArrayElementAccessor(cls, false);
    }

    public static MethodHandle arrayElementSetter(Class<?> cls) throws IllegalArgumentException {
        return MethodHandleImpl.makeArrayElementAccessor(cls, true);
    }

    public static MethodHandle spreadInvoker(MethodType methodType, int i2) {
        if (i2 < 0 || i2 > methodType.parameterCount()) {
            throw MethodHandleStatics.newIllegalArgumentException("bad argument count", Integer.valueOf(i2));
        }
        return methodType.asSpreaderType(Object[].class, methodType.parameterCount() - i2).invokers().spreadInvoker(i2);
    }

    public static MethodHandle exactInvoker(MethodType methodType) {
        return methodType.invokers().exactInvoker();
    }

    public static MethodHandle invoker(MethodType methodType) {
        return methodType.invokers().genericInvoker();
    }

    static MethodHandle basicInvoker(MethodType methodType) {
        return methodType.invokers().basicInvoker();
    }

    public static MethodHandle explicitCastArguments(MethodHandle methodHandle, MethodType methodType) {
        explicitCastArgumentsChecks(methodHandle, methodType);
        MethodType methodTypeType = methodHandle.type();
        if (methodTypeType == methodType) {
            return methodHandle;
        }
        if (methodTypeType.explicitCastEquivalentToAsType(methodType)) {
            return methodHandle.asFixedArity().asType(methodType);
        }
        return MethodHandleImpl.makePairwiseConvert(methodHandle, methodType, false);
    }

    private static void explicitCastArgumentsChecks(MethodHandle methodHandle, MethodType methodType) {
        if (methodHandle.type().parameterCount() != methodType.parameterCount()) {
            throw new WrongMethodTypeException("cannot explicitly cast " + ((Object) methodHandle) + " to " + ((Object) methodType));
        }
    }

    public static MethodHandle permuteArguments(MethodHandle methodHandle, MethodType methodType, int... iArr) {
        int[] iArrCopyOf = (int[]) iArr.clone();
        MethodType methodTypeType = methodHandle.type();
        permuteArgumentChecks(iArrCopyOf, methodType, methodTypeType);
        BoundMethodHandle boundMethodHandleRebind = methodHandle.rebind();
        LambdaForm lambdaFormAddArgumentForm = boundMethodHandleRebind.form;
        int iParameterCount = methodType.parameterCount();
        while (true) {
            int iFindFirstDupOrDrop = findFirstDupOrDrop(iArrCopyOf, iParameterCount);
            if (iFindFirstDupOrDrop != 0) {
                if (iFindFirstDupOrDrop > 0) {
                    int i2 = iFindFirstDupOrDrop;
                    int i3 = i2;
                    int i4 = iArrCopyOf[i2];
                    boolean z2 = false;
                    while (true) {
                        i3--;
                        int i5 = iArrCopyOf[i3];
                        if (i5 == i4) {
                            break;
                        }
                        if (i4 > i5) {
                            z2 = true;
                        }
                    }
                    if (!z2) {
                        i2 = i3;
                        i3 = iFindFirstDupOrDrop;
                    }
                    lambdaFormAddArgumentForm = lambdaFormAddArgumentForm.editor().dupArgumentForm(1 + i2, 1 + i3);
                    if (!$assertionsDisabled && iArrCopyOf[i2] != iArrCopyOf[i3]) {
                        throw new AssertionError();
                    }
                    methodTypeType = methodTypeType.dropParameterTypes(i3, i3 + 1);
                    int i6 = i3 + 1;
                    System.arraycopy(iArrCopyOf, i6, iArrCopyOf, i3, iArrCopyOf.length - i6);
                    iArrCopyOf = Arrays.copyOf(iArrCopyOf, iArrCopyOf.length - 1);
                } else {
                    int i7 = iFindFirstDupOrDrop ^ (-1);
                    int i8 = 0;
                    while (i8 < iArrCopyOf.length && iArrCopyOf[i8] < i7) {
                        i8++;
                    }
                    Class<?> clsParameterType = methodType.parameterType(i7);
                    lambdaFormAddArgumentForm = lambdaFormAddArgumentForm.editor().addArgumentForm(1 + i8, LambdaForm.BasicType.basicType(clsParameterType));
                    methodTypeType = methodTypeType.insertParameterTypes(i8, clsParameterType);
                    int i9 = i8 + 1;
                    iArrCopyOf = Arrays.copyOf(iArrCopyOf, iArrCopyOf.length + 1);
                    System.arraycopy(iArrCopyOf, i8, iArrCopyOf, i9, iArrCopyOf.length - i9);
                    iArrCopyOf[i8] = i7;
                }
                if (!$assertionsDisabled && !permuteArgumentChecks(iArrCopyOf, methodType, methodTypeType)) {
                    throw new AssertionError();
                }
            } else {
                if (!$assertionsDisabled && iArrCopyOf.length != iParameterCount) {
                    throw new AssertionError();
                }
                LambdaForm lambdaFormPermuteArgumentsForm = lambdaFormAddArgumentForm.editor().permuteArgumentsForm(1, iArrCopyOf);
                if (methodType == boundMethodHandleRebind.type() && lambdaFormPermuteArgumentsForm == boundMethodHandleRebind.internalForm()) {
                    return boundMethodHandleRebind;
                }
                return boundMethodHandleRebind.copyWith(methodType, lambdaFormPermuteArgumentsForm);
            }
        }
    }

    private static int findFirstDupOrDrop(int[] iArr, int i2) {
        if (i2 < 63) {
            long j2 = 0;
            for (int i3 = 0; i3 < iArr.length; i3++) {
                int i4 = iArr[i3];
                if (i4 >= i2) {
                    return iArr.length;
                }
                long j3 = 1 << i4;
                if ((j2 & j3) != 0) {
                    return i3;
                }
                j2 |= j3;
            }
            if (j2 == (1 << i2) - 1) {
                if ($assertionsDisabled || Long.numberOfTrailingZeros(Long.lowestOneBit(j2 ^ (-1))) == i2) {
                    return 0;
                }
                throw new AssertionError();
            }
            int iNumberOfTrailingZeros = Long.numberOfTrailingZeros(Long.lowestOneBit(j2 ^ (-1)));
            if (!$assertionsDisabled && iNumberOfTrailingZeros > i2) {
                throw new AssertionError();
            }
            if (iNumberOfTrailingZeros == i2) {
                return 0;
            }
            return iNumberOfTrailingZeros ^ (-1);
        }
        BitSet bitSet = new BitSet(i2);
        for (int i5 = 0; i5 < iArr.length; i5++) {
            int i6 = iArr[i5];
            if (i6 >= i2) {
                return iArr.length;
            }
            if (bitSet.get(i6)) {
                return i5;
            }
            bitSet.set(i6);
        }
        int iNextClearBit = bitSet.nextClearBit(0);
        if (!$assertionsDisabled && iNextClearBit > i2) {
            throw new AssertionError();
        }
        if (iNextClearBit == i2) {
            return 0;
        }
        return iNextClearBit ^ (-1);
    }

    private static boolean permuteArgumentChecks(int[] iArr, MethodType methodType, MethodType methodType2) {
        if (methodType.returnType() != methodType2.returnType()) {
            throw MethodHandleStatics.newIllegalArgumentException("return types do not match", methodType2, methodType);
        }
        if (iArr.length == methodType2.parameterCount()) {
            int iParameterCount = methodType.parameterCount();
            boolean z2 = false;
            for (int i2 = 0; i2 < iArr.length; i2++) {
                int i3 = iArr[i2];
                if (i3 < 0 || i3 >= iParameterCount) {
                    z2 = true;
                    break;
                }
                if (methodType.parameterType(i3) != methodType2.parameterType(i2)) {
                    throw MethodHandleStatics.newIllegalArgumentException("parameter types do not match after reorder", methodType2, methodType);
                }
            }
            if (!z2) {
                return true;
            }
        }
        throw MethodHandleStatics.newIllegalArgumentException("bad reorder array: " + Arrays.toString(iArr));
    }

    public static MethodHandle constant(Class<?> cls, Object obj) {
        if (cls.isPrimitive()) {
            if (cls == Void.TYPE) {
                throw MethodHandleStatics.newIllegalArgumentException("void type");
            }
            Wrapper wrapperForPrimitiveType = Wrapper.forPrimitiveType(cls);
            Object objConvert = wrapperForPrimitiveType.convert(obj, cls);
            return wrapperForPrimitiveType.zero().equals(objConvert) ? zero(wrapperForPrimitiveType, cls) : insertArguments(identity(cls), 0, objConvert);
        }
        if (obj == null) {
            return zero(Wrapper.OBJECT, cls);
        }
        return identity(cls).bindTo(obj);
    }

    public static MethodHandle identity(Class<?> cls) {
        Wrapper wrapperForPrimitiveType = cls.isPrimitive() ? Wrapper.forPrimitiveType(cls) : Wrapper.OBJECT;
        int iOrdinal = wrapperForPrimitiveType.ordinal();
        MethodHandle cachedMethodHandle = IDENTITY_MHS[iOrdinal];
        if (cachedMethodHandle == null) {
            cachedMethodHandle = setCachedMethodHandle(IDENTITY_MHS, iOrdinal, makeIdentity(wrapperForPrimitiveType.primitiveType()));
        }
        if (cachedMethodHandle.type().returnType() == cls) {
            return cachedMethodHandle;
        }
        if ($assertionsDisabled || wrapperForPrimitiveType == Wrapper.OBJECT) {
            return makeIdentity(cls);
        }
        throw new AssertionError();
    }

    private static MethodHandle makeIdentity(Class<?> cls) {
        return MethodHandleImpl.makeIntrinsic(MethodType.methodType(cls, cls), LambdaForm.identityForm(LambdaForm.BasicType.basicType(cls)), MethodHandleImpl.Intrinsic.IDENTITY);
    }

    private static MethodHandle zero(Wrapper wrapper, Class<?> cls) {
        int iOrdinal = wrapper.ordinal();
        MethodHandle cachedMethodHandle = ZERO_MHS[iOrdinal];
        if (cachedMethodHandle == null) {
            cachedMethodHandle = setCachedMethodHandle(ZERO_MHS, iOrdinal, makeZero(wrapper.primitiveType()));
        }
        if (cachedMethodHandle.type().returnType() == cls) {
            return cachedMethodHandle;
        }
        if ($assertionsDisabled || wrapper == Wrapper.OBJECT) {
            return makeZero(cls);
        }
        throw new AssertionError();
    }

    private static MethodHandle makeZero(Class<?> cls) {
        return MethodHandleImpl.makeIntrinsic(MethodType.methodType(cls), LambdaForm.zeroForm(LambdaForm.BasicType.basicType(cls)), MethodHandleImpl.Intrinsic.ZERO);
    }

    private static synchronized MethodHandle setCachedMethodHandle(MethodHandle[] methodHandleArr, int i2, MethodHandle methodHandle) {
        MethodHandle methodHandle2 = methodHandleArr[i2];
        if (methodHandle2 != null) {
            return methodHandle2;
        }
        methodHandleArr[i2] = methodHandle;
        return methodHandle;
    }

    public static MethodHandle insertArguments(MethodHandle methodHandle, int i2, Object... objArr) throws RuntimeException {
        BoundMethodHandle boundMethodHandleBindArgumentL;
        int length = objArr.length;
        Class<?>[] clsArrInsertArgumentsChecks = insertArgumentsChecks(methodHandle, length, i2);
        if (length == 0) {
            return methodHandle;
        }
        BoundMethodHandle boundMethodHandleRebind = methodHandle.rebind();
        for (int i3 = 0; i3 < length; i3++) {
            Object obj = objArr[i3];
            Class<?> cls = clsArrInsertArgumentsChecks[i2 + i3];
            if (cls.isPrimitive()) {
                boundMethodHandleBindArgumentL = insertArgumentPrimitive(boundMethodHandleRebind, i2, cls, obj);
            } else {
                boundMethodHandleBindArgumentL = boundMethodHandleRebind.bindArgumentL(i2, cls.cast(obj));
            }
            boundMethodHandleRebind = boundMethodHandleBindArgumentL;
        }
        return boundMethodHandleRebind;
    }

    private static BoundMethodHandle insertArgumentPrimitive(BoundMethodHandle boundMethodHandle, int i2, Class<?> cls, Object obj) {
        Wrapper wrapperForPrimitiveType = Wrapper.forPrimitiveType(cls);
        Object objConvert = wrapperForPrimitiveType.convert(obj, cls);
        switch (wrapperForPrimitiveType) {
            case INT:
                return boundMethodHandle.bindArgumentI(i2, ((Integer) objConvert).intValue());
            case LONG:
                return boundMethodHandle.bindArgumentJ(i2, ((Long) objConvert).longValue());
            case FLOAT:
                return boundMethodHandle.bindArgumentF(i2, ((Float) objConvert).floatValue());
            case DOUBLE:
                return boundMethodHandle.bindArgumentD(i2, ((Double) objConvert).doubleValue());
            default:
                return boundMethodHandle.bindArgumentI(i2, ValueConversions.widenSubword(objConvert));
        }
    }

    private static Class<?>[] insertArgumentsChecks(MethodHandle methodHandle, int i2, int i3) throws RuntimeException {
        MethodType methodTypeType = methodHandle.type();
        int iParameterCount = methodTypeType.parameterCount() - i2;
        if (iParameterCount < 0) {
            throw MethodHandleStatics.newIllegalArgumentException("too many values to insert");
        }
        if (i3 < 0 || i3 > iParameterCount) {
            throw MethodHandleStatics.newIllegalArgumentException("no argument type to append");
        }
        return methodTypeType.ptypes();
    }

    public static MethodHandle dropArguments(MethodHandle methodHandle, int i2, List<Class<?>> list) {
        List<Class<?>> listCopyTypes = copyTypes(list);
        MethodType methodTypeType = methodHandle.type();
        int iDropArgumentChecks = dropArgumentChecks(methodTypeType, i2, listCopyTypes);
        MethodType methodTypeInsertParameterTypes = methodTypeType.insertParameterTypes(i2, listCopyTypes);
        if (iDropArgumentChecks == 0) {
            return methodHandle;
        }
        BoundMethodHandle boundMethodHandleRebind = methodHandle.rebind();
        LambdaForm lambdaFormAddArgumentForm = boundMethodHandleRebind.form;
        int i3 = 1 + i2;
        Iterator<Class<?>> it = listCopyTypes.iterator();
        while (it.hasNext()) {
            int i4 = i3;
            i3++;
            lambdaFormAddArgumentForm = lambdaFormAddArgumentForm.editor().addArgumentForm(i4, LambdaForm.BasicType.basicType(it.next()));
        }
        return boundMethodHandleRebind.copyWith(methodTypeInsertParameterTypes, lambdaFormAddArgumentForm);
    }

    private static List<Class<?>> copyTypes(List<Class<?>> list) {
        Object[] array = list.toArray();
        return Arrays.asList(Arrays.copyOf(array, array.length, Class[].class));
    }

    private static int dropArgumentChecks(MethodType methodType, int i2, List<Class<?>> list) {
        int size = list.size();
        MethodType.checkSlotCount(size);
        int iParameterCount = methodType.parameterCount();
        int i3 = iParameterCount + size;
        if (i2 < 0 || i2 > iParameterCount) {
            throw MethodHandleStatics.newIllegalArgumentException("no argument type to remove" + ((Object) Arrays.asList(methodType, Integer.valueOf(i2), list, Integer.valueOf(i3), Integer.valueOf(iParameterCount))));
        }
        return size;
    }

    public static MethodHandle dropArguments(MethodHandle methodHandle, int i2, Class<?>... clsArr) {
        return dropArguments(methodHandle, i2, (List<Class<?>>) Arrays.asList(clsArr));
    }

    public static MethodHandle filterArguments(MethodHandle methodHandle, int i2, MethodHandle... methodHandleArr) throws RuntimeException {
        filterArgumentsCheckArity(methodHandle, i2, methodHandleArr);
        MethodHandle methodHandleFilterArgument = methodHandle;
        int i3 = i2 - 1;
        for (MethodHandle methodHandle2 : methodHandleArr) {
            i3++;
            if (methodHandle2 != null) {
                methodHandleFilterArgument = filterArgument(methodHandleFilterArgument, i3, methodHandle2);
            }
        }
        return methodHandleFilterArgument;
    }

    static MethodHandle filterArgument(MethodHandle methodHandle, int i2, MethodHandle methodHandle2) throws RuntimeException {
        filterArgumentChecks(methodHandle, i2, methodHandle2);
        MethodType methodTypeType = methodHandle.type();
        MethodType methodTypeType2 = methodHandle2.type();
        BoundMethodHandle boundMethodHandleRebind = methodHandle.rebind();
        Class<?> clsParameterType = methodTypeType2.parameterType(0);
        return boundMethodHandleRebind.copyWithExtendL(methodTypeType.changeParameterType(i2, clsParameterType), boundMethodHandleRebind.editor().filterArgumentForm(1 + i2, LambdaForm.BasicType.basicType(clsParameterType)), methodHandle2);
    }

    private static void filterArgumentsCheckArity(MethodHandle methodHandle, int i2, MethodHandle[] methodHandleArr) {
        if (i2 + methodHandleArr.length > methodHandle.type().parameterCount()) {
            throw MethodHandleStatics.newIllegalArgumentException("too many filters");
        }
    }

    private static void filterArgumentChecks(MethodHandle methodHandle, int i2, MethodHandle methodHandle2) throws RuntimeException {
        MethodType methodTypeType = methodHandle.type();
        MethodType methodTypeType2 = methodHandle2.type();
        if (methodTypeType2.parameterCount() != 1 || methodTypeType2.returnType() != methodTypeType.parameterType(i2)) {
            throw MethodHandleStatics.newIllegalArgumentException("target and filter types do not match", methodTypeType, methodTypeType2);
        }
    }

    public static MethodHandle collectArguments(MethodHandle methodHandle, int i2, MethodHandle methodHandle2) throws RuntimeException {
        LambdaForm lambdaFormCollectArgumentArrayForm;
        MethodType methodTypeCollectArgumentsChecks = collectArgumentsChecks(methodHandle, i2, methodHandle2);
        MethodType methodTypeType = methodHandle2.type();
        BoundMethodHandle boundMethodHandleRebind = methodHandle.rebind();
        if (methodTypeType.returnType().isArray() && methodHandle2.intrinsicName() == MethodHandleImpl.Intrinsic.NEW_ARRAY && (lambdaFormCollectArgumentArrayForm = boundMethodHandleRebind.editor().collectArgumentArrayForm(1 + i2, methodHandle2)) != null) {
            return boundMethodHandleRebind.copyWith(methodTypeCollectArgumentsChecks, lambdaFormCollectArgumentArrayForm);
        }
        return boundMethodHandleRebind.copyWithExtendL(methodTypeCollectArgumentsChecks, boundMethodHandleRebind.editor().collectArgumentsForm(1 + i2, methodTypeType.basicType()), methodHandle2);
    }

    private static MethodType collectArgumentsChecks(MethodHandle methodHandle, int i2, MethodHandle methodHandle2) throws RuntimeException {
        MethodType methodTypeType = methodHandle.type();
        MethodType methodTypeType2 = methodHandle2.type();
        Class<?> clsReturnType = methodTypeType2.returnType();
        List<Class<?>> listParameterList = methodTypeType2.parameterList();
        if (clsReturnType == Void.TYPE) {
            return methodTypeType.insertParameterTypes(i2, listParameterList);
        }
        if (clsReturnType != methodTypeType.parameterType(i2)) {
            throw MethodHandleStatics.newIllegalArgumentException("target and filter types do not match", methodTypeType, methodTypeType2);
        }
        return methodTypeType.dropParameterTypes(i2, i2 + 1).insertParameterTypes(i2, listParameterList);
    }

    public static MethodHandle filterReturnValue(MethodHandle methodHandle, MethodHandle methodHandle2) throws RuntimeException {
        MethodType methodTypeType = methodHandle.type();
        MethodType methodTypeType2 = methodHandle2.type();
        filterReturnValueChecks(methodTypeType, methodTypeType2);
        BoundMethodHandle boundMethodHandleRebind = methodHandle.rebind();
        return boundMethodHandleRebind.copyWithExtendL(methodTypeType.changeReturnType(methodTypeType2.returnType()), boundMethodHandleRebind.editor().filterReturnForm(LambdaForm.BasicType.basicType(methodTypeType2.returnType()), false), methodHandle2);
    }

    private static void filterReturnValueChecks(MethodType methodType, MethodType methodType2) throws RuntimeException {
        Class<?> clsReturnType = methodType.returnType();
        int iParameterCount = methodType2.parameterCount();
        if (iParameterCount == 0) {
            if (clsReturnType == Void.TYPE) {
                return;
            }
        } else if (clsReturnType == methodType2.parameterType(0) && iParameterCount == 1) {
            return;
        }
        throw MethodHandleStatics.newIllegalArgumentException("target and filter types do not match", methodType, methodType2);
    }

    public static MethodHandle foldArguments(MethodHandle methodHandle, MethodHandle methodHandle2) {
        MethodType methodTypeType = methodHandle.type();
        MethodType methodTypeType2 = methodHandle2.type();
        Class<?> clsFoldArgumentChecks = foldArgumentChecks(0, methodTypeType, methodTypeType2);
        BoundMethodHandle boundMethodHandleRebind = methodHandle.rebind();
        boolean z2 = clsFoldArgumentChecks == Void.TYPE;
        LambdaForm lambdaFormFoldArgumentsForm = boundMethodHandleRebind.editor().foldArgumentsForm(1 + 0, z2, methodTypeType2.basicType());
        MethodType methodTypeDropParameterTypes = methodTypeType;
        if (!z2) {
            methodTypeDropParameterTypes = methodTypeDropParameterTypes.dropParameterTypes(0, 0 + 1);
        }
        return boundMethodHandleRebind.copyWithExtendL(methodTypeDropParameterTypes, lambdaFormFoldArgumentsForm, methodHandle2);
    }

    private static Class<?> foldArgumentChecks(int i2, MethodType methodType, MethodType methodType2) {
        int iParameterCount = methodType2.parameterCount();
        Class<?> clsReturnType = methodType2.returnType();
        int i3 = clsReturnType == Void.TYPE ? 0 : 1;
        int i4 = i2 + i3;
        boolean z2 = methodType.parameterCount() >= i4 + iParameterCount;
        if (z2 && !methodType2.parameterList().equals(methodType.parameterList().subList(i4, i4 + iParameterCount))) {
            z2 = false;
        }
        if (z2 && i3 != 0 && methodType2.returnType() != methodType.parameterType(0)) {
            z2 = false;
        }
        if (!z2) {
            throw misMatchedTypes("target and combiner types", methodType, methodType2);
        }
        return clsReturnType;
    }

    public static MethodHandle guardWithTest(MethodHandle methodHandle, MethodHandle methodHandle2, MethodHandle methodHandle3) {
        MethodType methodTypeType = methodHandle.type();
        MethodType methodTypeType2 = methodHandle2.type();
        MethodType methodTypeType3 = methodHandle3.type();
        if (!methodTypeType2.equals((Object) methodTypeType3)) {
            throw misMatchedTypes("target and fallback types", methodTypeType2, methodTypeType3);
        }
        if (methodTypeType.returnType() != Boolean.TYPE) {
            throw MethodHandleStatics.newIllegalArgumentException("guard type is not a predicate " + ((Object) methodTypeType));
        }
        List<Class<?>> listParameterList = methodTypeType2.parameterList();
        List<Class<?>> listParameterList2 = methodTypeType.parameterList();
        if (!listParameterList.equals(listParameterList2)) {
            int size = listParameterList2.size();
            int size2 = listParameterList.size();
            if (size >= size2 || !listParameterList.subList(0, size).equals(listParameterList2)) {
                throw misMatchedTypes("target and test types", methodTypeType2, methodTypeType);
            }
            methodHandle = dropArguments(methodHandle, size, listParameterList.subList(size, size2));
            methodHandle.type();
        }
        return MethodHandleImpl.makeGuardWithTest(methodHandle, methodHandle2, methodHandle3);
    }

    static RuntimeException misMatchedTypes(String str, MethodType methodType, MethodType methodType2) {
        return MethodHandleStatics.newIllegalArgumentException(str + " must match: " + ((Object) methodType) + " != " + ((Object) methodType2));
    }

    public static MethodHandle catchException(MethodHandle methodHandle, Class<? extends Throwable> cls, MethodHandle methodHandle2) {
        MethodType methodTypeType = methodHandle.type();
        MethodType methodTypeType2 = methodHandle2.type();
        if (methodTypeType2.parameterCount() < 1 || !methodTypeType2.parameterType(0).isAssignableFrom(cls)) {
            throw MethodHandleStatics.newIllegalArgumentException("handler does not accept exception type " + ((Object) cls));
        }
        if (methodTypeType2.returnType() != methodTypeType.returnType()) {
            throw misMatchedTypes("target and handler return types", methodTypeType, methodTypeType2);
        }
        List<Class<?>> listParameterList = methodTypeType.parameterList();
        List<Class<?>> listParameterList2 = methodTypeType2.parameterList();
        List<Class<?>> listSubList = listParameterList2.subList(1, listParameterList2.size());
        if (!listParameterList.equals(listSubList)) {
            int size = listSubList.size();
            int size2 = listParameterList.size();
            if (size >= size2 || !listParameterList.subList(0, size).equals(listSubList)) {
                throw misMatchedTypes("target and handler types", methodTypeType, methodTypeType2);
            }
            methodHandle2 = dropArguments(methodHandle2, 1 + size, listParameterList.subList(size, size2));
            methodHandle2.type();
        }
        return MethodHandleImpl.makeGuardWithCatch(methodHandle, cls, methodHandle2);
    }

    public static MethodHandle throwException(Class<?> cls, Class<? extends Throwable> cls2) {
        if (!Throwable.class.isAssignableFrom(cls2)) {
            throw new ClassCastException(cls2.getName());
        }
        return MethodHandleImpl.throwException(MethodType.methodType(cls, cls2));
    }
}
