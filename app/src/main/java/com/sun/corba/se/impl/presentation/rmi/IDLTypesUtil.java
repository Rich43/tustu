package com.sun.corba.se.impl.presentation.rmi;

import com.sun.corba.se.impl.util.RepositoryId;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.Externalizable;
import java.io.IOException;
import java.io.Serializable;
import java.lang.reflect.Field;
import java.lang.reflect.Method;
import java.rmi.Remote;
import java.rmi.RemoteException;
import java.security.AccessController;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.HashSet;
import org.omg.CORBA.Object;
import org.omg.CORBA.portable.IDLEntity;

/* loaded from: rt.jar:com/sun/corba/se/impl/presentation/rmi/IDLTypesUtil.class */
public final class IDLTypesUtil {
    private static final String GET_PROPERTY_PREFIX = "get";
    private static final String SET_PROPERTY_PREFIX = "set";
    private static final String IS_PROPERTY_PREFIX = "is";
    public static final int VALID_TYPE = 0;
    public static final int INVALID_TYPE = 1;
    public static final boolean FOLLOW_RMIC = true;

    public void validateRemoteInterface(Class cls) throws IDLTypeException, SecurityException {
        if (cls == null) {
            throw new IllegalArgumentException();
        }
        if (!cls.isInterface()) {
            throw new IDLTypeException("Class " + ((Object) cls) + " must be a java interface.");
        }
        if (!Remote.class.isAssignableFrom(cls)) {
            throw new IDLTypeException("Class " + ((Object) cls) + " must extend java.rmi.Remote, either directly or indirectly.");
        }
        for (Method method : cls.getMethods()) {
            validateExceptions(method);
        }
        validateConstants(cls);
    }

    public boolean isRemoteInterface(Class cls) throws SecurityException {
        boolean z2 = true;
        try {
            validateRemoteInterface(cls);
        } catch (IDLTypeException e2) {
            z2 = false;
        }
        return z2;
    }

    public boolean isPrimitive(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException();
        }
        return cls.isPrimitive();
    }

    public boolean isValue(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException();
        }
        return (cls.isInterface() || !Serializable.class.isAssignableFrom(cls) || Remote.class.isAssignableFrom(cls)) ? false : true;
    }

    public boolean isArray(Class cls) {
        boolean z2 = false;
        if (cls == null) {
            throw new IllegalArgumentException();
        }
        if (cls.isArray()) {
            Class<?> componentType = cls.getComponentType();
            z2 = isPrimitive(componentType) || isRemoteInterface(componentType) || isEntity(componentType) || isException(componentType) || isValue(componentType) || isObjectReference(componentType);
        }
        return z2;
    }

    public boolean isException(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException();
        }
        return isCheckedException(cls) && !isRemoteException(cls) && isValue(cls);
    }

    public boolean isRemoteException(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException();
        }
        return RemoteException.class.isAssignableFrom(cls);
    }

    public boolean isCheckedException(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException();
        }
        return (!Throwable.class.isAssignableFrom(cls) || RuntimeException.class.isAssignableFrom(cls) || Error.class.isAssignableFrom(cls)) ? false : true;
    }

    public boolean isObjectReference(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException();
        }
        return cls.isInterface() && Object.class.isAssignableFrom(cls);
    }

    public boolean isEntity(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException();
        }
        return (cls.isInterface() || cls.getSuperclass() == null || !IDLEntity.class.isAssignableFrom(cls)) ? false : true;
    }

    public boolean isPropertyAccessorMethod(Method method, Class cls) {
        String name = method.getName();
        Class<?> returnType = method.getReturnType();
        Class<?>[] parameterTypes = method.getParameterTypes();
        method.getExceptionTypes();
        CharSequence charSequence = null;
        if (name.startsWith("get")) {
            if (parameterTypes.length == 0 && returnType != Void.TYPE && !readHasCorrespondingIsProperty(method, cls)) {
                charSequence = "get";
            }
        } else if (name.startsWith("set")) {
            if (returnType == Void.TYPE && parameterTypes.length == 1 && (hasCorrespondingReadProperty(method, cls, "get") || hasCorrespondingReadProperty(method, cls, "is"))) {
                charSequence = "set";
            }
        } else if (name.startsWith("is") && parameterTypes.length == 0 && returnType == Boolean.TYPE && !isHasCorrespondingReadProperty(method, cls)) {
            charSequence = "is";
        }
        if (charSequence != null && (!validPropertyExceptions(method) || name.length() <= charSequence.length())) {
            charSequence = null;
        }
        return charSequence != null;
    }

    /* JADX WARN: Removed duplicated region for block: B:8:0x003f  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    private boolean hasCorrespondingReadProperty(java.lang.reflect.Method r5, java.lang.Class r6, java.lang.String r7) {
        /*
            r4 = this;
            r0 = r5
            java.lang.String r0 = r0.getName()
            r8 = r0
            r0 = r5
            java.lang.Class[] r0 = r0.getParameterTypes()
            r9 = r0
            r0 = 0
            r10 = r0
            r0 = r8
            java.lang.String r1 = "set"
            r2 = r7
            java.lang.String r0 = r0.replaceFirst(r1, r2)     // Catch: java.lang.Exception -> L45
            r11 = r0
            r0 = r6
            r1 = r11
            r2 = 0
            java.lang.Class[] r2 = new java.lang.Class[r2]     // Catch: java.lang.Exception -> L45
            java.lang.reflect.Method r0 = r0.getMethod(r1, r2)     // Catch: java.lang.Exception -> L45
            r12 = r0
            r0 = r4
            r1 = r12
            r2 = r6
            boolean r0 = r0.isPropertyAccessorMethod(r1, r2)     // Catch: java.lang.Exception -> L45
            if (r0 == 0) goto L3f
            r0 = r12
            java.lang.Class r0 = r0.getReturnType()     // Catch: java.lang.Exception -> L45
            r1 = r9
            r2 = 0
            r1 = r1[r2]     // Catch: java.lang.Exception -> L45
            if (r0 != r1) goto L3f
            r0 = 1
            goto L40
        L3f:
            r0 = 0
        L40:
            r10 = r0
            goto L47
        L45:
            r11 = move-exception
        L47:
            r0 = r10
            return r0
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.corba.se.impl.presentation.rmi.IDLTypesUtil.hasCorrespondingReadProperty(java.lang.reflect.Method, java.lang.Class, java.lang.String):boolean");
    }

    private boolean readHasCorrespondingIsProperty(Method method, Class cls) {
        return false;
    }

    private boolean isHasCorrespondingReadProperty(Method method, Class cls) {
        boolean zIsPropertyAccessorMethod = false;
        try {
            zIsPropertyAccessorMethod = isPropertyAccessorMethod(cls.getMethod(method.getName().replaceFirst("is", "get"), new Class[0]), cls);
        } catch (Exception e2) {
        }
        return zIsPropertyAccessorMethod;
    }

    public String getAttributeNameForProperty(String str) {
        String str2 = null;
        CharSequence charSequence = null;
        if (str.startsWith("get")) {
            charSequence = "get";
        } else if (str.startsWith("set")) {
            charSequence = "set";
        } else if (str.startsWith("is")) {
            charSequence = "is";
        }
        if (charSequence != null && charSequence.length() < str.length()) {
            String strSubstring = str.substring(charSequence.length());
            str2 = (strSubstring.length() >= 2 && Character.isUpperCase(strSubstring.charAt(0)) && Character.isUpperCase(strSubstring.charAt(1))) ? strSubstring : Character.toLowerCase(strSubstring.charAt(0)) + strSubstring.substring(1);
        }
        return str2;
    }

    public IDLType getPrimitiveIDLTypeMapping(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException();
        }
        if (cls.isPrimitive()) {
            if (cls == Void.TYPE) {
                return new IDLType(cls, "void");
            }
            if (cls == Boolean.TYPE) {
                return new IDLType(cls, "boolean");
            }
            if (cls == Character.TYPE) {
                return new IDLType(cls, "wchar");
            }
            if (cls == Byte.TYPE) {
                return new IDLType(cls, "octet");
            }
            if (cls == Short.TYPE) {
                return new IDLType(cls, SchemaSymbols.ATTVAL_SHORT);
            }
            if (cls == Integer.TYPE) {
                return new IDLType(cls, SchemaSymbols.ATTVAL_LONG);
            }
            if (cls == Long.TYPE) {
                return new IDLType(cls, "long_long");
            }
            if (cls == Float.TYPE) {
                return new IDLType(cls, SchemaSymbols.ATTVAL_FLOAT);
            }
            if (cls == Double.TYPE) {
                return new IDLType(cls, SchemaSymbols.ATTVAL_DOUBLE);
            }
            return null;
        }
        return null;
    }

    public IDLType getSpecialCaseIDLTypeMapping(Class cls) {
        if (cls == null) {
            throw new IllegalArgumentException();
        }
        if (cls == Object.class) {
            return new IDLType(cls, new String[]{"java", "lang"}, "Object");
        }
        if (cls == String.class) {
            return new IDLType(cls, new String[]{"CORBA"}, RepositoryId.kWStringStubValue);
        }
        if (cls == Class.class) {
            return new IDLType(cls, new String[]{"javax", "rmi", "CORBA"}, RepositoryId.kClassDescStubValue);
        }
        if (cls == Serializable.class) {
            return new IDLType(cls, new String[]{"java", "io"}, RepositoryId.kSerializableStubValue);
        }
        if (cls == Externalizable.class) {
            return new IDLType(cls, new String[]{"java", "io"}, RepositoryId.kExternalizableStubValue);
        }
        if (cls == Remote.class) {
            return new IDLType(cls, new String[]{"java", "rmi"}, "Remote");
        }
        if (cls == Object.class) {
            return new IDLType(cls, "Object");
        }
        return null;
    }

    private void validateExceptions(Method method) throws IDLTypeException {
        Class<?>[] exceptionTypes = method.getExceptionTypes();
        boolean z2 = false;
        int i2 = 0;
        while (true) {
            if (i2 >= exceptionTypes.length) {
                break;
            }
            if (!isRemoteExceptionOrSuperClass(exceptionTypes[i2])) {
                i2++;
            } else {
                z2 = true;
                break;
            }
        }
        if (!z2) {
            throw new IDLTypeException("Method '" + ((Object) method) + "' must throw at least one exception of type java.rmi.RemoteException or one of its super-classes");
        }
        for (Class<?> cls : exceptionTypes) {
            if (isCheckedException(cls) && !isValue(cls) && !isRemoteException(cls)) {
                throw new IDLTypeException("Exception '" + ((Object) cls) + "' on method '" + ((Object) method) + "' is not a allowed RMI/IIOP exception type");
            }
        }
    }

    private boolean validPropertyExceptions(Method method) {
        for (Class<?> cls : method.getExceptionTypes()) {
            if (isCheckedException(cls) && !isRemoteException(cls)) {
                return false;
            }
        }
        return true;
    }

    private boolean isRemoteExceptionOrSuperClass(Class cls) {
        return cls == RemoteException.class || cls == IOException.class || cls == Exception.class || cls == Throwable.class;
    }

    private void validateDirectInterfaces(Class cls) throws IDLTypeException, SecurityException {
        Class<?>[] interfaces = cls.getInterfaces();
        if (interfaces.length < 2) {
            return;
        }
        HashSet hashSet = new HashSet();
        HashSet<String> hashSet2 = new HashSet();
        for (Class<?> cls2 : interfaces) {
            Method[] methods = cls2.getMethods();
            hashSet2.clear();
            for (Method method : methods) {
                hashSet2.add(method.getName());
            }
            for (String str : hashSet2) {
                if (hashSet.contains(str)) {
                    throw new IDLTypeException("Class " + ((Object) cls) + " inherits method " + str + " from multiple direct interfaces.");
                }
                hashSet.add(str);
            }
        }
    }

    private void validateConstants(final Class cls) throws IDLTypeException {
        try {
            for (Field field : (Field[]) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: com.sun.corba.se.impl.presentation.rmi.IDLTypesUtil.1
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws Exception {
                    return cls.getFields();
                }
            })) {
                Class<?> type = field.getType();
                if (type != String.class && !isPrimitive(type)) {
                    throw new IDLTypeException("Constant field '" + field.getName() + "' in class '" + field.getDeclaringClass().getName() + "' has invalid type' " + ((Object) field.getType()) + "'. Constants in RMI/IIOP interfaces can only have primitive types and java.lang.String types.");
                }
            }
        } catch (PrivilegedActionException e2) {
            IDLTypeException iDLTypeException = new IDLTypeException();
            iDLTypeException.initCause(e2);
            throw iDLTypeException;
        }
    }
}
