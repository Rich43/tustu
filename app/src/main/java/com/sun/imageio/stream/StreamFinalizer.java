package com.sun.imageio.stream;

import java.io.IOException;
import javax.imageio.stream.ImageInputStream;

/* loaded from: rt.jar:com/sun/imageio/stream/StreamFinalizer.class */
public class StreamFinalizer {
    private ImageInputStream stream;

    public StreamFinalizer(ImageInputStream imageInputStream) {
        this.stream = imageInputStream;
    }

    protected void finalize() throws Throwable {
        try {
            this.stream.close();
        } catch (IOException e2) {
        } finally {
            this.stream = null;
            super.finalize();
        }
    }
}
