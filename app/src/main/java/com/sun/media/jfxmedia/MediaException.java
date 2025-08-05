package com.sun.media.jfxmedia;

/* loaded from: jfxrt.jar:com/sun/media/jfxmedia/MediaException.class */
public class MediaException extends RuntimeException {
    private static final long serialVersionUID = 14;
    private MediaError error;

    public MediaException(String message) {
        super(message);
        this.error = null;
    }

    public MediaException(String message, Throwable cause) {
        super(message, cause);
        this.error = null;
    }

    public MediaException(String message, Throwable cause, MediaError error) {
        super(message, cause);
        this.error = null;
        this.error = error;
    }

    public MediaError getMediaError() {
        return this.error;
    }
}
