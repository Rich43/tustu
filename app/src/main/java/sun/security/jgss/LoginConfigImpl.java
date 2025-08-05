package sun.security.jgss;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.HashMap;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import org.ietf.jgss.Oid;
import sun.security.action.GetPropertyAction;
import sun.security.util.Debug;

/* loaded from: rt.jar:sun/security/jgss/LoginConfigImpl.class */
public class LoginConfigImpl extends Configuration {
    private final Configuration config;
    private final GSSCaller caller;
    private final String mechName;
    private static final Debug debug = Debug.getInstance("gssloginconfig", "\t[GSS LoginConfigImpl]");
    public static final boolean HTTP_USE_GLOBAL_CREDS;

    static {
        HTTP_USE_GLOBAL_CREDS = !"false".equalsIgnoreCase(GetPropertyAction.privilegedGetProperty("http.use.global.creds"));
    }

    public LoginConfigImpl(GSSCaller gSSCaller, Oid oid) {
        this.caller = gSSCaller;
        if (oid.equals(GSSUtil.GSS_KRB5_MECH_OID)) {
            this.mechName = "krb5";
            this.config = (Configuration) AccessController.doPrivileged(new PrivilegedAction<Configuration>() { // from class: sun.security.jgss.LoginConfigImpl.1
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Configuration run2() {
                    return Configuration.getConfiguration();
                }
            });
            return;
        }
        throw new IllegalArgumentException(oid.toString() + " not supported");
    }

    @Override // javax.security.auth.login.Configuration
    public AppConfigurationEntry[] getAppConfigurationEntry(String str) {
        AppConfigurationEntry[] defaultConfigurationEntry = null;
        if ("OTHER".equalsIgnoreCase(str)) {
            return null;
        }
        String[] strArr = null;
        if ("krb5".equals(this.mechName)) {
            if (this.caller == GSSCaller.CALLER_INITIATE) {
                strArr = new String[]{"com.sun.security.jgss.krb5.initiate", "com.sun.security.jgss.initiate"};
            } else if (this.caller == GSSCaller.CALLER_ACCEPT) {
                strArr = new String[]{"com.sun.security.jgss.krb5.accept", "com.sun.security.jgss.accept"};
            } else if (this.caller == GSSCaller.CALLER_SSL_CLIENT) {
                strArr = new String[]{"com.sun.security.jgss.krb5.initiate", "com.sun.net.ssl.client"};
            } else if (this.caller == GSSCaller.CALLER_SSL_SERVER) {
                strArr = new String[]{"com.sun.security.jgss.krb5.accept", "com.sun.net.ssl.server"};
            } else if (this.caller instanceof HttpCaller) {
                strArr = new String[]{"com.sun.security.jgss.krb5.initiate"};
            } else if (this.caller == GSSCaller.CALLER_UNKNOWN) {
                throw new AssertionError((Object) "caller not defined");
            }
            for (String str2 : strArr) {
                defaultConfigurationEntry = this.config.getAppConfigurationEntry(str2);
                if (debug != null) {
                    debug.println("Trying " + str2 + (defaultConfigurationEntry == null ? ": does not exist." : ": Found!"));
                }
                if (defaultConfigurationEntry != null) {
                    break;
                }
            }
            if (defaultConfigurationEntry == null) {
                if (debug != null) {
                    debug.println("Cannot read JGSS entry, use default values instead.");
                }
                defaultConfigurationEntry = getDefaultConfigurationEntry();
            }
            return defaultConfigurationEntry;
        }
        throw new IllegalArgumentException(this.mechName + " not supported");
    }

    private AppConfigurationEntry[] getDefaultConfigurationEntry() {
        HashMap map = new HashMap(2);
        if (this.mechName == null || this.mechName.equals("krb5")) {
            if (isServerSide(this.caller)) {
                map.put("useKeyTab", "true");
                map.put("storeKey", "true");
                map.put("doNotPrompt", "true");
                map.put("principal", "*");
                map.put("isInitiator", "false");
            } else {
                if ((this.caller instanceof HttpCaller) && !HTTP_USE_GLOBAL_CREDS) {
                    map.put("useTicketCache", "false");
                } else {
                    map.put("useTicketCache", "true");
                }
                map.put("doNotPrompt", "false");
            }
            return new AppConfigurationEntry[]{new AppConfigurationEntry("com.sun.security.auth.module.Krb5LoginModule", AppConfigurationEntry.LoginModuleControlFlag.REQUIRED, map)};
        }
        return null;
    }

    private static boolean isServerSide(GSSCaller gSSCaller) {
        return GSSCaller.CALLER_ACCEPT == gSSCaller || GSSCaller.CALLER_SSL_SERVER == gSSCaller;
    }
}
