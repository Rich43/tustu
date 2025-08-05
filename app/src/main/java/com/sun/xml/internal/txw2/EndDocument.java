package com.sun.xml.internal.txw2;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/EndDocument.class */
final class EndDocument extends Content {
    EndDocument() {
    }

    @Override // com.sun.xml.internal.txw2.Content
    boolean concludesPendingStartTag() {
        return true;
    }

    @Override // com.sun.xml.internal.txw2.Content
    void accept(ContentVisitor visitor) {
        visitor.onEndDocument();
    }
}
