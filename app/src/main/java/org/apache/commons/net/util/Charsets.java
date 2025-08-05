package org.apache.commons.net.util;

import java.nio.charset.Charset;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/util/Charsets.class */
public class Charsets {
    public static Charset toCharset(String charsetName) {
        return charsetName == null ? Charset.defaultCharset() : Charset.forName(charsetName);
    }

    public static Charset toCharset(String charsetName, String defaultCharsetName) {
        return charsetName == null ? Charset.forName(defaultCharsetName) : Charset.forName(charsetName);
    }
}
