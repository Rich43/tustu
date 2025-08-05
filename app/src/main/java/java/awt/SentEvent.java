package java.awt;

import sun.awt.AppContext;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:java/awt/SentEvent.class */
class SentEvent extends AWTEvent implements ActiveEvent {
    private static final long serialVersionUID = -383615247028828931L;
    static final int ID = 1007;
    boolean dispatched;
    private AWTEvent nested;
    private AppContext toNotify;

    SentEvent() {
        this(null);
    }

    SentEvent(AWTEvent aWTEvent) {
        this(aWTEvent, null);
    }

    /* JADX WARN: Illegal instructions before constructor call */
    SentEvent(AWTEvent aWTEvent, AppContext appContext) {
        Object defaultToolkit;
        if (aWTEvent != null) {
            defaultToolkit = aWTEvent.getSource();
        } else {
            defaultToolkit = Toolkit.getDefaultToolkit();
        }
        super(defaultToolkit, 1007);
        this.nested = aWTEvent;
        this.toNotify = appContext;
    }

    public void dispatch() {
        try {
            if (this.nested != null) {
                Toolkit.getEventQueue().dispatchEvent(this.nested);
            }
            this.dispatched = true;
            if (this.toNotify != null) {
                SunToolkit.postEvent(this.toNotify, new SentEvent());
            }
            synchronized (this) {
                notifyAll();
            }
        } catch (Throwable th) {
            this.dispatched = true;
            if (this.toNotify != null) {
                SunToolkit.postEvent(this.toNotify, new SentEvent());
            }
            synchronized (this) {
                notifyAll();
                throw th;
            }
        }
    }

    final void dispose() {
        this.dispatched = true;
        if (this.toNotify != null) {
            SunToolkit.postEvent(this.toNotify, new SentEvent());
        }
        synchronized (this) {
            notifyAll();
        }
    }
}
