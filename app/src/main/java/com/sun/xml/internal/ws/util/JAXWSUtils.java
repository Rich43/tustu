package com.sun.xml.internal.ws.util;

import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URL;
import java.util.UUID;
import java.util.regex.Pattern;
import javax.xml.namespace.QName;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/JAXWSUtils.class */
public final class JAXWSUtils {
    public static String getUUID() {
        return UUID.randomUUID().toString();
    }

    public static String getFileOrURLName(String fileOrURL) {
        try {
            try {
                return escapeSpace(new URL(fileOrURL).toExternalForm());
            } catch (MalformedURLException e2) {
                return new File(fileOrURL).getCanonicalFile().toURL().toExternalForm();
            }
        } catch (Exception e3) {
            return fileOrURL;
        }
    }

    public static URL getFileOrURL(String fileOrURL) throws IOException {
        try {
            URL url = new URL(fileOrURL);
            String scheme = String.valueOf(url.getProtocol()).toLowerCase();
            if (scheme.equals("http") || scheme.equals("https")) {
                return new URL(url.toURI().toASCIIString());
            }
            return url;
        } catch (MalformedURLException e2) {
            return new File(fileOrURL).toURL();
        } catch (URISyntaxException e3) {
            return new File(fileOrURL).toURL();
        }
    }

    public static URL getEncodedURL(String urlStr) throws MalformedURLException {
        URL url = new URL(urlStr);
        String scheme = String.valueOf(url.getProtocol()).toLowerCase();
        if (scheme.equals("http") || scheme.equals("https")) {
            try {
                return new URL(url.toURI().toASCIIString());
            } catch (URISyntaxException e2) {
                MalformedURLException malformedURLException = new MalformedURLException(e2.getMessage());
                malformedURLException.initCause(e2);
                throw malformedURLException;
            }
        }
        return url;
    }

    private static String escapeSpace(String url) {
        StringBuilder buf = new StringBuilder();
        for (int i2 = 0; i2 < url.length(); i2++) {
            if (url.charAt(i2) == ' ') {
                buf.append("%20");
            } else {
                buf.append(url.charAt(i2));
            }
        }
        return buf.toString();
    }

    public static String absolutize(String name) {
        try {
            URL baseURL = new File(".").getCanonicalFile().toURL();
            return new URL(baseURL, name).toExternalForm();
        } catch (IOException e2) {
            return name;
        }
    }

    public static void checkAbsoluteness(String systemId) {
        try {
            new URL(systemId);
        } catch (MalformedURLException e2) {
            try {
                new URI(systemId);
            } catch (URISyntaxException e3) {
                throw new IllegalArgumentException("system ID '" + systemId + "' isn't absolute", e3);
            }
        }
    }

    public static boolean matchQNames(QName target, QName pattern) {
        if (target != null && pattern != null && pattern.getNamespaceURI().equals(target.getNamespaceURI())) {
            String regex = pattern.getLocalPart().replaceAll("\\*", ".*");
            return Pattern.matches(regex, target.getLocalPart());
        }
        return false;
    }
}
