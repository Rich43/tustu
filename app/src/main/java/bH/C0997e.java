package bH;

import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;

/* renamed from: bH.e, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bH/e.class */
public class C0997e {
    public static String a(String str, String str2, String str3, String str4) {
        if (str == null || str2 == null || str3 == null || str4 == null || str.length() == 0 || str2.length() == 0 || str3.length() == 0 || str4.length() == 0) {
            return null;
        }
        byte[] bArr = new byte[str.length() + str2.length() + str3.length() + str2.length() + str4.length()];
        System.arraycopy(str2.getBytes(), 0, bArr, 0, str2.length());
        System.arraycopy(str3.getBytes(), 0, bArr, str2.length(), str3.length());
        System.arraycopy(str.getBytes(), 0, bArr, str2.length() + str3.length(), str.length());
        System.arraycopy(str2.getBytes(), 0, bArr, str2.length() + str3.length() + str.length(), str2.length());
        System.arraycopy(str4.getBytes(), 0, bArr, str2.length() + str3.length() + str.length() + str2.length(), str4.length());
        for (int i2 = 0; i2 < bArr.length / 2; i2++) {
            byte b2 = bArr[i2];
            byte b3 = bArr[(bArr.length - 1) - i2];
            bArr[(bArr.length - 1) - i2] = b2;
            bArr[i2] = b3;
        }
        for (int i3 = 0; i3 < bArr.length; i3++) {
            byte b4 = bArr[i3];
            int i4 = (b4 + ((b4 - 32) % 7)) - (i3 % 4);
            if (i4 > 122) {
                i4 -= 9;
            }
            bArr[i3] = (byte) i4;
        }
        for (int i5 = 0; i5 < bArr.length; i5++) {
            if (bArr[i5] == 96 || bArr[i5] == 92 || bArr[i5] == 91 || bArr[i5] == 93 || bArr[i5] == 59 || bArr[i5] == 46) {
                bArr[i5] = (byte) (bArr[i5] + 6 + (i5 % 10));
            }
        }
        return new String(bArr);
    }

    public static String a(String str, String str2, String str3, String str4, String str5) {
        if (str == null || str2 == null || str3 == null || str5 == null || str4 == null) {
            return null;
        }
        try {
            if (str.length() == 0 || str2.length() == 0 || str3.length() == 0 || str5.length() == 0) {
                return null;
            }
            byte[] bytes = a(str, str2, str3, str5).getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes2 = (new String(bytes) + str4).getBytes();
            messageDigest.update(bytes2);
            messageDigest.update(bytes2);
            messageDigest.digest();
            messageDigest.update(bytes2);
            byte[] bArrDigest = messageDigest.digest();
            bI.e eVar = new bI.e();
            eVar.a(bArrDigest);
            byte[] bArrA = C0995c.a(eVar.a(), new byte[4], true);
            byte[] bArr = new byte[bArrDigest.length + 4];
            System.arraycopy(bArrA, 0, bArr, 0, bArrA.length);
            System.arraycopy(bArrDigest, 0, bArr, 4, bArrDigest.length);
            String str6 = "";
            for (byte b2 : bArr) {
                str6 = str6 + "123456789ABCDEFGHIJKLMNPQRSTUVWXYZ".charAt(Math.abs((int) b2) % "123456789ABCDEFGHIJKLMNPQRSTUVWXYZ".length());
            }
            return str6;
        } catch (NoSuchAlgorithmException e2) {
            C.a("Unable to perform check with this machine");
            return null;
        }
    }

    public static String a(String str, String str2, String str3, String str4, String str5, String str6, String str7) {
        if (str == null || str2 == null || str3 == null || str5 == null || str4 == null) {
            return null;
        }
        try {
            if (str.length() == 0 || str2.length() == 0 || str3.length() == 0 || str5.length() == 0 || str6.length() == 0 || str7.length() == 0) {
                return null;
            }
            byte[] bytes = (a(str.toLowerCase(), str2.toLowerCase(), str3, str5.toLowerCase()) + str6 + str7).getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes2 = (new String(bytes) + str4).getBytes();
            messageDigest.update(bytes2);
            messageDigest.update(bytes2);
            messageDigest.update((new String(bytes2) + str6).getBytes());
            byte[] bArrDigest = messageDigest.digest();
            bI.e eVar = new bI.e();
            eVar.a(bArrDigest);
            byte[] bArrA = C0995c.a(eVar.a(), new byte[4], true);
            byte[] bArr = new byte[bArrDigest.length + 4];
            System.arraycopy(bArrA, 0, bArr, 0, bArrA.length);
            System.arraycopy(bArrDigest, 0, bArr, 4, bArrDigest.length);
            String str8 = "";
            for (byte b2 : bArr) {
                str8 = str8 + "23456789ABCDEFGHJKLMNPQRSTUVWXYZ".charAt(Math.abs((int) b2) % "23456789ABCDEFGHJKLMNPQRSTUVWXYZ".length());
            }
            return str8;
        } catch (NoSuchAlgorithmException e2) {
            C.a("Unable to perform check with this machine");
            return null;
        }
    }

    public static String a(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
        if (str == null || str2 == null || str3 == null || str5 == null || str4 == null || str8 == null) {
            return null;
        }
        try {
            if (str.length() == 0 || str2.length() == 0 || str3.length() == 0 || str5.length() == 0 || str6.length() == 0 || str7.length() == 0 || str8.length() == 0) {
                return null;
            }
            byte[] bytes = (a(str.toLowerCase(), str2.toLowerCase(), str3, str5.toLowerCase()) + str6 + str7 + str8).getBytes();
            MessageDigest messageDigest = MessageDigest.getInstance("MD5");
            byte[] bytes2 = (new String(bytes) + str4).getBytes();
            messageDigest.update(bytes2);
            messageDigest.update(bytes2);
            messageDigest.update((new String(bytes2) + str6).getBytes());
            byte[] bArrDigest = messageDigest.digest();
            bI.e eVar = new bI.e();
            eVar.a(bArrDigest);
            byte[] bArrA = C0995c.a(eVar.a(), new byte[4], true);
            byte[] bArr = new byte[bArrDigest.length + 4];
            System.arraycopy(bArrA, 0, bArr, 0, bArrA.length);
            System.arraycopy(bArrDigest, 0, bArr, 4, bArrDigest.length);
            String str9 = "";
            for (byte b2 : bArr) {
                str9 = str9 + "23456789ABCDEFGHJKLMNPQRSTUVWXYZ".charAt(Math.abs((int) b2) % "23456789ABCDEFGHJKLMNPQRSTUVWXYZ".length());
            }
            return str9;
        } catch (NoSuchAlgorithmException e2) {
            C.a("Unable to perform check with this machine");
            return null;
        }
    }
}
