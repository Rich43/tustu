package com.sun.webkit.event;

import java.util.Arrays;

/* loaded from: jfxrt.jar:com/sun/webkit/event/WCInputMethodEvent.class */
public final class WCInputMethodEvent {
    public static final int INPUT_METHOD_TEXT_CHANGED = 0;
    public static final int CARET_POSITION_CHANGED = 1;
    private final int id;
    private final String composed;
    private final String committed;
    private final int[] attributes;
    private final int caretPosition;

    public WCInputMethodEvent(String composed, String committed, int[] attributes, int caretPosition) {
        this.id = 0;
        this.composed = composed;
        this.committed = committed;
        this.attributes = Arrays.copyOf(attributes, attributes.length);
        this.caretPosition = caretPosition;
    }

    public WCInputMethodEvent(int caretPosition) {
        this.id = 1;
        this.composed = null;
        this.committed = null;
        this.attributes = null;
        this.caretPosition = caretPosition;
    }

    public int getID() {
        return this.id;
    }

    public String getComposed() {
        return this.composed;
    }

    public String getCommitted() {
        return this.committed;
    }

    public int[] getAttributes() {
        return Arrays.copyOf(this.attributes, this.attributes.length);
    }

    public int getCaretPosition() {
        return this.caretPosition;
    }
}
