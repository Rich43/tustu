package sun.rmi.transport;

import java.lang.ref.ReferenceQueue;
import java.lang.ref.WeakReference;
import sun.rmi.runtime.Log;

/* loaded from: rt.jar:sun/rmi/transport/WeakRef.class */
class WeakRef extends WeakReference<Object> {
    private int hashValue;
    private Object strongRef;

    public WeakRef(Object obj) {
        super(obj);
        this.strongRef = null;
        setHashValue(obj);
    }

    public WeakRef(Object obj, ReferenceQueue<Object> referenceQueue) {
        super(obj, referenceQueue);
        this.strongRef = null;
        setHashValue(obj);
    }

    public synchronized void pin() {
        if (this.strongRef == null) {
            this.strongRef = get();
            if (DGCImpl.dgcLog.isLoggable(Log.VERBOSE)) {
                DGCImpl.dgcLog.log(Log.VERBOSE, "strongRef = " + this.strongRef);
            }
        }
    }

    public synchronized void unpin() {
        if (this.strongRef != null) {
            if (DGCImpl.dgcLog.isLoggable(Log.VERBOSE)) {
                DGCImpl.dgcLog.log(Log.VERBOSE, "strongRef = " + this.strongRef);
            }
            this.strongRef = null;
        }
    }

    private void setHashValue(Object obj) {
        if (obj != null) {
            this.hashValue = System.identityHashCode(obj);
        } else {
            this.hashValue = 0;
        }
    }

    public int hashCode() {
        return this.hashValue;
    }

    public boolean equals(Object obj) {
        if (obj instanceof WeakRef) {
            if (obj == this) {
                return true;
            }
            Object obj2 = get();
            return obj2 != null && obj2 == ((WeakRef) obj).get();
        }
        return false;
    }
}
