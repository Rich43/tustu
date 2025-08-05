package com.sun.security.auth.login;

import java.net.URI;
import javax.security.auth.login.AppConfigurationEntry;
import javax.security.auth.login.Configuration;
import jdk.Exported;
import sun.security.provider.ConfigFile;

@Exported
/* loaded from: rt.jar:com/sun/security/auth/login/ConfigFile.class */
public class ConfigFile extends Configuration {
    private final ConfigFile.Spi spi;

    public ConfigFile() {
        this.spi = new ConfigFile.Spi();
    }

    public ConfigFile(URI uri) {
        this.spi = new ConfigFile.Spi(uri);
    }

    @Override // javax.security.auth.login.Configuration
    public AppConfigurationEntry[] getAppConfigurationEntry(String str) {
        return this.spi.engineGetAppConfigurationEntry(str);
    }

    @Override // javax.security.auth.login.Configuration
    public void refresh() {
        this.spi.engineRefresh();
    }
}
