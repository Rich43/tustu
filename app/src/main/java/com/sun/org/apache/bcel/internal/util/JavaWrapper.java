package com.sun.org.apache.bcel.internal.util;

import java.lang.reflect.Method;
import java.lang.reflect.Modifier;

/* loaded from: rt.jar:com/sun/org/apache/bcel/internal/util/JavaWrapper.class */
public class JavaWrapper {
    private java.lang.ClassLoader loader;

    private static java.lang.ClassLoader getClassLoader() {
        String s2 = SecuritySupport.getSystemProperty("bcel.classloader");
        if (s2 == null || "".equals(s2)) {
            s2 = "com.sun.org.apache.bcel.internal.util.ClassLoader";
        }
        try {
            return (java.lang.ClassLoader) Class.forName(s2).newInstance();
        } catch (Exception e2) {
            throw new RuntimeException(e2.toString());
        }
    }

    public JavaWrapper(java.lang.ClassLoader loader) {
        this.loader = loader;
    }

    public JavaWrapper() {
        this(getClassLoader());
    }

    public void runMain(String class_name, String[] argv) throws NoSuchMethodException, ClassNotFoundException, SecurityException {
        Class cl = this.loader.loadClass(class_name);
        try {
            Method method = cl.getMethod("_main", argv.getClass());
            int m2 = method.getModifiers();
            Class r2 = method.getReturnType();
            if (!Modifier.isPublic(m2) || !Modifier.isStatic(m2) || Modifier.isAbstract(m2) || r2 != Void.TYPE) {
                throw new NoSuchMethodException();
            }
            try {
                method.invoke(null, argv);
            } catch (Exception ex) {
                ex.printStackTrace();
            }
        } catch (NoSuchMethodException e2) {
            System.out.println("In class " + class_name + ": public static void _main(String[] argv) is not defined");
        }
    }

    public static void _main(String[] argv) throws Exception {
        if (argv.length == 0) {
            System.out.println("Missing class name.");
            return;
        }
        String class_name = argv[0];
        String[] new_argv = new String[argv.length - 1];
        System.arraycopy(argv, 1, new_argv, 0, new_argv.length);
        JavaWrapper wrapper = new JavaWrapper();
        wrapper.runMain(class_name, new_argv);
    }
}
