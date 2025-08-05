package javax.security.auth.login;

/* loaded from: rt.jar:javax/security/auth/login/ConfigurationSpi.class */
public abstract class ConfigurationSpi {
    protected abstract AppConfigurationEntry[] engineGetAppConfigurationEntry(String str);

    protected void engineRefresh() {
    }
}
