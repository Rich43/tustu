package com.sun.xml.internal.txw2;

/* loaded from: rt.jar:com/sun/xml/internal/txw2/Content.class */
abstract class Content {
    private Content next;
    static final /* synthetic */ boolean $assertionsDisabled;

    abstract boolean concludesPendingStartTag();

    abstract void accept(ContentVisitor contentVisitor);

    Content() {
    }

    static {
        $assertionsDisabled = !Content.class.desiredAssertionStatus();
    }

    final Content getNext() {
        return this.next;
    }

    final void setNext(Document doc, Content next) {
        if (!$assertionsDisabled && next == null) {
            throw new AssertionError();
        }
        if (!$assertionsDisabled && this.next != null) {
            throw new AssertionError((Object) ("next of " + ((Object) this) + " is already set to " + ((Object) this.next)));
        }
        this.next = next;
        doc.run();
    }

    boolean isReadyToCommit() {
        return true;
    }

    public void written() {
    }
}
