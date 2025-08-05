package com.sun.webkit.dom;

import org.w3c.dom.Node;
import org.w3c.dom.events.MutationEvent;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/MutationEventImpl.class */
public class MutationEventImpl extends EventImpl implements MutationEvent {
    public static final int MODIFICATION = 1;
    public static final int ADDITION = 2;
    public static final int REMOVAL = 3;

    static native long getRelatedNodeImpl(long j2);

    static native String getPrevValueImpl(long j2);

    static native String getNewValueImpl(long j2);

    static native String getAttrNameImpl(long j2);

    static native short getAttrChangeImpl(long j2);

    static native void initMutationEventImpl(long j2, String str, boolean z2, boolean z3, long j3, String str2, String str3, String str4, short s2);

    MutationEventImpl(long peer) {
        super(peer);
    }

    static MutationEvent getImpl(long peer) {
        return (MutationEvent) create(peer);
    }

    @Override // org.w3c.dom.events.MutationEvent
    public Node getRelatedNode() {
        return NodeImpl.getImpl(getRelatedNodeImpl(getPeer()));
    }

    @Override // org.w3c.dom.events.MutationEvent
    public String getPrevValue() {
        return getPrevValueImpl(getPeer());
    }

    @Override // org.w3c.dom.events.MutationEvent
    public String getNewValue() {
        return getNewValueImpl(getPeer());
    }

    @Override // org.w3c.dom.events.MutationEvent
    public String getAttrName() {
        return getAttrNameImpl(getPeer());
    }

    @Override // org.w3c.dom.events.MutationEvent
    public short getAttrChange() {
        return getAttrChangeImpl(getPeer());
    }

    @Override // org.w3c.dom.events.MutationEvent
    public void initMutationEvent(String type, boolean canBubble, boolean cancelable, Node relatedNode, String prevValue, String newValue, String attrName, short attrChange) {
        initMutationEventImpl(getPeer(), type, canBubble, cancelable, NodeImpl.getPeer(relatedNode), prevValue, newValue, attrName, attrChange);
    }
}
