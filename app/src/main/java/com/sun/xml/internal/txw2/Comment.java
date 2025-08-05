package com.sun.xml.internal.txw2;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/Comment.class */
final class Comment extends Content {
    private final StringBuilder buffer = new StringBuilder();

    public Comment(Document document, NamespaceResolver nsResolver, Object obj) {
        document.writeValue(obj, nsResolver, this.buffer);
    }

    @Override // com.sun.xml.internal.txw2.Content
    boolean concludesPendingStartTag() {
        return false;
    }

    @Override // com.sun.xml.internal.txw2.Content
    void accept(ContentVisitor visitor) {
        visitor.onComment(this.buffer);
    }
}
