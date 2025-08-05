package com.sun.org.apache.xml.internal.security.utils;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/XPathFactory.class */
public abstract class XPathFactory {
    private static boolean xalanInstalled;

    public abstract XPathAPI newXPathAPI();

    static {
        try {
            if (ClassLoaderUtils.loadClass("com.sun.org.apache.xpath.internal.compiler.FunctionTable", XPathFactory.class) != null) {
                xalanInstalled = true;
            }
        } catch (Exception e2) {
        }
    }

    protected static synchronized boolean isXalanInstalled() {
        return xalanInstalled;
    }

    public static XPathFactory newInstance() {
        if (!isXalanInstalled()) {
            return new JDKXPathFactory();
        }
        if (XalanXPathAPI.isInstalled()) {
            return new XalanXPathFactory();
        }
        return new JDKXPathFactory();
    }
}
