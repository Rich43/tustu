package com.sun.imageio.plugins.common;

import java.util.PropertyResourceBundle;

/* loaded from: rt.jar:com/sun/imageio/plugins/common/I18NImpl.class */
public class I18NImpl {
    protected static final String getString(String str, String str2, String str3) {
        try {
            return (String) new PropertyResourceBundle(Class.forName(str).getResourceAsStream(str2)).handleGetObject(str3);
        } catch (Throwable th) {
            throw new RuntimeException(th);
        }
    }
}
