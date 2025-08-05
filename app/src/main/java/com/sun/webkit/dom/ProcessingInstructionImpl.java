package com.sun.webkit.dom;

import org.w3c.dom.Node;
import org.w3c.dom.ProcessingInstruction;
import org.w3c.dom.stylesheets.StyleSheet;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/ProcessingInstructionImpl.class */
public class ProcessingInstructionImpl extends CharacterDataImpl implements ProcessingInstruction {
    static native String getTargetImpl(long j2);

    static native long getSheetImpl(long j2);

    ProcessingInstructionImpl(long peer) {
        super(peer);
    }

    static Node getImpl(long peer) {
        return create(peer);
    }

    @Override // org.w3c.dom.ProcessingInstruction
    public String getTarget() {
        return getTargetImpl(getPeer());
    }

    public StyleSheet getSheet() {
        return StyleSheetImpl.getImpl(getSheetImpl(getPeer()));
    }
}
