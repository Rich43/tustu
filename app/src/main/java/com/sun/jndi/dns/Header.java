package com.sun.jndi.dns;

import javax.naming.CommunicationException;
import javax.naming.NamingException;

/* loaded from: rt.jar:com/sun/jndi/dns/Header.class */
class Header {
    static final int HEADER_SIZE = 12;
    static final short QR_BIT = Short.MIN_VALUE;
    static final short OPCODE_MASK = 30720;
    static final int OPCODE_SHIFT = 11;
    static final short AA_BIT = 1024;
    static final short TC_BIT = 512;
    static final short RD_BIT = 256;
    static final short RA_BIT = 128;
    static final short RCODE_MASK = 15;
    int xid;
    boolean query;
    int opcode;
    boolean authoritative;
    boolean truncated;
    boolean recursionDesired;
    boolean recursionAvail;
    int rcode;
    int numQuestions;
    int numAnswers;
    int numAuthorities;
    int numAdditionals;

    Header(byte[] bArr, int i2) throws NamingException {
        decode(bArr, i2);
    }

    private void decode(byte[] bArr, int i2) throws NamingException {
        try {
            if (i2 < 12) {
                throw new CommunicationException("DNS error: corrupted message header");
            }
            this.xid = getShort(bArr, 0);
            int i3 = 0 + 2;
            short s2 = (short) getShort(bArr, i3);
            int i4 = i3 + 2;
            this.query = (s2 & Short.MIN_VALUE) == 0;
            this.opcode = (s2 & OPCODE_MASK) >>> 11;
            this.authoritative = (s2 & 1024) != 0;
            this.truncated = (s2 & 512) != 0;
            this.recursionDesired = (s2 & 256) != 0;
            this.recursionAvail = (s2 & 128) != 0;
            this.rcode = s2 & 15;
            this.numQuestions = getShort(bArr, i4);
            int i5 = i4 + 2;
            this.numAnswers = getShort(bArr, i5);
            int i6 = i5 + 2;
            this.numAuthorities = getShort(bArr, i6);
            int i7 = i6 + 2;
            this.numAdditionals = getShort(bArr, i7);
            int i8 = i7 + 2;
        } catch (IndexOutOfBoundsException e2) {
            throw new CommunicationException("DNS error: corrupted message header");
        }
    }

    private static int getShort(byte[] bArr, int i2) {
        return ((bArr[i2] & 255) << 8) | (bArr[i2 + 1] & 255);
    }
}
