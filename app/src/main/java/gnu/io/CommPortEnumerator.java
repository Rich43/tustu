package gnu.io;

import java.util.Enumeration;

/* loaded from: RXTXcomm.jar:gnu/io/CommPortEnumerator.class */
class CommPortEnumerator implements Enumeration {
    private CommPortIdentifier index;
    private static final boolean debug = false;

    CommPortEnumerator() {
    }

    @Override // java.util.Enumeration
    public Object nextElement() {
        CommPortIdentifier commPortIdentifier;
        synchronized (CommPortIdentifier.Sync) {
            if (this.index != null) {
                this.index = this.index.next;
            } else {
                this.index = CommPortIdentifier.CommPortIndex;
            }
            commPortIdentifier = this.index;
        }
        return commPortIdentifier;
    }

    @Override // java.util.Enumeration
    public boolean hasMoreElements() {
        synchronized (CommPortIdentifier.Sync) {
            if (this.index != null) {
                return this.index.next != null;
            }
            return CommPortIdentifier.CommPortIndex != null;
        }
    }
}
