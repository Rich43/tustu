package com.sun.org.apache.xml.internal.security.c14n.implementations;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/implementations/Canonicalizer11_OmitComments.class */
public class Canonicalizer11_OmitComments extends Canonicalizer20010315 {
    public Canonicalizer11_OmitComments() {
        super(false, true);
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public final String engineGetURI() {
        return "http://www.w3.org/2006/12/xml-c14n11";
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public final boolean engineGetIncludeComments() {
        return false;
    }
}
