package com.sun.javafx.scene.control.behavior;

import com.sun.javafx.tk.Toolkit;
import com.sun.javafx.util.Utils;
import javafx.event.EventType;
import javafx.scene.control.Control;
import javafx.scene.input.KeyCode;
import javafx.scene.input.KeyEvent;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/KeyBinding.class */
public class KeyBinding {
    private KeyCode code;
    private EventType<KeyEvent> eventType;
    private String action;
    private OptionalBoolean shift;
    private OptionalBoolean ctrl;
    private OptionalBoolean alt;
    private OptionalBoolean meta;

    public KeyBinding(KeyCode code, String action) {
        this.eventType = KeyEvent.KEY_PRESSED;
        this.shift = OptionalBoolean.FALSE;
        this.ctrl = OptionalBoolean.FALSE;
        this.alt = OptionalBoolean.FALSE;
        this.meta = OptionalBoolean.FALSE;
        this.code = code;
        this.action = action;
    }

    public KeyBinding(KeyCode code, EventType<KeyEvent> type, String action) {
        this.eventType = KeyEvent.KEY_PRESSED;
        this.shift = OptionalBoolean.FALSE;
        this.ctrl = OptionalBoolean.FALSE;
        this.alt = OptionalBoolean.FALSE;
        this.meta = OptionalBoolean.FALSE;
        this.code = code;
        this.eventType = type;
        this.action = action;
    }

    public KeyBinding shift() {
        return shift(OptionalBoolean.TRUE);
    }

    public KeyBinding shift(OptionalBoolean value) {
        this.shift = value;
        return this;
    }

    public KeyBinding ctrl() {
        return ctrl(OptionalBoolean.TRUE);
    }

    public KeyBinding ctrl(OptionalBoolean value) {
        this.ctrl = value;
        return this;
    }

    public KeyBinding alt() {
        return alt(OptionalBoolean.TRUE);
    }

    public KeyBinding alt(OptionalBoolean value) {
        this.alt = value;
        return this;
    }

    public KeyBinding meta() {
        return meta(OptionalBoolean.TRUE);
    }

    public KeyBinding meta(OptionalBoolean value) {
        this.meta = value;
        return this;
    }

    public KeyBinding shortcut() {
        if (Toolkit.getToolkit().getClass().getName().endsWith("StubToolkit")) {
            if (Utils.isMac()) {
                return meta();
            }
            return ctrl();
        }
        switch (Toolkit.getToolkit().getPlatformShortcutKey()) {
            case SHIFT:
                return shift();
            case CONTROL:
                return ctrl();
            case ALT:
                return alt();
            case META:
                return meta();
            default:
                return this;
        }
    }

    public final KeyCode getCode() {
        return this.code;
    }

    public final EventType<KeyEvent> getType() {
        return this.eventType;
    }

    public final String getAction() {
        return this.action;
    }

    public final OptionalBoolean getShift() {
        return this.shift;
    }

    public final OptionalBoolean getCtrl() {
        return this.ctrl;
    }

    public final OptionalBoolean getAlt() {
        return this.alt;
    }

    public final OptionalBoolean getMeta() {
        return this.meta;
    }

    public int getSpecificity(Control control, KeyEvent event) {
        if (this.code != null && this.code != event.getCode()) {
            return 0;
        }
        int s2 = 1;
        if (!this.shift.equals(event.isShiftDown())) {
            return 0;
        }
        if (this.shift != OptionalBoolean.ANY) {
            s2 = 1 + 1;
        }
        if (!this.ctrl.equals(event.isControlDown())) {
            return 0;
        }
        if (this.ctrl != OptionalBoolean.ANY) {
            s2++;
        }
        if (!this.alt.equals(event.isAltDown())) {
            return 0;
        }
        if (this.alt != OptionalBoolean.ANY) {
            s2++;
        }
        if (!this.meta.equals(event.isMetaDown())) {
            return 0;
        }
        if (this.meta != OptionalBoolean.ANY) {
            s2++;
        }
        if (this.eventType == null || this.eventType == event.getEventType()) {
            return s2 + 1;
        }
        return 0;
    }

    public String toString() {
        return "KeyBinding [code=" + ((Object) this.code) + ", shift=" + ((Object) this.shift) + ", ctrl=" + ((Object) this.ctrl) + ", alt=" + ((Object) this.alt) + ", meta=" + ((Object) this.meta) + ", type=" + ((Object) this.eventType) + ", action=" + this.action + "]";
    }
}
