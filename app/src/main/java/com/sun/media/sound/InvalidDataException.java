package com.sun.media.sound;

import java.io.IOException;

/* loaded from: rt.jar:com/sun/media/sound/InvalidDataException.class */
public class InvalidDataException extends IOException {
    private static final long serialVersionUID = 1;

    public InvalidDataException() {
        super("Invalid Data!");
    }

    public InvalidDataException(String str) {
        super(str);
    }
}
