package com.sun.javafx.css.converters;

import com.sun.javafx.css.StyleConverterImpl;
import com.sun.javafx.util.Logging;
import com.sun.javafx.util.Utils;
import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.security.AccessController;
import java.security.CodeSource;
import java.security.PrivilegedActionException;
import java.security.ProtectionDomain;
import javafx.application.Application;
import javafx.css.ParsedValue;
import javafx.css.StyleConverter;
import javafx.scene.text.Font;
import sun.util.logging.PlatformLogger;

/* loaded from: jfxrt.jar:com/sun/javafx/css/converters/URLConverter.class */
public final class URLConverter extends StyleConverterImpl<ParsedValue[], String> {
    @Override // javafx.css.StyleConverter
    public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
        return convert((ParsedValue<ParsedValue[], String>) parsedValue, font);
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/URLConverter$Holder.class */
    private static class Holder {
        static final URLConverter INSTANCE = new URLConverter();
        static final SequenceConverter SEQUENCE_INSTANCE = new SequenceConverter();

        private Holder() {
        }
    }

    public static StyleConverter<ParsedValue[], String> getInstance() {
        return Holder.INSTANCE;
    }

    private URLConverter() {
    }

    @Override // javafx.css.StyleConverter
    public String convert(ParsedValue<ParsedValue[], String> value, Font font) {
        String resource;
        String url = null;
        ParsedValue[] values = value.getValue();
        String resource2 = values.length > 0 ? StringConverter.getInstance().convert(values[0], font) : null;
        if (resource2 != null && !resource2.trim().isEmpty()) {
            if (resource2.startsWith("url(")) {
                resource = Utils.stripQuotes(resource2.substring(4, resource2.length() - 1));
            } else {
                resource = Utils.stripQuotes(resource2);
            }
            String stylesheetURL = (values.length <= 1 || values[1] == null) ? null : values[1].getValue();
            URL resolvedURL = resolve(stylesheetURL, resource);
            if (resolvedURL != null) {
                url = resolvedURL.toExternalForm();
            }
        }
        return url;
    }

    URL resolve(String stylesheetUrl, String resource) {
        String resourcePath = resource != null ? resource.trim() : null;
        if (resourcePath == null || resourcePath.isEmpty()) {
            return null;
        }
        try {
            URI resourceUri = new URI(resourcePath);
            if (resourceUri.isAbsolute()) {
                return resourceUri.toURL();
            }
            URL rtJarUrl = resolveRuntimeImport(resourceUri);
            if (rtJarUrl != null) {
                return rtJarUrl;
            }
            String path = resourceUri.getPath();
            if (path.startsWith("/")) {
                ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                return contextClassLoader.getResource(path.substring(1));
            }
            String stylesheetPath = stylesheetUrl != null ? stylesheetUrl.trim() : null;
            if (stylesheetPath != null && !stylesheetPath.isEmpty()) {
                URI stylesheetUri = new URI(stylesheetPath);
                if (!stylesheetUri.isOpaque()) {
                    URI resolved = stylesheetUri.resolve(resourceUri);
                    return resolved.toURL();
                }
                URL url = stylesheetUri.toURL();
                return new URL(url, resourceUri.getPath());
            }
            ClassLoader contextClassLoader2 = Thread.currentThread().getContextClassLoader();
            return contextClassLoader2.getResource(path);
        } catch (MalformedURLException | URISyntaxException e2) {
            PlatformLogger cssLogger = Logging.getCSSLogger();
            if (cssLogger.isLoggable(PlatformLogger.Level.WARNING)) {
                cssLogger.warning(e2.getLocalizedMessage());
                return null;
            }
            return null;
        }
    }

    private URL resolveRuntimeImport(URI resourceUri) {
        String path = resourceUri.getPath();
        String resourcePath = path.startsWith("/") ? path.substring(1) : path;
        if (resourcePath.startsWith("com/sun/javafx/scene/control/skin/modena/") || resourcePath.startsWith("com/sun/javafx/scene/control/skin/caspian/")) {
            if (resourcePath.endsWith(".css") || resourcePath.endsWith(".bss")) {
                SecurityManager sm = System.getSecurityManager();
                if (sm == null) {
                    ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
                    URL resolved = contextClassLoader.getResource(resourcePath);
                    return resolved;
                }
                try {
                    URL rtJarURL = (URL) AccessController.doPrivileged(() -> {
                        ProtectionDomain protectionDomain = Application.class.getProtectionDomain();
                        CodeSource codeSource = protectionDomain.getCodeSource();
                        return codeSource.getLocation();
                    });
                    URI rtJarURI = rtJarURL.toURI();
                    String scheme = rtJarURI.getScheme();
                    String rtJarPath = rtJarURI.getPath();
                    if (DeploymentDescriptorParser.ATTR_FILE.equals(scheme) && rtJarPath.endsWith(".jar") && DeploymentDescriptorParser.ATTR_FILE.equals(scheme)) {
                        scheme = "jar:file";
                        rtJarPath = rtJarPath.concat("!/");
                    }
                    String rtJarPath2 = rtJarPath.concat(resourcePath);
                    String rtJarUserInfo = rtJarURI.getUserInfo();
                    String rtJarHost = rtJarURI.getHost();
                    int rtJarPort = rtJarURI.getPort();
                    URI resolved2 = new URI(scheme, rtJarUserInfo, rtJarHost, rtJarPort, rtJarPath2, null, null);
                    return resolved2.toURL();
                } catch (MalformedURLException | URISyntaxException | PrivilegedActionException e2) {
                    return null;
                }
            }
            return null;
        }
        return null;
    }

    public String toString() {
        return "URLType";
    }

    /* loaded from: jfxrt.jar:com/sun/javafx/css/converters/URLConverter$SequenceConverter.class */
    public static final class SequenceConverter extends StyleConverterImpl<ParsedValue<ParsedValue[], String>[], String[]> {
        @Override // javafx.css.StyleConverter
        public /* bridge */ /* synthetic */ Object convert(ParsedValue parsedValue, Font font) {
            return convert((ParsedValue<ParsedValue<ParsedValue[], String>[], String[]>) parsedValue, font);
        }

        public static SequenceConverter getInstance() {
            return Holder.SEQUENCE_INSTANCE;
        }

        private SequenceConverter() {
        }

        @Override // javafx.css.StyleConverter
        public String[] convert(ParsedValue<ParsedValue<ParsedValue[], String>[], String[]> value, Font font) {
            ParsedValue<ParsedValue[], String>[] layers = value.getValue();
            String[] urls = new String[layers.length];
            for (int layer = 0; layer < layers.length; layer++) {
                urls[layer] = URLConverter.getInstance().convert(layers[layer], font);
            }
            return urls;
        }

        public String toString() {
            return "URLSeqType";
        }
    }
}
