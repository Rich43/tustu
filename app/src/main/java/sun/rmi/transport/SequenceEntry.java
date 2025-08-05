package sun.rmi.transport;

/* compiled from: Target.java */
/* loaded from: rt.jar:sun/rmi/transport/SequenceEntry.class */
class SequenceEntry {
    long sequenceNum;
    boolean keep = false;

    SequenceEntry(long j2) {
        this.sequenceNum = j2;
    }

    void retain(long j2) {
        this.sequenceNum = j2;
        this.keep = true;
    }

    void update(long j2) {
        this.sequenceNum = j2;
    }
}
