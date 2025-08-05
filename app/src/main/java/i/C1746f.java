package i;

import bH.C;
import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileReader;
import java.io.IOException;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: i.f, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:i/f.class */
public class C1746f {

    /* renamed from: b, reason: collision with root package name */
    File f12350b = new File(h.h.a(), "MLV.pipe");

    /* renamed from: e, reason: collision with root package name */
    private final int f12352e = 86400000;

    /* renamed from: f, reason: collision with root package name */
    private final List f12353f = new ArrayList();

    /* renamed from: c, reason: collision with root package name */
    C1747g f12354c = null;

    /* renamed from: g, reason: collision with root package name */
    private boolean f12355g = false;

    /* renamed from: a, reason: collision with root package name */
    public static int f12349a = 250;

    /* renamed from: d, reason: collision with root package name */
    private static C1746f f12351d = null;

    private C1746f() {
    }

    public static C1746f a() {
        if (f12351d == null) {
            f12351d = new C1746f();
        }
        return f12351d;
    }

    public void b() {
        if (this.f12350b.lastModified() < System.currentTimeMillis() - 86400000) {
            this.f12350b.delete();
        }
        d();
        this.f12354c = new C1747g(this);
        this.f12354c.start();
    }

    public void c() {
        this.f12350b.delete();
        this.f12354c.a();
    }

    public void a(InterfaceC1745e interfaceC1745e) {
        this.f12353f.add(interfaceC1745e);
    }

    private void a(String str, String str2) {
        Iterator it = this.f12353f.iterator();
        while (it.hasNext() && !((InterfaceC1745e) it.next()).a(str, str2)) {
        }
    }

    private void d() {
        if (this.f12350b.exists()) {
            this.f12350b.setLastModified(System.currentTimeMillis());
        } else {
            try {
                this.f12350b.createNewFile();
            } catch (IOException e2) {
                Logger.getLogger(C1746f.class.getName()).log(Level.SEVERE, "Failed to create Pipe", (Throwable) e2);
            }
        }
        this.f12350b.deleteOnExit();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void e() {
        int i2;
        StringBuilder sb;
        String strSubstring;
        StringBuilder fileReader = null;
        try {
            try {
                try {
                    fileReader = new FileReader(this.f12350b);
                    char[] cArr = new char[50];
                    StringBuilder sb2 = null;
                    do {
                        i2 = fileReader.read(cArr);
                        if (i2 > 0) {
                            if (sb2 == null) {
                                sb2 = new StringBuilder();
                            }
                            sb2.append(cArr, 0, i2);
                        }
                    } while (i2 > 0);
                    fileReader.close();
                    if (sb != null && fileReader.length() > 0) {
                        this.f12350b.delete();
                        d();
                        String string = fileReader.toString();
                        String strSubstring2 = null;
                        int iIndexOf = string.indexOf("=");
                        if (iIndexOf != -1) {
                            strSubstring = string.substring(0, iIndexOf);
                            strSubstring2 = string.substring(iIndexOf + 1);
                        } else {
                            strSubstring = string;
                        }
                        a(strSubstring, strSubstring2);
                    }
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (Exception e2) {
                            C.c("error closing Pipe reader");
                        }
                    }
                } catch (FileNotFoundException e3) {
                    d();
                    if (fileReader != null) {
                        try {
                            fileReader.close();
                        } catch (Exception e4) {
                            C.c("error closing Pipe reader");
                        }
                    }
                }
            } catch (IOException e5) {
                Logger.getLogger(C1746f.class.getName()).log(Level.WARNING, "Could not read from pipe file.", (Throwable) e5);
                this.f12350b.delete();
                if (fileReader != null) {
                    try {
                        fileReader.close();
                    } catch (Exception e6) {
                        C.c("error closing Pipe reader");
                    }
                }
            }
        } finally {
            if (fileReader != null) {
                try {
                    fileReader.close();
                } catch (Exception e7) {
                    C.c("error closing Pipe reader");
                }
            }
        }
    }

    public void a(boolean z2) {
        this.f12355g = z2;
    }
}
