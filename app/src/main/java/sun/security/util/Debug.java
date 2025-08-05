package sun.security.util;

import java.math.BigInteger;
import java.security.AccessController;
import java.util.Locale;
import java.util.regex.Matcher;
import java.util.regex.Pattern;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.action.GetPropertyAction;
import sun.security.pkcs11.wrapper.Constants;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:sun/security/util/Debug.class */
public class Debug {
    private String prefix;
    private static String args;
    private static final char[] hexDigits;

    static {
        args = (String) AccessController.doPrivileged(new GetPropertyAction("java.security.debug"));
        String str = (String) AccessController.doPrivileged(new GetPropertyAction("java.security.auth.debug"));
        if (args == null) {
            args = str;
        } else if (str != null) {
            args += "," + str;
        }
        if (args != null) {
            args = marshal(args);
            if (args.equals("help")) {
                Help();
            }
        }
        hexDigits = "0123456789abcdef".toCharArray();
    }

    public static void Help() {
        System.err.println();
        System.err.println("all           turn on all debugging");
        System.err.println("access        print all checkPermission results");
        System.err.println("certpath      PKIX CertPathBuilder and");
        System.err.println("              CertPathValidator debugging");
        System.err.println("combiner      SubjectDomainCombiner debugging");
        System.err.println("gssloginconfig");
        System.err.println("              GSS LoginConfigImpl debugging");
        System.err.println("configfile    JAAS ConfigFile loading");
        System.err.println("configparser  JAAS ConfigFile parsing");
        System.err.println("jar           jar verification");
        System.err.println("logincontext  login context results");
        System.err.println("jca           JCA engine class debugging");
        System.err.println("policy        loading and granting");
        System.err.println("provider      security provider debugging");
        System.err.println("pkcs11        PKCS11 session manager debugging");
        System.err.println("pkcs11keystore");
        System.err.println("              PKCS11 KeyStore debugging");
        System.err.println("sunpkcs11     SunPKCS11 provider debugging");
        System.err.println("scl           permissions SecureClassLoader assigns");
        System.err.println("ts            timestamping");
        System.err.println();
        System.err.println("The following can be used with access:");
        System.err.println();
        System.err.println("stack         include stack trace");
        System.err.println("domain        dump all domains in context");
        System.err.println("failure       before throwing exception, dump stack");
        System.err.println("              and domain that didn't have permission");
        System.err.println();
        System.err.println("The following can be used with stack and domain:");
        System.err.println();
        System.err.println("permission=<classname>");
        System.err.println("              only dump output if specified permission");
        System.err.println("              is being checked");
        System.err.println("codebase=<URL>");
        System.err.println("              only dump output if specified codebase");
        System.err.println("              is being checked");
        System.err.println();
        System.err.println("The following can be used with provider:");
        System.err.println();
        System.err.println("engine=<engines>");
        System.err.println("              only dump output for the specified list");
        System.err.println("              of JCA engines. Supported values:");
        System.err.println("              Cipher, KeyAgreement, KeyGenerator,");
        System.err.println("              KeyPairGenerator, KeyStore, Mac,");
        System.err.println("              MessageDigest, SecureRandom, Signature.");
        System.err.println();
        System.err.println("Note: Separate multiple options with a comma");
        System.exit(0);
    }

    public static Debug getInstance(String str) {
        return getInstance(str, str);
    }

    public static Debug getInstance(String str, String str2) {
        if (isOn(str)) {
            Debug debug = new Debug();
            debug.prefix = str2;
            return debug;
        }
        return null;
    }

    public static boolean isOn(String str) {
        if (args == null) {
            return false;
        }
        return (args.indexOf("all") == -1 && args.indexOf(str) == -1) ? false : true;
    }

    public void println(String str) {
        System.err.println(this.prefix + ": " + str);
    }

    public void println() {
        System.err.println(this.prefix + CallSiteDescriptor.TOKEN_DELIMITER);
    }

    public static void println(String str, String str2) {
        System.err.println(str + ": " + str2);
    }

    public static String toHexString(BigInteger bigInteger) {
        String string = bigInteger.toString(16);
        StringBuffer stringBuffer = new StringBuffer(string.length() * 2);
        if (string.startsWith(LanguageTag.SEP)) {
            stringBuffer.append("   -");
            string = string.substring(1);
        } else {
            stringBuffer.append("    ");
        }
        if (string.length() % 2 != 0) {
            string = "0" + string;
        }
        int i2 = 0;
        while (i2 < string.length()) {
            stringBuffer.append(string.substring(i2, i2 + 2));
            i2 += 2;
            if (i2 != string.length()) {
                if (i2 % 64 == 0) {
                    stringBuffer.append("\n    ");
                } else if (i2 % 8 == 0) {
                    stringBuffer.append(" ");
                }
            }
        }
        return stringBuffer.toString();
    }

    private static String marshal(String str) {
        if (str != null) {
            StringBuffer stringBuffer = new StringBuffer();
            Matcher matcher = Pattern.compile("[Pp][Ee][Rr][Mm][Ii][Ss][Ss][Ii][Oo][Nn]=[a-zA-Z_$][a-zA-Z0-9_$]*([.][a-zA-Z_$][a-zA-Z0-9_$]*)*").matcher(new StringBuffer(str));
            StringBuffer stringBuffer2 = new StringBuffer();
            while (matcher.find()) {
                stringBuffer.append(matcher.group().replaceFirst("[Pp][Ee][Rr][Mm][Ii][Ss][Ss][Ii][Oo][Nn]=", "permission="));
                stringBuffer.append(Constants.INDENT);
                matcher.appendReplacement(stringBuffer2, "");
            }
            matcher.appendTail(stringBuffer2);
            Matcher matcher2 = Pattern.compile("[Cc][Oo][Dd][Ee][Bb][Aa][Ss][Ee]=[^, ;]*").matcher(stringBuffer2);
            StringBuffer stringBuffer3 = new StringBuffer();
            while (matcher2.find()) {
                stringBuffer.append(matcher2.group().replaceFirst("[Cc][Oo][Dd][Ee][Bb][Aa][Ss][Ee]=", "codebase="));
                stringBuffer.append(Constants.INDENT);
                matcher2.appendReplacement(stringBuffer3, "");
            }
            matcher2.appendTail(stringBuffer3);
            stringBuffer.append(stringBuffer3.toString().toLowerCase(Locale.ENGLISH));
            return stringBuffer.toString();
        }
        return null;
    }

    public static String toString(byte[] bArr) {
        if (bArr == null) {
            return "(null)";
        }
        StringBuilder sb = new StringBuilder(bArr.length * 3);
        for (int i2 = 0; i2 < bArr.length; i2++) {
            int i3 = bArr[i2] & 255;
            if (i2 != 0) {
                sb.append(':');
            }
            sb.append(hexDigits[i3 >>> 4]);
            sb.append(hexDigits[i3 & 15]);
        }
        return sb.toString();
    }
}
