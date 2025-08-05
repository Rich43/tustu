package com.sun.org.apache.xerces.internal.utils;

import com.sun.xml.internal.ws.transport.http.DeploymentDescriptorParser;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.security.PrivilegedActionException;
import java.security.PrivilegedExceptionAction;
import java.util.Locale;
import java.util.MissingResourceException;
import java.util.Properties;
import java.util.PropertyResourceBundle;
import java.util.ResourceBundle;
import jdk.internal.dynalink.CallSiteDescriptor;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/utils/SecuritySupport.class */
public final class SecuritySupport {
    private static final SecuritySupport securitySupport = new SecuritySupport();
    static final Properties cacheProps = new Properties();
    static volatile boolean firstTime = true;

    public static SecuritySupport getInstance() {
        return securitySupport;
    }

    static ClassLoader getContextClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.xerces.internal.utils.SecuritySupport.1
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                ClassLoader cl = null;
                try {
                    cl = Thread.currentThread().getContextClassLoader();
                } catch (SecurityException e2) {
                }
                return cl;
            }
        });
    }

    static ClassLoader getSystemClassLoader() {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.xerces.internal.utils.SecuritySupport.2
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                ClassLoader cl = null;
                try {
                    cl = ClassLoader.getSystemClassLoader();
                } catch (SecurityException e2) {
                }
                return cl;
            }
        });
    }

    static ClassLoader getParentClassLoader(final ClassLoader cl) {
        return (ClassLoader) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.xerces.internal.utils.SecuritySupport.3
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                ClassLoader parent = null;
                try {
                    parent = cl.getParent();
                } catch (SecurityException e2) {
                }
                if (parent == cl) {
                    return null;
                }
                return parent;
            }
        });
    }

    public static String getSystemProperty(final String propName) {
        return (String) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.xerces.internal.utils.SecuritySupport.4
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return System.getProperty(propName);
            }
        });
    }

    static FileInputStream getFileInputStream(final File file) throws FileNotFoundException {
        try {
            return (FileInputStream) AccessController.doPrivileged(new PrivilegedExceptionAction() { // from class: com.sun.org.apache.xerces.internal.utils.SecuritySupport.5
                @Override // java.security.PrivilegedExceptionAction
                public Object run() throws FileNotFoundException {
                    return new FileInputStream(file);
                }
            });
        } catch (PrivilegedActionException e2) {
            throw ((FileNotFoundException) e2.getException());
        }
    }

    public static ResourceBundle getResourceBundle(String bundle) {
        return getResourceBundle(bundle, Locale.getDefault());
    }

    public static ResourceBundle getResourceBundle(final String bundle, final Locale locale) {
        return (ResourceBundle) AccessController.doPrivileged(new PrivilegedAction<ResourceBundle>() { // from class: com.sun.org.apache.xerces.internal.utils.SecuritySupport.6
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public ResourceBundle run2() {
                try {
                    return PropertyResourceBundle.getBundle(bundle, locale);
                } catch (MissingResourceException e2) {
                    try {
                        return PropertyResourceBundle.getBundle(bundle, new Locale("en", "US"));
                    } catch (MissingResourceException e3) {
                        throw new MissingResourceException("Could not load any resource bundle by " + bundle, bundle, "");
                    }
                }
            }
        });
    }

    static boolean getFileExists(final File f2) {
        return ((Boolean) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.xerces.internal.utils.SecuritySupport.7
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return f2.exists() ? Boolean.TRUE : Boolean.FALSE;
            }
        })).booleanValue();
    }

    static long getLastModified(final File f2) {
        return ((Long) AccessController.doPrivileged(new PrivilegedAction() { // from class: com.sun.org.apache.xerces.internal.utils.SecuritySupport.8
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Object run2() {
                return new Long(f2.lastModified());
            }
        })).longValue();
    }

    public static String sanitizePath(String uri) {
        if (uri == null) {
            return "";
        }
        int i2 = uri.lastIndexOf("/");
        if (i2 > 0) {
            return uri.substring(i2 + 1, uri.length());
        }
        return uri;
    }

    public static String checkAccess(String systemId, String allowedProtocols, String accessAny) throws IOException {
        String protocol;
        if (systemId != null) {
            if (allowedProtocols != null && allowedProtocols.equalsIgnoreCase(accessAny)) {
                return null;
            }
            if (systemId.indexOf(CallSiteDescriptor.TOKEN_DELIMITER) == -1) {
                protocol = DeploymentDescriptorParser.ATTR_FILE;
            } else {
                URL url = new URL(systemId);
                protocol = url.getProtocol();
                if (protocol.equalsIgnoreCase("jar")) {
                    String path = url.getPath();
                    protocol = path.substring(0, path.indexOf(CallSiteDescriptor.TOKEN_DELIMITER));
                }
            }
            if (isProtocolAllowed(protocol, allowedProtocols)) {
                return null;
            }
            return protocol;
        }
        return null;
    }

    private static boolean isProtocolAllowed(String protocol, String allowedProtocols) {
        if (allowedProtocols == null) {
            return false;
        }
        String[] temp = allowedProtocols.split(",");
        for (String t2 : temp) {
            if (t2.trim().equalsIgnoreCase(protocol)) {
                return true;
            }
        }
        return false;
    }

    public static String getJAXPSystemProperty(String sysPropertyId) {
        String accessExternal = getSystemProperty(sysPropertyId);
        if (accessExternal == null) {
            accessExternal = readJAXPProperty(sysPropertyId);
        }
        return accessExternal;
    }

    static String readJAXPProperty(String propertyId) {
        String value = null;
        InputStream is = null;
        try {
            if (firstTime) {
                synchronized (cacheProps) {
                    if (firstTime) {
                        String configFile = getSystemProperty("java.home") + File.separator + "lib" + File.separator + "jaxp.properties";
                        File f2 = new File(configFile);
                        if (getFileExists(f2)) {
                            is = getFileInputStream(f2);
                            cacheProps.load(is);
                        }
                        firstTime = false;
                    }
                }
            }
            value = cacheProps.getProperty(propertyId);
            if (is != null) {
                try {
                    is.close();
                } catch (IOException e2) {
                }
            }
        } catch (Exception e3) {
            if (0 != 0) {
                try {
                    is.close();
                } catch (IOException e4) {
                }
            }
        } catch (Throwable th) {
            if (0 != 0) {
                try {
                    is.close();
                } catch (IOException e5) {
                }
            }
            throw th;
        }
        return value;
    }

    private SecuritySupport() {
    }
}
