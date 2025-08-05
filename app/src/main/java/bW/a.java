package bW;

import bH.C;
import bH.C1011s;
import java.io.BufferedReader;
import java.io.File;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLEncoder;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;

/* loaded from: TunerStudioMS.jar:bW/a.class */
public class a {

    /* renamed from: a, reason: collision with root package name */
    String[] f7643a = {"http://updates.efianalytics.com/register/CheckForUpdates?", "http://updates2.efianalytics.com/register/CheckForUpdates?", "http://updates3.efianalytics.com/register/CheckForUpdates?"};

    /* renamed from: b, reason: collision with root package name */
    long f7644b = 0;

    /* renamed from: c, reason: collision with root package name */
    long f7645c = 0;

    /* renamed from: d, reason: collision with root package name */
    ArrayList f7646d = new ArrayList();

    /* renamed from: e, reason: collision with root package name */
    List f7647e = new ArrayList();

    public d a(String str, String str2, String str3, String str4, String str5, String str6, String str7, String str8) {
        e eVar = new e();
        eVar.a(str);
        eVar.b(str2);
        eVar.c(str3);
        eVar.d(str4);
        eVar.e(str5);
        eVar.f(str6);
        eVar.g(str7);
        eVar.h(str8);
        return a(eVar);
    }

    public d a(e eVar) throws IOException {
        String str = "";
        String strA = a();
        try {
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(new URL(strA + "uid=" + eVar.a() + "&version=" + eVar.c() + "&appName=" + URLEncoder.encode(eVar.b(), "UTF-8") + "&appEdition=" + URLEncoder.encode(eVar.g(), "UTF-8") + "&installDate=" + eVar.d() + "&javaVersion=" + System.getProperty("java.version") + "&os=" + URLEncoder.encode(System.getProperty("os.name"), "UTF-8") + "&arch=" + System.getProperty("os.arch") + "&loopCount=" + eVar.e() + "&regKey=" + URLEncoder.encode(eVar.f(), "UTF-8") + "&firmwareSignature=" + URLEncoder.encode(eVar.h(), "UTF-8") + "&langCode=" + eVar.i()).openConnection().getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    break;
                }
                str = str + line;
            }
            bufferedReader.close();
        } catch (MalformedURLException e2) {
            System.out.println("Invalid URL: " + strA);
        }
        return new d().a(str);
    }

    public String a() {
        return this.f7643a[(int) Math.floor(this.f7643a.length * Math.random())];
    }

    public void a(d dVar) {
        try {
            File file = new File(".", "updateStage");
            if (!file.exists()) {
                File file2 = new File(file, "aaaaaa");
                file2.mkdirs();
                file2.delete();
            }
            a(file, dVar);
            for (File file3 : file.listFiles()) {
                File file4 = new File(".", file3.getName());
                if (!file4.exists() || file4.delete()) {
                    try {
                        C1011s.a(file3, file4);
                        file3.delete();
                    } catch (V.a e2) {
                        C.b("Failed to copy file: " + e2.getLocalizedMessage());
                        try {
                            C1011s.a(file3, new File(".", file3.getName() + ".temp"));
                            file3.delete();
                        } catch (V.a e3) {
                            C.a("Update failed.");
                            throw new IOException(e3.getLocalizedMessage());
                        }
                    }
                } else {
                    C.d("Lock on file: " + file4.getName() + " will leave a temp version to be renamed on restart.");
                    try {
                        C1011s.a(file3, new File(".", file3.getName() + ".temp"));
                        file3.delete();
                    } catch (V.a e4) {
                        C.a("Update failed.");
                        throw new IOException(e4.getLocalizedMessage());
                    }
                }
            }
            file.delete();
        } catch (IOException e5) {
            C.d("Failed on update, cleaning up.. Err: " + e5.getLocalizedMessage());
            for (File file5 : this.f7647e) {
                C.d("Deleteing: " + file5.getName());
                file5.delete();
            }
        }
    }

    public void a(File file, d dVar) throws IOException {
        this.f7644b = b(dVar);
        this.f7645c = 0L;
        Iterator itC = dVar.c();
        while (itC.hasNext()) {
            c cVar = (c) itC.next();
            if (cVar.a().equals("fileUpdate")) {
                String[] strArrB = cVar.b();
                String str = null;
                if (strArrB.length > 2) {
                    str = strArrB[2];
                }
                a(file, strArrB[0], strArrB[1], str);
            }
        }
        c();
    }

    public long b(d dVar) throws IOException {
        long contentLength = 0;
        Iterator itC = dVar.c();
        while (itC.hasNext()) {
            if (((c) itC.next()).a().equals("fileUpdate")) {
                try {
                    contentLength += new URL(r0.b()[1]).openConnection().getContentLength();
                } catch (MalformedURLException e2) {
                    System.out.println("Error getting file size");
                }
            }
        }
        return contentLength;
    }

    public long a(String str) {
        try {
            return new URL(str).openConnection().getContentLength();
        } catch (MalformedURLException e2) {
            System.out.println("Error getting file size");
            return -1L;
        }
    }

    /* JADX WARN: Removed duplicated region for block: B:110:0x0341 A[SYNTHETIC] */
    /* JADX WARN: Removed duplicated region for block: B:63:0x0330 A[Catch: Exception -> 0x0338, TryCatch #1 {Exception -> 0x0338, blocks: (B:60:0x0326, B:63:0x0330), top: B:103:0x0326 }] */
    /* JADX WARN: Removed duplicated region for block: B:75:0x0374  */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public void a(java.io.File r8, java.lang.String r9, java.lang.String r10, java.lang.String r11) throws java.io.IOException {
        /*
            Method dump skipped, instructions count: 1218
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: bW.a.a(java.io.File, java.lang.String, java.lang.String, java.lang.String):void");
    }

    public void a(b bVar) {
        this.f7646d.add(bVar);
    }

    private void b() {
        Iterator it = this.f7646d.iterator();
        while (it.hasNext()) {
            try {
                ((b) it.next()).a(this.f7645c, this.f7644b);
            } catch (Exception e2) {
            }
        }
    }

    private void c() {
        Iterator it = this.f7646d.iterator();
        while (it.hasNext()) {
            try {
                ((b) it.next()).a();
            } catch (Exception e2) {
            }
        }
    }
}
