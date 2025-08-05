package com.sun.nio.file;

import java.nio.file.WatchEvent;

/* loaded from: rt.jar:com/sun/nio/file/SensitivityWatchEventModifier.class */
public enum SensitivityWatchEventModifier implements WatchEvent.Modifier {
    HIGH(2),
    MEDIUM(10),
    LOW(30);

    private final int sensitivity;

    public int sensitivityValueInSeconds() {
        return this.sensitivity;
    }

    SensitivityWatchEventModifier(int i2) {
        this.sensitivity = i2;
    }
}
