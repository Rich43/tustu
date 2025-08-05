package com.sun.jndi.dns;

import java.io.IOException;
import java.nio.charset.StandardCharsets;
import javax.naming.CommunicationException;
import javax.naming.InvalidNameException;

/* loaded from: rt.jar:com/sun/jndi/dns/ResourceRecord.class */
public class ResourceRecord {
    static final int TYPE_A = 1;
    static final int TYPE_NS = 2;
    static final int TYPE_CNAME = 5;
    static final int TYPE_SOA = 6;
    static final int TYPE_PTR = 12;
    static final int TYPE_HINFO = 13;
    static final int TYPE_MX = 15;
    static final int TYPE_TXT = 16;
    static final int TYPE_AAAA = 28;
    static final int TYPE_SRV = 33;
    static final int TYPE_NAPTR = 35;
    static final int QTYPE_AXFR = 252;
    static final int QTYPE_STAR = 255;
    static final int CLASS_INTERNET = 1;
    static final int CLASS_HESIOD = 2;
    static final int QCLASS_STAR = 255;
    private static final int MAXIMUM_COMPRESSION_REFERENCES = 16;
    byte[] msg;
    int msgLen;
    boolean qSection;
    int offset;
    int rrlen;
    DnsName name;
    int rrtype;
    String rrtypeName;
    int rrclass;
    String rrclassName;
    int ttl = 0;
    int rdlen = 0;
    Object rdata = null;
    private static final boolean debug = false;
    static final String[] rrTypeNames = {null, "A", "NS", null, null, "CNAME", "SOA", null, null, null, null, null, "PTR", "HINFO", null, "MX", "TXT", null, null, null, null, null, null, null, null, null, null, null, "AAAA", null, null, null, null, "SRV", null, "NAPTR"};
    static final String[] rrClassNames = {null, "IN", null, null, "HS"};

    ResourceRecord(byte[] bArr, int i2, int i3, boolean z2, boolean z3) throws IOException, CommunicationException {
        this.msg = bArr;
        this.msgLen = i2;
        this.offset = i3;
        this.qSection = z2;
        decode(z3);
    }

    public String toString() {
        String str = ((Object) this.name) + " " + this.rrclassName + " " + this.rrtypeName;
        if (!this.qSection) {
            str = str + " " + this.ttl + " " + (this.rdata != null ? this.rdata : "[n/a]");
        }
        return str;
    }

    public DnsName getName() {
        return this.name;
    }

    public int size() {
        return this.rrlen;
    }

    public int getType() {
        return this.rrtype;
    }

    public int getRrclass() {
        return this.rrclass;
    }

    public Object getRdata() {
        return this.rdata;
    }

    public static String getTypeName(int i2) {
        return valueToName(i2, rrTypeNames);
    }

    public static int getType(String str) {
        return nameToValue(str, rrTypeNames);
    }

    public static String getRrclassName(int i2) {
        return valueToName(i2, rrClassNames);
    }

    public static int getRrclass(String str) {
        return nameToValue(str, rrClassNames);
    }

    private static String valueToName(int i2, String[] strArr) {
        String string = null;
        if (i2 > 0 && i2 < strArr.length) {
            string = strArr[i2];
        } else if (i2 == 255) {
            string = "*";
        }
        if (string == null) {
            string = Integer.toString(i2);
        }
        return string;
    }

    private static int nameToValue(String str, String[] strArr) {
        if (str.equals("")) {
            return -1;
        }
        if (str.equals("*")) {
            return 255;
        }
        if (Character.isDigit(str.charAt(0))) {
            try {
                return Integer.parseInt(str);
            } catch (NumberFormatException e2) {
            }
        }
        for (int i2 = 1; i2 < strArr.length; i2++) {
            if (strArr[i2] != null && str.equalsIgnoreCase(strArr[i2])) {
                return i2;
            }
        }
        return -1;
    }

    public static int compareSerialNumbers(long j2, long j3) {
        long j4 = j3 - j2;
        if (j4 == 0) {
            return 0;
        }
        if (j4 > 0 && j4 <= 2147483647L) {
            return -1;
        }
        if (j4 < 0 && (-j4) > 2147483647L) {
            return -1;
        }
        return 1;
    }

    private void decode(boolean z2) throws IOException, CommunicationException {
        int i2 = this.offset;
        this.name = new DnsName();
        int iDecodeName = decodeName(i2, this.name);
        this.rrtype = getUShort(iDecodeName);
        this.rrtypeName = this.rrtype < rrTypeNames.length ? rrTypeNames[this.rrtype] : null;
        if (this.rrtypeName == null) {
            this.rrtypeName = Integer.toString(this.rrtype);
        }
        int i3 = iDecodeName + 2;
        this.rrclass = getUShort(i3);
        this.rrclassName = this.rrclass < rrClassNames.length ? rrClassNames[this.rrclass] : null;
        if (this.rrclassName == null) {
            this.rrclassName = Integer.toString(this.rrclass);
        }
        int i4 = i3 + 2;
        if (!this.qSection) {
            this.ttl = getInt(i4);
            int i5 = i4 + 4;
            this.rdlen = getUShort(i5);
            int i6 = i5 + 2;
            this.rdata = (z2 || this.rrtype == 6) ? decodeRdata(i6) : null;
            if (this.rdata instanceof DnsName) {
                this.rdata = this.rdata.toString();
            }
            i4 = i6 + this.rdlen;
        }
        this.rrlen = i4 - this.offset;
        this.msg = null;
    }

    private int getUByte(int i2) {
        return this.msg[i2] & 255;
    }

    private int getUShort(int i2) {
        return ((this.msg[i2] & 255) << 8) | (this.msg[i2 + 1] & 255);
    }

    private int getInt(int i2) {
        return (getUShort(i2) << 16) | getUShort(i2 + 2);
    }

    private long getUInt(int i2) {
        return getInt(i2) & 4294967295L;
    }

    private DnsName decodeName(int i2) throws IOException, CommunicationException {
        DnsName dnsName = new DnsName();
        decodeName(i2, dnsName);
        return dnsName;
    }

    private int decodeName(int i2, DnsName dnsName) throws IOException, CommunicationException {
        int i3 = -1;
        int i4 = 0;
        while (i4 <= 16) {
            try {
                int i5 = this.msg[i2] & 255;
                if (i5 == 0) {
                    int i6 = i2 + 1;
                    dnsName.add(0, "");
                    if (i3 == -1) {
                        i3 = i6;
                    }
                    return i3;
                }
                if (i5 <= 63) {
                    int i7 = i2 + 1;
                    dnsName.add(0, new String(this.msg, i7, i5, StandardCharsets.ISO_8859_1));
                    i2 = i7 + i5;
                } else if ((i5 & 192) == 192) {
                    i4++;
                    if (i3 == -1) {
                        i3 = i2 + 2;
                    }
                    i2 = getUShort(i2) & 16383;
                } else {
                    throw new IOException("Invalid label type: " + i5);
                }
            } catch (IOException | InvalidNameException e2) {
                CommunicationException communicationException = new CommunicationException("DNS error: malformed packet");
                communicationException.initCause(e2);
                throw communicationException;
            }
        }
        throw new IOException("Too many compression references");
    }

    private Object decodeRdata(int i2) throws CommunicationException {
        if (this.rrclass == 1) {
            switch (this.rrtype) {
                case 1:
                    return decodeA(i2);
                case 2:
                case 5:
                case 12:
                    return decodeName(i2);
                case 6:
                    return decodeSoa(i2);
                case 13:
                    return decodeHinfo(i2);
                case 15:
                    return decodeMx(i2);
                case 16:
                    return decodeTxt(i2);
                case 28:
                    return decodeAAAA(i2);
                case 33:
                    return decodeSrv(i2);
                case 35:
                    return decodeNaptr(i2);
            }
        }
        byte[] bArr = new byte[this.rdlen];
        System.arraycopy(this.msg, i2, bArr, 0, this.rdlen);
        return bArr;
    }

    private String decodeMx(int i2) throws IOException, CommunicationException {
        return getUShort(i2) + " " + ((Object) decodeName(i2 + 2));
    }

    private String decodeSoa(int i2) throws IOException, CommunicationException {
        DnsName dnsName = new DnsName();
        int iDecodeName = decodeName(i2, dnsName);
        DnsName dnsName2 = new DnsName();
        int iDecodeName2 = decodeName(iDecodeName, dnsName2);
        long uInt = getUInt(iDecodeName2);
        int i3 = iDecodeName2 + 4;
        long uInt2 = getUInt(i3);
        int i4 = i3 + 4;
        long uInt3 = getUInt(i4);
        int i5 = i4 + 4;
        long uInt4 = getUInt(i5);
        int i6 = i5 + 4;
        long uInt5 = getUInt(i6);
        int i7 = i6 + 4;
        return ((Object) dnsName) + " " + ((Object) dnsName2) + " " + uInt + " " + uInt2 + " " + uInt3 + " " + uInt4 + " " + uInt5;
    }

    private String decodeSrv(int i2) throws IOException, CommunicationException {
        int uShort = getUShort(i2);
        int i3 = i2 + 2;
        int uShort2 = getUShort(i3);
        int i4 = i3 + 2;
        return uShort + " " + uShort2 + " " + getUShort(i4) + " " + ((Object) decodeName(i4 + 2));
    }

    private String decodeNaptr(int i2) throws IOException, CommunicationException {
        int uShort = getUShort(i2);
        int i3 = i2 + 2;
        int uShort2 = getUShort(i3);
        int i4 = i3 + 2;
        StringBuffer stringBuffer = new StringBuffer();
        int iDecodeCharString = i4 + decodeCharString(i4, stringBuffer);
        StringBuffer stringBuffer2 = new StringBuffer();
        int iDecodeCharString2 = iDecodeCharString + decodeCharString(iDecodeCharString, stringBuffer2);
        StringBuffer stringBuffer3 = new StringBuffer(this.rdlen);
        return uShort + " " + uShort2 + " " + ((Object) stringBuffer) + " " + ((Object) stringBuffer2) + " " + ((Object) stringBuffer3) + " " + ((Object) decodeName(iDecodeCharString2 + decodeCharString(iDecodeCharString2, stringBuffer3)));
    }

    private String decodeTxt(int i2) {
        StringBuffer stringBuffer = new StringBuffer(this.rdlen);
        int i3 = i2 + this.rdlen;
        while (i2 < i3) {
            i2 += decodeCharString(i2, stringBuffer);
            if (i2 < i3) {
                stringBuffer.append(' ');
            }
        }
        return stringBuffer.toString();
    }

    private String decodeHinfo(int i2) {
        StringBuffer stringBuffer = new StringBuffer(this.rdlen);
        int iDecodeCharString = i2 + decodeCharString(i2, stringBuffer);
        stringBuffer.append(' ');
        int iDecodeCharString2 = iDecodeCharString + decodeCharString(iDecodeCharString, stringBuffer);
        return stringBuffer.toString();
    }

    private int decodeCharString(int i2, StringBuffer stringBuffer) {
        int length = stringBuffer.length();
        int i3 = i2 + 1;
        int uByte = getUByte(i2);
        boolean z2 = uByte == 0;
        for (int i4 = 0; i4 < uByte; i4++) {
            int i5 = i3;
            i3++;
            int uByte2 = getUByte(i5);
            z2 |= uByte2 == 32;
            if (uByte2 == 92 || uByte2 == 34) {
                z2 = true;
                stringBuffer.append('\\');
            }
            stringBuffer.append((char) uByte2);
        }
        if (z2) {
            stringBuffer.insert(length, '\"');
            stringBuffer.append('\"');
        }
        return uByte + 1;
    }

    private String decodeA(int i2) {
        return (this.msg[i2] & 255) + "." + (this.msg[i2 + 1] & 255) + "." + (this.msg[i2 + 2] & 255) + "." + (this.msg[i2 + 3] & 255);
    }

    private String decodeAAAA(int i2) {
        int[] iArr = new int[8];
        for (int i3 = 0; i3 < 8; i3++) {
            iArr[i3] = getUShort(i2);
            i2 += 2;
        }
        int i4 = -1;
        int i5 = 0;
        int i6 = -1;
        int i7 = 0;
        for (int i8 = 0; i8 < 8; i8++) {
            if (iArr[i8] == 0) {
                if (i4 == -1) {
                    i4 = i8;
                    i5 = 1;
                } else {
                    i5++;
                    if (i5 >= 2 && i5 > i7) {
                        i6 = i4;
                        i7 = i5;
                    }
                }
            } else {
                i4 = -1;
            }
        }
        if (i6 == 0) {
            if (i7 == 6 || (i7 == 7 && iArr[7] > 1)) {
                return "::" + decodeA(i2 - 4);
            }
            if (i7 == 5 && iArr[5] == 65535) {
                return "::ffff:" + decodeA(i2 - 4);
            }
        }
        boolean z2 = i6 != -1;
        StringBuffer stringBuffer = new StringBuffer(40);
        if (i6 == 0) {
            stringBuffer.append(':');
        }
        for (int i9 = 0; i9 < 8; i9++) {
            if (!z2 || i9 < i6 || i9 >= i6 + i7) {
                stringBuffer.append(Integer.toHexString(iArr[i9]));
                if (i9 < 7) {
                    stringBuffer.append(':');
                }
            } else if (z2 && i9 == i6) {
                stringBuffer.append(':');
            }
        }
        return stringBuffer.toString();
    }

    private static void dprint(String str) {
    }
}
