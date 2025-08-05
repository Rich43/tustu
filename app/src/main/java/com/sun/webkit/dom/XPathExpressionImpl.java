package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import org.w3c.dom.DOMException;
import org.w3c.dom.Node;
import org.w3c.dom.xpath.XPathExpression;
import org.w3c.dom.xpath.XPathResult;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/XPathExpressionImpl.class */
public class XPathExpressionImpl implements XPathExpression {
    private final long peer;

    /* JADX INFO: Access modifiers changed from: private */
    public static native void dispose(long j2);

    static native long evaluateImpl(long j2, long j3, short s2, long j4);

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/XPathExpressionImpl$SelfDisposer.class */
    private static class SelfDisposer implements DisposerRecord {
        private final long peer;

        SelfDisposer(long peer) {
            this.peer = peer;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            XPathExpressionImpl.dispose(this.peer);
        }
    }

    XPathExpressionImpl(long peer) {
        this.peer = peer;
        Disposer.addRecord(this, new SelfDisposer(peer));
    }

    static XPathExpression create(long peer) {
        if (peer == 0) {
            return null;
        }
        return new XPathExpressionImpl(peer);
    }

    long getPeer() {
        return this.peer;
    }

    public boolean equals(Object that) {
        return (that instanceof XPathExpressionImpl) && this.peer == ((XPathExpressionImpl) that).peer;
    }

    public int hashCode() {
        long p2 = this.peer;
        return (int) (p2 ^ (p2 >> 17));
    }

    static long getPeer(XPathExpression arg) {
        if (arg == null) {
            return 0L;
        }
        return ((XPathExpressionImpl) arg).getPeer();
    }

    static XPathExpression getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.xpath.XPathExpression
    public Object evaluate(Node contextNode, short type, Object result) throws DOMException {
        return evaluate(contextNode, type, (XPathResult) result);
    }

    public XPathResult evaluate(Node contextNode, short type, XPathResult inResult) throws DOMException {
        return XPathResultImpl.getImpl(evaluateImpl(getPeer(), NodeImpl.getPeer(contextNode), type, XPathResultImpl.getPeer(inResult)));
    }
}
