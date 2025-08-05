package org.apache.commons.net.io;

import java.util.EventListener;
import java.util.Iterator;
import org.apache.commons.net.util.ListenerList;

/* loaded from: commons-net-3.6.jar:org/apache/commons/net/io/CopyStreamAdapter.class */
public class CopyStreamAdapter implements CopyStreamListener {
    private final ListenerList internalListeners = new ListenerList();

    @Override // org.apache.commons.net.io.CopyStreamListener
    public void bytesTransferred(CopyStreamEvent event) {
        Iterator i$ = this.internalListeners.iterator();
        while (i$.hasNext()) {
            EventListener listener = i$.next();
            ((CopyStreamListener) listener).bytesTransferred(event);
        }
    }

    @Override // org.apache.commons.net.io.CopyStreamListener
    public void bytesTransferred(long totalBytesTransferred, int bytesTransferred, long streamSize) {
        Iterator i$ = this.internalListeners.iterator();
        while (i$.hasNext()) {
            EventListener listener = i$.next();
            ((CopyStreamListener) listener).bytesTransferred(totalBytesTransferred, bytesTransferred, streamSize);
        }
    }

    public void addCopyStreamListener(CopyStreamListener listener) {
        this.internalListeners.addListener(listener);
    }

    public void removeCopyStreamListener(CopyStreamListener listener) {
        this.internalListeners.removeListener(listener);
    }
}
