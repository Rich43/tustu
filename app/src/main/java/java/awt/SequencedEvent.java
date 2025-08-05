package java.awt;

import java.awt.EventFilter;
import java.util.Iterator;
import java.util.LinkedList;
import sun.awt.AWTAccessor;
import sun.awt.AppContext;
import sun.awt.SunToolkit;

/* loaded from: rt.jar:java/awt/SequencedEvent.class */
class SequencedEvent extends AWTEvent implements ActiveEvent {
    private static final long serialVersionUID = 547742659238625067L;
    private static final int ID = 1006;
    private static final LinkedList<SequencedEvent> list = new LinkedList<>();
    private final AWTEvent nested;
    private AppContext appContext;
    private boolean disposed;
    private final LinkedList<AWTEvent> pendingEvents;

    static {
        AWTAccessor.setSequencedEventAccessor(new AWTAccessor.SequencedEventAccessor() { // from class: java.awt.SequencedEvent.1
            @Override // sun.awt.AWTAccessor.SequencedEventAccessor
            public AWTEvent getNested(AWTEvent aWTEvent) {
                return ((SequencedEvent) aWTEvent).nested;
            }

            @Override // sun.awt.AWTAccessor.SequencedEventAccessor
            public boolean isSequencedEvent(AWTEvent aWTEvent) {
                return aWTEvent instanceof SequencedEvent;
            }
        });
    }

    /* loaded from: rt.jar:java/awt/SequencedEvent$SequencedEventsFilter.class */
    private static final class SequencedEventsFilter implements EventFilter {
        private final SequencedEvent currentSequencedEvent;

        private SequencedEventsFilter(SequencedEvent sequencedEvent) {
            this.currentSequencedEvent = sequencedEvent;
        }

        @Override // java.awt.EventFilter
        public EventFilter.FilterAction acceptEvent(AWTEvent aWTEvent) {
            if (aWTEvent.getID() == 1006) {
                synchronized (SequencedEvent.class) {
                    Iterator<E> it = SequencedEvent.list.iterator();
                    while (it.hasNext()) {
                        SequencedEvent sequencedEvent = (SequencedEvent) it.next();
                        if (sequencedEvent.equals(this.currentSequencedEvent)) {
                            break;
                        }
                        if (sequencedEvent.equals(aWTEvent)) {
                            return EventFilter.FilterAction.ACCEPT;
                        }
                    }
                }
            } else if (aWTEvent.getID() == 1007) {
                return EventFilter.FilterAction.ACCEPT;
            }
            this.currentSequencedEvent.pendingEvents.add(aWTEvent);
            return EventFilter.FilterAction.REJECT;
        }
    }

    public SequencedEvent(AWTEvent aWTEvent) {
        super(aWTEvent.getSource(), 1006);
        this.pendingEvents = new LinkedList<>();
        this.nested = aWTEvent;
        SunToolkit.setSystemGenerated(aWTEvent);
        synchronized (SequencedEvent.class) {
            list.add(this);
        }
    }

    @Override // java.awt.ActiveEvent
    public final void dispatch() {
        try {
            this.appContext = AppContext.getAppContext();
            if (getFirst() != this) {
                if (EventQueue.isDispatchThread()) {
                    ((EventDispatchThread) Thread.currentThread()).pumpEventsForFilter(() -> {
                        return !isFirstOrDisposed();
                    }, new SequencedEventsFilter());
                } else {
                    while (!isFirstOrDisposed()) {
                        synchronized (SequencedEvent.class) {
                            try {
                                SequencedEvent.class.wait(1000L);
                            } catch (InterruptedException e2) {
                            }
                        }
                    }
                }
            }
            if (!this.disposed) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().setCurrentSequencedEvent(this);
                Toolkit.getEventQueue().dispatchEvent(this.nested);
            }
        } finally {
            dispose();
        }
    }

    private static final boolean isOwnerAppContextDisposed(SequencedEvent sequencedEvent) {
        if (sequencedEvent != null) {
            Object source = sequencedEvent.nested.getSource();
            if (source instanceof Component) {
                return ((Component) source).appContext.isDisposed();
            }
            return false;
        }
        return false;
    }

    public final boolean isFirstOrDisposed() {
        return this.disposed || this == getFirstWithContext() || this.disposed;
    }

    private static final synchronized SequencedEvent getFirst() {
        return list.getFirst();
    }

    private static final SequencedEvent getFirstWithContext() {
        SequencedEvent first = getFirst();
        while (true) {
            SequencedEvent sequencedEvent = first;
            if (isOwnerAppContextDisposed(sequencedEvent)) {
                sequencedEvent.dispose();
                first = getFirst();
            } else {
                return sequencedEvent;
            }
        }
    }

    final void dispose() {
        synchronized (SequencedEvent.class) {
            if (this.disposed) {
                return;
            }
            if (KeyboardFocusManager.getCurrentKeyboardFocusManager().getCurrentSequencedEvent() == this) {
                KeyboardFocusManager.getCurrentKeyboardFocusManager().setCurrentSequencedEvent(null);
            }
            this.disposed = true;
            SequencedEvent first = null;
            synchronized (SequencedEvent.class) {
                SequencedEvent.class.notifyAll();
                if (list.getFirst() == this) {
                    list.removeFirst();
                    if (!list.isEmpty()) {
                        first = list.getFirst();
                    }
                } else {
                    list.remove(this);
                }
            }
            if (first != null && first.appContext != null) {
                SunToolkit.postEvent(first.appContext, new SentEvent());
            }
            Iterator<AWTEvent> it = this.pendingEvents.iterator();
            while (it.hasNext()) {
                SunToolkit.postEvent(this.appContext, it.next());
            }
        }
    }
}
