package com.sun.xml.internal.bind.marshaller;

/* loaded from: rt.jar:com/sun/xml/internal/bind/marshaller/NamespacePrefixMapper.class */
public abstract class NamespacePrefixMapper {
    private static final String[] EMPTY_STRING = new String[0];

    public abstract String getPreferredPrefix(String str, String str2, boolean z2);

    public String[] getPreDeclaredNamespaceUris() {
        return EMPTY_STRING;
    }

    public String[] getPreDeclaredNamespaceUris2() {
        return EMPTY_STRING;
    }

    public String[] getContextualNamespaceDecls() {
        return EMPTY_STRING;
    }
}
