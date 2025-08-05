package sun.security.krb5.internal;

import sun.security.krb5.Confounder;

/* loaded from: rt.jar:sun/security/krb5/internal/LocalSeqNumber.class */
public class LocalSeqNumber implements SeqNumber {
    private int lastSeqNumber;

    public LocalSeqNumber() {
        randInit();
    }

    public LocalSeqNumber(int i2) {
        init(i2);
    }

    public LocalSeqNumber(Integer num) {
        init(num.intValue());
    }

    @Override // sun.security.krb5.internal.SeqNumber
    public synchronized void randInit() {
        byte[] bArrBytes = Confounder.bytes(4);
        bArrBytes[0] = (byte) (bArrBytes[0] & 63);
        int i2 = (bArrBytes[3] & 255) | ((bArrBytes[2] & 255) << 8) | ((bArrBytes[1] & 255) << 16) | ((bArrBytes[0] & 255) << 24);
        if (i2 == 0) {
            i2 = 1;
        }
        this.lastSeqNumber = i2;
    }

    @Override // sun.security.krb5.internal.SeqNumber
    public synchronized void init(int i2) {
        this.lastSeqNumber = i2;
    }

    @Override // sun.security.krb5.internal.SeqNumber
    public synchronized int current() {
        return this.lastSeqNumber;
    }

    @Override // sun.security.krb5.internal.SeqNumber
    public synchronized int next() {
        return this.lastSeqNumber + 1;
    }

    @Override // sun.security.krb5.internal.SeqNumber
    public synchronized int step() {
        int i2 = this.lastSeqNumber + 1;
        this.lastSeqNumber = i2;
        return i2;
    }
}
