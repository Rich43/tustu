package com.sun.org.apache.xerces.internal.impl.dv;

import java.util.Locale;

/* loaded from: rt.jar:com/sun/org/apache/xerces/internal/impl/dv/ValidationContext.class */
public interface ValidationContext {
    boolean needFacetChecking();

    boolean needExtraChecking();

    boolean needToNormalize();

    boolean useNamespaces();

    boolean isEntityDeclared(String str);

    boolean isEntityUnparsed(String str);

    boolean isIdDeclared(String str);

    void addId(String str);

    void addIdRef(String str);

    String getSymbol(String str);

    String getURI(String str);

    Locale getLocale();
}
