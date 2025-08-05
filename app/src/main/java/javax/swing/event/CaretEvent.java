package javax.swing.event;

import java.util.EventObject;

/* loaded from: rt.jar:javax/swing/event/CaretEvent.class */
public abstract class CaretEvent extends EventObject {
    public abstract int getDot();

    public abstract int getMark();

    public CaretEvent(Object obj) {
        super(obj);
    }
}
