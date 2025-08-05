package javax.rmi;

import java.io.File;
import java.io.FileInputStream;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Properties;

/* compiled from: PortableRemoteObject.java */
/* loaded from: rt.jar:javax/rmi/GetORBPropertiesFileAction.class */
class GetORBPropertiesFileAction implements PrivilegedAction {
    private boolean debug = false;

    private String getSystemProperty(final String str) {
        return (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: javax.rmi.GetORBPropertiesFileAction.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return System.getProperty(str);
            }
        });
    }

    private void getPropertiesFromFile(Properties properties, String str) {
        try {
            File file = new File(str);
            if (!file.exists()) {
                return;
            }
            FileInputStream fileInputStream = new FileInputStream(file);
            try {
                properties.load(fileInputStream);
                fileInputStream.close();
            } catch (Throwable th) {
                fileInputStream.close();
                throw th;
            }
        } catch (Exception e2) {
            if (this.debug) {
                System.out.println("ORB properties file " + str + " not found: " + ((Object) e2));
            }
        }
    }

    @Override // java.security.PrivilegedAction
    /* renamed from: run */
    public Object run2() {
        Properties properties = new Properties();
        getPropertiesFromFile(properties, getSystemProperty("java.home") + File.separator + "lib" + File.separator + "orb.properties");
        Properties properties2 = new Properties(properties);
        getPropertiesFromFile(properties2, getSystemProperty("user.home") + File.separator + "orb.properties");
        return properties2;
    }
}
