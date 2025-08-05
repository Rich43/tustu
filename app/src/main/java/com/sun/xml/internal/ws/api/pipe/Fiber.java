package com.sun.xml.internal.ws.api.pipe;

import com.sun.istack.internal.NotNull;
import com.sun.istack.internal.Nullable;
import com.sun.xml.internal.ws.api.Cancelable;
import com.sun.xml.internal.ws.api.Component;
import com.sun.xml.internal.ws.api.ComponentRegistry;
import com.sun.xml.internal.ws.api.SOAPVersion;
import com.sun.xml.internal.ws.api.addressing.AddressingVersion;
import com.sun.xml.internal.ws.api.message.AddressingUtils;
import com.sun.xml.internal.ws.api.message.Packet;
import com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor;
import com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl;
import com.sun.xml.internal.ws.api.server.Container;
import com.sun.xml.internal.ws.api.server.ContainerResolver;
import java.util.ArrayList;
import java.util.Iterator;
import java.util.List;
import java.util.Set;
import java.util.concurrent.CopyOnWriteArraySet;
import java.util.concurrent.atomic.AtomicInteger;
import java.util.concurrent.locks.Condition;
import java.util.concurrent.locks.ReentrantLock;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.xml.ws.Holder;
import javax.xml.ws.WebServiceException;
import org.icepdf.core.util.PdfOps;

/* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/Fiber.class */
public final class Fiber implements Runnable, Cancelable, ComponentRegistry {
    private int contsSize;
    private Tube next;
    private Packet packet;
    private Throwable throwable;
    public final Engine owner;
    private boolean synchronous;
    private boolean interrupted;
    private List<FiberContextSwitchInterceptor> interceptors;

    @Nullable
    private ClassLoader contextClassLoader;

    @Nullable
    private CompletionCallback completionCallback;
    private Thread currentThread;
    private volatile boolean isCanceled;
    private boolean started;
    private boolean startedSync;
    private static final PlaceholderTube PLACEHOLDER;
    private static final ThreadLocal<Fiber> CURRENT_FIBER;
    private static final AtomicInteger iotaGen;
    private static final Logger LOGGER;
    private static final ReentrantLock serializedExecutionLock;
    public static volatile boolean serializeExecution;
    static final /* synthetic */ boolean $assertionsDisabled;
    private final List<Listener> _listeners = new ArrayList();
    private Tube[] conts = new Tube[16];
    private volatile int suspendedCount = 0;
    private volatile boolean isInsideSuspendCallbacks = false;
    private boolean isDeliverThrowableInPacket = false;
    private final ReentrantLock lock = new ReentrantLock();
    private final Condition condition = this.lock.newCondition();
    private final Set<Component> components = new CopyOnWriteArraySet();
    private final int id = iotaGen.incrementAndGet();

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/Fiber$CompletionCallback.class */
    public interface CompletionCallback {
        void onCompletion(@NotNull Packet packet);

        void onCompletion(@NotNull Throwable th);
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/Fiber$Listener.class */
    public interface Listener {
        void fiberSuspended(Fiber fiber);

        void fiberResumed(Fiber fiber);
    }

    static {
        $assertionsDisabled = !Fiber.class.desiredAssertionStatus();
        PLACEHOLDER = new PlaceholderTube();
        CURRENT_FIBER = new ThreadLocal<>();
        iotaGen = new AtomicInteger();
        LOGGER = Logger.getLogger(Fiber.class.getName());
        serializedExecutionLock = new ReentrantLock();
        serializeExecution = Boolean.getBoolean(Fiber.class.getName() + ".serialize");
    }

    public void addListener(Listener listener) {
        synchronized (this._listeners) {
            if (!this._listeners.contains(listener)) {
                this._listeners.add(listener);
            }
        }
    }

    public void removeListener(Listener listener) {
        synchronized (this._listeners) {
            this._listeners.remove(listener);
        }
    }

    List<Listener> getCurrentListeners() {
        ArrayList arrayList;
        synchronized (this._listeners) {
            arrayList = new ArrayList(this._listeners);
        }
        return arrayList;
    }

    private void clearListeners() {
        synchronized (this._listeners) {
            this._listeners.clear();
        }
    }

    public void setDeliverThrowableInPacket(boolean isDeliverThrowableInPacket) {
        this.isDeliverThrowableInPacket = isDeliverThrowableInPacket;
    }

    Fiber(Engine engine) {
        this.owner = engine;
        if (isTraceEnabled()) {
            LOGGER.log(Level.FINE, "{0} created", getName());
        }
        this.contextClassLoader = Thread.currentThread().getContextClassLoader();
    }

    public void start(@NotNull Tube tubeline, @NotNull Packet request, @Nullable CompletionCallback completionCallback) {
        start(tubeline, request, completionCallback, false);
    }

    private void dumpFiberContext(String desc) {
        String actionAndMsgDesc;
        String tubeDesc;
        if (isTraceEnabled()) {
            String action = null;
            String msgId = null;
            if (this.packet != null) {
                for (SOAPVersion sv : SOAPVersion.values()) {
                    for (AddressingVersion av2 : AddressingVersion.values()) {
                        action = this.packet.getMessage() != null ? AddressingUtils.getAction(this.packet.getMessage().getHeaders(), av2, sv) : null;
                        msgId = this.packet.getMessage() != null ? AddressingUtils.getMessageID(this.packet.getMessage().getHeaders(), av2, sv) : null;
                        if (action != null || msgId != null) {
                            break;
                        }
                    }
                    if (action != null || msgId != null) {
                        break;
                    }
                }
            }
            if (action == null && msgId == null) {
                actionAndMsgDesc = "NO ACTION or MSG ID";
            } else {
                actionAndMsgDesc = PdfOps.SINGLE_QUOTE_TOKEN + action + "' and msgId '" + msgId + PdfOps.SINGLE_QUOTE_TOKEN;
            }
            if (this.next != null) {
                tubeDesc = this.next.toString() + ".processRequest()";
            } else {
                tubeDesc = ((Object) peekCont()) + ".processResponse()";
            }
            Logger logger = LOGGER;
            Level level = Level.FINE;
            Object[] objArr = new Object[6];
            objArr[0] = getName();
            objArr[1] = desc;
            objArr[2] = actionAndMsgDesc;
            objArr[3] = tubeDesc;
            objArr[4] = Thread.currentThread().getName();
            objArr[5] = this.packet != null ? this.packet.toShortString() : null;
            logger.log(level, "{0} {1} with {2} and ''current'' tube {3} from thread {4} with Packet: {5}", objArr);
        }
    }

    public void start(@NotNull Tube tubeline, @NotNull Packet request, @Nullable CompletionCallback completionCallback, boolean forceSync) {
        this.next = tubeline;
        this.packet = request;
        this.completionCallback = completionCallback;
        if (forceSync) {
            this.startedSync = true;
            dumpFiberContext("starting (sync)");
            run();
        } else {
            this.started = true;
            dumpFiberContext("starting (async)");
            this.owner.addRunnable(this);
        }
    }

    public void resume(@NotNull Packet resumePacket) {
        resume(resumePacket, false);
    }

    public void resume(@NotNull Packet resumePacket, boolean forceSync) {
        resume(resumePacket, forceSync, (CompletionCallback) null);
    }

    public void resume(@NotNull Packet resumePacket, boolean forceSync, CompletionCallback callback) {
        this.lock.lock();
        if (callback != null) {
            try {
                setCompletionCallback(callback);
            } finally {
                this.lock.unlock();
            }
        }
        if (isTraceEnabled()) {
            LOGGER.log(Level.FINE, "{0} resuming. Will have suspendedCount={1}", new Object[]{getName(), Integer.valueOf(this.suspendedCount - 1)});
        }
        this.packet = resumePacket;
        int i2 = this.suspendedCount - 1;
        this.suspendedCount = i2;
        if (i2 == 0) {
            if (!this.isInsideSuspendCallbacks) {
                List<Listener> listeners = getCurrentListeners();
                for (Listener listener : listeners) {
                    try {
                        listener.fiberResumed(this);
                    } catch (Throwable e2) {
                        if (isTraceEnabled()) {
                            LOGGER.log(Level.FINE, "Listener {0} threw exception: {1}", new Object[]{listener, e2.getMessage()});
                        }
                    }
                }
                if (this.synchronous) {
                    this.condition.signalAll();
                } else if (forceSync || this.startedSync) {
                    run();
                } else {
                    dumpFiberContext("resuming (async)");
                    this.owner.addRunnable(this);
                }
            }
        } else if (isTraceEnabled()) {
            LOGGER.log(Level.FINE, "{0} taking no action on resume because suspendedCount != 0: {1}", new Object[]{getName(), Integer.valueOf(this.suspendedCount)});
        }
    }

    public void resumeAndReturn(@NotNull Packet resumePacket, boolean forceSync) {
        if (isTraceEnabled()) {
            LOGGER.log(Level.FINE, "{0} resumed with Return Packet", getName());
        }
        this.next = null;
        resume(resumePacket, forceSync);
    }

    public void resume(@NotNull Throwable throwable) {
        resume(throwable, this.packet, false);
    }

    public void resume(@NotNull Throwable throwable, @NotNull Packet packet) {
        resume(throwable, packet, false);
    }

    public void resume(@NotNull Throwable error, boolean forceSync) {
        resume(error, this.packet, forceSync);
    }

    public void resume(@NotNull Throwable error, @NotNull Packet packet, boolean forceSync) {
        if (isTraceEnabled()) {
            LOGGER.log(Level.FINE, "{0} resumed with Return Throwable", getName());
        }
        this.next = null;
        this.throwable = error;
        resume(packet, forceSync);
    }

    @Override // com.sun.xml.internal.ws.api.Cancelable
    public void cancel(boolean mayInterrupt) {
        this.isCanceled = true;
        if (mayInterrupt) {
            synchronized (this) {
                if (this.currentThread != null) {
                    this.currentThread.interrupt();
                }
            }
        }
    }

    /* JADX WARN: Type inference failed for: r1v8, types: [T, java.lang.Boolean] */
    private boolean suspend(Holder<Boolean> isRequireUnlock, Runnable onExitRunnable) {
        if (isTraceEnabled()) {
            LOGGER.log(Level.FINE, "{0} suspending. Will have suspendedCount={1}", new Object[]{getName(), Integer.valueOf(this.suspendedCount + 1)});
            if (this.suspendedCount > 0) {
                LOGGER.log(Level.FINE, "WARNING - {0} suspended more than resumed. Will require more than one resume to actually resume this fiber.", getName());
            }
        }
        List<Listener> listeners = getCurrentListeners();
        int i2 = this.suspendedCount + 1;
        this.suspendedCount = i2;
        if (i2 == 1) {
            this.isInsideSuspendCallbacks = true;
            try {
                for (Listener listener : listeners) {
                    try {
                        listener.fiberSuspended(this);
                    } catch (Throwable e2) {
                        if (isTraceEnabled()) {
                            LOGGER.log(Level.FINE, "Listener {0} threw exception: {1}", new Object[]{listener, e2.getMessage()});
                        }
                    }
                }
            } finally {
                this.isInsideSuspendCallbacks = false;
            }
        }
        if (this.suspendedCount <= 0) {
            for (Listener listener2 : listeners) {
                try {
                    listener2.fiberResumed(this);
                } catch (Throwable e3) {
                    if (isTraceEnabled()) {
                        LOGGER.log(Level.FINE, "Listener {0} threw exception: {1}", new Object[]{listener2, e3.getMessage()});
                    }
                }
            }
            return false;
        }
        if (onExitRunnable != null) {
            if (!this.synchronous) {
                synchronized (this) {
                    this.currentThread = null;
                }
                this.lock.unlock();
                if (!$assertionsDisabled && this.lock.isHeldByCurrentThread()) {
                    throw new AssertionError();
                }
                isRequireUnlock.value = Boolean.FALSE;
                try {
                    onExitRunnable.run();
                    return true;
                } catch (Throwable t2) {
                    throw new OnExitRunnableException(t2);
                }
            }
            if (isTraceEnabled()) {
                LOGGER.fine("onExitRunnable used with synchronous Fiber execution -- not exiting current thread");
            }
            onExitRunnable.run();
            return false;
        }
        return false;
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/Fiber$OnExitRunnableException.class */
    private static final class OnExitRunnableException extends RuntimeException {
        private static final long serialVersionUID = 1;
        Throwable target;

        public OnExitRunnableException(Throwable target) {
            super((Throwable) null);
            this.target = target;
        }
    }

    public synchronized void addInterceptor(@NotNull FiberContextSwitchInterceptor interceptor) {
        if (this.interceptors == null) {
            this.interceptors = new ArrayList();
        } else {
            List<FiberContextSwitchInterceptor> l2 = new ArrayList<>();
            l2.addAll(this.interceptors);
            this.interceptors = l2;
        }
        this.interceptors.add(interceptor);
    }

    public synchronized boolean removeInterceptor(@NotNull FiberContextSwitchInterceptor interceptor) {
        if (this.interceptors != null) {
            boolean result = this.interceptors.remove(interceptor);
            if (this.interceptors.isEmpty()) {
                this.interceptors = null;
            } else {
                List<FiberContextSwitchInterceptor> l2 = new ArrayList<>();
                l2.addAll(this.interceptors);
                this.interceptors = l2;
            }
            return result;
        }
        return false;
    }

    @Nullable
    public ClassLoader getContextClassLoader() {
        return this.contextClassLoader;
    }

    public ClassLoader setContextClassLoader(@Nullable ClassLoader contextClassLoader) {
        ClassLoader r2 = this.contextClassLoader;
        this.contextClassLoader = contextClassLoader;
        return r2;
    }

    @Override // java.lang.Runnable
    @Deprecated
    public void run() {
        Container old = ContainerResolver.getDefault().enterContainer(this.owner.getContainer());
        try {
            if (!$assertionsDisabled && this.synchronous) {
                throw new AssertionError();
            }
            if (!doRun()) {
                if (this.startedSync && this.suspendedCount == 0 && (this.next != null || this.contsSize > 0)) {
                    this.startedSync = false;
                    dumpFiberContext("restarting (async) after startSync");
                    this.owner.addRunnable(this);
                } else {
                    completionCheck();
                }
            }
            ContainerResolver.getDefault().exitContainer(old);
        } catch (Throwable th) {
            ContainerResolver.getDefault().exitContainer(old);
            throw th;
        }
    }

    @NotNull
    public Packet runSync(@NotNull Tube tubeline, @NotNull Packet request) {
        this.lock.lock();
        try {
            Tube[] oldCont = this.conts;
            int oldContSize = this.contsSize;
            boolean oldSynchronous = this.synchronous;
            Tube oldNext = this.next;
            if (oldContSize > 0) {
                this.conts = new Tube[16];
                this.contsSize = 0;
            }
            try {
                this.synchronous = true;
                this.packet = request;
                this.next = tubeline;
                doRun();
                if (this.throwable != null) {
                    if (this.isDeliverThrowableInPacket) {
                        this.packet.addSatellite(new ThrowableContainerPropertySet(this.throwable));
                    } else {
                        if (this.throwable instanceof RuntimeException) {
                            throw ((RuntimeException) this.throwable);
                        }
                        if (this.throwable instanceof Error) {
                            throw ((Error) this.throwable);
                        }
                        throw new AssertionError(this.throwable);
                    }
                }
                Packet packet = this.packet;
                this.conts = oldCont;
                this.contsSize = oldContSize;
                this.synchronous = oldSynchronous;
                this.next = oldNext;
                if (this.interrupted) {
                    Thread.currentThread().interrupt();
                    this.interrupted = false;
                }
                if (!this.started && !this.startedSync) {
                    completionCheck();
                }
                return packet;
            } catch (Throwable th) {
                this.conts = oldCont;
                this.contsSize = oldContSize;
                this.synchronous = oldSynchronous;
                this.next = oldNext;
                if (this.interrupted) {
                    Thread.currentThread().interrupt();
                    this.interrupted = false;
                }
                if (!this.started && !this.startedSync) {
                    completionCheck();
                }
                throw th;
            }
        } finally {
            this.lock.unlock();
        }
    }

    private void completionCheck() {
        this.lock.lock();
        try {
            if (!this.isCanceled && this.contsSize == 0 && this.suspendedCount == 0) {
                if (isTraceEnabled()) {
                    LOGGER.log(Level.FINE, "{0} completed", getName());
                }
                clearListeners();
                this.condition.signalAll();
                if (this.completionCallback != null) {
                    if (this.throwable != null) {
                        if (this.isDeliverThrowableInPacket) {
                            this.packet.addSatellite(new ThrowableContainerPropertySet(this.throwable));
                            this.completionCallback.onCompletion(this.packet);
                        } else {
                            this.completionCallback.onCompletion(this.throwable);
                        }
                    } else {
                        this.completionCallback.onCompletion(this.packet);
                    }
                }
            }
        } finally {
            this.lock.unlock();
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/Fiber$InterceptorHandler.class */
    private class InterceptorHandler implements FiberContextSwitchInterceptor.Work<Tube, Tube> {
        private final Holder<Boolean> isUnlockRequired;
        private final List<FiberContextSwitchInterceptor> ints;
        private int idx;

        public InterceptorHandler(Holder<Boolean> isUnlockRequired, List<FiberContextSwitchInterceptor> ints) {
            this.isUnlockRequired = isUnlockRequired;
            this.ints = ints;
        }

        Tube invoke(Tube next) {
            this.idx = 0;
            return execute(next);
        }

        @Override // com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor.Work
        public Tube execute(Tube next) {
            if (this.idx == this.ints.size()) {
                Fiber.this.next = next;
                return Fiber.this.__doRun(this.isUnlockRequired, this.ints) ? Fiber.PLACEHOLDER : Fiber.this.next;
            }
            List<FiberContextSwitchInterceptor> list = this.ints;
            int i2 = this.idx;
            this.idx = i2 + 1;
            FiberContextSwitchInterceptor interceptor = list.get(i2);
            return (Tube) interceptor.execute(Fiber.this, next, this);
        }
    }

    /* loaded from: rt.jar:com/sun/xml/internal/ws/api/pipe/Fiber$PlaceholderTube.class */
    private static class PlaceholderTube extends AbstractTubeImpl {
        private PlaceholderTube() {
        }

        @Override // com.sun.xml.internal.ws.api.pipe.Tube
        public NextAction processRequest(Packet request) {
            throw new UnsupportedOperationException();
        }

        @Override // com.sun.xml.internal.ws.api.pipe.Tube
        public NextAction processResponse(Packet response) {
            throw new UnsupportedOperationException();
        }

        @Override // com.sun.xml.internal.ws.api.pipe.Tube
        public NextAction processException(Throwable t2) {
            return doThrow(t2);
        }

        @Override // com.sun.xml.internal.ws.api.pipe.Tube, com.sun.xml.internal.ws.api.pipe.Pipe
        public void preDestroy() {
        }

        @Override // com.sun.xml.internal.ws.api.pipe.helper.AbstractTubeImpl, com.sun.xml.internal.ws.api.pipe.Tube
        public PlaceholderTube copy(TubeCloner cloner) {
            throw new UnsupportedOperationException();
        }
    }

    private boolean doRun() {
        dumpFiberContext("running");
        if (serializeExecution) {
            serializedExecutionLock.lock();
            try {
                boolean z_doRun = _doRun(this.next);
                serializedExecutionLock.unlock();
                return z_doRun;
            } catch (Throwable th) {
                serializedExecutionLock.unlock();
                throw th;
            }
        }
        return _doRun(this.next);
    }

    /* JADX WARN: Finally extract failed */
    private boolean _doRun(Tube next) {
        List<FiberContextSwitchInterceptor> ints;
        ClassLoader old;
        boolean needsToReenter;
        Holder<Boolean> isRequireUnlock = new Holder<>(Boolean.TRUE);
        this.lock.lock();
        try {
            synchronized (this) {
                ints = this.interceptors;
                this.currentThread = Thread.currentThread();
                if (isTraceEnabled()) {
                    LOGGER.log(Level.FINE, "Thread entering _doRun(): {0}", this.currentThread);
                }
                old = this.currentThread.getContextClassLoader();
                this.currentThread.setContextClassLoader(this.contextClassLoader);
            }
            do {
                if (ints == null) {
                    try {
                        try {
                            this.next = next;
                            if (__doRun(isRequireUnlock, null)) {
                                Thread thread = Thread.currentThread();
                                thread.setContextClassLoader(old);
                                if (isTraceEnabled()) {
                                    LOGGER.log(Level.FINE, "Thread leaving _doRun(): {0}", thread);
                                }
                                if (isRequireUnlock.value.booleanValue()) {
                                    synchronized (this) {
                                        this.currentThread = null;
                                    }
                                    this.lock.unlock();
                                }
                                return true;
                            }
                        } catch (OnExitRunnableException o2) {
                            Throwable t2 = o2.target;
                            if (t2 instanceof WebServiceException) {
                                throw ((WebServiceException) t2);
                            }
                            throw new WebServiceException(t2);
                        }
                    } catch (Throwable th) {
                        Thread thread2 = Thread.currentThread();
                        thread2.setContextClassLoader(old);
                        if (isTraceEnabled()) {
                            LOGGER.log(Level.FINE, "Thread leaving _doRun(): {0}", thread2);
                        }
                        throw th;
                    }
                } else {
                    next = new InterceptorHandler(isRequireUnlock, ints).invoke(next);
                    if (next == PLACEHOLDER) {
                        Thread thread3 = Thread.currentThread();
                        thread3.setContextClassLoader(old);
                        if (isTraceEnabled()) {
                            LOGGER.log(Level.FINE, "Thread leaving _doRun(): {0}", thread3);
                        }
                        if (isRequireUnlock.value.booleanValue()) {
                            synchronized (this) {
                                this.currentThread = null;
                            }
                            this.lock.unlock();
                        }
                        return true;
                    }
                }
                synchronized (this) {
                    needsToReenter = ints != this.interceptors;
                    if (needsToReenter) {
                        ints = this.interceptors;
                    }
                }
            } while (needsToReenter);
            Thread thread4 = Thread.currentThread();
            thread4.setContextClassLoader(old);
            if (isTraceEnabled()) {
                LOGGER.log(Level.FINE, "Thread leaving _doRun(): {0}", thread4);
            }
            if (isRequireUnlock.value.booleanValue()) {
                synchronized (this) {
                    this.currentThread = null;
                }
                this.lock.unlock();
            }
            return false;
        } catch (Throwable th2) {
            if (isRequireUnlock.value.booleanValue()) {
                synchronized (this) {
                    this.currentThread = null;
                    this.lock.unlock();
                }
            }
            throw th2;
        }
    }

    /* JADX INFO: Access modifiers changed from: private */
    /* JADX WARN: Can't fix incorrect switch cases order, some code will duplicate */
    /* JADX WARN: Code restructure failed: missing block: B:101:0x033b, code lost:
    
        com.sun.xml.internal.ws.api.pipe.Fiber.CURRENT_FIBER.set(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:106:0x034e, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:21:0x006b, code lost:
    
        r9.contsSize = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:22:0x0076, code lost:
    
        com.sun.xml.internal.ws.api.pipe.Fiber.CURRENT_FIBER.set(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:23:0x007c, code lost:
    
        return false;
     */
    /* JADX WARN: Code restructure failed: missing block: B:42:0x0132, code lost:
    
        r9.contsSize = 0;
     */
    /* JADX WARN: Code restructure failed: missing block: B:43:0x013d, code lost:
    
        com.sun.xml.internal.ws.api.pipe.Fiber.CURRENT_FIBER.set(r0);
     */
    /* JADX WARN: Code restructure failed: missing block: B:44:0x0143, code lost:
    
        return false;
     */
    /*
        Code decompiled incorrectly, please refer to instructions dump.
        To view partially-correct code enable 'Show inconsistent code' option in preferences
    */
    public boolean __doRun(javax.xml.ws.Holder<java.lang.Boolean> r10, java.util.List<com.sun.xml.internal.ws.api.pipe.FiberContextSwitchInterceptor> r11) {
        /*
            Method dump skipped, instructions count: 848
            To view this dump change 'Code comments level' option to 'DEBUG'
        */
        throw new UnsupportedOperationException("Method not decompiled: com.sun.xml.internal.ws.api.pipe.Fiber.__doRun(javax.xml.ws.Holder, java.util.List):boolean");
    }

    private void pushCont(Tube tube) {
        Tube[] tubeArr = this.conts;
        int i2 = this.contsSize;
        this.contsSize = i2 + 1;
        tubeArr[i2] = tube;
        int len = this.conts.length;
        if (this.contsSize == len) {
            Tube[] newBuf = new Tube[len * 2];
            System.arraycopy(this.conts, 0, newBuf, 0, len);
            this.conts = newBuf;
        }
    }

    private Tube popCont() {
        Tube[] tubeArr = this.conts;
        int i2 = this.contsSize - 1;
        this.contsSize = i2;
        return tubeArr[i2];
    }

    private Tube peekCont() {
        int index = this.contsSize - 1;
        if (index >= 0 && index < this.conts.length) {
            return this.conts[index];
        }
        return null;
    }

    public void resetCont(Tube[] conts, int contsSize) {
        this.conts = conts;
        this.contsSize = contsSize;
    }

    private boolean isReady(List<FiberContextSwitchInterceptor> originalInterceptors) {
        boolean z2;
        boolean z3;
        if (this.synchronous) {
            while (this.suspendedCount == 1) {
                try {
                    if (isTraceEnabled()) {
                        LOGGER.log(Level.FINE, "{0} is blocking thread {1}", new Object[]{getName(), Thread.currentThread().getName()});
                    }
                    this.condition.await();
                } catch (InterruptedException e2) {
                    this.interrupted = true;
                }
            }
            synchronized (this) {
                z3 = this.interceptors == originalInterceptors;
            }
            return z3;
        }
        if (this.suspendedCount > 0) {
            return false;
        }
        synchronized (this) {
            z2 = this.interceptors == originalInterceptors;
        }
        return z2;
    }

    private String getName() {
        return "engine-" + this.owner.id + "fiber-" + this.id;
    }

    public String toString() {
        return getName();
    }

    @Nullable
    public Packet getPacket() {
        return this.packet;
    }

    public CompletionCallback getCompletionCallback() {
        return this.completionCallback;
    }

    public void setCompletionCallback(CompletionCallback completionCallback) {
        this.completionCallback = completionCallback;
    }

    public static boolean isSynchronous() {
        return current().synchronous;
    }

    public boolean isStartedSync() {
        return this.startedSync;
    }

    @NotNull
    public static Fiber current() {
        Fiber fiber = CURRENT_FIBER.get();
        if (fiber == null) {
            throw new IllegalStateException("Can be only used from fibers");
        }
        return fiber;
    }

    public static Fiber getCurrentIfSet() {
        return CURRENT_FIBER.get();
    }

    private static boolean isTraceEnabled() {
        return LOGGER.isLoggable(Level.FINE);
    }

    @Override // com.sun.xml.internal.ws.api.Component
    public <S> S getSPI(Class<S> cls) {
        Iterator<Component> it = this.components.iterator();
        while (it.hasNext()) {
            S s2 = (S) it.next().getSPI(cls);
            if (s2 != null) {
                return s2;
            }
        }
        return null;
    }

    @Override // com.sun.xml.internal.ws.api.ComponentRegistry
    public Set<Component> getComponents() {
        return this.components;
    }
}
