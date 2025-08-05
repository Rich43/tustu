package com.sun.org.apache.xml.internal.security.utils.resolver;

import org.w3c.dom.Attr;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/resolver/ResourceResolverContext.class */
public class ResourceResolverContext {
    public final String uriToResolve;
    public final boolean secureValidation;
    public final String baseUri;
    public final Attr attr;

    public ResourceResolverContext(Attr attr, String str, boolean z2) {
        this.attr = attr;
        this.baseUri = str;
        this.secureValidation = z2;
        this.uriToResolve = attr != null ? attr.getValue() : null;
    }
}
