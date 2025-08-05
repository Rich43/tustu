package com.sun.org.apache.xml.internal.security.c14n.implementations;

/* loaded from: rt.jar:com/sun/org/apache/xml/internal/security/c14n/implementations/Canonicalizer20010315ExclWithComments.class */
public class Canonicalizer20010315ExclWithComments extends Canonicalizer20010315Excl {
    public Canonicalizer20010315ExclWithComments() {
        super(true);
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public final String engineGetURI() {
        return "http://www.w3.org/2001/10/xml-exc-c14n#WithComments";
    }

    @Override // com.sun.org.apache.xml.internal.security.c14n.CanonicalizerSpi
    public final boolean engineGetIncludeComments() {
        return true;
    }
}
