package com.sun.webkit.dom;

import org.w3c.dom.views.AbstractView;

/* loaded from: jfxrt.jar:com/sun/webkit/dom/KeyboardEventImpl.class */
public class KeyboardEventImpl extends UIEventImpl {
    public static final int KEY_LOCATION_STANDARD = 0;
    public static final int KEY_LOCATION_LEFT = 1;
    public static final int KEY_LOCATION_RIGHT = 2;
    public static final int KEY_LOCATION_NUMPAD = 3;

    static native String getKeyIdentifierImpl(long j2);

    static native int getLocationImpl(long j2);

    static native int getKeyLocationImpl(long j2);

    static native boolean getCtrlKeyImpl(long j2);

    static native boolean getShiftKeyImpl(long j2);

    static native boolean getAltKeyImpl(long j2);

    static native boolean getMetaKeyImpl(long j2);

    static native boolean getAltGraphKeyImpl(long j2);

    static native int getKeyCodeImpl(long j2);

    static native int getCharCodeImpl(long j2);

    static native boolean getModifierStateImpl(long j2, String str);

    static native void initKeyboardEventImpl(long j2, String str, boolean z2, boolean z3, long j3, String str2, int i2, boolean z4, boolean z5, boolean z6, boolean z7, boolean z8);

    static native void initKeyboardEventExImpl(long j2, String str, boolean z2, boolean z3, long j3, String str2, int i2, boolean z4, boolean z5, boolean z6, boolean z7);

    KeyboardEventImpl(long peer) {
        super(peer);
    }

    static KeyboardEventImpl getImpl(long peer) {
        return (KeyboardEventImpl) create(peer);
    }

    public String getKeyIdentifier() {
        return getKeyIdentifierImpl(getPeer());
    }

    public int getLocation() {
        return getLocationImpl(getPeer());
    }

    public int getKeyLocation() {
        return getKeyLocationImpl(getPeer());
    }

    public boolean getCtrlKey() {
        return getCtrlKeyImpl(getPeer());
    }

    public boolean getShiftKey() {
        return getShiftKeyImpl(getPeer());
    }

    public boolean getAltKey() {
        return getAltKeyImpl(getPeer());
    }

    public boolean getMetaKey() {
        return getMetaKeyImpl(getPeer());
    }

    public boolean getAltGraphKey() {
        return getAltGraphKeyImpl(getPeer());
    }

    @Override // com.sun.webkit.dom.UIEventImpl
    public int getKeyCode() {
        return getKeyCodeImpl(getPeer());
    }

    @Override // com.sun.webkit.dom.UIEventImpl
    public int getCharCode() {
        return getCharCodeImpl(getPeer());
    }

    public boolean getModifierState(String keyIdentifierArg) {
        return getModifierStateImpl(getPeer(), keyIdentifierArg);
    }

    public void initKeyboardEvent(String type, boolean canBubble, boolean cancelable, AbstractView view, String keyIdentifier, int location, boolean ctrlKey, boolean altKey, boolean shiftKey, boolean metaKey, boolean altGraphKey) {
        initKeyboardEventImpl(getPeer(), type, canBubble, cancelable, DOMWindowImpl.getPeer(view), keyIdentifier, location, ctrlKey, altKey, shiftKey, metaKey, altGraphKey);
    }

    public void initKeyboardEventEx(String type, boolean canBubble, boolean cancelable, AbstractView view, String keyIdentifier, int location, boolean ctrlKey, boolean altKey, boolean shiftKey, boolean metaKey) {
        initKeyboardEventExImpl(getPeer(), type, canBubble, cancelable, DOMWindowImpl.getPeer(view), keyIdentifier, location, ctrlKey, altKey, shiftKey, metaKey);
    }
}
