package i;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.logging.Level;
import java.util.logging.Logger;
import r.C1807j;

/* renamed from: i.h, reason: case insensitive filesystem */
/* loaded from: TunerStudioMS.jar:i/h.class */
public class C1748h {

    /* renamed from: a, reason: collision with root package name */
    private static C1748h f12358a = null;

    public static C1748h a() {
        if (f12358a == null) {
            f12358a = new C1748h();
        }
        return f12358a;
    }

    public synchronized boolean a(String str, String str2) {
        C1746f.a().a(true);
        try {
            boolean zB = b(str, str2);
            C1746f.a().a(false);
            return zB;
        } catch (Throwable th) {
            C1746f.a().a(false);
            throw th;
        }
    }

    private synchronized boolean b(String str, String str2) {
        File file = new File(C1807j.z(), "MLV.pipe");
        if (!file.exists()) {
            return false;
        }
        String str3 = str + "=" + str2;
        FileOutputStream fileOutputStream = null;
        long jLastModified = -1;
        try {
            try {
                fileOutputStream = new FileOutputStream(file);
                fileOutputStream.write(str3.getBytes());
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                        jLastModified = file.lastModified();
                    } catch (Exception e2) {
                    }
                }
                if (jLastModified <= 0) {
                    return false;
                }
                long jCurrentTimeMillis = System.currentTimeMillis() + C1746f.f12349a + 10;
                do {
                    try {
                        Thread.sleep(50L);
                    } catch (InterruptedException e3) {
                        Logger.getLogger(C1748h.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    }
                    if (!file.exists() || jLastModified != file.lastModified()) {
                        break;
                    }
                } while (System.currentTimeMillis() < jCurrentTimeMillis);
                if (!file.exists() || jLastModified != file.lastModified()) {
                    return true;
                }
                file.delete();
                return false;
            } catch (FileNotFoundException e4) {
                Logger.getLogger(C1748h.class.getName()).log(Level.WARNING, "MLV Pipe File not found, but it was there..", (Throwable) e4);
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                        file.lastModified();
                    } catch (Exception e5) {
                    }
                }
                return false;
            } catch (IOException e6) {
                Logger.getLogger(C1748h.class.getName()).log(Level.WARNING, "Unable to write to MLV Pipe File", (Throwable) e6);
                if (fileOutputStream != null) {
                    try {
                        fileOutputStream.close();
                        file.lastModified();
                    } catch (Exception e7) {
                    }
                }
                return false;
            }
        } catch (Throwable th) {
            if (fileOutputStream != null) {
                try {
                    fileOutputStream.close();
                    file.lastModified();
                } catch (Exception e8) {
                }
            }
            throw th;
        }
    }
}
