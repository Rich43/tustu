package sun.net;

import java.util.EventListener;

/* loaded from: rt.jar:sun/net/ProgressListener.class */
public interface ProgressListener extends EventListener {
    void progressStart(ProgressEvent progressEvent);

    void progressUpdate(ProgressEvent progressEvent);

    void progressFinish(ProgressEvent progressEvent);
}
