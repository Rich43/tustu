package sun.net;

import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;

/* loaded from: rt.jar:sun/net/NetProperties.class */
public class NetProperties {
    private static Properties props = new Properties();

    static {
        AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: sun.net.NetProperties.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() {
                NetProperties.loadDefaultProperties();
                return null;
            }
        });
    }

    private NetProperties() {
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static void loadDefaultProperties() {
        String property = System.getProperty("java.home");
        if (property == null) {
            throw new Error("Can't find java.home ??");
        }
        try {
            BufferedInputStream bufferedInputStream = new BufferedInputStream(new FileInputStream(new File(new File(property, "lib"), "net.properties").getCanonicalPath()));
            props.load(bufferedInputStream);
            bufferedInputStream.close();
        } catch (Exception e2) {
        }
    }

    public static String get(String str) {
        try {
            return System.getProperty(str, props.getProperty(str));
        } catch (IllegalArgumentException | NullPointerException e2) {
            return null;
        }
    }

    public static Integer getInteger(String str, int i2) {
        String property = null;
        try {
            property = System.getProperty(str, props.getProperty(str));
        } catch (IllegalArgumentException e2) {
        } catch (NullPointerException e3) {
        }
        if (property != null) {
            try {
                return Integer.decode(property);
            } catch (NumberFormatException e4) {
            }
        }
        return new Integer(i2);
    }

    public static Boolean getBoolean(String str) {
        String property = null;
        try {
            property = System.getProperty(str, props.getProperty(str));
        } catch (IllegalArgumentException e2) {
        } catch (NullPointerException e3) {
        }
        if (property != null) {
            try {
                return Boolean.valueOf(property);
            } catch (NumberFormatException e4) {
                return null;
            }
        }
        return null;
    }
}
