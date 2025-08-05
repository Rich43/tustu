package com.sun.xml.internal.org.jvnet.mimepull;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.UnsupportedEncodingException;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/org/jvnet/mimepull/FactoryFinder.class */
class FactoryFinder {
    private static ClassLoader cl = FactoryFinder.class.getClassLoader();

    FactoryFinder() {
    }

    static Object find(String factoryId) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        String systemProp = System.getProperty(factoryId);
        if (systemProp != null) {
            return newInstance(systemProp);
        }
        String providerName = findJarServiceProviderName(factoryId);
        if (providerName != null && providerName.trim().length() > 0) {
            return newInstance(providerName);
        }
        return null;
    }

    static Object newInstance(String className) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Class providerClass = cl.loadClass(className);
        Object instance = providerClass.newInstance();
        return instance;
    }

    private static String findJarServiceProviderName(String factoryId) {
        String serviceId = "META-INF/services/" + factoryId;
        InputStream is = cl.getResourceAsStream(serviceId);
        if (is == null) {
            return null;
        }
        BufferedReader rd = null;
        try {
            try {
                rd = new BufferedReader(new InputStreamReader(is, "UTF-8"));
            } catch (UnsupportedEncodingException e2) {
                rd = new BufferedReader(new InputStreamReader(is));
            }
            try {
                String factoryClassName = rd.readLine();
                if (rd != null) {
                    try {
                        rd.close();
                    } catch (IOException ex) {
                        Logger.getLogger(FactoryFinder.class.getName()).log(Level.INFO, (String) null, (Throwable) ex);
                    }
                }
                return factoryClassName;
            } catch (IOException e3) {
                return null;
            }
        } finally {
            if (rd != null) {
                try {
                    rd.close();
                } catch (IOException ex2) {
                    Logger.getLogger(FactoryFinder.class.getName()).log(Level.INFO, (String) null, (Throwable) ex2);
                }
            }
        }
    }
}
