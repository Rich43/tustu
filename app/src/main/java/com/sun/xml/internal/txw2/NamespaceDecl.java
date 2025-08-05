package com.sun.xml.internal.txw2;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/NamespaceDecl.class */
final class NamespaceDecl {
    final String uri;
    boolean requirePrefix;
    final String dummyPrefix;
    final char uniqueId;
    String prefix;
    boolean declared;
    NamespaceDecl next;

    NamespaceDecl(char uniqueId, String uri, String prefix, boolean requirePrefix) {
        this.dummyPrefix = new StringBuilder(2).append((char) 0).append(uniqueId).toString();
        this.uri = uri;
        this.prefix = prefix;
        this.requirePrefix = requirePrefix;
        this.uniqueId = uniqueId;
    }
}
