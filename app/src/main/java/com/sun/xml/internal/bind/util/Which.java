package com.sun.xml.internal.bind.util;

import java.net.URL;

/* loaded from: rt.jar:com/sun/xml/internal/bind/util/Which.class */
public class Which {
    public static String which(Class clazz) {
        return which(clazz.getName(), SecureLoader.getClassClassLoader(clazz));
    }

    public static String which(String classname, ClassLoader loader) {
        String classnameAsResource = classname.replace('.', '/') + ".class";
        if (loader == null) {
            loader = SecureLoader.getSystemClassLoader();
        }
        URL it = loader.getResource(classnameAsResource);
        if (it != null) {
            return it.toString();
        }
        return null;
    }
}
