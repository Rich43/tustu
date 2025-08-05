package com.sun.javafx.scene.control.behavior;

import javafx.event.EventType;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/OrientedKeyBinding.class */
public abstract class OrientedKeyBinding extends KeyBinding {
    private OptionalBoolean vertical;

    protected abstract boolean getVertical(Control control);

    public OrientedKeyBinding(KeyCode code, String action) {
        super(code, action);
        this.vertical = OptionalBoolean.FALSE;
    }

    public OrientedKeyBinding(KeyCode code, EventType<KeyEvent> type, String action) {
        super(code, type, action);
        this.vertical = OptionalBoolean.FALSE;
    }

    public OrientedKeyBinding vertical() {
        this.vertical = OptionalBoolean.TRUE;
        return this;
    }

    @Override // com.sun.javafx.scene.control.behavior.KeyBinding
    public int getSpecificity(Control control, KeyEvent event) {
        int s2;
        boolean verticalControl = getVertical(control);
        if (this.vertical.equals(verticalControl) && (s2 = super.getSpecificity(control, event)) != 0) {
            return this.vertical != OptionalBoolean.ANY ? s2 + 1 : s2;
        }
        return 0;
    }

    @Override // com.sun.javafx.scene.control.behavior.KeyBinding
    public String toString() {
        return "OrientedKeyBinding [code=" + ((Object) getCode()) + ", shift=" + ((Object) getShift()) + ", ctrl=" + ((Object) getCtrl()) + ", alt=" + ((Object) getAlt()) + ", meta=" + ((Object) getMeta()) + ", type=" + ((Object) getType()) + ", vertical=" + ((Object) this.vertical) + ", action=" + getAction() + "]";
    }
}
