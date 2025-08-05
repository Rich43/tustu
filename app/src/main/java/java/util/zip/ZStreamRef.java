package java.util.zip;

/* loaded from: rt.jar:java/util/zip/ZStreamRef.class */
class ZStreamRef {
    private volatile long address;

    ZStreamRef(long j2) {
        this.address = j2;
    }

    long address() {
        return this.address;
    }

    void clear() {
        this.address = 0L;
    }
}
