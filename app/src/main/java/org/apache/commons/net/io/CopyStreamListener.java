package org.apache.commons.net.io;

import java.util.EventListener;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/io/CopyStreamListener.class */
public interface CopyStreamListener extends EventListener {
    void bytesTransferred(CopyStreamEvent copyStreamEvent);

    void bytesTransferred(long j2, int i2, long j3);
}
