package java.lang.invoke;

import java.lang.invoke.MethodHandles;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.PrivilegedAction;

/* loaded from: rt.jar:java/lang/invoke/InfoFromMemberName.class */
final class InfoFromMemberName implements MethodHandleInfo {
    private final MemberName member;
    private final int referenceKind;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !InfoFromMemberName.class.desiredAssertionStatus();
    }

    InfoFromMemberName(MethodHandles.Lookup lookup, MemberName memberName, byte b2) {
        if (!$assertionsDisabled && !memberName.isResolved() && !memberName.isMethodHandleInvoke()) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && !memberName.referenceKindIsConsistentWith(b2)) {
            throw new AssertionError();
        }
        this.member = memberName;
        this.referenceKind = b2;
    }

    @Override // java.lang.invoke.MethodHandleInfo
    public Class<?> getDeclaringClass() {
        return this.member.getDeclaringClass();
    }

    @Override // java.lang.invoke.MethodHandleInfo
    public String getName() {
        return this.member.getName();
    }

    @Override // java.lang.invoke.MethodHandleInfo
    public MethodType getMethodType() {
        return this.member.getMethodOrFieldType();
    }

    @Override // java.lang.invoke.MethodHandleInfo
    public int getModifiers() {
        return this.member.getModifiers();
    }

    @Override // java.lang.invoke.MethodHandleInfo
    public int getReferenceKind() {
        return this.referenceKind;
    }

    public String toString() {
        return MethodHandleInfo.toString(getReferenceKind(), getDeclaringClass(), getName(), getMethodType());
    }

    @Override // java.lang.invoke.MethodHandleInfo
    public <T extends Member> T reflectAs(Class<T> cls, MethodHandles.Lookup lookup) {
        if (this.member.isMethodHandleInvoke() && !this.member.isVarargs()) {
            throw new IllegalArgumentException("cannot reflect signature polymorphic method");
        }
        Member member = (Member) AccessController.doPrivileged(new PrivilegedAction<Member>() { // from class: java.lang.invoke.InfoFromMemberName.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Member run2() {
                try {
                    return InfoFromMemberName.this.reflectUnchecked();
                } catch (ReflectiveOperationException e2) {
                    throw new IllegalArgumentException(e2);
                }
            }
        });
        try {
            Class<?> declaringClass = getDeclaringClass();
            byte referenceKind = (byte) getReferenceKind();
            lookup.checkAccess(referenceKind, declaringClass, convertToMemberName(referenceKind, member));
            return cls.cast(member);
        } catch (IllegalAccessException e2) {
            throw new IllegalArgumentException(e2);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public Member reflectUnchecked() throws ReflectiveOperationException {
        byte referenceKind = (byte) getReferenceKind();
        Class<?> declaringClass = getDeclaringClass();
        boolean zIsPublic = Modifier.isPublic(getModifiers());
        if (MethodHandleNatives.refKindIsMethod(referenceKind)) {
            if (zIsPublic) {
                return declaringClass.getMethod(getName(), getMethodType().parameterArray());
            }
            return declaringClass.getDeclaredMethod(getName(), getMethodType().parameterArray());
        }
        if (MethodHandleNatives.refKindIsConstructor(referenceKind)) {
            if (zIsPublic) {
                return declaringClass.getConstructor(getMethodType().parameterArray());
            }
            return declaringClass.getDeclaredConstructor(getMethodType().parameterArray());
        }
        if (MethodHandleNatives.refKindIsField(referenceKind)) {
            if (zIsPublic) {
                return declaringClass.getField(getName());
            }
            return declaringClass.getDeclaredField(getName());
        }
        throw new IllegalArgumentException("referenceKind=" + ((int) referenceKind));
    }

    private static MemberName convertToMemberName(byte b2, Member member) throws IllegalAccessException {
        if (member instanceof Method) {
            return new MemberName((Method) member, b2 == 7);
        }
        if (member instanceof Constructor) {
            return new MemberName((Constructor<?>) member);
        }
        if (member instanceof Field) {
            return new MemberName((Field) member, b2 == 3 || b2 == 4);
        }
        throw new InternalError(member.getClass().getName());
    }
}
