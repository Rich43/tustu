package com.sun.xml.internal.ws.util;

import java.io.IOException;
import java.io.InputStream;

/* loaded from: rt.jar:com/sun/xml/internal/ws/util/RuntimeVersion.class */
public final class RuntimeVersion {
    public static final Version VERSION;

    static {
        InputStream in = RuntimeVersion.class.getResourceAsStream("version.properties");
        try {
            Version version = Version.create(in);
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e2) {
                }
            }
            VERSION = version == null ? Version.create(null) : version;
        } catch (Throwable th) {
            if (in != null) {
                try {
                    in.close();
                } catch (IOException e3) {
                }
            }
            throw th;
        }
    }

    public String getVersion() {
        return VERSION.toString();
    }
}
