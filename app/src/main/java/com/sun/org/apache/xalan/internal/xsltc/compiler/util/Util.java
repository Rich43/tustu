package com.sun.org.apache.xalan.internal.xsltc.compiler.util;

import com.sun.org.apache.xalan.internal.utils.SecuritySupport;
import com.sun.org.apache.xml.internal.utils.XML11Char;
import java.util.StringTokenizer;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/org/apache/xalan/internal/xsltc/compiler/util/Util.class */
public final class Util {
    private static char filesep;

    static {
        String temp = SecuritySupport.getSystemProperty("file.separator", "/");
        filesep = temp.charAt(0);
    }

    public static String noExtName(String name) {
        int index = name.lastIndexOf(46);
        return name.substring(0, index >= 0 ? index : name.length());
    }

    public static String baseName(String name) {
        int index = name.lastIndexOf(92);
        if (index < 0) {
            index = name.lastIndexOf(47);
        }
        if (index >= 0) {
            return name.substring(index + 1);
        }
        int lastColonIndex = name.lastIndexOf(58);
        if (lastColonIndex > 0) {
            return name.substring(lastColonIndex + 1);
        }
        return name;
    }

    public static String pathName(String name) {
        int index = name.lastIndexOf(47);
        if (index < 0) {
            index = name.lastIndexOf(92);
        }
        return name.substring(0, index + 1);
    }

    public static String toJavaName(String name) {
        if (name.length() > 0) {
            StringBuffer result = new StringBuffer();
            char ch = name.charAt(0);
            result.append(Character.isJavaIdentifierStart(ch) ? ch : '_');
            int n2 = name.length();
            for (int i2 = 1; i2 < n2; i2++) {
                char ch2 = name.charAt(i2);
                result.append(Character.isJavaIdentifierPart(ch2) ? ch2 : '_');
            }
            return result.toString();
        }
        return name;
    }

    public static com.sun.org.apache.bcel.internal.generic.Type getJCRefType(String signature) {
        return com.sun.org.apache.bcel.internal.generic.Type.getType(signature);
    }

    public static String internalName(String cname) {
        return cname.replace('.', filesep);
    }

    public static void println(String s2) {
        System.out.println(s2);
    }

    public static void println(char ch) {
        System.out.println(ch);
    }

    public static void TRACE1() {
        System.out.println("TRACE1");
    }

    public static void TRACE2() {
        System.out.println("TRACE2");
    }

    public static void TRACE3() {
        System.out.println("TRACE3");
    }

    public static String replace(String base, char ch, String str) {
        return base.indexOf(ch) < 0 ? base : replace(base, String.valueOf(ch), new String[]{str});
    }

    public static String replace(String base, String delim, String[] str) {
        int len = base.length();
        StringBuffer result = new StringBuffer();
        for (int i2 = 0; i2 < len; i2++) {
            char ch = base.charAt(i2);
            int k2 = delim.indexOf(ch);
            if (k2 >= 0) {
                result.append(str[k2]);
            } else {
                result.append(ch);
            }
        }
        return result.toString();
    }

    public static String escape(String input) {
        return replace(input, ".-/:", new String[]{"$dot$", "$dash$", "$slash$", "$colon$"});
    }

    public static String getLocalName(String qname) {
        int index = qname.lastIndexOf(CallSiteDescriptor.TOKEN_DELIMITER);
        return index > 0 ? qname.substring(index + 1) : qname;
    }

    public static String getPrefix(String qname) {
        int index = qname.lastIndexOf(CallSiteDescriptor.TOKEN_DELIMITER);
        return index > 0 ? qname.substring(0, index) : "";
    }

    public static boolean isLiteral(String str) {
        int length = str.length();
        for (int i2 = 0; i2 < length - 1; i2++) {
            if (str.charAt(i2) == '{' && str.charAt(i2 + 1) != '{') {
                return false;
            }
        }
        return true;
    }

    public static boolean isValidQNames(String str) {
        if (str != null && !str.equals("")) {
            StringTokenizer tokens = new StringTokenizer(str);
            while (tokens.hasMoreTokens()) {
                if (!XML11Char.isXML11ValidQName(tokens.nextToken())) {
                    return false;
                }
            }
            return true;
        }
        return true;
    }
}
