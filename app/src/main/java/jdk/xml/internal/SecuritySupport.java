package jdk.xml.internal;

import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Properties;

/* loaded from: rt.jar:jdk/xml/internal/SecuritySupport.class */
class SecuritySupport {
    static final Properties cacheProps = new Properties();
    static volatile boolean firstTime = true;

    private SecuritySupport() {
    }

    public static String getSystemProperty(final String propName) {
        return (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: jdk.xml.internal.SecuritySupport.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public String run2() {
                return System.getProperty(propName);
            }
        });
    }

    public static <T> T getJAXPSystemProperty(Class<T> type, String propName, String defValue) {
        String value = getJAXPSystemProperty(propName);
        if (value == null) {
            value = defValue;
        }
        if (Integer.class.isAssignableFrom(type)) {
            return type.cast(Integer.valueOf(Integer.parseInt(value)));
        }
        if (Boolean.class.isAssignableFrom(type)) {
            return type.cast(Boolean.valueOf(Boolean.parseBoolean(value)));
        }
        return type.cast(value);
    }

    public static String getJAXPSystemProperty(String propName) {
        String value = getSystemProperty(propName);
        if (value == null) {
            value = readJAXPProperty(propName);
        }
        return value;
    }

    public static String readJAXPProperty(String propName) {
        String value = null;
        InputStream is = null;
        try {
            if (firstTime) {
                synchronized (cacheProps) {
                    if (firstTime) {
                        String configFile = getSystemProperty("java.home") + File.separator + "lib" + File.separator + "jaxp.properties";
                        File f2 = new File(configFile);
                        if (getFileExists(f2)) {
                            is = getFileInputStream(f2);
                            cacheProps.load(is);
                        }
                        firstTime = false;
                    }
                }
            }
            value = cacheProps.getProperty(propName);
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e2) {
                }
            }
        } catch (IOException e3) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e4) {
                }
            }
        } catch (Throwable th) {
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e5) {
                }
            }
            throw th;
        }
        return value;
    }

    static boolean getFileExists(final File f2) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: jdk.xml.internal.SecuritySupport.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                return f2.exists() ? Boolean.TRUE : Boolean.FALSE;
            }
        })).booleanValue();
    }

    static FileInputStream getFileInputStream(final File file) throws FileNotFoundException {
        try {
            return (FileInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction<FileInputStream>() { // from class: jdk.xml.internal.SecuritySupport.3
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedExceptionAction
                public FileInputStream run() throws Exception {
                    return new FileInputStream(file);
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((FileNotFoundException) e2.getException());
        }
    }
}
