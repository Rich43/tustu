package jdk.internal.org.objectweb.asm.commons;

import com.sun.org.apache.xalan.internal.xsltc.compiler.Constants;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.lang.reflect.Constructor;
import java.util.HashMap;
import java.util.Map;
import jdk.internal.org.objectweb.asm.Type;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:jdk/internal/org/objectweb/asm/commons/Method.class */
public class Method {
    private final String name;
    private final String desc;
    private static final Map<String, String> DESCRIPTORS = new HashMap();

    static {
        DESCRIPTORS.put("void", "V");
        DESCRIPTORS.put(SchemaSymbols.ATTVAL_BYTE, PdfOps.B_TOKEN);
        DESCRIPTORS.put("char", "C");
        DESCRIPTORS.put(SchemaSymbols.ATTVAL_DOUBLE, PdfOps.D_TOKEN);
        DESCRIPTORS.put(SchemaSymbols.ATTVAL_FLOAT, PdfOps.F_TOKEN);
        DESCRIPTORS.put("int", "I");
        DESCRIPTORS.put(SchemaSymbols.ATTVAL_LONG, "J");
        DESCRIPTORS.put(SchemaSymbols.ATTVAL_SHORT, PdfOps.S_TOKEN);
        DESCRIPTORS.put("boolean", Constants.HASIDCALL_INDEX_SIG);
    }

    public Method(String str, String str2) {
        this.name = str;
        this.desc = str2;
    }

    public Method(String str, Type type, Type[] typeArr) {
        this(str, Type.getMethodDescriptor(type, typeArr));
    }

    public static Method getMethod(java.lang.reflect.Method method) {
        return new Method(method.getName(), Type.getMethodDescriptor(method));
    }

    public static Method getMethod(Constructor<?> constructor) {
        return new Method(com.sun.org.apache.bcel.internal.Constants.CONSTRUCTOR_NAME, Type.getConstructorDescriptor(constructor));
    }

    public static Method getMethod(String str) throws IllegalArgumentException {
        return getMethod(str, false);
    }

    public static Method getMethod(String str, boolean z2) throws IllegalArgumentException {
        int iIndexOf;
        String map;
        int iIndexOf2 = str.indexOf(32);
        int iIndexOf3 = str.indexOf(40, iIndexOf2) + 1;
        int iIndexOf4 = str.indexOf(41, iIndexOf3);
        if (iIndexOf2 == -1 || iIndexOf3 == -1 || iIndexOf4 == -1) {
            throw new IllegalArgumentException();
        }
        String strSubstring = str.substring(0, iIndexOf2);
        String strTrim = str.substring(iIndexOf2 + 1, iIndexOf3 - 1).trim();
        StringBuilder sb = new StringBuilder();
        sb.append('(');
        do {
            iIndexOf = str.indexOf(44, iIndexOf3);
            if (iIndexOf == -1) {
                map = map(str.substring(iIndexOf3, iIndexOf4).trim(), z2);
            } else {
                map = map(str.substring(iIndexOf3, iIndexOf).trim(), z2);
                iIndexOf3 = iIndexOf + 1;
            }
            sb.append(map);
        } while (iIndexOf != -1);
        sb.append(')');
        sb.append(map(strSubstring, z2));
        return new Method(strTrim, sb.toString());
    }

    private static String map(String str, boolean z2) {
        if ("".equals(str)) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        int i2 = 0;
        while (true) {
            int iIndexOf = str.indexOf("[]", i2) + 1;
            i2 = iIndexOf;
            if (iIndexOf <= 0) {
                break;
            }
            sb.append('[');
        }
        String strSubstring = str.substring(0, str.length() - (sb.length() * 2));
        String str2 = DESCRIPTORS.get(strSubstring);
        if (str2 != null) {
            sb.append(str2);
        } else {
            sb.append('L');
            if (strSubstring.indexOf(46) < 0) {
                if (!z2) {
                    sb.append("java/lang/");
                }
                sb.append(strSubstring);
            } else {
                sb.append(strSubstring.replace('.', '/'));
            }
            sb.append(';');
        }
        return sb.toString();
    }

    public String getName() {
        return this.name;
    }

    public String getDescriptor() {
        return this.desc;
    }

    public Type getReturnType() {
        return Type.getReturnType(this.desc);
    }

    public Type[] getArgumentTypes() {
        return Type.getArgumentTypes(this.desc);
    }

    public String toString() {
        return this.name + this.desc;
    }

    public boolean equals(Object obj) {
        if (!(obj instanceof Method)) {
            return false;
        }
        Method method = (Method) obj;
        return this.name.equals(method.name) && this.desc.equals(method.desc);
    }

    public int hashCode() {
        return this.name.hashCode() ^ this.desc.hashCode();
    }
}
