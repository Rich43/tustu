package ak;

import W.C0186l;
import bH.C0995c;
import com.sun.media.sound.DLSModulator;
import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.IOException;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;
import java.util.zip.GZIPInputStream;
import javax.xml.bind.JAXBContext;
import javax.xml.bind.JAXBException;

/* renamed from: ak.v, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:ak/v.class */
public class C0562v extends W.V {

    /* renamed from: b, reason: collision with root package name */
    String f4901b;

    /* renamed from: h, reason: collision with root package name */
    int f4905h;

    /* renamed from: i, reason: collision with root package name */
    int f4906i;

    /* renamed from: k, reason: collision with root package name */
    float f4908k;

    /* renamed from: a, reason: collision with root package name */
    byte[] f4900a = null;

    /* renamed from: e, reason: collision with root package name */
    StringBuilder f4902e = new StringBuilder();

    /* renamed from: f, reason: collision with root package name */
    float[] f4903f = null;

    /* renamed from: g, reason: collision with root package name */
    int f4904g = 0;

    /* renamed from: j, reason: collision with root package name */
    float f4907j = 0.0f;

    /* renamed from: l, reason: collision with root package name */
    HashMap f4909l = new HashMap();

    /* renamed from: m, reason: collision with root package name */
    List f4910m = new ArrayList();

    /* renamed from: n, reason: collision with root package name */
    List f4911n = new ArrayList();

    @Override // W.V
    public String i() {
        return W.X.f2001Q;
    }

    @Override // W.V
    public boolean a(String str) {
        String str2;
        this.f4901b = str;
        AutoCloseable autoCloseable = null;
        try {
            try {
                GZIPInputStream gZIPInputStream = new GZIPInputStream(new FileInputStream(this.f4901b));
                ByteArrayOutputStream byteArrayOutputStream = new ByteArrayOutputStream();
                byte[] bArr = new byte[1024];
                while (true) {
                    int i2 = gZIPInputStream.read(bArr);
                    if (i2 == -1) {
                        break;
                    }
                    byteArrayOutputStream.write(bArr, 0, i2);
                }
                byteArrayOutputStream.flush();
                this.f4900a = byteArrayOutputStream.toByteArray();
                int iA = C0995c.a(this.f4900a, 0, 4, false, false);
                int iA2 = C0995c.a(this.f4900a, 4, 4, false, false);
                this.f4905h = C0995c.a(this.f4900a, 8, 4, false, false);
                this.f4902e.append("FileName: ").append(str).append("\nVersion: ").append(iA2).append("\nNumRecords: ").append(this.f4905h);
                switch (iA) {
                    case 541089920:
                        str2 = "./inc/emu_v1_208.xml";
                        this.f4906i = 256;
                        this.f4908k = 0.05f;
                        break;
                    case 574906498:
                        str2 = "./inc/emu_v2_076.xml";
                        this.f4906i = DLSModulator.CONN_DST_VIB_FREQUENCY;
                        this.f4908k = 0.04f;
                        break;
                    default:
                        throw new V.a("Invalid file header");
                }
                if (this.f4900a.length < 12 + (this.f4905h * this.f4906i)) {
                    throw new V.a("Failed to open " + str + " - Log file is incomplete");
                }
                b(str2);
                if (gZIPInputStream != null) {
                    try {
                        gZIPInputStream.close();
                    } catch (IOException e2) {
                        Logger.getLogger(C0562v.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                }
                return true;
            } catch (IOException e3) {
                throw new V.a("Failed to open " + str, e3);
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    autoCloseable.close();
                } catch (IOException e4) {
                    Logger.getLogger(C0562v.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                }
            }
            throw th;
        }
    }

    private void b(String str) throws NumberFormatException {
        this.f4910m.add(new C0566z(this));
        File file = new File(str);
        if (file.isFile() && file.exists()) {
            try {
                C0565y c0565y = (C0565y) JAXBContext.newInstance(C0565y.class).createUnmarshaller().unmarshal(file);
                int i2 = 0;
                this.f4902e.append("\nLogging fields: ");
                Iterator it = c0565y.f4918a.f4917a.f4590a.iterator();
                while (it.hasNext()) {
                    C0525C c0525c = (C0525C) it.next();
                    if (!"Debug".equals(c0525c.f4585b)) {
                        C0543c c0543c = new C0543c(c0525c.f4584a, c0525c.f4586c);
                        if (c0525c.f4588e.startsWith("paramList:")) {
                            String strSubstring = c0525c.f4588e.substring("paramList:".length());
                            ArrayList arrayList = new ArrayList();
                            Iterator it2 = c0565y.f4918a.f4917a.f4591b.iterator();
                            while (true) {
                                if (!it2.hasNext()) {
                                    break;
                                }
                                C0524B c0524b = (C0524B) it2.next();
                                if (c0524b.f4582a.equals(strSubstring)) {
                                    int i3 = 0;
                                    Iterator it3 = c0524b.f4583b.iterator();
                                    while (it3.hasNext()) {
                                        C0523A c0523a = (C0523A) it3.next();
                                        int i4 = Integer.parseInt(c0523a.f4581b);
                                        while (i3 < i4) {
                                            arrayList.add("UNDEFINED");
                                            i3++;
                                        }
                                        arrayList.add(c0523a.f4580a);
                                        i3++;
                                    }
                                }
                            }
                            c0543c.b(255);
                            c0543c.a(new C0186l(arrayList));
                        }
                        this.f4910m.add(c0543c);
                        this.f4911n.add(new C0563w(c0525c, i2));
                        this.f4902e.append("\n").append(c0525c.f4584a).append("[").append(c0525c.f4586c).append("]");
                    }
                    i2 += c0525c.f4587d.endsWith(SchemaSymbols.ATTVAL_BYTE) ? 1 : 2;
                }
            } catch (JAXBException e2) {
                Logger.getLogger(C0562v.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        this.f4903f = new float[this.f4911n.size() + 1];
    }

    @Override // W.V
    public void a() {
        this.f4900a = null;
    }

    @Override // W.V
    public Iterator b() {
        return this.f4910m.iterator();
    }

    @Override // W.V
    public float[] c() {
        this.f4903f[0] = this.f4907j;
        int i2 = 1;
        Iterator it = this.f4911n.iterator();
        while (it.hasNext()) {
            this.f4903f[i2] = ((C0563w) it.next()).f4915d * C0995c.a(this.f4900a, 12 + (this.f4904g * this.f4906i) + r0.f4913b, r0.f4914c, false, false);
            i2++;
        }
        this.f4907j += this.f4908k;
        this.f4904g++;
        return this.f4903f;
    }

    @Override // W.V
    public long d() {
        return this.f4905h;
    }

    @Override // W.V
    public boolean e() {
        return this.f4904g < this.f4905h;
    }

    @Override // W.V
    public boolean f() {
        return false;
    }

    @Override // W.V
    public HashMap g() {
        return this.f4909l;
    }

    @Override // W.V
    public String h() {
        return this.f4902e.toString();
    }
}
