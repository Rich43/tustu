package com.sun.nio.zipfs;

/* loaded from: zipfs.jar:com/sun/nio/zipfs/ZipConstants.class */
class ZipConstants {
    static final int METHOD_STORED = 0;
    static final int METHOD_DEFLATED = 8;
    static final int METHOD_DEFLATED64 = 9;
    static final int METHOD_BZIP2 = 12;
    static final int METHOD_LZMA = 14;
    static final int METHOD_LZ77 = 19;
    static final int METHOD_AES = 99;
    static final int FLAG_ENCRYPTED = 1;
    static final int FLAG_DATADESCR = 8;
    static final int FLAG_EFS = 2048;
    static long LOCSIG = 67324752;
    static long EXTSIG = 134695760;
    static long CENSIG = 33639248;
    static long ENDSIG = 101010256;
    static final int LOCHDR = 30;
    static final int EXTHDR = 16;
    static final int CENHDR = 46;
    static final int ENDHDR = 22;
    static final int LOCVER = 4;
    static final int LOCFLG = 6;
    static final int LOCHOW = 8;
    static final int LOCTIM = 10;
    static final int LOCCRC = 14;
    static final int LOCSIZ = 18;
    static final int LOCLEN = 22;
    static final int LOCNAM = 26;
    static final int LOCEXT = 28;
    static final int EXTCRC = 4;
    static final int EXTSIZ = 8;
    static final int EXTLEN = 12;
    static final int CENVEM = 4;
    static final int CENVER = 6;
    static final int CENFLG = 8;
    static final int CENHOW = 10;
    static final int CENTIM = 12;
    static final int CENCRC = 16;
    static final int CENSIZ = 20;
    static final int CENLEN = 24;
    static final int CENNAM = 28;
    static final int CENEXT = 30;
    static final int CENCOM = 32;
    static final int CENDSK = 34;
    static final int CENATT = 36;
    static final int CENATX = 38;
    static final int CENOFF = 42;
    static final int ENDSUB = 8;
    static final int ENDTOT = 10;
    static final int ENDSIZ = 12;
    static final int ENDOFF = 16;
    static final int ENDCOM = 20;
    static final long ZIP64_ENDSIG = 101075792;
    static final long ZIP64_LOCSIG = 117853008;
    static final int ZIP64_ENDHDR = 56;
    static final int ZIP64_LOCHDR = 20;
    static final int ZIP64_EXTHDR = 24;
    static final int ZIP64_EXTID = 1;
    static final int ZIP64_MINVAL32 = 65535;
    static final long ZIP64_MINVAL = 4294967295L;
    static final int ZIP64_ENDLEN = 4;
    static final int ZIP64_ENDVEM = 12;
    static final int ZIP64_ENDVER = 14;
    static final int ZIP64_ENDNMD = 16;
    static final int ZIP64_ENDDSK = 20;
    static final int ZIP64_ENDTOD = 24;
    static final int ZIP64_ENDTOT = 32;
    static final int ZIP64_ENDSIZ = 40;
    static final int ZIP64_ENDOFF = 48;
    static final int ZIP64_ENDEXT = 56;
    static final int ZIP64_LOCDSK = 4;
    static final int ZIP64_LOCOFF = 8;
    static final int ZIP64_LOCTOT = 16;
    static final int ZIP64_EXTCRC = 4;
    static final int ZIP64_EXTSIZ = 8;
    static final int ZIP64_EXTLEN = 16;
    static final int EXTID_ZIP64 = 1;
    static final int EXTID_NTFS = 10;
    static final int EXTID_UNIX = 13;
    static final int EXTID_EFS = 23;
    static final int EXTID_EXTT = 21589;
    static final long END_MAXLEN = 65557;
    static final int READBLOCKSZ = 128;

    ZipConstants() {
    }

    static final int CH(byte[] bArr, int i2) {
        return Byte.toUnsignedInt(bArr[i2]);
    }

    static final int SH(byte[] bArr, int i2) {
        return Byte.toUnsignedInt(bArr[i2]) | (Byte.toUnsignedInt(bArr[i2 + 1]) << 8);
    }

    static final long LG(byte[] bArr, int i2) {
        return (SH(bArr, i2) | (SH(bArr, i2 + 2) << 16)) & 4294967295L;
    }

    static final long LL(byte[] bArr, int i2) {
        return LG(bArr, i2) | (LG(bArr, i2 + 4) << 32);
    }

    static long getSig(byte[] bArr, int i2) {
        return LG(bArr, i2);
    }

    private static boolean pkSigAt(byte[] bArr, int i2, int i3, int i4) {
        return (bArr[i2] == 80) & (bArr[i2 + 1] == 75) & (bArr[i2 + 2] == i3) & (bArr[i2 + 3] == i4);
    }

    static boolean cenSigAt(byte[] bArr, int i2) {
        return pkSigAt(bArr, i2, 1, 2);
    }

    static boolean locSigAt(byte[] bArr, int i2) {
        return pkSigAt(bArr, i2, 3, 4);
    }

    static boolean endSigAt(byte[] bArr, int i2) {
        return pkSigAt(bArr, i2, 5, 6);
    }

    static boolean extSigAt(byte[] bArr, int i2) {
        return pkSigAt(bArr, i2, 7, 8);
    }

    static boolean end64SigAt(byte[] bArr, int i2) {
        return pkSigAt(bArr, i2, 6, 6);
    }

    static boolean locator64SigAt(byte[] bArr, int i2) {
        return pkSigAt(bArr, i2, 6, 7);
    }

    static final long LOCSIG(byte[] bArr) {
        return LG(bArr, 0);
    }

    static final int LOCVER(byte[] bArr) {
        return SH(bArr, 4);
    }

    static final int LOCFLG(byte[] bArr) {
        return SH(bArr, 6);
    }

    static final int LOCHOW(byte[] bArr) {
        return SH(bArr, 8);
    }

    static final long LOCTIM(byte[] bArr) {
        return LG(bArr, 10);
    }

    static final long LOCCRC(byte[] bArr) {
        return LG(bArr, 14);
    }

    static final long LOCSIZ(byte[] bArr) {
        return LG(bArr, 18);
    }

    static final long LOCLEN(byte[] bArr) {
        return LG(bArr, 22);
    }

    static final int LOCNAM(byte[] bArr) {
        return SH(bArr, 26);
    }

    static final int LOCEXT(byte[] bArr) {
        return SH(bArr, 28);
    }

    static final long EXTCRC(byte[] bArr) {
        return LG(bArr, 4);
    }

    static final long EXTSIZ(byte[] bArr) {
        return LG(bArr, 8);
    }

    static final long EXTLEN(byte[] bArr) {
        return LG(bArr, 12);
    }

    static final int ENDSUB(byte[] bArr) {
        return SH(bArr, 8);
    }

    static final int ENDTOT(byte[] bArr) {
        return SH(bArr, 10);
    }

    static final long ENDSIZ(byte[] bArr) {
        return LG(bArr, 12);
    }

    static final long ENDOFF(byte[] bArr) {
        return LG(bArr, 16);
    }

    static final int ENDCOM(byte[] bArr) {
        return SH(bArr, 20);
    }

    static final int ENDCOM(byte[] bArr, int i2) {
        return SH(bArr, i2 + 20);
    }

    static final long ZIP64_ENDTOD(byte[] bArr) {
        return LL(bArr, 24);
    }

    static final long ZIP64_ENDTOT(byte[] bArr) {
        return LL(bArr, 32);
    }

    static final long ZIP64_ENDSIZ(byte[] bArr) {
        return LL(bArr, 40);
    }

    static final long ZIP64_ENDOFF(byte[] bArr) {
        return LL(bArr, 48);
    }

    static final long ZIP64_LOCOFF(byte[] bArr) {
        return LL(bArr, 8);
    }

    static final long CENSIG(byte[] bArr, int i2) {
        return LG(bArr, i2 + 0);
    }

    static final int CENVEM(byte[] bArr, int i2) {
        return SH(bArr, i2 + 4);
    }

    static final int CENVER(byte[] bArr, int i2) {
        return SH(bArr, i2 + 6);
    }

    static final int CENFLG(byte[] bArr, int i2) {
        return SH(bArr, i2 + 8);
    }

    static final int CENHOW(byte[] bArr, int i2) {
        return SH(bArr, i2 + 10);
    }

    static final long CENTIM(byte[] bArr, int i2) {
        return LG(bArr, i2 + 12);
    }

    static final long CENCRC(byte[] bArr, int i2) {
        return LG(bArr, i2 + 16);
    }

    static final long CENSIZ(byte[] bArr, int i2) {
        return LG(bArr, i2 + 20);
    }

    static final long CENLEN(byte[] bArr, int i2) {
        return LG(bArr, i2 + 24);
    }

    static final int CENNAM(byte[] bArr, int i2) {
        return SH(bArr, i2 + 28);
    }

    static final int CENEXT(byte[] bArr, int i2) {
        return SH(bArr, i2 + 30);
    }

    static final int CENCOM(byte[] bArr, int i2) {
        return SH(bArr, i2 + 32);
    }

    static final int CENDSK(byte[] bArr, int i2) {
        return SH(bArr, i2 + 34);
    }

    static final int CENATT(byte[] bArr, int i2) {
        return SH(bArr, i2 + 36);
    }

    static final long CENATX(byte[] bArr, int i2) {
        return LG(bArr, i2 + 38);
    }

    static final long CENOFF(byte[] bArr, int i2) {
        return LG(bArr, i2 + 42);
    }
}
