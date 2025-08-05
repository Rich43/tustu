package com.sun.corba.se.impl.io;

import com.sun.corba.se.impl.util.RepositoryId;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.omg.CORBA.AttributeDescription;
import com.sun.org.omg.CORBA.Initializer;
import com.sun.org.omg.CORBA.OperationDescription;
import com.sun.org.omg.CORBA.ValueDefPackage.FullValueDescription;
import com.sun.org.omg.CORBA._IDLTypeStub;
import com.sun.org.omg.SendingContext.CodeBase;
import java.io.Serializable;
import java.lang.reflect.Modifier;
import java.rmi.Remote;
import java.util.Iterator;
import java.util.Stack;
import javax.rmi.CORBA.ValueHandler;
import org.icepdf.core.util.PdfOps;
import org.omg.CORBA.ORB;
import org.omg.CORBA.Object;
import org.omg.CORBA.TCKind;
import org.omg.CORBA.TypeCode;
import org.omg.CORBA.ValueMember;
import sun.corba.JavaCorbaAccess;
import sun.corba.SharedSecrets;

/* loaded from: rt.jar:com/sun/corba/se/impl/io/ValueUtility.class */
public class ValueUtility {
    public static final short PRIVATE_MEMBER = 0;
    public static final short PUBLIC_MEMBER = 1;
    private static final String[] primitiveConstants = {null, null, PdfOps.S_TOKEN, "I", PdfOps.S_TOKEN, "I", PdfOps.F_TOKEN, PdfOps.D_TOKEN, Constants.HASIDCALL_INDEX_SIG, "C", PdfOps.B_TOKEN, null, null, null, null, null, null, null, null, null, null, null, null, "J", "J", PdfOps.D_TOKEN, "C", null, null, null, null, null, null};

    static {
        SharedSecrets.setJavaCorbaAccess(new JavaCorbaAccess() { // from class: com.sun.corba.se.impl.io.ValueUtility.1
            @Override // sun.corba.JavaCorbaAccess
            public ValueHandlerImpl newValueHandlerImpl() {
                return ValueHandlerImpl.getInstance();
            }

            @Override // sun.corba.JavaCorbaAccess
            public Class<?> loadClass(String str) throws ClassNotFoundException {
                if (Thread.currentThread().getContextClassLoader() != null) {
                    return Thread.currentThread().getContextClassLoader().loadClass(str);
                }
                return ClassLoader.getSystemClassLoader().loadClass(str);
            }
        });
    }

    public static String getSignature(ValueMember valueMember) throws ClassNotFoundException {
        if (valueMember.type.kind().value() == 30 || valueMember.type.kind().value() == 29 || valueMember.type.kind().value() == 14) {
            return ObjectStreamClass.getSignature((Class<?>) RepositoryId.cache.getId(valueMember.id).getClassFromType());
        }
        return primitiveConstants[valueMember.type.kind().value()];
    }

    public static FullValueDescription translate(ORB orb, ObjectStreamClass objectStreamClass, ValueHandler valueHandler) {
        FullValueDescription fullValueDescription = new FullValueDescription();
        Class<?> clsForClass = objectStreamClass.forClass();
        ValueHandlerImpl valueHandlerImpl = (ValueHandlerImpl) valueHandler;
        String strCreateForAnyType = valueHandlerImpl.createForAnyType(clsForClass);
        fullValueDescription.name = valueHandlerImpl.getUnqualifiedName(strCreateForAnyType);
        if (fullValueDescription.name == null) {
            fullValueDescription.name = "";
        }
        fullValueDescription.id = valueHandlerImpl.getRMIRepositoryID(clsForClass);
        if (fullValueDescription.id == null) {
            fullValueDescription.id = "";
        }
        fullValueDescription.is_abstract = ObjectStreamClassCorbaExt.isAbstractInterface(clsForClass);
        fullValueDescription.is_custom = objectStreamClass.hasWriteObject() || objectStreamClass.isExternalizable();
        fullValueDescription.defined_in = valueHandlerImpl.getDefinedInId(strCreateForAnyType);
        if (fullValueDescription.defined_in == null) {
            fullValueDescription.defined_in = "";
        }
        fullValueDescription.version = valueHandlerImpl.getSerialVersionUID(strCreateForAnyType);
        if (fullValueDescription.version == null) {
            fullValueDescription.version = "";
        }
        fullValueDescription.operations = new OperationDescription[0];
        fullValueDescription.attributes = new AttributeDescription[0];
        fullValueDescription.members = translateMembers(orb, objectStreamClass, valueHandler, new IdentityKeyValueStack());
        fullValueDescription.initializers = new Initializer[0];
        Class<?>[] interfaces = objectStreamClass.forClass().getInterfaces();
        int i2 = 0;
        fullValueDescription.supported_interfaces = new String[interfaces.length];
        for (int i3 = 0; i3 < interfaces.length; i3++) {
            fullValueDescription.supported_interfaces[i3] = valueHandlerImpl.createForAnyType(interfaces[i3]);
            if (!Remote.class.isAssignableFrom(interfaces[i3]) || !Modifier.isPublic(interfaces[i3].getModifiers())) {
                i2++;
            }
        }
        fullValueDescription.abstract_base_values = new String[i2];
        for (int i4 = 0; i4 < interfaces.length; i4++) {
            if (!Remote.class.isAssignableFrom(interfaces[i4]) || !Modifier.isPublic(interfaces[i4].getModifiers())) {
                fullValueDescription.abstract_base_values[i4] = valueHandlerImpl.createForAnyType(interfaces[i4]);
            }
        }
        fullValueDescription.is_truncatable = false;
        Class<? super Object> superclass = objectStreamClass.forClass().getSuperclass();
        if (Serializable.class.isAssignableFrom(superclass)) {
            fullValueDescription.base_value = valueHandlerImpl.getRMIRepositoryID(superclass);
        } else {
            fullValueDescription.base_value = "";
        }
        fullValueDescription.type = orb.get_primitive_tc(TCKind.tk_value);
        return fullValueDescription;
    }

    private static ValueMember[] translateMembers(ORB orb, ObjectStreamClass objectStreamClass, ValueHandler valueHandler, IdentityKeyValueStack identityKeyValueStack) {
        ValueHandlerImpl valueHandlerImpl = (ValueHandlerImpl) valueHandler;
        ObjectStreamField[] fields = objectStreamClass.getFields();
        int length = fields.length;
        ValueMember[] valueMemberArr = new ValueMember[length];
        int i2 = 0;
        while (i2 < length) {
            String rMIRepositoryID = valueHandlerImpl.getRMIRepositoryID(fields[i2].getClazz());
            valueMemberArr[i2] = new ValueMember();
            valueMemberArr[i2].name = fields[i2].getName();
            valueMemberArr[i2].id = rMIRepositoryID;
            valueMemberArr[i2].defined_in = valueHandlerImpl.getDefinedInId(rMIRepositoryID);
            valueMemberArr[i2].version = "1.0";
            valueMemberArr[i2].type_def = new _IDLTypeStub();
            if (fields[i2].getField() != null && Modifier.isPublic(fields[i2].getField().getModifiers())) {
                valueMemberArr[i2].access = (short) 1;
            } else {
                valueMemberArr[i2].access = (short) 0;
            }
            switch (fields[i2].getTypeCode()) {
                case 'B':
                    valueMemberArr[i2].type = orb.get_primitive_tc(TCKind.tk_octet);
                    break;
                case 'C':
                    valueMemberArr[i2].type = orb.get_primitive_tc(valueHandlerImpl.getJavaCharTCKind());
                    break;
                case 'D':
                    valueMemberArr[i2].type = orb.get_primitive_tc(TCKind.tk_double);
                    break;
                case 'E':
                case 'G':
                case 'H':
                case 'K':
                case 'L':
                case 'M':
                case 'N':
                case 'O':
                case 'P':
                case 'Q':
                case 'R':
                case 'T':
                case 'U':
                case 'V':
                case 'W':
                case 'X':
                case 'Y':
                default:
                    valueMemberArr[i2].type = createTypeCodeForClassInternal(orb, fields[i2].getClazz(), valueHandlerImpl, identityKeyValueStack);
                    valueMemberArr[i2].id = valueHandlerImpl.createForAnyType(fields[i2].getType());
                    break;
                case 'F':
                    valueMemberArr[i2].type = orb.get_primitive_tc(TCKind.tk_float);
                    break;
                case 'I':
                    valueMemberArr[i2].type = orb.get_primitive_tc(TCKind.tk_long);
                    break;
                case 'J':
                    valueMemberArr[i2].type = orb.get_primitive_tc(TCKind.tk_longlong);
                    break;
                case 'S':
                    valueMemberArr[i2].type = orb.get_primitive_tc(TCKind.tk_short);
                    break;
                case 'Z':
                    valueMemberArr[i2].type = orb.get_primitive_tc(TCKind.tk_boolean);
                    break;
            }
            i2++;
        }
        return valueMemberArr;
    }

    private static boolean exists(String str, String[] strArr) {
        for (String str2 : strArr) {
            if (str.equals(str2)) {
                return true;
            }
        }
        return false;
    }

    public static boolean isAssignableFrom(String str, FullValueDescription fullValueDescription, CodeBase codeBase) {
        if (exists(str, fullValueDescription.supported_interfaces) || str.equals(fullValueDescription.id)) {
            return true;
        }
        if (fullValueDescription.base_value != null && !fullValueDescription.base_value.equals("")) {
            return isAssignableFrom(str, codeBase.meta(fullValueDescription.base_value), codeBase);
        }
        return false;
    }

    public static TypeCode createTypeCodeForClass(ORB orb, Class cls, ValueHandler valueHandler) {
        return createTypeCodeForClassInternal(orb, cls, valueHandler, new IdentityKeyValueStack());
    }

    private static TypeCode createTypeCodeForClassInternal(ORB orb, Class cls, ValueHandler valueHandler, IdentityKeyValueStack identityKeyValueStack) {
        String str = (String) identityKeyValueStack.get(cls);
        if (str != null) {
            return orb.create_recursive_tc(str);
        }
        String rMIRepositoryID = valueHandler.getRMIRepositoryID(cls);
        if (rMIRepositoryID == null) {
            rMIRepositoryID = "";
        }
        identityKeyValueStack.push(cls, rMIRepositoryID);
        TypeCode typeCodeCreateTypeCodeInternal = createTypeCodeInternal(orb, cls, valueHandler, rMIRepositoryID, identityKeyValueStack);
        identityKeyValueStack.pop();
        return typeCodeCreateTypeCodeInternal;
    }

    /* loaded from: rt.jar:com/sun/corba/se/impl/io/ValueUtility$IdentityKeyValueStack.class */
    private static class IdentityKeyValueStack {
        Stack pairs;

        private IdentityKeyValueStack() {
            this.pairs = null;
        }

        /* loaded from: rt.jar:com/sun/corba/se/impl/io/ValueUtility$IdentityKeyValueStack$KeyValuePair.class */
        private static class KeyValuePair {
            Object key;
            Object value;

            KeyValuePair(Object obj, Object obj2) {
                this.key = obj;
                this.value = obj2;
            }

            boolean equals(KeyValuePair keyValuePair) {
                return keyValuePair.key == this.key;
            }
        }

        Object get(Object obj) {
            if (this.pairs == null) {
                return null;
            }
            Iterator<E> it = this.pairs.iterator();
            while (it.hasNext()) {
                KeyValuePair keyValuePair = (KeyValuePair) it.next();
                if (keyValuePair.key == obj) {
                    return keyValuePair.value;
                }
            }
            return null;
        }

        void push(Object obj, Object obj2) {
            if (this.pairs == null) {
                this.pairs = new Stack();
            }
            this.pairs.push(new KeyValuePair(obj, obj2));
        }

        void pop() {
            this.pairs.pop();
        }
    }

    private static TypeCode createTypeCodeInternal(ORB orb, Class cls, ValueHandler valueHandler, String str, IdentityKeyValueStack identityKeyValueStack) {
        TypeCode typeCodeCreateTypeCodeForClassInternal;
        if (cls.isArray()) {
            Class<?> componentType = cls.getComponentType();
            if (componentType.isPrimitive()) {
                typeCodeCreateTypeCodeForClassInternal = getPrimitiveTypeCodeForClass(orb, componentType, valueHandler);
            } else {
                typeCodeCreateTypeCodeForClassInternal = createTypeCodeForClassInternal(orb, componentType, valueHandler, identityKeyValueStack);
            }
            return orb.create_value_box_tc(str, "Sequence", orb.create_sequence_tc(0, typeCodeCreateTypeCodeForClassInternal));
        }
        if (cls == String.class) {
            return orb.create_value_box_tc(str, "StringValue", orb.create_string_tc(0));
        }
        if (Remote.class.isAssignableFrom(cls)) {
            return orb.get_primitive_tc(TCKind.tk_objref);
        }
        if (Object.class.isAssignableFrom(cls)) {
            return orb.get_primitive_tc(TCKind.tk_objref);
        }
        ObjectStreamClass objectStreamClassLookup = ObjectStreamClass.lookup(cls);
        if (objectStreamClassLookup == null) {
            return orb.create_value_box_tc(str, "Value", orb.get_primitive_tc(TCKind.tk_value));
        }
        short s2 = objectStreamClassLookup.isCustomMarshaled() ? (short) 1 : (short) 0;
        TypeCode typeCodeCreateTypeCodeForClassInternal2 = null;
        Class superclass = cls.getSuperclass();
        if (superclass != null && Serializable.class.isAssignableFrom(superclass)) {
            typeCodeCreateTypeCodeForClassInternal2 = createTypeCodeForClassInternal(orb, superclass, valueHandler, identityKeyValueStack);
        }
        return orb.create_value_tc(str, cls.getName(), s2, typeCodeCreateTypeCodeForClassInternal2, translateMembers(orb, objectStreamClassLookup, valueHandler, identityKeyValueStack));
    }

    public static TypeCode getPrimitiveTypeCodeForClass(ORB orb, Class cls, ValueHandler valueHandler) {
        if (cls == Integer.TYPE) {
            return orb.get_primitive_tc(TCKind.tk_long);
        }
        if (cls == Byte.TYPE) {
            return orb.get_primitive_tc(TCKind.tk_octet);
        }
        if (cls == Long.TYPE) {
            return orb.get_primitive_tc(TCKind.tk_longlong);
        }
        if (cls == Float.TYPE) {
            return orb.get_primitive_tc(TCKind.tk_float);
        }
        if (cls == Double.TYPE) {
            return orb.get_primitive_tc(TCKind.tk_double);
        }
        if (cls == Short.TYPE) {
            return orb.get_primitive_tc(TCKind.tk_short);
        }
        if (cls == Character.TYPE) {
            return orb.get_primitive_tc(((ValueHandlerImpl) valueHandler).getJavaCharTCKind());
        }
        if (cls == Boolean.TYPE) {
            return orb.get_primitive_tc(TCKind.tk_boolean);
        }
        return orb.get_primitive_tc(TCKind.tk_any);
    }
}
