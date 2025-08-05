package W;

import java.io.BufferedInputStream;
import java.io.BufferedOutputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.util.Date;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: TunerStudioMS.jar:W/ao.class */
public class ao implements am {

    /* renamed from: b, reason: collision with root package name */
    private Properties f2095b;

    /* renamed from: a, reason: collision with root package name */
    ar f2096a;

    /* renamed from: c, reason: collision with root package name */
    private File f2097c;

    /* renamed from: d, reason: collision with root package name */
    private File f2098d;

    public ao(aE.a aVar) {
        this.f2095b = null;
        this.f2096a = new ar(aVar, "PersistedAccumulatedValue");
        this.f2097c = aVar.g();
        this.f2098d = new File(this.f2097c.getParentFile(), this.f2097c.getName() + ".bak");
        this.f2095b = new Properties();
        if (this.f2097c.exists()) {
            try {
                this.f2095b.load(new BufferedInputStream(new FileInputStream(this.f2097c)));
            } catch (IOException e2) {
                bH.C.a("Failed to load Persisted Channel Properties: " + this.f2097c.getAbsolutePath());
                e2.printStackTrace();
            }
        }
    }

    private boolean a(int i2) {
        try {
            Thread.sleep(i2);
            return true;
        } catch (InterruptedException e2) {
            Logger.getLogger(ao.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            return false;
        }
    }

    @Override // W.am
    public void a() {
        if (this.f2097c.exists() && this.f2097c.length() > 0) {
            try {
                if (this.f2098d.exists() && !this.f2098d.delete()) {
                    bH.C.b("Failed to delete PersistedChannel Back up File: " + this.f2098d.getAbsolutePath());
                } else if (!a(50) || !this.f2097c.renameTo(this.f2098d)) {
                    bH.C.b("Failed to rename PersistedChannel File: " + this.f2097c.getAbsolutePath() + " to: " + this.f2098d.getAbsolutePath());
                }
            } catch (Exception e2) {
                Logger.getLogger(ao.class.getName()).log(Level.WARNING, "Failed to create PersistedChannel Backup file", (Throwable) e2);
            }
        }
        if (this.f2097c.exists()) {
            return;
        }
        BufferedOutputStream bufferedOutputStream = null;
        try {
            try {
                if (this.f2097c.createNewFile()) {
                    bufferedOutputStream = new BufferedOutputStream(new FileOutputStream(this.f2097c));
                    this.f2095b.store(bufferedOutputStream, "Persisted Channels storage file  by EFI Analytics, Inc\n#Last Saved on: " + new Date().toString());
                } else {
                    bH.C.b("Failed to create PersistedChannel File: " + this.f2097c.getAbsolutePath());
                }
                try {
                    bufferedOutputStream.close();
                } catch (IOException e3) {
                    Logger.getLogger(ao.class.getName()).log(Level.SEVERE, "Failed to close file: " + this.f2097c.getAbsolutePath(), (Throwable) e3);
                }
            } catch (FileNotFoundException e4) {
                Logger.getLogger(ao.class.getName()).log(Level.SEVERE, "Failed to save PersistedChannel File, File not Found", (Throwable) e4);
                try {
                    bufferedOutputStream.close();
                } catch (IOException e5) {
                    Logger.getLogger(ao.class.getName()).log(Level.SEVERE, "Failed to close file: " + this.f2097c.getAbsolutePath(), (Throwable) e5);
                }
            } catch (IOException e6) {
                Logger.getLogger(ao.class.getName()).log(Level.SEVERE, "Failed to save PersistedChannel File", (Throwable) e6);
                try {
                    bufferedOutputStream.close();
                } catch (IOException e7) {
                    Logger.getLogger(ao.class.getName()).log(Level.SEVERE, "Failed to close file: " + this.f2097c.getAbsolutePath(), (Throwable) e7);
                }
            }
        } catch (Throwable th) {
            try {
                bufferedOutputStream.close();
            } catch (IOException e8) {
                Logger.getLogger(ao.class.getName()).log(Level.SEVERE, "Failed to close file: " + this.f2097c.getAbsolutePath(), (Throwable) e8);
            }
            throw th;
        }
    }

    @Override // W.ap
    public void a(String str, String str2) {
        if (str2 == null || str2.isEmpty()) {
            this.f2095b.remove(str);
        } else {
            this.f2095b.setProperty(str, str2);
        }
    }

    @Override // W.ap
    public String b(String str, String str2) {
        String property = this.f2095b.getProperty(str);
        if (property == null) {
            property = this.f2096a.b(str, null);
            if (property != null) {
                a(str, property);
            }
        }
        if (property == null) {
            property = str2;
        }
        return property;
    }
}
