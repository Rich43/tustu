package sun.applet;

import java.util.EventObject;

/* loaded from: rt.jar:sun/applet/AppletEvent.class */
public class AppletEvent extends EventObject {
    private Object arg;
    private int id;

    public AppletEvent(Object obj, int i2, Object obj2) {
        super(obj);
        this.arg = obj2;
        this.id = i2;
    }

    public int getID() {
        return this.id;
    }

    public Object getArgument() {
        return this.arg;
    }

    @Override // java.util.EventObject
    public String toString() {
        String str = getClass().getName() + "[source=" + this.source + " + id=" + this.id;
        if (this.arg != null) {
            str = str + " + arg=" + this.arg;
        }
        return str + " ]";
    }
}
