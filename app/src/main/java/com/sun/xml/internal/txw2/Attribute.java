package com.sun.xml.internal.txw2;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/Attribute.class */
final class Attribute {
    final String nsUri;
    final String localName;
    Attribute next;
    final StringBuilder value = new StringBuilder();
    static final /* synthetic */ boolean $assertionsDisabled;

    static {
        $assertionsDisabled = !Attribute.class.desiredAssertionStatus();
    }

    Attribute(String nsUri, String localName) {
        if (!$assertionsDisabled && (nsUri == null || localName == null)) {
            throw new AssertionError();
        }
        this.nsUri = nsUri;
        this.localName = localName;
    }

    boolean hasName(String nsUri, String localName) {
        return this.localName.equals(localName) && this.nsUri.equals(nsUri);
    }
}
