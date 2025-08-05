package com.sun.xml.internal.bind.v2.runtime.reflect.opt;

import com.sun.xml.internal.bind.Util;
import com.sun.xml.internal.bind.v2.bytecode.ClassTailor;
import java.io.InputStream;
import java.util.logging.Level;
import java.util.logging.Logger;

/* loaded from: rt.jar:com/sun/xml/internal/bind/v2/runtime/reflect/opt/AccessorInjector.class */
class AccessorInjector {
    private static final Logger logger = Util.getClassLogger();
    protected static final boolean noOptimize;
    private static final ClassLoader CLASS_LOADER;

    AccessorInjector() {
    }

    static {
        noOptimize = Util.getSystemProperty(new StringBuilder().append(ClassTailor.class.getName()).append(".noOptimize").toString()) != null;
        if (noOptimize) {
            logger.info("The optimized code generation is disabled");
        }
        CLASS_LOADER = SecureLoader.getClassClassLoader(AccessorInjector.class);
    }

    public static Class<?> prepare(Class beanClass, String templateClassName, String newClassName, String... replacements) {
        if (noOptimize) {
            return null;
        }
        try {
            ClassLoader cl = SecureLoader.getClassClassLoader(beanClass);
            if (cl == null) {
                return null;
            }
            Class c2 = Injector.find(cl, newClassName);
            if (c2 == null) {
                byte[] image = tailor(templateClassName, newClassName, replacements);
                if (image == null) {
                    return null;
                }
                c2 = Injector.inject(cl, newClassName, image);
                if (c2 == null) {
                    Injector.find(cl, newClassName);
                }
            }
            return c2;
        } catch (SecurityException e2) {
            logger.log(Level.INFO, "Unable to create an optimized TransducedAccessor ", (Throwable) e2);
            return null;
        }
    }

    private static byte[] tailor(String templateClassName, String newClassName, String... replacements) {
        InputStream resource;
        if (CLASS_LOADER != null) {
            resource = CLASS_LOADER.getResourceAsStream(templateClassName + ".class");
        } else {
            resource = ClassLoader.getSystemResourceAsStream(templateClassName + ".class");
        }
        if (resource == null) {
            return null;
        }
        return ClassTailor.tailor(resource, templateClassName, newClassName, replacements);
    }
}
