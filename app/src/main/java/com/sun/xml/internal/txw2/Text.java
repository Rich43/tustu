package com.sun.xml.internal.txw2;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/Text.class */
abstract class Text extends Content {
    protected final StringBuilder buffer = new StringBuilder();

    protected Text(Document document, NamespaceResolver nsResolver, Object obj) {
        document.writeValue(obj, nsResolver, this.buffer);
    }

    @Override // com.sun.xml.internal.txw2.Content
    boolean concludesPendingStartTag() {
        return false;
    }
}
