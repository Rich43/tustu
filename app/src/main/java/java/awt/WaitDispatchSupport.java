package java.awt;

import java.security.AccessController;
import java.security.PrivilegedAction;
import java.util.Timer;
import java.util.TimerTask;
import java.util.concurrent.atomic.AtomicBoolean;
import sun.awt.PeerEvent;
import sun.util.logging.PlatformLogger;

/* loaded from: rt.jar:java/awt/WaitDispatchSupport.class */
class WaitDispatchSupport implements SecondaryLoop {
    private static final PlatformLogger log = PlatformLogger.getLogger("java.awt.event.WaitDispatchSupport");
    private EventDispatchThread dispatchThread;
    private EventFilter filter;
    private volatile Conditional extCondition;
    private volatile Conditional condition;
    private long interval;
    private static Timer timer;
    private TimerTask timerTask;
    private AtomicBoolean keepBlockingEDT;
    private AtomicBoolean keepBlockingCT;
    private AtomicBoolean afterExit;
    private final Runnable wakingRunnable;

    private static synchronized void initializeTimer() {
        if (timer == null) {
            timer = new Timer("AWT-WaitDispatchSupport-Timer", true);
        }
    }

    public WaitDispatchSupport(EventDispatchThread eventDispatchThread) {
        this(eventDispatchThread, null);
    }

    public WaitDispatchSupport(EventDispatchThread eventDispatchThread, Conditional conditional) {
        this.keepBlockingEDT = new AtomicBoolean(false);
        this.keepBlockingCT = new AtomicBoolean(false);
        this.afterExit = new AtomicBoolean(false);
        this.wakingRunnable = new Runnable() { // from class: java.awt.WaitDispatchSupport.5
            @Override // java.lang.Runnable
            public void run() {
                WaitDispatchSupport.log.fine("Wake up EDT");
                synchronized (WaitDispatchSupport.getTreeLock()) {
                    WaitDispatchSupport.this.keepBlockingCT.set(false);
                    WaitDispatchSupport.getTreeLock().notifyAll();
                }
                WaitDispatchSupport.log.fine("Wake up EDT done");
            }
        };
        if (eventDispatchThread == null) {
            throw new IllegalArgumentException("The dispatchThread can not be null");
        }
        this.dispatchThread = eventDispatchThread;
        this.extCondition = conditional;
        this.condition = new Conditional() { // from class: java.awt.WaitDispatchSupport.1
            @Override // java.awt.Conditional
            public boolean evaluate() {
                if (WaitDispatchSupport.log.isLoggable(PlatformLogger.Level.FINEST)) {
                    WaitDispatchSupport.log.finest("evaluate(): blockingEDT=" + WaitDispatchSupport.this.keepBlockingEDT.get() + ", blockingCT=" + WaitDispatchSupport.this.keepBlockingCT.get());
                }
                boolean zEvaluate = WaitDispatchSupport.this.extCondition != null ? WaitDispatchSupport.this.extCondition.evaluate() : true;
                if (!WaitDispatchSupport.this.keepBlockingEDT.get() || !zEvaluate) {
                    if (WaitDispatchSupport.this.timerTask != null) {
                        WaitDispatchSupport.this.timerTask.cancel();
                        WaitDispatchSupport.this.timerTask = null;
                        return false;
                    }
                    return false;
                }
                return true;
            }
        };
    }

    public WaitDispatchSupport(EventDispatchThread eventDispatchThread, Conditional conditional, EventFilter eventFilter, long j2) {
        this(eventDispatchThread, conditional);
        this.filter = eventFilter;
        if (j2 < 0) {
            throw new IllegalArgumentException("The interval value must be >= 0");
        }
        this.interval = j2;
        if (j2 != 0) {
            initializeTimer();
        }
    }

    /* JADX WARN: Finally extract failed */
    @Override // java.awt.SecondaryLoop
    public boolean enter() {
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            log.fine("enter(): blockingEDT=" + this.keepBlockingEDT.get() + ", blockingCT=" + this.keepBlockingCT.get());
        }
        if (!this.keepBlockingEDT.compareAndSet(false, true)) {
            log.fine("The secondary loop is already running, aborting");
            return false;
        }
        try {
            if (this.afterExit.get()) {
                log.fine("Exit was called already, aborting");
                this.keepBlockingEDT.set(false);
                this.keepBlockingCT.set(false);
                this.afterExit.set(false);
                return false;
            }
            final Runnable runnable = new Runnable() { // from class: java.awt.WaitDispatchSupport.2
                @Override // java.lang.Runnable
                public void run() {
                    WaitDispatchSupport.log.fine("Starting a new event pump");
                    if (WaitDispatchSupport.this.filter == null) {
                        WaitDispatchSupport.this.dispatchThread.pumpEvents(WaitDispatchSupport.this.condition);
                    } else {
                        WaitDispatchSupport.this.dispatchThread.pumpEventsForFilter(WaitDispatchSupport.this.condition, WaitDispatchSupport.this.filter);
                    }
                }
            };
            Thread threadCurrentThread = Thread.currentThread();
            if (threadCurrentThread == this.dispatchThread) {
                if (log.isLoggable(PlatformLogger.Level.FINEST)) {
                    log.finest("On dispatch thread: " + ((Object) this.dispatchThread));
                }
                if (this.interval != 0) {
                    if (log.isLoggable(PlatformLogger.Level.FINEST)) {
                        log.finest("scheduling the timer for " + this.interval + " ms");
                    }
                    Timer timer2 = timer;
                    TimerTask timerTask = new TimerTask() { // from class: java.awt.WaitDispatchSupport.3
                        @Override // java.util.TimerTask, java.lang.Runnable
                        public void run() {
                            if (WaitDispatchSupport.this.keepBlockingEDT.compareAndSet(true, false)) {
                                WaitDispatchSupport.this.wakeupEDT();
                            }
                        }
                    };
                    this.timerTask = timerTask;
                    timer2.schedule(timerTask, this.interval);
                }
                SequencedEvent currentSequencedEvent = KeyboardFocusManager.getCurrentKeyboardFocusManager().getCurrentSequencedEvent();
                if (currentSequencedEvent != null) {
                    if (log.isLoggable(PlatformLogger.Level.FINE)) {
                        log.fine("Dispose current SequencedEvent: " + ((Object) currentSequencedEvent));
                    }
                    currentSequencedEvent.dispose();
                }
                AccessController.doPrivileged(new PrivilegedAction<Void>() { // from class: java.awt.WaitDispatchSupport.4
                    /* JADX WARN: Can't rename method to resolve collision */
                    @Override // java.security.PrivilegedAction
                    /* renamed from: run */
                    public Void run2() {
                        runnable.run();
                        return null;
                    }
                });
            } else {
                if (log.isLoggable(PlatformLogger.Level.FINEST)) {
                    log.finest("On non-dispatch thread: " + ((Object) threadCurrentThread));
                }
                this.keepBlockingCT.set(true);
                synchronized (getTreeLock()) {
                    if (this.afterExit.get()) {
                        return false;
                    }
                    if (this.filter != null) {
                        this.dispatchThread.addEventFilter(this.filter);
                    }
                    try {
                        try {
                            this.dispatchThread.getEventQueue().postEvent(new PeerEvent(this, runnable, 1L));
                            if (this.interval > 0) {
                                long jCurrentTimeMillis = System.currentTimeMillis();
                                while (this.keepBlockingCT.get() && ((this.extCondition == null || this.extCondition.evaluate()) && jCurrentTimeMillis + this.interval > System.currentTimeMillis())) {
                                    getTreeLock().wait(this.interval);
                                }
                            } else {
                                while (this.keepBlockingCT.get() && (this.extCondition == null || this.extCondition.evaluate())) {
                                    getTreeLock().wait();
                                }
                            }
                            if (log.isLoggable(PlatformLogger.Level.FINE)) {
                                log.fine("waitDone " + this.keepBlockingEDT.get() + " " + this.keepBlockingCT.get());
                            }
                            if (this.filter != null) {
                                this.dispatchThread.removeEventFilter(this.filter);
                            }
                        } catch (Throwable th) {
                            if (this.filter != null) {
                                this.dispatchThread.removeEventFilter(this.filter);
                            }
                            throw th;
                        }
                    } catch (InterruptedException e2) {
                        if (log.isLoggable(PlatformLogger.Level.FINE)) {
                            log.fine("Exception caught while waiting: " + ((Object) e2));
                        }
                        if (this.filter != null) {
                            this.dispatchThread.removeEventFilter(this.filter);
                        }
                    }
                }
            }
            this.keepBlockingEDT.set(false);
            this.keepBlockingCT.set(false);
            this.afterExit.set(false);
            return true;
        } finally {
            this.keepBlockingEDT.set(false);
            this.keepBlockingCT.set(false);
            this.afterExit.set(false);
        }
    }

    @Override // java.awt.SecondaryLoop
    public boolean exit() {
        if (log.isLoggable(PlatformLogger.Level.FINE)) {
            log.fine("exit(): blockingEDT=" + this.keepBlockingEDT.get() + ", blockingCT=" + this.keepBlockingCT.get());
        }
        this.afterExit.set(true);
        if (this.keepBlockingEDT.getAndSet(false)) {
            wakeupEDT();
            return true;
        }
        return false;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public static final Object getTreeLock() {
        return Component.LOCK;
    }

    /* JADX INFO: Access modifiers changed from: private */
    public void wakeupEDT() {
        if (log.isLoggable(PlatformLogger.Level.FINEST)) {
            log.finest("wakeupEDT(): EDT == " + ((Object) this.dispatchThread));
        }
        this.dispatchThread.getEventQueue().postEvent(new PeerEvent(this, this.wakingRunnable, 1L));
    }
}
