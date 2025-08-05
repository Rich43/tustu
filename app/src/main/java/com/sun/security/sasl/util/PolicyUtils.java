package com.sun.security.sasl.util;

import java.util.Map;
import javax.security.sasl.Sasl;

/* loaded from: rt.jar:com/sun/security/sasl/util/PolicyUtils.class */
public final class PolicyUtils {
    public static final int NOPLAINTEXT = 1;
    public static final int NOACTIVE = 2;
    public static final int NODICTIONARY = 4;
    public static final int FORWARD_SECRECY = 8;
    public static final int NOANONYMOUS = 16;
    public static final int PASS_CREDENTIALS = 512;

    private PolicyUtils() {
    }

    public static boolean checkPolicy(int i2, Map<String, ?> map) {
        if (map == null) {
            return true;
        }
        if ("true".equalsIgnoreCase((String) map.get(Sasl.POLICY_NOPLAINTEXT)) && (i2 & 1) == 0) {
            return false;
        }
        if ("true".equalsIgnoreCase((String) map.get(Sasl.POLICY_NOACTIVE)) && (i2 & 2) == 0) {
            return false;
        }
        if ("true".equalsIgnoreCase((String) map.get(Sasl.POLICY_NODICTIONARY)) && (i2 & 4) == 0) {
            return false;
        }
        if ("true".equalsIgnoreCase((String) map.get(Sasl.POLICY_NOANONYMOUS)) && (i2 & 16) == 0) {
            return false;
        }
        if ("true".equalsIgnoreCase((String) map.get(Sasl.POLICY_FORWARD_SECRECY)) && (i2 & 8) == 0) {
            return false;
        }
        if ("true".equalsIgnoreCase((String) map.get(Sasl.POLICY_PASS_CREDENTIALS)) && (i2 & 512) == 0) {
            return false;
        }
        return true;
    }

    public static String[] filterMechs(String[] strArr, int[] iArr, Map<String, ?> map) {
        if (map == null) {
            return (String[]) strArr.clone();
        }
        boolean[] zArr = new boolean[strArr.length];
        int i2 = 0;
        for (int i3 = 0; i3 < strArr.length; i3++) {
            boolean zCheckPolicy = checkPolicy(iArr[i3], map);
            zArr[i3] = zCheckPolicy;
            if (zCheckPolicy) {
                i2++;
            }
        }
        String[] strArr2 = new String[i2];
        int i4 = 0;
        for (int i5 = 0; i5 < strArr.length; i5++) {
            if (zArr[i5]) {
                int i6 = i4;
                i4++;
                strArr2[i6] = strArr[i5];
            }
        }
        return strArr2;
    }
}
