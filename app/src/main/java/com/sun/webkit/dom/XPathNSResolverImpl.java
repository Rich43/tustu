package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.xpath.XPathNSResolver;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/XPathNSResolverImpl.class */
public class XPathNSResolverImpl implements XPathNSResolver {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native String lookupNamespaceURIImpl(long j2, String str);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/XPathNSResolverImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            XPathNSResolverImpl.dispose(this.peer);
        }
    }

    XPathNSResolverImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static XPathNSResolver create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new XPathNSResolverImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof XPathNSResolverImpl) && this.peer == ((XPathNSResolverImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(XPathNSResolver arg) {
        if (arg == null) {
            return 0L;
        }
        return ((XPathNSResolverImpl) arg).getPeer();
    }

    static XPathNSResolver getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.xpath.XPathNSResolver
    public String lookupNamespaceURI(String prefix) {
        return lookupNamespaceURIImpl(getPeer(), prefix);
    }
}
