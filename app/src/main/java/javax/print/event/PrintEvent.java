package javax.print.event;

import java.util.EventObject;

/* loaded from: rt.jar:javax/print/event/PrintEvent.class */
public class PrintEvent extends EventObject {
    private static final long serialVersionUID = 2286914924430763847L;

    public PrintEvent(Object obj) {
        super(obj);
    }

    @Override // java.util.EventObject
    public String toString() {
        return "PrintEvent on " + getSource().toString();
    }
}
