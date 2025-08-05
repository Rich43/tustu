package com.sun.xml.internal.txw2;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/Cdata.class */
final class Cdata extends Text {
    Cdata(Document document, NamespaceResolver nsResolver, Object obj) {
        super(document, nsResolver, obj);
    }

    @Override // com.sun.xml.internal.txw2.Content
    void accept(ContentVisitor visitor) {
        visitor.onCdata(this.buffer);
    }
}
