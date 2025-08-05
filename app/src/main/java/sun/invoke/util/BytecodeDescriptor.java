package sun.invoke.util;

import java.lang.invoke.MethodType;
import java.lang.reflect.Array;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: rt.jar:sun/invoke/util/BytecodeDescriptor.class */
public class BytecodeDescriptor {
    private BytecodeDescriptor() {
    }

    public static List<Class<?>> parseMethod(String str, ClassLoader classLoader) {
        return parseMethod(str, 0, str.length(), classLoader);
    }

    static List<Class<?>> parseMethod(String str, int i2, int i3, ClassLoader classLoader) {
        if (classLoader == null) {
            classLoader = ClassLoader.getSystemClassLoader();
        }
        int[] iArr = {i2};
        ArrayList arrayList = new ArrayList();
        if (iArr[0] < i3 && str.charAt(iArr[0]) == '(') {
            iArr[0] = iArr[0] + 1;
            while (iArr[0] < i3 && str.charAt(iArr[0]) != ')') {
                Class<?> sig = parseSig(str, iArr, i3, classLoader);
                if (sig == null || sig == Void.TYPE) {
                    parseError(str, "bad argument type");
                }
                arrayList.add(sig);
            }
            iArr[0] = iArr[0] + 1;
        } else {
            parseError(str, "not a method type");
        }
        Class<?> sig2 = parseSig(str, iArr, i3, classLoader);
        if (sig2 == null || iArr[0] != i3) {
            parseError(str, "bad return type");
        }
        arrayList.add(sig2);
        return arrayList;
    }

    private static void parseError(String str, String str2) {
        throw new IllegalArgumentException("bad signature: " + str + ": " + str2);
    }

    private static Class<?> parseSig(String str, int[] iArr, int i2, ClassLoader classLoader) {
        if (iArr[0] == i2) {
            return null;
        }
        int i3 = iArr[0];
        iArr[0] = i3 + 1;
        char cCharAt = str.charAt(i3);
        if (cCharAt == 'L') {
            int i4 = iArr[0];
            int iIndexOf = str.indexOf(59, i4);
            if (iIndexOf < 0) {
                return null;
            }
            iArr[0] = iIndexOf + 1;
            String strReplace = str.substring(i4, iIndexOf).replace('/', '.');
            try {
                return classLoader.loadClass(strReplace);
            } catch (ClassNotFoundException e2) {
                throw new TypeNotPresentException(strReplace, e2);
            }
        }
        if (cCharAt == '[') {
            Class<?> sig = parseSig(str, iArr, i2, classLoader);
            if (sig != null) {
                sig = Array.newInstance(sig, 0).getClass();
            }
            return sig;
        }
        return Wrapper.forBasicType(cCharAt).primitiveType();
    }

    public static String unparse(Class<?> cls) {
        StringBuilder sb = new StringBuilder();
        unparseSig(cls, sb);
        return sb.toString();
    }

    public static String unparse(MethodType methodType) {
        return unparseMethod(methodType.returnType(), methodType.parameterList());
    }

    public static String unparse(Object obj) {
        if (obj instanceof Class) {
            return unparse((Class<?>) obj);
        }
        if (obj instanceof MethodType) {
            return unparse((MethodType) obj);
        }
        return (String) obj;
    }

    public static String unparseMethod(Class<?> cls, List<Class<?>> list) {
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        Iterator<Class<?>> it = list.iterator();
        while (it.hasNext()) {
            unparseSig(it.next(), sb);
        }
        sb.append(')');
        unparseSig(cls, sb);
        return sb.toString();
    }

    private static void unparseSig(Class<?> cls, StringBuilder sb) {
        char cBasicTypeChar = Wrapper.forBasicType(cls).basicTypeChar();
        if (cBasicTypeChar != 'L') {
            sb.append(cBasicTypeChar);
            return;
        }
        boolean z2 = !cls.isArray();
        if (z2) {
            sb.append('L');
        }
        sb.append(cls.getName().replace('.', '/'));
        if (z2) {
            sb.append(';');
        }
    }
}
