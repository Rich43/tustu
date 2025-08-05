package com.sun.corba.se.impl.orbutil;

import com.sun.corba.se.impl.io.ObjectStreamClass;
import com.sun.org.apache.bcel.internal.Constants;
import java.io.ByteArrayOutputStream;
import java.io.DataOutputStream;
import java.io.Externalizable;
import java.io.IOException;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.lang.reflect.Constructor;
import java.lang.reflect.Field;
import java.lang.reflect.Member;
import java.lang.reflect.Method;
import java.lang.reflect.Modifier;
import java.security.AccessController;
import java.security.DigestOutputStream;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.PrivilegedAction;
import java.util.Arrays;
import java.util.Comparator;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/ObjectStreamClassUtil_1_3.class */
public final class ObjectStreamClassUtil_1_3 {
    private static Comparator compareClassByName = new CompareClassByName();
    private static Comparator compareMemberByName = new CompareMemberByName();
    private static Method hasStaticInitializerMethod = null;

    public static long computeSerialVersionUID(Class cls) {
        long serialVersionUID = ObjectStreamClass.getSerialVersionUID(cls);
        if (serialVersionUID == 0) {
            return serialVersionUID;
        }
        return getSerialVersion(serialVersionUID, cls).longValue();
    }

    private static Long getSerialVersion(final long j2, final Class cls) {
        return (Long) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.orbutil.ObjectStreamClassUtil_1_3.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() throws SecurityException {
                long j_computeSerialVersionUID;
                try {
                    int modifiers = cls.getDeclaredField("serialVersionUID").getModifiers();
                    if (!Modifier.isStatic(modifiers) || !Modifier.isFinal(modifiers) || !Modifier.isPrivate(modifiers)) {
                        j_computeSerialVersionUID = ObjectStreamClassUtil_1_3._computeSerialVersionUID(cls);
                    } else {
                        j_computeSerialVersionUID = j2;
                    }
                } catch (NoSuchFieldException e2) {
                    j_computeSerialVersionUID = ObjectStreamClassUtil_1_3._computeSerialVersionUID(cls);
                }
                return new Long(j_computeSerialVersionUID);
            }
        });
    }

    public static long computeStructuralUID(boolean z2, Class<?> cls) {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
        long j2 = 0;
        try {
        } catch (IOException e2) {
            j2 = -1;
        } catch (NoSuchAlgorithmException e3) {
            throw new SecurityException(e3.getMessage());
        }
        if (!Serializable.class.isAssignableFrom(cls) || cls.isInterface()) {
            return 0L;
        }
        if (Externalizable.class.isAssignableFrom(cls)) {
            return 1L;
        }
        MessageDigest messageDigest = MessageDigest.getInstance("SHA");
        DataOutputStream dataOutputStream = new DataOutputStream(new DigestOutputStream(byteArrayOutputStream, messageDigest));
        Class<? super Object> superclass = cls.getSuperclass();
        if (superclass != null && superclass != Object.class) {
            boolean z3 = false;
            if (getDeclaredMethod(superclass, "writeObject", new Class[]{ObjectOutputStream.class}, 2, 8) != null) {
                z3 = true;
            }
            dataOutputStream.writeLong(computeStructuralUID(z3, superclass));
        }
        if (z2) {
            dataOutputStream.writeInt(2);
        } else {
            dataOutputStream.writeInt(1);
        }
        Field[] declaredFields = getDeclaredFields(cls);
        Arrays.sort(declaredFields, compareMemberByName);
        for (Field field : declaredFields) {
            int modifiers = field.getModifiers();
            if (!Modifier.isTransient(modifiers) && !Modifier.isStatic(modifiers)) {
                dataOutputStream.writeUTF(field.getName());
                dataOutputStream.writeUTF(getSignature(field.getType()));
            }
        }
        dataOutputStream.flush();
        for (int iMin = Math.min(8, messageDigest.digest().length); iMin > 0; iMin--) {
            j2 += (r0[iMin] & 255) << (iMin * 8);
        }
        return j2;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static long _computeSerialVersionUID(Class cls) throws SecurityException {
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(512);
        long j2 = 0;
        try {
            MessageDigest messageDigest = MessageDigest.getInstance("SHA");
            DataOutputStream dataOutputStream = new DataOutputStream(new DigestOutputStream(byteArrayOutputStream, messageDigest));
            dataOutputStream.writeUTF(cls.getName());
            int modifiers = cls.getModifiers() & ObjectStreamClass.CLASS_MASK;
            Method[] declaredMethods = cls.getDeclaredMethods();
            if ((modifiers & 512) != 0) {
                modifiers &= -1025;
                if (declaredMethods.length > 0) {
                    modifiers |= 1024;
                }
            }
            dataOutputStream.writeInt(modifiers);
            if (!cls.isArray()) {
                Class<?>[] interfaces = cls.getInterfaces();
                Arrays.sort(interfaces, compareClassByName);
                for (Class<?> cls2 : interfaces) {
                    dataOutputStream.writeUTF(cls2.getName());
                }
            }
            Field[] declaredFields = cls.getDeclaredFields();
            Arrays.sort(declaredFields, compareMemberByName);
            for (Field field : declaredFields) {
                int modifiers2 = field.getModifiers();
                if (!Modifier.isPrivate(modifiers2) || (!Modifier.isTransient(modifiers2) && !Modifier.isStatic(modifiers2))) {
                    dataOutputStream.writeUTF(field.getName());
                    dataOutputStream.writeInt(modifiers2);
                    dataOutputStream.writeUTF(getSignature(field.getType()));
                }
            }
            if (hasStaticInitializer(cls)) {
                dataOutputStream.writeUTF(Constants.STATIC_INITIALIZER_NAME);
                dataOutputStream.writeInt(8);
                dataOutputStream.writeUTF("()V");
            }
            for (MethodSignature methodSignature : MethodSignature.removePrivateAndSort(cls.getDeclaredConstructors())) {
                String strReplace = methodSignature.signature.replace('/', '.');
                dataOutputStream.writeUTF(Constants.CONSTRUCTOR_NAME);
                dataOutputStream.writeInt(methodSignature.member.getModifiers());
                dataOutputStream.writeUTF(strReplace);
            }
            for (MethodSignature methodSignature2 : MethodSignature.removePrivateAndSort(declaredMethods)) {
                String strReplace2 = methodSignature2.signature.replace('/', '.');
                dataOutputStream.writeUTF(methodSignature2.member.getName());
                dataOutputStream.writeInt(methodSignature2.member.getModifiers());
                dataOutputStream.writeUTF(strReplace2);
            }
            dataOutputStream.flush();
            for (int i2 = 0; i2 < Math.min(8, messageDigest.digest().length); i2++) {
                j2 += (r0[i2] & 255) << (i2 * 8);
            }
        } catch (IOException e2) {
            j2 = -1;
        } catch (NoSuchAlgorithmException e3) {
            throw new SecurityException(e3.getMessage());
        }
        return j2;
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/ObjectStreamClassUtil_1_3$CompareClassByName.class */
    private static class CompareClassByName implements Comparator {
        private CompareClassByName() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            return ((Class) obj).getName().compareTo(((Class) obj2).getName());
        }
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/ObjectStreamClassUtil_1_3$CompareMemberByName.class */
    private static class CompareMemberByName implements Comparator {
        private CompareMemberByName() {
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            String name = ((Member) obj).getName();
            String name2 = ((Member) obj2).getName();
            if (obj instanceof Method) {
                name = name + ObjectStreamClassUtil_1_3.getSignature((Method) obj);
                name2 = name2 + ObjectStreamClassUtil_1_3.getSignature((Method) obj2);
            } else if (obj instanceof Constructor) {
                name = name + ObjectStreamClassUtil_1_3.getSignature((Constructor) obj);
                name2 = name2 + ObjectStreamClassUtil_1_3.getSignature((Constructor) obj2);
            }
            return name.compareTo(name2);
        }
    }

    private static String getSignature(Class cls) {
        String string = null;
        if (cls.isArray()) {
            Class componentType = cls;
            int i2 = 0;
            while (componentType.isArray()) {
                i2++;
                componentType = componentType.getComponentType();
            }
            StringBuffer stringBuffer = new StringBuffer();
            for (int i3 = 0; i3 < i2; i3++) {
                stringBuffer.append("[");
            }
            stringBuffer.append(getSignature(componentType));
            string = stringBuffer.toString();
        } else if (cls.isPrimitive()) {
            if (cls == Integer.TYPE) {
                string = "I";
            } else if (cls == Byte.TYPE) {
                string = PdfOps.B_TOKEN;
            } else if (cls == Long.TYPE) {
                string = "J";
            } else if (cls == Float.TYPE) {
                string = PdfOps.F_TOKEN;
            } else if (cls == Double.TYPE) {
                string = PdfOps.D_TOKEN;
            } else if (cls == Short.TYPE) {
                string = PdfOps.S_TOKEN;
            } else if (cls == Character.TYPE) {
                string = "C";
            } else if (cls == Boolean.TYPE) {
                string = com.sun.org.apache.xalan.internal.xsltc.compiler.Constants.HASIDCALL_INDEX_SIG;
            } else if (cls == Void.TYPE) {
                string = "V";
            }
        } else {
            string = "L" + cls.getName().replace('.', '/') + ";";
        }
        return string;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getSignature(Method method) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("(");
        for (Class<?> cls : method.getParameterTypes()) {
            stringBuffer.append(getSignature(cls));
        }
        stringBuffer.append(")");
        stringBuffer.append(getSignature(method.getReturnType()));
        return stringBuffer.toString();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static String getSignature(Constructor constructor) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append("(");
        for (Class<?> cls : constructor.getParameterTypes()) {
            stringBuffer.append(getSignature(cls));
        }
        stringBuffer.append(")V");
        return stringBuffer.toString();
    }

    private static Field[] getDeclaredFields(final Class cls) {
        return (Field[]) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.orbutil.ObjectStreamClassUtil_1_3.2
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return cls.getDeclaredFields();
            }
        });
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/orbutil/ObjectStreamClassUtil_1_3$MethodSignature.class */
    private static class MethodSignature implements Comparator {
        Member member;
        String signature;

        static MethodSignature[] removePrivateAndSort(Member[] memberArr) {
            int i2 = 0;
            for (Member member : memberArr) {
                if (!Modifier.isPrivate(member.getModifiers())) {
                    i2++;
                }
            }
            MethodSignature[] methodSignatureArr = new MethodSignature[i2];
            int i3 = 0;
            for (int i4 = 0; i4 < memberArr.length; i4++) {
                if (!Modifier.isPrivate(memberArr[i4].getModifiers())) {
                    methodSignatureArr[i3] = new MethodSignature(memberArr[i4]);
                    i3++;
                }
            }
            if (i3 > 0) {
                Arrays.sort(methodSignatureArr, methodSignatureArr[0]);
            }
            return methodSignatureArr;
        }

        @Override // java.util.Comparator
        public int compare(Object obj, Object obj2) {
            int iCompareTo;
            if (obj == obj2) {
                return 0;
            }
            MethodSignature methodSignature = (MethodSignature) obj;
            MethodSignature methodSignature2 = (MethodSignature) obj2;
            if (isConstructor()) {
                iCompareTo = methodSignature.signature.compareTo(methodSignature2.signature);
            } else {
                iCompareTo = methodSignature.member.getName().compareTo(methodSignature2.member.getName());
                if (iCompareTo == 0) {
                    iCompareTo = methodSignature.signature.compareTo(methodSignature2.signature);
                }
            }
            return iCompareTo;
        }

        private final boolean isConstructor() {
            return this.member instanceof Constructor;
        }

        private MethodSignature(Member member) {
            this.member = member;
            if (isConstructor()) {
                this.signature = ObjectStreamClassUtil_1_3.getSignature((Constructor) member);
            } else {
                this.signature = ObjectStreamClassUtil_1_3.getSignature((Method) member);
            }
        }
    }

    private static boolean hasStaticInitializer(Class cls) {
        if (hasStaticInitializerMethod == null) {
            Class cls2 = null;
            if (0 == 0) {
                cls2 = java.io.ObjectStreamClass.class;
            }
            try {
                hasStaticInitializerMethod = cls2.getDeclaredMethod("hasStaticInitializer", Class.class);
            } catch (NoSuchMethodException e2) {
            }
            if (hasStaticInitializerMethod == null) {
                throw new InternalError("Can't find hasStaticInitializer method on " + cls2.getName());
            }
            hasStaticInitializerMethod.setAccessible(true);
        }
        try {
            return ((Boolean) hasStaticInitializerMethod.invoke(null, cls)).booleanValue();
        } catch (Exception e3) {
            throw new InternalError("Error invoking hasStaticInitializer: " + ((Object) e3));
        }
    }

    private static Method getDeclaredMethod(final Class cls, final String str, final Class[] clsArr, final int i2, final int i3) {
        return (Method) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.corba.se.impl.orbutil.ObjectStreamClassUtil_1_3.3
            /* JADX WARN: Removed duplicated region for block: B:7:0x002d  */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            /*
                Code decompiled incorrectly, please refer to instructions dump.
                To view partially-correct code enable 'Show inconsistent code' option in preferences
            */
            public java.lang.Object run2() throws java.lang.SecurityException {
                /*
                    r4 = this;
                    r0 = 0
                    r5 = r0
                    r0 = r4
                    java.lang.Class r0 = r4     // Catch: java.lang.NoSuchMethodException -> L32
                    r1 = r4
                    java.lang.String r1 = r5     // Catch: java.lang.NoSuchMethodException -> L32
                    r2 = r4
                    java.lang.Class[] r2 = r6     // Catch: java.lang.NoSuchMethodException -> L32
                    java.lang.reflect.Method r0 = r0.getDeclaredMethod(r1, r2)     // Catch: java.lang.NoSuchMethodException -> L32
                    r5 = r0
                    r0 = r5
                    int r0 = r0.getModifiers()     // Catch: java.lang.NoSuchMethodException -> L32
                    r6 = r0
                    r0 = r6
                    r1 = r4
                    int r1 = r7     // Catch: java.lang.NoSuchMethodException -> L32
                    r0 = r0 & r1
                    if (r0 != 0) goto L2d
                    r0 = r6
                    r1 = r4
                    int r1 = r8     // Catch: java.lang.NoSuchMethodException -> L32
                    r0 = r0 & r1
                    r1 = r4
                    int r1 = r8     // Catch: java.lang.NoSuchMethodException -> L32
                    if (r0 == r1) goto L2f
                L2d:
                    r0 = 0
                    r5 = r0
                L2f:
                    goto L33
                L32:
                    r6 = move-exception
                L33:
                    r0 = r5
                    return r0
                */
                throw new UnsupportedOperationException("Method not decompiled: com.sun.corba.se.impl.orbutil.ObjectStreamClassUtil_1_3.AnonymousClass3.run2():java.lang.Object");
            }
        });
    }
}
