package com.sun.xml.internal.messaging.saaj.packaging.mime.util;

import java.io.IOException;
import java.io.OutputStream;

/* loaded from: rt.jar:com/sun/xml/internal/messaging/saaj/packaging/mime/util/OutputUtil.class */
public abstract class OutputUtil {
    private static byte[] newline = {13, 10};

    public static void writeln(String s2, OutputStream out) throws IOException {
        writeAsAscii(s2, out);
        writeln(out);
    }

    public static void writeAsAscii(String s2, OutputStream out) throws IOException {
        int len = s2.length();
        for (int i2 = 0; i2 < len; i2++) {
            out.write((byte) s2.charAt(i2));
        }
    }

    public static void writeln(OutputStream out) throws IOException {
        out.write(newline);
    }
}
