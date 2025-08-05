package com.sun.org.apache.bcel.internal.generic;

import com.sun.org.apache.bcel.internal.classfile.ClassFormatException;
import com.sun.org.apache.bcel.internal.classfile.Utility;
import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import java.io.Serializable;
import java.lang.reflect.Method;
import java.util.ArrayList;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/generic/Type.class */
public abstract class Type implements Serializable {
    protected byte type;
    protected String signature;
    public static final BasicType VOID = new BasicType((byte) 12);
    public static final BasicType BOOLEAN = new BasicType((byte) 4);
    public static final BasicType INT = new BasicType((byte) 10);
    public static final BasicType SHORT = new BasicType((byte) 9);
    public static final BasicType BYTE = new BasicType((byte) 8);
    public static final BasicType LONG = new BasicType((byte) 11);
    public static final BasicType DOUBLE = new BasicType((byte) 7);
    public static final BasicType FLOAT = new BasicType((byte) 6);
    public static final BasicType CHAR = new BasicType((byte) 5);
    public static final ObjectType OBJECT = new ObjectType(Constants.OBJECT_CLASS);
    public static final ObjectType STRING = new ObjectType("java.lang.String");
    public static final ObjectType STRINGBUFFER = new ObjectType(Constants.STRING_BUFFER_CLASS);
    public static final ObjectType THROWABLE = new ObjectType("java.lang.Throwable");
    public static final Type[] NO_ARGS = new Type[0];
    public static final ReferenceType NULL = new ReferenceType() { // from class: com.sun.org.apache.bcel.internal.generic.Type.1
    };
    public static final Type UNKNOWN = new Type(15, "<unknown object>") { // from class: com.sun.org.apache.bcel.internal.generic.Type.2
    };
    private static int consumed_chars = 0;

    protected Type(byte t2, String s2) {
        this.type = t2;
        this.signature = s2;
    }

    public String getSignature() {
        return this.signature;
    }

    public byte getType() {
        return this.type;
    }

    public int getSize() {
        switch (this.type) {
            case 7:
            case 11:
                return 2;
            case 12:
                return 0;
            default:
                return 1;
        }
    }

    public String toString() {
        return (equals(NULL) || this.type >= 15) ? this.signature : Utility.signatureToString(this.signature, false);
    }

    public static String getMethodSignature(Type return_type, Type[] arg_types) {
        StringBuffer buf = new StringBuffer("(");
        int length = arg_types == null ? 0 : arg_types.length;
        for (int i2 = 0; i2 < length; i2++) {
            buf.append(arg_types[i2].getSignature());
        }
        buf.append(')');
        buf.append(return_type.getSignature());
        return buf.toString();
    }

    public static final Type getType(String signature) throws StringIndexOutOfBoundsException, ClassFormatException {
        byte type = Utility.typeOfSignature(signature);
        if (type <= 12) {
            consumed_chars = 1;
            return BasicType.getType(type);
        }
        if (type == 13) {
            int dim = 0;
            do {
                dim++;
            } while (signature.charAt(dim) == '[');
            Type t2 = getType(signature.substring(dim));
            consumed_chars += dim;
            return new ArrayType(t2, dim);
        }
        int index = signature.indexOf(59);
        if (index < 0) {
            throw new ClassFormatException("Invalid signature: " + signature);
        }
        consumed_chars = index + 1;
        return new ObjectType(signature.substring(1, index).replace('/', '.'));
    }

    public static Type getReturnType(String signature) {
        try {
            int index = signature.lastIndexOf(41) + 1;
            return getType(signature.substring(index));
        } catch (StringIndexOutOfBoundsException e2) {
            throw new ClassFormatException("Invalid method signature: " + signature);
        }
    }

    public static Type[] getArgumentTypes(String signature) {
        ArrayList vec = new ArrayList();
        try {
            if (signature.charAt(0) != '(') {
                throw new ClassFormatException("Invalid method signature: " + signature);
            }
            for (int index = 1; signature.charAt(index) != ')'; index += consumed_chars) {
                vec.add(getType(signature.substring(index)));
            }
            Type[] types = new Type[vec.size()];
            vec.toArray(types);
            return types;
        } catch (StringIndexOutOfBoundsException e2) {
            throw new ClassFormatException("Invalid method signature: " + signature);
        }
    }

    public static Type getType(Class cl) {
        if (cl == null) {
            throw new IllegalArgumentException("Class must not be null");
        }
        if (cl.isArray()) {
            return getType(cl.getName());
        }
        if (cl.isPrimitive()) {
            if (cl == Integer.TYPE) {
                return INT;
            }
            if (cl == Void.TYPE) {
                return VOID;
            }
            if (cl == Double.TYPE) {
                return DOUBLE;
            }
            if (cl == Float.TYPE) {
                return FLOAT;
            }
            if (cl == Boolean.TYPE) {
                return BOOLEAN;
            }
            if (cl == Byte.TYPE) {
                return BYTE;
            }
            if (cl == Short.TYPE) {
                return SHORT;
            }
            if (cl == Byte.TYPE) {
                return BYTE;
            }
            if (cl == Long.TYPE) {
                return LONG;
            }
            if (cl == Character.TYPE) {
                return CHAR;
            }
            throw new IllegalStateException("Ooops, what primitive type is " + ((Object) cl));
        }
        return new ObjectType(cl.getName());
    }

    public static String getSignature(Method meth) {
        StringBuffer sb = new StringBuffer("(");
        Class[] params = meth.getParameterTypes();
        for (Class cls : params) {
            sb.append(getType(cls).getSignature());
        }
        sb.append(")");
        sb.append(getType(meth.getReturnType()).getSignature());
        return sb.toString();
    }
}
