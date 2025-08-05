package com.sun.webkit;

import java.io.IOException;
import java.io.InputStreamReader;
import java.net.URL;
import java.util.Locale;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: jfxrt.jar:com/sun/webkit/LocalizedStrings.class */
final class LocalizedStrings {
    private static final Logger log = Logger.getLogger(LocalizedStrings.class.getName());
    private static final ResourceBundle BUNDLE = ResourceBundle.getBundle("com.sun.webkit.LocalizedStrings", Locale.getDefault(), new EncodingResourceBundleControl("utf-8"));

    private LocalizedStrings() {
    }

    private static String getLocalizedProperty(String propName) {
        log.log(Level.FINE, "Get property: " + propName);
        String propValue = BUNDLE.getString(propName);
        if (propValue != null && propValue.trim().length() > 0) {
            log.log(Level.FINE, "Property value: " + propValue);
            return propValue.trim();
        }
        log.log(Level.FINE, "Unknown property value");
        return null;
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/LocalizedStrings$EncodingResourceBundleControl.class */
    private static final class EncodingResourceBundleControl extends ResourceBundle.Control {
        private final String encoding;

        private EncodingResourceBundleControl(String encoding) {
            this.encoding = encoding;
        }

        @Override // java.util.ResourceBundle.Control
        public ResourceBundle newBundle(String baseName, Locale locale, String format, ClassLoader loader, boolean reload) throws IllegalAccessException, InstantiationException, IOException {
            String bundleName = toBundleName(baseName, locale);
            String resourceName = toResourceName(bundleName, "properties");
            URL resourceURL = loader.getResource(resourceName);
            if (resourceURL != null) {
                try {
                    return new PropertyResourceBundle(new InputStreamReader(resourceURL.openStream(), this.encoding));
                } catch (Exception z2) {
                    LocalizedStrings.log.log(Level.FINE, "exception thrown during bundle initialization", (Throwable) z2);
                }
            }
            return super.newBundle(baseName, locale, format, loader, reload);
        }
    }
}
