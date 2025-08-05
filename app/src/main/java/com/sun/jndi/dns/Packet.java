package com.sun.jndi.dns;

/* compiled from: DnsClient.java */
/* loaded from: rt.jar:com/sun/jndi/dns/Packet.class */
class Packet {
    byte[] buf;

    Packet(int i2) {
        this.buf = new byte[i2];
    }

    Packet(byte[] bArr, int i2) {
        this.buf = new byte[i2];
        System.arraycopy(bArr, 0, this.buf, 0, i2);
    }

    void putInt(int i2, int i3) {
        this.buf[i3 + 0] = (byte) (i2 >> 24);
        this.buf[i3 + 1] = (byte) (i2 >> 16);
        this.buf[i3 + 2] = (byte) (i2 >> 8);
        this.buf[i3 + 3] = (byte) i2;
    }

    void putShort(int i2, int i3) {
        this.buf[i3 + 0] = (byte) (i2 >> 8);
        this.buf[i3 + 1] = (byte) i2;
    }

    void putByte(int i2, int i3) {
        this.buf[i3] = (byte) i2;
    }

    void putBytes(byte[] bArr, int i2, int i3, int i4) {
        System.arraycopy(bArr, i2, this.buf, i3, i4);
    }

    int length() {
        return this.buf.length;
    }

    byte[] getData() {
        return this.buf;
    }
}
