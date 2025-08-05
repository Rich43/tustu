package java.util;

import java.io.Serializable;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.security.SecureRandom;
import sun.util.locale.LanguageTag;

/* loaded from: rt.jar:java/util/UUID.class */
public final class UUID implements Serializable, Comparable<UUID> {
    private static final long serialVersionUID = -4856846361193249489L;
    private final long mostSigBits;
    private final long leastSigBits;
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !UUID.class.desiredAssertionStatus();
    }

    /* loaded from: rt.jar:java/util/UUID$Holder.class */
    private static class Holder {
        static final SecureRandom numberGenerator = new SecureRandom();

        private Holder() {
        }
    }

    private UUID(byte[] bArr) {
        long j2 = 0;
        long j3 = 0;
        if (!$assertionsDisabled && bArr.length != 16) {
            throw new AssertionError((Object) "data must be 16 bytes in length");
        }
        for (int i2 = 0; i2 < 8; i2++) {
            j2 = (j2 << 8) | (bArr[i2] & 255);
        }
        for (int i3 = 8; i3 < 16; i3++) {
            j3 = (j3 << 8) | (bArr[i3] & 255);
        }
        this.mostSigBits = j2;
        this.leastSigBits = j3;
    }

    public UUID(long j2, long j3) {
        this.mostSigBits = j2;
        this.leastSigBits = j3;
    }

    public static UUID randomUUID() {
        byte[] bArr = new byte[16];
        Holder.numberGenerator.nextBytes(bArr);
        bArr[6] = (byte) (bArr[6] & 15);
        bArr[6] = (byte) (bArr[6] | 64);
        bArr[8] = (byte) (bArr[8] & 63);
        bArr[8] = (byte) (bArr[8] | 128);
        return new UUID(bArr);
    }

    public static UUID nameUUIDFromBytes(byte[] bArr) {
        try {
            byte[] bArrDigest = MessageDigest.getInstance("MD5").digest(bArr);
            bArrDigest[6] = (byte) (bArrDigest[6] & 15);
            bArrDigest[6] = (byte) (bArrDigest[6] | 48);
            bArrDigest[8] = (byte) (bArrDigest[8] & 63);
            bArrDigest[8] = (byte) (bArrDigest[8] | 128);
            return new UUID(bArrDigest);
        } catch (NoSuchAlgorithmException e2) {
            throw new InternalError("MD5 not supported", e2);
        }
    }

    public static UUID fromString(String str) {
        String[] strArrSplit = str.split(LanguageTag.SEP);
        if (strArrSplit.length != 5) {
            throw new IllegalArgumentException("Invalid UUID string: " + str);
        }
        for (int i2 = 0; i2 < 5; i2++) {
            strArrSplit[i2] = "0x" + strArrSplit[i2];
        }
        return new UUID((((Long.decode(strArrSplit[0]).longValue() << 16) | Long.decode(strArrSplit[1]).longValue()) << 16) | Long.decode(strArrSplit[2]).longValue(), (Long.decode(strArrSplit[3]).longValue() << 48) | Long.decode(strArrSplit[4]).longValue());
    }

    public long getLeastSignificantBits() {
        return this.leastSigBits;
    }

    public long getMostSignificantBits() {
        return this.mostSigBits;
    }

    public int version() {
        return (int) ((this.mostSigBits >> 12) & 15);
    }

    public int variant() {
        return (int) ((this.leastSigBits >>> ((int) (64 - (this.leastSigBits >>> 62)))) & (this.leastSigBits >> 63));
    }

    public long timestamp() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }
        return ((this.mostSigBits & 4095) << 48) | (((this.mostSigBits >> 16) & 65535) << 32) | (this.mostSigBits >>> 32);
    }

    public int clockSequence() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }
        return (int) ((this.leastSigBits & 4611404543450677248L) >>> 48);
    }

    public long node() {
        if (version() != 1) {
            throw new UnsupportedOperationException("Not a time-based UUID");
        }
        return this.leastSigBits & 281474976710655L;
    }

    public String toString() {
        return digits(this.mostSigBits >> 32, 8) + LanguageTag.SEP + digits(this.mostSigBits >> 16, 4) + LanguageTag.SEP + digits(this.mostSigBits, 4) + LanguageTag.SEP + digits(this.leastSigBits >> 48, 4) + LanguageTag.SEP + digits(this.leastSigBits, 12);
    }

    private static String digits(long j2, int i2) {
        long j3 = 1 << (i2 * 4);
        return Long.toHexString(j3 | (j2 & (j3 - 1))).substring(1);
    }

    public int hashCode() {
        long j2 = this.mostSigBits ^ this.leastSigBits;
        return ((int) (j2 >> 32)) ^ ((int) j2);
    }

    public boolean equals(Object obj) {
        if (null == obj || obj.getClass() != UUID.class) {
            return false;
        }
        UUID uuid = (UUID) obj;
        return this.mostSigBits == uuid.mostSigBits && this.leastSigBits == uuid.leastSigBits;
    }

    @Override // java.lang.Comparable
    public int compareTo(UUID uuid) {
        if (this.mostSigBits < uuid.mostSigBits) {
            return -1;
        }
        if (this.mostSigBits > uuid.mostSigBits) {
            return 1;
        }
        if (this.leastSigBits < uuid.leastSigBits) {
            return -1;
        }
        return this.leastSigBits > uuid.leastSigBits ? 1 : 0;
    }
}
