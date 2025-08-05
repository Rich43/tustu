package sun.awt;

/* compiled from: AppContext.java */
/* loaded from: rt.jar:sun/awt/MostRecentKeyValue.class */
final class MostRecentKeyValue {
    Object key;
    Object value;

    MostRecentKeyValue(Object obj, Object obj2) {
        this.key = obj;
        this.value = obj2;
    }

    void setPair(Object obj, Object obj2) {
        this.key = obj;
        this.value = obj2;
    }
}
