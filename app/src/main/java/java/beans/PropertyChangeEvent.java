package java.beans;

import java.util.EventObject;

/* loaded from: rt.jar:java/beans/PropertyChangeEvent.class */
public class PropertyChangeEvent extends EventObject {
    private static final long serialVersionUID = 7042693688939648123L;
    private String propertyName;
    private Object newValue;
    private Object oldValue;
    private Object propagationId;

    public PropertyChangeEvent(Object obj, String str, Object obj2, Object obj3) {
        super(obj);
        this.propertyName = str;
        this.newValue = obj3;
        this.oldValue = obj2;
    }

    public String getPropertyName() {
        return this.propertyName;
    }

    public Object getNewValue() {
        return this.newValue;
    }

    public Object getOldValue() {
        return this.oldValue;
    }

    public void setPropagationId(Object obj) {
        this.propagationId = obj;
    }

    public Object getPropagationId() {
        return this.propagationId;
    }

    @Override // java.util.EventObject
    public String toString() {
        StringBuilder sb = new StringBuilder(getClass().getName());
        sb.append("[propertyName=").append(getPropertyName());
        appendTo(sb);
        sb.append("; oldValue=").append(getOldValue());
        sb.append("; newValue=").append(getNewValue());
        sb.append("; propagationId=").append(getPropagationId());
        sb.append("; source=").append(getSource());
        return sb.append("]").toString();
    }

    void appendTo(StringBuilder sb) {
    }
}
