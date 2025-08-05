package java.awt;

import java.awt.EventFilter;
import java.util.ArrayList;
import sun.awt.EventQueueDelegate;
import sun.awt.ModalExclude;
import sun.awt.SunToolkit;
import sun.awt.dnd.SunDragSourceContextPeer;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/awt/EventDispatchThread.class */
class EventDispatchThread extends Thread {
    private static final PlatformLogger eventLog = PlatformLogger.getLogger("java.awt.event.EventDispatchThread");
    private EventQueue theQueue;
    private volatile boolean doDispatch;
    private static final int ANY_EVENT = -1;
    private ArrayList<EventFilter> eventFilters;

    EventDispatchThread(ThreadGroup threadGroup, String str, EventQueue eventQueue) {
        super(threadGroup, str);
        this.doDispatch = true;
        this.eventFilters = new ArrayList<>();
        setEventQueue(eventQueue);
    }

    public void stopDispatching() {
        this.doDispatch = false;
    }

    @Override // java.lang.Thread, java.lang.Runnable
    public void run() {
        try {
            pumpEvents(new Conditional() { // from class: java.awt.EventDispatchThread.1
                @Override // java.awt.Conditional
                public boolean evaluate() {
                    return true;
                }
            });
        } finally {
            getEventQueue().detachDispatchThread(this);
        }
    }

    void pumpEvents(Conditional conditional) {
        pumpEvents(-1, conditional);
    }

    void pumpEventsForHierarchy(Conditional conditional, Component component) {
        pumpEventsForHierarchy(-1, conditional, component);
    }

    void pumpEvents(int i2, Conditional conditional) {
        pumpEventsForHierarchy(i2, conditional, null);
    }

    void pumpEventsForHierarchy(int i2, Conditional conditional, Component component) {
        pumpEventsForFilter(i2, conditional, new HierarchyEventFilter(component));
    }

    void pumpEventsForFilter(Conditional conditional, EventFilter eventFilter) {
        pumpEventsForFilter(-1, conditional, eventFilter);
    }

    void pumpEventsForFilter(int i2, Conditional conditional, EventFilter eventFilter) {
        addEventFilter(eventFilter);
        this.doDispatch = true;
        while (this.doDispatch && !isInterrupted() && conditional.evaluate()) {
            pumpOneEventForFilters(i2);
        }
        removeEventFilter(eventFilter);
    }

    void addEventFilter(EventFilter eventFilter) {
        if (eventLog.isLoggable(PlatformLogger.Level.FINEST)) {
            eventLog.finest("adding the event filter: " + ((Object) eventFilter));
        }
        synchronized (this.eventFilters) {
            if (!this.eventFilters.contains(eventFilter)) {
                if (eventFilter instanceof ModalEventFilter) {
                    ModalEventFilter modalEventFilter = (ModalEventFilter) eventFilter;
                    int i2 = 0;
                    while (i2 < this.eventFilters.size()) {
                        EventFilter eventFilter2 = this.eventFilters.get(i2);
                        if ((eventFilter2 instanceof ModalEventFilter) && ((ModalEventFilter) eventFilter2).compareTo(modalEventFilter) > 0) {
                            break;
                        } else {
                            i2++;
                        }
                    }
                    this.eventFilters.add(i2, eventFilter);
                } else {
                    this.eventFilters.add(eventFilter);
                }
            }
        }
    }

    void removeEventFilter(EventFilter eventFilter) {
        if (eventLog.isLoggable(PlatformLogger.Level.FINEST)) {
            eventLog.finest("removing the event filter: " + ((Object) eventFilter));
        }
        synchronized (this.eventFilters) {
            this.eventFilters.remove(eventFilter);
        }
    }

    boolean filterAndCheckEvent(AWTEvent aWTEvent) {
        boolean z2 = true;
        synchronized (this.eventFilters) {
            int size = this.eventFilters.size() - 1;
            while (true) {
                if (size < 0) {
                    break;
                }
                EventFilter.FilterAction filterActionAcceptEvent = this.eventFilters.get(size).acceptEvent(aWTEvent);
                if (filterActionAcceptEvent == EventFilter.FilterAction.REJECT) {
                    z2 = false;
                    break;
                }
                if (filterActionAcceptEvent == EventFilter.FilterAction.ACCEPT_IMMEDIATELY) {
                    break;
                }
                size--;
            }
        }
        return z2 && SunDragSourceContextPeer.checkEvent(aWTEvent);
    }

    void pumpOneEventForFilters(int i2) {
        EventQueue eventQueue;
        EventQueueDelegate.Delegate delegate;
        AWTEvent nextEvent;
        boolean zFilterAndCheckEvent;
        do {
            try {
                eventQueue = getEventQueue();
                delegate = EventQueueDelegate.getDelegate();
                if (delegate != null && i2 == -1) {
                    nextEvent = delegate.getNextEvent(eventQueue);
                } else {
                    nextEvent = i2 == -1 ? eventQueue.getNextEvent() : eventQueue.getNextEvent(i2);
                }
                zFilterAndCheckEvent = filterAndCheckEvent(nextEvent);
                if (!zFilterAndCheckEvent) {
                    nextEvent.consume();
                }
            } catch (InterruptedException e2) {
                this.doDispatch = false;
                return;
            } catch (ThreadDeath e3) {
                this.doDispatch = false;
                throw e3;
            } catch (Throwable th) {
                processException(th);
                return;
            }
        } while (!zFilterAndCheckEvent);
        if (eventLog.isLoggable(PlatformLogger.Level.FINEST)) {
            eventLog.finest("Dispatching: " + ((Object) nextEvent));
        }
        Object objBeforeDispatch = null;
        if (delegate != null) {
            objBeforeDispatch = delegate.beforeDispatch(nextEvent);
        }
        eventQueue.dispatchEvent(nextEvent);
        if (delegate != null) {
            delegate.afterDispatch(nextEvent, objBeforeDispatch);
        }
    }

    private void processException(Throwable th) {
        if (eventLog.isLoggable(PlatformLogger.Level.FINE)) {
            eventLog.fine("Processing exception: " + ((Object) th));
        }
        getUncaughtExceptionHandler().uncaughtException(this, th);
    }

    public synchronized EventQueue getEventQueue() {
        return this.theQueue;
    }

    public synchronized void setEventQueue(EventQueue eventQueue) {
        this.theQueue = eventQueue;
    }

    /* loaded from: rt.jar:java/awt/EventDispatchThread$HierarchyEventFilter.class */
    private static class HierarchyEventFilter implements EventFilter {
        private Component modalComponent;

        public HierarchyEventFilter(Component component) {
            this.modalComponent = component;
        }

        @Override // java.awt.EventFilter
        public EventFilter.FilterAction acceptEvent(AWTEvent aWTEvent) {
            if (this.modalComponent != null) {
                int id = aWTEvent.getID();
                boolean z2 = id >= 500 && id <= 507;
                boolean z3 = id >= 1001 && id <= 1001;
                boolean z4 = id == 201;
                if (Component.isInstanceOf(this.modalComponent, "javax.swing.JInternalFrame")) {
                    return z4 ? EventFilter.FilterAction.REJECT : EventFilter.FilterAction.ACCEPT;
                }
                if (z2 || z3 || z4) {
                    Object source = aWTEvent.getSource();
                    if (source instanceof ModalExclude) {
                        return EventFilter.FilterAction.ACCEPT;
                    }
                    if (source instanceof Component) {
                        Component parent = (Component) source;
                        boolean z5 = false;
                        if (this.modalComponent instanceof Container) {
                            while (true) {
                                if (parent != this.modalComponent && parent != null) {
                                    if ((parent instanceof Window) && SunToolkit.isModalExcluded((Window) parent)) {
                                        z5 = true;
                                        break;
                                    }
                                    parent = parent.getParent();
                                } else {
                                    break;
                                }
                            }
                        }
                        if (!z5 && parent != this.modalComponent) {
                            return EventFilter.FilterAction.REJECT;
                        }
                    }
                }
            }
            return EventFilter.FilterAction.ACCEPT;
        }
    }
}
