package bH;

import com.sun.xml.internal.fastinfoset.EncodingConstants;
import java.math.BigInteger;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.StringTokenizer;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;
import org.icepdf.ri.common.utility.annotation.GoToActionDialog;
import sun.security.pkcs11.wrapper.PKCS11Constants;
import sun.util.locale.LanguageTag;

/* renamed from: bH.c, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bH/c.class */
public class C0995c {

    /* renamed from: a, reason: collision with root package name */
    private static final int[] f7041a = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, 8388608, 16777216, 33554432, 67108864, 134217728, 268435456, 536870912, 1073741824, Integer.MIN_VALUE};

    /* renamed from: b, reason: collision with root package name */
    private static final long[] f7042b = {1, 2, 4, 8, 16, 32, 64, 128, 256, 512, 1024, 2048, 4096, 8192, 16384, 32768, 65536, 131072, 262144, 524288, 1048576, 2097152, 4194304, PKCS11Constants.CKF_SO_PIN_TO_BE_CHANGED, PKCS11Constants.CKF_EC_UNCOMPRESS, PKCS11Constants.CKF_EC_COMPRESS, PKCS11Constants.CKF_EC_CURVENAME, 134217728, 268435456, 536870912, PKCS11Constants.CKF_ARRAY_ATTRIBUTE, -2147483648L, EncodingConstants.OCTET_STRING_MAXIMUM_LENGTH, 8589934592L, 17179869184L, 34359738368L, 68719476736L, 137438953472L, 274877906944L, 549755813888L, 1099511627776L, 2199023255552L, 4398046511104L, 8796093022208L, 17592186044416L, 35184372088832L, 70368744177664L, 140737488355328L, 281474976710656L, 562949953421312L, 1125899906842624L, 2251799813685248L, 4503599627370496L, 9007199254740992L, 18014398509481984L, 36028797018963968L, 72057594037927936L, 144115188075855872L, 288230376151711744L, 576460752303423488L, 1152921504606846976L, 2305843009213693952L, 4611686018427387904L, Long.MIN_VALUE};

    public static boolean a(byte[] bArr) {
        if (bArr == null) {
            return false;
        }
        for (byte b2 : bArr) {
            if (!a((int) b2)) {
                return false;
            }
        }
        return true;
    }

    public static boolean a(ArrayList arrayList) {
        Iterator it = arrayList.iterator();
        while (it.hasNext()) {
            Byte b2 = (Byte) it.next();
            if (b2.intValue() < 9 || b2.byteValue() > 126) {
                return false;
            }
        }
        return true;
    }

    public static boolean a(int i2) {
        return (i2 >= 32 && i2 <= 126) || i2 == 9 || i2 == 10;
    }

    public static byte[] a(int[] iArr) {
        byte[] bArr = new byte[iArr.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            bArr[i2] = (byte) iArr[i2];
        }
        return bArr;
    }

    public static int[] b(byte[] bArr) {
        int[] iArr = new int[bArr.length];
        for (int i2 = 0; i2 < iArr.length; i2++) {
            iArr[i2] = a(bArr[i2]);
        }
        return iArr;
    }

    public static int a(int i2, int i3, int i4) {
        return (i2 & a(i3, i4)) >>> i3;
    }

    public static long a(long j2, int i2, int i3) {
        return (j2 & a(i2, i3)) >>> i2;
    }

    public static byte[] c(byte[] bArr) {
        int i2 = 0;
        while (true) {
            if (i2 >= bArr.length) {
                break;
            }
            if (bArr[i2] == 0) {
                byte[] bArr2 = new byte[i2];
                System.arraycopy(bArr, 0, bArr2, 0, bArr2.length);
                bArr = bArr2;
                break;
            }
            i2++;
        }
        return bArr;
    }

    public static String a(String str) {
        boolean z2 = false;
        int i2 = 0;
        while (true) {
            if (i2 >= str.length()) {
                break;
            }
            if ((str.charAt(i2) < ' ' || str.charAt(i2) > '~') && str.charAt(i2) != '\t') {
                z2 = true;
                break;
            }
            i2++;
        }
        if (!z2) {
            return str;
        }
        StringBuilder sb = new StringBuilder();
        for (int i3 = 0; i3 < str.length(); i3++) {
            if (str.charAt(i3) < ' ' || str.charAt(i3) > '~') {
                sb.append('.');
            } else {
                sb.append(str.charAt(i3));
            }
        }
        return sb.toString();
    }

    public static int[] b(String str) {
        String[] strArrSplit = str.contains(",") ? str.split(",") : str.split(" ");
        ArrayList arrayList = new ArrayList();
        for (String str2 : strArrSplit) {
            if (!str2.contains(".") && str2.length() > 1 && str2.length() < 5) {
                arrayList.add(str2);
            }
        }
        return a((String[]) arrayList.toArray(new String[arrayList.size()]));
    }

    public static int[] a(String[] strArr) {
        int[] iArr = new int[strArr.length];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            if (strArr[i2].startsWith("0x") || strArr[i2].startsWith(LanguageTag.PRIVATEUSE)) {
                strArr[i2] = W.b(strArr[i2], "0x", "");
                strArr[i2] = W.b(strArr[i2], LanguageTag.PRIVATEUSE, "");
                iArr[i2] = Integer.parseInt(strArr[i2], 16);
            } else {
                iArr[i2] = Integer.parseInt(strArr[i2]);
            }
        }
        return iArr;
    }

    public static byte[] a(Byte[] bArr) {
        byte[] bArr2 = new byte[bArr.length];
        for (int i2 = 0; i2 < bArr2.length; i2++) {
            bArr2[i2] = bArr[i2].byteValue();
        }
        return bArr2;
    }

    public static byte[] c(String str) {
        StringTokenizer stringTokenizer = new StringTokenizer(str, ",");
        byte[] bArr = new byte[stringTokenizer.countTokens()];
        int i2 = 0;
        while (stringTokenizer.hasMoreTokens()) {
            bArr[i2] = Byte.parseByte(stringTokenizer.nextToken());
            i2++;
        }
        return bArr;
    }

    public static BigInteger a(int[] iArr, int i2, int i3, boolean z2, boolean z3) {
        if (!z2) {
            int[] iArr2 = new int[i3];
            for (int i4 = 0; i4 < i3; i4++) {
                iArr2[(iArr2.length - 1) - i4] = iArr[i2 + i4];
            }
            iArr = iArr2;
            i2 = 0;
        }
        byte[] bArr = new byte[i3];
        for (int i5 = 0; i5 < i3; i5++) {
            bArr[i5] = (byte) iArr[i2 + i5];
        }
        return new BigInteger(bArr);
    }

    public static byte[] a(byte[] bArr, byte[] bArr2) {
        if (bArr.length == 0) {
            return bArr2;
        }
        if (bArr2.length == 0) {
            return bArr;
        }
        byte[] bArr3 = new byte[bArr.length + bArr2.length];
        System.arraycopy(bArr, 0, bArr3, 0, bArr.length);
        System.arraycopy(bArr2, 0, bArr3, bArr.length, bArr2.length);
        return bArr3;
    }

    public static byte[] a(byte[] bArr, int i2, int i3, int i4, boolean z2) {
        byte[] bArrA = a(i2, new byte[i4], z2);
        for (int i5 = i3; i5 < bArr.length && i5 < i3 + i4; i5++) {
            bArr[i5] = bArrA[i5 - i3];
        }
        return bArr;
    }

    public static byte[] a(byte[] bArr, byte b2) {
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[i2] = b2;
        }
        return bArr;
    }

    public static int b(int[] iArr, int i2, int i3, boolean z2, boolean z3) {
        return a(iArr, i2, i3, 0, z2, z3);
    }

    public static int a(int[] iArr, int i2, int i3, int i4, boolean z2, boolean z3) {
        int i5 = 0;
        if (!z2) {
            int[] iArr2 = new int[i3];
            for (int i6 = 0; i6 < i3; i6++) {
                iArr2[(iArr2.length - 1) - i6] = iArr[i2 + i6];
            }
            iArr = iArr2;
            i2 = 0;
        }
        for (int i7 = 0; i7 < i3; i7++) {
            i5 += iArr[i2 + i7] << (((i3 - i7) - 1) * 8);
        }
        if (z3 && (iArr[i2] | 128) == iArr[i2]) {
            int i8 = i5 + i4;
            for (int i9 = i3; i9 < 4; i9++) {
                i8 += 255 << (i9 * 8);
            }
            i5 = i8 - i4;
        }
        return i5;
    }

    public static long c(int[] iArr, int i2, int i3, boolean z2, boolean z3) {
        long j2 = 0;
        if (!z2) {
            int[] iArr2 = new int[i3];
            for (int i4 = 0; i4 < i3; i4++) {
                iArr2[(iArr2.length - 1) - i4] = iArr[i2 + i4];
            }
            iArr = iArr2;
            i2 = 0;
        }
        for (int i5 = 0; i5 < i3; i5++) {
            j2 += iArr[i2 + i5] << (((i3 - i5) - 1) * 8);
        }
        if (z3 && (iArr[i2] | 128) == iArr[i2]) {
            for (int i6 = i3; i6 < 4; i6++) {
                j2 += 255 << (i6 * 8);
            }
        }
        return j2;
    }

    public static int a(byte[] bArr, int i2, int i3, boolean z2, boolean z3) {
        int iA = 0;
        if (i3 == 0) {
            return 0;
        }
        if (!z2) {
            byte[] bArr2 = new byte[i3];
            for (int i4 = 0; i4 < i3; i4++) {
                bArr2[(bArr2.length - 1) - i4] = bArr[i2 + i4];
            }
            bArr = bArr2;
            i2 = 0;
        }
        for (int i5 = 0; i5 < i3; i5++) {
            iA += a(bArr[i2 + i5]) << (((i3 - i5) - 1) * 8);
        }
        if (z3 && (bArr[i2] | 128) == bArr[i2]) {
            for (int i6 = i3; i6 < 4; i6++) {
                iA += 255 << (i6 * 8);
            }
        }
        return iA;
    }

    public static long b(byte[] bArr, int i2, int i3, boolean z2, boolean z3) {
        long jA = 0;
        if (i3 == 0) {
            return 0L;
        }
        if (!z2) {
            byte[] bArr2 = new byte[i3];
            for (int i4 = 0; i4 < i3; i4++) {
                bArr2[(bArr2.length - 1) - i4] = bArr[i2 + i4];
            }
            bArr = bArr2;
            i2 = 0;
        }
        for (int i5 = 0; i5 < i3; i5++) {
            jA += a(bArr[i2 + i5]) << (((i3 - i5) - 1) * 8);
        }
        if (z3 && (bArr[i2] | 128) == bArr[i2]) {
            for (int i6 = i3; i6 < 4; i6++) {
                jA += 255 << (i6 * 8);
            }
        }
        return jA;
    }

    public static byte[] a(int i2, byte[] bArr, boolean z2) {
        boolean z3 = false;
        if (i2 < 0) {
            i2 = (i2 ^ (-1)) + 0;
            z3 = true;
        }
        for (int i3 = 0; i3 < bArr.length; i3++) {
            bArr[(bArr.length - i3) - 1] = (byte) (i2 & (2147483392 ^ (-1)));
            if (z3) {
                bArr[(bArr.length - i3) - 1] = (byte) (255 - bArr[(bArr.length - i3) - 1]);
            }
            i2 >>= 8;
        }
        if (!z2) {
            for (int i4 = 0; i4 < bArr.length / 2; i4++) {
                byte b2 = bArr[i4];
                bArr[i4] = bArr[(bArr.length - 1) - i4];
                bArr[(bArr.length - 1) - i4] = b2;
            }
        }
        return bArr;
    }

    public static byte[] a(long j2, byte[] bArr, boolean z2) {
        boolean z3 = false;
        if (j2 < 0) {
            j2 = (j2 ^ (-1)) + 0;
            z3 = true;
        }
        for (int i2 = 0; i2 < bArr.length; i2++) {
            bArr[(bArr.length - i2) - 1] = (byte) (j2 & (9223372036854775552L ^ (-1)));
            if (z3) {
                bArr[(bArr.length - i2) - 1] = (byte) (255 - bArr[(bArr.length - i2) - 1]);
            }
            j2 >>= 8;
        }
        if (!z2) {
            for (int i3 = 0; i3 < bArr.length / 2; i3++) {
                byte b2 = bArr[i3];
                bArr[i3] = bArr[(bArr.length - 1) - i3];
                bArr[(bArr.length - 1) - i3] = b2;
            }
        }
        return bArr;
    }

    public static int[] a(int i2, int[] iArr, boolean z2) {
        for (int i3 = 0; i3 < iArr.length; i3++) {
            if (z2) {
                iArr[(iArr.length - i3) - 1] = i2 & 255;
            } else {
                iArr[i3] = i2 & 255;
                iArr[i3] = iArr[i3];
            }
            i2 >>>= 8;
        }
        return iArr;
    }

    public static int[] a(long j2, int[] iArr, boolean z2) {
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (z2) {
                iArr[(iArr.length - i2) - 1] = (int) (j2 & 255);
            } else {
                iArr[i2] = (int) (j2 & 255);
                iArr[i2] = iArr[i2];
            }
            j2 >>>= 8;
        }
        return iArr;
    }

    public static String b(int[] iArr) {
        return a(iArr, 16);
    }

    public static String a(int[] iArr, int i2) {
        if (iArr == null) {
            return FXMLLoader.NULL_KEYWORD;
        }
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (int i3 = 0; i3 < iArr.length; i3++) {
            if (i2 > 0 && i3 > 0 && i3 % i2 == 0) {
                sb.append(GoToActionDialog.EMPTY_DESTINATION).append(sb2.toString()).append("\n");
                sb2.delete(0, sb2.length());
            }
            int i4 = iArr[i3] & 255;
            sb.append(W.a(LanguageTag.PRIVATEUSE + W.a(Integer.toHexString(i4).toUpperCase(), '0', 2) + " ", ' ', 4));
            if (i4 <= 32 || i4 >= 127) {
                sb2.append(".");
            } else {
                sb2.append((char) i4);
            }
        }
        if (sb2.length() > 0) {
            sb.append(W.a(sb2.toString(), ' ', ((sb2.length() + (4 * i2)) + 6) - (4 * sb2.length())));
        }
        return sb.toString();
    }

    public static String d(byte[] bArr) {
        return a(bArr, 16);
    }

    public static String a(byte[] bArr, int i2) {
        return a(bArr, i2, 0, bArr.length);
    }

    public static String a(byte[] bArr, int i2, int i3, int i4) {
        StringBuilder sb = new StringBuilder();
        StringBuilder sb2 = new StringBuilder();
        for (int i5 = i3; i5 < bArr.length && i5 < i3 + i4; i5++) {
            int iA = a(bArr[i5]);
            if (i2 > 0 && i5 > 0 && i5 % i2 == 0) {
                sb.append(GoToActionDialog.EMPTY_DESTINATION).append((CharSequence) sb2).append("\n");
                sb2.delete(0, sb2.length());
            }
            sb.append(W.a(LanguageTag.PRIVATEUSE + W.a(Integer.toHexString(iA).toUpperCase(), '0', 2) + " ", ' ', 4));
            if (iA <= 32 || iA >= 127) {
                sb2.append(".");
            } else {
                sb2.append((char) iA);
            }
        }
        if (sb2.length() > 0) {
            sb.append(W.a(sb2.toString(), ' ', ((sb2.length() + (4 * i2)) + 6) - (4 * sb2.length())));
        }
        return sb.toString();
    }

    /* JADX WARN: Multi-variable type inference failed */
    /* JADX WARN: Type inference failed for: r0v4, types: [int] */
    public static int a(byte b2) {
        byte b3 = b2;
        if (b3 < 0) {
            b3 += 256;
        }
        return b3;
    }

    public static String b(byte b2) {
        return "0x" + W.a(Integer.toHexString(a(b2)).toUpperCase(), '0', 2);
    }

    public static String a(int[] iArr, int[] iArr2) {
        return a(0, iArr, iArr2);
    }

    public static String a(int i2, int[] iArr, int[] iArr2) {
        String str = "";
        String str2 = "";
        String str3 = "";
        int length = iArr.length > iArr2.length ? iArr.length : iArr2.length;
        int i3 = 0;
        String str4 = "";
        String str5 = "";
        int i4 = 0;
        for (int i5 = 0; i5 < length; i5++) {
            boolean z2 = false;
            if (iArr.length <= i5 || iArr2.length <= i5 || iArr[i5] != iArr2[i5]) {
                z2 = true;
                str2 = str2 + LanguageTag.PRIVATEUSE + Integer.toHexString(i5 + i2).toUpperCase() + ", ";
                str3 = str3 + i5 + ", ";
                i3++;
            }
            if (iArr.length > i5) {
                String strA = W.a(Integer.toHexString(iArr[i5] & 255).toUpperCase(), '0', 2);
                str4 = !z2 ? str4 + W.a(" x" + strA + " ", ' ', 6) : str4 + W.a(" \"x" + strA + PdfOps.DOUBLE_QUOTE__TOKEN, ' ', 6);
            } else {
                str4 = str4 + W.a(" ", ' ', 6);
            }
            if (iArr2.length > i5) {
                String strA2 = W.a(Integer.toHexString(iArr2[i5] & 255).toUpperCase(), '0', 2);
                str5 = !z2 ? str5 + W.a(" x" + strA2 + " ", ' ', 6) : str5 + W.a(" \"x" + strA2 + PdfOps.DOUBLE_QUOTE__TOKEN, ' ', 6);
            } else {
                str5 = str5 + W.a(" ", ' ', 6);
            }
            if (i5 == length - 1 || (8 > 0 && i5 > 0 && (i5 + 1) % 8 == 0)) {
                String str6 = LanguageTag.PRIVATEUSE + W.a(Integer.toHexString(i4).toUpperCase(), '0', 4);
                if ((i5 + 1) % 8 != 0) {
                    for (int i6 = 0; ((i5 + 1) + i6) % 8 != 0; i6++) {
                        str4 = str4 + GoToActionDialog.EMPTY_DESTINATION;
                    }
                }
                str = str + str6 + "\t" + str4 + "\t   " + str5 + "\n";
                str4 = "";
                str5 = "";
                i4 += 8;
            }
        }
        return i3 > 0 ? "Differences Detected at the following addresses:\n" + str2 + "\nor in Decimal:\n" + str3 + "\nNon Matching bytes in quotes:\n                          Set 1                                                        Set 2\n" + str : "No difference detected in the following data sets:\n                          Set 1                                                        Set 2\n" + str;
    }

    public static boolean b(int[] iArr, int[] iArr2) {
        if (iArr == null && iArr2 == null) {
            return true;
        }
        if (iArr == null || iArr2 == null || iArr.length != iArr2.length) {
            return false;
        }
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (iArr[i2] >= 0 && iArr2[i2] >= 0 && iArr[i2] != iArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    public static boolean a(double[][] dArr, double[][] dArr2, int i2, double d2) {
        if (dArr == null || dArr2 == null || dArr.length != dArr2.length) {
            return false;
        }
        if (dArr.length == 0 || dArr[0].length == 0) {
            return true;
        }
        for (int i3 = 0; i3 < dArr.length; i3++) {
            for (int i4 = 0; i4 < dArr[0].length; i4++) {
                if (dArr[i3][i4] != dArr2[i3][i4]) {
                    if (Math.round(Math.abs(dArr[i3][i4] - dArr2[i3][i4]) * r0) / Math.pow(10.0d, i2) >= d2) {
                        return false;
                    }
                }
            }
        }
        return true;
    }

    public static boolean b(byte[] bArr, byte[] bArr2) {
        if (bArr == null && bArr2 == null) {
            return true;
        }
        if (bArr == null || bArr2 == null || bArr.length != bArr2.length) {
            return false;
        }
        for (int i2 = 0; i2 < bArr.length; i2++) {
            if (bArr[i2] != bArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    public static boolean c(byte[] bArr, byte[] bArr2) {
        if (bArr == null || bArr2 == null || bArr.length != bArr2.length) {
            return false;
        }
        for (int i2 = 0; i2 < bArr.length; i2++) {
            if (bArr[i2] != bArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    public static boolean c(int[] iArr, int[] iArr2) {
        if (iArr == null || iArr2 == null || iArr.length != iArr2.length) {
            return false;
        }
        for (int i2 = 0; i2 < iArr.length; i2++) {
            if (iArr[i2] != iArr2[i2]) {
                return false;
            }
        }
        return true;
    }

    private static int a(int i2, int i3) {
        int i4 = 0;
        for (int i5 = i2; i5 <= i3; i5++) {
            i4 |= f7041a[i5];
        }
        return i4;
    }

    public static byte[] b(int i2) {
        return new byte[]{(byte) (i2 >>> 24), (byte) (i2 >>> 16), (byte) (i2 >>> 8), (byte) i2};
    }

    public static boolean e(byte[] bArr) {
        for (byte b2 : bArr) {
            if (b2 != 0) {
                return false;
            }
        }
        return true;
    }

    public static boolean c(int[] iArr) {
        for (int i2 : iArr) {
            if (i2 != 0) {
                return false;
            }
        }
        return true;
    }

    public static int d(String str) {
        int i2;
        if (str.startsWith(LanguageTag.PRIVATEUSE)) {
            str = str.substring(1);
            i2 = 16;
        } else {
            i2 = 8;
        }
        return Integer.parseInt(str, i2);
    }
}
