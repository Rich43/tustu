package com.sun.javafx.iio;

import java.io.IOException;

/* loaded from: jfxrt.jar:com/sun/javafx/iio/ImageStorageException.class */
public class ImageStorageException extends IOException {
    private static final long serialVersionUID = 1;

    public ImageStorageException(String message) {
        super(message);
    }

    public ImageStorageException(String message, Throwable cause) {
        super(message);
        initCause(cause);
    }
}
