package com.sun.xml.internal.txw2;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/StartDocument.class */
final class StartDocument extends Content {
    StartDocument() {
    }

    @Override // com.sun.xml.internal.txw2.Content
    boolean concludesPendingStartTag() {
        return true;
    }

    @Override // com.sun.xml.internal.txw2.Content
    void accept(ContentVisitor visitor) {
        visitor.onStartDocument();
    }
}
