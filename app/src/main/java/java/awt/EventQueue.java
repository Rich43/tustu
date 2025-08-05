package java.awt;

import java.awt.event.ActionEvent;
import java.awt.event.FocusEvent;
import java.awt.event.InputEvent;
import java.awt.event.InputMethodEvent;
import java.awt.event.InvocationEvent;
import java.awt.event.KeyEvent;
import java.awt.event.MouseEvent;
import java.awt.event.PaintEvent;
import java.awt.event.WindowEvent;
import java.awt.peer.ComponentPeer;
import java.lang.ref.WeakReference;
import java.lang.reflect.InvocationTargetException;
import java.security.AccessControlContext;
import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.EmptyStackException;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.Lock;
import sun.awt.AWTAccessor;
import sun.awt.AWTAutoShutdown;
import sun.awt.AppContext;
import sun.awt.EventQueueItem;
import sun.awt.FwDispatcher;
import sun.awt.PeerEvent;
import sun.awt.SunToolkit;
import sun.awt.dnd.SunDropTargetEvent;
import sun.misc.JavaSecurityAccess;
import sun.misc.SharedSecrets;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/awt/EventQueue.class */
public class EventQueue {
    private static final int LOW_PRIORITY = 0;
    private static final int NORM_PRIORITY = 1;
    private static final int HIGH_PRIORITY = 2;
    private static final int ULTIMATE_PRIORITY = 3;
    private static final int NUM_PRIORITIES = 4;
    private EventQueue nextQueue;
    private EventQueue previousQueue;
    private final Lock pushPopLock;
    private final Condition pushPopCond;
    private EventDispatchThread dispatchThread;
    private WeakReference<AWTEvent> currentEvent;
    private volatile int waitForID;
    private final AppContext appContext;
    private FwDispatcher fwDispatcher;
    private static volatile PlatformLogger eventLog;
    private static final int PAINT = 0;
    private static final int UPDATE = 1;
    private static final int MOVE = 2;
    private static final int DRAG = 3;
    private static final int PEER = 4;
    private static final int CACHE_LENGTH = 5;
    private static final JavaSecurityAccess javaSecurityAccess;
    private static final AtomicInteger threadInitNumber = new AtomicInteger(0);
    private static final Runnable dummyRunnable = new Runnable() { // from class: java.awt.EventQueue.1
        @Override // java.lang.Runnable
        public void run() {
        }
    };
    private Queue[] queues = new Queue[4];
    private final ThreadGroup threadGroup = Thread.currentThread().getThreadGroup();
    private final ClassLoader classLoader = Thread.currentThread().getContextClassLoader();
    private long mostRecentEventTime = System.currentTimeMillis();
    private long mostRecentKeyEventTime = System.currentTimeMillis();
    private final String name = "AWT-EventQueue-" + threadInitNumber.getAndIncrement();

    static {
        AWTAccessor.setEventQueueAccessor(new AWTAccessor.EventQueueAccessor() { // from class: java.awt.EventQueue.2
            @Override // sun.awt.AWTAccessor.EventQueueAccessor
            public Thread getDispatchThread(EventQueue eventQueue) {
                return eventQueue.getDispatchThread();
            }

            @Override // sun.awt.AWTAccessor.EventQueueAccessor
            public boolean isDispatchThreadImpl(EventQueue eventQueue) {
                return eventQueue.isDispatchThreadImpl();
            }

            @Override // sun.awt.AWTAccessor.EventQueueAccessor
            public void removeSourceEvents(EventQueue eventQueue, Object obj, boolean z2) {
                eventQueue.removeSourceEvents(obj, z2);
            }

            @Override // sun.awt.AWTAccessor.EventQueueAccessor
            public boolean noEvents(EventQueue eventQueue) {
                return eventQueue.noEvents();
            }

            @Override // sun.awt.AWTAccessor.EventQueueAccessor
            public void wakeup(EventQueue eventQueue, boolean z2) {
                eventQueue.wakeup(z2);
            }

            @Override // sun.awt.AWTAccessor.EventQueueAccessor
            public void invokeAndWait(Object obj, Runnable runnable) throws InterruptedException, InvocationTargetException {
                EventQueue.invokeAndWait(obj, runnable);
            }

            @Override // sun.awt.AWTAccessor.EventQueueAccessor
            public void setFwDispatcher(EventQueue eventQueue, FwDispatcher fwDispatcher) {
                eventQueue.setFwDispatcher(fwDispatcher);
            }

            @Override // sun.awt.AWTAccessor.EventQueueAccessor
            public long getMostRecentEventTime(EventQueue eventQueue) {
                return eventQueue.getMostRecentEventTimeImpl();
            }
        });
        javaSecurityAccess = SharedSecrets.getJavaSecurityAccess();
    }

    private static final PlatformLogger getEventLog() {
        if (eventLog == null) {
            eventLog = PlatformLogger.getLogger("java.awt.event.EventQueue");
        }
        return eventLog;
    }

    public EventQueue() {
        for (int i2 = 0; i2 < 4; i2++) {
            this.queues[i2] = new Queue();
        }
        this.appContext = AppContext.getAppContext();
        this.pushPopLock = (Lock) this.appContext.get(AppContext.EVENT_QUEUE_LOCK_KEY);
        this.pushPopCond = (Condition) this.appContext.get(AppContext.EVENT_QUEUE_COND_KEY);
    }

    public void postEvent(AWTEvent aWTEvent) {
        SunToolkit.flushPendingEvents(this.appContext);
        postEventPrivate(aWTEvent);
    }

    private final void postEventPrivate(AWTEvent aWTEvent) {
        aWTEvent.isPosted = true;
        this.pushPopLock.lock();
        try {
            if (this.nextQueue != null) {
                this.nextQueue.postEventPrivate(aWTEvent);
                return;
            }
            if (this.dispatchThread == null) {
                if (aWTEvent.getSource() == AWTAutoShutdown.getInstance()) {
                    return;
                } else {
                    initDispatchThread();
                }
            }
            postEvent(aWTEvent, getPriority(aWTEvent));
        } finally {
            this.pushPopLock.unlock();
        }
    }

    private static int getPriority(AWTEvent aWTEvent) {
        if (aWTEvent instanceof PeerEvent) {
            PeerEvent peerEvent = (PeerEvent) aWTEvent;
            if ((peerEvent.getFlags() & 2) != 0) {
                return 3;
            }
            if ((peerEvent.getFlags() & 1) != 0) {
                return 2;
            }
            if ((peerEvent.getFlags() & 4) != 0) {
                return 0;
            }
        }
        int id = aWTEvent.getID();
        if (id >= 800 && id <= 801) {
            return 0;
        }
        return 1;
    }

    private void postEvent(AWTEvent aWTEvent, int i2) {
        if (coalesceEvent(aWTEvent, i2)) {
            return;
        }
        EventQueueItem eventQueueItem = new EventQueueItem(aWTEvent);
        cacheEQItem(eventQueueItem);
        boolean z2 = aWTEvent.getID() == this.waitForID;
        if (this.queues[i2].head == null) {
            boolean zNoEvents = noEvents();
            Queue queue = this.queues[i2];
            this.queues[i2].tail = eventQueueItem;
            queue.head = eventQueueItem;
            if (zNoEvents) {
                if (aWTEvent.getSource() != AWTAutoShutdown.getInstance()) {
                    AWTAutoShutdown.getInstance().notifyThreadBusy(this.dispatchThread);
                }
                this.pushPopCond.signalAll();
                return;
            } else {
                if (z2) {
                    this.pushPopCond.signalAll();
                    return;
                }
                return;
            }
        }
        this.queues[i2].tail.next = eventQueueItem;
        this.queues[i2].tail = eventQueueItem;
        if (z2) {
            this.pushPopCond.signalAll();
        }
    }

    private boolean coalescePaintEvent(PaintEvent paintEvent) {
        int iEventToCacheIndex;
        PaintEvent paintEventMergePaintEvents;
        ComponentPeer componentPeer = ((Component) paintEvent.getSource()).peer;
        if (componentPeer != null) {
            componentPeer.coalescePaintEvent(paintEvent);
        }
        EventQueueItem[] eventQueueItemArr = ((Component) paintEvent.getSource()).eventCache;
        if (eventQueueItemArr != null && (iEventToCacheIndex = eventToCacheIndex(paintEvent)) != -1 && eventQueueItemArr[iEventToCacheIndex] != null && (paintEventMergePaintEvents = mergePaintEvents(paintEvent, (PaintEvent) eventQueueItemArr[iEventToCacheIndex].event)) != null) {
            eventQueueItemArr[iEventToCacheIndex].event = paintEventMergePaintEvents;
            return true;
        }
        return false;
    }

    private PaintEvent mergePaintEvents(PaintEvent paintEvent, PaintEvent paintEvent2) {
        Rectangle updateRect = paintEvent.getUpdateRect();
        Rectangle updateRect2 = paintEvent2.getUpdateRect();
        if (updateRect2.contains(updateRect)) {
            return paintEvent2;
        }
        if (updateRect.contains(updateRect2)) {
            return paintEvent;
        }
        return null;
    }

    private boolean coalesceMouseEvent(MouseEvent mouseEvent) {
        int iEventToCacheIndex;
        EventQueueItem[] eventQueueItemArr = ((Component) mouseEvent.getSource()).eventCache;
        if (eventQueueItemArr != null && (iEventToCacheIndex = eventToCacheIndex(mouseEvent)) != -1 && eventQueueItemArr[iEventToCacheIndex] != null) {
            eventQueueItemArr[iEventToCacheIndex].event = mouseEvent;
            return true;
        }
        return false;
    }

    private boolean coalescePeerEvent(PeerEvent peerEvent) {
        int iEventToCacheIndex;
        EventQueueItem[] eventQueueItemArr = ((Component) peerEvent.getSource()).eventCache;
        if (eventQueueItemArr != null && (iEventToCacheIndex = eventToCacheIndex(peerEvent)) != -1 && eventQueueItemArr[iEventToCacheIndex] != null) {
            PeerEvent peerEventCoalesceEvents = peerEvent.coalesceEvents((PeerEvent) eventQueueItemArr[iEventToCacheIndex].event);
            if (peerEventCoalesceEvents != null) {
                eventQueueItemArr[iEventToCacheIndex].event = peerEventCoalesceEvents;
                return true;
            }
            eventQueueItemArr[iEventToCacheIndex] = null;
            return false;
        }
        return false;
    }

    private boolean coalesceOtherEvent(AWTEvent aWTEvent, int i2) {
        AWTEvent aWTEventCoalesceEvents;
        int id = aWTEvent.getID();
        Component component = (Component) aWTEvent.getSource();
        EventQueueItem eventQueueItem = this.queues[i2].head;
        while (true) {
            EventQueueItem eventQueueItem2 = eventQueueItem;
            if (eventQueueItem2 != null) {
                if (eventQueueItem2.event.getSource() != component || eventQueueItem2.event.getID() != id || (aWTEventCoalesceEvents = component.coalesceEvents(eventQueueItem2.event, aWTEvent)) == null) {
                    eventQueueItem = eventQueueItem2.next;
                } else {
                    eventQueueItem2.event = aWTEventCoalesceEvents;
                    return true;
                }
            } else {
                return false;
            }
        }
    }

    private boolean coalesceEvent(AWTEvent aWTEvent, int i2) {
        if (!(aWTEvent.getSource() instanceof Component)) {
            return false;
        }
        if (aWTEvent instanceof PeerEvent) {
            return coalescePeerEvent((PeerEvent) aWTEvent);
        }
        if (((Component) aWTEvent.getSource()).isCoalescingEnabled() && coalesceOtherEvent(aWTEvent, i2)) {
            return true;
        }
        if (aWTEvent instanceof PaintEvent) {
            return coalescePaintEvent((PaintEvent) aWTEvent);
        }
        if (aWTEvent instanceof MouseEvent) {
            return coalesceMouseEvent((MouseEvent) aWTEvent);
        }
        return false;
    }

    private void cacheEQItem(EventQueueItem eventQueueItem) {
        int iEventToCacheIndex = eventToCacheIndex(eventQueueItem.event);
        if (iEventToCacheIndex != -1 && (eventQueueItem.event.getSource() instanceof Component)) {
            Component component = (Component) eventQueueItem.event.getSource();
            if (component.eventCache == null) {
                component.eventCache = new EventQueueItem[5];
            }
            component.eventCache[iEventToCacheIndex] = eventQueueItem;
        }
    }

    private void uncacheEQItem(EventQueueItem eventQueueItem) {
        int iEventToCacheIndex = eventToCacheIndex(eventQueueItem.event);
        if (iEventToCacheIndex != -1 && (eventQueueItem.event.getSource() instanceof Component)) {
            Component component = (Component) eventQueueItem.event.getSource();
            if (component.eventCache == null) {
                return;
            }
            component.eventCache[iEventToCacheIndex] = null;
        }
    }

    private static int eventToCacheIndex(AWTEvent aWTEvent) {
        switch (aWTEvent.getID()) {
            case 503:
                return 2;
            case 506:
                return aWTEvent instanceof SunDropTargetEvent ? -1 : 3;
            case 800:
                return 0;
            case 801:
                return 1;
            default:
                return aWTEvent instanceof PeerEvent ? 4 : -1;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public boolean noEvents() {
        for (int i2 = 0; i2 < 4; i2++) {
            if (this.queues[i2].head != null) {
                return false;
            }
        }
        return true;
    }

    public AWTEvent getNextEvent() throws InterruptedException {
        while (true) {
            SunToolkit.flushPendingEvents(this.appContext);
            this.pushPopLock.lock();
            try {
                AWTEvent nextEventPrivate = getNextEventPrivate();
                if (nextEventPrivate != null) {
                    return nextEventPrivate;
                }
                AWTAutoShutdown.getInstance().notifyThreadFree(this.dispatchThread);
                this.pushPopCond.await();
                this.pushPopLock.unlock();
            } finally {
                this.pushPopLock.unlock();
            }
        }
    }

    AWTEvent getNextEventPrivate() throws InterruptedException {
        for (int i2 = 3; i2 >= 0; i2--) {
            if (this.queues[i2].head != null) {
                EventQueueItem eventQueueItem = this.queues[i2].head;
                this.queues[i2].head = eventQueueItem.next;
                if (eventQueueItem.next == null) {
                    this.queues[i2].tail = null;
                }
                uncacheEQItem(eventQueueItem);
                return eventQueueItem.event;
            }
        }
        return null;
    }

    AWTEvent getNextEvent(int i2) throws InterruptedException {
        int i3;
        EventQueueItem eventQueueItem;
        EventQueueItem eventQueueItem2;
        loop0: while (true) {
            SunToolkit.flushPendingEvents(this.appContext);
            this.pushPopLock.lock();
            i3 = 0;
            while (i3 < 4) {
                try {
                    eventQueueItem = this.queues[i3].head;
                    eventQueueItem2 = null;
                    while (eventQueueItem != null) {
                        if (eventQueueItem.event.getID() == i2) {
                            break loop0;
                        }
                        eventQueueItem2 = eventQueueItem;
                        eventQueueItem = eventQueueItem.next;
                    }
                    i3++;
                } catch (Throwable th) {
                    this.pushPopLock.unlock();
                    throw th;
                }
            }
            this.waitForID = i2;
            this.pushPopCond.await();
            this.waitForID = 0;
            this.pushPopLock.unlock();
        }
        if (eventQueueItem2 == null) {
            this.queues[i3].head = eventQueueItem.next;
        } else {
            eventQueueItem2.next = eventQueueItem.next;
        }
        if (this.queues[i3].tail == eventQueueItem) {
            this.queues[i3].tail = eventQueueItem2;
        }
        uncacheEQItem(eventQueueItem);
        AWTEvent aWTEvent = eventQueueItem.event;
        this.pushPopLock.unlock();
        return aWTEvent;
    }

    public AWTEvent peekEvent() {
        this.pushPopLock.lock();
        for (int i2 = 3; i2 >= 0; i2--) {
            try {
                if (this.queues[i2].head != null) {
                    return this.queues[i2].head.event;
                }
            } finally {
                this.pushPopLock.unlock();
            }
        }
        return null;
    }

    public AWTEvent peekEvent(int i2) {
        this.pushPopLock.lock();
        for (int i3 = 3; i3 >= 0; i3--) {
            try {
                for (EventQueueItem eventQueueItem = this.queues[i3].head; eventQueueItem != null; eventQueueItem = eventQueueItem.next) {
                    if (eventQueueItem.event.getID() == i2) {
                        AWTEvent aWTEvent = eventQueueItem.event;
                        this.pushPopLock.unlock();
                        return aWTEvent;
                    }
                }
            } finally {
                this.pushPopLock.unlock();
            }
        }
        return null;
    }

    protected void dispatchEvent(final AWTEvent aWTEvent) {
        final Object source = aWTEvent.getSource();
        final PrivilegedAction<Void> privilegedAction = new PrivilegedAction<Void>() { // from class: java.awt.EventQueue.3
            /* JADX WARN: Can't rename method to resolve collision */
            @Override // java.security.PrivilegedAction
            /* renamed from: run */
            public Void run2() throws IllegalArgumentException {
                if (EventQueue.this.fwDispatcher == null || EventQueue.this.isDispatchThreadImpl()) {
                    EventQueue.this.dispatchEventImpl(aWTEvent, source);
                    return null;
                }
                EventQueue.this.fwDispatcher.scheduleDispatch(new Runnable() { // from class: java.awt.EventQueue.3.1
                    @Override // java.lang.Runnable
                    public void run() throws IllegalArgumentException {
                        if (EventQueue.this.dispatchThread.filterAndCheckEvent(aWTEvent)) {
                            EventQueue.this.dispatchEventImpl(aWTEvent, source);
                        }
                    }
                });
                return null;
            }
        };
        AccessControlContext context = AccessController.getContext();
        AccessControlContext accessControlContextFrom = getAccessControlContextFrom(source);
        final AccessControlContext accessControlContext = aWTEvent.getAccessControlContext();
        if (accessControlContextFrom == null) {
            javaSecurityAccess.doIntersectionPrivilege(privilegedAction, context, accessControlContext);
        } else {
            javaSecurityAccess.doIntersectionPrivilege(new PrivilegedAction<Void>() { // from class: java.awt.EventQueue.4
                /* JADX WARN: Can't rename method to resolve collision */
                @Override // java.security.PrivilegedAction
                /* renamed from: run */
                public Void run2() {
                    EventQueue.javaSecurityAccess.doIntersectionPrivilege(privilegedAction, accessControlContext);
                    return null;
                }
            }, context, accessControlContextFrom);
        }
    }

    private static AccessControlContext getAccessControlContextFrom(Object obj) {
        if (obj instanceof Component) {
            return ((Component) obj).getAccessControlContext();
        }
        if (obj instanceof MenuComponent) {
            return ((MenuComponent) obj).getAccessControlContext();
        }
        if (obj instanceof TrayIcon) {
            return ((TrayIcon) obj).getAccessControlContext();
        }
        return null;
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Multi-variable type inference failed */
    public void dispatchEventImpl(AWTEvent aWTEvent, Object obj) throws IllegalArgumentException {
        aWTEvent.isPosted = true;
        if (aWTEvent instanceof ActiveEvent) {
            setCurrentEventAndMostRecentTimeImpl(aWTEvent);
            ((ActiveEvent) aWTEvent).dispatch();
            return;
        }
        if (obj instanceof Component) {
            ((Component) obj).dispatchEvent(aWTEvent);
            aWTEvent.dispatched();
            return;
        }
        if (obj instanceof MenuComponent) {
            ((MenuComponent) obj).dispatchEvent(aWTEvent);
            return;
        }
        if (obj instanceof TrayIcon) {
            ((TrayIcon) obj).dispatchEvent(aWTEvent);
            return;
        }
        if (obj instanceof AWTAutoShutdown) {
            if (noEvents()) {
                this.dispatchThread.stopDispatching();
            }
        } else if (getEventLog().isLoggable(PlatformLogger.Level.FINE)) {
            getEventLog().fine("Unable to dispatch event: " + ((Object) aWTEvent));
        }
    }

    public static long getMostRecentEventTime() {
        return Toolkit.getEventQueue().getMostRecentEventTimeImpl();
    }

    /* JADX INFO: Access modifiers changed from: private */
    public long getMostRecentEventTimeImpl() {
        this.pushPopLock.lock();
        try {
            return Thread.currentThread() == this.dispatchThread ? this.mostRecentEventTime : System.currentTimeMillis();
        } finally {
            this.pushPopLock.unlock();
        }
    }

    long getMostRecentEventTimeEx() {
        this.pushPopLock.lock();
        try {
            return this.mostRecentEventTime;
        } finally {
            this.pushPopLock.unlock();
        }
    }

    public static AWTEvent getCurrentEvent() {
        return Toolkit.getEventQueue().getCurrentEventImpl();
    }

    private AWTEvent getCurrentEventImpl() {
        this.pushPopLock.lock();
        try {
            return Thread.currentThread() == this.dispatchThread ? this.currentEvent.get() : null;
        } finally {
            this.pushPopLock.unlock();
        }
    }

    public void push(EventQueue eventQueue) {
        if (getEventLog().isLoggable(PlatformLogger.Level.FINE)) {
            getEventLog().fine("EventQueue.push(" + ((Object) eventQueue) + ")");
        }
        this.pushPopLock.lock();
        EventQueue eventQueue2 = this;
        while (eventQueue2.nextQueue != null) {
            try {
                eventQueue2 = eventQueue2.nextQueue;
            } catch (Throwable th) {
                this.pushPopLock.unlock();
                throw th;
            }
        }
        if (eventQueue2.fwDispatcher != null) {
            throw new RuntimeException("push() to queue with fwDispatcher");
        }
        if (eventQueue2.dispatchThread != null && eventQueue2.dispatchThread.getEventQueue() == this) {
            eventQueue.dispatchThread = eventQueue2.dispatchThread;
            eventQueue2.dispatchThread.setEventQueue(eventQueue);
        }
        while (eventQueue2.peekEvent() != null) {
            try {
                eventQueue.postEventPrivate(eventQueue2.getNextEventPrivate());
            } catch (InterruptedException e2) {
                if (getEventLog().isLoggable(PlatformLogger.Level.FINE)) {
                    getEventLog().fine("Interrupted push", e2);
                }
            }
        }
        if (eventQueue2.dispatchThread != null) {
            eventQueue2.postEventPrivate(new InvocationEvent(eventQueue2, dummyRunnable));
        }
        eventQueue.previousQueue = eventQueue2;
        eventQueue2.nextQueue = eventQueue;
        if (this.appContext.get(AppContext.EVENT_QUEUE_KEY) == eventQueue2) {
            this.appContext.put(AppContext.EVENT_QUEUE_KEY, eventQueue);
        }
        this.pushPopCond.signalAll();
        this.pushPopLock.unlock();
    }

    protected void pop() throws EmptyStackException {
        if (getEventLog().isLoggable(PlatformLogger.Level.FINE)) {
            getEventLog().fine("EventQueue.pop(" + ((Object) this) + ")");
        }
        this.pushPopLock.lock();
        EventQueue eventQueue = this;
        while (eventQueue.nextQueue != null) {
            try {
                eventQueue = eventQueue.nextQueue;
            } catch (Throwable th) {
                this.pushPopLock.unlock();
                throw th;
            }
        }
        EventQueue eventQueue2 = eventQueue.previousQueue;
        if (eventQueue2 == null) {
            throw new EmptyStackException();
        }
        eventQueue.previousQueue = null;
        eventQueue2.nextQueue = null;
        while (eventQueue.peekEvent() != null) {
            try {
                eventQueue2.postEventPrivate(eventQueue.getNextEventPrivate());
            } catch (InterruptedException e2) {
                if (getEventLog().isLoggable(PlatformLogger.Level.FINE)) {
                    getEventLog().fine("Interrupted pop", e2);
                }
            }
        }
        if (eventQueue.dispatchThread != null && eventQueue.dispatchThread.getEventQueue() == this) {
            eventQueue2.dispatchThread = eventQueue.dispatchThread;
            eventQueue.dispatchThread.setEventQueue(eventQueue2);
        }
        if (this.appContext.get(AppContext.EVENT_QUEUE_KEY) == this) {
            this.appContext.put(AppContext.EVENT_QUEUE_KEY, eventQueue2);
        }
        eventQueue.postEventPrivate(new InvocationEvent(eventQueue, dummyRunnable));
        this.pushPopCond.signalAll();
        this.pushPopLock.unlock();
    }

    public SecondaryLoop createSecondaryLoop() {
        return createSecondaryLoop(null, null, 0L);
    }

    /* loaded from: rt.jar:java/awt/EventQueue$FwSecondaryLoopWrapper.class */
    private class FwSecondaryLoopWrapper implements SecondaryLoop {
        private final SecondaryLoop loop;
        private final EventFilter filter;

        public FwSecondaryLoopWrapper(SecondaryLoop secondaryLoop, EventFilter eventFilter) {
            this.loop = secondaryLoop;
            this.filter = eventFilter;
        }

        @Override // java.awt.SecondaryLoop
        public boolean enter() {
            if (this.filter != null) {
                EventQueue.this.dispatchThread.addEventFilter(this.filter);
            }
            return this.loop.enter();
        }

        @Override // java.awt.SecondaryLoop
        public boolean exit() {
            if (this.filter != null) {
                EventQueue.this.dispatchThread.removeEventFilter(this.filter);
            }
            return this.loop.exit();
        }
    }

    SecondaryLoop createSecondaryLoop(Conditional conditional, EventFilter eventFilter, long j2) {
        this.pushPopLock.lock();
        try {
            if (this.nextQueue != null) {
                SecondaryLoop secondaryLoopCreateSecondaryLoop = this.nextQueue.createSecondaryLoop(conditional, eventFilter, j2);
                this.pushPopLock.unlock();
                return secondaryLoopCreateSecondaryLoop;
            }
            if (this.fwDispatcher != null) {
                FwSecondaryLoopWrapper fwSecondaryLoopWrapper = new FwSecondaryLoopWrapper(this.fwDispatcher.createSecondaryLoop(), eventFilter);
                this.pushPopLock.unlock();
                return fwSecondaryLoopWrapper;
            }
            if (this.dispatchThread == null) {
                initDispatchThread();
            }
            WaitDispatchSupport waitDispatchSupport = new WaitDispatchSupport(this.dispatchThread, conditional, eventFilter, j2);
            this.pushPopLock.unlock();
            return waitDispatchSupport;
        } catch (Throwable th) {
            this.pushPopLock.unlock();
            throw th;
        }
    }

    public static boolean isDispatchThread() {
        return Toolkit.getEventQueue().isDispatchThreadImpl();
    }

    final boolean isDispatchThreadImpl() {
        EventQueue eventQueue = this;
        this.pushPopLock.lock();
        try {
            EventQueue eventQueue2 = eventQueue.nextQueue;
            while (eventQueue2 != null) {
                eventQueue = eventQueue2;
                eventQueue2 = eventQueue.nextQueue;
            }
            if (eventQueue.fwDispatcher != null) {
                boolean zIsDispatchThread = eventQueue.fwDispatcher.isDispatchThread();
                this.pushPopLock.unlock();
                return zIsDispatchThread;
            }
            return Thread.currentThread() == eventQueue.dispatchThread;
        } finally {
            this.pushPopLock.unlock();
        }
    }

    final void initDispatchThread() {
        this.pushPopLock.lock();
        try {
            if (this.dispatchThread == null && !this.threadGroup.isDestroyed() && !this.appContext.isDisposed()) {
                this.dispatchThread = (EventDispatchThread) AccessController.doPrivileged(new PrivilegedAction<EventDispatchThread>() { // from class: java.awt.EventQueue.5
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public EventDispatchThread run2() {
                        EventDispatchThread eventDispatchThread = new EventDispatchThread(EventQueue.this.threadGroup, EventQueue.this.name, EventQueue.this);
                        eventDispatchThread.setContextClassLoader(EventQueue.this.classLoader);
                        eventDispatchThread.setPriority(6);
                        eventDispatchThread.setDaemon(false);
                        AWTAutoShutdown.getInstance().notifyThreadBusy(eventDispatchThread);
                        return eventDispatchThread;
                    }
                });
                this.dispatchThread.start();
            }
        } finally {
            this.pushPopLock.unlock();
        }
    }

    final void detachDispatchThread(EventDispatchThread eventDispatchThread) {
        SunToolkit.flushPendingEvents(this.appContext);
        this.pushPopLock.lock();
        try {
            if (eventDispatchThread == this.dispatchThread) {
                this.dispatchThread = null;
            }
            AWTAutoShutdown.getInstance().notifyThreadFree(eventDispatchThread);
            if (peekEvent() != null) {
                initDispatchThread();
            }
        } finally {
            this.pushPopLock.unlock();
        }
    }

    final EventDispatchThread getDispatchThread() {
        this.pushPopLock.lock();
        try {
            return this.dispatchThread;
        } finally {
            this.pushPopLock.unlock();
        }
    }

    final void removeSourceEvents(Object obj, boolean z2) {
        SunToolkit.flushPendingEvents(this.appContext);
        this.pushPopLock.lock();
        for (int i2 = 0; i2 < 4; i2++) {
            try {
                EventQueueItem eventQueueItem = null;
                for (EventQueueItem eventQueueItem2 = this.queues[i2].head; eventQueueItem2 != null; eventQueueItem2 = eventQueueItem2.next) {
                    if (eventQueueItem2.event.getSource() == obj && (z2 || (!(eventQueueItem2.event instanceof SequencedEvent) && !(eventQueueItem2.event instanceof SentEvent) && !(eventQueueItem2.event instanceof FocusEvent) && !(eventQueueItem2.event instanceof WindowEvent) && !(eventQueueItem2.event instanceof KeyEvent) && !(eventQueueItem2.event instanceof InputMethodEvent)))) {
                        if (eventQueueItem2.event instanceof SequencedEvent) {
                            ((SequencedEvent) eventQueueItem2.event).dispose();
                        }
                        if (eventQueueItem2.event instanceof SentEvent) {
                            ((SentEvent) eventQueueItem2.event).dispose();
                        }
                        if (eventQueueItem2.event instanceof InvocationEvent) {
                            AWTAccessor.getInvocationEventAccessor().dispose((InvocationEvent) eventQueueItem2.event);
                        }
                        if (eventQueueItem == null) {
                            this.queues[i2].head = eventQueueItem2.next;
                        } else {
                            eventQueueItem.next = eventQueueItem2.next;
                        }
                        uncacheEQItem(eventQueueItem2);
                    } else {
                        eventQueueItem = eventQueueItem2;
                    }
                }
                this.queues[i2].tail = eventQueueItem;
            } finally {
                this.pushPopLock.unlock();
            }
        }
    }

    synchronized long getMostRecentKeyEventTime() {
        this.pushPopLock.lock();
        try {
            return this.mostRecentKeyEventTime;
        } finally {
            this.pushPopLock.unlock();
        }
    }

    static void setCurrentEventAndMostRecentTime(AWTEvent aWTEvent) {
        Toolkit.getEventQueue().setCurrentEventAndMostRecentTimeImpl(aWTEvent);
    }

    private void setCurrentEventAndMostRecentTimeImpl(AWTEvent aWTEvent) {
        this.pushPopLock.lock();
        try {
            if (Thread.currentThread() != this.dispatchThread) {
                return;
            }
            this.currentEvent = new WeakReference<>(aWTEvent);
            long when = Long.MIN_VALUE;
            if (aWTEvent instanceof InputEvent) {
                InputEvent inputEvent = (InputEvent) aWTEvent;
                when = inputEvent.getWhen();
                if (aWTEvent instanceof KeyEvent) {
                    this.mostRecentKeyEventTime = inputEvent.getWhen();
                }
            } else if (aWTEvent instanceof InputMethodEvent) {
                when = ((InputMethodEvent) aWTEvent).getWhen();
            } else if (aWTEvent instanceof ActionEvent) {
                when = ((ActionEvent) aWTEvent).getWhen();
            } else if (aWTEvent instanceof InvocationEvent) {
                when = ((InvocationEvent) aWTEvent).getWhen();
            }
            this.mostRecentEventTime = Math.max(this.mostRecentEventTime, when);
            this.pushPopLock.unlock();
        } finally {
            this.pushPopLock.unlock();
        }
    }

    public static void invokeLater(Runnable runnable) {
        Toolkit.getEventQueue().postEvent(new InvocationEvent(Toolkit.getDefaultToolkit(), runnable));
    }

    public static void invokeAndWait(Runnable runnable) throws InterruptedException, InvocationTargetException {
        invokeAndWait(Toolkit.getDefaultToolkit(), runnable);
    }

    static void invokeAndWait(Object obj, Runnable runnable) throws InterruptedException, InvocationTargetException {
        if (isDispatchThread()) {
            throw new Error("Cannot call invokeAndWait from the event dispatcher thread");
        }
        Object obj2 = new Object() { // from class: java.awt.EventQueue.1AWTInvocationLock
        };
        InvocationEvent invocationEvent = new InvocationEvent(obj, runnable, obj2, true);
        synchronized (obj2) {
            Toolkit.getEventQueue().postEvent(invocationEvent);
            while (!invocationEvent.isDispatched()) {
                obj2.wait();
            }
        }
        Throwable throwable = invocationEvent.getThrowable();
        if (throwable != null) {
            throw new InvocationTargetException(throwable);
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wakeup(boolean z2) {
        this.pushPopLock.lock();
        try {
            if (this.nextQueue != null) {
                this.nextQueue.wakeup(z2);
            } else if (this.dispatchThread != null) {
                this.pushPopCond.signalAll();
            } else if (!z2) {
                initDispatchThread();
            }
        } finally {
            this.pushPopLock.unlock();
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void setFwDispatcher(FwDispatcher fwDispatcher) {
        if (this.nextQueue != null) {
            this.nextQueue.setFwDispatcher(fwDispatcher);
        } else {
            this.fwDispatcher = fwDispatcher;
        }
    }
}
