package javax.imageio;

import java.io.IOException;

/* loaded from: rt.jar:javax/imageio/IIOException.class */
public class IIOException extends IOException {
    public IIOException(String str) {
        super(str);
    }

    public IIOException(String str, Throwable th) {
        super(str);
        initCause(th);
    }
}
