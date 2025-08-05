package sun.security.jgss.wrapper;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.Provider;
import java.util.HashMap;
import org.ietf.jgss.Oid;
import sun.security.action.PutAllAction;

/* loaded from: rt.jar:sun/security/jgss/wrapper/SunNativeProvider.class */
public final class SunNativeProvider extends Provider {
    private static final long serialVersionUID = -238911724858694204L;
    private static final String NAME = "SunNativeGSS";
    private static final String INFO = "Sun Native GSS provider";
    private static final String MF_CLASS = "sun.security.jgss.wrapper.NativeGSSFactory";
    private static final String LIB_PROP = "sun.security.jgss.lib";
    private static final String DEBUG_PROP = "sun.security.nativegss.debug";
    static boolean DEBUG;
    static final Provider INSTANCE = new SunNativeProvider();
    private static HashMap<String, String> MECH_MAP = (HashMap) AccessController.doPrivileged(new PrivilegedAction<HashMap<String, String>>() { // from class: sun.security.jgss.wrapper.SunNativeProvider.1
        /* JADX WARN: Can't rename method to resolve collision */
        @Override // java.security.PrivilegedAction
        /* renamed from: run */
        public HashMap<String, String> run2() {
            SunNativeProvider.DEBUG = Boolean.parseBoolean(System.getProperty(SunNativeProvider.DEBUG_PROP));
            try {
                System.loadLibrary("j2gss");
                String[] strArr = new String[0];
                String property = System.getProperty(SunNativeProvider.LIB_PROP);
                if (property == null || property.trim().equals("")) {
                    String property2 = System.getProperty("os.name");
                    if (property2.startsWith("SunOS")) {
                        strArr = new String[]{"libgss.so"};
                    } else if (property2.startsWith("Linux")) {
                        strArr = new String[]{"libgssapi.so", "libgssapi_krb5.so", "libgssapi_krb5.so.2"};
                    } else if (property2.contains("OS X")) {
                        strArr = new String[]{"libgssapi_krb5.dylib", "/usr/lib/sasl2/libgssapiv2.2.so"};
                    }
                } else {
                    strArr = new String[]{property};
                }
                for (String str : strArr) {
                    if (GSSLibStub.init(str, SunNativeProvider.DEBUG)) {
                        SunNativeProvider.debug("Loaded GSS library: " + str);
                        Oid[] oidArrIndicateMechs = GSSLibStub.indicateMechs();
                        HashMap<String, String> map = new HashMap<>();
                        for (int i2 = 0; i2 < oidArrIndicateMechs.length; i2++) {
                            SunNativeProvider.debug("Native MF for " + ((Object) oidArrIndicateMechs[i2]));
                            map.put("GssApiMechanism." + ((Object) oidArrIndicateMechs[i2]), SunNativeProvider.MF_CLASS);
                        }
                        return map;
                    }
                }
                return null;
            } catch (Error e2) {
                SunNativeProvider.debug("No j2gss library found!");
                if (SunNativeProvider.DEBUG) {
                    e2.printStackTrace();
                    return null;
                }
                return null;
            }
        }
    });

    static void debug(String str) {
        if (DEBUG) {
            if (str == null) {
                throw new NullPointerException();
            }
            System.out.println("SunNativeGSS: " + str);
        }
    }

    public SunNativeProvider() {
        super(NAME, 1.8d, INFO);
        if (MECH_MAP != null) {
            AccessController.doPrivileged(new PutAllAction(this, MECH_MAP));
        }
    }
}
