package com.sun.webkit.plugin;

import java.net.URL;

/* loaded from: jfxrt.jar:com/sun/webkit/plugin/PluginHandler.class */
interface PluginHandler {
    String getName();

    String getFileName();

    String getDescription();

    String[] supportedMIMETypes();

    boolean isSupportedMIMEType(String str);

    boolean isSupportedPlatform();

    Plugin createPlugin(URL url, String str, String[] strArr, String[] strArr2);
}
