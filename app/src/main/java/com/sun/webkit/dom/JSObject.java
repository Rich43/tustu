package com.sun.webkit.dom;

import com.sun.webkit.Disposer;
import com.sun.webkit.DisposerRecord;
import com.sun.webkit.Invoker;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.util.concurrent.atomic.AtomicInteger;
import netscape.javascript.JSException;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/JSObject.class */
class JSObject extends netscape.javascript.JSObject {
    static final int JS_CONTEXT_OBJECT = 0;
    static final int JS_DOM_NODE_OBJECT = 1;
    static final int JS_DOM_WINDOW_OBJECT = 2;
    private final long peer;
    private final int peer_type;
    private static final String UNDEFINED = new String("undefined");
    private static AtomicInteger peerCount = new AtomicInteger();

    /* JADX INFO: Access modifiers changed from: private */
    public static native void unprotectImpl(long j2, int i2);

    private static native Object evalImpl(long j2, int i2, String str);

    private static native Object getMemberImpl(long j2, int i2, String str);

    private static native void setMemberImpl(long j2, int i2, String str, Object obj, AccessControlContext accessControlContext);

    private static native void removeMemberImpl(long j2, int i2, String str);

    private static native Object getSlotImpl(long j2, int i2, int i3);

    private static native void setSlotImpl(long j2, int i2, int i3, Object obj, AccessControlContext accessControlContext);

    private static native Object callImpl(long j2, int i2, String str, Object[] objArr, AccessControlContext accessControlContext);

    private static native String toStringImpl(long j2, int i2);

    JSObject(long peer, int peer_type) {
        this.peer = peer;
        this.peer_type = peer_type;
        if (peer_type == 0) {
            Disposer.addRecord(this, new SelfDisposer(peer, peer_type));
            peerCount.incrementAndGet();
        }
    }

    long getPeer() {
        return this.peer;
    }

    static int test_getPeerCount() {
        return peerCount.get();
    }

    @Override // netscape.javascript.JSObject
    public Object eval(String s2) throws JSException {
        Invoker.getInvoker().checkEventThread();
        return evalImpl(this.peer, this.peer_type, s2);
    }

    @Override // netscape.javascript.JSObject
    public Object getMember(String name) {
        Invoker.getInvoker().checkEventThread();
        return getMemberImpl(this.peer, this.peer_type, name);
    }

    @Override // netscape.javascript.JSObject
    public void setMember(String name, Object value) throws JSException {
        Invoker.getInvoker().checkEventThread();
        setMemberImpl(this.peer, this.peer_type, name, value, AccessController.getContext());
    }

    @Override // netscape.javascript.JSObject
    public void removeMember(String name) throws JSException {
        Invoker.getInvoker().checkEventThread();
        removeMemberImpl(this.peer, this.peer_type, name);
    }

    @Override // netscape.javascript.JSObject
    public Object getSlot(int index) throws JSException {
        Invoker.getInvoker().checkEventThread();
        return getSlotImpl(this.peer, this.peer_type, index);
    }

    @Override // netscape.javascript.JSObject
    public void setSlot(int index, Object value) throws JSException {
        Invoker.getInvoker().checkEventThread();
        setSlotImpl(this.peer, this.peer_type, index, value, AccessController.getContext());
    }

    @Override // netscape.javascript.JSObject
    public Object call(String methodName, Object... args) throws JSException {
        Invoker.getInvoker().checkEventThread();
        return callImpl(this.peer, this.peer_type, methodName, args, AccessController.getContext());
    }

    public String toString() {
        Invoker.getInvoker().checkEventThread();
        return toStringImpl(this.peer, this.peer_type);
    }

    public boolean equals(Object other) {
        return other == this || (other != null && other.getClass() == JSObject.class && this.peer == ((JSObject) other).peer);
    }

    public int hashCode() {
        return (int) (this.peer ^ (this.peer >> 17));
    }

    private static JSException fwkMakeException(Object value) {
        String string = value == null ? null : value.toString();
        JSException ex = new JSException(value == null ? null : value.toString());
        if (value instanceof Throwable) {
            ex.initCause((Throwable) value);
        }
        return ex;
    }

    /* loaded from: jfxrt.jar:com/sun/webkit/dom/JSObject$SelfDisposer.class */
    private static final class SelfDisposer implements DisposerRecord {
        long peer;
        final int peer_type;

        private SelfDisposer(long peer, int peer_type) {
            this.peer = peer;
            this.peer_type = peer_type;
        }

        @Override // com.sun.webkit.DisposerRecord
        public void dispose() {
            if (this.peer != 0) {
                JSObject.unprotectImpl(this.peer, this.peer_type);
                this.peer = 0L;
                JSObject.peerCount.decrementAndGet();
            }
        }
    }
}
