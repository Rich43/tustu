package bH;

import W.InterfaceC0191q;
import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.BufferedReader;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.MalformedURLException;
import java.net.URL;
import java.net.URLConnection;
import java.util.logging.Level;
import java.util.logging.Logger;

/* renamed from: bH.m, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:bH/m.class */
public class C1005m {

    /* renamed from: a, reason: collision with root package name */
    static boolean f7054a = false;

    /* renamed from: b, reason: collision with root package name */
    static long f7055b = 0;

    private C1005m() {
    }

    public static String a(String str) {
        String str2 = "";
        try {
            URLConnection uRLConnectionOpenConnection = new URL(str).openConnection();
            uRLConnectionOpenConnection.setReadTimeout(12000);
            BufferedReader bufferedReader = new BufferedReader(new InputStreamReader(uRLConnectionOpenConnection.getInputStream()));
            while (true) {
                String line = bufferedReader.readLine();
                if (line == null) {
                    bufferedReader.close();
                    return str2;
                }
                str2 = str2 + line;
            }
        } catch (MalformedURLException e2) {
            System.out.println("Invalid URL: " + str);
            throw new IOException("Invalid URL");
        } catch (IOException e3) {
            throw new IOException("Unable to communicate with server.");
        }
    }

    public static void a(String str, String str2) {
        a(str, str2, null);
    }

    /* JADX WARN: Finally extract failed */
    public static void a(String str, String str2, InterfaceC0191q interfaceC0191q) {
        String str3 = str2.lastIndexOf(".") != -1 ? str2.substring(0, str2.lastIndexOf(".") + 1) + "temp" : str2 + ".temp";
        try {
            str = W.b(str, " ", "%20");
            URLConnection uRLConnectionOpenConnection = new URL(str).openConnection();
            BufferedInputStream bufferedInputStream = new BufferedInputStream(uRLConnectionOpenConnection.getInputStream());
            long contentLength = uRLConnectionOpenConnection.getContentLength();
            File file = new File(str3);
            if (file.exists()) {
                file.delete();
            }
            File file2 = new File(str3);
            BufferedOutputStream bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(file2));
            if (interfaceC0191q != null) {
                try {
                    interfaceC0191q.started(contentLength);
                } catch (Throwable th) {
                    bufferedOutputStream.flush();
                    bufferedOutputStream.close();
                    bufferedInputStream.close();
                    throw th;
                }
            }
            int i2 = 0;
            while (true) {
                int i3 = bufferedInputStream.read();
                if (i3 == -1) {
                    break;
                }
                bufferedOutputStream.write(i3);
                if (interfaceC0191q != null) {
                    int i4 = i2;
                    i2++;
                    if (i4 % 512 == 0) {
                        interfaceC0191q.updateProgress(i2, i2 / contentLength);
                    }
                }
            }
            if (interfaceC0191q != null) {
                interfaceC0191q.completed();
            }
            bufferedOutputStream.flush();
            bufferedOutputStream.close();
            bufferedInputStream.close();
            File file3 = new File(str2);
            if (file3.exists() && !file3.delete()) {
                System.out.println("Delete " + str2 + " FAILED");
            }
            if (!file2.renameTo(file3)) {
                System.out.println("Rename " + str3 + " to " + str2 + " FAILED, will finish on restart");
            }
        } catch (MalformedURLException e2) {
            System.out.println("Bad URL:\n" + str);
        }
    }

    public static boolean a() {
        V v2 = new V();
        v2.start();
        long jCurrentTimeMillis = System.currentTimeMillis() + 10000;
        while (!v2.a() && jCurrentTimeMillis > System.currentTimeMillis()) {
            try {
                Thread.sleep(30L);
            } catch (InterruptedException e2) {
                Logger.getLogger(C1005m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        f7055b = System.currentTimeMillis();
        f7054a = v2.a();
        return f7054a;
    }

    public static boolean b() {
        V v2 = new V();
        v2.start();
        v2.a(3000);
        if (System.currentTimeMillis() - f7055b > 70000) {
            long jCurrentTimeMillis = System.currentTimeMillis() + 3000;
            while (!v2.c() && jCurrentTimeMillis > System.currentTimeMillis()) {
                try {
                    Thread.sleep(50L);
                } catch (InterruptedException e2) {
                    Logger.getLogger(C1005m.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
            }
            f7055b = System.currentTimeMillis();
            f7054a = v2.a();
        } else {
            new C1006n(v2).start();
        }
        return f7054a;
    }

    public static boolean b(String str) {
        HttpURLConnection httpURLConnection = null;
        try {
            try {
                HttpURLConnection.setFollowRedirects(false);
                httpURLConnection = (HttpURLConnection) new URL(W.b(str, " ", "%20")).openConnection();
                httpURLConnection.setRequestMethod("HEAD");
                boolean z2 = httpURLConnection.getResponseCode() == 200;
                if (httpURLConnection != null) {
                    try {
                        httpURLConnection.disconnect();
                    } catch (Exception e2) {
                    }
                }
                return z2;
            } catch (Exception e3) {
                e3.printStackTrace();
                if (httpURLConnection != null) {
                    try {
                        httpURLConnection.disconnect();
                    } catch (Exception e4) {
                    }
                }
                return false;
            }
        } catch (Throwable th) {
            if (httpURLConnection != null) {
                try {
                    httpURLConnection.disconnect();
                } catch (Exception e5) {
                }
            }
            throw th;
        }
    }
}
