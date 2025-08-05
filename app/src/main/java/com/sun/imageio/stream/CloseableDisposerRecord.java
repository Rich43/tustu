package com.sun.imageio.stream;

import java.io.Closeable;
import java.io.IOException;
import sun.java2d.DisposerRecord;

/* loaded from: rt.jar:com/sun/imageio/stream/CloseableDisposerRecord.class */
public class CloseableDisposerRecord implements DisposerRecord {
    private Closeable closeable;

    public CloseableDisposerRecord(Closeable closeable) {
        this.closeable = closeable;
    }

    @Override // sun.java2d.DisposerRecord
    public synchronized void dispose() {
        if (this.closeable != null) {
            try {
                this.closeable.close();
            } catch (IOException e2) {
            } finally {
                this.closeable = null;
            }
        }
    }
}
