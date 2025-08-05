package java.lang.invoke;

import com.sun.org.apache.xml.internal.dtm.DTMManager;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Objects;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.invoke.util.BytecodeDescriptor;
import sun.invoke.util.VerifyAccess;

/* loaded from: rt.jar:java/lang/invoke/MemberName.class */
final class MemberName implements Member, Cloneable {
    private Class<?> clazz;
    private String name;
    private Object type;
    private int flags;
    private Object resolution;
    private static final int MH_INVOKE_MODS = 273;
    static final int BRIDGE = 64;
    static final int VARARGS = 128;
    static final int SYNTHETIC = 4096;
    static final int ANNOTATION = 8192;
    static final int ENUM = 16384;
    static final String CONSTRUCTOR_NAME = "<init>";
    static final int RECOGNIZED_MODIFIERS = 65535;
    static final int IS_METHOD = 65536;
    static final int IS_CONSTRUCTOR = 131072;
    static final int IS_FIELD = 262144;
    static final int IS_TYPE = 524288;
    static final int CALLER_SENSITIVE = 1048576;
    static final int ALL_ACCESS = 7;
    static final int ALL_KINDS = 983040;
    static final int IS_INVOCABLE = 196608;
    static final int IS_FIELD_OR_METHOD = 327680;
    static final int SEARCH_ALL_SUPERS = 3145728;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !MemberName.class.desiredAssertionStatus();
    }

    @Override // java.lang.reflect.Member
    public Class<?> getDeclaringClass() {
        return this.clazz;
    }

    public ClassLoader getClassLoader() {
        return this.clazz.getClassLoader();
    }

    @Override // java.lang.reflect.Member
    public String getName() {
        if (this.name == null) {
            expandFromVM();
            if (this.name == null) {
                return null;
            }
        }
        return this.name;
    }

    public MethodType getMethodOrFieldType() {
        if (isInvocable()) {
            return getMethodType();
        }
        if (isGetter()) {
            return MethodType.methodType(getFieldType());
        }
        if (isSetter()) {
            return MethodType.methodType(Void.TYPE, getFieldType());
        }
        throw new InternalError("not a method or field: " + ((Object) this));
    }

    public MethodType getMethodType() {
        if (this.type == null) {
            expandFromVM();
            if (this.type == null) {
                return null;
            }
        }
        if (!isInvocable()) {
            throw MethodHandleStatics.newIllegalArgumentException("not invocable, no method type");
        }
        Object obj = this.type;
        if (obj instanceof MethodType) {
            return (MethodType) obj;
        }
        synchronized (this) {
            if (this.type instanceof String) {
                this.type = MethodType.fromMethodDescriptorString((String) this.type, getClassLoader());
            } else if (this.type instanceof Object[]) {
                Object[] objArr = (Object[]) this.type;
                this.type = MethodType.methodType((Class<?>) objArr[0], (Class<?>[]) objArr[1]);
            }
            if (!$assertionsDisabled && !(this.type instanceof MethodType)) {
                throw new AssertionError((Object) ("bad method type " + this.type));
            }
        }
        return (MethodType) this.type;
    }

    public MethodType getInvocationType() {
        MethodType methodOrFieldType = getMethodOrFieldType();
        if (isConstructor() && getReferenceKind() == 8) {
            return methodOrFieldType.changeReturnType(this.clazz);
        }
        if (!isStatic()) {
            return methodOrFieldType.insertParameterTypes(0, this.clazz);
        }
        return methodOrFieldType;
    }

    public Class<?>[] getParameterTypes() {
        return getMethodType().parameterArray();
    }

    public Class<?> getReturnType() {
        return getMethodType().returnType();
    }

    public Class<?> getFieldType() {
        if (this.type == null) {
            expandFromVM();
            if (this.type == null) {
                return null;
            }
        }
        if (isInvocable()) {
            throw MethodHandleStatics.newIllegalArgumentException("not a field or nested class, no simple type");
        }
        Object obj = this.type;
        if (obj instanceof Class) {
            return (Class) obj;
        }
        synchronized (this) {
            if (this.type instanceof String) {
                this.type = MethodType.fromMethodDescriptorString("()" + ((String) this.type), getClassLoader()).returnType();
            }
            if (!$assertionsDisabled && !(this.type instanceof Class)) {
                throw new AssertionError((Object) ("bad field type " + this.type));
            }
        }
        return (Class) this.type;
    }

    public Object getType() {
        return isInvocable() ? getMethodType() : getFieldType();
    }

    public String getSignature() {
        if (this.type == null) {
            expandFromVM();
            if (this.type == null) {
                return null;
            }
        }
        if (isInvocable()) {
            return BytecodeDescriptor.unparse(getMethodType());
        }
        return BytecodeDescriptor.unparse(getFieldType());
    }

    @Override // java.lang.reflect.Member
    public int getModifiers() {
        return this.flags & 65535;
    }

    public byte getReferenceKind() {
        return (byte) ((this.flags >>> 24) & 15);
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean referenceKindIsConsistent() {
        byte referenceKind = getReferenceKind();
        if (referenceKind == 0) {
            return isType();
        }
        if (isField()) {
            if (!$assertionsDisabled && !staticIsConsistent()) {
                throw new AssertionError();
            }
            if ($assertionsDisabled || MethodHandleNatives.refKindIsField(referenceKind)) {
                return true;
            }
            throw new AssertionError();
        }
        if (isConstructor()) {
            if ($assertionsDisabled || referenceKind == 8 || referenceKind == 7) {
                return true;
            }
            throw new AssertionError();
        }
        if (isMethod()) {
            if (!$assertionsDisabled && !staticIsConsistent()) {
                throw new AssertionError();
            }
            if (!$assertionsDisabled && !MethodHandleNatives.refKindIsMethod(referenceKind)) {
                throw new AssertionError();
            }
            if (this.clazz.isInterface() && !$assertionsDisabled && referenceKind != 9 && referenceKind != 6 && referenceKind != 7) {
                if (referenceKind != 5 || !isObjectPublicMethod()) {
                    throw new AssertionError();
                }
                return true;
            }
            return true;
        }
        if ($assertionsDisabled) {
            return true;
        }
        throw new AssertionError();
    }

    private boolean isObjectPublicMethod() {
        if (this.clazz == Object.class) {
            return true;
        }
        MethodType methodType = getMethodType();
        if (this.name.equals("toString") && methodType.returnType() == String.class && methodType.parameterCount() == 0) {
            return true;
        }
        if (this.name.equals("hashCode") && methodType.returnType() == Integer.TYPE && methodType.parameterCount() == 0) {
            return true;
        }
        if (this.name.equals("equals") && methodType.returnType() == Boolean.TYPE && methodType.parameterCount() == 1 && methodType.parameterType(0) == Object.class) {
            return true;
        }
        return false;
    }

    boolean referenceKindIsConsistentWith(int i2) {
        byte referenceKind = getReferenceKind();
        if (referenceKind == i2) {
            return true;
        }
        switch (i2) {
            case 5:
            case 8:
                if ($assertionsDisabled || referenceKind == 7) {
                    return true;
                }
                throw new AssertionError(this);
            case 6:
            case 7:
            default:
                if ($assertionsDisabled) {
                    return true;
                }
                throw new AssertionError((Object) (((Object) this) + " != " + MethodHandleNatives.refKindName((byte) i2)));
            case 9:
                if ($assertionsDisabled || referenceKind == 5 || referenceKind == 7) {
                    return true;
                }
                throw new AssertionError(this);
        }
    }

    private boolean staticIsConsistent() {
        return MethodHandleNatives.refKindIsStatic(getReferenceKind()) == isStatic() || getModifiers() == 0;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean vminfoIsConsistent() {
        byte referenceKind = getReferenceKind();
        if (!$assertionsDisabled && !isResolved()) {
            throw new AssertionError();
        }
        Object memberVMInfo = MethodHandleNatives.getMemberVMInfo(this);
        if (!$assertionsDisabled && !(memberVMInfo instanceof Object[])) {
            throw new AssertionError();
        }
        long jLongValue = ((Long) ((Object[]) memberVMInfo)[0]).longValue();
        Object obj = ((Object[]) memberVMInfo)[1];
        if (MethodHandleNatives.refKindIsField(referenceKind)) {
            if (!$assertionsDisabled && jLongValue < 0) {
                throw new AssertionError((Object) (jLongValue + CallSiteDescriptor.TOKEN_DELIMITER + ((Object) this)));
            }
            if ($assertionsDisabled || (obj instanceof Class)) {
                return true;
            }
            throw new AssertionError();
        }
        if (MethodHandleNatives.refKindDoesDispatch(referenceKind)) {
            if (!$assertionsDisabled && jLongValue < 0) {
                throw new AssertionError((Object) (jLongValue + CallSiteDescriptor.TOKEN_DELIMITER + ((Object) this)));
            }
        } else if (!$assertionsDisabled && jLongValue >= 0) {
            throw new AssertionError(jLongValue);
        }
        if ($assertionsDisabled || (obj instanceof MemberName)) {
            return true;
        }
        throw new AssertionError((Object) (obj + " in " + ((Object) this)));
    }

    private MemberName changeReferenceKind(byte b2, byte b3) {
        if (!$assertionsDisabled && getReferenceKind() != b3) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !MethodHandleNatives.refKindIsValid(b2)) {
            throw new AssertionError();
        }
        this.flags += (b2 - b3) << 24;
        return this;
    }

    private boolean testFlags(int i2, int i3) {
        return (this.flags & i2) == i3;
    }

    private boolean testAllFlags(int i2) {
        return testFlags(i2, i2);
    }

    private boolean testAnyFlags(int i2) {
        return !testFlags(i2, 0);
    }

    public boolean isMethodHandleInvoke() {
        if (testFlags(280, 272) && this.clazz == MethodHandle.class) {
            return isMethodHandleInvokeName(this.name);
        }
        return false;
    }

    public static boolean isMethodHandleInvokeName(String str) {
        switch (str) {
            case "invoke":
            case "invokeExact":
                return true;
            default:
                return false;
        }
    }

    public boolean isStatic() {
        return Modifier.isStatic(this.flags);
    }

    public boolean isPublic() {
        return Modifier.isPublic(this.flags);
    }

    public boolean isPrivate() {
        return Modifier.isPrivate(this.flags);
    }

    public boolean isProtected() {
        return Modifier.isProtected(this.flags);
    }

    public boolean isFinal() {
        return Modifier.isFinal(this.flags);
    }

    public boolean canBeStaticallyBound() {
        return Modifier.isFinal(this.flags | this.clazz.getModifiers());
    }

    public boolean isVolatile() {
        return Modifier.isVolatile(this.flags);
    }

    public boolean isAbstract() {
        return Modifier.isAbstract(this.flags);
    }

    public boolean isNative() {
        return Modifier.isNative(this.flags);
    }

    public boolean isBridge() {
        return testAllFlags(65600);
    }

    public boolean isVarargs() {
        return testAllFlags(128) && isInvocable();
    }

    @Override // java.lang.reflect.Member
    public boolean isSynthetic() {
        return testAllFlags(4096);
    }

    public boolean isInvocable() {
        return testAnyFlags(IS_INVOCABLE);
    }

    public boolean isFieldOrMethod() {
        return testAnyFlags(327680);
    }

    public boolean isMethod() {
        return testAllFlags(65536);
    }

    public boolean isConstructor() {
        return testAllFlags(131072);
    }

    public boolean isField() {
        return testAllFlags(262144);
    }

    public boolean isType() {
        return testAllFlags(524288);
    }

    public boolean isPackage() {
        return !testAnyFlags(7);
    }

    public boolean isCallerSensitive() {
        return testAllFlags(1048576);
    }

    public boolean isAccessibleFrom(Class<?> cls) {
        return VerifyAccess.isMemberAccessible(getDeclaringClass(), getDeclaringClass(), this.flags, cls, 15);
    }

    private void init(Class<?> cls, String str, Object obj, int i2) {
        this.clazz = cls;
        this.name = str;
        this.type = obj;
        this.flags = i2;
        if (!$assertionsDisabled && !testAnyFlags(ALL_KINDS)) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.resolution != null) {
            throw new AssertionError();
        }
    }

    private void expandFromVM() {
        if (this.type != null || !isResolved()) {
            return;
        }
        MethodHandleNatives.expand(this);
    }

    private static int flagsMods(int i2, int i3, byte b2) {
        if (!$assertionsDisabled && (i2 & 65535) != 0) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && (i3 & DTMManager.IDENT_DTM_DEFAULT) != 0) {
            throw new AssertionError();
        }
        if ($assertionsDisabled || (b2 & (-16)) == 0) {
            return i2 | i3 | (b2 << 24);
        }
        throw new AssertionError();
    }

    public MemberName(Method method) {
        this(method, false);
    }

    public MemberName(Method method, boolean z2) {
        method.getClass();
        MethodHandleNatives.init(this, method);
        if (this.clazz == null) {
            if (method.getDeclaringClass() == MethodHandle.class && isMethodHandleInvokeName(method.getName())) {
                init(MethodHandle.class, method.getName(), MethodType.methodType(method.getReturnType(), method.getParameterTypes()), flagsMods(65536, method.getModifiers(), (byte) 5));
                if (isMethodHandleInvoke()) {
                    return;
                }
            }
            throw new LinkageError(method.toString());
        }
        if (!$assertionsDisabled && (!isResolved() || this.clazz == null)) {
            throw new AssertionError();
        }
        this.name = method.getName();
        if (this.type == null) {
            this.type = new Object[]{method.getReturnType(), method.getParameterTypes()};
        }
        if (z2) {
            if (isAbstract()) {
                throw new AbstractMethodError(toString());
            }
            if (getReferenceKind() == 5) {
                changeReferenceKind((byte) 7, (byte) 5);
            } else if (getReferenceKind() == 9) {
                changeReferenceKind((byte) 7, (byte) 9);
            }
        }
    }

    public MemberName asSpecial() {
        switch (getReferenceKind()) {
            case 5:
                return m3020clone().changeReferenceKind((byte) 7, (byte) 5);
            case 6:
            default:
                throw new IllegalArgumentException(toString());
            case 7:
                return this;
            case 8:
                return m3020clone().changeReferenceKind((byte) 7, (byte) 8);
            case 9:
                return m3020clone().changeReferenceKind((byte) 7, (byte) 9);
        }
    }

    public MemberName asConstructor() {
        switch (getReferenceKind()) {
            case 7:
                return m3020clone().changeReferenceKind((byte) 8, (byte) 7);
            case 8:
                return this;
            default:
                throw new IllegalArgumentException(toString());
        }
    }

    public MemberName asNormalOriginal() {
        byte b2 = this.clazz.isInterface() ? (byte) 9 : (byte) 5;
        byte referenceKind = getReferenceKind();
        byte b3 = referenceKind;
        switch (referenceKind) {
            case 5:
            case 7:
            case 9:
                b3 = b2;
                break;
        }
        if (b3 == referenceKind) {
            return this;
        }
        MemberName memberNameChangeReferenceKind = m3020clone().changeReferenceKind(b3, referenceKind);
        if ($assertionsDisabled || referenceKindIsConsistentWith(memberNameChangeReferenceKind.getReferenceKind())) {
            return memberNameChangeReferenceKind;
        }
        throw new AssertionError();
    }

    public MemberName(Constructor<?> constructor) {
        constructor.getClass();
        MethodHandleNatives.init(this, constructor);
        if (!$assertionsDisabled && (!isResolved() || this.clazz == null)) {
            throw new AssertionError();
        }
        this.name = "<init>";
        if (this.type == null) {
            this.type = new Object[]{Void.TYPE, constructor.getParameterTypes()};
        }
    }

    public MemberName(Field field) {
        this(field, false);
    }

    public MemberName(Field field, boolean z2) {
        field.getClass();
        MethodHandleNatives.init(this, field);
        if (!$assertionsDisabled && (!isResolved() || this.clazz == null)) {
            throw new AssertionError();
        }
        this.name = field.getName();
        this.type = field.getType();
        byte referenceKind = getReferenceKind();
        if (!$assertionsDisabled) {
            if (referenceKind != (isStatic() ? (byte) 2 : (byte) 1)) {
                throw new AssertionError();
            }
        }
        if (z2) {
            changeReferenceKind((byte) (referenceKind + 2), referenceKind);
        }
    }

    public boolean isGetter() {
        return MethodHandleNatives.refKindIsGetter(getReferenceKind());
    }

    public boolean isSetter() {
        return MethodHandleNatives.refKindIsSetter(getReferenceKind());
    }

    public MemberName asSetter() {
        byte referenceKind = getReferenceKind();
        if (!$assertionsDisabled && !MethodHandleNatives.refKindIsGetter(referenceKind)) {
            throw new AssertionError();
        }
        return m3020clone().changeReferenceKind((byte) (referenceKind + 2), referenceKind);
    }

    public MemberName(Class<?> cls) {
        init(cls.getDeclaringClass(), cls.getSimpleName(), cls, flagsMods(524288, cls.getModifiers(), (byte) 0));
        initResolved(true);
    }

    static MemberName makeMethodHandleInvoke(String str, MethodType methodType) {
        return makeMethodHandleInvoke(str, methodType, 4369);
    }

    static MemberName makeMethodHandleInvoke(String str, MethodType methodType, int i2) {
        MemberName memberName = new MemberName((Class<?>) MethodHandle.class, str, methodType, (byte) 5);
        memberName.flags |= i2;
        if ($assertionsDisabled || memberName.isMethodHandleInvoke()) {
            return memberName;
        }
        throw new AssertionError(memberName);
    }

    MemberName() {
    }

    /* JADX INFO: Access modifiers changed from: protected */
    /* renamed from: clone, reason: merged with bridge method [inline-methods] */
    public MemberName m3020clone() {
        try {
            return (MemberName) super.clone();
        } catch (CloneNotSupportedException e2) {
            throw MethodHandleStatics.newInternalError(e2);
        }
    }

    public MemberName getDefinition() {
        if (!isResolved()) {
            throw new IllegalStateException("must be resolved: " + ((Object) this));
        }
        if (isType()) {
            return this;
        }
        MemberName memberNameM3020clone = m3020clone();
        memberNameM3020clone.clazz = null;
        memberNameM3020clone.type = null;
        memberNameM3020clone.name = null;
        memberNameM3020clone.resolution = memberNameM3020clone;
        memberNameM3020clone.expandFromVM();
        if ($assertionsDisabled || memberNameM3020clone.getName().equals(getName())) {
            return memberNameM3020clone;
        }
        throw new AssertionError();
    }

    public int hashCode() {
        return Objects.hash(this.clazz, Byte.valueOf(getReferenceKind()), this.name, getType());
    }

    public boolean equals(Object obj) {
        return (obj instanceof MemberName) && equals((MemberName) obj);
    }

    public boolean equals(MemberName memberName) {
        if (this == memberName) {
            return true;
        }
        return memberName != null && this.clazz == memberName.clazz && getReferenceKind() == memberName.getReferenceKind() && Objects.equals(this.name, memberName.name) && Objects.equals(getType(), memberName.getType());
    }

    public MemberName(Class<?> cls, String str, Class<?> cls2, byte b2) {
        init(cls, str, cls2, flagsMods(262144, 0, b2));
        initResolved(false);
    }

    public MemberName(Class<?> cls, String str, MethodType methodType, byte b2) {
        init(cls, str, methodType, flagsMods((str == null || !str.equals("<init>")) ? 65536 : 131072, 0, b2));
        initResolved(false);
    }

    public MemberName(byte b2, Class<?> cls, String str, Object obj) {
        int i2;
        if (MethodHandleNatives.refKindIsField(b2)) {
            i2 = 262144;
            if (!(obj instanceof Class)) {
                throw MethodHandleStatics.newIllegalArgumentException("not a field type");
            }
        } else if (MethodHandleNatives.refKindIsMethod(b2)) {
            i2 = 65536;
            if (!(obj instanceof MethodType)) {
                throw MethodHandleStatics.newIllegalArgumentException("not a method type");
            }
        } else if (b2 == 8) {
            i2 = 131072;
            if (!(obj instanceof MethodType) || !"<init>".equals(str)) {
                throw MethodHandleStatics.newIllegalArgumentException("not a constructor type or name");
            }
        } else {
            throw MethodHandleStatics.newIllegalArgumentException("bad reference kind " + ((int) b2));
        }
        init(cls, str, obj, flagsMods(i2, 0, b2));
        initResolved(false);
    }

    public boolean hasReceiverTypeDispatch() {
        return MethodHandleNatives.refKindDoesDispatch(getReferenceKind());
    }

    public boolean isResolved() {
        return this.resolution == null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void initResolved(boolean z2) {
        if (!$assertionsDisabled && this.resolution != null) {
            throw new AssertionError();
        }
        if (!z2) {
            this.resolution = this;
        }
        if (!$assertionsDisabled && isResolved() != z2) {
            throw new AssertionError();
        }
    }

    void checkForTypeAlias(Class<?> cls) {
        Class<?> cls2;
        MethodType methodType;
        if (isInvocable()) {
            if (this.type instanceof MethodType) {
                methodType = (MethodType) this.type;
            } else {
                MethodType methodType2 = getMethodType();
                methodType = methodType2;
                this.type = methodType2;
            }
            if (methodType.erase() != methodType && !VerifyAccess.isTypeVisible(methodType, cls)) {
                throw new LinkageError("bad method type alias: " + ((Object) methodType) + " not visible from " + ((Object) cls));
            }
            return;
        }
        if (this.type instanceof Class) {
            cls2 = (Class) this.type;
        } else {
            Class<?> fieldType = getFieldType();
            cls2 = fieldType;
            this.type = fieldType;
        }
        if (!VerifyAccess.isTypeVisible(cls2, cls)) {
            throw new LinkageError("bad field type alias: " + ((Object) cls2) + " not visible from " + ((Object) cls));
        }
    }

    public String toString() {
        if (isType()) {
            return this.type.toString();
        }
        StringBuilder sb = new StringBuilder();
        if (getDeclaringClass() != null) {
            sb.append(getName(this.clazz));
            sb.append('.');
        }
        String name = getName();
        sb.append(name == null ? "*" : name);
        Object type = getType();
        if (!isInvocable()) {
            sb.append('/');
            sb.append(type == null ? "*" : getName(type));
        } else {
            sb.append(type == null ? "(*)*" : getName(type));
        }
        byte referenceKind = getReferenceKind();
        if (referenceKind != 0) {
            sb.append('/');
            sb.append(MethodHandleNatives.refKindName(referenceKind));
        }
        return sb.toString();
    }

    private static String getName(Object obj) {
        if (obj instanceof Class) {
            return ((Class) obj).getName();
        }
        return String.valueOf(obj);
    }

    public IllegalAccessException makeAccessException(String str, Object obj) {
        String str2 = str + ": " + toString();
        if (obj != null) {
            str2 = str2 + ", from " + obj;
        }
        return new IllegalAccessException(str2);
    }

    private String message() {
        if (isResolved()) {
            return "no access";
        }
        if (isConstructor()) {
            return "no such constructor";
        }
        if (isMethod()) {
            return "no such method";
        }
        return "no such field";
    }

    public ReflectiveOperationException makeAccessException() {
        ReflectiveOperationException illegalAccessException;
        String str = message() + ": " + toString();
        if (isResolved() || (!(this.resolution instanceof NoSuchMethodError) && !(this.resolution instanceof NoSuchFieldError))) {
            illegalAccessException = new IllegalAccessException(str);
        } else if (isConstructor() || isMethod()) {
            illegalAccessException = new NoSuchMethodException(str);
        } else {
            illegalAccessException = new NoSuchFieldException(str);
        }
        if (this.resolution instanceof Throwable) {
            illegalAccessException.initCause((Throwable) this.resolution);
        }
        return illegalAccessException;
    }

    static Factory getFactory() {
        return Factory.INSTANCE;
    }

    /* loaded from: rt.jar:java/lang/invoke/MemberName$Factory.class */
    static class Factory {
        static Factory INSTANCE;
        private static int ALLOWED_FLAGS;
        static final /* synthetic */ boolean $assertionsDisabled;

        static {
            $assertionsDisabled = !MemberName.class.desiredAssertionStatus();
            INSTANCE = new Factory();
            ALLOWED_FLAGS = MemberName.ALL_KINDS;
        }

        private Factory() {
        }

        List<MemberName> getMembers(Class<?> cls, String str, Object obj, int i2, Class<?> cls2) {
            int members;
            int i3 = i2 & ALLOWED_FLAGS;
            String strUnparse = null;
            if (obj != null) {
                strUnparse = BytecodeDescriptor.unparse(obj);
                if (strUnparse.startsWith("(")) {
                    i3 &= -786433;
                } else {
                    i3 &= -720897;
                }
            }
            MemberName[] memberNameArrNewMemberBuffer = newMemberBuffer(str == null ? 10 : obj == null ? 4 : 1);
            int length = 0;
            ArrayList arrayList = null;
            while (true) {
                members = MethodHandleNatives.getMembers(cls, str, strUnparse, i3, cls2, length, memberNameArrNewMemberBuffer);
                if (members <= memberNameArrNewMemberBuffer.length) {
                    break;
                }
                length += memberNameArrNewMemberBuffer.length;
                int length2 = members - memberNameArrNewMemberBuffer.length;
                if (arrayList == null) {
                    arrayList = new ArrayList(1);
                }
                arrayList.add(memberNameArrNewMemberBuffer);
                memberNameArrNewMemberBuffer = newMemberBuffer(Math.min(8192, Math.max(Math.max(memberNameArrNewMemberBuffer.length, length2), length / 4)));
            }
            if (members < 0) {
                members = 0;
            }
            ArrayList arrayList2 = new ArrayList(length + members);
            if (arrayList != null) {
                Iterator it = arrayList.iterator();
                while (it.hasNext()) {
                    Collections.addAll(arrayList2, (MemberName[]) it.next());
                }
            }
            arrayList2.addAll(Arrays.asList(memberNameArrNewMemberBuffer).subList(0, members));
            if (obj != null && obj != strUnparse) {
                Iterator it2 = arrayList2.iterator();
                while (it2.hasNext()) {
                    if (!obj.equals(((MemberName) it2.next()).getType())) {
                        it2.remove();
                    }
                }
            }
            return arrayList2;
        }

        private MemberName resolve(byte b2, MemberName memberName, Class<?> cls) {
            MemberName memberNameM3020clone = memberName.m3020clone();
            if (!$assertionsDisabled && b2 != memberNameM3020clone.getReferenceKind()) {
                throw new AssertionError();
            }
            try {
                memberNameM3020clone = MethodHandleNatives.resolve(memberNameM3020clone, cls);
                memberNameM3020clone.checkForTypeAlias(memberNameM3020clone.getDeclaringClass());
                memberNameM3020clone.resolution = null;
                if (!$assertionsDisabled && !memberNameM3020clone.referenceKindIsConsistent()) {
                    throw new AssertionError();
                }
                memberNameM3020clone.initResolved(true);
                if ($assertionsDisabled || memberNameM3020clone.vminfoIsConsistent()) {
                    return memberNameM3020clone;
                }
                throw new AssertionError();
            } catch (ClassNotFoundException | LinkageError e2) {
                if (!$assertionsDisabled && memberNameM3020clone.isResolved()) {
                    throw new AssertionError();
                }
                memberNameM3020clone.resolution = e2;
                return memberNameM3020clone;
            }
        }

        public <NoSuchMemberException extends ReflectiveOperationException> MemberName resolveOrFail(byte b2, MemberName memberName, Class<?> cls, Class<NoSuchMemberException> cls2) throws ReflectiveOperationException {
            MemberName memberNameResolve = resolve(b2, memberName, cls);
            if (memberNameResolve.isResolved()) {
                return memberNameResolve;
            }
            ReflectiveOperationException reflectiveOperationExceptionMakeAccessException = memberNameResolve.makeAccessException();
            if (reflectiveOperationExceptionMakeAccessException instanceof IllegalAccessException) {
                throw ((IllegalAccessException) reflectiveOperationExceptionMakeAccessException);
            }
            throw cls2.cast(reflectiveOperationExceptionMakeAccessException);
        }

        public MemberName resolveOrNull(byte b2, MemberName memberName, Class<?> cls) {
            MemberName memberNameResolve = resolve(b2, memberName, cls);
            if (memberNameResolve.isResolved()) {
                return memberNameResolve;
            }
            return null;
        }

        public List<MemberName> getMethods(Class<?> cls, boolean z2, Class<?> cls2) {
            return getMethods(cls, z2, null, null, cls2);
        }

        public List<MemberName> getMethods(Class<?> cls, boolean z2, String str, MethodType methodType, Class<?> cls2) {
            return getMembers(cls, str, methodType, 65536 | (z2 ? MemberName.SEARCH_ALL_SUPERS : 0), cls2);
        }

        public List<MemberName> getConstructors(Class<?> cls, Class<?> cls2) {
            return getMembers(cls, null, null, 131072, cls2);
        }

        public List<MemberName> getFields(Class<?> cls, boolean z2, Class<?> cls2) {
            return getFields(cls, z2, null, null, cls2);
        }

        public List<MemberName> getFields(Class<?> cls, boolean z2, String str, Class<?> cls2, Class<?> cls3) {
            return getMembers(cls, str, cls2, 262144 | (z2 ? MemberName.SEARCH_ALL_SUPERS : 0), cls3);
        }

        public List<MemberName> getNestedTypes(Class<?> cls, boolean z2, Class<?> cls2) {
            return getMembers(cls, null, null, 524288 | (z2 ? MemberName.SEARCH_ALL_SUPERS : 0), cls2);
        }

        private static MemberName[] newMemberBuffer(int i2) {
            MemberName[] memberNameArr = new MemberName[i2];
            for (int i3 = 0; i3 < i2; i3++) {
                memberNameArr[i3] = new MemberName();
            }
            return memberNameArr;
        }
    }
}
