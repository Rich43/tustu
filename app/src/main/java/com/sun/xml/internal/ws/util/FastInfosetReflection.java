package com.sun.xml.internal.ws.util;

import java.io.InputStream;
import java.lang.reflect.Constructor;
import java.lang.reflect.Method;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/FastInfosetReflection.class */
public class FastInfosetReflection {
    public static final Constructor fiStAXDocumentParser_new;
    public static final Method fiStAXDocumentParser_setInputStream;
    public static final Method fiStAXDocumentParser_setStringInterning;

    static {
        Constructor tmp_new = null;
        Method tmp_setInputStream = null;
        Method tmp_setStringInterning = null;
        try {
            Class clazz = Class.forName("com.sun.xml.internal.fastinfoset.stax.StAXDocumentParser");
            tmp_new = clazz.getConstructor(new Class[0]);
            tmp_setInputStream = clazz.getMethod("setInputStream", InputStream.class);
            tmp_setStringInterning = clazz.getMethod("setStringInterning", Boolean.TYPE);
        } catch (Exception e2) {
        }
        fiStAXDocumentParser_new = tmp_new;
        fiStAXDocumentParser_setInputStream = tmp_setInputStream;
        fiStAXDocumentParser_setStringInterning = tmp_setStringInterning;
    }
}
