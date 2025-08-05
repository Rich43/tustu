package R;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.IOException;
import java.lang.reflect.AccessibleObject;
import java.lang.reflect.Field;
import java.util.ArrayList;
import java.util.Properties;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.swing.JOptionPane;

/* loaded from: TunerStudioMS.jar:R/g.class */
public class g {

    /* renamed from: e, reason: collision with root package name */
    private static g f1796e = null;

    /* renamed from: a, reason: collision with root package name */
    public static String f1797a = JOptionPane.MESSAGE_TYPE_PROPERTY;

    /* renamed from: b, reason: collision with root package name */
    public static String f1798b = "messageId";

    /* renamed from: c, reason: collision with root package name */
    public static String f1799c = "creationTime";

    /* renamed from: d, reason: collision with root package name */
    boolean f1800d = true;

    private g() {
    }

    public static g a() {
        if (f1796e == null) {
            f1796e = new g();
        }
        return f1796e;
    }

    public File a(l lVar) {
        Properties properties = new Properties();
        Field[] declaredFields = lVar.getClass().getDeclaredFields();
        AccessibleObject.setAccessible(declaredFields, true);
        for (Field field : declaredFields) {
            try {
                properties.setProperty(field.getName(), "" + field.get(lVar));
            } catch (Exception e2) {
            }
        }
        properties.setProperty(f1797a, lVar.a());
        properties.setProperty(f1798b, lVar.k());
        properties.setProperty(f1799c, lVar.l() + "");
        return a(properties);
    }

    public ArrayList b() {
        ArrayList arrayList = new ArrayList();
        for (File file : e()) {
            try {
                arrayList.add(a(file));
            } catch (i e2) {
                Logger.getLogger(g.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
            }
        }
        return arrayList;
    }

    private l a(File file) throws SecurityException, i {
        Properties propertiesB = b(file);
        String property = propertiesB.getProperty(f1797a);
        if (property.equals(l.f1811a)) {
            a aVar = new a();
            a(propertiesB, aVar);
            return aVar;
        }
        if (property.equals(l.f1813c)) {
            j jVar = new j();
            a(propertiesB, jVar);
            return jVar;
        }
        if (property.equals(l.f1812b)) {
            throw new i("Not supported messageType:" + property + "\nin message:\n" + file.getAbsolutePath());
        }
        throw new i("Unknown messageType:" + property + "\nin message:\n" + file.getAbsolutePath());
    }

    private l a(Properties properties, l lVar) throws SecurityException {
        try {
            lVar.a(Long.parseLong(properties.getProperty(f1799c)));
        } catch (Exception e2) {
        }
        Field[] declaredFields = lVar.getClass().getDeclaredFields();
        AccessibleObject.setAccessible(declaredFields, true);
        for (Field field : declaredFields) {
            try {
                if (field.getType() == Integer.TYPE) {
                    field.setInt(lVar, Integer.parseInt(properties.getProperty(field.getName())));
                } else {
                    field.set(lVar, properties.getProperty(field.getName()));
                }
            } catch (Exception e3) {
                e3.printStackTrace();
            }
        }
        return lVar;
    }

    private Properties b(File file) {
        FileInputStream fileInputStream = null;
        try {
            try {
                Properties properties = new Properties();
                fileInputStream = new FileInputStream(file);
                properties.loadFromXML(fileInputStream);
                try {
                    fileInputStream.close();
                } catch (IOException e2) {
                    Logger.getLogger(g.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                }
                return properties;
            } catch (Exception e3) {
                Logger.getLogger(g.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                throw new i("Could not load message:\n" + file.getAbsolutePath() + "\nMessage:" + e3.getMessage());
            }
        } catch (Throwable th) {
            try {
                fileInputStream.close();
            } catch (IOException e4) {
                Logger.getLogger(g.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
            }
            throw th;
        }
    }

    private File[] e() {
        return c().listFiles(new h(this));
    }

    public File c() {
        File file = new File(System.getProperty("user.home") + File.separator + ".efiAnalytics" + File.separator + "messages" + File.separator);
        if (!file.exists()) {
            file.mkdirs();
        }
        return file;
    }

    public boolean a(String str) {
        return new File(c().getAbsolutePath() + File.separator + str + ".svcmsg").delete();
    }

    private File a(Properties properties) {
        FileOutputStream fileOutputStream = null;
        File file = new File(c().getAbsolutePath() + File.separator + properties.getProperty(f1798b) + ".svcmsg");
        try {
            try {
                try {
                    file.createNewFile();
                    fileOutputStream = new FileOutputStream(file);
                    properties.storeToXML(fileOutputStream, "Message Stored for processing when network connectivity is available.");
                    try {
                        fileOutputStream.close();
                    } catch (IOException e2) {
                        Logger.getLogger(g.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e2);
                    }
                    this.f1800d = true;
                    return file;
                } catch (IOException e3) {
                    Logger.getLogger(g.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e3);
                    throw new i("unable to write message to:\n" + file.getAbsolutePath());
                }
            } catch (FileNotFoundException e4) {
                Logger.getLogger(g.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e4);
                throw new i("Create File failed:\n" + file.getAbsolutePath());
            }
        } catch (Throwable th) {
            try {
                fileOutputStream.close();
            } catch (IOException e5) {
                Logger.getLogger(g.class.getName()).log(Level.SEVERE, (String) null, (Throwable) e5);
            }
            throw th;
        }
    }

    boolean d() {
        if (!this.f1800d) {
            return false;
        }
        this.f1800d = e().length > 0;
        return this.f1800d;
    }
}
