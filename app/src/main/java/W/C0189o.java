package W;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.File;
import java.io.FileReader;
import java.io.FileWriter;
import java.io.IOException;
import java.io.Writer;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.Scanner;
import javafx.fxml.FXMLLoader;
import org.icepdf.core.util.PdfOps;

/* renamed from: W.o, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:W/o.class */
public class C0189o {

    /* renamed from: g, reason: collision with root package name */
    private File f2175g;

    /* renamed from: a, reason: collision with root package name */
    Writer f2176a;

    /* renamed from: d, reason: collision with root package name */
    static String f2180d = "[FooterData]";

    /* renamed from: b, reason: collision with root package name */
    int f2177b = 0;

    /* renamed from: h, reason: collision with root package name */
    private String f2178h = "\t";

    /* renamed from: c, reason: collision with root package name */
    ArrayList f2179c = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    boolean f2181e = false;

    /* renamed from: f, reason: collision with root package name */
    String f2182f = "";

    private C0189o(String str) {
        this.f2175g = null;
        this.f2176a = null;
        this.f2175g = new File(str);
        this.f2176a = new BufferedWriter(new FileWriter(this.f2175g));
    }

    public static C0189o a(C0188n c0188n, String str, String str2) throws IOException {
        C0189o c0189o = new C0189o(str);
        c0189o.a(str2);
        c0189o.c(c0188n);
        return c0189o;
    }

    public static void b(C0188n c0188n, String str, String str2) {
        C0189o c0189o = null;
        BufferedReader bufferedReader = null;
        try {
            if (str.toLowerCase().endsWith(".mlg")) {
                throw new IOException("Attempting to update LogFooter of a binary log as a delimited ASCII log");
            }
            try {
                File file = new File(str + ".tmp");
                File file2 = new File(str);
                BufferedReader bufferedReader2 = new BufferedReader(new FileReader(file2));
                C0189o c0189o2 = new C0189o(file.getAbsolutePath());
                c0189o2.a(str2);
                while (true) {
                    String line = bufferedReader2.readLine();
                    if (line == null || line.startsWith(f2180d)) {
                        break;
                    }
                    c0189o2.f2176a.write(line);
                    c0189o2.f2176a.write(10);
                }
                c0189o2.b(c0188n);
                try {
                    bufferedReader2.close();
                } catch (Exception e2) {
                    e2.printStackTrace();
                }
                try {
                    c0189o2.a();
                } catch (Exception e3) {
                    e3.printStackTrace();
                }
                if (!file2.delete()) {
                    throw new IOException("Unable to update file:\n" + file2.getAbsolutePath());
                }
                file.renameTo(file2);
                try {
                    c0189o2.a();
                } catch (Exception e4) {
                }
                try {
                    bufferedReader2.close();
                } catch (Exception e5) {
                }
            } catch (IOException e6) {
                throw e6;
            }
        } catch (Throwable th) {
            try {
                c0189o.a();
            } catch (Exception e7) {
            }
            try {
                bufferedReader.close();
            } catch (Exception e8) {
            }
            throw th;
        }
    }

    private void c(C0188n c0188n) throws IOException {
        boolean z2 = false;
        Scanner scanner = new Scanner(c0188n.g());
        while (scanner.hasNextLine()) {
            String strNextLine = scanner.nextLine();
            if (z2) {
                if (strNextLine.startsWith("NEW_INFO_PROVIDER") && strNextLine.contains("[FooterData]")) {
                    while (scanner.hasNextLine()) {
                        this.f2182f += scanner.nextLine() + "\n";
                    }
                }
            } else if (strNextLine.startsWith("NEW_INFO_PROVIDER") && !strNextLine.contains("[FooterData]")) {
                z2 = true;
            } else if (strNextLine.startsWith(PdfOps.DOUBLE_QUOTE__TOKEN) || strNextLine.startsWith(FXMLLoader.CONTROLLER_METHOD_PREFIX)) {
                this.f2176a.write(strNextLine + "\n");
            } else {
                this.f2176a.write(FXMLLoader.CONTROLLER_METHOD_PREFIX + strNextLine + "\n");
            }
        }
        boolean zE = c0188n.e();
        for (int i2 = 0; i2 < c0188n.size(); i2++) {
            this.f2176a.write(((C0184j) c0188n.get(i2)).a());
            if (i2 < c0188n.size() - 1) {
                this.f2176a.write(this.f2178h);
            }
        }
        this.f2176a.write("\n");
        if (zE) {
            for (int i3 = 0; i3 < c0188n.size(); i3++) {
                C0184j c0184j = (C0184j) c0188n.get(i3);
                if (c0184j.n() != null) {
                    this.f2176a.write(c0184j.n());
                }
                if (i3 < c0188n.size() - 1) {
                    this.f2176a.write(this.f2178h);
                }
            }
            this.f2176a.write("\n");
        }
    }

    public void a(aq aqVar) {
        this.f2179c.add(aqVar);
    }

    private void b() {
        Iterator it = this.f2179c.iterator();
        while (it.hasNext()) {
            ((aq) it.next()).a();
        }
    }

    private void a(double d2) {
        Iterator it = this.f2179c.iterator();
        while (it.hasNext()) {
            ((aq) it.next()).a(d2);
        }
    }

    public void a(C0188n c0188n) throws IOException {
        a(c0188n, 0, c0188n.d());
    }

    public void a(C0188n c0188n, int i2, int i3) throws IOException {
        int iD = c0188n.d();
        for (int i4 = 0; i4 < iD; i4++) {
            for (int i5 = 0; i5 < c0188n.size(); i5++) {
                this.f2176a.write(((C0184j) c0188n.get(i5)).b(i4) + "");
                if (i5 < c0188n.size() - 1) {
                    this.f2176a.write(this.f2178h);
                }
            }
            this.f2176a.write("\n");
            if (i4 % 200 == 0 || i4 == iD - 1) {
                a(i4 / iD);
            }
        }
        Writer writer = this.f2176a;
        StringBuilder sbAppend = new StringBuilder().append("MARK ");
        StringBuilder sbAppend2 = new StringBuilder().append("");
        int i6 = this.f2177b;
        this.f2177b = i6 + 1;
        writer.write(sbAppend.append(bH.W.a(sbAppend2.append(i6).toString(), '0', 3)).append("\n").toString());
        this.f2176a.flush();
    }

    public void b(C0188n c0188n) throws IOException {
        if (c0188n.h()) {
            this.f2176a.append((CharSequence) f2180d).append((CharSequence) "\n");
            for (String str : c0188n.i()) {
                this.f2176a.append((CharSequence) str).append('=').append((CharSequence) URLEncoder.encode(c0188n.f(str), "UTF-8")).append('\n');
            }
            this.f2176a.flush();
        }
    }

    public void a() {
        try {
            this.f2176a.flush();
            this.f2176a.close();
        } finally {
            b();
        }
    }

    public void a(String str) {
        this.f2178h = str;
    }
}
