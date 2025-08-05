package java.awt.event;

import java.awt.AWTEvent;
import java.awt.ActiveEvent;
import sun.awt.AWTAccessor;

/* loaded from: rt.jar:java/awt/event/InvocationEvent.class */
public class InvocationEvent extends AWTEvent implements ActiveEvent {
    public static final int INVOCATION_FIRST = 1200;
    public static final int INVOCATION_DEFAULT = 1200;
    public static final int INVOCATION_LAST = 1200;
    protected Runnable runnable;
    protected volatile Object notifier;
    private final Runnable listener;
    private volatile boolean dispatched;
    protected boolean catchExceptions;
    private Exception exception;
    private Throwable throwable;
    private long when;
    private static final long serialVersionUID = 436056344909459450L;

    static {
        AWTAccessor.setInvocationEventAccessor(new AWTAccessor.InvocationEventAccessor() { // from class: java.awt.event.InvocationEvent.1
            @Override // sun.awt.AWTAccessor.InvocationEventAccessor
            public void dispose(InvocationEvent invocationEvent) {
                invocationEvent.finishedDispatching(false);
            }
        });
    }

    public InvocationEvent(Object obj, Runnable runnable) {
        this(obj, 1200, runnable, null, null, false);
    }

    public InvocationEvent(Object obj, Runnable runnable, Object obj2, boolean z2) {
        this(obj, 1200, runnable, obj2, null, z2);
    }

    public InvocationEvent(Object obj, Runnable runnable, Runnable runnable2, boolean z2) {
        this(obj, 1200, runnable, null, runnable2, z2);
    }

    protected InvocationEvent(Object obj, int i2, Runnable runnable, Object obj2, boolean z2) {
        this(obj, i2, runnable, obj2, null, z2);
    }

    private InvocationEvent(Object obj, int i2, Runnable runnable, Object obj2, Runnable runnable2, boolean z2) {
        super(obj, i2);
        this.dispatched = false;
        this.exception = null;
        this.throwable = null;
        this.runnable = runnable;
        this.notifier = obj2;
        this.listener = runnable2;
        this.catchExceptions = z2;
        this.when = System.currentTimeMillis();
    }

    @Override // java.awt.ActiveEvent
    public void dispatch() {
        try {
            if (this.catchExceptions) {
                try {
                    this.runnable.run();
                } catch (Throwable th) {
                    if (th instanceof Exception) {
                        this.exception = (Exception) th;
                    }
                    this.throwable = th;
                }
            } else {
                this.runnable.run();
            }
        } finally {
            finishedDispatching(true);
        }
    }

    public Exception getException() {
        if (this.catchExceptions) {
            return this.exception;
        }
        return null;
    }

    public Throwable getThrowable() {
        if (this.catchExceptions) {
            return this.throwable;
        }
        return null;
    }

    public long getWhen() {
        return this.when;
    }

    public boolean isDispatched() {
        return this.dispatched;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void finishedDispatching(boolean z2) {
        this.dispatched = z2;
        if (this.notifier != null) {
            synchronized (this.notifier) {
                this.notifier.notifyAll();
            }
        }
        if (this.listener != null) {
            this.listener.run();
        }
    }

    @Override // java.awt.AWTEvent
    public String paramString() {
        String str;
        switch (this.id) {
            case 1200:
                str = "INVOCATION_DEFAULT";
                break;
            default:
                str = "unknown type";
                break;
        }
        return str + ",runnable=" + ((Object) this.runnable) + ",notifier=" + this.notifier + ",catchExceptions=" + this.catchExceptions + ",when=" + this.when;
    }
}
