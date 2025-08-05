package sun.security.util;

import java.security.InvalidParameterException;
import java.util.regex.PatternSyntaxException;
import javax.crypto.spec.DHParameterSpec;
import jdk.internal.dynalink.CallSiteDescriptor;
import sun.security.action.GetPropertyAction;

/* loaded from: rt.jar:sun/security/util/SecurityProviderConstants.class */
public final class SecurityProviderConstants {
    private static final Debug debug = Debug.getInstance("jca", "ProviderConfig");
    public static final int DEF_DSA_KEY_SIZE;
    public static final int DEF_RSA_KEY_SIZE;
    public static final int DEF_RSASSA_PSS_KEY_SIZE;
    public static final int DEF_DH_KEY_SIZE;
    public static final int DEF_EC_KEY_SIZE;
    private static final String KEY_LENGTH_PROP = "jdk.security.defaultKeySize";

    static {
        String strPrivilegedGetProperty = GetPropertyAction.privilegedGetProperty(KEY_LENGTH_PROP);
        int i2 = 2048;
        int i3 = 2048;
        int i4 = 2048;
        int i5 = 2048;
        int i6 = 256;
        if (strPrivilegedGetProperty != null) {
            try {
                for (String str : strPrivilegedGetProperty.split(",")) {
                    String[] strArrSplit = str.split(CallSiteDescriptor.TOKEN_DELIMITER);
                    if (strArrSplit.length != 2) {
                        if (debug != null) {
                            debug.println("Ignoring invalid pair in jdk.security.defaultKeySize property: " + str);
                        }
                    } else {
                        String upperCase = strArrSplit[0].trim().toUpperCase();
                        try {
                            int i7 = Integer.parseInt(strArrSplit[1].trim());
                            if (upperCase.equals("DSA")) {
                                i2 = i7;
                            } else if (upperCase.equals("RSA")) {
                                i3 = i7;
                            } else if (upperCase.equals("RSASSA-PSS")) {
                                i4 = i7;
                            } else if (upperCase.equals("DH")) {
                                i5 = i7;
                            } else if (upperCase.equals("EC")) {
                                i6 = i7;
                            } else if (debug != null) {
                                debug.println("Ignoring unsupported algo in jdk.security.defaultKeySize property: " + str);
                            }
                            if (debug != null) {
                                debug.println("Overriding default " + upperCase + " keysize with value from " + KEY_LENGTH_PROP + " property: " + i7);
                            }
                        } catch (NumberFormatException e2) {
                            if (debug != null) {
                                debug.println("Ignoring invalid value in jdk.security.defaultKeySize property: " + str);
                            }
                        }
                    }
                }
            } catch (PatternSyntaxException e3) {
                if (debug != null) {
                    debug.println("Unexpected exception while parsing jdk.security.defaultKeySize property: " + ((Object) e3));
                }
            }
        }
        DEF_DSA_KEY_SIZE = i2;
        DEF_RSA_KEY_SIZE = i3;
        DEF_RSASSA_PSS_KEY_SIZE = i4;
        DEF_DH_KEY_SIZE = i5;
        DEF_EC_KEY_SIZE = i6;
    }

    private SecurityProviderConstants() {
    }

    public static final int getDefDSASubprimeSize(int i2) {
        if (i2 <= 1024) {
            return 160;
        }
        if (i2 == 2048) {
            return 224;
        }
        if (i2 == 3072) {
            return 256;
        }
        throw new InvalidParameterException("Invalid DSA Prime Size: " + i2);
    }

    public static final int getDefDHPrivateExpSize(DHParameterSpec dHParameterSpec) {
        int iBitLength = dHParameterSpec.getP().bitLength();
        if (!(dHParameterSpec instanceof SafeDHParameterSpec)) {
            return Math.max(iBitLength >= 2048 ? 1024 : iBitLength >> 1, 384);
        }
        if (iBitLength >= 15360) {
            return 512;
        }
        if (iBitLength >= 8192) {
            return 400;
        }
        if (iBitLength >= 7680) {
            return 384;
        }
        if (iBitLength >= 6144) {
            return 352;
        }
        if (iBitLength >= 4096) {
            return 304;
        }
        if (iBitLength >= 3072) {
            return 256;
        }
        if (iBitLength >= 2048) {
            return 224;
        }
        return 160;
    }
}
