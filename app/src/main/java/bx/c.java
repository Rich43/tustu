package bX;

import java.io.Serializable;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Iterator;
import java.util.LinkedHashMap;
import java.util.LinkedList;
import java.util.List;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: TunerStudioMS.jar:bX/c.class */
public class c implements Serializable {

    /* renamed from: c, reason: collision with root package name */
    private static final Logger f7669c;

    /* renamed from: d, reason: collision with root package name */
    private final byte f7670d;

    /* renamed from: e, reason: collision with root package name */
    private final byte[] f7671e;

    /* renamed from: f, reason: collision with root package name */
    private final boolean f7672f;

    /* renamed from: g, reason: collision with root package name */
    private static final Object[] f7673g;

    /* renamed from: a, reason: collision with root package name */
    static final Map f7674a;

    /* renamed from: b, reason: collision with root package name */
    static final /* synthetic */ boolean f7675b;

    public c(byte b2, byte[] bArr, boolean z2) {
        if (b2 == 0) {
            throw new IllegalArgumentException("code=0 is not allowed (reserved for padding");
        }
        if (b2 == -1) {
            throw new IllegalArgumentException("code=-1 is not allowed (reserved for End Of Options)");
        }
        this.f7670d = b2;
        this.f7671e = bArr != null ? (byte[]) bArr.clone() : null;
        this.f7672f = z2;
    }

    public c(byte b2, byte[] bArr) {
        this(b2, bArr, false);
    }

    public byte a() {
        return this.f7670d;
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof c)) {
            return false;
        }
        c cVar = (c) obj;
        return cVar.f7670d == this.f7670d && cVar.f7672f == this.f7672f && Arrays.equals(cVar.f7671e, this.f7671e);
    }

    public int hashCode() {
        return (this.f7670d ^ Arrays.hashCode(this.f7671e)) ^ (this.f7672f ? Integer.MIN_VALUE : 0);
    }

    public byte[] b() {
        return this.f7671e;
    }

    public static final boolean a(byte b2) {
        return e.BYTE.equals(f7674a.get(Byte.valueOf(b2)));
    }

    public static c a(byte b2, byte b3) {
        if (a(b2)) {
            return new c(b2, h(b3));
        }
        throw new IllegalArgumentException("DHCP option type (" + ((int) b2) + ") is not byte");
    }

    public byte c() {
        if (!a(this.f7670d)) {
            throw new IllegalArgumentException("DHCP option type (" + ((int) this.f7670d) + ") is not byte");
        }
        if (this.f7671e == null) {
            throw new IllegalStateException("value is null");
        }
        if (this.f7671e.length != 1) {
            throw new a("option " + ((int) this.f7670d) + " is wrong size:" + this.f7671e.length + " should be 1");
        }
        return this.f7671e[0];
    }

    public static final boolean b(byte b2) {
        return e.SHORT.equals(f7674a.get(Byte.valueOf(b2)));
    }

    public short d() {
        if (!b(this.f7670d)) {
            throw new IllegalArgumentException("DHCP option type (" + ((int) this.f7670d) + ") is not short");
        }
        if (this.f7671e == null) {
            throw new IllegalStateException("value is null");
        }
        if (this.f7671e.length != 2) {
            throw new a("option " + ((int) this.f7670d) + " is wrong size:" + this.f7671e.length + " should be 2");
        }
        return (short) (((this.f7671e[0] & 255) << 8) | (this.f7671e[1] & 255));
    }

    public static final boolean c(byte b2) {
        return e.INT.equals(f7674a.get(Byte.valueOf(b2)));
    }

    public int e() {
        if (!c(this.f7670d)) {
            throw new IllegalArgumentException("DHCP option type (" + ((int) this.f7670d) + ") is not int");
        }
        if (this.f7671e == null) {
            throw new IllegalStateException("value is null");
        }
        if (this.f7671e.length != 4) {
            throw new a("option " + ((int) this.f7670d) + " is wrong size:" + this.f7671e.length + " should be 4");
        }
        return ((this.f7671e[0] & 255) << 24) | ((this.f7671e[1] & 255) << 16) | ((this.f7671e[2] & 255) << 8) | (this.f7671e[3] & 255);
    }

    public static final boolean d(byte b2) {
        return e.INET.equals(f7674a.get(Byte.valueOf(b2)));
    }

    public InetAddress f() {
        if (!d(this.f7670d)) {
            throw new IllegalArgumentException("DHCP option type (" + ((int) this.f7670d) + ") is not InetAddr");
        }
        if (this.f7671e == null) {
            throw new IllegalStateException("value is null");
        }
        if (this.f7671e.length != 4) {
            throw new a("option " + ((int) this.f7670d) + " is wrong size:" + this.f7671e.length + " should be 4");
        }
        try {
            return InetAddress.getByAddress(this.f7671e);
        } catch (UnknownHostException e2) {
            f7669c.log(Level.SEVERE, "Unexpected UnknownHostException", (Throwable) e2);
            return null;
        }
    }

    public static final boolean e(byte b2) {
        return e.STRING.equals(f7674a.get(Byte.valueOf(b2)));
    }

    public String g() {
        if (!e(this.f7670d)) {
            throw new IllegalArgumentException("DHCP option type (" + ((int) this.f7670d) + ") is not String");
        }
        if (this.f7671e == null) {
            throw new IllegalStateException("value is null");
        }
        return f.f(this.f7671e);
    }

    public static final boolean f(byte b2) {
        return e.SHORTS.equals(f7674a.get(Byte.valueOf(b2)));
    }

    public short[] h() {
        if (!f(this.f7670d)) {
            throw new IllegalArgumentException("DHCP option type (" + ((int) this.f7670d) + ") is not short[]");
        }
        if (this.f7671e == null) {
            throw new IllegalStateException("value is null");
        }
        if (this.f7671e.length % 2 != 0) {
            throw new a("option " + ((int) this.f7670d) + " is wrong size:" + this.f7671e.length + " should be 2*X");
        }
        short[] sArr = new short[this.f7671e.length / 2];
        int i2 = 0;
        for (int i3 = 0; i3 < this.f7671e.length; i3 += 2) {
            sArr[i2] = (short) (((this.f7671e[i3] & 255) << 8) | (this.f7671e[i3 + 1] & 255));
            i2++;
        }
        return sArr;
    }

    public static final boolean g(byte b2) {
        return e.INETS.equals(f7674a.get(Byte.valueOf(b2)));
    }

    public InetAddress[] i() {
        if (!g(this.f7670d)) {
            throw new IllegalArgumentException("DHCP option type (" + ((int) this.f7670d) + ") is not InetAddr[]");
        }
        if (this.f7671e == null) {
            throw new IllegalStateException("value is null");
        }
        if (this.f7671e.length % 4 != 0) {
            throw new a("option " + ((int) this.f7670d) + " is wrong size:" + this.f7671e.length + " should be 4*X");
        }
        try {
            byte[] bArr = new byte[4];
            InetAddress[] inetAddressArr = new InetAddress[this.f7671e.length / 4];
            int i2 = 0;
            for (int i3 = 0; i3 < this.f7671e.length; i3 += 4) {
                bArr[0] = this.f7671e[i3];
                bArr[1] = this.f7671e[i3 + 1];
                bArr[2] = this.f7671e[i3 + 2];
                bArr[3] = this.f7671e[i3 + 3];
                inetAddressArr[i2] = InetAddress.getByAddress(bArr);
                i2++;
            }
            return inetAddressArr;
        } catch (UnknownHostException e2) {
            f7669c.log(Level.SEVERE, "Unexpected UnknownHostException", (Throwable) e2);
            return null;
        }
    }

    public static c a(byte b2, int i2) {
        if (c(b2)) {
            return new c(b2, a(i2));
        }
        throw new IllegalArgumentException("DHCP option type (" + ((int) b2) + ") is not int");
    }

    public static c a(byte b2, InetAddress inetAddress) {
        if (d(b2) || g(b2)) {
            return new c(b2, a(inetAddress));
        }
        throw new IllegalArgumentException("DHCP option type (" + ((int) b2) + ") is not InetAddress");
    }

    public static c a(byte b2, String str) {
        if (e(b2)) {
            return new c(b2, f.a(str));
        }
        throw new IllegalArgumentException("DHCP option type (" + ((int) b2) + ") is not string");
    }

    public c a(f fVar) {
        c cVarH;
        if (fVar == null) {
            throw new NullPointerException("request is null");
        }
        if (this.f7672f && (cVarH = fVar.h(a())) != null) {
            return cVarH;
        }
        return this;
    }

    public void a(StringBuilder sb) {
        if (b.f7667f.containsKey(Byte.valueOf(this.f7670d))) {
            sb.append((String) b.f7667f.get(Byte.valueOf(this.f7670d)));
        }
        sb.append('(').append(i(this.f7670d)).append(")=");
        if (this.f7672f) {
            sb.append("<mirror>");
        }
        if (this.f7671e == null) {
            sb.append("<null>");
            return;
        }
        if (this.f7670d == 53) {
            Byte bValueOf = Byte.valueOf(c());
            if (b.f7666e.containsKey(bValueOf)) {
                sb.append((String) b.f7666e.get(bValueOf));
                return;
            } else {
                sb.append((Object) bValueOf);
                return;
            }
        }
        if (this.f7670d == 77) {
            sb.append(b(this.f7671e));
            return;
        }
        if (this.f7670d == 82) {
            sb.append(c(this.f7671e));
            return;
        }
        if (!f7674a.containsKey(Byte.valueOf(this.f7670d))) {
            sb.append("0x");
            f.a(sb, this.f7671e);
            return;
        }
        try {
            switch ((e) f7674a.get(Byte.valueOf(this.f7670d))) {
                case INET:
                    f.a(sb, f());
                    break;
                case INETS:
                    for (InetAddress inetAddress : i()) {
                        f.a(sb, inetAddress);
                        sb.append(' ');
                    }
                    break;
                case INT:
                    sb.append(e());
                    break;
                case SHORT:
                    sb.append((int) d());
                    break;
                case SHORTS:
                    for (short s2 : h()) {
                        sb.append((int) s2).append(' ');
                    }
                    break;
                case BYTE:
                    sb.append((int) c());
                    break;
                case STRING:
                    sb.append('\"').append(g()).append('\"');
                    break;
                case BYTES:
                    if (this.f7671e != null) {
                        for (byte b2 : this.f7671e) {
                            sb.append(i(b2)).append(' ');
                        }
                        break;
                    }
                    break;
                default:
                    sb.append("0x");
                    f.a(sb, this.f7671e);
                    break;
            }
        } catch (IllegalArgumentException e2) {
            sb.append("0x");
            f.a(sb, this.f7671e);
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        a(sb);
        return sb.toString();
    }

    private static int i(byte b2) {
        return b2 & 255;
    }

    public static byte[] h(byte b2) {
        return new byte[]{b2};
    }

    public static byte[] a(int i2) {
        return new byte[]{(byte) ((i2 & (-16777216)) >>> 24), (byte) ((i2 & 16711680) >>> 16), (byte) ((i2 & NormalizerImpl.CC_MASK) >>> 8), (byte) (i2 & 255)};
    }

    public static byte[] a(InetAddress inetAddress) {
        if (inetAddress == null) {
            return null;
        }
        if (inetAddress instanceof Inet4Address) {
            return inetAddress.getAddress();
        }
        throw new IllegalArgumentException("Adress must be of subclass Inet4Address");
    }

    public static List a(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        LinkedList linkedList = new LinkedList();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= bArr.length) {
                return linkedList;
            }
            int i4 = i3 + 1;
            int i5 = i(bArr[i3]);
            int length = bArr.length - i4;
            if (i5 > length) {
                i5 = length;
            }
            linkedList.add(f.a(bArr, i4, i5));
            i2 = i4 + i5;
        }
    }

    public static String b(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        Iterator it = a(bArr).iterator();
        StringBuffer stringBuffer = new StringBuffer();
        while (it.hasNext()) {
            stringBuffer.append('\"').append((String) it.next()).append('\"');
            if (it.hasNext()) {
                stringBuffer.append(',');
            }
        }
        return stringBuffer.toString();
    }

    public static String c(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        Map mapD = d(bArr);
        StringBuffer stringBuffer = new StringBuffer();
        for (Map.Entry entry : mapD.entrySet()) {
            stringBuffer.append('{').append(i(((Byte) entry.getKey()).byteValue())).append("}\"");
            stringBuffer.append((String) entry.getValue()).append('\"');
            stringBuffer.append(',');
        }
        if (stringBuffer.length() > 0) {
            stringBuffer.setLength(stringBuffer.length() - 1);
        }
        return stringBuffer.toString();
    }

    public static final Map d(byte[] bArr) {
        if (bArr == null) {
            return null;
        }
        LinkedHashMap linkedHashMap = new LinkedHashMap();
        int i2 = 0;
        while (true) {
            int i3 = i2;
            if (i3 >= bArr.length || bArr.length - i3 < 2) {
                break;
            }
            int i4 = i3 + 1;
            Byte bValueOf = Byte.valueOf(bArr[i3]);
            int i5 = i4 + 1;
            int i6 = i(bArr[i4]);
            int length = bArr.length - i5;
            if (i6 > length) {
                i6 = length;
            }
            linkedHashMap.put(bValueOf, f.a(bArr, i5, i6));
            i2 = i5 + i6;
        }
        return linkedHashMap;
    }

    static {
        f7675b = !c.class.desiredAssertionStatus();
        f7669c = Logger.getLogger(c.class.getName().toLowerCase());
        f7673g = new Object[]{(byte) 1, e.INET, (byte) 2, e.INT, (byte) 3, e.INETS, (byte) 4, e.INETS, (byte) 5, e.INETS, (byte) 6, e.INETS, (byte) 7, e.INETS, (byte) 8, e.INETS, (byte) 9, e.INETS, (byte) 10, e.INETS, (byte) 11, e.INETS, (byte) 12, e.STRING, (byte) 13, e.SHORT, (byte) 14, e.STRING, (byte) 15, e.STRING, (byte) 16, e.INET, (byte) 17, e.STRING, (byte) 18, e.STRING, (byte) 19, e.BYTE, (byte) 20, e.BYTE, (byte) 21, e.INETS, (byte) 22, e.SHORT, (byte) 23, e.BYTE, (byte) 24, e.INT, (byte) 25, e.SHORTS, (byte) 26, e.SHORT, (byte) 27, e.BYTE, (byte) 28, e.INET, (byte) 29, e.BYTE, (byte) 30, e.BYTE, (byte) 31, e.BYTE, (byte) 32, e.INET, (byte) 33, e.INETS, (byte) 34, e.BYTE, (byte) 35, e.INT, (byte) 36, e.BYTE, (byte) 37, e.BYTE, (byte) 38, e.INT, (byte) 39, e.BYTE, (byte) 41, e.INETS, (byte) 42, e.INETS, (byte) 44, e.INETS, (byte) 45, e.INETS, (byte) 46, e.BYTE, (byte) 47, e.STRING, (byte) 48, e.INETS, (byte) 49, e.INETS, (byte) 50, e.INET, (byte) 51, e.INT, (byte) 52, e.BYTE, (byte) 53, e.BYTE, (byte) 54, e.INET, (byte) 55, e.BYTES, (byte) 56, e.STRING, (byte) 57, e.SHORT, (byte) 58, e.INT, (byte) 59, e.INT, (byte) 60, e.STRING, (byte) 62, e.STRING, (byte) 64, e.STRING, (byte) 65, e.STRING, (byte) 66, e.STRING, (byte) 67, e.STRING, (byte) 68, e.INETS, (byte) 69, e.INETS, (byte) 70, e.INETS, (byte) 71, e.INETS, (byte) 72, e.INETS, (byte) 73, e.INETS, (byte) 74, e.INETS, (byte) 75, e.INETS, (byte) 76, e.INETS, (byte) 85, e.INETS, (byte) 86, e.STRING, (byte) 87, e.STRING, (byte) 91, e.INT, (byte) 92, e.INETS, (byte) 98, e.STRING, (byte) 116, e.BYTE, (byte) 117, e.SHORTS, (byte) 118, e.INET, (byte) 119, e.STRING};
        f7674a = new LinkedHashMap();
        for (int i2 = 0; i2 < f7673g.length / 2; i2++) {
            f7674a.put((Byte) f7673g[i2 * 2], (e) f7673g[(i2 * 2) + 1]);
        }
    }
}
