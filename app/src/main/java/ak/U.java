package ak;

import W.C0186l;
import al.AbstractC0570d;
import al.C0567a;
import al.C0568b;
import al.C0571e;
import am.C0577e;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.IOException;
import java.nio.BufferUnderflowException;
import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.nio.channels.SeekableByteChannel;
import java.nio.file.FileSystems;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardOpenOption;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import org.apache.commons.net.ftp.FTP;

/* loaded from: TunerStudioMS.jar:ak/U.class */
public class U extends W.V {

    /* renamed from: b, reason: collision with root package name */
    ArrayList f4601b = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    List f4602e = new ArrayList();

    /* renamed from: f, reason: collision with root package name */
    int f4603f = 0;

    /* renamed from: g, reason: collision with root package name */
    boolean f4604g = false;

    /* renamed from: h, reason: collision with root package name */
    List f4605h = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    C0568b f4606i = null;

    /* renamed from: j, reason: collision with root package name */
    SeekableByteChannel f4607j = null;

    /* renamed from: k, reason: collision with root package name */
    int f4608k = -1;

    /* renamed from: l, reason: collision with root package name */
    int f4609l = 0;

    /* renamed from: m, reason: collision with root package name */
    float[] f4610m = null;

    /* renamed from: n, reason: collision with root package name */
    int[] f4611n = null;

    /* renamed from: o, reason: collision with root package name */
    String f4612o = null;

    /* renamed from: p, reason: collision with root package name */
    am.i f4613p = null;

    /* renamed from: q, reason: collision with root package name */
    am.h f4614q = null;

    /* renamed from: s, reason: collision with root package name */
    HashMap f4616s = new HashMap();

    /* renamed from: a, reason: collision with root package name */
    public static final byte[] f4600a = {77, 68, 70, 32, 32, 32, 32, 32, 52};

    /* renamed from: r, reason: collision with root package name */
    public static V f4615r = null;

    @Override // W.V
    public String i() {
        return W.X.f1997M;
    }

    @Override // W.V
    public boolean a(String str) throws V.a {
        Path path = FileSystems.getDefault().getPath(str, new String[0]);
        try {
            this.f4607j = Files.newByteChannel(path, StandardOpenOption.READ);
            if (!a(this.f4607j).startsWith("4")) {
                throw new V.a("This reader is for valid MDF 4 files only!");
            }
            this.f4613p = am.i.a(path, this.f4607j);
            this.f4614q = this.f4613p.j();
            Properties propertiesA = new C0571e().a(this.f4614q);
            StringBuilder sb = new StringBuilder();
            for (String str2 : propertiesA.stringPropertyNames()) {
                sb.append(str2).append(": ").append(propertiesA.getProperty(str2)).append("\n");
            }
            List<C0577e> listA = AbstractC0570d.a(this.f4614q);
            if (f4615r != null) {
                listA = f4615r.a(listA);
            }
            for (C0577e c0577e : listA) {
                try {
                    C0568b c0568b = new C0568b(this.f4607j, c0577e);
                    c0568b.a("Data Group " + c0577e.h());
                    if (c0568b.c() && c0568b.b() > 2) {
                        this.f4602e.add(c0568b);
                    }
                } catch (IOException | BufferUnderflowException e2) {
                    bH.C.b("Failed to add DataGroup: " + e2.getLocalizedMessage());
                }
            }
            if (this.f4602e.isEmpty()) {
                return false;
            }
            Iterator it = this.f4602e.iterator();
            while (it.hasNext()) {
                this.f4609l += ((C0568b) it.next()).f();
            }
            this.f4609l -= this.f4602e.size() - 1;
            this.f4606i = l();
            bH.C.d("Core Data Group selected: " + this.f4606i.h() + ", with cycle count of: " + this.f4606i.b());
            this.f4602e.remove(this.f4606i);
            sb.append("------------------ Data Groups Info ------------------\n");
            sb.append("Total Channels for all groups: ").append(this.f4609l).append("\n");
            sb.append("Data Rate driven by ").append(this.f4606i.h()).append(" Avg Records/Sec: ").append(bH.W.c(this.f4606i.a(), 3)).append("\n");
            sb.append(this.f4606i.h()).append(" ").append(this.f4606i.f()).append(" Channels:\n");
            for (C0567a c0567a : this.f4606i.e()) {
                sb.append("\t").append(c0567a.e());
                if (c0567a.f() == null || c0567a.f().isEmpty()) {
                    sb.append("\n");
                } else {
                    sb.append(" (").append(c0567a.f()).append(")\n");
                }
            }
            for (C0568b c0568b2 : this.f4602e) {
                c0568b2.a(false);
                sb.append("\n").append(c0568b2.h()).append(" - ").append(c0568b2.f()).append(" channels ").append(" at ").append(bH.W.c(c0568b2.a(), 3)).append(" rec/s ").append(" rec/s, Channels:\n");
                for (C0567a c0567a2 : c0568b2.e()) {
                    sb.append("\t").append(c0567a2.e());
                    if (c0567a2.f() == null || c0567a2.f().isEmpty()) {
                        sb.append("\n");
                    } else {
                        sb.append(" (").append(c0567a2.f()).append(")\n");
                    }
                }
            }
            this.f4612o = sb.toString();
            return true;
        } catch (IOException e3) {
            Logger.getLogger(U.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
            throw new V.a("Unable to open file!\nError: " + e3.getLocalizedMessage());
        }
    }

    private C0568b l() {
        if (1 == 0) {
            return null;
        }
        C0568b c0568b = null;
        for (C0568b c0568b2 : this.f4602e) {
            if (c0568b2.c() && c0568b2.b() * this.f4609l < 1200000000 && (c0568b == null || c0568b2.f() > 2)) {
                if (c0568b == null || c0568b2.b() > c0568b.b()) {
                    c0568b = c0568b2;
                }
            }
        }
        if (c0568b == null) {
            bH.C.b("Very large MDF4 dataset, looking for lower record core data group");
            for (C0568b c0568b3 : this.f4602e) {
                if (c0568b == null) {
                    c0568b = c0568b3;
                } else if (c0568b3.b() > 10000 && c0568b3.b() < c0568b.b()) {
                    c0568b = c0568b3;
                }
            }
        }
        return c0568b;
    }

    @Override // W.V
    public void a() {
        try {
            this.f4607j.close();
        } catch (Exception e2) {
        }
    }

    @Override // W.V
    public Iterator b() {
        this.f4608k = 0;
        Iterator it = this.f4606i.e().iterator();
        while (it.hasNext()) {
            this.f4601b.add(a((C0567a) it.next()));
        }
        Iterator it2 = this.f4602e.iterator();
        while (it2.hasNext()) {
            Iterator it3 = ((C0568b) it2.next()).e().iterator();
            while (it3.hasNext()) {
                this.f4601b.add(a((C0567a) it3.next()));
            }
        }
        this.f4608k = this.f4601b.size();
        this.f4610m = new float[this.f4608k];
        return this.f4601b.iterator();
    }

    private W.T a(C0567a c0567a) {
        C0543c c0543c = new C0543c();
        if (c0567a.e().equals(SchemaSymbols.ATTVAL_TIME)) {
            c0543c.a("Time");
            c0543c.a(3);
        } else {
            c0543c.a(c0567a.e());
            c0543c.a(c0567a.g());
        }
        if (c0567a.f() != null) {
            c0543c.b(c0567a.f());
        }
        List listB = c0567a.b();
        if (listB != null && !listB.isEmpty()) {
            c0543c.a(new C0186l(listB));
            c0543c.b(255);
        }
        if (c0567a.c() != 0.0d) {
            c0543c.c((float) c0567a.c());
        }
        if (c0567a.d() != 0.0d) {
            c0543c.d((float) c0567a.d());
        }
        return c0543c;
    }

    @Override // W.V
    public float[] c() throws V.a {
        if (this.f4603f >= this.f4606i.b()) {
            throw new V.f();
        }
        if (this.f4603f >= 500 && k()) {
            throw new V.a("This Edition is limited to loading 500 rows of data. \nPlease Register to load large log files.");
        }
        double d2 = this.f4606i.d();
        int i2 = 0;
        try {
            this.f4606i.a(d2, 0, this.f4610m);
            int iF = 0 + this.f4606i.f();
            i2 = 0 + 1;
            for (C0568b c0568b : this.f4602e) {
                try {
                    c0568b.a(d2, iF, this.f4610m);
                } catch (Exception e2) {
                    bH.C.b("Error updating " + c0568b.h() + " on record " + (this.f4603f + 1) + ". Error Message: " + e2.getLocalizedMessage());
                    int iIntValue = ((Integer) this.f4616s.getOrDefault(Integer.valueOf(i2), 0)).intValue() + 1;
                    this.f4616s.put(Integer.valueOf(i2), Integer.valueOf(iIntValue));
                    if (iIntValue > 4) {
                        c0568b.b(true);
                        bH.C.b("5 Errors updating " + c0568b.h() + ", disabling updates.");
                    }
                    e2.printStackTrace();
                }
                iF += c0568b.f();
                i2++;
            }
            this.f4603f++;
            return this.f4610m;
        } catch (IOException e3) {
            Logger.getLogger(U.class.getName()).log(Level.SEVERE, "Error updating data group values, record: " + this.f4603f + " Group " + i2, (Throwable) e3);
            throw new V.f();
        }
    }

    @Override // W.V
    public long d() {
        if (this.f4606i != null) {
            return this.f4606i.g().e();
        }
        return 200L;
    }

    @Override // W.V
    public boolean e() {
        return this.f4606i != null && ((long) this.f4603f) < this.f4606i.g().e();
    }

    @Override // W.V
    public boolean f() {
        return false;
    }

    @Override // W.V
    public HashMap g() {
        return new HashMap();
    }

    @Override // W.V
    public String h() {
        return this.f4612o;
    }

    private static String a(SeekableByteChannel seekableByteChannel) throws IOException {
        ByteBuffer byteBufferAllocate = ByteBuffer.allocate(64);
        byteBufferAllocate.order(ByteOrder.LITTLE_ENDIAN);
        seekableByteChannel.position(0L);
        seekableByteChannel.read(byteBufferAllocate);
        byteBufferAllocate.rewind();
        byte[] bArr = new byte[8];
        byteBufferAllocate.get(bArr);
        String str = new String(bArr, FTP.DEFAULT_CONTROL_ENCODING);
        if (!str.equals("MDF     ")) {
            throw new IOException("Invalid or corrupt MDF file: " + str);
        }
        byte[] bArr2 = new byte[8];
        byteBufferAllocate.get(bArr2);
        return new String(bArr2, FTP.DEFAULT_CONTROL_ENCODING);
    }
}
