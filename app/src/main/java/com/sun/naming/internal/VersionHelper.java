package com.sun.naming.internal;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.net.URL;
import java.util.StringTokenizer;
import java.util.Vector;
import javax.naming.Context;
import javax.naming.NamingEnumeration;
import javax.naming.ldap.LdapContext;

/* loaded from: rt.jar:com/sun/naming/internal/VersionHelper.class */
public abstract class VersionHelper {
    private static VersionHelper helper;
    static final String[] PROPS = {Context.INITIAL_CONTEXT_FACTORY, Context.OBJECT_FACTORIES, Context.URL_PKG_PREFIXES, Context.STATE_FACTORIES, Context.PROVIDER_URL, Context.DNS_URL, LdapContext.CONTROL_FACTORIES};
    public static final int INITIAL_CONTEXT_FACTORY = 0;
    public static final int OBJECT_FACTORIES = 1;
    public static final int URL_PKG_PREFIXES = 2;
    public static final int STATE_FACTORIES = 3;
    public static final int PROVIDER_URL = 4;
    public static final int DNS_URL = 5;
    public static final int CONTROL_FACTORIES = 6;

    public abstract Class<?> loadClassWithoutInit(String str) throws ClassNotFoundException;

    public abstract Class<?> loadClass(String str) throws ClassNotFoundException;

    abstract Class<?> loadClass(String str, ClassLoader classLoader) throws ClassNotFoundException;

    public abstract Class<?> loadClass(String str, String str2) throws MalformedURLException, ClassNotFoundException;

    abstract String getJndiProperty(int i2);

    abstract String[] getJndiProperties();

    abstract InputStream getResourceAsStream(Class<?> cls, String str);

    abstract InputStream getJavaHomeLibStream(String str);

    abstract NamingEnumeration<InputStream> getResources(ClassLoader classLoader, String str) throws IOException;

    abstract ClassLoader getContextClassLoader();

    static {
        helper = null;
        helper = new VersionHelper12();
    }

    VersionHelper() {
    }

    public static VersionHelper getVersionHelper() {
        return helper;
    }

    protected static URL[] getUrlArray(String str) throws MalformedURLException {
        StringTokenizer stringTokenizer = new StringTokenizer(str);
        Vector vector = new Vector(10);
        while (stringTokenizer.hasMoreTokens()) {
            vector.addElement(stringTokenizer.nextToken());
        }
        String[] strArr = new String[vector.size()];
        for (int i2 = 0; i2 < strArr.length; i2++) {
            strArr[i2] = (String) vector.elementAt(i2);
        }
        URL[] urlArr = new URL[strArr.length];
        for (int i3 = 0; i3 < urlArr.length; i3++) {
            urlArr[i3] = new URL(strArr[i3]);
        }
        return urlArr;
    }
}
