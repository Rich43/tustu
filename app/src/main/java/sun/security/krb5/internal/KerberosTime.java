package sun.security.krb5.internal;

import java.io.IOException;
import java.time.Instant;
import java.util.Calendar;
import java.util.Date;
import java.util.TimeZone;
import sun.security.krb5.Asn1Exception;
import sun.security.krb5.Config;
import sun.security.krb5.KrbException;
import sun.security.util.DerInputStream;
import sun.security.util.DerOutputStream;
import sun.security.util.DerValue;

/* loaded from: rt.jar:sun/security/krb5/internal/KerberosTime.class */
public class KerberosTime {
    private final long kerberosTime;
    private final int microSeconds;
    private static long initMilli = System.currentTimeMillis();
    private static long initMicro = System.nanoTime() / 1000;
    private static boolean DEBUG = Krb5.DEBUG;

    private KerberosTime(long j2, int i2) {
        this.kerberosTime = j2;
        this.microSeconds = i2;
    }

    public KerberosTime(long j2) {
        this(j2, 0);
    }

    public KerberosTime(String str) throws Asn1Exception {
        this(toKerberosTime(str), 0);
    }

    private static long toKerberosTime(String str) throws Asn1Exception, NumberFormatException {
        if (str.length() != 15) {
            throw new Asn1Exception(900);
        }
        if (str.charAt(14) != 'Z') {
            throw new Asn1Exception(900);
        }
        int i2 = Integer.parseInt(str.substring(0, 4));
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        calendar.set(i2, Integer.parseInt(str.substring(4, 6)) - 1, Integer.parseInt(str.substring(6, 8)), Integer.parseInt(str.substring(8, 10)), Integer.parseInt(str.substring(10, 12)), Integer.parseInt(str.substring(12, 14)));
        return calendar.getTimeInMillis();
    }

    public KerberosTime(Date date) {
        this(date.getTime(), 0);
    }

    public KerberosTime(Instant instant) {
        this((instant.getEpochSecond() * 1000) + (instant.getNano() / 1000000), (instant.getNano() / 1000) % 1000);
    }

    public static KerberosTime now() {
        long jCurrentTimeMillis = System.currentTimeMillis();
        long jNanoTime = System.nanoTime() / 1000;
        long j2 = jNanoTime - initMicro;
        long j3 = initMilli + (j2 / 1000);
        if (j3 - jCurrentTimeMillis > 100 || jCurrentTimeMillis - j3 > 100) {
            if (DEBUG) {
                System.out.println("System time adjusted");
            }
            initMilli = jCurrentTimeMillis;
            initMicro = jNanoTime;
            return new KerberosTime(jCurrentTimeMillis, 0);
        }
        return new KerberosTime(j3, (int) (j2 % 1000));
    }

    public String toGeneralizedTimeString() {
        Calendar calendar = Calendar.getInstance(TimeZone.getTimeZone("UTC"));
        calendar.clear();
        calendar.setTimeInMillis(this.kerberosTime);
        return String.format("%04d%02d%02d%02d%02d%02dZ", Integer.valueOf(calendar.get(1)), Integer.valueOf(calendar.get(2) + 1), Integer.valueOf(calendar.get(5)), Integer.valueOf(calendar.get(11)), Integer.valueOf(calendar.get(12)), Integer.valueOf(calendar.get(13)));
    }

    public byte[] asn1Encode() throws Asn1Exception, IOException {
        DerOutputStream derOutputStream = new DerOutputStream();
        derOutputStream.putGeneralizedTime(toDate());
        return derOutputStream.toByteArray();
    }

    public long getTime() {
        return this.kerberosTime;
    }

    public Date toDate() {
        return new Date(this.kerberosTime);
    }

    public int getMicroSeconds() {
        return new Long((this.kerberosTime % 1000) * 1000).intValue() + this.microSeconds;
    }

    public KerberosTime withMicroSeconds(int i2) {
        return new KerberosTime((this.kerberosTime - (this.kerberosTime % 1000)) + (i2 / 1000), i2 % 1000);
    }

    private boolean inClockSkew(int i2) {
        return Math.abs(this.kerberosTime - System.currentTimeMillis()) <= ((long) i2) * 1000;
    }

    public boolean inClockSkew() {
        return inClockSkew(getDefaultSkew());
    }

    public boolean greaterThanWRTClockSkew(KerberosTime kerberosTime, int i2) {
        if (this.kerberosTime - kerberosTime.kerberosTime > i2 * 1000) {
            return true;
        }
        return false;
    }

    public boolean greaterThanWRTClockSkew(KerberosTime kerberosTime) {
        return greaterThanWRTClockSkew(kerberosTime, getDefaultSkew());
    }

    public boolean greaterThan(KerberosTime kerberosTime) {
        return this.kerberosTime > kerberosTime.kerberosTime || (this.kerberosTime == kerberosTime.kerberosTime && this.microSeconds > kerberosTime.microSeconds);
    }

    public boolean equals(Object obj) {
        if (this == obj) {
            return true;
        }
        return (obj instanceof KerberosTime) && this.kerberosTime == ((KerberosTime) obj).kerberosTime && this.microSeconds == ((KerberosTime) obj).microSeconds;
    }

    public int hashCode() {
        return ((629 + ((int) (this.kerberosTime ^ (this.kerberosTime >>> 32)))) * 17) + this.microSeconds;
    }

    public boolean isZero() {
        return this.kerberosTime == 0 && this.microSeconds == 0;
    }

    public int getSeconds() {
        return new Long(this.kerberosTime / 1000).intValue();
    }

    public static KerberosTime parse(DerInputStream derInputStream, byte b2, boolean z2) throws Asn1Exception, IOException {
        if (z2 && (((byte) derInputStream.peekByte()) & 31) != b2) {
            return null;
        }
        DerValue derValue = derInputStream.getDerValue();
        if (b2 != (derValue.getTag() & 31)) {
            throw new Asn1Exception(Krb5.ASN1_BAD_ID);
        }
        return new KerberosTime(derValue.getData().getDerValue().getGeneralizedTime().getTime(), 0);
    }

    public static int getDefaultSkew() {
        int i2 = 300;
        try {
            int intValue = Config.getInstance().getIntValue("libdefaults", "clockskew");
            i2 = intValue;
            if (intValue == Integer.MIN_VALUE) {
                i2 = 300;
            }
        } catch (KrbException e2) {
            if (DEBUG) {
                System.out.println("Exception in getting clockskew from Configuration using default value " + e2.getMessage());
            }
        }
        return i2;
    }

    public String toString() {
        return toGeneralizedTimeString();
    }
}
