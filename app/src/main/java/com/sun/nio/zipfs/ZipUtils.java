package com.sun.nio.zipfs;

import java.io.IOException;
import java.io.OutputStream;
import java.util.Arrays;
import java.util.Date;
import java.util.concurrent.TimeUnit;

/* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipUtils.class */
class ZipUtils {
    private static final long WINDOWS_EPOCH_IN_MICROSECONDS = -11644473600000000L;
    private static final String regexMetaChars = ".^$+{[]|()";
    private static final String globMetaChars = "\\*?[{";
    private static char EOL = 0;

    ZipUtils() {
    }

    public static void writeShort(OutputStream outputStream, int i2) throws IOException {
        outputStream.write(i2 & 255);
        outputStream.write((i2 >>> 8) & 255);
    }

    public static void writeInt(OutputStream outputStream, long j2) throws IOException {
        outputStream.write((int) (j2 & 255));
        outputStream.write((int) ((j2 >>> 8) & 255));
        outputStream.write((int) ((j2 >>> 16) & 255));
        outputStream.write((int) ((j2 >>> 24) & 255));
    }

    public static void writeLong(OutputStream outputStream, long j2) throws IOException {
        outputStream.write((int) (j2 & 255));
        outputStream.write((int) ((j2 >>> 8) & 255));
        outputStream.write((int) ((j2 >>> 16) & 255));
        outputStream.write((int) ((j2 >>> 24) & 255));
        outputStream.write((int) ((j2 >>> 32) & 255));
        outputStream.write((int) ((j2 >>> 40) & 255));
        outputStream.write((int) ((j2 >>> 48) & 255));
        outputStream.write((int) ((j2 >>> 56) & 255));
    }

    public static void writeBytes(OutputStream outputStream, byte[] bArr) throws IOException {
        outputStream.write(bArr, 0, bArr.length);
    }

    public static void writeBytes(OutputStream outputStream, byte[] bArr, int i2, int i3) throws IOException {
        outputStream.write(bArr, i2, i3);
    }

    public static byte[] toDirectoryPath(byte[] bArr) {
        if (bArr.length != 0 && bArr[bArr.length - 1] != 47) {
            bArr = Arrays.copyOf(bArr, bArr.length + 1);
            bArr[bArr.length - 1] = 47;
        }
        return bArr;
    }

    public static long dosToJavaTime(long j2) {
        return new Date((int) (((j2 >> 25) & 127) + 80), (int) (((j2 >> 21) & 15) - 1), (int) ((j2 >> 16) & 31), (int) ((j2 >> 11) & 31), (int) ((j2 >> 5) & 63), (int) ((j2 << 1) & 62)).getTime();
    }

    public static long javaToDosTime(long j2) {
        if (new Date(j2).getYear() + 1900 < 1980) {
            return 2162688L;
        }
        return ((r0 - 1980) << 25) | ((r0.getMonth() + 1) << 21) | (r0.getDate() << 16) | (r0.getHours() << 11) | (r0.getMinutes() << 5) | (r0.getSeconds() >> 1);
    }

    public static final long winToJavaTime(long j2) {
        return TimeUnit.MILLISECONDS.convert((j2 / 10) + WINDOWS_EPOCH_IN_MICROSECONDS, TimeUnit.MICROSECONDS);
    }

    public static final long javaToWinTime(long j2) {
        return (TimeUnit.MICROSECONDS.convert(j2, TimeUnit.MILLISECONDS) - WINDOWS_EPOCH_IN_MICROSECONDS) * 10;
    }

    public static final long unixToJavaTime(long j2) {
        return TimeUnit.MILLISECONDS.convert(j2, TimeUnit.SECONDS);
    }

    public static final long javaToUnixTime(long j2) {
        return TimeUnit.SECONDS.convert(j2, TimeUnit.MILLISECONDS);
    }

    private static boolean isRegexMeta(char c2) {
        return regexMetaChars.indexOf(c2) != -1;
    }

    private static boolean isGlobMeta(char c2) {
        return globMetaChars.indexOf(c2) != -1;
    }

    private static char next(String str, int i2) {
        if (i2 < str.length()) {
            return str.charAt(i2);
        }
        return EOL;
    }

    /* JADX WARN: Removed duplicated region for block: B:108:0x01c9 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:67:0x01d7  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public static java.lang.String toRegexPattern(java.lang.String r7) {
        /*
            Method dump skipped, instructions count: 657
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.nio.zipfs.ZipUtils.toRegexPattern(java.lang.String):java.lang.String");
    }
}
