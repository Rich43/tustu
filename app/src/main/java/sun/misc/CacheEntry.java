package sun.misc;

/* compiled from: Cache.java */
/* loaded from: rt.jar:sun/misc/CacheEntry.class */
class CacheEntry extends Ref {
    int hash;
    Object key;
    CacheEntry next;

    CacheEntry() {
    }

    @Override // sun.misc.Ref
    public Object reconstitute() {
        return null;
    }
}
