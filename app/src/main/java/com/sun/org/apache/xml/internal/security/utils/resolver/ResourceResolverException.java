package com.sun.org.apache.xml.internal.security.utils.resolver;

import com.sun.org.apache.xml.internal.security.exceptions.XMLSecurityException;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/utils/resolver/ResourceResolverException.class */
public class ResourceResolverException extends XMLSecurityException {
    private static final long serialVersionUID = 1;
    private String uri;
    private String baseURI;

    public ResourceResolverException(String str, String str2, String str3) {
        super(str);
        this.uri = str2;
        this.baseURI = str3;
    }

    public ResourceResolverException(String str, Object[] objArr, String str2, String str3) {
        super(str, objArr);
        this.uri = str2;
        this.baseURI = str3;
    }

    public ResourceResolverException(Exception exc, String str, String str2, String str3) {
        super(exc, str3);
        this.uri = str;
        this.baseURI = str2;
    }

    @Deprecated
    public ResourceResolverException(String str, Exception exc, String str2, String str3) {
        this(exc, str2, str3, str);
    }

    public ResourceResolverException(Exception exc, String str, String str2, String str3, Object[] objArr) {
        super(exc, str3, objArr);
        this.uri = str;
        this.baseURI = str2;
    }

    @Deprecated
    public ResourceResolverException(String str, Object[] objArr, Exception exc, String str2, String str3) {
        this(exc, str2, str3, str, objArr);
    }

    public void setURI(String str) {
        this.uri = str;
    }

    public String getURI() {
        return this.uri;
    }

    public void setbaseURI(String str) {
        this.baseURI = str;
    }

    public String getbaseURI() {
        return this.baseURI;
    }
}
