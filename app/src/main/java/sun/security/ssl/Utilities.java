package sun.security.ssl;

import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.regex.Pattern;
import javax.net.ssl.SNIHostName;
import javax.net.ssl.SNIServerName;
import net.lingala.zip4j.crypto.PBKDF2.BinTools;
import sun.net.util.IPAddressUtil;
import sun.security.action.GetPropertyAction;

/* loaded from: jsse.jar:sun/security/ssl/Utilities.class */
final class Utilities {
    private static final String indent = "  ";
    static final char[] hexDigits = BinTools.hex.toCharArray();
    private static final Pattern lineBreakPatern = Pattern.compile("\\r\\n|\\n|\\r");

    Utilities() {
    }

    /* JADX WARN: Multi-variable type inference failed */
    static List<SNIServerName> addToSNIServerNameList(List<SNIServerName> list, String str) {
        SNIHostName sNIHostNameRawToSNIHostName = rawToSNIHostName(str);
        if (sNIHostNameRawToSNIHostName == null) {
            return list;
        }
        int size = list.size();
        ArrayList arrayList = size != 0 ? new ArrayList(list) : new ArrayList(1);
        boolean z2 = false;
        int i2 = 0;
        while (true) {
            if (i2 >= size) {
                break;
            }
            SNIServerName sNIServerName = (SNIServerName) arrayList.get(i2);
            if (sNIServerName.getType() != 0) {
                i2++;
            } else {
                arrayList.set(i2, sNIHostNameRawToSNIHostName);
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.fine("the previous server name in SNI (" + ((Object) sNIServerName) + ") was replaced with (" + ((Object) sNIHostNameRawToSNIHostName) + ")", new Object[0]);
                }
                z2 = true;
            }
        }
        if (!z2) {
            arrayList.add(sNIHostNameRawToSNIHostName);
        }
        return Collections.unmodifiableList(arrayList);
    }

    private static SNIHostName rawToSNIHostName(String str) {
        if (str != null && str.endsWith(".")) {
            str = str.substring(0, str.length() - 1);
        }
        if (str != null && str.indexOf(46) > 0 && !str.endsWith(".") && !IPAddressUtil.isIPv4LiteralAddress(str) && !IPAddressUtil.isIPv6LiteralAddress(str)) {
            try {
                return new SNIHostName(str);
            } catch (IllegalArgumentException e2) {
                if (SSLLogger.isOn && SSLLogger.isOn("ssl")) {
                    SSLLogger.fine(str + "\" is not a legal HostName for  server name indication", new Object[0]);
                    return null;
                }
                return null;
            }
        }
        return null;
    }

    static boolean getBooleanProperty(String str, boolean z2) {
        String strPrivilegedGetProperty = GetPropertyAction.privilegedGetProperty(str);
        if (strPrivilegedGetProperty == null) {
            return z2;
        }
        if (strPrivilegedGetProperty.equalsIgnoreCase("false")) {
            return false;
        }
        if (strPrivilegedGetProperty.equalsIgnoreCase("true")) {
            return true;
        }
        throw new RuntimeException("Value of " + str + " must either be 'true' or 'false'");
    }

    static String indent(String str) {
        return indent(str, "  ");
    }

    static String indent(String str, String str2) {
        StringBuilder sb = new StringBuilder();
        if (str == null) {
            sb.append("\n" + str2 + "<blank message>");
        } else {
            boolean z2 = true;
            for (String str3 : lineBreakPatern.split(str)) {
                if (z2) {
                    z2 = false;
                } else {
                    sb.append("\n");
                }
                sb.append(str2).append(str3);
            }
        }
        return sb.toString();
    }

    static String toHexString(byte b2) {
        return String.valueOf(hexDigits[(b2 >> 4) & 15]) + String.valueOf(hexDigits[b2 & 15]);
    }

    static String byte16HexString(int i2) {
        return "0x" + hexDigits[(i2 >> 12) & 15] + hexDigits[(i2 >> 8) & 15] + hexDigits[(i2 >> 4) & 15] + hexDigits[i2 & 15];
    }

    static String toHexString(byte[] bArr) {
        if (bArr == null || bArr.length == 0) {
            return "";
        }
        StringBuilder sb = new StringBuilder(bArr.length * 3);
        boolean z2 = true;
        for (byte b2 : bArr) {
            if (z2) {
                z2 = false;
            } else {
                sb.append(' ');
            }
            sb.append(hexDigits[(b2 >> 4) & 15]);
            sb.append(hexDigits[b2 & 15]);
        }
        return sb.toString();
    }

    static String toHexString(long j2) {
        StringBuilder sb = new StringBuilder(128);
        boolean z2 = true;
        do {
            if (z2) {
                z2 = false;
            } else {
                sb.append(' ');
            }
            sb.append(hexDigits[(int) (j2 & 15)]);
            long j3 = j2 >>> 4;
            sb.append(hexDigits[(int) (j3 & 15)]);
            j2 = j3 >>> 4;
        } while (j2 != 0);
        sb.reverse();
        return sb.toString();
    }

    static byte[] toByteArray(BigInteger bigInteger) {
        byte[] byteArray = bigInteger.toByteArray();
        if (byteArray.length > 1 && byteArray[0] == 0) {
            int length = byteArray.length - 1;
            byte[] bArr = new byte[length];
            System.arraycopy(byteArray, 1, bArr, 0, length);
            byteArray = bArr;
        }
        return byteArray;
    }

    private static void rangeCheck(int i2, int i3, int i4) {
        if (i3 > i4) {
            throw new IllegalArgumentException("fromIndex(" + i3 + ") > toIndex(" + i4 + ")");
        }
        if (i3 < 0) {
            throw new ArrayIndexOutOfBoundsException(i3);
        }
        if (i4 > i2) {
            throw new ArrayIndexOutOfBoundsException(i4);
        }
    }
}
