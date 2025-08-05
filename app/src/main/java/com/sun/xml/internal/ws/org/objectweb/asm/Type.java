package com.sun.xml.internal.ws.org.objectweb.asm;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/* loaded from: rt.jar:com/sun/xml/internal/ws/org/objectweb/asm/Type.class */
public class Type {
    public static final int VOID = 0;
    public static final int BOOLEAN = 1;
    public static final int CHAR = 2;
    public static final int BYTE = 3;
    public static final int SHORT = 4;
    public static final int INT = 5;
    public static final int FLOAT = 6;
    public static final int LONG = 7;
    public static final int DOUBLE = 8;
    public static final int ARRAY = 9;
    public static final int OBJECT = 10;
    public static final Type VOID_TYPE = new Type(0);
    public static final Type BOOLEAN_TYPE = new Type(1);
    public static final Type CHAR_TYPE = new Type(2);
    public static final Type BYTE_TYPE = new Type(3);
    public static final Type SHORT_TYPE = new Type(4);
    public static final Type INT_TYPE = new Type(5);
    public static final Type FLOAT_TYPE = new Type(6);
    public static final Type LONG_TYPE = new Type(7);
    public static final Type DOUBLE_TYPE = new Type(8);
    private final int sort;
    private final char[] buf;
    private final int off;
    private final int len;

    private Type(int sort) {
        this(sort, null, 0, 1);
    }

    private Type(int sort, char[] buf, int off, int len) {
        this.sort = sort;
        this.buf = buf;
        this.off = off;
        this.len = len;
    }

    public static Type getType(String typeDescriptor) {
        return getType(typeDescriptor.toCharArray(), 0);
    }

    public static Type getObjectType(String internalName) {
        char[] buf = internalName.toCharArray();
        return new Type(buf[0] == '[' ? 9 : 10, buf, 0, buf.length);
    }

    public static Type getType(Class c2) {
        if (c2.isPrimitive()) {
            if (c2 == Integer.TYPE) {
                return INT_TYPE;
            }
            if (c2 == Void.TYPE) {
                return VOID_TYPE;
            }
            if (c2 == Boolean.TYPE) {
                return BOOLEAN_TYPE;
            }
            if (c2 == Byte.TYPE) {
                return BYTE_TYPE;
            }
            if (c2 == Character.TYPE) {
                return CHAR_TYPE;
            }
            if (c2 == Short.TYPE) {
                return SHORT_TYPE;
            }
            if (c2 == Double.TYPE) {
                return DOUBLE_TYPE;
            }
            if (c2 == Float.TYPE) {
                return FLOAT_TYPE;
            }
            return LONG_TYPE;
        }
        return getType(getDescriptor(c2));
    }

    public static Type[] getArgumentTypes(String methodDescriptor) {
        int i2;
        char[] buf = methodDescriptor.toCharArray();
        int off = 1;
        int size = 0;
        while (true) {
            int i3 = off;
            off++;
            char car = buf[i3];
            if (car == ')') {
                break;
            }
            if (car == 'L') {
                do {
                    i2 = off;
                    off++;
                } while (buf[i2] != ';');
                size++;
            } else if (car != '[') {
                size++;
            }
        }
        Type[] args = new Type[size];
        int off2 = 1;
        int size2 = 0;
        while (buf[off2] != ')') {
            args[size2] = getType(buf, off2);
            off2 += args[size2].len + (args[size2].sort == 10 ? 2 : 0);
            size2++;
        }
        return args;
    }

    public static Type[] getArgumentTypes(Method method) {
        Class[] classes = method.getParameterTypes();
        Type[] types = new Type[classes.length];
        for (int i2 = classes.length - 1; i2 >= 0; i2--) {
            types[i2] = getType(classes[i2]);
        }
        return types;
    }

    public static Type getReturnType(String methodDescriptor) {
        char[] buf = methodDescriptor.toCharArray();
        return getType(buf, methodDescriptor.indexOf(41) + 1);
    }

    public static Type getReturnType(Method method) {
        return getType(method.getReturnType());
    }

    private static Type getType(char[] buf, int off) {
        switch (buf[off]) {
            case 'B':
                return BYTE_TYPE;
            case 'C':
                return CHAR_TYPE;
            case 'D':
                return DOUBLE_TYPE;
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
            case 'W':
            case 'X':
            case 'Y':
            default:
                int len = 1;
                while (buf[off + len] != ';') {
                    len++;
                }
                return new Type(10, buf, off + 1, len - 1);
            case 'F':
                return FLOAT_TYPE;
            case 'I':
                return INT_TYPE;
            case 'J':
                return LONG_TYPE;
            case 'S':
                return SHORT_TYPE;
            case 'V':
                return VOID_TYPE;
            case 'Z':
                return BOOLEAN_TYPE;
            case '[':
                int len2 = 1;
                while (buf[off + len2] == '[') {
                    len2++;
                }
                if (buf[off + len2] == 'L') {
                    do {
                        len2++;
                    } while (buf[off + len2] != ';');
                }
                return new Type(9, buf, off, len2 + 1);
        }
    }

    public int getSort() {
        return this.sort;
    }

    public int getDimensions() {
        int i2 = 1;
        while (this.buf[this.off + i2] == '[') {
            i2++;
        }
        return i2;
    }

    public Type getElementType() {
        return getType(this.buf, this.off + getDimensions());
    }

    public String getClassName() {
        switch (this.sort) {
            case 0:
                return "void";
            case 1:
                return "boolean";
            case 2:
                return "char";
            case 3:
                return SchemaSymbols.ATTVAL_BYTE;
            case 4:
                return SchemaSymbols.ATTVAL_SHORT;
            case 5:
                return "int";
            case 6:
                return SchemaSymbols.ATTVAL_FLOAT;
            case 7:
                return SchemaSymbols.ATTVAL_LONG;
            case 8:
                return SchemaSymbols.ATTVAL_DOUBLE;
            case 9:
                StringBuffer b2 = new StringBuffer(getElementType().getClassName());
                for (int i2 = getDimensions(); i2 > 0; i2--) {
                    b2.append("[]");
                }
                return b2.toString();
            default:
                return new String(this.buf, this.off, this.len).replace('/', '.');
        }
    }

    public String getInternalName() {
        return new String(this.buf, this.off, this.len);
    }

    public String getDescriptor() {
        StringBuffer buf = new StringBuffer();
        getDescriptor(buf);
        return buf.toString();
    }

    public static String getMethodDescriptor(Type returnType, Type[] argumentTypes) {
        StringBuffer buf = new StringBuffer();
        buf.append('(');
        for (Type type : argumentTypes) {
            type.getDescriptor(buf);
        }
        buf.append(')');
        returnType.getDescriptor(buf);
        return buf.toString();
    }

    private void getDescriptor(StringBuffer buf) {
        switch (this.sort) {
            case 0:
                buf.append('V');
                break;
            case 1:
                buf.append('Z');
                break;
            case 2:
                buf.append('C');
                break;
            case 3:
                buf.append('B');
                break;
            case 4:
                buf.append('S');
                break;
            case 5:
                buf.append('I');
                break;
            case 6:
                buf.append('F');
                break;
            case 7:
                buf.append('J');
                break;
            case 8:
                buf.append('D');
                break;
            case 9:
                buf.append(this.buf, this.off, this.len);
                break;
            default:
                buf.append('L');
                buf.append(this.buf, this.off, this.len);
                buf.append(';');
                break;
        }
    }

    public static String getInternalName(Class c2) {
        return c2.getName().replace('.', '/');
    }

    public static String getDescriptor(Class c2) {
        StringBuffer buf = new StringBuffer();
        getDescriptor(buf, c2);
        return buf.toString();
    }

    public static String getConstructorDescriptor(Constructor c2) {
        Class[] parameters = c2.getParameterTypes();
        StringBuffer buf = new StringBuffer();
        buf.append('(');
        for (Class cls : parameters) {
            getDescriptor(buf, cls);
        }
        return buf.append(")V").toString();
    }

    public static String getMethodDescriptor(Method m2) {
        Class[] parameters = m2.getParameterTypes();
        StringBuffer buf = new StringBuffer();
        buf.append('(');
        for (Class cls : parameters) {
            getDescriptor(buf, cls);
        }
        buf.append(')');
        getDescriptor(buf, m2.getReturnType());
        return buf.toString();
    }

    private static void getDescriptor(StringBuffer buf, Class c2) {
        char car;
        Class componentType = c2;
        while (true) {
            Class d2 = componentType;
            if (d2.isPrimitive()) {
                if (d2 == Integer.TYPE) {
                    car = 'I';
                } else if (d2 == Void.TYPE) {
                    car = 'V';
                } else if (d2 == Boolean.TYPE) {
                    car = 'Z';
                } else if (d2 == Byte.TYPE) {
                    car = 'B';
                } else if (d2 == Character.TYPE) {
                    car = 'C';
                } else if (d2 == Short.TYPE) {
                    car = 'S';
                } else if (d2 == Double.TYPE) {
                    car = 'D';
                } else if (d2 == Float.TYPE) {
                    car = 'F';
                } else {
                    car = 'J';
                }
                buf.append(car);
                return;
            }
            if (d2.isArray()) {
                buf.append('[');
                componentType = d2.getComponentType();
            } else {
                buf.append('L');
                String name = d2.getName();
                int len = name.length();
                for (int i2 = 0; i2 < len; i2++) {
                    char car2 = name.charAt(i2);
                    buf.append(car2 == '.' ? '/' : car2);
                }
                buf.append(';');
                return;
            }
        }
    }

    public int getSize() {
        return (this.sort == 7 || this.sort == 8) ? 2 : 1;
    }

    public int getOpcode(int opcode) {
        if (opcode == 46 || opcode == 79) {
            switch (this.sort) {
                case 1:
                case 3:
                    return opcode + 5;
                case 2:
                    return opcode + 6;
                case 4:
                    return opcode + 7;
                case 5:
                    return opcode;
                case 6:
                    return opcode + 2;
                case 7:
                    return opcode + 1;
                case 8:
                    return opcode + 3;
                default:
                    return opcode + 4;
            }
        }
        switch (this.sort) {
            case 0:
                return opcode + 5;
            case 1:
            case 2:
            case 3:
            case 4:
            case 5:
                return opcode;
            case 6:
                return opcode + 2;
            case 7:
                return opcode + 1;
            case 8:
                return opcode + 3;
            default:
                return opcode + 4;
        }
    }

    public boolean equals(Object o2) {
        if (this == o2) {
            return true;
        }
        if (!(o2 instanceof Type)) {
            return false;
        }
        Type t2 = (Type) o2;
        if (this.sort != t2.sort) {
            return false;
        }
        if (this.sort == 10 || this.sort == 9) {
            if (this.len != t2.len) {
                return false;
            }
            int i2 = this.off;
            int j2 = t2.off;
            int end = i2 + this.len;
            while (i2 < end) {
                if (this.buf[i2] == t2.buf[j2]) {
                    i2++;
                    j2++;
                } else {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    public int hashCode() {
        int hc = 13 * this.sort;
        if (this.sort == 10 || this.sort == 9) {
            int i2 = this.off;
            int end = i2 + this.len;
            while (i2 < end) {
                hc = 17 * (hc + this.buf[i2]);
                i2++;
            }
        }
        return hc;
    }

    public String toString() {
        return getDescriptor();
    }
}
