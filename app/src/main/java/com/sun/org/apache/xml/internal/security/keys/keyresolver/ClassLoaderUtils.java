package com.sun.org.apache.xml.internal.security.keys.keyresolver;

import com.sun.org.slf4j.internal.Logger;
import com.sun.org.slf4j.internal.LoggerFactory;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/keys/keyresolver/ClassLoaderUtils.class */
final class ClassLoaderUtils {
    private static final Logger LOG = LoggerFactory.getLogger(ClassLoaderUtils.class);

    private ClassLoaderUtils() {
    }

    static Class<?> loadClass(String str, Class<?> cls) throws ClassNotFoundException {
        try {
            ClassLoader contextClassLoader = Thread.currentThread().getContextClassLoader();
            if (contextClassLoader != null) {
                return contextClassLoader.loadClass(str);
            }
        } catch (ClassNotFoundException e2) {
            LOG.debug(e2.getMessage(), e2);
        }
        return loadClass2(str, cls);
    }

    private static Class<?> loadClass2(String str, Class<?> cls) throws ClassNotFoundException {
        try {
            return Class.forName(str);
        } catch (ClassNotFoundException e2) {
            try {
                if (ClassLoaderUtils.class.getClassLoader() != null) {
                    return ClassLoaderUtils.class.getClassLoader().loadClass(str);
                }
            } catch (ClassNotFoundException e3) {
                if (cls != null && cls.getClassLoader() != null) {
                    return cls.getClassLoader().loadClass(str);
                }
            }
            LOG.debug(e2.getMessage(), e2);
            throw e2;
        }
    }
}
