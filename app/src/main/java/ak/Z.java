package ak;

import java.io.IOException;
import java.io.RandomAccessFile;
import java.nio.charset.StandardCharsets;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;
import net.lingala.zip4j.util.InternalZipConstants;

/* loaded from: TunerStudioMS.jar:ak/Z.class */
public class Z extends W.V {

    /* renamed from: a, reason: collision with root package name */
    String f4637a;

    /* renamed from: e, reason: collision with root package name */
    byte[] f4639e;

    /* renamed from: j, reason: collision with root package name */
    int f4644j;

    /* renamed from: k, reason: collision with root package name */
    boolean f4645k;

    /* renamed from: f, reason: collision with root package name */
    public static final byte[] f4640f = {77, 68, 70, 32, 32, 32, 32, 32};

    /* renamed from: s, reason: collision with root package name */
    public static final String[] f4653s = {"UINT", "INT", "FLOAT", "DOUBLE", "F_FLOAT", "G_FLOAT", "D_FLOAT", "STRING", "BYTE_ARRAY", "UINT_BE", "INT_BE", "FLOAT_BE", "DOUBLE_BE", "UINT_LE", "INT_LE", "FLOAT_LE", "DOUBLE_LE"};

    /* renamed from: t, reason: collision with root package name */
    private static final boolean[] f4654t = {false, true, true, true, true, true, true, true, true, false, true, true, true, false, true, true, true};

    /* renamed from: b, reason: collision with root package name */
    String f4638b = "";

    /* renamed from: g, reason: collision with root package name */
    HashMap f4641g = new HashMap();

    /* renamed from: h, reason: collision with root package name */
    List f4642h = new ArrayList();

    /* renamed from: i, reason: collision with root package name */
    Set f4643i = new TreeSet();

    /* renamed from: l, reason: collision with root package name */
    float[] f4646l = null;

    /* renamed from: m, reason: collision with root package name */
    int f4647m = 0;

    /* renamed from: n, reason: collision with root package name */
    int f4648n = 0;

    /* renamed from: o, reason: collision with root package name */
    double f4649o = Double.MAX_VALUE;

    /* renamed from: p, reason: collision with root package name */
    double f4650p = Double.NaN;

    /* renamed from: q, reason: collision with root package name */
    double f4651q = Double.MIN_VALUE;

    /* renamed from: r, reason: collision with root package name */
    double f4652r = 0.02d;

    @Override // W.V
    public boolean a(String str) {
        this.f4637a = str;
        RandomAccessFile randomAccessFile = null;
        try {
            try {
                randomAccessFile = new RandomAccessFile(str, InternalZipConstants.READ_MODE);
                this.f4639e = new byte[(int) randomAccessFile.length()];
                randomAccessFile.readFully(this.f4639e);
                C0538ag c0538ag = new C0538ag(this);
                C0533ab c0533ab = new C0533ab(this, 64);
                StringBuilder sbAppend = new StringBuilder().append("Date: ").append(c0533ab.f4673e).append("\n").append("Time: ").append(c0533ab.f4674f).append("\n").append("Format: ").append(c0538ag.f4728a).append(" ").append(c0538ag.f4729b).append(" ").append(c0538ag.f4731d ? "BE" : "LE").append("\n").append("Program: ").append(c0538ag.f4730c).append("\n").append("Operator: ").append(c0533ab.f4675g).append("\n").append("Divition: ").append(c0533ab.f4676h).append("\n").append("Project: ").append(c0533ab.f4677i).append("\n").append("Sub Project: ").append(c0533ab.f4678j).append("\n");
                if (c0533ab.f4680l != null) {
                    sbAppend.append("Start Comment:\n").append(c0533ab.f4680l.f4746b).append("\nEndComment\n").append("Logged Fields:\n");
                } else {
                    sbAppend.append("Start Comment:\n").append("").append("\nEndComment\n").append("Logged Fields:\n");
                }
                Iterator it = c0533ab.f4682n.iterator();
                while (it.hasNext()) {
                    for (C0535ad c0535ad : ((C0534ac) it.next()).f4688e) {
                        int i2 = 0;
                        Iterator it2 = c0535ad.f4697h.iterator();
                        while (it2.hasNext()) {
                            if (((C0536ae) it2.next()).f4710h == 7) {
                                i2++;
                            }
                        }
                        if (i2 < c0535ad.f4694e - 1) {
                            this.f4643i.add(c0535ad);
                        }
                    }
                }
                this.f4642h.add(new C0539ah(this));
                int size = 1;
                int i3 = -1;
                int i4 = 0;
                for (C0535ad c0535ad2 : this.f4643i) {
                    c0535ad2.f4699j = size;
                    size += c0535ad2.f4697h.size();
                    if (c0535ad2.f4696g > this.f4648n) {
                        this.f4648n = c0535ad2.f4696g;
                    }
                    for (C0536ae c0536ae : c0535ad2.f4697h) {
                        if (c0536ae.f4706d) {
                            i3++;
                            if (i3 > 0) {
                                StringBuilder sb = new StringBuilder();
                                C0536ae c0536ae2 = (C0536ae) c0535ad2.f4697h.get(0);
                                c0536ae2.f4707e = sb.append(c0536ae2.f4707e).append(i3).toString();
                            } else {
                                StringBuilder sb2 = new StringBuilder();
                                C0536ae c0536ae3 = (C0536ae) c0535ad2.f4697h.get(0);
                                c0536ae3.f4707e = sb2.append(c0536ae3.f4707e).append("0").toString();
                                if (c0536ae.f4711i > 0.0d && c0536ae.f4711i < this.f4652r) {
                                    this.f4652r = c0536ae.f4711i;
                                }
                            }
                            sbAppend.append("\nChannelGroup").append(i4).append(" ").append(c0535ad2.f4696g).append(" records (").append(c0536ae.f4711i).append("s ):\n");
                            this.f4649o = c0536ae.f4719q < this.f4649o ? c0536ae.f4719q : this.f4649o;
                            this.f4651q = c0536ae.f4720r > this.f4651q ? c0536ae.f4720r : this.f4651q;
                        }
                        sbAppend.append(c0536ae.f4707e).append("\n");
                        this.f4642h.add(c0536ae);
                    }
                    i4++;
                }
                double d2 = ((int) (this.f4649o / this.f4652r)) * this.f4652r;
                this.f4649o = d2;
                this.f4650p = d2;
                this.f4651q = ((int) (this.f4651q / this.f4652r)) * this.f4652r;
                if (this.f4651q > this.f4649o && this.f4652r > 0.0d) {
                    this.f4648n = ((int) ((this.f4651q - this.f4649o) / this.f4652r)) + 2;
                }
                this.f4646l = new float[this.f4642h.size()];
                this.f4638b = sbAppend.toString();
                for (int i5 = 0; i5 < this.f4646l.length; i5++) {
                    this.f4646l[i5] = Float.NaN;
                }
                if (randomAccessFile != null) {
                    try {
                        randomAccessFile.close();
                    } catch (IOException e2) {
                    }
                }
                return true;
            } catch (V.a | IOException e3) {
                throw new V.a(e3.getMessage(), e3);
            }
        } catch (Throwable th) {
            if (randomAccessFile != null) {
                try {
                    randomAccessFile.close();
                } catch (IOException e4) {
                }
            }
            throw th;
        }
    }

    @Override // W.V
    public void a() {
    }

    @Override // W.V
    public String i() {
        return W.X.f1996L;
    }

    @Override // W.V
    public Iterator b() {
        return this.f4642h.iterator();
    }

    @Override // W.V
    public float[] c() {
        this.f4646l[0] = (float) this.f4650p;
        Iterator it = this.f4643i.iterator();
        while (it.hasNext()) {
            ((C0535ad) it.next()).a();
        }
        this.f4650p += this.f4652r;
        this.f4647m++;
        return this.f4646l;
    }

    @Override // W.V
    public long d() {
        return this.f4648n;
    }

    @Override // W.V
    public boolean e() {
        return this.f4647m < this.f4648n;
    }

    @Override // W.V
    public boolean f() {
        return true;
    }

    @Override // W.V
    public HashMap g() {
        return this.f4641g;
    }

    @Override // W.V
    public String h() {
        return this.f4638b;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public String a(int i2, int i3) {
        return new String(this.f4639e, i2, i3, StandardCharsets.US_ASCII).replace("�", "°");
    }
}
