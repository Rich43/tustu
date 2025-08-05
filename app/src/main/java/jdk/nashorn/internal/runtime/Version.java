package jdk.nashorn.internal.runtime;

import java.util.MissingResourceException;
import java.util.ResourceBundle;
import javax.swing.plaf.basic.BasicRootPaneUI;

/* loaded from: nashorn.jar:jdk/nashorn/internal/runtime/Version.class */
public final class Version {
    private static final String VERSION_RB_NAME = "jdk.nashorn.internal.runtime.resources.version";
    private static ResourceBundle versionRB;

    private Version() {
    }

    public static String version() {
        return version(BasicRootPaneUI.Actions.RELEASE);
    }

    public static String fullVersion() {
        return version("full");
    }

    private static String version(String key) {
        if (versionRB == null) {
            try {
                versionRB = ResourceBundle.getBundle(VERSION_RB_NAME);
            } catch (MissingResourceException e2) {
                return "version not available";
            }
        }
        try {
            return versionRB.getString(key);
        } catch (MissingResourceException e3) {
            return "version not available";
        }
    }
}
