package java.beans;

/* loaded from: rt.jar:java/beans/IndexedPropertyChangeEvent.class */
public class IndexedPropertyChangeEvent extends PropertyChangeEvent {
    private static final long serialVersionUID = -320227448495806870L;
    private int index;

    public IndexedPropertyChangeEvent(Object obj, String str, Object obj2, Object obj3, int i2) {
        super(obj, str, obj2, obj3);
        this.index = i2;
    }

    public int getIndex() {
        return this.index;
    }

    @Override // java.beans.PropertyChangeEvent
    void appendTo(StringBuilder sb) {
        sb.append("; index=").append(getIndex());
    }
}
