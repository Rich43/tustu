package bX;

import java.io.ByteArrayInputStream;
import java.io.ByteArrayOutputStream;
import java.io.DataInputStream;
import java.io.DataOutputStream;
import java.io.IOException;
import java.net.DatagramPacket;
import java.net.Inet4Address;
import java.net.InetAddress;
import java.net.InetSocketAddress;
import java.net.UnknownHostException;
import java.util.Arrays;
import java.util.Collection;
import java.util.Collections;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.logging.Level;
import java.util.logging.Logger;
import sun.text.normalizer.NormalizerImpl;

/* loaded from: TunerStudioMS.jar:bX/f.class */
public class f implements Cloneable {

    /* renamed from: b, reason: collision with root package name */
    private static final Logger f7686b;

    /* renamed from: g, reason: collision with root package name */
    private byte f7691g;

    /* renamed from: h, reason: collision with root package name */
    private int f7692h;

    /* renamed from: i, reason: collision with root package name */
    private short f7693i;

    /* renamed from: j, reason: collision with root package name */
    private short f7694j;

    /* renamed from: t, reason: collision with root package name */
    private boolean f7704t;

    /* renamed from: v, reason: collision with root package name */
    private InetAddress f7706v;

    /* renamed from: w, reason: collision with root package name */
    private int f7707w;

    /* renamed from: x, reason: collision with root package name */
    private static final char[] f7708x;

    /* renamed from: a, reason: collision with root package name */
    static final /* synthetic */ boolean f7709a;

    /* renamed from: c, reason: collision with root package name */
    private String f7687c = "";

    /* renamed from: d, reason: collision with root package name */
    private byte f7688d = 2;

    /* renamed from: e, reason: collision with root package name */
    private byte f7689e = 1;

    /* renamed from: f, reason: collision with root package name */
    private byte f7690f = 6;

    /* renamed from: k, reason: collision with root package name */
    private byte[] f7695k = new byte[4];

    /* renamed from: l, reason: collision with root package name */
    private byte[] f7696l = new byte[4];

    /* renamed from: m, reason: collision with root package name */
    private byte[] f7697m = new byte[4];

    /* renamed from: n, reason: collision with root package name */
    private byte[] f7698n = new byte[4];

    /* renamed from: o, reason: collision with root package name */
    private byte[] f7699o = new byte[16];

    /* renamed from: p, reason: collision with root package name */
    private byte[] f7700p = new byte[64];

    /* renamed from: q, reason: collision with root package name */
    private byte[] f7701q = new byte[128];

    /* renamed from: u, reason: collision with root package name */
    private byte[] f7705u = new byte[0];

    /* renamed from: s, reason: collision with root package name */
    private boolean f7703s = true;

    /* renamed from: r, reason: collision with root package name */
    private Map f7702r = new LinkedHashMap();

    public static f a(DatagramPacket datagramPacket) {
        if (datagramPacket == null) {
            throw new IllegalArgumentException("datagram is null");
        }
        f fVar = new f();
        fVar.a(datagramPacket.getData(), datagramPacket.getOffset(), datagramPacket.getLength(), datagramPacket.getAddress(), datagramPacket.getPort(), true);
        return fVar;
    }

    /* renamed from: a, reason: merged with bridge method [inline-methods] */
    public f clone() {
        try {
            f fVar = (f) super.clone();
            fVar.f7695k = (byte[]) this.f7695k.clone();
            fVar.f7696l = (byte[]) this.f7696l.clone();
            fVar.f7697m = (byte[]) this.f7697m.clone();
            fVar.f7698n = (byte[]) this.f7698n.clone();
            fVar.f7699o = (byte[]) this.f7699o.clone();
            fVar.f7700p = (byte[]) this.f7700p.clone();
            fVar.f7701q = (byte[]) this.f7701q.clone();
            fVar.f7702r = new LinkedHashMap(this.f7702r);
            fVar.f7705u = (byte[]) this.f7705u.clone();
            fVar.f7704t = false;
            return fVar;
        } catch (CloneNotSupportedException e2) {
            throw new InternalError();
        }
    }

    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof f)) {
            return false;
        }
        f fVar = (f) obj;
        return this.f7687c.equals(fVar.f7687c) & (this.f7688d == fVar.f7688d) & (this.f7689e == fVar.f7689e) & (this.f7690f == fVar.f7690f) & (this.f7691g == fVar.f7691g) & (this.f7692h == fVar.f7692h) & (this.f7693i == fVar.f7693i) & (this.f7694j == fVar.f7694j) & Arrays.equals(this.f7695k, fVar.f7695k) & Arrays.equals(this.f7696l, fVar.f7696l) & Arrays.equals(this.f7697m, fVar.f7697m) & Arrays.equals(this.f7698n, fVar.f7698n) & Arrays.equals(this.f7699o, fVar.f7699o) & Arrays.equals(this.f7700p, fVar.f7700p) & Arrays.equals(this.f7701q, fVar.f7701q) & this.f7702r.equals(fVar.f7702r) & (this.f7703s == fVar.f7703s) & Arrays.equals(this.f7705u, fVar.f7705u) & a(this.f7706v, fVar.f7706v) & (this.f7707w == fVar.f7707w);
    }

    public int hashCode() {
        return ((((((((((((((((((((-1) ^ this.f7687c.hashCode()) + this.f7688d) + this.f7689e) + this.f7690f) + this.f7691g) + this.f7692h) + this.f7693i) ^ this.f7694j) ^ Arrays.hashCode(this.f7695k)) ^ Arrays.hashCode(this.f7696l)) ^ Arrays.hashCode(this.f7697m)) ^ Arrays.hashCode(this.f7698n)) ^ Arrays.hashCode(this.f7699o)) ^ Arrays.hashCode(this.f7700p)) ^ Arrays.hashCode(this.f7701q)) ^ this.f7702r.hashCode()) + (this.f7703s ? 1 : 0)) ^ Arrays.hashCode(this.f7705u)) ^ (this.f7706v != null ? this.f7706v.hashCode() : 0)) + this.f7707w;
    }

    private static boolean a(Object obj, Object obj2) {
        return obj == null ? obj2 == null : obj.equals(obj2);
    }

    private void A() {
        if (!f7709a && this.f7687c == null) {
            throw new AssertionError();
        }
        if (!f7709a && this.f7695k == null) {
            throw new AssertionError();
        }
        if (!f7709a && this.f7695k.length != 4) {
            throw new AssertionError();
        }
        if (!f7709a && this.f7696l == null) {
            throw new AssertionError();
        }
        if (!f7709a && this.f7696l.length != 4) {
            throw new AssertionError();
        }
        if (!f7709a && this.f7697m == null) {
            throw new AssertionError();
        }
        if (!f7709a && this.f7697m.length != 4) {
            throw new AssertionError();
        }
        if (!f7709a && this.f7698n == null) {
            throw new AssertionError();
        }
        if (!f7709a && this.f7698n.length != 4) {
            throw new AssertionError();
        }
        if (!f7709a && this.f7699o == null) {
            throw new AssertionError();
        }
        if (!f7709a && this.f7699o.length != 16) {
            throw new AssertionError();
        }
        if (!f7709a && this.f7700p == null) {
            throw new AssertionError();
        }
        if (!f7709a && this.f7700p.length != 64) {
            throw new AssertionError();
        }
        if (!f7709a && this.f7701q == null) {
            throw new AssertionError();
        }
        if (!f7709a && this.f7701q.length != 128) {
            throw new AssertionError();
        }
        if (!f7709a && this.f7705u == null) {
            throw new AssertionError();
        }
        if (!f7709a && this.f7702r == null) {
            throw new AssertionError();
        }
        for (Map.Entry entry : this.f7702r.entrySet()) {
            Byte b2 = (Byte) entry.getKey();
            c cVar = (c) entry.getValue();
            if (!f7709a && b2 == null) {
                throw new AssertionError();
            }
            if (!f7709a && b2.byteValue() == 0) {
                throw new AssertionError();
            }
            if (!f7709a && b2.byteValue() == -1) {
                throw new AssertionError();
            }
            if (!f7709a && cVar == null) {
                throw new AssertionError();
            }
            if (!f7709a && cVar.a() != b2.byteValue()) {
                throw new AssertionError();
            }
            if (!f7709a && cVar.b() == null) {
                throw new AssertionError();
            }
        }
    }

    protected f a(byte[] bArr, int i2, int i3, InetAddress inetAddress, int i4, boolean z2) {
        int i5;
        if (bArr == null) {
            throw new IllegalArgumentException("null buffer not allowed");
        }
        if (i2 < 0) {
            throw new IndexOutOfBoundsException("negative offset not allowed");
        }
        if (i3 < 0) {
            throw new IllegalArgumentException("negative length not allowed");
        }
        if (bArr.length < i2 + i3) {
            throw new IndexOutOfBoundsException("offset+length exceeds buffer length");
        }
        if (i3 < 236) {
            throw new a("DHCP Packet too small (" + i3 + ") absolute minimum is 236");
        }
        if (i3 > 1500) {
            throw new a("DHCP Packet too big (" + i3 + ") max MTU is 1500");
        }
        this.f7706v = inetAddress;
        this.f7707w = i4;
        try {
            ByteArrayInputStream byteArrayInputStream = new ByteArrayInputStream(bArr, i2, i3);
            DataInputStream dataInputStream = new DataInputStream(byteArrayInputStream);
            this.f7688d = dataInputStream.readByte();
            this.f7689e = dataInputStream.readByte();
            this.f7690f = dataInputStream.readByte();
            this.f7691g = dataInputStream.readByte();
            this.f7692h = dataInputStream.readInt();
            this.f7693i = dataInputStream.readShort();
            this.f7694j = dataInputStream.readShort();
            dataInputStream.readFully(this.f7695k, 0, 4);
            dataInputStream.readFully(this.f7696l, 0, 4);
            dataInputStream.readFully(this.f7697m, 0, 4);
            dataInputStream.readFully(this.f7698n, 0, 4);
            dataInputStream.readFully(this.f7699o, 0, 16);
            dataInputStream.readFully(this.f7700p, 0, 64);
            dataInputStream.readFully(this.f7701q, 0, 128);
            this.f7703s = true;
            byteArrayInputStream.mark(4);
            if (dataInputStream.readInt() != 1669485411) {
                this.f7703s = false;
                byteArrayInputStream.reset();
            }
            if (this.f7703s) {
                byte b2 = 0;
                while (true) {
                    int i6 = byteArrayInputStream.read();
                    if (i6 < 0) {
                        break;
                    }
                    b2 = (byte) i6;
                    if (b2 != 0) {
                        if (b2 != -1 && (i5 = byteArrayInputStream.read()) >= 0) {
                            byte[] bArr2 = new byte[Math.min(i5, byteArrayInputStream.available())];
                            byteArrayInputStream.read(bArr2);
                            a(new c(b2, bArr2));
                        }
                    }
                }
                this.f7704t = b2 != -1;
                if (z2 && this.f7704t) {
                    throw new a("Packet seams to be truncated");
                }
            }
            this.f7705u = new byte[byteArrayInputStream.available()];
            byteArrayInputStream.read(this.f7705u);
            A();
            return this;
        } catch (IOException e2) {
            throw new a("IOException: " + e2.toString(), e2);
        }
    }

    public byte[] b() {
        int i2 = 236;
        if (this.f7703s) {
            i2 = 236 + 64;
        }
        return a(i2, 576);
    }

    public byte[] a(int i2, int i3) {
        A();
        ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream(750);
        DataOutputStream dataOutputStream = new DataOutputStream(byteArrayOutputStream);
        try {
            dataOutputStream.writeByte(this.f7688d);
            dataOutputStream.writeByte(this.f7689e);
            dataOutputStream.writeByte(this.f7690f);
            dataOutputStream.writeByte(this.f7691g);
            dataOutputStream.writeInt(this.f7692h);
            dataOutputStream.writeShort(this.f7693i);
            dataOutputStream.writeShort(this.f7694j);
            dataOutputStream.write(this.f7695k, 0, 4);
            dataOutputStream.write(this.f7696l, 0, 4);
            dataOutputStream.write(this.f7697m, 0, 4);
            dataOutputStream.write(this.f7698n, 0, 4);
            dataOutputStream.write(this.f7699o, 0, 16);
            dataOutputStream.write(this.f7700p, 0, 64);
            dataOutputStream.write(this.f7701q, 0, 128);
            if (this.f7703s) {
                dataOutputStream.writeInt(1669485411);
                for (c cVar : x()) {
                    if (!f7709a && cVar == null) {
                        throw new AssertionError();
                    }
                    if (!f7709a && cVar.a() == 0) {
                        throw new AssertionError();
                    }
                    if (!f7709a && cVar.a() == -1) {
                        throw new AssertionError();
                    }
                    if (!f7709a && cVar.b() == null) {
                        throw new AssertionError();
                    }
                    int length = cVar.b().length;
                    if (!f7709a && length < 0) {
                        throw new AssertionError();
                    }
                    if (length > 255) {
                        throw new a("Options larger than 255 bytes are not yet supported");
                    }
                    dataOutputStream.writeByte(cVar.a());
                    dataOutputStream.writeByte(length);
                    dataOutputStream.write(cVar.b());
                }
                dataOutputStream.writeByte(-1);
            }
            dataOutputStream.write(this.f7705u);
            int size = i2 - byteArrayOutputStream.size();
            if (size > 0) {
                dataOutputStream.write(new byte[size]);
            }
            byte[] byteArray = byteArrayOutputStream.toByteArray();
            if (byteArray.length > 1500) {
                throw new a("serialize: packet too big (" + byteArray.length + " greater than max MAX_MTU (1500)");
            }
            return byteArray;
        } catch (IOException e2) {
            f7686b.log(Level.SEVERE, "Unexpected Exception", (Throwable) e2);
            throw new a("IOException raised: " + e2.toString());
        }
    }

    public String toString() {
        StringBuilder sb = new StringBuilder();
        try {
            sb.append(this.f7703s ? "DHCP Packet" : "BOOTP Packet").append("\ncomment=").append(this.f7687c).append("\naddress=").append(this.f7706v != null ? this.f7706v.getHostAddress() : "").append('(').append(this.f7707w).append(')').append("\nop=");
            Object obj = b.f7664c.get(Byte.valueOf(this.f7688d));
            if (obj != null) {
                sb.append(obj).append('(').append((int) this.f7688d).append(')');
            } else {
                sb.append((int) this.f7688d);
            }
            sb.append("\nhtype=");
            Object obj2 = b.f7665d.get(Byte.valueOf(this.f7689e));
            if (obj2 != null) {
                sb.append(obj2).append('(').append((int) this.f7689e).append(')');
            } else {
                sb.append((int) this.f7689e);
            }
            sb.append("\nhlen=").append((int) this.f7690f).append("\nhops=").append((int) this.f7691g).append("\nxid=0x");
            a(sb, this.f7692h);
            sb.append("\nsecs=").append((int) this.f7693i).append("\nflags=0x").append(Integer.toHexString(this.f7694j)).append("\nciaddr=");
            a(sb, InetAddress.getByAddress(this.f7695k));
            sb.append("\nyiaddr=");
            a(sb, InetAddress.getByAddress(this.f7696l));
            sb.append("\nsiaddr=");
            a(sb, InetAddress.getByAddress(this.f7697m));
            sb.append("\ngiaddr=");
            a(sb, InetAddress.getByAddress(this.f7698n));
            sb.append("\nchaddr=0x");
            a(sb);
            sb.append("\nsname=").append(s()).append("\nfile=").append(h());
            if (this.f7703s) {
                sb.append("\nOptions follows:");
                for (c cVar : x()) {
                    sb.append('\n');
                    cVar.a(sb);
                }
            }
            sb.append("\npadding[").append(this.f7705u.length).append("]=");
            a(sb, this.f7705u);
        } catch (Exception e2) {
        }
        return sb.toString();
    }

    public byte[] c() {
        return (byte[]) this.f7699o.clone();
    }

    private StringBuilder a(StringBuilder sb) {
        a(sb, this.f7699o, 0, this.f7690f & 255);
        return sb;
    }

    public String d() {
        return a(new StringBuilder(this.f7690f & 255)).toString();
    }

    public void a(byte[] bArr) {
        if (bArr == null) {
            Arrays.fill(this.f7699o, (byte) 0);
        } else {
            if (bArr.length > this.f7699o.length) {
                throw new IllegalArgumentException("chaddr is too long: " + bArr.length + ", max is: " + this.f7699o.length);
            }
            Arrays.fill(this.f7699o, (byte) 0);
            System.arraycopy(bArr, 0, this.f7699o, 0, bArr.length);
        }
    }

    public InetAddress e() {
        try {
            return InetAddress.getByAddress(f());
        } catch (UnknownHostException e2) {
            f7686b.log(Level.SEVERE, "Unexpected UnknownHostException", (Throwable) e2);
            return null;
        }
    }

    public byte[] f() {
        return (byte[]) this.f7695k.clone();
    }

    public void b(byte[] bArr) {
        if (bArr.length != 4) {
            throw new IllegalArgumentException("4-byte array required");
        }
        System.arraycopy(bArr, 0, this.f7695k, 0, 4);
    }

    public byte[] g() {
        return (byte[]) this.f7701q.clone();
    }

    public String h() {
        return f(g());
    }

    public short i() {
        return this.f7694j;
    }

    public void a(short s2) {
        this.f7694j = s2;
    }

    public InetAddress j() {
        try {
            return InetAddress.getByAddress(k());
        } catch (UnknownHostException e2) {
            f7686b.log(Level.SEVERE, "Unexpected UnknownHostException", (Throwable) e2);
            return null;
        }
    }

    public byte[] k() {
        return (byte[]) this.f7698n.clone();
    }

    public void c(byte[] bArr) {
        if (bArr.length != 4) {
            throw new IllegalArgumentException("4-byte array required");
        }
        System.arraycopy(bArr, 0, this.f7698n, 0, 4);
    }

    public byte l() {
        return this.f7690f;
    }

    public void a(byte b2) {
        this.f7690f = b2;
    }

    public byte m() {
        return this.f7689e;
    }

    public void b(byte b2) {
        this.f7689e = b2;
    }

    public boolean n() {
        return this.f7703s;
    }

    public byte o() {
        return this.f7688d;
    }

    public void c(byte b2) {
        this.f7688d = b2;
    }

    public InetAddress p() {
        try {
            return InetAddress.getByAddress(q());
        } catch (UnknownHostException e2) {
            f7686b.log(Level.SEVERE, "Unexpected UnknownHostException", (Throwable) e2);
            return null;
        }
    }

    public byte[] q() {
        return (byte[]) this.f7697m.clone();
    }

    public void a(InetAddress inetAddress) {
        if (!(inetAddress instanceof Inet4Address)) {
            throw new IllegalArgumentException("Inet4Address required");
        }
        d(inetAddress.getAddress());
    }

    public void d(byte[] bArr) {
        if (bArr.length != 4) {
            throw new IllegalArgumentException("4-byte array required");
        }
        System.arraycopy(bArr, 0, this.f7697m, 0, 4);
    }

    public byte[] r() {
        return (byte[]) this.f7700p.clone();
    }

    public String s() {
        return f(r());
    }

    public int t() {
        return this.f7692h;
    }

    public void a(int i2) {
        this.f7692h = i2;
    }

    public InetAddress u() {
        try {
            return InetAddress.getByAddress(v());
        } catch (UnknownHostException e2) {
            f7686b.log(Level.SEVERE, "Unexpected UnknownHostException", (Throwable) e2);
            return null;
        }
    }

    public byte[] v() {
        return (byte[]) this.f7696l.clone();
    }

    public void b(InetAddress inetAddress) {
        if (!(inetAddress instanceof Inet4Address)) {
            throw new IllegalArgumentException("Inet4Address required");
        }
        e(inetAddress.getAddress());
    }

    public void e(byte[] bArr) {
        if (bArr.length != 4) {
            throw new IllegalArgumentException("4-byte array required");
        }
        System.arraycopy(bArr, 0, this.f7696l, 0, 4);
    }

    public Byte w() {
        return e((byte) 53);
    }

    public void d(byte b2) {
        a((byte) 53, b2);
    }

    public Byte e(byte b2) {
        c cVarH = h(b2);
        if (cVarH == null) {
            return null;
        }
        return Byte.valueOf(cVarH.c());
    }

    public Integer f(byte b2) {
        c cVarH = h(b2);
        if (cVarH == null) {
            return null;
        }
        return Integer.valueOf(cVarH.e());
    }

    public String g(byte b2) {
        c cVarH = h(b2);
        if (cVarH == null) {
            return null;
        }
        return cVarH.g();
    }

    public void a(byte b2, byte b3) {
        a(c.a(b2, b3));
    }

    public void a(byte b2, int i2) {
        a(c.a(b2, i2));
    }

    public void a(byte b2, InetAddress inetAddress) {
        a(c.a(b2, inetAddress));
    }

    public void a(byte b2, String str) {
        a(c.a(b2, str));
    }

    public c h(byte b2) {
        c cVar = (c) this.f7702r.get(Byte.valueOf(b2));
        if (cVar == null) {
            return null;
        }
        if (!f7709a && cVar.a() != b2) {
            throw new AssertionError();
        }
        if (f7709a || cVar.b() != null) {
            return cVar;
        }
        throw new AssertionError();
    }

    public boolean i(byte b2) {
        return this.f7702r.containsKey(Byte.valueOf(b2));
    }

    public Collection x() {
        return Collections.unmodifiableCollection(this.f7702r.values());
    }

    public void a(c cVar) {
        if (cVar != null) {
            if (cVar.b() == null) {
                j(cVar.a());
            } else {
                this.f7702r.put(Byte.valueOf(cVar.a()), cVar);
            }
        }
    }

    public void j(byte b2) {
        this.f7702r.remove(Byte.valueOf(b2));
    }

    public InetAddress y() {
        return this.f7706v;
    }

    public void c(InetAddress inetAddress) {
        if (inetAddress == null) {
            this.f7706v = null;
        } else {
            if (!(inetAddress instanceof Inet4Address)) {
                throw new IllegalArgumentException("only IPv4 addresses accepted");
            }
            this.f7706v = inetAddress;
        }
    }

    public int z() {
        return this.f7707w;
    }

    public void b(int i2) {
        this.f7707w = i2;
    }

    public void a(InetSocketAddress inetSocketAddress) {
        if (inetSocketAddress == null) {
            c((InetAddress) null);
            b(0);
        } else {
            c(inetSocketAddress.getAddress());
            b(inetSocketAddress.getPort());
        }
    }

    static String f(byte[] bArr) {
        return bArr == null ? "" : a(bArr, 0, bArr.length);
    }

    static String a(byte[] bArr, int i2, int i3) {
        if (bArr == null) {
            return "";
        }
        if (i2 < 0) {
            i3 += i2;
            i2 = 0;
        }
        if (i3 <= 0 || i2 >= bArr.length) {
            return "";
        }
        if (i2 + i3 > bArr.length) {
            i3 = bArr.length - i2;
        }
        int i4 = i2;
        while (true) {
            if (i4 >= i2 + i3) {
                break;
            }
            if (bArr[i4] == 0) {
                i3 = i4 - i2;
                break;
            }
            i4++;
        }
        char[] cArr = new char[i3];
        for (int i5 = i2; i5 < i2 + i3; i5++) {
            cArr[i5 - i2] = (char) bArr[i5];
        }
        return new String(cArr);
    }

    static void a(StringBuilder sb, byte b2) {
        int i2 = b2 & 255;
        sb.append(f7708x[(i2 & 240) >> 4]).append(f7708x[i2 & 15]);
    }

    static void a(StringBuilder sb, byte[] bArr, int i2, int i3) {
        if (bArr == null) {
            return;
        }
        if (i2 < 0) {
            i3 += i2;
            i2 = 0;
        }
        if (i3 <= 0 || i2 >= bArr.length) {
            return;
        }
        if (i2 + i3 > bArr.length) {
            i3 = bArr.length - i2;
        }
        for (int i4 = i2; i4 < i2 + i3; i4++) {
            a(sb, bArr[i4]);
        }
    }

    static void a(StringBuilder sb, byte[] bArr) {
        a(sb, bArr, 0, bArr.length);
    }

    private static void a(StringBuilder sb, int i2) {
        a(sb, (byte) ((i2 & (-16777216)) >>> 24));
        a(sb, (byte) ((i2 & 16711680) >>> 16));
        a(sb, (byte) ((i2 & NormalizerImpl.CC_MASK) >>> 8));
        a(sb, (byte) (i2 & 255));
    }

    public static byte[] a(String str) {
        if (str == null) {
            return null;
        }
        char[] charArray = str.toCharArray();
        int length = charArray.length;
        byte[] bArr = new byte[length];
        for (int i2 = 0; i2 < length; i2++) {
            bArr[i2] = (byte) charArray[i2];
        }
        return bArr;
    }

    public static void a(StringBuilder sb, InetAddress inetAddress) {
        if (inetAddress == null) {
            throw new IllegalArgumentException("addr must not be null");
        }
        if (!(inetAddress instanceof Inet4Address)) {
            throw new IllegalArgumentException("addr must be an instance of Inet4Address");
        }
        byte[] address = inetAddress.getAddress();
        sb.append(address[0] & 255).append('.').append(address[1] & 255).append('.').append(address[2] & 255).append('.').append(address[3] & 255);
    }

    static {
        f7709a = !f.class.desiredAssertionStatus();
        f7686b = Logger.getLogger(f.class.getName().toLowerCase());
        f7708x = new char[]{'0', '1', '2', '3', '4', '5', '6', '7', '8', '9', 'A', 'B', 'C', 'D', 'E', 'F'};
    }
}
