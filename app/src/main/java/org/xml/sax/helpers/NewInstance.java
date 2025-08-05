package org.xml.sax.helpers;

/* loaded from: rt.jar:org/xml/sax/helpers/NewInstance.class */
class NewInstance {
    private static final String DEFAULT_PACKAGE = "com.sun.org.apache.xerces.internal";

    NewInstance() {
    }

    static Object newInstance(ClassLoader classLoader, String className) throws IllegalAccessException, InstantiationException, ClassNotFoundException {
        Class driverClass;
        boolean internal = false;
        if (System.getSecurityManager() != null && className != null && className.startsWith(DEFAULT_PACKAGE)) {
            internal = true;
        }
        if (classLoader == null || internal) {
            driverClass = Class.forName(className);
        } else {
            driverClass = classLoader.loadClass(className);
        }
        return driverClass.newInstance();
    }
}
