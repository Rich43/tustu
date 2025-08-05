package org.apache.commons.net.io;

import java.util.EventObject;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/io/CopyStreamEvent.class */
public class CopyStreamEvent extends EventObject {
    private static final long serialVersionUID = -964927635655051867L;
    public static final long UNKNOWN_STREAM_SIZE = -1;
    private final int bytesTransferred;
    private final long totalBytesTransferred;
    private final long streamSize;

    public CopyStreamEvent(Object source, long totalBytesTransferred, int bytesTransferred, long streamSize) {
        super(source);
        this.bytesTransferred = bytesTransferred;
        this.totalBytesTransferred = totalBytesTransferred;
        this.streamSize = streamSize;
    }

    public int getBytesTransferred() {
        return this.bytesTransferred;
    }

    public long getTotalBytesTransferred() {
        return this.totalBytesTransferred;
    }

    public long getStreamSize() {
        return this.streamSize;
    }

    @Override // java.util.EventObject
    public String toString() {
        return getClass().getName() + "[source=" + this.source + ", total=" + this.totalBytesTransferred + ", bytes=" + this.bytesTransferred + ", size=" + this.streamSize + "]";
    }
}
