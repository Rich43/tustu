package sun.security.krb5;

import java.io.IOException;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Collection;
import java.util.Hashtable;
import java.util.Iterator;
import java.util.Vector;
import sun.security.krb5.internal.Krb5;

/* loaded from: rt.jar:sun/security/krb5/SCDynamicStoreConfig.class */
public class SCDynamicStoreConfig {
    private static boolean DEBUG = Krb5.DEBUG;

    private static native void installNotificationCallback();

    private static native Hashtable<String, Object> getKerberosConfig();

    static {
        if (((Boolean) AccessController.doPrivileged(new PrivilegedAction<Boolean>() { // from class: sun.security.krb5.SCDynamicStoreConfig.1
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Boolean run2() {
                if (System.getProperty("os.name").contains("OS X")) {
                    System.loadLibrary("osx");
                    return true;
                }
                return false;
            }
        })).booleanValue()) {
            installNotificationCallback();
        }
    }

    private static Vector<String> unwrapHost(Collection<Hashtable<String, String>> collection) {
        Vector<String> vector = new Vector<>();
        Iterator<Hashtable<String, String>> it = collection.iterator();
        while (it.hasNext()) {
            vector.add(it.next().get("host"));
        }
        return vector;
    }

    private static Hashtable<String, Object> convertRealmConfigs(Hashtable<String, ?> hashtable) {
        Hashtable<String, Object> hashtable2 = new Hashtable<>();
        for (String str : hashtable.keySet()) {
            Hashtable hashtable3 = (Hashtable) hashtable.get(str);
            Hashtable hashtable4 = new Hashtable();
            Collection collection = (Collection) hashtable3.get("kdc");
            if (collection != null) {
                hashtable4.put("kdc", unwrapHost(collection));
            }
            Collection collection2 = (Collection) hashtable3.get("kadmin");
            if (collection2 != null) {
                hashtable4.put("admin_server", unwrapHost(collection2));
            }
            hashtable2.put(str, hashtable4);
        }
        return hashtable2;
    }

    public static Hashtable<String, Object> getConfig() throws IOException {
        Hashtable<String, Object> kerberosConfig = getKerberosConfig();
        if (kerberosConfig == null) {
            throw new IOException("Could not load configuration from SCDynamicStore");
        }
        if (DEBUG) {
            System.out.println("Raw map from JNI: " + ((Object) kerberosConfig));
        }
        return convertNativeConfig(kerberosConfig);
    }

    private static Hashtable<String, Object> convertNativeConfig(Hashtable<String, Object> hashtable) {
        Hashtable hashtable2 = (Hashtable) hashtable.get("realms");
        if (hashtable2 != null) {
            hashtable.remove("realms");
            hashtable.put("realms", convertRealmConfigs(hashtable2));
        }
        WrapAllStringInVector(hashtable);
        if (DEBUG) {
            System.out.println("stanzaTable : " + ((Object) hashtable));
        }
        return hashtable;
    }

    private static void WrapAllStringInVector(Hashtable<String, Object> hashtable) {
        for (String str : hashtable.keySet()) {
            Object obj = hashtable.get(str);
            if (obj instanceof Hashtable) {
                WrapAllStringInVector((Hashtable) obj);
            } else if (obj instanceof String) {
                Vector vector = new Vector();
                vector.add((String) obj);
                hashtable.put(str, vector);
            }
        }
    }
}
