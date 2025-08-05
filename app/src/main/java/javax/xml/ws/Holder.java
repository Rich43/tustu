package javax.xml.ws;

import java.io.Serializable;

/* loaded from: rt.jar:javax/xml/ws/Holder.class */
public final class Holder<T> implements Serializable {
    private static final long serialVersionUID = 2623699057546497185L;
    public T value;

    public Holder() {
    }

    public Holder(T value) {
        this.value = value;
    }
}
