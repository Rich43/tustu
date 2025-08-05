package java.util.prefs;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.security.AccessController;
import java.security.AllPermission;
import java.security.Permission;
import java.security.PrivilegedAction;
import java.util.Iterator;
import java.util.ServiceConfigurationError;
import java.util.ServiceLoader;

/* loaded from: rt.jar:java/util/prefs/Preferences.class */
public abstract class Preferences {
    public static final int MAX_KEY_LENGTH = 80;
    public static final int MAX_VALUE_LENGTH = 8192;
    public static final int MAX_NAME_LENGTH = 80;
    private static final PreferencesFactory factory = factory();
    private static Permission prefsPerm = new RuntimePermission("preferences");

    public abstract void put(String str, String str2);

    public abstract String get(String str, String str2);

    public abstract void remove(String str);

    public abstract void clear() throws BackingStoreException;

    public abstract void putInt(String str, int i2);

    public abstract int getInt(String str, int i2);

    public abstract void putLong(String str, long j2);

    public abstract long getLong(String str, long j2);

    public abstract void putBoolean(String str, boolean z2);

    public abstract boolean getBoolean(String str, boolean z2);

    public abstract void putFloat(String str, float f2);

    public abstract float getFloat(String str, float f2);

    public abstract void putDouble(String str, double d2);

    public abstract double getDouble(String str, double d2);

    public abstract void putByteArray(String str, byte[] bArr);

    public abstract byte[] getByteArray(String str, byte[] bArr);

    public abstract String[] keys() throws BackingStoreException;

    public abstract String[] childrenNames() throws BackingStoreException;

    public abstract Preferences parent();

    public abstract Preferences node(String str);

    public abstract boolean nodeExists(String str) throws BackingStoreException;

    public abstract void removeNode() throws BackingStoreException;

    public abstract String name();

    public abstract String absolutePath();

    public abstract boolean isUserNode();

    public abstract String toString();

    public abstract void flush() throws BackingStoreException;

    public abstract void sync() throws BackingStoreException;

    public abstract void addPreferenceChangeListener(PreferenceChangeListener preferenceChangeListener);

    public abstract void removePreferenceChangeListener(PreferenceChangeListener preferenceChangeListener);

    public abstract void addNodeChangeListener(NodeChangeListener nodeChangeListener);

    public abstract void removeNodeChangeListener(NodeChangeListener nodeChangeListener);

    public abstract void exportNode(OutputStream outputStream) throws IOException, BackingStoreException;

    public abstract void exportSubtree(OutputStream outputStream) throws IOException, BackingStoreException;

    private static PreferencesFactory factory() {
        String str = (String) AccessController.doPrivileged(new PrivilegedAction<String>() { // from class: java.util.prefs.Preferences.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public String run() {
                return System.getProperty("java.util.prefs.PreferencesFactory");
            }
        });
        if (str != null) {
            try {
                return (PreferencesFactory) Class.forName(str, false, ClassLoader.getSystemClassLoader()).newInstance();
            } catch (Exception e2) {
                try {
                    SecurityManager securityManager = System.getSecurityManager();
                    if (securityManager != null) {
                        securityManager.checkPermission(new AllPermission());
                    }
                    return (PreferencesFactory) Class.forName(str, false, Thread.currentThread().getContextClassLoader()).newInstance();
                } catch (Exception e3) {
                    throw new InternalError("Can't instantiate Preferences factory " + str, e3);
                }
            }
        }
        return (PreferencesFactory) AccessController.doPrivileged(new PrivilegedAction<PreferencesFactory>() { // from class: java.util.prefs.Preferences.2
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            public PreferencesFactory run() {
                return Preferences.factory1();
            }
        });
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static PreferencesFactory factory1() {
        String str;
        Iterator it = ServiceLoader.load(PreferencesFactory.class, ClassLoader.getSystemClassLoader()).iterator();
        while (it.hasNext()) {
            try {
                return (PreferencesFactory) it.next();
            } catch (ServiceConfigurationError e2) {
                if (!(e2.getCause() instanceof SecurityException)) {
                    throw e2;
                }
            }
        }
        String property = System.getProperty("os.name");
        if (property.startsWith("Windows")) {
            str = "java.util.prefs.WindowsPreferencesFactory";
        } else if (property.contains("OS X")) {
            str = "java.util.prefs.MacOSXPreferencesFactory";
        } else {
            str = "java.util.prefs.FileSystemPreferencesFactory";
        }
        try {
            return (PreferencesFactory) Class.forName(str, false, Preferences.class.getClassLoader()).newInstance();
        } catch (Exception e3) {
            throw new InternalError("Can't instantiate platform default Preferences factory " + str, e3);
        }
    }

    public static Preferences userNodeForPackage(Class<?> cls) {
        return userRoot().node(nodeName(cls));
    }

    public static Preferences systemNodeForPackage(Class<?> cls) {
        return systemRoot().node(nodeName(cls));
    }

    private static String nodeName(Class<?> cls) {
        if (cls.isArray()) {
            throw new IllegalArgumentException("Arrays have no associated preferences node.");
        }
        String name = cls.getName();
        int iLastIndexOf = name.lastIndexOf(46);
        if (iLastIndexOf < 0) {
            return "/<unnamed>";
        }
        return "/" + name.substring(0, iLastIndexOf).replace('.', '/');
    }

    public static Preferences userRoot() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(prefsPerm);
        }
        return factory.userRoot();
    }

    public static Preferences systemRoot() {
        SecurityManager securityManager = System.getSecurityManager();
        if (securityManager != null) {
            securityManager.checkPermission(prefsPerm);
        }
        return factory.systemRoot();
    }

    protected Preferences() {
    }

    public static void importPreferences(InputStream inputStream) throws IOException, InvalidPreferencesFormatException {
        XmlSupport.importPreferences(inputStream);
    }
}
