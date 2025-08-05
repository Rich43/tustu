package jdk.internal.org.objectweb.asm;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/Type.class */
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
    public static final int METHOD = 11;
    public static final Type VOID_TYPE = new Type(0, null, 1443168256, 1);
    public static final Type BOOLEAN_TYPE = new Type(1, null, 1509950721, 1);
    public static final Type CHAR_TYPE = new Type(2, null, 1124075009, 1);
    public static final Type BYTE_TYPE = new Type(3, null, 1107297537, 1);
    public static final Type SHORT_TYPE = new Type(4, null, 1392510721, 1);
    public static final Type INT_TYPE = new Type(5, null, 1224736769, 1);
    public static final Type FLOAT_TYPE = new Type(6, null, 1174536705, 1);
    public static final Type LONG_TYPE = new Type(7, null, 1241579778, 1);
    public static final Type DOUBLE_TYPE = new Type(8, null, 1141048066, 1);
    private final int sort;
    private final char[] buf;
    private final int off;
    private final int len;

    private Type(int i2, char[] cArr, int i3, int i4) {
        this.sort = i2;
        this.buf = cArr;
        this.off = i3;
        this.len = i4;
    }

    public static Type getType(String str) {
        return getType(str.toCharArray(), 0);
    }

    public static Type getObjectType(String str) {
        char[] charArray = str.toCharArray();
        return new Type(charArray[0] == '[' ? 9 : 10, charArray, 0, charArray.length);
    }

    public static Type getMethodType(String str) {
        return getType(str.toCharArray(), 0);
    }

    public static Type getMethodType(Type type, Type... typeArr) {
        return getType(getMethodDescriptor(type, typeArr));
    }

    public static Type getType(Class<?> cls) {
        if (cls.isPrimitive()) {
            if (cls == Integer.TYPE) {
                return INT_TYPE;
            }
            if (cls == Void.TYPE) {
                return VOID_TYPE;
            }
            if (cls == Boolean.TYPE) {
                return BOOLEAN_TYPE;
            }
            if (cls == Byte.TYPE) {
                return BYTE_TYPE;
            }
            if (cls == Character.TYPE) {
                return CHAR_TYPE;
            }
            if (cls == Short.TYPE) {
                return SHORT_TYPE;
            }
            if (cls == Double.TYPE) {
                return DOUBLE_TYPE;
            }
            if (cls == Float.TYPE) {
                return FLOAT_TYPE;
            }
            return LONG_TYPE;
        }
        return getType(getDescriptor(cls));
    }

    public static Type getType(Constructor<?> constructor) {
        return getType(getConstructorDescriptor(constructor));
    }

    public static Type getType(Method method) {
        return getType(getMethodDescriptor(method));
    }

    public static Type[] getArgumentTypes(String str) {
        int i2;
        char[] charArray = str.toCharArray();
        int i3 = 1;
        int i4 = 0;
        while (true) {
            int i5 = i3;
            i3++;
            char c2 = charArray[i5];
            if (c2 == ')') {
                break;
            }
            if (c2 == 'L') {
                do {
                    i2 = i3;
                    i3++;
                } while (charArray[i2] != ';');
                i4++;
            } else if (c2 != '[') {
                i4++;
            }
        }
        Type[] typeArr = new Type[i4];
        int i6 = 1;
        int i7 = 0;
        while (charArray[i6] != ')') {
            typeArr[i7] = getType(charArray, i6);
            i6 += typeArr[i7].len + (typeArr[i7].sort == 10 ? 2 : 0);
            i7++;
        }
        return typeArr;
    }

    public static Type[] getArgumentTypes(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        Type[] typeArr = new Type[parameterTypes.length];
        for (int length = parameterTypes.length - 1; length >= 0; length--) {
            typeArr[length] = getType(parameterTypes[length]);
        }
        return typeArr;
    }

    public static Type getReturnType(String str) {
        return getType(str.toCharArray(), str.indexOf(41) + 1);
    }

    public static Type getReturnType(Method method) {
        return getType(method.getReturnType());
    }

    public static int getArgumentsAndReturnSizes(String str) {
        int i2;
        char cCharAt;
        int i3 = 1;
        int i4 = 1;
        while (true) {
            int i5 = i4;
            i4++;
            char cCharAt2 = str.charAt(i5);
            if (cCharAt2 == ')') {
                break;
            }
            if (cCharAt2 == 'L') {
                do {
                    i2 = i4;
                    i4++;
                } while (str.charAt(i2) != ';');
                i3++;
            } else if (cCharAt2 == '[') {
                while (true) {
                    cCharAt = str.charAt(i4);
                    if (cCharAt != '[') {
                        break;
                    }
                    i4++;
                }
                if (cCharAt == 'D' || cCharAt == 'J') {
                    i3--;
                }
            } else if (cCharAt2 == 'D' || cCharAt2 == 'J') {
                i3 += 2;
            } else {
                i3++;
            }
        }
        char cCharAt3 = str.charAt(i4);
        return (i3 << 2) | (cCharAt3 == 'V' ? 0 : (cCharAt3 == 'D' || cCharAt3 == 'J') ? 2 : 1);
    }

    private static Type getType(char[] cArr, int i2) {
        switch (cArr[i2]) {
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
                return new Type(11, cArr, i2, cArr.length - i2);
            case 'F':
                return FLOAT_TYPE;
            case 'I':
                return INT_TYPE;
            case 'J':
                return LONG_TYPE;
            case 'L':
                int i3 = 1;
                while (cArr[i2 + i3] != ';') {
                    i3++;
                }
                return new Type(10, cArr, i2 + 1, i3 - 1);
            case 'S':
                return SHORT_TYPE;
            case 'V':
                return VOID_TYPE;
            case 'Z':
                return BOOLEAN_TYPE;
            case '[':
                int i4 = 1;
                while (cArr[i2 + i4] == '[') {
                    i4++;
                }
                if (cArr[i2 + i4] == 'L') {
                    do {
                        i4++;
                    } while (cArr[i2 + i4] != ';');
                }
                return new Type(9, cArr, i2, i4 + 1);
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
                StringBuilder sb = new StringBuilder(getElementType().getClassName());
                for (int dimensions = getDimensions(); dimensions > 0; dimensions--) {
                    sb.append("[]");
                }
                return sb.toString();
            case 10:
                return new String(this.buf, this.off, this.len).replace('/', '.');
            default:
                return null;
        }
    }

    public String getInternalName() {
        return new String(this.buf, this.off, this.len);
    }

    public Type[] getArgumentTypes() {
        return getArgumentTypes(getDescriptor());
    }

    public Type getReturnType() {
        return getReturnType(getDescriptor());
    }

    public int getArgumentsAndReturnSizes() {
        return getArgumentsAndReturnSizes(getDescriptor());
    }

    public String getDescriptor() {
        StringBuffer stringBuffer = new StringBuffer();
        getDescriptor(stringBuffer);
        return stringBuffer.toString();
    }

    public static String getMethodDescriptor(Type type, Type... typeArr) {
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (Type type2 : typeArr) {
            type2.getDescriptor(stringBuffer);
        }
        stringBuffer.append(')');
        type.getDescriptor(stringBuffer);
        return stringBuffer.toString();
    }

    private void getDescriptor(StringBuffer stringBuffer) {
        if (this.buf == null) {
            stringBuffer.append((char) ((this.off & (-16777216)) >>> 24));
        } else {
            if (this.sort == 10) {
                stringBuffer.append('L');
                stringBuffer.append(this.buf, this.off, this.len);
                stringBuffer.append(';');
                return;
            }
            stringBuffer.append(this.buf, this.off, this.len);
        }
    }

    public static String getInternalName(Class<?> cls) {
        return cls.getName().replace('.', '/');
    }

    public static String getDescriptor(Class<?> cls) {
        StringBuffer stringBuffer = new StringBuffer();
        getDescriptor(stringBuffer, cls);
        return stringBuffer.toString();
    }

    public static String getConstructorDescriptor(Constructor<?> constructor) {
        Class<?>[] parameterTypes = constructor.getParameterTypes();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (Class<?> cls : parameterTypes) {
            getDescriptor(stringBuffer, cls);
        }
        return stringBuffer.append(")V").toString();
    }

    public static String getMethodDescriptor(Method method) {
        Class<?>[] parameterTypes = method.getParameterTypes();
        StringBuffer stringBuffer = new StringBuffer();
        stringBuffer.append('(');
        for (Class<?> cls : parameterTypes) {
            getDescriptor(stringBuffer, cls);
        }
        stringBuffer.append(')');
        getDescriptor(stringBuffer, method.getReturnType());
        return stringBuffer.toString();
    }

    private static void getDescriptor(StringBuffer stringBuffer, Class<?> cls) {
        char c2;
        Class<?> componentType = cls;
        while (true) {
            Class<?> cls2 = componentType;
            if (cls2.isPrimitive()) {
                if (cls2 == Integer.TYPE) {
                    c2 = 'I';
                } else if (cls2 == Void.TYPE) {
                    c2 = 'V';
                } else if (cls2 == Boolean.TYPE) {
                    c2 = 'Z';
                } else if (cls2 == Byte.TYPE) {
                    c2 = 'B';
                } else if (cls2 == Character.TYPE) {
                    c2 = 'C';
                } else if (cls2 == Short.TYPE) {
                    c2 = 'S';
                } else if (cls2 == Double.TYPE) {
                    c2 = 'D';
                } else if (cls2 == Float.TYPE) {
                    c2 = 'F';
                } else {
                    c2 = 'J';
                }
                stringBuffer.append(c2);
                return;
            }
            if (cls2.isArray()) {
                stringBuffer.append('[');
                componentType = cls2.getComponentType();
            } else {
                stringBuffer.append('L');
                String name = cls2.getName();
                int length = name.length();
                for (int i2 = 0; i2 < length; i2++) {
                    char cCharAt = name.charAt(i2);
                    stringBuffer.append(cCharAt == '.' ? '/' : cCharAt);
                }
                stringBuffer.append(';');
                return;
            }
        }
    }

    public int getSize() {
        if (this.buf == null) {
            return this.off & 255;
        }
        return 1;
    }

    public int getOpcode(int i2) {
        if (i2 == 46 || i2 == 79) {
            return i2 + (this.buf == null ? (this.off & NormalizerImpl.CC_MASK) >> 8 : 4);
        }
        return i2 + (this.buf == null ? (this.off & 16711680) >> 16 : 4);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        if (!(obj instanceof Type)) {
            return false;
        }
        Type type = (Type) obj;
        if (this.sort != type.sort) {
            return false;
        }
        if (this.sort >= 9) {
            if (this.len != type.len) {
                return false;
            }
            int i2 = this.off;
            int i3 = type.off;
            int i4 = i2 + this.len;
            while (i2 < i4) {
                if (this.buf[i2] == type.buf[i3]) {
                    i2++;
                    i3++;
                } else {
                    return false;
                }
            }
            return true;
        }
        return true;
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v1, types: [int] */
    /* JADX WARN: Type inference failed for: r0v11, types: [int] */
    public int hashCode() {
        char c2 = 13 * this.sort;
        if (this.sort >= 9) {
            int i2 = this.off;
            int i3 = i2 + this.len;
            while (i2 < i3) {
                c2 = 17 * (c2 + this.buf[i2]);
                i2++;
            }
        }
        return c2;
    }

    public String toString() {
        return getDescriptor();
    }
}
