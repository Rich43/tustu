package javax.security.auth.login;

import com.sun.org.apache.xerces.internal.impl.xs.SchemaSymbols;
import java.util.Collections;
import java.util.Map;
import sun.security.util.ResourcesMgr;

/* loaded from: rt.jar:javax/security/auth/login/AppConfigurationEntry.class */
public class AppConfigurationEntry {
    private String loginModuleName;
    private LoginModuleControlFlag controlFlag;
    private Map<String, ?> options;

    public AppConfigurationEntry(String str, LoginModuleControlFlag loginModuleControlFlag, Map<String, ?> map) {
        if (str == null || str.length() == 0 || ((loginModuleControlFlag != LoginModuleControlFlag.REQUIRED && loginModuleControlFlag != LoginModuleControlFlag.REQUISITE && loginModuleControlFlag != LoginModuleControlFlag.SUFFICIENT && loginModuleControlFlag != LoginModuleControlFlag.OPTIONAL) || map == null)) {
            throw new IllegalArgumentException();
        }
        this.loginModuleName = str;
        this.controlFlag = loginModuleControlFlag;
        this.options = Collections.unmodifiableMap(map);
    }

    public String getLoginModuleName() {
        return this.loginModuleName;
    }

    public LoginModuleControlFlag getControlFlag() {
        return this.controlFlag;
    }

    public Map<String, ?> getOptions() {
        return this.options;
    }

    /* loaded from: rt.jar:javax/security/auth/login/AppConfigurationEntry$LoginModuleControlFlag.class */
    public static class LoginModuleControlFlag {
        private String controlFlag;
        public static final LoginModuleControlFlag REQUIRED = new LoginModuleControlFlag(SchemaSymbols.ATTVAL_REQUIRED);
        public static final LoginModuleControlFlag REQUISITE = new LoginModuleControlFlag("requisite");
        public static final LoginModuleControlFlag SUFFICIENT = new LoginModuleControlFlag("sufficient");
        public static final LoginModuleControlFlag OPTIONAL = new LoginModuleControlFlag(SchemaSymbols.ATTVAL_OPTIONAL);

        private LoginModuleControlFlag(String str) {
            this.controlFlag = str;
        }

        public String toString() {
            return ResourcesMgr.getString("LoginModuleControlFlag.") + this.controlFlag;
        }
    }
}
