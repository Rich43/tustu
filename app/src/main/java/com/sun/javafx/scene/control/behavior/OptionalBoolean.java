package com.sun.javafx.scene.control.behavior;

/* loaded from: jfxrt.jar:com/sun/javafx/scene/control/behavior/OptionalBoolean.class */
public enum OptionalBoolean {
    TRUE,
    FALSE,
    ANY;

    public boolean equals(boolean b2) {
        if (this == ANY) {
            return true;
        }
        if (b2 && this == TRUE) {
            return true;
        }
        return !b2 && this == FALSE;
    }
}
