package com.sun.imageio.plugins.common;

/* loaded from: rt.jar:com/sun/imageio/plugins/common/I18N.class */
public final class I18N extends I18NImpl {
    private static final String resource_name = "iio-plugin.properties";

    public static String getString(String str) {
        return getString("com.sun.imageio.plugins.common.I18N", resource_name, str);
    }
}
